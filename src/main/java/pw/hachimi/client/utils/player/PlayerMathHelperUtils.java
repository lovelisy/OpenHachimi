package pw.hachimi.client.utils.player;

import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.Mc;
import pw.hachimi.client.utils.world.EntityBoxUtils;

public class PlayerMathHelperUtils {
   private static float a;
   private static float b;
   private static float c;
   private static float d;
   private static float e;
   private static float f;
   public static int g;

   public static void wH(float f2, float f3) {
      g = Mc.client.player.age;
      d = c;
      b = a;
      f = e;
      e = wI(f2, f);
      c = f2;
      a = f3;
   }

   private static float wI(float f2, float f3) {
      float f4 = f3;
      double d2 = Mc.client.player.getX() - Mc.client.player.prevX;
      double d3 = Mc.client.player.getZ() - Mc.client.player.prevZ;
      if (d2 * d2 + d3 * d3 > 0.0025000002F) {
         float f5 = (float)MathHelper.atan2(d3, d2) * (180.0F / (float)Math.PI) - 90.0F;
         float f6 = MathHelper.abs(MathHelper.wrapDegrees(f2) - f5);
         if (95.0F < f6 && f6 < 265.0F) {
            f4 = f5 - 180.0F;
         } else {
            f4 = f5;
         }
      }

      if (Mc.client.player.handSwingProgress > 0.0F) {
         f4 = f2;
      }

      f4 = f3 + MathHelper.wrapDegrees(f4 - f3) * 0.3F;
      float f7 = MathHelper.wrapDegrees(f2 - f4);
      if (f7 < -75.0F) {
         f7 = -75.0F;
      } else if (f7 >= 75.0F) {
         f7 = 75.0F;
      }

      f4 = f2 - f7;
      if (f7 * f7 > 2500.0F) {
         f4 += f7 * 0.2F;
      }

      return f4;
   }

   public static float wJ(float f2) {
      return EntityBoxUtils.BB(f, e, f2);
   }

   public static float wK(float f2) {
      return EntityBoxUtils.BB(d, c, f2);
   }

   public static float wL(float f2) {
      return EntityBoxUtils.BB(b, a, f2);
   }

   public static float wM() {
      return a;
   }

   public static float wN() {
      return c;
   }

   public static float wO() {
      return e;
   }

   public static float wP() {
      return b;
   }

   public static float wQ() {
      return d;
   }

   public static float wR() {
      return f;
   }
}
