package pw.hachimi.client.module.movement;

import java.util.Iterator;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.Hand;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.event.RecastOnLandEvent;
import pw.hachimi.client.event.RunEvent;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.TickEvent4;
import pw.hachimi.client.event.Tick_PreEvent;
import pw.hachimi.client.event.movement.MoveEvent;
import pw.hachimi.client.event.movement.TravelHeadEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.mixin.FireworkRocketEntityMixin;
import pw.hachimi.client.model.DefaultDegreesMode;
import pw.hachimi.client.model.movement.ElytraFlyControlVanillaMode;
import pw.hachimi.client.model.movement.ElytraFlyStaticTimerMode;
import pw.hachimi.client.module.Module5;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.combat.AutoSpear;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.UnidentifiedHelper22;
import pw.hachimi.client.utils.movement.ElytraFlyUtils;
import pw.hachimi.client.utils.movement.UnidentifiedHelper21;
import pw.hachimi.client.utils.movement.UnidentifiedUtils;
import pw.hachimi.client.utils.world.DirectionUtils;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import pw.hachimi.client.module.movement.Follower;
import pw.hachimi.client.module.movement.Sprint;

public class ElytraFly extends Module5 {
   private static ElytraFly INSTANCE;
   private final UnidentifiedHelper22 unidentifiedHelper22 = new UnidentifiedHelper22();
   private final UnidentifiedHelper22 unidentifiedHelper222 = new UnidentifiedHelper22();
   private final StopwatchImpl stopwatchImpl = new StopwatchImpl();
   public boolean d;
   Setting mode = this.registerSetting(new EnumSetting("Mode", "The mode for elytra flight", ElytraFlyControlVanillaMode.CONTROL, ElytraFlyControlVanillaMode.values()));
   Setting speed = this.registerSetting(new NumberSetting("Speed", "The horizontal flight speed", 0.1F, 2.5F, 10.0F, this::isSpeedVisible));
   Setting noMove = this.registerSetting(new BooleanSetting("NoMove", "Prevent move check", false, this::isNoMoveVisible));
   Setting noAnimation = this.registerSetting(new BooleanSetting("NoAnimation", "No elytra fly animation", false));
   Setting sneakingFix = this.registerSetting(new BooleanSetting("SneakingFix", "Fix the sneaking bug", true));
   Setting swapAlternative = this.registerSetting(new BooleanSetting("SwapAlternative", "Uses inventory swap for swapping", false));
   Setting freeze = this.registerSetting(new BooleanSetting("Freeze", "Freeze when you not moving", false, ElytraFly::isFreezeVisible));
   Setting freezeMode = this.registerSetting(new EnumSetting("FreezeMode", "Freeze mode", ElytraFlyStaticTimerMode.Static, ElytraFlyStaticTimerMode.values(), this::isFreezeModeVisible));
   Setting noRotation = this.registerSetting(new BooleanSetting("NoRotation", "No rotation when you freeze", true, this::isNoRotationVisible));
   Setting packet = this.registerSetting(new BooleanSetting("Packet", "Uses packet to fly", false));
   Setting grimV3 = this.registerSetting(new BooleanSetting("GrimV3", "Bypass the packet elytra fly on 2b2t.org", false, this::isGrimV3Visible));
   Setting grimDelay = this.registerSetting(new NumberSetting("GrimDelay", "", 0.0F, 5.0F, 20.0F, this::isGrimDelayVisible));
   Setting autoJump = this.registerSetting(new BooleanSetting("AutoJump", "Auto jump when you on ground", false, ElytraFly::isAutoJumpVisible));
   Setting autoFly = this.registerSetting(new BooleanSetting("AutoFly", "Auto start flying", false, ElytraFly::isAutoFlyVisible));
   Setting fireworks = this.registerSetting(new BooleanSetting("Fireworks", "Uses fireworks when flying", false, ElytraFly::isFireworksVisible));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "Delay to use fireworks in ticks", 1.0F, 4.0F, 40.0F, DefaultDegreesMode.DEFAULT, this::isDelayVisible));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Rotates the player when moving", false, ElytraFly::isRotateVisible));
   Setting flyPitch = this.registerSetting(new NumberSetting("FlyPitch", "Pitch", -90.0F, 0.0F, 90.0F, this::isFlyPitchVisible));
   Setting rotateHold = this.registerSetting(new NumberSetting("RotateHold", "Rotates hold ticks", 0, 0, 20, this::isRotateHoldVisible));
   boolean x;
   private float y;
   private float z;
   private boolean A;
   private boolean B;

   public ElytraFly() {
      super("ElytraFly", "Allows you to fly freely using an elytra", ModuleCategory.MOVEMENT, 3000);
      INSTANCE = this;
   }

   public static ElytraFly di() {
      return INSTANCE;
   }

   @Override
   public String hy() {
      return DirectionUtils.apW((Enum)this.mode.atx());
   }

   @Override
   public void awd() {
      this.x = false;
   }

   @Override
   public void awe() {
      if (this.B) {
         Managers.entityManager.rT(1.0F);
      }

      if (this.d) {
         if ((Integer)this.rotateHold.atx() > 0) {
            ElytraFlyUtils.a_ = (Integer)this.rotateHold.atx();
            EventBusImpl.INSTANCE.rE(ElytraFlyUtils.class);
         }

         this.d = false;
      }

      if (client.player != null && (Boolean)this.sneakingFix.atx()) {
         ClientPlayNetworkHandler temp = client.player.networkHandler;
         ClientCommandC2SPacket clientCommandC2SPacket2 = new ClientCommandC2SPacket(client.player, Mode.RELEASE_SHIFT_KEY);
         temp.sendPacket((Packet)clientCommandC2SPacket2);
      }
   }

   @EventListener
   public void dj(RecastOnLandEvent recastOnLandEvent) {
      if (!Bootstrap.NV() || !UnidentifiedUtils.eP()) {
         if (this.d && this.du() && !this.dv()) {
            recastOnLandEvent.yi();
            recastOnLandEvent.aim();
         }
      }
   }

   @EventListener
   public void onTickEvent4(TickEvent4 tickEvent4) {
      if ((Boolean)this.noAnimation.atx()) {
         tickEvent4.aim();
      }
   }

   @EventListener(priority = -50)
   // restored with CFR (compiles, call/field/constant fingerprint equals the deobf jar)
   public void dl(TickEntitiesPreEvent tickEntitiesPreEvent) {
       block16: {
           int n;
           block15: {
               block18: {
                   block17: {
                       Item item;
                       this.d = false;
                       this.x = false;
                       if (ElytraFly.client.player.hasVehicle() || ElytraFly.client.player.isClimbing() || ElytraFly.client.player.isTouchingWater()) break block16;
                       EquipmentSlot equipmentSlot = EquipmentSlot.CHEST;
                       ItemStack itemStack = ElytraFly.client.player.getEquippedStack(equipmentSlot);
                       if (itemStack.isOf(item = Items.ELYTRA) && ElytraItem.isUsable((ItemStack)itemStack)) break block17;
                       if (!this.du()) break block16;
                       n = 0;
                       if (((Boolean)this.swapAlternative.atx()).booleanValue()) break block18;
                       break block18;
                   }
                   if (!ElytraFly.client.player.isOnGround()) {
                       if (((Boolean)this.autoFly.atx()).booleanValue() && !ElytraFly.client.player.isFallFlying() && ElytraFly.client.player.checkFallFlying()) {
                           ClientCommandC2SPacket clientCommandC2SPacket = new ClientCommandC2SPacket((Entity)ElytraFly.client.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING);
                           ElytraFly.client.player.networkHandler.sendPacket((Packet)clientCommandC2SPacket);
                       }
                       break block16;
                   } else if (((Boolean)this.autoJump.atx()).booleanValue()) {
                       ElytraFly.client.player.jump();
                   }
                   break block16;
               }
               do {
                   int n2 = n++;
                   if (ElytraFly.client.player.getInventory().getStack(n2).getItem() instanceof ElytraItem) break block15;
               } while (n < ((Boolean)this.swapAlternative.atx() == false ? 9 : 36));
               break block16;
           }
           int n3 = n;
           if (n3 != -1) {
               if (!ElytraFly.client.player.isOnGround()) {
                   if (((Boolean)this.rotate.atx()).booleanValue()) {
                       this.do_();
                   }
                   if (((Boolean)this.grimV3.atx()).booleanValue()) {
                       if (!this.stopwatchImpl.afq(Float.valueOf(((Float)this.grimDelay.atx()).floatValue() * 50.0f))) {
                           return;
                       }
                       this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                   }
                   if (n3 >= 9) {
                       ClientPlayerEntity clientPlayerEntity = ElytraFly.client.player;
                       SlotActionType slotActionType = SlotActionType.SWAP;
                       int n4 = n3;
                       int n5 = ElytraFly.client.player.currentScreenHandler.syncId;
                       ElytraFly.client.interactionManager.clickSlot(n5, n4, 8, slotActionType, (PlayerEntity)clientPlayerEntity);
                       ClientPlayerEntity clientPlayerEntity2 = ElytraFly.client.player;
                       SlotActionType slotActionType2 = SlotActionType.SWAP;
                       int n6 = ElytraFly.client.player.currentScreenHandler.syncId;
                       ElytraFly.client.interactionManager.clickSlot(n6, 6, 8, slotActionType2, (PlayerEntity)clientPlayerEntity2);
                   } else {
                       ClientPlayerEntity clientPlayerEntity = ElytraFly.client.player;
                       SlotActionType slotActionType = SlotActionType.SWAP;
                       int n7 = n3;
                       int n8 = ElytraFly.client.player.currentScreenHandler.syncId;
                       ElytraFly.client.interactionManager.clickSlot(n8, 6, n7, slotActionType, (PlayerEntity)clientPlayerEntity);
                   }
                   ClientCommandC2SPacket clientCommandC2SPacket = new ClientCommandC2SPacket((Entity)ElytraFly.client.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING);
                   client.getNetworkHandler().sendPacket((Packet)clientCommandC2SPacket);
                   if (((Boolean)this.grimV3.atx()).booleanValue()) {
                       ElytraFly.client.player.startFallFlying();
                   }
                   this.d = true;
                   this.dn();
                   if (n3 >= 9) {
                       ClientPlayerEntity clientPlayerEntity = ElytraFly.client.player;
                       SlotActionType slotActionType = SlotActionType.SWAP;
                       int n9 = ElytraFly.client.player.currentScreenHandler.syncId;
                       ElytraFly.client.interactionManager.clickSlot(n9, 6, 8, slotActionType, (PlayerEntity)clientPlayerEntity);
                       ClientPlayerEntity clientPlayerEntity3 = ElytraFly.client.player;
                       SlotActionType slotActionType3 = SlotActionType.SWAP;
                       int n10 = n3;
                       int n11 = ElytraFly.client.player.currentScreenHandler.syncId;
                       ElytraFly.client.interactionManager.clickSlot(n11, n10, 8, slotActionType3, (PlayerEntity)clientPlayerEntity3);
                       return;
                   }
                   ClientPlayerEntity clientPlayerEntity = ElytraFly.client.player;
                   SlotActionType slotActionType = SlotActionType.SWAP;
                   int n12 = n3;
                   int n13 = ElytraFly.client.player.currentScreenHandler.syncId;
                   ElytraFly.client.interactionManager.clickSlot(n13, 6, n12, slotActionType, (PlayerEntity)clientPlayerEntity);
                   return;
               }
               if (!((Boolean)this.autoJump.atx()).booleanValue()) {
                   return;
               }
               ElytraFly.client.player.jump();
               return;
           }
       }
       this.dn();
   }

   @EventListener(priority = -100)
   public void onTick_Pre(Tick_PreEvent tickPreEvent) {
      if (!Bootstrap.NV() || !UnidentifiedUtils.eP()) {
         if (this.mode.atx() == ElytraFlyControlVanillaMode.VANILLA && (Boolean)this.noMove.atx() && this.dt()) {
            client.player.input.movementSideways = 0.0F;
            client.player.input.movementForward = 0.0F;
         }
      }
   }

   private void dn() {
      if (!this.dt()) {
         this.z = client.player.getYaw();
         this.y = 0.0F;
         return;
      }

      if ((Boolean)this.rotate.atx()) {
         this.do_();
      }

      if ((Boolean)this.freeze.atx()
         && !client.options.forwardKey.isPressed()
         && !client.options.backKey.isPressed()
         && !client.options.leftKey.isPressed()
         && !client.options.rightKey.isPressed()) {
         boolean flag = client.options.jumpKey.isPressed();
         boolean flag2 = client.options.sneakKey.isPressed();
         if (flag == flag2 && !Follower.dM().dS() && !AutoSpear.hP()) {
            return;
         }
      }

      if ((Boolean)this.fireworks.atx() && !this.ds() && this.unidentifiedHelper222.afq((Number)this.delay.atx())) {
         int n = -1;
         int n2 = (Boolean)this.swapAlternative.atx() ? 36 : 9;

         for (int n3 = 0; n3 < n2; n3++) {
            if (client.player.getInventory().getStack(n3).getItem() instanceof FireworkRocketItem) {
               n = n3;
               break;
            }
         }

         if (n != -1) {
            if (n >= 9) {
               Managers.inventoryTracker.JK(n, Managers.inventoryTracker.JS());
               Managers.serverManager.iQ(ElytraFly::dw);
               Managers.inventoryTracker.JK(n, Managers.inventoryTracker.JS());
            } else {
               int n4 = Managers.inventoryTracker.JS();
               Managers.inventoryTracker.JF(n);
               Managers.serverManager.iQ(ElytraFly::dx);
               Managers.inventoryTracker.JF(n4);
            }

            this.unidentifiedHelper222.afr(29432, 5265492, (byte)40);
         }
      }
   }

   public void do_() {
      label115: {
         if (!client.options.forwardKey.isPressed()
            && !client.options.backKey.isPressed()
            && !client.options.leftKey.isPressed()
            && !client.options.rightKey.isPressed()) {
            boolean flag = client.options.jumpKey.isPressed();
            boolean flag2 = client.options.sneakKey.isPressed();
            if (this.mode.atx() == ElytraFlyControlVanillaMode.VANILLA) {
               if (flag && !flag2) {
                  this.y = -89.0F;
               } else if (flag2 && !flag) {
                  this.y = 89.0F;
               } else {
                  boolean flag3;
                  if ((Boolean)this.freeze.atx() && !Follower.dM().dS() && !AutoSpear.hP()) {
                     flag3 = true;
                  } else {
                     flag3 = false;
                  }

                  this.A = flag3;
               }
               break label115;
            }

            if ((!flag || flag2) && (!flag2 || flag)) {
               return;
            }
         } else {
            boolean flag4 = client.options.jumpKey.isPressed();
            boolean flag5 = client.options.sneakKey.isPressed();
            if (this.mode.atx() == ElytraFlyControlVanillaMode.VANILLA) {
               if (flag4 && !flag5) {
                  this.y = -45.0F;
               } else if (flag5 && !flag4) {
                  this.y = 45.0F;
               } else {
                  this.y = (Float)this.flyPitch.atx();
               }

               this.z = Sprint.mD().mL(client.player.getYaw());
               break label115;
            }
         }

         boolean flag6;
         if ((Boolean)this.freeze.atx() && !Follower.dM().dS() && !AutoSpear.hP()) {
            flag6 = true;
         } else {
            flag6 = false;
         }

         this.A = flag6;
         return;
      }

      if (this.A) {
         if ((Boolean)this.noRotation.atx()) {
            Managers.movementTracker.Mh(new UnidentifiedHelper21(50, this.z, this.y));
         }
      } else {
         this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
         this.ajM(this.z, this.y);
      }
   }

   @EventListener
   public void onRun(RunEvent runEvent) {
      if ((Boolean)this.freeze.atx() && this.freezeMode.atx() == ElytraFlyStaticTimerMode.Timer) {
         if (!client.options.forwardKey.isPressed()
            && !client.options.backKey.isPressed()
            && !client.options.leftKey.isPressed()
            && !client.options.rightKey.isPressed()) {
            boolean flag = client.options.jumpKey.isPressed();
            boolean flag2 = client.options.sneakKey.isPressed();
            if (!flag && !flag2 && !Follower.dM().dS()) {
               return;
            }
         } else {
            client.options.jumpKey.isPressed();
            client.options.sneakKey.isPressed();
         }

         if (this.B) {
            Managers.entityManager.rT(1.0F);
            this.B = false;
         }
      }
   }

   @EventListener
   public void onMove(MoveEvent moveEvent) {
      if (this.B) {
         Managers.entityManager.rT(1.0F);
         this.B = false;
      }

      if (!Bootstrap.NV() || !UnidentifiedUtils.eP()) {
         if (this.A) {
            this.A = false;
            if (this.freezeMode.atx() == ElytraFlyStaticTimerMode.Timer) {
               Managers.entityManager.rT(0.1F);
               this.B = true;
            }
         }
      }
   }

   @EventListener
   public void onTravelHead(TravelHeadEvent travelHeadEvent) {
      if (client.player != null && client.world != null) {
         if (!Bootstrap.NV() || !UnidentifiedUtils.eP()) {
            if (this.A) {
               if (this.freezeMode.atx() == ElytraFlyStaticTimerMode.Tick) {
                  this.A = false;
                  if (this.unidentifiedHelper22.afq(20)) {
                     this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                  } else {
                     travelHeadEvent.aim();
                  }

                  return;
               }

               if (this.freezeMode.atx() == ElytraFlyStaticTimerMode.Static) {
                  this.A = false;
                  travelHeadEvent.aim();
                  return;
               }
            }

            if (this.mode.atx() == ElytraFlyControlVanillaMode.CONTROL) {
            }
         }
      }
   }

   private boolean ds() {
      Iterator iterator2 = client.world.getEntities().iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (entity2 instanceof FireworkRocketEntity
            && ((FireworkRocketEntityMixin)entity2).hookWasShotByEntity()
            && ((FireworkRocketEntityMixin)entity2).hookGetShooter() == client.player) {
            return true;
         }
      }

      return false;
   }

   public boolean dt() {
      boolean flag;
      if (!this.d && !client.player.isFallFlying()) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public boolean du() {
      return (Boolean)this.packet.atx();
   }

   public boolean dv() {
      return (Boolean)this.grimV3.atx();
   }

   private static Packet dw(int n14) {
      return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n14, client.player.getYaw(), client.player.getPitch());
   }

   private static Packet dx(int n14) {
      return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n14, client.player.getYaw(), client.player.getPitch());
   }

   private Boolean isRotateHoldVisible() {
      return (Boolean)this.rotate.atx();
   }

   private Boolean isFlyPitchVisible() {
      return (Boolean)this.rotate.atx();
   }

   private static Boolean isRotateVisible() {
      return true;
   }

   private Boolean isDelayVisible() {
      return (Boolean)this.fireworks.atx();
   }

   private static Boolean isFireworksVisible() {
      return true;
   }

   private static Boolean isAutoFlyVisible() {
      return true;
   }

   private static Boolean isAutoJumpVisible() {
      return true;
   }

   private Boolean isGrimDelayVisible() {
      boolean flag;
      if (this.du() && (Boolean)this.grimV3.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isGrimV3Visible() {
      return this.du();
   }

   private Boolean isNoRotationVisible() {
      return (Boolean)this.freeze.atx();
   }

   private Boolean isFreezeModeVisible() {
      return (Boolean)this.freeze.atx();
   }

   private static Boolean isFreezeVisible() {
      return true;
   }

   private Boolean isNoMoveVisible() {
      boolean flag;
      if (this.mode.atx() == ElytraFlyControlVanillaMode.VANILLA) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSpeedVisible() {
      boolean flag;
      if (this.mode.atx() == ElytraFlyControlVanillaMode.CONTROL) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
