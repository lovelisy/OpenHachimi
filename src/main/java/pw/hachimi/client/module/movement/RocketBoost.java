package pw.hachimi.client.module.movement;

import java.util.Iterator;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.TickEvent;
import pw.hachimi.client.event.movement.ElytraVelocityEvent;
import pw.hachimi.client.event.movement.TravelHeadEvent;
import pw.hachimi.client.event.player.GetRotationVectorHookEvent;
import pw.hachimi.client.mixin.FireworkRocketEntityMixin;
import pw.hachimi.client.model.movement.RocketBoostNormalDirectMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.movement.UnidentifiedHelper20;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public final class RocketBoost extends ClientModule {
   private static RocketBoost INSTANCE;
   Setting mode = this.registerSetting(new EnumSetting("Mode", "mode", RocketBoostNormalDirectMode.Normal, RocketBoostNormalDirectMode.values()));
   Setting numberSetting1 = this.registerSetting(new NumberSetting("DirectSpeed", ":)", 0.0F, 1.7F, 5.0F, this::isNumberSetting1Visible));
   Setting numberSetting2 = this.registerSetting(new NumberSetting("Accelerate", ":0", 0.0F, 1.5F, 10.0F, this::isNumberSetting2Visible));
   Setting compensation = this.registerSetting(new NumberSetting("YSpeed", ":(", 0.0F, 8.0F, 10.0F, this::isCompensationVisible));
   Setting velocity = this.registerSetting(new NumberSetting("Compensation", ":>", 0.0F, 0.01F, 0.05F, this::isVelocityVisible));
   Setting baseSpeed = this.registerSetting(new NumberSetting("BaseSpeed", "offset * baseSpeed + (offset * speed - velocity) * factor", 0.0F, 0.1F, 5.0F, this::isBaseSpeedVisible));
   Setting speed = this.registerSetting(new NumberSetting("Speed", "offset * baseSpeed + (offset * speed - velocity) * factor", 0.0F, 1.5F, 5.0F, this::isSpeedVisible));
   Setting factor = this.registerSetting(new NumberSetting("Factor", "offset * baseSpeed + (offset * speed - velocity) * factor", 0.0F, 0.5F, 5.0F, this::isFactorVisible));
   Vec3d vec = Vec3d.ZERO;
   private static final double j = 0.01;

   public RocketBoost() {
      super("RocketBoost", "Allows you to change the acceleration of your firework", ModuleCategory.MOVEMENT);
      INSTANCE = this;
   }

   public static RocketBoost aqv() {
      return INSTANCE;
   }

   @EventListener
   public void onTravelHead(TravelHeadEvent travelHeadEvent) {
      if (this.mode.atx() == RocketBoostNormalDirectMode.Grim && this.aqI() && client.player.isFallFlying()) {
         double d = ((Float)this.numberSetting1.atx()).floatValue();
         Vec3d vec2 = this.aqD().multiply(d);
         client.player.setVelocity(vec2);
      }
   }

   @EventListener(priority = -99999)
   public void aqx(UnidentifiedHelper20 unidentifiedHelper20) {
      double temp = unidentifiedHelper20.aqO() - client.player.lastX;
      double d = unidentifiedHelper20.aqQ() - client.player.lastBaseY;
      double d2 = temp;
      this.vec = new Vec3d(d2, d, unidentifiedHelper20.aqS() - client.player.lastZ);
   }

   public Vec3d aqy(Vec3d vec2, Vec3d vec3) {
      if (!(vec2.lengthSquared() < 1.0E-6)) {
         Vec3d vec4 = this.vec;
         Vec3d vec5 = aqH(client.player, vec4, vec3);
         Vec3d vec6 = aqE(client.player.lastPitch, client.player.lastYaw);
         Vec3d vec7 = vec3.normalize();
         Vec3d vec8 = vec6.normalize();
         double d = Math.min(-0.05, vec7.getX()) + Math.min(-0.05, vec8.getX());
         double d2 = Math.min(-0.05, vec7.getY()) + Math.min(-0.05, vec8.getY());
         double d3 = Math.min(-0.05, vec7.getZ()) + Math.min(-0.05, vec8.getZ());
         double d4 = Math.max(0.05, vec7.getX()) + Math.max(0.05, vec8.getX());
         double d5 = Math.max(0.05, vec7.getY()) + Math.max(0.05, vec8.getY());
         double d6 = Math.max(0.05, vec7.getZ()) + Math.max(0.05, vec8.getZ());
         double d7 = Math.min(((Float)this.numberSetting2.atx()).floatValue(), vec2.length());
         d *= d7;
         d4 *= d7;
         d2 *= d7;
         d5 *= d7;
         d3 *= d7;
         d6 *= d7;
         d = Math.max(-d7, d);
         d4 = Math.min(d7, d4);
         d2 = Math.max(-d7, d2);
         d5 = Math.min(d7, d5);
         d3 = Math.max(-d7, d3);
         d6 = Math.min(d7, d6);
         double d8 = Math.min(0.0, d - vec4.x);
         double d9 = Math.max(0.0, d4 - vec4.x);
         double d10 = Math.min(0.0, d2 - vec4.y);
         double d11 = Math.max(0.0, d5 - vec4.y);
         double d12 = Math.min(0.0, d3 - vec4.z);
         double d13 = Math.max(0.0, d6 - vec4.z);
         double d14 = vec5.x + d8 - 0.0;
         double d15 = vec5.x + d9 + 0.0;
         double d16 = vec5.y + d10;
         double d17 = vec5.y + d11;
         double d18 = vec5.z + d12 - 0.0;
         double d19 = vec5.z + d13 + 0.0;
         if (d17 > 1.0E-6) {
            double d20 = vec2.length();
            if (vec2.horizontalLength() < 0.04 * vec2.y) {
               ClientPlayerEntity temp = client.player;
               double d21 = (d20 + ((Float)this.velocity.atx()).floatValue()) / d20;
               double d22 = aqH(temp, vec2.multiply(d21), vec3).y;
               d17 = Math.max(d17, d22);
            }
         }

         double d23;
         double d24;
         double d25;
         double d26;
         double d27;
         double d28;
         label66: {
            label65: {
               label64: {
                  label63: {
                     label62: {
                        label61: {
                           d27 = vec2.x;
                           d23 = vec2.y;
                           d28 = vec2.z;
                           d24 = 0.0;
                           d25 = 0.0;
                           d26 = 0.0;
                           if (!(d27 > 0.0)) {
                              if (!(d27 < 0.0)) {
                                 if (!(d23 > 0.0)) {
                                    if (!(d23 < 0.0)) {
                                       if (!(d28 > 0.0)) {
                                          if (!(d28 < 0.0)) {
                                             Math.max(0.0, Math.max(0.0, 0.0));
                                             return null;
                                          }
                                          break label65;
                                       }
                                       break label64;
                                    }
                                    break label62;
                                 }
                                 break label61;
                              }

                              d24 = d27 / d14;
                           } else {
                              d24 = d27 / d15;
                           }

                           if (!(d23 > 0.0)) {
                              if (!(d23 < 0.0)) {
                                 break label63;
                              }
                              break label62;
                           }
                        }

                        d25 = d23 / d17;
                        break label63;
                     }

                     d25 = d23 / d16;
                  }

                  if (!(d28 > 0.0)) {
                     if (!(d28 < 0.0)) {
                        break label66;
                     }
                     break label65;
                  }
               }

               d26 = d28 / d19;
               break label66;
            }

            d26 = d28 / d18;
         }

         double d29 = Math.max(d24, Math.max(d25, d26));
         if (!(d29 < 1.0E-6)) {
            Vec3d vec9 = aqH(client.player, vec2, vec3);
            double d30 = vec2.length() / vec9.length();
            if (!(d29 >= d30)) {
               double d31 = 1.0 / d29;
               Vec3d vec10 = vec2.multiply(d31);
               return (Float)this.compensation.atx() == 0.0F
                  ? vec10
                  : aqz(vec10, d27, d23, d28, d14, d15, d16, d17, d18, d19, ((Float)this.compensation.atx()).floatValue());
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static Vec3d aqz(
      Vec3d vec2, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10
   ) {
      double d11;
      if (d10 > 0.0 && Double.isFinite(d10)) {
         d11 = d10;
      } else {
         d11 = Double.MAX_VALUE;
      }

      double d12 = vec2.x;
      double temp = aqA(d, d4, d5, d12, Double.MAX_VALUE);
      d12 = vec2.y;
      double temp2 = aqA(d2, d6, d7, d12, d11);
      d12 = vec2.z;
      return new Vec3d(temp, temp2, aqA(d3, d8, d9, d12, Double.MAX_VALUE));
   }

   private static double aqA(double d, double d2, double d3, double d4, double d5) {
      if (d > 0.0) {
         return Math.max(d4, Math.min(d3 - 0.01, d5));
      } else {
         return d < 0.0 ? Math.min(d4, Math.max(d2 + 0.01, -d5)) : d4;
      }
   }

   @EventListener
   public void onElytraVelocity(ElytraVelocityEvent elytraVelocityEvent) {
      if (this.mode.atx() == RocketBoostNormalDirectMode.Grim && this.aqI()) {
         elytraVelocityEvent.aim();
         Vec3d vec2 = this.aqD();
         double d = ((Float)this.numberSetting1.atx()).floatValue();
         Vec3d vec3 = vec2.multiply(d);
         Vec3d vec4 = this.aqy(vec3, vec2);
         if (vec4 != null) {
            client.player.setVelocity(vec4);
         }
      }
   }

   @EventListener
   public void aqC(TickEvent tickEvent) {
      tickEvent.aim();
      int n;
      if ((n = ((RocketBoostNormalDirectMode)this.mode.atx()).ordinal()) != 0) {
         if (n != 1) {
            if (n == 2) {
               tickEvent.aqg(0.0, 0.0, 0.0);
            }
         } else {
            Vec3d vec2 = this.aqD();
            double d = ((Float)this.numberSetting1.atx()).floatValue();
            Vec3d vec3 = client.player.getVelocity();
            Vec3d vec4 = vec2.multiply(d);
            tickEvent.aqg(vec4.x - vec3.x, vec4.y - vec3.y, vec4.z - vec3.z);
         }
      } else {
         Vec3d vec5 = this.aqD();
         double d2 = ((Float)this.speed.atx()).floatValue();
         double d3 = ((Float)this.factor.atx()).floatValue();
         double d4 = ((Float)this.baseSpeed.atx()).floatValue();
         Vec3d vec6 = client.player.getVelocity();
         tickEvent.aqg(
            vec5.x * d4 + (vec5.x * d2 - vec6.x) * d3,
            vec5.y * d4 + (vec5.y * d2 - vec6.y) * d3,
            vec5.z * d4 + (vec5.z * d2 - vec6.z) * d3
         );
      }
   }

   private Vec3d aqD() {
      GetRotationVectorHookEvent getRotationVectorHookEvent = new GetRotationVectorHookEvent(client.player, client.player.getYaw(), client.player.getPitch());
      EventBusImpl.INSTANCE.rC(getRotationVectorHookEvent);
      return getRotationVectorHookEvent.aik() ? getRotationVectorHookEvent.nF() : client.player.getRotationVector();
   }

   public static Vec3d aqE(float f, float f2) {
      float f3 = f * (float) (Math.PI / 180.0);
      float f4 = -f2 * (float) (Math.PI / 180.0);
      float f5 = MathHelper.cos(f4);
      float f6 = MathHelper.sin(f4);
      float f7 = MathHelper.cos(f3);
      float f8 = MathHelper.sin(f3);
      return new Vec3d(f6 * f7, -f8, f5 * f7);
   }

   public static double aqF(ClientPlayerEntity player2) {
      if (player2.getVelocity().y <= 0.0) {
         RegistryEntry registryEntry = StatusEffects.SLOW_FALLING;
         if (player2.hasStatusEffect(registryEntry)) {
            return Math.min(player2.getFinalGravity(), 0.01);
         }
      }

      return player2.getFinalGravity();
   }

   public static float aqG(Vec3d vec2) {
      return (float)Math.toDegrees(Math.asin(-vec2.y));
   }

   public static Vec3d aqH(ClientPlayerEntity player2, Vec3d vec2, Vec3d vec3) {
      float f = aqG(vec3) * (float) (Math.PI / 180.0);
      double d = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z);
      double d2 = vec2.horizontalLength();
      double d3 = aqF(player2);
      double d4 = MathHelper.square(Math.cos(f));
      double d5 = vec2.y + d3 * (d4 * 0.75 - 1.0);
      Vec3d vec4 = new Vec3d(vec2.x, d5, vec2.z);
      if (d5 < 0.0 && d > 0.0) {
         double d6 = d5 * -0.1 * d4;
         Vec3d temp = vec4;
         double temp2 = vec3.x * d6 / d;
         double d7 = vec3.z * d6 / d;
         double d8 = temp2;
         vec4 = temp.add(d8, d6, d7);
      }

      if (f < 0.0F && d > 0.0) {
         double d9 = d2 * -MathHelper.sin(f) * 0.04;
         Vec3d vec5 = vec4;
         double d10 = -vec3.x * d9 / d;
         double temp3 = d9 * 3.2;
         double d11 = -vec3.z * d9 / d;
         double d12 = temp3;
         double d13 = d10;
         vec4 = vec5.add(d13, d12, d11);
      }

      if (d > 0.0) {
         double d14 = d2 / d;
         Vec3d vec6 = vec4;
         double d15 = (vec3.x * d14 - vec4.x) * 0.1;
         double d16 = (vec3.z * d14 - vec4.z) * 0.1;
         double d17 = d15;
         vec4 = vec6.add(d17, 0.0, d16);
      }

      return vec4.multiply(0.99, 0.98, 0.99);
   }

   private boolean aqI() {
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

   private Boolean isFactorVisible() {
      boolean flag;
      if (this.mode.atx() == RocketBoostNormalDirectMode.Normal) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSpeedVisible() {
      boolean flag;
      if (this.mode.atx() == RocketBoostNormalDirectMode.Normal) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBaseSpeedVisible() {
      boolean flag;
      if (this.mode.atx() == RocketBoostNormalDirectMode.Normal) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isVelocityVisible() {
      boolean flag;
      if (this.mode.atx() == RocketBoostNormalDirectMode.Grim) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isCompensationVisible() {
      boolean flag;
      if (this.mode.atx() == RocketBoostNormalDirectMode.Grim) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isNumberSetting2Visible() {
      boolean flag;
      if (this.mode.atx() == RocketBoostNormalDirectMode.Grim) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isNumberSetting1Visible() {
      boolean flag;
      if (this.mode.atx() != RocketBoostNormalDirectMode.Direct && this.mode.atx() != RocketBoostNormalDirectMode.Grim) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }
}
