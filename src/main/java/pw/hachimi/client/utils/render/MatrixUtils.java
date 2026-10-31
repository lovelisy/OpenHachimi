package pw.hachimi.client.utils.render;

import net.minecraft.client.util.math.MatrixStack;
import pw.hachimi.client.model.render.LinesTrianglesMode;
import pw.hachimi.client.model.render.UnidentifiedOption;

public class MatrixUtils {
   private static Vec3dGameRendererHelper vec3dGameRendererHelper;
   private static final MatrixStack b = new MatrixStack();
   private static final long a = 0L;

   private MatrixUtils() {
   }

   public static void Tn() {
      vec3dGameRendererHelper = new Vec3dGameRendererHelper(LinesTrianglesMode.Triangles, UnidentifiedOption.Vec2);
      vec3dGameRendererHelper.px();
      vec3dGameRendererHelper.pD(
         vec3dGameRendererHelper.pz(-1.0, -1.0).pB(),
         vec3dGameRendererHelper.pz(-1.0, 1.0).pB(),
         vec3dGameRendererHelper.pz(1.0, 1.0).pB(),
         vec3dGameRendererHelper.pz(1.0, -1.0).pB()
      );
      vec3dGameRendererHelper.pG();
   }

   public static void To() {
      long l = (132273415924242L ^ a ^ 116211189468243L) >>> 32;
      int n = (int)((132273415924242L ^ a ^ 116211189468243L) << 32 >>> 32);
      vec3dGameRendererHelper.pH(l, n, b);
   }

   public static void Tp() {
      vec3dGameRendererHelper.pI(b);
   }

   public static void Tq() {
      vec3dGameRendererHelper.pJ();
   }
}
