package pw.hachimi.client.module.movement;

import java.util.Iterator;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.TickEvent4;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.Tick_PreEvent;
import pw.hachimi.client.event.UnknownEvent7;
import pw.hachimi.client.event.movement.TravelHeadEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.MovementTracker;
import pw.hachimi.client.manager.ServerManager;
import pw.hachimi.client.mixin.FireworkRocketEntityMixin;
import pw.hachimi.client.model.movement.FakeFlyOption;
import pw.hachimi.client.model.movement.FakeFlyTickableForceMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.combat.AutoSpear;
import pw.hachimi.client.module.player.Freecam;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.movement.UnidentifiedHelper21;
import pw.hachimi.eventbus.annotation.EventListener;

public class FakeFly extends ClientModule {
   private static FakeFly fakeFly;
   private final Setting mode = this.registerSetting(new EnumSetting("Mode", "mode.", FakeFlyOption.a_, FakeFlyOption.values()));
   Setting swapAlternative = this.registerSetting(new BooleanSetting("SwapAlternative", "Uses inventory swap for swapping", false));
   Setting autoBoost = this.registerSetting(new BooleanSetting("AutoBoost", "Auto release firework when you flying", true));
   Setting grimFix = this.registerSetting(new BooleanSetting("GrimFix", "Fix sneak state (useless)", false));
   Setting rocketDelay = this.registerSetting(new NumberSetting("RocketDelay", "Delay for release rocket", 0.0F, 0.5F, 5.0F, this::isRocketDelayVisible));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Rotates the player when moving", true));
   Setting pitch = this.registerSetting(new NumberSetting("Pitch", "Pitch", -90.0F, 0.0F, 90.0F, this::isPitchVisible));
   Setting freeze = this.registerSetting(new EnumSetting("Freeze", "Freeze when not moving", FakeFlyTickableForceMode.Tickable, FakeFlyTickableForceMode.values()));
   Setting time = this.registerSetting(new NumberSetting("Time", "Time to tick", 0, 1000, 5000));
   Setting autoJump = this.registerSetting(new BooleanSetting("AutoJump", "I dont know.", true));
   Setting highPriority = this.registerSetting(new BooleanSetting("HighPriority", "Rotation priority.", true, this::isHighPriorityVisible));
   private final StopwatchImpl stopwatchImpl = new StopwatchImpl();
   private final StopwatchImpl stopwatchImpl2 = new StopwatchImpl();
   private final StopwatchImpl stopwatchImpl3 = new StopwatchImpl();
   float p;
   float q;
   boolean r;
   public static boolean bs;

   public FakeFly() {
      super("FakeFly", "", ModuleCategory.MOVEMENT);
      fakeFly = this;
   }

   public static FakeFly qr() {
      return fakeFly;
   }

   @EventListener
   public void onTickEvent4(TickEvent4 tickEvent4) {
      tickEvent4.aim();
   }

   @Override
   public String hy() {
      return ((FakeFlyOption)this.mode.atx()).name();
   }

   @Override
   protected void awd() {
      if (client.player != null) {
         this.r = false;
         this.p = client.player.getYaw();
         this.q = client.player.getPitch();
      }
   }

   @EventListener(priority = -999999)
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         label132: {
            label115: {
               label129: {
                  if (client.options.forwardKey.isPressed() && !client.options.backKey.isPressed()) {
                     if (client.options.backKey.isPressed() && !client.options.forwardKey.isPressed()) {
                     }
                  } else if (!client.options.backKey.isPressed() || client.options.forwardKey.isPressed()) {
                     if (!client.options.leftKey.isPressed() || client.options.rightKey.isPressed()) {
                        if (!client.options.rightKey.isPressed() || client.options.leftKey.isPressed()) {
                           boolean flag = client.options.jumpKey.isPressed();
                           boolean flag2 = client.options.sneakKey.isPressed();
                           if ((flag || flag2 || Follower.dM().dS() || AutoSpear.hP()) && !Freecam.gP().awg()) {
                              this.r = false;
                              float f;
                              if (flag) {
                                 f = -89.0F;
                              } else if (flag2) {
                                 f = 89.0F;
                              } else {
                                 f = (Float)this.pitch.atx();
                              }

                              this.q = f;
                              break label132;
                           }

                           this.r = true;
                           break label132;
                        }
                        break label115;
                     }
                     break label129;
                  }

                  if (client.options.leftKey.isPressed() && !client.options.rightKey.isPressed()) {
                  }
               }

               if (client.options.rightKey.isPressed() && !client.options.leftKey.isPressed()) {
               }
            }

            boolean flag3 = client.options.jumpKey.isPressed();
            boolean flag4 = client.options.sneakKey.isPressed();
            if (!flag3 && !flag4) {
            }

            if (Freecam.gP().awg()) {
               this.r = true;
            } else {
               this.r = false;
               float f2;
               if (flag3) {
                  f2 = -89.0F;
               } else if (flag4) {
                  f2 = 89.0F;
               } else {
                  f2 = (Float)this.pitch.atx();
               }

               this.q = f2;
               if (flag3 || flag4) {
                  if (flag3) {
                     f2 = -45.0F;
                  } else {
                     f2 = 45.0F;
                  }

                  this.q = f2;
               }
            }

            this.p = Sprint.mD().mL(client.player.getYaw());
         }

