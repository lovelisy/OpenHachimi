package pw.hachimi.client.utils.movement;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pw.hachimi.client.utils.UnidentifiedHelper12;
import pw.hachimi.client.utils.world.BlockPosHelper;

public class UnidentifiedUtils7 {
   private static final float a_ = 0.017453292F;
   private static final float[] b = new float[65536];
   private static final long a = 0L;

   static {
      for (int n = 0; n < 65536; n++) {
         b[n] = (float)StrictMath.sin((double)n * Math.PI * 2.0 / 65536.0);
      }
   }

   @NotNull
   public static UnidentifiedHelper12 akD(long l, @NotNull UnidentifiedHelper12 unidentifiedHelper12, @NotNull UnidentifiedHelper12 unidentifiedHelper122, @NotNull UnidentifiedHelper12 unidentifiedHelper123) {
      BlockPosHelper blockPosHelper = new BlockPosHelper(unidentifiedHelper122, unidentifiedHelper123).pU();
      return akE(unidentifiedHelper12, 61055371958401L, blockPosHelper);
   }

   @Contract("_, _ -> new")
   @NotNull
   public static UnidentifiedHelper12 akE(@NotNull UnidentifiedHelper12 unidentifiedHelper12, long l, @NotNull BlockPosHelper blockPosHelper) {
      return akF(unidentifiedHelper12.Qg(), unidentifiedHelper12.Qh(), 69849346800158L, unidentifiedHelper12.Qi(), blockPosHelper);
   }

   @NotNull
   public static UnidentifiedHelper12 akF(double d2, double d3, long l, double d4, @NotNull BlockPosHelper blockPosHelper) {
      double temp = akK(d2, blockPosHelper.a_, blockPosHelper.d);
      double d5 = blockPosHelper.e;
      double d6 = blockPosHelper.b;
      double temp2 = akK(d3, d6, d5);
      d5 = blockPosHelper.f;
      d6 = blockPosHelper.c;
      return a(temp, temp2, akK(d4, d6, d5));
   }

   public static float akG(float f2) {
      return f2 * (float) (Math.PI / 180.0);
   }

   public static double akH(double d2) {
      return d2 * d2;
   }

   public static float akI(float f2) {
      return b[(int)(f2 * 10430.378F) & 65535];
   }

   public static float akJ(float f2) {
      return b[(int)(f2 * 10430.378F + 16384.0F) & 65535];
   }

   public static double akK(double d2, double d3, double d4) {
      return d2 < d3 ? d3 : Math.min(d2, d4);
   }

   public static int akL(int n, int n2, int n3) {
      return n < n2 ? n2 : Math.min(n, n3);
   }

   public static float akM(float f2, float f3, float f4) {
      return f2 < f3 ? f3 : Math.min(f2, f4);
   }

   private static UnidentifiedHelper12 a(double d2, double d3, double d4) {
      return new UnidentifiedHelper12(d2, d3, d4);
   }
}
