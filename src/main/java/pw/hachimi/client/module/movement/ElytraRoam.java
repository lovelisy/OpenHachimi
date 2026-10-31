package pw.hachimi.client.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.TickEvent4;
import pw.hachimi.client.event.Tick_PreEvent;
import pw.hachimi.client.event.UnknownEvent7;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.movement.ElytraRoamBouncePitch40Mode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.movement.UnidentifiedHelper20;
import pw.hachimi.client.utils.movement.UnidentifiedHelper21;
import pw.hachimi.client.utils.movement.UnidentifiedUtils;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class ElytraRoam extends ClientModule {
   private static ElytraRoam elytraRoam;
   Setting mode = this.registerSetting(new EnumSetting("Mode", "mode.", ElytraRoamBouncePitch40Mode.Bounce, ElytraRoamBouncePitch40Mode.values()));
   Setting packetArmor = this.registerSetting(new BooleanSetting("PacketArmor", "Allow you glide with armor", false));
   Setting pitch = this.registerSetting(new NumberSetting("Pitch", "Pitch", -90.0F, 90.0F, 90.0F, this::isPitchVisible));
   Setting autoForward = this.registerSetting(new BooleanSetting("AutoForward", "Press W", true, this::isAutoForwardVisible));
   Setting lagBoost = this.registerSetting(new BooleanSetting("LagBoost", "Exploit", false, this::isLagBoostVisible));
   Setting maxSpeed = this.registerSetting(new NumberSetting("MaxSpeed", "Max glide speed", 50.0F, 150.0F, 170.0F, this::isMaxSpeedVisible));
   Setting minSpeed = this.registerSetting(new NumberSetting("MinSpeed", "Min glide speed", 10.0F, 25.0F, 70.0F, this::isMinSpeedVisible));
   Setting height = this.registerSetting(new NumberSetting("Height", "The altitude you are at", -50.0F, 300.0F, 500.0F, this::isHeightVisible));
   Setting obstaclePasser = new BooleanSetting("ObstaclePasser", "Passes obstacles and resets fly using Baritone", false);
   private boolean k;
   private boolean l;
   private int m;
   private boolean n;
   private float o;
   private Vec3d vec;
   public static boolean ey;

   public ElytraRoam() {
      super("ElytraRoam", "", ModuleCategory.MOVEMENT);
      elytraRoam = this;
      if (Bootstrap.NV()) {
         this.registerSetting(this.obstaclePasser);
      }
   }

   public static ElytraRoam ZG() {
      return elytraRoam;
   }

   @Override
   public void awe() {
      if ((Boolean)this.obstaclePasser.atx() && Bootstrap.NV()) {
         UnidentifiedUtils.eQ();
      }
   }

   @Override
   protected void awd() {
      if (client.player != null) {
         this.l = client.player.isOnGround();
      } else {
         this.l = true;
      }
   }

   @EventListener
   public void onTickEvent4(TickEvent4 tickEvent4) {
      if ((Boolean)this.packetArmor.atx()) {
         tickEvent4.aim();
      }
   }

   @Override
   public String hy() {
      return ((ElytraRoamBouncePitch40Mode)this.mode.atx()).name();
   }

   @EventListener(priority = -100)
   public void onTick_Pre(Tick_PreEvent tickPreEvent) {
      if (this.mode.atx() == ElytraRoamBouncePitch40Mode.Bounce) {
         client.player.input.jumping = false;
         if ((Boolean)this.autoForward.atx() && !this.l) {
            client.player.input.movementForward = 1.0F;
            client.player.input.movementSideways = 0.0F;
         }
      }
   }

   @EventListener
   public void ZJ(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (client.player != null && client.world != null && (!Bootstrap.NV() || !UnidentifiedUtils.eP())) {
         if (Bootstrap.NV() && (Boolean)this.obstaclePasser.atx()) {
            if (!UnidentifiedUtils.eS()) {
               ChatUtils.UC(Formatting.RED + "Please enable FreeLook in Baritone to use ObstaclePasser!", 5005);
               return;
            }

            if (client.player.horizontalCollision) {
               this.l = client.player.isOnGround();
               Vec3d vec2 = client.player.getPos();
               Vec3d vec3 = RotationUtils.nS(0.0F, client.player.getYaw());
               int n2 = 3;

               BlockPos pos;
               label136: {
                  do {
                     double temp = vec3.x * n2;
                     double d = vec3.z * n2;
                     double d2 = temp;
                     BlockPos pos2 = BlockPos.ofFloored((Position)vec2.add(d2, 0.0, d));
                     if (client.world.getBlockState(pos2).isAir()) {
                        ClientWorld world2 = client.world;
                        BlockPos pos3 = pos2.up();
                        if (world2.getBlockState(pos3).isAir()) {
                           pos = pos2;
                           if (pos2 != null) {
                              break label136;
                           }
                           break;
                        }
                     }
                  } while (++n2 < 64);

                  double d3 = vec3.x * 200.0;
                  double d4 = vec3.z * 200.0;
                  double d5 = d3;
                  pos = BlockPos.ofFloored((Position)vec2.add(d5, 0.0, d4));
               }

               UnidentifiedUtils.eR(pos.getX(), pos.getY(), pos.getZ());
               return;
            }
         }

         Managers.movementTracker.a_ = true;
         int n3;
         if ((n3 = ((ElytraRoamBouncePitch40Mode)this.mode.atx()).ordinal()) != 0) {
            if (n3 == 1 && this.vec != null) {
               double d6 = Math.hypot(client.player.getX() - this.vec.getX(), client.player.getZ() - this.vec.getZ()) * 72.0;
               if (!(client.player.getY() < ((Float)this.height.atx()).floatValue())) {
                  this.n = true;
               } else if (!(d6 < ((Float)this.minSpeed.atx()).floatValue())) {
                  if (d6 > ((Float)this.maxSpeed.atx()).floatValue()) {
                     this.n = false;
                  }
               } else {
                  this.n = true;
               }

               if (!this.n) {
                  this.o -= 3.0F;
               } else {
                  this.o += 3.0F;
               }

               this.o = MathHelper.clamp(this.o, -40.0F, 40.0F);
               if (!Follower.dM().dS() && client.player.isFallFlying()) {
                  Managers.movementTracker.Mh(new UnidentifiedHelper21(114514, client.player.getYaw(),this.o));
               }

               if (!client.player.isTouchingWater() && !client.player.isOnGround()) {
                  EquipmentSlot equipmentSlot = EquipmentSlot.CHEST;
                  ItemStack temp2 = client.player.getEquippedStack(equipmentSlot);
                  Item item2 = Items.ELYTRA;
                  if (!temp2.isOf(item2)) {
                     if ((Boolean)this.packetArmor.atx()) {
                        int n4 = this.ZK();
                        if (n4 != -1) {
                           if (!Follower.dM().dS()) {
                              Managers.movementTracker.Mh(new UnidentifiedHelper21(114514, client.player.getYaw(),this.o));
                           }

                           this.ZL(n4);
                        }
                     }
                  } else if (!client.player.isFallFlying()) {
                     if (!Follower.dM().dS()) {
                        Managers.movementTracker.Mh(new UnidentifiedHelper21(114514, client.player.getYaw(),this.o));
                     }

                     ClientCommandC2SPacket clientCommandC2SPacket = new ClientCommandC2SPacket(client.player, Mode.START_FALL_FLYING);
                     client.getNetworkHandler().sendPacket((Packet)clientCommandC2SPacket);
                     client.player.startFallFlying();
                  }
               }
            }
         } else {
            if (!Follower.dM().dS()) {
               Managers.movementTracker.Mh(new UnidentifiedHelper21(114514, client.player.getYaw(),(Float)this.pitch.atx()));
            }

            if (!client.player.isOnGround()) {
               this.m++;
            } else {
               this.m = 0;
            }

            if (this.l) {
               if (client.player.isOnGround()) {
                  client.player.jump();
                  return;
               }

               this.l = false;
            }

            if (this.k) {
               ClientCommandC2SPacket clientCommandC2SPacket2 = new ClientCommandC2SPacket(client.player, Mode.START_FALL_FLYING);
               client.getNetworkHandler().sendPacket((Packet)clientCommandC2SPacket2);
               this.k = false;
            }

            if (client.player.getHungerManager().getFoodLevel() > 6.0F) {
               client.player.setSprinting(true);
            }

            if (!client.player.isTouchingWater()) {
               EquipmentSlot equipmentSlot2 = EquipmentSlot.CHEST;
               ItemStack stack = client.player.getEquippedStack(equipmentSlot2);
               Item item3 = Items.ELYTRA;
               if (!stack.isOf(item3)) {
                  if ((Boolean)this.packetArmor.atx()) {
                     int n5 = this.ZK();
                     if (n5 != -1) {
                        if (client.player.isOnGround()) {
                           client.player.jump();
                        }

                        this.ZL(n5);
                     }
                  }
               } else {
                  if (client.player.isOnGround()) {
                     client.player.jump();
                  }

                  if (!client.player.isFallFlying()) {
                     if (client.player.isOnGround()) {
                        this.k = true;
                     } else {
                        ClientCommandC2SPacket clientCommandC2SPacket3 = new ClientCommandC2SPacket(client.player, Mode.START_FALL_FLYING);
                        client.getNetworkHandler().sendPacket((Packet)clientCommandC2SPacket3);
                     }

                     client.player.startFallFlying();
                  }
               }
            }
         }
      }
   }

   private int ZK() {
      int n2 = 0;

      do {
         PlayerInventory temp = client.player.getInventory();
         int n3 = n2;
         if (temp.getStack(n3).getItem() instanceof ElytraItem) {
            return n2;
         }
      } while (++n2 < 36);

      return -1;
   }

   private void ZL(int n2) {
      if (!client.player.isFallFlying()) {
         if (n2 >= 9) {
            ClientPlayerEntity player2 = client.player;
            SlotActionType slotActionType = SlotActionType.SWAP;
            int n3 = client.player.currentScreenHandler.syncId;
            client.interactionManager.clickSlot(n3, n2, 8, slotActionType, (PlayerEntity)player2);
            ClientPlayerEntity player3 = client.player;
            SlotActionType slotActionType2 = SlotActionType.SWAP;
            int n4 = client.player.currentScreenHandler.syncId;
            client.interactionManager.clickSlot(n4, 6, 8, slotActionType2, (PlayerEntity)player3);
         } else {
            ClientPlayerEntity player4 = client.player;
            SlotActionType slotActionType3 = SlotActionType.SWAP;
            int n5 = client.player.currentScreenHandler.syncId;
            client.interactionManager.clickSlot(n5, 6, n2, slotActionType3, (PlayerEntity)player4);
         }

         ClientCommandC2SPacket clientCommandC2SPacket = new ClientCommandC2SPacket(client.player, Mode.START_FALL_FLYING);
         client.getNetworkHandler().sendPacket((Packet)clientCommandC2SPacket);
         client.player.startFallFlying();
         if (n2 >= 9) {
            ClientPlayerEntity player5 = client.player;
            SlotActionType slotActionType4 = SlotActionType.SWAP;
            int n6 = client.player.currentScreenHandler.syncId;
            client.interactionManager.clickSlot(n6, 6, 8, slotActionType4, (PlayerEntity)player5);
            ClientPlayerEntity player6 = client.player;
            SlotActionType slotActionType5 = SlotActionType.SWAP;
            int n7 = client.player.currentScreenHandler.syncId;
            client.interactionManager.clickSlot(n7, n2, 8, slotActionType5, (PlayerEntity)player6);
         } else {
            ClientPlayerEntity player7 = client.player;
            SlotActionType slotActionType6 = SlotActionType.SWAP;
            int n8 = client.player.currentScreenHandler.syncId;
            client.interactionManager.clickSlot(n8, 6, n2, slotActionType6, (PlayerEntity)player7);
         }
      }
   }

   @EventListener(priority = -1)
   public void ZM(UnidentifiedHelper20 unidentifiedHelper20) {
      this.vec = new Vec3d(client.player.lastX, client.player.lastBaseY, client.player.lastZ);
      if (this.mode.atx() == ElytraRoamBouncePitch40Mode.Bounce && !this.l && (Boolean)this.lagBoost.atx() && this.m > 1 && !client.player.isTouchingWater()) {
         double d = client.player.getY() - client.player.prevY;
         if ((!(d > 0.3) || !(d < 0.5)) && (!(d >= 0.0) || !(d < 0.1))) {
            unidentifiedHelper20.are();
            unidentifiedHelper20.aqR(unidentifiedHelper20.aqQ() + 2.5 * (client.player.age % 3 + 1));
         }
      }
   }

   @EventListener
   public void ZN(UnknownEvent7 unknownEvent7) {
      if ((Boolean)this.packetArmor.atx()) {
         unknownEvent7.aim();
      }
   }

   private Boolean isHeightVisible() {
      boolean flag;
      if (this.mode.atx() == ElytraRoamBouncePitch40Mode.Pitch40) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isMinSpeedVisible() {
      boolean flag;
      if (this.mode.atx() == ElytraRoamBouncePitch40Mode.Pitch40) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isMaxSpeedVisible() {
      boolean flag;
      if (this.mode.atx() == ElytraRoamBouncePitch40Mode.Pitch40) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isLagBoostVisible() {
      boolean flag;
      if (this.mode.atx() == ElytraRoamBouncePitch40Mode.Bounce) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isAutoForwardVisible() {
      boolean flag;
      if (this.mode.atx() == ElytraRoamBouncePitch40Mode.Bounce) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isPitchVisible() {
      boolean flag;
      if (this.mode.atx() == ElytraRoamBouncePitch40Mode.Bounce) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
