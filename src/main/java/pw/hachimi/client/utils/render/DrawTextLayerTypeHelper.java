package pw.hachimi.client.utils.render;

import java.awt.Color;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.Mc;
import pw.hachimi.client.mixin.TextRendererMixin;
import pw.hachimi.client.model.render.LinesTrianglesMode;
import pw.hachimi.client.model.render.UnidentifiedOption;
import pw.hachimi.client.module.client.Font;
import pw.hachimi.client.render.ColorManager;

public class DrawTextLayerTypeHelper {
   public static DrawTextLayerTypeHelper drawTextLayerTypeHelper;
   public static DrawTextLayerTypeHelper drawTextLayerTypeHelper2;
   public final Vec3dGameRendererHelper vec3dGameRendererHelper;
   public final Vec3dGameRendererHelper vec3dGameRendererHelper2;

   public DrawTextLayerTypeHelper(boolean flag) {
      ColorManager temp = flag ? UnidentifiedUtils10.colorManager2 : UnidentifiedUtils10.colorManager;
      UnidentifiedOption[] unidentifiedOptions = flag
         ? new UnidentifiedOption[]{UnidentifiedOption.Vec2, UnidentifiedOption.Vec2, UnidentifiedOption.Color}
         : new UnidentifiedOption[]{UnidentifiedOption.Vec2, UnidentifiedOption.Color};
      this.vec3dGameRendererHelper = new UnidentifiedHelper16(temp, LinesTrianglesMode.Triangles, unidentifiedOptions);
      this.vec3dGameRendererHelper2 = new UnidentifiedHelper16(UnidentifiedUtils10.colorManager, LinesTrianglesMode.Lines, UnidentifiedOption.Vec2, UnidentifiedOption.Color);
   }

   public static void wV() {
      drawTextLayerTypeHelper = new DrawTextLayerTypeHelper(false);
      drawTextLayerTypeHelper2 = new DrawTextLayerTypeHelper(true);
   }

   public void wW(double d2) {
      this.vec3dGameRendererHelper.d = d2;
   }

   public void wX() {
      UnidentifiedUtils8.alt();
      this.vec3dGameRendererHelper.px();
      this.vec3dGameRendererHelper2.px();
      FontLoader.fontRenderer.aaY();
   }

   public void wY(DrawContext context) {
      this.vec3dGameRendererHelper.pI(context.getMatrices());
      this.vec3dGameRendererHelper2.pI(context.getMatrices());
      context.getVertexConsumers().draw();
      FontLoader.fontRenderer.aba();
      UnidentifiedUtils8.b = true;
   }

   public void wZ(DrawContext context, String str, float f, float f2, int n) {
      if (Font.kT().awg()) {
         FontLoader.fontRenderer.aaR(context.getMatrices(), str, f, f2, n);
      } else {
         ((TextRendererMixin)Mc.client.textRenderer)
            .hookDrawLayer(
               str,
               f,
               f2,
               xj(n),
               true,
               context.getMatrices().peek().getPositionMatrix(),
               context.getVertexConsumers(),
               TextLayerType.SEE_THROUGH,
               0,
               15728880
            );
         ((TextRendererMixin)Mc.client.textRenderer)
            .hookDrawLayer(
               str,
               f,
               f2,
               xj(n),
               false,
               context.getMatrices().peek().getPositionMatrix(),
               context.getVertexConsumers(),
               TextLayerType.SEE_THROUGH,
               0,
               15728880
            );
      }
   }

   public void xa(double d2, double d3, double d4, double d5, double d6, double d7, Color color) {
      this.vec3dGameRendererHelper
         .pE(
            this.vec3dGameRendererHelper.pz(d2, d3).pA(color).pB(),
            this.vec3dGameRendererHelper.pz(d4, d5).pA(color).pB(),
            this.vec3dGameRendererHelper.pz(d6, d7).pA(color).pB()
         );
   }

   public void xb(double d2, double d3, double d4, double d5, Color color) {
      this.vec3dGameRendererHelper2.pC(this.vec3dGameRendererHelper2.pz(d2, d3).pA(color).pB(), this.vec3dGameRendererHelper2.pz(d4, d5).pA(color).pB());
   }

   public void xc(double d2, double d3, double d4, double d5, Color color) {
      int n = this.vec3dGameRendererHelper2.pz(d2, d3).pA(color).pB();
      int n2 = this.vec3dGameRendererHelper2.pz(d2, d3 + d5).pA(color).pB();
      int n3 = this.vec3dGameRendererHelper2.pz(d2 + d4, d3 + d5).pA(color).pB();
      int n4 = this.vec3dGameRendererHelper2.pz(d2 + d4, d3).pA(color).pB();
      this.vec3dGameRendererHelper2.pC(n, n2);
      this.vec3dGameRendererHelper2.pC(n2, n3);
      this.vec3dGameRendererHelper2.pC(n3, n4);
      this.vec3dGameRendererHelper2.pC(n4, n);
   }

