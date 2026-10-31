package pw.hachimi.client.module.movement;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CobwebBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.GetSlipperinessEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.Tick_PreEvent;
import pw.hachimi.client.event.movement.GetVelocityMultiplierEvent;
import pw.hachimi.client.event.movement.OnSteppedOnEvent;
import pw.hachimi.client.event.movement.SlowMovementEvent;
import pw.hachimi.client.event.movement.TickMovementPostEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.event.network.SendMovementPacketsTailEvent;
import pw.hachimi.client.event.player.SetCurrentHandEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.mixin.KeyBindingMixin2;
import pw.hachimi.client.model.movement.NoSlowDisabledVanillaMode;
import pw.hachimi.client.model.movement.NoSlowDisabledVanillaMode2;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.player.BlockPosBoxMultitask;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class NoSlow extends ClientModule {
   public static NoSlow INSTANCE;
   Setting airStrict = this.registerSetting(new BooleanSetting("AirStrict", "Strict NCP bypass for air slowdowns", false));
   Setting inventoryMove = this.registerSetting(new BooleanSetting("InventoryMove", "Allows the player to move while in inventories or screens", true));
   Setting arrowMove = this.registerSetting(new BooleanSetting("ArrowMove", "Allows the player to look while in inventories or screens by using the arrow keys", false));
   Setting items = this.registerSetting(new EnumSetting("Items", "Removes the slowdown effect caused by using items", NoSlowDisabledVanillaMode2.Vanilla, NoSlowDisabledVanillaMode2.values()));
   Setting shields = this.registerSetting(new BooleanSetting("Shields", "Removes the slowdown effect caused by shields", true, this::isShieldsVisible));
   Setting sneak = this.registerSetting(new BooleanSetting("Sneak", "Removes sneak slowdown", false));
   Setting crawl = this.registerSetting(new BooleanSetting("Crawl", "Removes crawl slowdown", false));
   Setting webs = this.registerSetting(new EnumSetting("Webs", "Removes the slowdown caused when moving through webs", NoSlowDisabledVanillaMode.Disabled, NoSlowDisabledVanillaMode.values()));
   Setting berryBush = this.registerSetting(new BooleanSetting("BerryBush", "Removes the slowdown caused when moving through webs", false));
   Setting webMultiplier = this.registerSetting(new NumberSetting("WebMultiplier", "Speed to fall through webs", 0.0F, 1.0F, 1.0F, this::isWebMultiplierVisible));
   Setting soulSand = this.registerSetting(new BooleanSetting("SoulSand", "Removes the slowdown effect caused by walking over SoulSand blocks", false));
   Setting honeyBlock = this.registerSetting(new BooleanSetting("HoneyBlock", "Removes the slowdown effect caused by walking over Honey blocks", false));
   Setting slimeBlock = this.registerSetting(new BooleanSetting("SlimeBlock", "Removes the slowdown effect caused by walking over Slime blocks", false));
   private boolean n;
   private int o;
   public static boolean cx;

   public NoSlow() {
      super("NoSlow", "Prevents items from slowing down player", ModuleCategory.MOVEMENT);
      INSTANCE = this;
   }

   public static NoSlow Vi() {
      return INSTANCE;
   }

   @Override
   public void awe() {
      if ((Boolean)this.airStrict.atx() && this.n) {
         Managers.serverManager.iO('\uf560', new ClientCommandC2SPacket(client.player, Mode.RELEASE_SHIFT_KEY));
      }

      this.n = false;
      Managers.entityManager.rT(1.0F);
   }

   @EventListener
   public void onSetCurrentHand(SetCurrentHandEvent setCurrentHandEvent) {
      if ((Boolean)this.airStrict.atx() && !this.n && this.Vx()) {
         this.n = true;
         Managers.serverManager.iO('\uf560', new ClientCommandC2SPacket(client.player, Mode.PRESS_SHIFT_KEY));
      }
   }

   @EventListener
   public void onSendMovementPacketsTail(SendMovementPacketsTailEvent sendMovementPacketsTailEvent) {
      if (sendMovementPacketsTailEvent.getEventPhase() == EventPhase.PRE && this.items.atx() == NoSlowDisabledVanillaMode2.GrimLegacy && client.player.isUsingItem()) {
         if (client.player.getActiveHand() == Hand.OFF_HAND && this.Vl(Managers.inventoryTracker.JW())) {
            Managers.serverManager.iQ(NoSlow::VC);
         } else if (this.Vl(client.player.getOffHandStack())) {
            Managers.serverManager.iQ(NoSlow::VB);
         }
      }
   }

   private boolean Vl(ItemStack stack) {
      ComponentMap temp = stack.getComponents();
      ComponentType componentType = DataComponentTypes.FOOD;
      boolean flag;
      if (!temp.contains(componentType) && stack.getItem() != Items.BOW && stack.getItem() != Items.CROSSBOW && stack.getItem() != Items.SHIELD) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   @EventListener
   private void Vm(Render3DContext render3DContext) {
      if ((Boolean)this.arrowMove.atx() && this.VA()) {
         long l = client.getWindow().getHandle();
         float f = client.player.getYaw();
         float f2 = client.player.getPitch();
         float f3 = (float)render3DContext.e / 10.0F;
         if (InputUtil.isKeyPressed(l, 265)) {
            f2 -= f3;
         }

         if (InputUtil.isKeyPressed(l, 264)) {
            f2 += f3;
         }

         if (InputUtil.isKeyPressed(l, 263)) {
            f -= f3;
         }

         if (InputUtil.isKeyPressed(l, 262)) {
            f += f3;
         }

         client.player.setYaw(f);
         ClientPlayerEntity temp = client.player;
         float f4 = MathHelper.clamp(f2, -90.0F, 90.0F);
         temp.setPitch(f4);
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if ((Boolean)this.airStrict.atx() && !client.player.isUsingItem()) {
            this.n = false;
            Managers.serverManager.iO('\uf560', new ClientCommandC2SPacket(client.player, Mode.RELEASE_SHIFT_KEY));
         }

         if ((Boolean)this.inventoryMove.atx() && this.VA()) {
            long l = client.getWindow().getHandle();
            KeyBinding[] keyBindings = new KeyBinding[]{
               client.options.jumpKey, client.options.forwardKey, client.options.backKey, client.options.rightKey, client.options.leftKey
            };
            int n2 = keyBindings.length;

            for (int n3 = 0; n3 < n2; n3++) {
               KeyBinding keyBinding = keyBindings[n3];
               boolean flag = InputUtil.isKeyPressed(l, ((KeyBindingMixin2)keyBinding).getBoundKey().getCode());
               keyBinding.setPressed(flag);
            }
         }
      }
   }

   @EventListener(priority = 100)
   public void onTick_Pre(Tick_PreEvent tickPreEvent) {
      if (client.player != null && (Boolean)this.inventoryMove.atx() && this.VA() && tickPreEvent.getEventPhase() == EventPhase.PRE) {
         long l = client.getWindow().getHandle();
         KeyBinding[] keyBindings = new KeyBinding[]{
            client.options.jumpKey, client.options.forwardKey, client.options.backKey, client.options.rightKey, client.options.leftKey
         };
         int n2 = keyBindings.length;

         for (int n3 = 0; n3 < n2; n3++) {
            KeyBinding keyBinding = keyBindings[n3];
            boolean flag = InputUtil.isKeyPressed(l, ((KeyBindingMixin2)keyBinding).getBoundKey().getCode());
            keyBinding.setPressed(flag);
         }
      }
   }

   @EventListener
   public void onSendMovementPacketsTail2(SendMovementPacketsTailEvent sendMovementPacketsTailEvent) {
      if (sendMovementPacketsTailEvent.getEventPhase() == EventPhase.PRE && this.webs.atx() != NoSlowDisabledVanillaMode.Vanilla && this.Vw()) {
         Iterator iterator2 = WorldUtils.Nh(client.player.getBoundingBox()).iterator();

         while (iterator2.hasNext()) {
            BlockPos pos = (BlockPos)iterator2.next();
            Managers.serverManager.iO('\uf560', new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, pos, Direction.DOWN));
         }
      }
   }

   @EventListener
   public void onSlowMovement(SlowMovementEvent slowMovementEvent) {
      Block block2 = slowMovementEvent.getState().getBlock();
      if (block2 instanceof CobwebBlock && this.Vw() || block2 instanceof SweetBerryBushBlock && (Boolean)this.berryBush.atx()) {
         float f = (Float)this.webMultiplier.atx();
         if ((Float)this.webMultiplier.atx() == 1.0F) {
            f = 0.0F;
         }

         slowMovementEvent.aim();
         slowMovementEvent.WW(f);
      }
   }

   @EventListener
   public void onTickMovementPost(TickMovementPostEvent tickMovementPostEvent) {
      if ((Boolean)this.sneak.atx() && client.player.isSneaking() || (Boolean)this.crawl.atx() && client.player.isCrawling()) {
         RegistryEntry registryEntry = EntityAttributes.PLAYER_SNEAKING_SPEED;
         float f = 1.0F / (float)client.player.getAttributeValue(registryEntry);
         float f2 = tickMovementPostEvent.input.movementForward * f;
         tickMovementPostEvent.input.movementForward = f2;
         float f3 = tickMovementPostEvent.input.movementSideways * f;
         tickMovementPostEvent.input.movementSideways = f3;
      }

      if (this.Vx()) {
         float f4 = tickMovementPostEvent.input.movementForward * 5.0F;
         tickMovementPostEvent.input.movementForward = f4;
         float f5 = tickMovementPostEvent.input.movementSideways * 5.0F;
         tickMovementPostEvent.input.movementSideways = f5;
      }
   }

   @EventListener
   public void onGetVelocityMultiplier(GetVelocityMultiplierEvent getVelocityMultiplierEvent) {
      if (getVelocityMultiplierEvent.aqh() == Blocks.SOUL_SAND && (Boolean)this.soulSand.atx() || getVelocityMultiplierEvent.aqh() == Blocks.HONEY_BLOCK && (Boolean)this.honeyBlock.atx()) {
         getVelocityMultiplierEvent.aim();
      }
   }

   @EventListener
   public void onOnSteppedOn(OnSteppedOnEvent onSteppedOnEvent) {
      if ((Boolean)this.slimeBlock.atx()) {
         onSteppedOnEvent.aim();
      }
   }

   @EventListener
   public void onGetSlipperiness(GetSlipperinessEvent getSlipperinessEvent) {
      if (getSlipperinessEvent.getBlock() == Blocks.SLIME_BLOCK && (Boolean)this.slimeBlock.atx()) {
         getSlipperinessEvent.aim();
         getSlipperinessEvent.lV();
      }
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (client.player != null && client.world != null && !client.isInSingleplayer()) {
         Packet packet2 = sendImmediatelyEvent.getPacket();
         if (packet2 instanceof PlayerMoveC2SPacket
            && ((PlayerMoveC2SPacket)packet2).changesPosition()
            && this.items.atx() == NoSlowDisabledVanillaMode2.NCP
            && this.Vx()) {
            Managers.inventoryTracker.JH(client.player.getInventory().selectedSlot);
         } else if (sendImmediatelyEvent.getPacket() instanceof ClickSlotC2SPacket && this.items.atx() == NoSlowDisabledVanillaMode2.NCP) {
            if (client.player.isUsingItem()) {
               client.player.stopUsingItem();
            }

            if (this.n || Managers.vec3dPlayerHelper.BV()) {
               Managers.serverManager.iO('\uf560', new ClientCommandC2SPacket(client.player, Mode.RELEASE_SHIFT_KEY));
            }

            if (Managers.vec3dPlayerHelper.BW()) {
               Managers.serverManager.iO('\uf560', new ClientCommandC2SPacket(client.player, Mode.STOP_SPRINTING));
            }
         }
      }
   }

   @Override
   public String hy() {
      return this.items.atx() != NoSlowDisabledVanillaMode2.Disabled ? ((NoSlowDisabledVanillaMode2)this.items.atx()).name() : super.hy();
   }

   public boolean Vw() {
      if (this.webs.atx() != NoSlowDisabledVanillaMode.Disabled) {
         if (this.webs.atx() != NoSlowDisabledVanillaMode.GrimMineable) {
            return true;
         }

         if (BlockPosBoxMultitask.alv().alG() != null) {
            ClientWorld temp = client.world;
            BlockPos pos = BlockPosBoxMultitask.alv().alG();
            if (!temp.isAir(pos)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean Vx() {
      if (this.items.atx() == NoSlowDisabledVanillaMode2.Disabled) {
         return false;
      }

      if (this.items.atx() != NoSlowDisabledVanillaMode2.Grim50 || client.player.getItemUseTime() > 1 && client.player.getItemUseTime() % 2 != 0) {
         if (this.items.atx() == NoSlowDisabledVanillaMode2.Grim95) {
            if (client.player.isFallFlying()) {
               return false;
            }

            if (client.player.getItemUseTime() < 2) {
               return false;
            }

            if (client.player.playerScreenHandler.syncId != client.player.currentScreenHandler.syncId) {
               return false;
            }

            if (this.Vz() == -1) {
               return false;
            }

            if (client.player.getActiveHand() == Hand.OFF_HAND && (client.player.getItemUseTime() <= 1 || client.player.getItemUseTime() % 2 == 0)) {
               return false;
            }
         }

         return !client.player.isRiding() && (client.player.isUsingItem() || client.player.isBlocking() && (Boolean)this.shields.atx());
      } else {
         return false;
      }
   }

   @EventListener
   public void onSendMovementPacketsTail3(SendMovementPacketsTailEvent sendMovementPacketsTailEvent) {
      if (this.items.atx() == NoSlowDisabledVanillaMode2.Grim95
         && this.Vx()
         && client.player.getActiveHand() != Hand.OFF_HAND
         && (client.player.getItemUseTime() <= 1 || client.player.getItemUseTime() % 2 == 0)
         && client.player.getItemUseTime() >= 2) {
         if (sendMovementPacketsTailEvent.getEventPhase() != EventPhase.PRE) {
            if (sendMovementPacketsTailEvent.getEventPhase() == EventPhase.POST && this.o != -1) {
               ClientPlayerInteractionManager temp = client.interactionManager;
               int temp2 = client.player.playerScreenHandler.syncId;
               int temp3 = this.o;
               int temp4 = Managers.inventoryTracker.JS();
               ClientPlayerEntity player2 = client.player;
               SlotActionType slotActionType = SlotActionType.SWAP;
               int n2 = temp4;
               int n3 = temp3;
               int n4 = temp2;
               temp.clickSlot(n4, n3, n2, slotActionType, (PlayerEntity)player2);
            }
         } else {
            this.o = this.Vz();
            if (this.o != -1) {
               ClientPlayerInteractionManager interaction = client.interactionManager;
               int n5 = client.player.playerScreenHandler.syncId;
               int n6 = this.o;
               int n7 = Managers.inventoryTracker.JS();
               ClientPlayerEntity player3 = client.player;
               SlotActionType slotActionType2 = SlotActionType.SWAP;
               int n8 = n7;
               int n9 = n6;
               int n10 = n5;
               interaction.clickSlot(n10, n9, n8, slotActionType2, (PlayerEntity)player3);
            }
         }
      }
   }

   private int Vz() {
      Iterator iterator2 = client.player.playerScreenHandler.slots.iterator();

      while (iterator2.hasNext()) {
         Slot slot2 = (Slot)iterator2.next();
         if ((slot2.id > 0 && slot2.id < 5 || slot2.id > 8) && slot2.getStack().isEmpty()) {
            return slot2.id;
         }
      }

      return -1;
   }

   public boolean VA() {
      boolean flag;
      if (client.currentScreen != null
         && !(client.currentScreen instanceof ChatScreen)
         && !(client.currentScreen instanceof SignEditScreen)
         && !(client.currentScreen instanceof DeathScreen)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static Packet VB(int n2) {
      return new PlayerInteractItemC2SPacket(Hand.OFF_HAND, n2, client.player.getYaw(), client.player.getPitch());
   }

   private static Packet VC(int n2) {
      return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n2, client.player.getYaw(), client.player.getPitch());
   }

   private Boolean isWebMultiplierVisible() {
      boolean flag;
      if (this.webs.atx() == NoSlowDisabledVanillaMode.Disabled && !(Boolean)this.berryBush.atx()) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private Boolean isShieldsVisible() {
      boolean flag;
      if (this.items.atx() != NoSlowDisabledVanillaMode2.Disabled) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
