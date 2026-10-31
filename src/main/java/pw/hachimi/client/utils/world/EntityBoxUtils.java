package pw.hachimi.client.utils.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.Mc;

public class EntityBoxUtils {
   public static Vec3d By(Vec3d vec, Vec3d vec2, float f) {
      return new Vec3d(
         vec.x - MathHelper.lerp(f, vec2.x, vec.x), vec.y - MathHelper.lerp(f, vec2.y, vec.y), vec.z - MathHelper.lerp(f, vec2.z, vec.z)
      );
   }

   public static Vec3d Bz(Entity entity2, float f) {
      return new Vec3d(
         entity2.getX() - MathHelper.lerp(f, entity2.lastRenderX, entity2.getX()),
         entity2.getY() - MathHelper.lerp(f, entity2.lastRenderY, entity2.getY()),
         entity2.getZ() - MathHelper.lerp(f, entity2.lastRenderZ, entity2.getZ())
      );
   }

   public static Vec3d BA(Entity entity2, float f) {
      return new Vec3d(
         entity2.prevX + (entity2.getX() - entity2.prevX) * f, entity2.prevY + (entity2.getY() - entity2.prevY) * f, entity2.prevZ + (entity2.getZ() - entity2.prevZ) * f
      );
   }

   public static float BB(float f, float f2, float f3) {
      return f + (f2 - f) * f3;
   }

   public static double BC(double d, double d2, double d3) {
      return d + (d2 - d) * d3;
   }

   public static Box BD(Box box, Box box2) {
      double d;
      if (Mc.client.isPaused()) {
         d = 1.0;
      } else {
         d = Mc.client.getRenderTickCounter().getTickDelta(true);
      }

      double d2 = d;
      return new Box(
         BC(box.minX, box2.minX, d2),
         BC(box.minY, box2.minY, d2),
         BC(box.minZ, box2.minZ, d2),
         BC(box.maxX, box2.maxX, d2),
         BC(box.maxY, box2.maxY, d2),
         BC(box.maxZ, box2.maxZ, d2)
      );
   }

   public static Box BE(Entity entity2) {
      Box box = entity2.getBoundingBox();
      Box temp = entity2.getBoundingBox();
      double temp2 = entity2.prevX - entity2.getX();
      double temp3 = entity2.prevY - entity2.getY();
      double d = entity2.prevZ - entity2.getZ();
      double d2 = temp3;
      double d3 = temp2;
      return BD(temp.offset(d3, d2, d), box);
   }
}
