package pw.hachimi.client.module.combat;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.movement.MoveToWorldEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.OffhandDisableAlwaysMode;
import pw.hachimi.client.model.combat.OffhandItemOption;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.exploit.SpearFix;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.KeybindSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper17;
import pw.hachimi.client.utils.player.StackPlayerUtils2;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BlockStateUtils;
import pw.hachimi.client.utils.world.Vec3dEntityUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public final class Offhand extends ClientModule {
   private static Offhand INSTANCE;
   Setting item = this.registerSetting(new EnumSetting("Item", "The item to wield in your offhand", OffhandItemOption.TOTEM, OffhandItemOption.values()));
   Setting health = this.registerSetting(new NumberSetting("Health", "The health required to fall below before swapping to a totem", 0.0F, 14.0F, 36.0F));
   Setting swordGap = this.registerSetting(new BooleanSetting("SwordGap", "Equips a golden apple if holding down the item use button", true));
   Setting pickaxeGap = this.registerSetting(new BooleanSetting("PickaxeGap", "Equips a golden apple if holding down the item use button", false));
   Setting totemGap = this.registerSetting(new BooleanSetting("TotemGap", "Equips a golden apple if holding down the item use button", false));
   Setting rightclickGap = this.registerSetting(new BooleanSetting("RightclickGap", "Equips a golden apple if holding down the item use button", false));
   Setting crapple = this.registerSetting(new BooleanSetting("Crapple", "Uses a normal golden apple if Absorption is present", true));
   Setting lethal = this.registerSetting(new BooleanSetting("Lethal", "Calculates lethal damage sources", false));
   Setting fall = this.registerSetting(new BooleanSetting("Fall", "Calculates fall damage sources", false));
   Setting alternative = this.registerSetting(new BooleanSetting("Alternative", "Replaces totem using the swap packet", false));
   Setting screen = this.registerSetting(new BooleanSetting("Screen", "Replace the totem in a non-inventory screen.", true));
   Setting antiMiss = this.registerSetting(new EnumSetting("AntiMiss", "Force replace totem to prevents fail", OffhandDisableAlwaysMode.Disable, OffhandDisableAlwaysMode.values()));
   Setting debug = this.registerSetting(new BooleanSetting("Debug", "Debug on death", false));
   private boolean n;
   Setting chorus;
   private boolean p;
   Setting shield;
   private int r;
   private int s;
   private Item item2;
   private Item item3;
   private boolean v;
   private long w;
   private boolean x;
   private int y;

   public Offhand() {
      super("Offhand", "Automatically replenishes the item in your offhand", ModuleCategory.COMBAT);
      KeybindSetting keybindSetting = new KeybindSetting("Chorus", "Swaps chorus item when click", h(this.Kn() + "-chorusswap", this::isChorusVisible));
      this.chorus = this.registerSetting(keybindSetting);
      keybindSetting = new KeybindSetting("Shield", "Swaps shield item when click", h(this.Kn() + "-shieldswap", this::isShieldVisible));
      this.shield = this.registerSetting(keybindSetting);
      this.y = -1;
      INSTANCE = this;
      Managers.playerScreenManager.akq((UnidentifiedHelper17)this.chorus.atx(), (UnidentifiedHelper17)this.shield.atx());
   }

   public static Offhand abZ() {
      return INSTANCE;
   }

   private void isChorusVisible() {
      if (this.awg()) {
         boolean flag;
         if (!this.n) {
            flag = true;
         } else {
            flag = false;
         }

         this.n = flag;
      }
   }

   private void isShieldVisible() {
      if (this.awg()) {
         boolean flag;
         if (!this.p) {
            flag = true;
         } else {
            flag = false;
         }

         this.p = flag;
      }
   }

   @Override
   public String hy() {
      if (this.p) {
         return "Shield," + StackPlayerUtils2.abK(Items.TOTEM_OF_UNDYING);
      } else {
         return this.n ? "Chorus, " + StackPlayerUtils2.abK(Items.TOTEM_OF_UNDYING) : String.valueOf(StackPlayerUtils2.abK(Items.TOTEM_OF_UNDYING));
      }
   }

   @Override
   public void awe() {
      super.awe();
      this.r = -1;
      this.item2 = null;
      this.item3 = null;
   }

   @EventListener
   public void onMoveToWorld(MoveToWorldEvent moveToWorldEvent) {
      this.s = StackPlayerUtils2.abK(Items.TOTEM_OF_UNDYING);
   }

   @EventListener(priority = 2147483646)
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (client.player != null && tickPreEvent.getEventPhase() == EventPhase.PRE) {
         this.item3 = ((OffhandItemOption)this.item.atx()).Xh();
         if (!this.n) {
            if (!this.p) {
               if (!this.acg()) {
                  Item item4 = client.player.getMainHandStack().getItem();
                  if (client.options.useKey.isPressed()
                     && (
                        (Boolean)this.swordGap.atx()
                              && (item4 instanceof SwordItem || item4 instanceof TridentItem || item4 instanceof MaceItem || item4 instanceof AxeItem)
                              && !SpearFix.nt(client.player.getMainHandStack())
                           || (Boolean)this.pickaxeGap.atx() && item4 instanceof PickaxeItem
                           || (Boolean)this.totemGap.atx() && item4 == Items.TOTEM_OF_UNDYING
                           || (Boolean)this.rightclickGap.atx()
                     )
                     && WorldUtils.Nc() >= (Float)this.health.atx()) {
                     HitResult hitResult = client.crosshairTarget;
                     if (!(hitResult instanceof BlockHitResult)) {
                        this.item3 = this.acj();
                     } else {
                        ClientWorld temp = client.world;
                        BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
                        if (!BlockStateUtils.Ww(temp.getBlockState(pos))) {
                           this.item3 = this.acj();
                        }
                     }
                  }
               } else {
                  this.item3 = Items.TOTEM_OF_UNDYING;
               }
            } else {
               this.item3 = Items.SHIELD;
            }
         } else {
            ItemCooldownManager itemCooldownManager = client.player.getItemCooldownManager();
            Item item5 = Items.CHORUS_FRUIT;
            if (itemCooldownManager.isCoolingDown(item5)) {
               this.n = false;
               this.onTickPre(tickPreEvent);
               return;
            }

            this.item3 = Items.CHORUS_FRUIT;
         }

         if (this.antiMiss.atx() != OffhandDisableAlwaysMode.Disable && this.item3 == Items.TOTEM_OF_UNDYING) {
            if (this.x || this.antiMiss.atx() == OffhandDisableAlwaysMode.Always) {
               for (int n2 = 0; n2 < client.player.currentScreenHandler.slots.size(); n2++) {
                  this.y++;
                  if (this.y == 45 && client.player.currentScreenHandler.syncId == client.player.playerScreenHandler.syncId) {
                     this.y++;
                  }

                  if (this.y >= client.player.currentScreenHandler.slots.size()) {
                     this.y = 0;
                  }

                  int n3 = this.y;
                  if (((Slot)client.player.currentScreenHandler.slots.get(n3)).getStack().getItem() == Items.TOTEM_OF_UNDYING) {
                     ClientPlayerEntity player2 = client.player;
                     SlotActionType slotActionType = SlotActionType.SWAP;
                     int n4 = this.y;
                     client.interactionManager.clickSlot(0, n4, 40, slotActionType, (PlayerEntity)player2);
                     break;
                  }
               }
            }

            this.x = !this.x;
         }

         if (client.player.getOffHandStack().getItem() != this.item3) {
            if (client.player.currentScreenHandler.syncId == 0) {
               int n5 = 35;
               if (this.r != -1) {
                  if (this.item2 != null) {
                     PlayerInventory playerInventory = client.player.getInventory();
                     int n6 = this.r;
                     if (playerInventory.getStack(n6).getItem().equals(this.item3) && this.item2.equals(client.player.getOffHandStack().getItem())) {
                        int n7 = this.r;
                        this.r = -1;
                        this.item2 = null;
                        n5 = n7;
                     }
                  }

                  if (n5 < 0) {
                     return;
                  }
               }

               while (true) {
                  PlayerInventory playerInventory2 = client.player.getInventory();
                  int n8 = n5;
                  if (playerInventory2.getStack(n8).getItem() == this.item3) {
                     if (n5 < 9) {
                        this.item2 = this.item3;
                        this.r = n5;
                     }

                     int n9 = n5 >= 9 ? n5 : n5 + 36;
                     this.v = true;
                     if ((Boolean)this.alternative.atx()) {
                        ClientPlayerEntity player3 = client.player;
                        SlotActionType slotActionType2 = SlotActionType.SWAP;
                        int n10 = n9;
                        client.interactionManager.clickSlot(0, n10, 40, slotActionType2, (PlayerEntity)player3);
                        this.v = false;
                        break;
                     }

                     if (client.player.currentScreenHandler.getCursorStack().getItem() != this.item3) {
                        ClientPlayerEntity player4 = client.player;
                        SlotActionType slotActionType3 = SlotActionType.PICKUP;
                        int n11 = n9;
                        client.interactionManager.clickSlot(0, n11, 0, slotActionType3, (PlayerEntity)player4);
                     }

                     if (client.player.currentScreenHandler.getCursorStack().getItem() == this.item3) {
                        ClientPlayerEntity player5 = client.player;
                        SlotActionType slotActionType4 = SlotActionType.PICKUP;
                        client.interactionManager.clickSlot(0, 45, 0, slotActionType4, (PlayerEntity)player5);
                        this.s = StackPlayerUtils2.abK(Items.TOTEM_OF_UNDYING) + 1;
                     }

                     this.v = false;
                     if (!client.player.currentScreenHandler.getCursorStack().isEmpty() && client.player.getOffHandStack().getItem() == this.item3) {
                        ClientPlayerEntity player6 = client.player;
                        SlotActionType slotActionType5 = SlotActionType.PICKUP;
                        int n12 = n9;
                        client.interactionManager.clickSlot(0, n12, 0, slotActionType5, (PlayerEntity)player6);
                        break;
                     }
                  }

                  if (--n5 < 0) {
                     return;
                  }
               }

               return;
            }

            if ((Boolean)this.screen.atx()) {
               Iterator iterator2 = client.player.currentScreenHandler.slots.iterator();

               while (iterator2.hasNext()) {
                  Object obj = iterator2.next();
                  if (((Slot)obj).getStack().getItem() == this.item3) {
                     ClientPlayerEntity player7 = client.player;
                     SlotActionType slotActionType6 = SlotActionType.SWAP;
                     int n13 = ((Slot)obj).id;
                     int n14 = client.player.currentScreenHandler.syncId;
                     client.interactionManager.clickSlot(n14, n13, 40, slotActionType6, (PlayerEntity)player7);
                     break;
                  }
               }
            }
         }
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null && client.world != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof HealthUpdateS2CPacket && ((HealthUpdateS2CPacket)packet2).getHealth() <= 0.0F && (Boolean)this.debug.atx()) {
            if (this.s <= 0) {
               return;
            }

            Set set = this.acf();
            if (set.isEmpty()) {
               long l = System.currentTimeMillis() - this.w;
               this.hu("Failed to replace totem in %sms!", l);
            } else {
               this.hu("Failed to replace totem! Possible reasons: %s", String.join(", ", set));
            }
         }

         packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof ScreenHandlerSlotUpdateS2CPacket
            && ((ScreenHandlerSlotUpdateS2CPacket)packet2).getSlot() == 45
            && this.item3 == Items.TOTEM_OF_UNDYING) {
            if (client.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING || !((ScreenHandlerSlotUpdateS2CPacket)packet2).getStack().isEmpty()) {
               return;
            }

            this.w = System.currentTimeMillis();
         }
      }
   }

   private Set acf() {
      LinkedHashSet linkedHashSet = new LinkedHashSet();
      if (client.player.currentScreenHandler.syncId != 0) {
         linkedHashSet.add("Current screen handler is not the player inventory");
      }

      if (!client.player.currentScreenHandler.getCursorStack().isEmpty()) {
         linkedHashSet.add("Totem was not placed in offhand on time");
      }

      return linkedHashSet;
   }

   private boolean acg() {
      float f = WorldUtils.Nc();
      boolean flag;
      if (!(f <= (Float)this.health.atx())
         && (!(Boolean)this.lethal.atx() || !this.aci(f))
         && (!(WorldUtils.Nd(client.player.fallDistance, 1.0F) + 0.5F > client.player.getHealth()) || !(Boolean)this.fall.atx())) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private boolean ach() {
      Iterator iterator2 = client.world.getPlayers().iterator();

      while (iterator2.hasNext()) {
         PlayerEntity player2 = (PlayerEntity)iterator2.next();
         double d = player2.getX() - client.player.getX();
         double d2 = player2.getZ() - client.player.getZ();
         if (d * d + d2 * d2 <= 100.0 && player2.getY() > client.player.getY() && !player2.isOnGround()) {
            return true;
         }
      }

      return false;
   }

   private boolean aci(float f) {
      Iterator iterator2 = Lists.newArrayList(client.world.getEntities()).iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (entity2 != null && entity2.isAlive() && entity2 instanceof EndCrystalEntity && !(client.player.squaredDistanceTo(entity2) > 144.0)) {
            double d = Vec3dEntityUtils.vK(client.player, ((EndCrystalEntity)entity2).getPos(), false);
            if (!(f - d > ((Float)this.health.atx()).floatValue())) {
               return true;
            }
         }
      }

      return false;
   }

   private Item acj() {
      if ((Boolean)this.crapple.atx() && StackPlayerUtils2.abI(Items.GOLDEN_APPLE)) {
         RegistryEntry registryEntry = StatusEffects.ABSORPTION;
         if (client.player.hasStatusEffect(registryEntry) || !StackPlayerUtils2.abI(Items.ENCHANTED_GOLDEN_APPLE)) {
            return Items.GOLDEN_APPLE;
         }
      }

      return Items.ENCHANTED_GOLDEN_APPLE;
   }

   private static UnidentifiedHelper17 h(String str, Runnable runnable) {
      return new UnidentifiedHelper17(str, -1, runnable);
   }
}
