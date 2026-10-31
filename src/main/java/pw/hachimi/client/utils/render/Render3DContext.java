package pw.hachimi.client.utils.render;

import net.minecraft.client.util.math.MatrixStack;
import pw.hachimi.client.render.BoxRenderer;
import pw.hachimi.client.utils.render.Render3DContext;

public class Render3DContext {
   private static final Render3DContext INSTANCE = new Render3DContext();
   private static boolean a_;
   private static long b;
   public MatrixStack matrices;
   public BoxRenderer boxRenderer;
   public double e;
   public float f;
   public double g;
   public double h;
   public double i;
   private static final long a = 0L;

   public static Render3DContext afa(MatrixStack matrices2, BoxRenderer boxRenderer2, float f2, double d, double d2, double d3) {
      INSTANCE.matrices = matrices2;
      INSTANCE.boxRenderer = boxRenderer2;
      long l = System.currentTimeMillis();
      INSTANCE.e = l - b;
      b = l;
      INSTANCE.f = f2;
      INSTANCE.g = d;
      INSTANCE.h = d2;
      INSTANCE.i = d3;
      return INSTANCE;
   }
}
