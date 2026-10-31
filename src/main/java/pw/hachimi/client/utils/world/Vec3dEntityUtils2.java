package pw.hachimi.client.utils.world;

import java.util.function.Predicate;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import pw.hachimi.client.Mc;
import pw.hachimi.client.utils.player.RotationUtils;

public final class Vec3dEntityUtils2 {
   public static HitResult Ry(double d, Vec3d vec, float[] fs) {
      Camera camera = Mc.client.gameRenderer.getCamera();
      Vec3d vec2 = RotationUtils.nS(fs[1], fs[0]);
      double temp = vec2.x * d;
      double temp2 = vec2.y * d;
      double d2 = vec2.z * d;
      double d3 = temp2;
      double d4 = temp;
      Vec3d vec3 = vec.add(d4, d3, d2);
      Box temp3 = new Box(vec, vec);
      Vec3d vec4 = vec2.multiply(d);
      Box box = temp3.stretch(vec4).expand(1.0, 1.0, 1.0);
      Entity entity2 = camera.getFocusedEntity();
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return Vec3dEntityUtils2.RD((Entity)obj);
         }
      };
      double d5 = d * d;
      Predicate predicate2 = predicate;
      return ProjectileUtil.raycast(entity2, vec, vec3, box, predicate2, d5);
   }

   public static HitResult Rz(double d) {
      Camera camera = Mc.client.gameRenderer.getCamera();
      Vec3d vec = camera.getPos();
      Vec3d vec2 = RotationUtils.nS(camera.getPitch(), camera.getYaw());
      Vec3d temp = vec;
      double temp2 = vec2.x * d;
      double temp3 = vec2.y * d;
      double d2 = vec2.z * d;
      double d3 = temp3;
      double d4 = temp2;
      Vec3d vec3 = temp.add(d4, d3, d2);
      Box box = camera.getFocusedEntity().getBoundingBox();
      Vec3d vec4 = vec2.multiply(d);
      Box box2 = box.stretch(vec4).expand(1.0, 1.0, 1.0);
      Entity entity2 = camera.getFocusedEntity();
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return Vec3dEntityUtils2.RC((Entity)obj);
         }
      };
      double d5 = d * d;
      Predicate predicate2 = predicate;
      return ProjectileUtil.raycast(entity2, vec, vec3, box2, predicate2, d5);
   }

   public static HitResult RA(int n, short s, double d, float[] fs) {
      while (s > 0) {
         if (n >= 0) {
            if (s > 0) {
               double d2 = Mc.client.player.getStandingEyeHeight();
               if (n < 0 && s > 0) {
                  return null;
               }

               while (s <= 0) {
                  if (s > 0) {
                     return null;
                  }
               }

               if (s >= 0) {
                  label100:
                  while (n >= 0) {
                     if (s > 0) {
                        Vec3d vec = new Vec3d(Mc.client.player.getX(), Mc.client.player.getY() + d2, Mc.client.player.getZ());

                        while (s <= 0) {
                           if (s > 0) {
                              return null;
                           }
                        }

                        while (n >= 0) {
                           if (s > 0) {
                              return RB(d, vec, fs);
                           }

                           if (n < 0) {
                              break label100;
                           }

                           if (s > 0) {
                              return null;
                           }
                        }
                        break;
                     }

                     if (n < 0) {
                        break;
                     }

                     if (s > 0) {
                        return null;
                     }
                  }
               } else {
                  label131: {
                     while (n >= 0) {
                        if (s > 0) {
                           return null;
                        }

                        if (n < 0) {
                           break label131;
                        }

                        if (s > 0) {
                           return null;
                        }
                     }

                     if (n >= 0) {
                     }

                     while (n >= 0) {
                        if (s > 0) {
                           return null;
                        }
                     }
                  }
               }
            } else if (n >= 0) {
               if (s > 0) {
                  return null;
               }
               continue;
            }
         }

         if (n >= 0) {
         }

         while (s <= 0) {
         }

         return null;
      }

      return s <= 0 ? null : null;
   }

   public static HitResult RB(double d, Vec3d vec, float[] fs) {
      if (!Float.isNaN(fs[0]) && !Float.isNaN(fs[1])) {
         Vec3d vec2 = RotationUtils.nS(fs[1], fs[0]);
         double temp = vec2.x * d;
         double temp2 = vec2.y * d;
         double d2 = vec2.z * d;
         double d3 = temp2;
         double d4 = temp;
         ClientWorld world2 = Mc.client.world;
         RaycastContext raycastContext = new RaycastContext(vec, vec.add(d4, d3, d2), ShapeType.COLLIDER, FluidHandling.NONE, Mc.client.player);
         return world2.raycast(raycastContext);
      } else {
         return null;
      }
   }

   static boolean RC(Entity entity2) {
      boolean flag;
      if (!entity2.isSpectator() && entity2.canHit()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static boolean RD(Entity entity2) {
      boolean flag;
      if (!entity2.isSpectator() && entity2.canHit()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
