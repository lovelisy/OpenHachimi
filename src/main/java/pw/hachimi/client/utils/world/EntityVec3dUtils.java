package pw.hachimi.client.utils.world;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.model.world.EntityTicks;
import pw.hachimi.client.world.Vec3dLivingLivingEntity;
import java.util.concurrent.CopyOnWriteArrayList;

public class EntityVec3dUtils {
   public static List list = new CopyOnWriteArrayList();
   private static final long a = 0L;

   public static Vec3d nT(Entity entity2, int n) {
      return nU(entity2, n, null);
   }

   // $VF: Handled exception range with multiple entry points by splitting it
   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   public static Vec3d nU(Entity entity2, int n, List list2) {
      if (n > 0) {
         Entity temp = entity2;

         try {
            if (!(temp instanceof LivingEntity)) {
               return Vec3d.ZERO;
            }

            Iterator iterator2 = list.iterator();

            while (iterator2.hasNext()) {
               EntityTicks entityTicks = (EntityTicks)iterator2.next();
               if (entityTicks.entity().equals(entity2) && entityTicks.ticks() == n) {
                  Vec3d vec = entityTicks.playerPos();
                  Vec3d vec2 = entity2.getPos();
                  if (vec.equals(vec2)) {
                     return entityTicks.offset();
                  }
               }
            }
         } catch (Exception exception2) {
            exception2.printStackTrace();
            return Vec3d.ZERO;
         }

         temp = entity2;

         double d;
         double d2;
         double d3;
         try {
            d = temp.getX() - entity2.prevX;
            d2 = entity2.getY() - entity2.prevY;
            d3 = entity2.getZ() - entity2.prevZ;
            if (d == 0.0 && d2 == 0.0 && d3 == 0.0) {
               return Vec3d.ZERO;
            }
         } catch (Exception exception3) {
            exception3.printStackTrace();
            return Vec3d.ZERO;
         }

         double d4 = d * d + d3 * d3;

         double d5;
         Vec3dLivingLivingEntity vec3dLivingLivingEntity;
         label114: {
            try {
               d5 = Math.sqrt(d4);
               vec3dLivingLivingEntity = new Vec3dLivingLivingEntity((LivingEntity)entity2);
               if (entity2.isOnGround() || d2 >= 1.0) {
                  Vec3dLivingLivingEntity vec3dLivingLivingEntity2 = vec3dLivingLivingEntity;
                  double temp2 = entity2.getX();
                  double d6 = entity2.getZ();
                  double d7 = entity2.getY();
                  double d8 = temp2;
                  vec3dLivingLivingEntity2.setPosition(d8, d7, d6);
                  vec3dLivingLivingEntity.setVelocity(d, 0.0, d3);
                  break label114;
               }
            } catch (Exception exception4) {
               exception4.printStackTrace();
               return Vec3d.ZERO;
            }

            Vec3dLivingLivingEntity vec3dLivingLivingEntity3 = vec3dLivingLivingEntity;

            try {
               Vec3dLivingLivingEntity vec3dLivingLivingEntity4 = vec3dLivingLivingEntity3;
               double d9 = entity2.prevZ;
               double d10 = entity2.prevY;
               double d11 = entity2.prevX;
               vec3dLivingLivingEntity4.setPosition(d11, d10, d9);
               vec3dLivingLivingEntity.setVelocity(d, d2, d3);
            } catch (Exception exception5) {
               exception5.printStackTrace();
               return Vec3d.ZERO;
            }
         }

         double d12;
         double d13;
         double d14;
         label67: {
            double d15;
            try {
               vec3dLivingLivingEntity.a = true;

               for (int n2 = 0; n2 < n + 1; n2++) {
                  vec3dLivingLivingEntity.Sl();
                  if (list2 != null) {
                     Vec3d vec3 = vec3dLivingLivingEntity.getPos();
                     list2.add(vec3);
                  }
               }

               d13 = vec3dLivingLivingEntity.getX() - entity2.getX();
               d15 = vec3dLivingLivingEntity.getY() - entity2.getY();
               d12 = vec3dLivingLivingEntity.getZ() - entity2.getZ();
               if (d2 == 0.0 && !entity2.isOnGround()) {
                  d14 = 0.0;
                  break label67;
               }
            } catch (Exception exception6) {
               exception6.printStackTrace();
               return Vec3d.ZERO;
            }

            d14 = d15;
         }

         Vec3d vec4;
         try {
            vec4 = new Vec3d(d13, d14, d12);
            List list3 = list;
            EntityTicks entityTicks2 = new EntityTicks(entity2, n, entity2.getPos(), vec4, d5);
            list3.add(entityTicks2);
         } catch (Exception exception7) {
            exception7.printStackTrace();
            return Vec3d.ZERO;
         }

         return vec4;
      } else {
         return Vec3d.ZERO;
      }
   }
}
