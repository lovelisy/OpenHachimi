package pw.hachimi.client.module.movement;

import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.event.movement.DismountVehicleEvent;
import pw.hachimi.client.event.movement.TravelHeadEvent;
import pw.hachimi.client.event.movement.TravelPreEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.mixin.PlayerMoveC2SPacketMixin;
import pw.hachimi.client.model.movement.BoatFlyNormalPacketMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.movement.MovementUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class BoatFly extends ClientModule {
   Setting numberSetting1 = this.registerSetting(new NumberSetting("Speed", "The horizontal flight speed", 0.1F, 2.5F, 10.0F));
   Setting numberSetting2 = this.registerSetting(new NumberSetting("VerticalSpeed", "The vertical flight speed", 0.1F, 1.0F, 5.0F));
   Setting normalPacketMode = this.registerSetting(new EnumSetting("AntiKick", "Prevents vanilla flight detection", BoatFlyNormalPacketMode.NORMAL, BoatFlyNormalPacketMode.values()));
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Accelerate", "Accelerate as you fly", false));
   Setting numberSetting3 = this.registerSetting(new NumberSetting("AccelerateSpeed", "Speed to accelerate as", 0.01F, 0.2F, 1.0F, this::isNumberSetting3Visible));
   Setting numberSetting4 = this.registerSetting(new NumberSetting("MaxSpeed", "Max speed to accelerate to", 1.0F, 5.0F, 10.0F, this::isNumberSetting4Visible));
   private double g;
   private double h;
   private boolean i;
   private int j;
   private boolean k;
   public static boolean ei;

   public BoatFly() {
      super("BoatFly", "Allows you fly with entities", ModuleCategory.MOVEMENT);
   }

   @Override
   public void awd() {
      this.g = 0.0;
   }

   @Override
   public void awe() {
      this.k = false;
   }

   @EventListener
   public void onTravelPre(TravelPreEvent travelPreEvent) {
      if (client.player.getVehicle() != null && travelPreEvent.getLiving().getId() == client.player.getVehicle().getId()) {
         travelPreEvent.aim();
      }
   }

   @EventListener
   public void onDismountVehicle(DismountVehicleEvent dismountVehicleEvent) {
      if (client.options.sneakKey.isPressed()) {
         dismountVehicleEvent.aim();
      }
   }

   @EventListener
   public void onTravelHead(TravelHeadEvent travelHeadEvent) {
      if (client.player.getVehicle() != null) {
         Entity entity2 = client.player.getVehicle();
         if ((Boolean)this.booleanSetting1_1.atx()) {
            if (!MovementUtils.awH() || entity2.horizontalCollision) {
               this.g = 0.0;
            }

            this.g = this.g + ((Float)this.numberSetting3.atx()).floatValue();
            if (this.g > ((Float)this.numberSetting4.atx()).floatValue()) {
               this.g = ((Float)this.numberSetting4.atx()).floatValue();
            }
         } else {
            this.g = ((Float)this.numberSetting1.atx()).floatValue();
         }

         Entity temp = entity2;
         float f = client.player.getYaw();
         temp.setYaw(f);
         if (entity2 instanceof LlamaEntity) {
            LlamaEntity llamaEntity = (LlamaEntity)entity2;
            float f2 = client.player.getYaw();
            llamaEntity.headYaw = f2;
         }

         label47: {
            label46: {
               if (this.i) {
                  this.j++;
                  if (this.j >= 20) {
                     if (this.normalPacketMode.atx() == BoatFlyNormalPacketMode.PACKET) {
                        this.k = true;
                     } else if (this.normalPacketMode.atx() == BoatFlyNormalPacketMode.NORMAL) {
                        temp = entity2;
                        double d = entity2.getX();
                        double d2 = entity2.getY() - 0.0313;
                        double d3 = entity2.getZ();
                        double d4 = d2;
                        double d5 = d;
                        temp.setPosition(d5, d4, d3);
                     }

                     this.j = 0;
                     this.i = false;
                     temp = entity2;
                     double d6 = entity2.getVelocity().x;
                     double d7 = entity2.getVelocity().z;
                     double d8 = d6;
                     temp.setVelocity(d8, 0.0, d7);
                     if (client.options.jumpKey.isPressed()) {
                     }
                     break label46;
                  }
               }

               temp = entity2;
               double temp2 = entity2.getVelocity().x;
               double d9 = entity2.getVelocity().z;
               double d10 = temp2;
               temp.setVelocity(d10, 0.0, d9);
               if (client.options.jumpKey.isPressed()) {
                  temp = entity2;
                  temp2 = entity2.getVelocity().x;
                  double temp3 = ((Float)this.numberSetting2.atx()).floatValue();
                  double d11 = entity2.getVelocity().z;
                  double d12 = temp3;
                  double d13 = temp2;
                  temp.setVelocity(d13, d12, d11);
                  break label47;
               }
            }

            if (client.options.sneakKey.isPressed()) {
               temp = entity2;
               double d14 = entity2.getVelocity().x;
               double d15 = -(Float)this.numberSetting2.atx();
               double d16 = entity2.getVelocity().z;
               double d17 = d15;
               double d18 = d14;
               temp.setVelocity(d18, d17, d16);
            }
         }

         this.g = Math.max(this.g, 0.2873F);
         float f3 = client.player.input.movementForward;
         float f4 = client.player.input.movementSideways;
         float f5 = client.player.getYaw();
         if (f3 != 0.0F || f4 != 0.0F) {
            double d19 = Math.cos(Math.toRadians(f5 + 90.0F));
            double d20 = Math.sin(Math.toRadians(f5 + 90.0F));
            double d21 = f3 * this.g * d19 + f4 * this.g * d20;
            double d22 = f3 * this.g * d20 - f4 * this.g * d19;
            double d23 = entity2.getVelocity().y;
            double d24 = d21;
            entity2.setVelocity(d24, d23, d22);
            travelHeadEvent.aim();
         }
      }
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (client.player != null && client.player.getVehicle() != null) {
         Entity entity2 = client.player.getVehicle();
         Packet packet2 = sendImmediatelyEvent.getPacket();
         if (packet2 instanceof VehicleMoveC2SPacket && this.normalPacketMode.atx() != BoatFlyNormalPacketMode.OFF) {
            double d = ((VehicleMoveC2SPacket)packet2).getY();
            if (this.k) {
               ((PlayerMoveC2SPacketMixin)((VehicleMoveC2SPacket)packet2)).hookSetY(this.h - 0.04);
               this.k = false;
            } else {
               this.i = this.ajf((VehicleMoveC2SPacket)packet2, entity2);
               this.h = d;
            }
         }

         packet2 = sendImmediatelyEvent.getPacket();
         if (packet2 instanceof ClientCommandC2SPacket
            && ((ClientCommandC2SPacket)packet2).getMode() == Mode.PRESS_SHIFT_KEY
            && client.player.getVehicle() != null
            && !client.player.getVehicle().isOnGround()) {
            sendImmediatelyEvent.aim();
         }
      }
   }

   private boolean ajf(VehicleMoveC2SPacket vehicleMoveC2SPacket, Entity entity2) {
      boolean flag;
      if (MathHelper.clamp(vehicleMoveC2SPacket.getY(), -2.0E7, 2.0E7) - this.h >= -0.03125 && !entity2.groundCollision && this.ajg(entity2)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private boolean ajg(Entity entity2) {
      Box box = entity2.getBoundingBox().expand(0.0625).stretch(0.0, -0.55, 0.0);
      Stream temp = entity2.getWorld().getStatesInBox(box);
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object entity2) {
            return ((AbstractBlockState)entity2).isAir();
         }
      };
      return temp.allMatch(predicate);
   }

   private Boolean isNumberSetting4Visible() {
      return (Boolean)this.booleanSetting1_1.atx();
   }

   private Boolean isNumberSetting3Visible() {
      return (Boolean)this.booleanSetting1_1.atx();
   }
}