         if ((Boolean)this.rotate.atx() && !Follower.dM().dS() && !AutoSpear.hP()) {
            if (this.r) {
               Managers.movementTracker.Mh(new UnidentifiedHelper21(50, this.p, this.q));
            } else {
               Managers.movementTracker.Mh(new UnidentifiedHelper21((Boolean)this.highPriority.atx() ? 99999 : 200, this.p, this.q));
            }
         }
      }
   }

   @EventListener(priority = -100)
   public void onTick_Pre(Tick_PreEvent tickPreEvent) {
      if ((Boolean)this.autoJump.atx() && client.player.isOnGround() && tickPreEvent.getEventPhase() == EventPhase.POST) {
         client.player.input.jumping = true;
      }
   }

   @EventListener
   public void onTravelHead(TravelHeadEvent travelHeadEvent) {
      if (!this.r
         || Follower.dM().dS()
         || AutoSpear.hP()
         || this.freeze.atx() != FakeFlyTickableForceMode.Force
            && (this.freeze.atx() != FakeFlyTickableForceMode.Tickable || this.stopwatchImpl3.afq((Number)this.time.atx()))) {
         this.stopwatchImpl3.afr(29432, 5265492, (byte)40);
      } else {
         travelHeadEvent.aim();
      }
   }

   @EventListener
   public void qw(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (client.player != null && client.world != null) {
         EquipmentSlot equipmentSlot = EquipmentSlot.CHEST;
         ItemStack temp = client.player.getEquippedStack(equipmentSlot);
         Item item2 = Items.ELYTRA;
         if (!temp.isOf(item2)) {
            int n = 0;

            do {
               PlayerInventory playerInventory = client.player.getInventory();
               int n2 = n;
               if (playerInventory.getStack(n2).getItem() instanceof ElytraItem) {
                  int n3 = n;
                  if (n3 != -1
                     && (
                        !client.player.isFallFlying()
                           || this.stopwatchImpl.afq(250)
                              && (Boolean)this.autoBoost.atx()
                              && !this.qz()
                              && this.stopwatchImpl2.afq((Float)this.rocketDelay.atx() * 1000.0F)
                     )
                     && !client.player.isOnGround()
                     && !client.player.isTouchingWater()
                     && !this.r) {
                     this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                     if (n3 >= 9) {
                        ClientPlayerEntity player2 = client.player;
                        SlotActionType slotActionType = SlotActionType.SWAP;
                        int n4 = n3;
                        int n5 = client.player.currentScreenHandler.syncId;
                        client.interactionManager.clickSlot(n5, n4, 8, slotActionType, (PlayerEntity)player2);
                        ClientPlayerEntity player3 = client.player;
                        SlotActionType slotActionType2 = SlotActionType.SWAP;
                        int n6 = client.player.currentScreenHandler.syncId;
                        client.interactionManager.clickSlot(n6, 6, 8, slotActionType2, (PlayerEntity)player3);
                     } else {
                        ClientPlayerEntity player4 = client.player;
                        SlotActionType slotActionType3 = SlotActionType.SWAP;
                        int n7 = n3;
                        int n8 = client.player.currentScreenHandler.syncId;
                        client.interactionManager.clickSlot(n8, 6, n7, slotActionType3, (PlayerEntity)player4);
                     }

                     ServerManager serverManager2 = Managers.serverManager;
                     ClientCommandC2SPacket clientCommandC2SPacket = new ClientCommandC2SPacket(client.player, Mode.START_FALL_FLYING);
                     serverManager2.iO('\uf560', clientCommandC2SPacket);
                     if ((Boolean)this.grimFix.atx() && !client.player.isSneaking()) {
                        ServerManager serverManager3 = Managers.serverManager;
                        clientCommandC2SPacket = new ClientCommandC2SPacket(client.player, Mode.PRESS_SHIFT_KEY);
                        serverManager3.iO('\uf560', clientCommandC2SPacket);
                        ServerManager serverManager4 = Managers.serverManager;
                        clientCommandC2SPacket = new ClientCommandC2SPacket(client.player, Mode.RELEASE_SHIFT_KEY);
                        serverManager4.iO('\uf560', clientCommandC2SPacket);
                     }

                     client.player.startFallFlying();
                     this.qx();
                     if (n3 >= 9) {
                        ClientPlayerEntity player5 = client.player;
                        SlotActionType slotActionType4 = SlotActionType.SWAP;
                        int n9 = client.player.currentScreenHandler.syncId;
                        client.interactionManager.clickSlot(n9, 6, 8, slotActionType4, (PlayerEntity)player5);
                        ClientPlayerEntity player6 = client.player;
                        SlotActionType slotActionType5 = SlotActionType.SWAP;
                        int n10 = n3;
                        int n11 = client.player.currentScreenHandler.syncId;
                        client.interactionManager.clickSlot(n11, n10, 8, slotActionType5, (PlayerEntity)player6);
                     } else {
                        ClientPlayerEntity player7 = client.player;
                        SlotActionType slotActionType6 = SlotActionType.SWAP;
                        int n12 = n3;
                        int n13 = client.player.currentScreenHandler.syncId;
                        client.interactionManager.clickSlot(n13, 6, n12, slotActionType6, (PlayerEntity)player7);
                     }
                  }
                  break;
               }
            } while (++n < 36);
         } else if (!client.player.isFallFlying() && !client.player.isOnGround() && !client.player.isTouchingWater()) {
            ClientCommandC2SPacket clientCommandC2SPacket2 = new ClientCommandC2SPacket(client.player, Mode.START_FALL_FLYING);
            client.getNetworkHandler().sendPacket((Packet)clientCommandC2SPacket2);
            client.player.startFallFlying();
            this.qx();
         } else if (client.player.isFallFlying()) {
            this.qx();
         }
      }
   }

   private void qx() {
      if ((Boolean)this.autoBoost.atx() && !this.r && !this.qz() && this.stopwatchImpl2.afq((Float)this.rocketDelay.atx() * 1000.0F)) {
         int n = 0;
         if ((Boolean)this.swapAlternative.atx()) {
            if (client.player.getInventory().getStack(0).getItem() instanceof FireworkRocketItem) {
               n = Managers.inventoryTracker.JS();
               Managers.inventoryTracker.JF(0);
               Managers.serverManager.iQ(FakeFly::qB);
               Managers.inventoryTracker.JF(n);
               this.stopwatchImpl2.afr(29432, 5265492, (byte)40);
               return;
            }

            n++;
            byte b;
            if ((Boolean)this.swapAlternative.atx()) {
               b = 36;
            } else {
               b = 9;
            }

            if (n >= b) {
               return;
            }
         }

         byte b2;
         do {
            PlayerInventory temp = client.player.getInventory();
            int n2 = n;
            if (temp.getStack(n2).getItem() instanceof FireworkRocketItem) {
               int n3 = n;
               if (n3 != -1) {
                  n = Managers.inventoryTracker.JS();
                  if (n3 < 9) {
                     Managers.inventoryTracker.JF(n3);
                     Managers.serverManager.iQ(FakeFly::qB);
                     Managers.inventoryTracker.JF(n);
                  } else {
                     Managers.inventoryTracker.JK(n3, n);
                     Managers.serverManager.iQ(FakeFly::qA);
                     Managers.inventoryTracker.JK(n3, n);
                  }

                  this.stopwatchImpl2.afr(29432, 5265492, (byte)40);
               }

               return;
            }

            n++;
            if ((Boolean)this.swapAlternative.atx()) {
               b2 = 36;
            } else {
               b2 = 9;
            }
         } while (n < b2);
      }
   }

   @EventListener
   public void qy(UnknownEvent7 unknownEvent7) {
      if (client.player != null && !client.player.isOnGround()) {
         unknownEvent7.aim();
      }
   }

   private boolean qz() {
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

   private static Packet qA(int n) {
      return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, client.player.getYaw(), client.player.getPitch());
   }

   private static Packet qB(int n) {
      return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, client.player.getYaw(), client.player.getPitch());
   }

   private Boolean isHighPriorityVisible() {
      return (Boolean)this.rotate.atx();
   }

   private Boolean isPitchVisible() {
      return (Boolean)this.rotate.atx();
   }

   private Boolean isRocketDelayVisible() {
      return (Boolean)this.autoBoost.atx();
   }
}