   public void xd(double d2, double d3, double d4, double d5, Color color, Color color2, Color color3, Color color4) {
      this.vec3dGameRendererHelper
         .pD(
            this.vec3dGameRendererHelper.pz(d2, d3).pA(color).pB(),
            this.vec3dGameRendererHelper.pz(d2, d3 + d5).pA(color4).pB(),
            this.vec3dGameRendererHelper.pz(d2 + d4, d3 + d5).pA(color3).pB(),
            this.vec3dGameRendererHelper.pz(d2 + d4, d3).pA(color2).pB()
         );
   }

   public void xe(double d2, double d3, double d4, double d5, Color color) {
      this.xd(d2, d3, d4, d5, color, color, color, color);
   }

   public void xf(double d2, double d3, double d4, double d5, Color color) {
      this.vec3dGameRendererHelper
         .pD(
            this.vec3dGameRendererHelper.pz(d2, d3).pz(0.0, 0.0).pA(color).pB(),
            this.vec3dGameRendererHelper.pz(d2, d3 + d5).pz(0.0, 1.0).pA(color).pB(),
            this.vec3dGameRendererHelper.pz(d2 + d4, d3 + d5).pz(1.0, 1.0).pA(color).pB(),
            this.vec3dGameRendererHelper.pz(d2 + d4, d3).pz(1.0, 0.0).pA(color).pB()
         );
   }

   public void xg(double d2, double d3, double d4, double d5, UnidentifiedUtils9 unidentifiedUtils9, Color color) {
      this.vec3dGameRendererHelper
         .pD(
            this.vec3dGameRendererHelper.pz(d2, d3).pz(unidentifiedUtils9.a_, unidentifiedUtils9.b).pA(color).pB(),
            this.vec3dGameRendererHelper.pz(d2, d3 + d5).pz(unidentifiedUtils9.a_, unidentifiedUtils9.d).pA(color).pB(),
            this.vec3dGameRendererHelper.pz(d2 + d4, d3 + d5).pz(unidentifiedUtils9.c, unidentifiedUtils9.d).pA(color).pB(),
            this.vec3dGameRendererHelper.pz(d2 + d4, d3).pz(unidentifiedUtils9.c, unidentifiedUtils9.b).pA(color).pB()
         );
   }

   public void xh(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, Color color) {
      double d11 = Math.toRadians(d6);
      double d12 = Math.cos(d11);
      double d13 = Math.sin(d11);
      double d14 = d2 + d4 / 2.0;
      double d15 = d3 + d5 / 2.0;
      double d16 = (d2 - d14) * d12 - (d3 - d15) * d13 + d14;
      double d17 = (d3 - d15) * d12 + (d2 - d14) * d13 + d15;
      int n = this.vec3dGameRendererHelper.pz(d16, d17).pz(d7, d8).pA(color).pB();
      double d18 = (d2 - d14) * d12 - (d3 + d5 - d15) * d13 + d14;
      double d19 = (d3 + d5 - d15) * d12 + (d2 - d14) * d13 + d15;
      int n2 = this.vec3dGameRendererHelper.pz(d18, d19).pz(d7, d10).pA(color).pB();
      double d20 = (d2 + d4 - d14) * d12 - (d3 + d5 - d15) * d13 + d14;
      double d21 = (d3 + d5 - d15) * d12 + (d2 + d4 - d14) * d13 + d15;
      int n3 = this.vec3dGameRendererHelper.pz(d20, d21).pz(d9, d10).pA(color).pB();
      double d22 = (d2 + d4 - d14) * d12 - (d3 - d15) * d13 + d14;
      double d23 = (d3 - d15) * d12 + (d2 + d4 - d14) * d13 + d15;
      int n4 = this.vec3dGameRendererHelper.pz(d22, d23).pz(d9, d8).pA(color).pB();
      this.vec3dGameRendererHelper.pD(n, n2, n3, n4);
   }

   public void xi(double d2, double d3, double d4, double d5, double d6, UnidentifiedUtils9 unidentifiedUtils9, Color color) {
      this.xh(d2, d3, d4, d5, d6, unidentifiedUtils9.a_, unidentifiedUtils9.b, unidentifiedUtils9.c, unidentifiedUtils9.d, color);
   }

   public static int xj(int n) {
      return (n & -67108864) == 0 ? n & 16777215 | 83886080 : n;
   }
}
