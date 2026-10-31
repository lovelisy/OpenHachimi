package pw.hachimi.client.utils.render;

import java.awt.Color;

public class ColorUtils {
   public static boolean a;

   public static int iv(int n) {
      return n >> 16 & 0xFF;
   }

   public static int iw(int n) {
      return n >> 8 & 0xFF;
   }

   public static int ix(int n) {
      return n & 0xFF;
   }

   public static int iy(int n) {
      return n >> 24 & 0xFF;
   }

   public static Color iz(float f, float f2, float f3, float f4) {
      if (f2 < 0.0F || f2 > 100.0F) {
         throw new IllegalArgumentException("Color parameter outside of expected range - Saturation");
      }

      if (f3 < 0.0F || f3 > 100.0F) {
         throw new IllegalArgumentException("Color parameter outside of expected range - Lightness");
      }

      if (!(f4 < 0.0F) && !(f4 > 1.0F)) {
         f %= 360.0F;
         float f5;
         if (f3 < 0.5) {
            f5 = f3 * (1.0F + f2);
         } else {
            float f6;
            f5 = (f3 /= 100.0F) + (f6 = f2 / 100.0F) - f6 * f3;
         }

         f2 = 2.0F * f3 - f5;
         float f7;
         f3 = Math.max(0.0F, iB(f2, f5, (f7 = f / 360.0F) + 0.33333334F));
         float f8 = Math.max(0.0F, iB(f2, f5, f7));
         f2 = Math.max(0.0F, iB(f2, f5, f7 - 0.33333334F));
         f3 = Math.min(f3, 1.0F);
         f8 = Math.min(f8, 1.0F);
         f2 = Math.min(f2, 1.0F);
         return new Color(f3, f8, f2, f4);
      } else {
         throw new IllegalArgumentException("Color parameter outside of expected range - Alpha");
      }
   }

   public static Color iA(float f, Color color, Color color2) {
      float f2 = color.getRed() / 255.0F;
      float f3 = color.getGreen() / 255.0F;
      float f4 = color.getBlue() / 255.0F;
      float f5 = color.getAlpha() / 255.0F;
      float f6 = color2.getRed() / 255.0F;
      float f7 = color2.getGreen() / 255.0F;
      float f8 = color2.getBlue() / 255.0F;
      float f9 = color2.getAlpha() / 255.0F;
      return new Color(
         f2 * f + f6 * (1.0F - f), f3 * f + f7 * (1.0F - f), f4 * f + f8 * (1.0F - f), f5 * f + f9 * (1.0F - f)
      );
   }

   private static float iB(float f, float f2, float f3) {
      if (f3 < 0.0F) {
         f3++;
      }

      if (f3 > 1.0F) {
         f3--;
      }

      if (6.0F * f3 < 1.0F) {
         return f + (f2 - f) * 6.0F * f3;
      } else if (2.0F * f3 < 1.0F) {
         return f2;
      } else {
         return 3.0F * f3 < 2.0F ? f + (f2 - f) * 6.0F * (0.6666667F - f3) : f;
      }
   }

   public static Color iC(Color color, double d) {
      float[] fs = new float[3];
      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fs);
      double temp = System.currentTimeMillis() * 1.0 % 2000.0;
      Float.floatToIntBits(0.0013786979F);
      Float.intBitsToFloat(1148846080);
      temp /= 1000.0;
      double temp2 = d / 10.0;
      Float.floatToIntBits(0.09192204F);
      Float.intBitsToFloat(1073741824);
      temp += temp2 * 2.0;
      Float.floatToIntBits(0.7858098F);
      Float.intBitsToFloat(1073741824);
      temp %= 2.0;
      Float.floatToIntBits(6.46708F);
      Float.intBitsToFloat(1065353216);
      double d2 = Math.abs(temp - 1.0);
      Float.floatToIntBits(18.996923F);
      Float.intBitsToFloat(1056964608);
      Float.floatToIntBits(2.7958195F);
      Float.intBitsToFloat(1056964608);
      d2 = 0.5 + 0.5 * d2;
      float[] fs2 = fs;
      Float.floatToIntBits(0.8992331F);
      Float.intBitsToFloat(1073741824);
      fs2[2] = (float)(d2 % 2.0);
      return new Color(Color.HSBtoRGB(fs[0], fs[1], fs[2]));
   }

   public static Color iD(Color color, Color color2, double d) {
      d = Math.clamp(d, 0.0, 1.0);
      int n = color.getRed();
      int n2 = color.getGreen();
      int n3 = color.getBlue();
      int n4 = color.getAlpha();
      int n5 = color2.getRed();
      int n6 = color2.getGreen();
      int n7 = color2.getBlue();
      int n8 = color2.getAlpha();
      return new Color(
         Math.min((int)(n + (n5 - n) * d), 255),
         Math.min((int)(n2 + (n6 - n2) * d), 255),
         Math.min((int)(n3 + (n7 - n3) * d), 255),
         Math.min((int)(n4 + (n8 - n4) * d), 255)
      );
   }

   public static Color iE(Color color, int n) {
      n = Math.clamp(n, 0, 255);
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
   }

   public static int iF(int n, float f) {
      return iG(n, (int)(255.0F * f));
   }

   public static int iG(int n, int n2) {
      int n3 = 0xFF & n >> 16;
      int n4 = 0xFF & n;
      int n5 = 0xFF & n >> 8;
      return n2 << 24 | n3 << 16 | n5 << 8 | n4;
   }

   public static int iH(int n, float f) {
      if (f == 1.0F) {
         return n;
      }

      float f2 = n >> 24 & 0xFF;
      f = Math.clamp(f, 0.0F, 1.0F);
      return Math.max(10, (int)(f2 * f)) << 24 | n & 16777215;
   }
}
