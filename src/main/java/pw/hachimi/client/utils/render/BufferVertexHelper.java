package pw.hachimi.client.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import pw.hachimi.client.Mc;
import pw.hachimi.client.config.ConfigContainer;
import pw.hachimi.client.event.Event;
import pw.hachimi.client.gui.ScreenDrawScreen;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.client.UI;

public class BufferVertexHelper {
   private final String text;
   private final Identifier id;
   private float c;
   private float d;
   private float e;
   private float f;
   private float g;
   private float h;
   private float i;
   private float j;
   private float k;
   private boolean l;
   private boolean m;
   private boolean n;
   private boolean o;
   private boolean p = true;

   protected BufferVertexHelper(float f2, float f3) {
      this.Yc(f2);
      this.Ye(f3);
      this.Yg();
      this.Yi();
      this.text = "Items";
      this.id = null;
   }

   public static boolean XN(double d2, double d3, double d4, double d5, double d6, double d7) {
      return d2 >= d4 && d2 - d6 <= d4 && d3 >= d5 && d3 - d7 <= d5;
   }

   public static void XO(MatrixStack matrices, float f2, float f3, float f4, float f5, int n2) {
      Matrix4f matrix4f = matrices.peek().getPositionMatrix();
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      Tessellator temp = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_COLOR;
      DrawMode drawMode = DrawMode.DEBUG_LINES;
      BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
      bufferBuilder.vertex(matrix4f, f2, f3, 0.0F).color(n2);
      bufferBuilder.vertex(matrix4f, f4, f5, 0.0F).color(n2);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      RenderSystem.disableBlend();
   }

   public static void XP(MatrixStack matrices, float f2, float f3, float f4, float f5, int n2) {
      Matrix4f matrix4f = matrices.peek().getPositionMatrix();
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      Tessellator temp = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_COLOR;
      DrawMode drawMode = DrawMode.QUADS;
      BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
      float f6 = f3 + f5;
      bufferBuilder.vertex(matrix4f, f2, f6, 0.0F).color(n2);
      float temp2 = f2 + f4;
      float f7 = f3 + f5;
      float f8 = temp2;
      bufferBuilder.vertex(matrix4f, f8, f7, 0.0F).color(n2);
      float f9 = f2 + f4;
      bufferBuilder.vertex(matrix4f, f9, f3, 0.0F).color(n2);
      bufferBuilder.vertex(matrix4f, f2, f3, 0.0F).color(n2);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      RenderSystem.disableBlend();
   }

   public static float XQ() {
      return Mc.client.getCurrentFps() > 5 ? 1.0F / Mc.client.getCurrentFps() : 0.016F;
   }

   public static float XR(float f2, float f3) {
      float f4 = MathHelper.clamp(XQ() * 12.0F, 0.0F, 1.0F);
      return (1.0F - f4) * f2 + f4 * f3;
   }

   public static void XS(MatrixStack matrices, float f2, float f3, float f4, float f5, Color color2) {
      XP(matrices, f2, f3, f4, f5, color2.getRGB());
   }

   public static void XT(MatrixStack matrices, float f2, float f3, float f4, float f5, Color color2, Color color3) {
      Matrix4f matrix4f = matrices.peek().getPositionMatrix();
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      Tessellator temp = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_COLOR;
      DrawMode drawMode = DrawMode.QUADS;
      BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
      float f6 = f3 + f5;
      VertexConsumer vertexConsumer = bufferBuilder.vertex(matrix4f, f2, f6, 0.0F);
      int n2 = color2.getRGB();
      vertexConsumer.color(n2);
      float temp2 = f2 + f4;
      float f7 = f3 + f5;
      float f8 = temp2;
      VertexConsumer vertexConsumer2 = bufferBuilder.vertex(matrix4f, f8, f7, 0.0F);
      int n3 = color2.getRGB();
      vertexConsumer2.color(n3);
      float f9 = f2 + f4;
      VertexConsumer vertexConsumer3 = bufferBuilder.vertex(matrix4f, f9, f3, 0.0F);
      int n4 = color2.getRGB();
      vertexConsumer3.color(n4);
      VertexConsumer vertexConsumer4 = bufferBuilder.vertex(matrix4f, f2, f3, 0.0F);
      int n5 = color2.getRGB();
      vertexConsumer4.color(n5);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      temp = Tessellator.getInstance();
      VertexFormat vertexFormat2 = VertexFormats.POSITION_COLOR;
      DrawMode drawMode2 = DrawMode.DEBUG_LINE_STRIP;
      bufferBuilder = temp.begin(drawMode2, vertexFormat2);
      float f10 = f3 + f5;
      VertexConsumer vertexConsumer5 = bufferBuilder.vertex(matrix4f, f2, f10, 0.0F);
      int n6 = color3.getRGB();
      vertexConsumer5.color(n6);
      temp2 = f2 + f4;
      float f11 = f3 + f5;
      float f12 = temp2;
      VertexConsumer vertexConsumer6 = bufferBuilder.vertex(matrix4f, f12, f11, 0.0F);
      int n7 = color3.getRGB();
      vertexConsumer6.color(n7);
      float f13 = f2 + f4;
      VertexConsumer vertexConsumer7 = bufferBuilder.vertex(matrix4f, f13, f3, 0.0F);
      int n8 = color3.getRGB();
      vertexConsumer7.color(n8);
      VertexConsumer vertexConsumer8 = bufferBuilder.vertex(matrix4f, f2, f3, 0.0F);
      int n9 = color3.getRGB();
      vertexConsumer8.color(n9);
      float f14 = f3 + f5;
      VertexConsumer vertexConsumer9 = bufferBuilder.vertex(matrix4f, f2, f14, 0.0F);
      int n10 = color3.getRGB();
      vertexConsumer9.color(n10);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      RenderSystem.disableBlend();
   }

   public static void XU(MatrixStack matrices, float f2, float f3, float f4, float f5, Color color2, Color color3) {
      Matrix4f matrix4f = matrices.peek().getPositionMatrix();
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      Tessellator temp = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_COLOR;
      DrawMode drawMode = DrawMode.QUADS;
      BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
      VertexConsumer vertexConsumer = bufferBuilder.vertex(matrix4f, f2, f3, 0.0F);
      int n2 = color2.getRGB();
      vertexConsumer.color(n2);
      VertexConsumer vertexConsumer2 = bufferBuilder.vertex(matrix4f, f2, f5, 0.0F);
      int n3 = color2.getRGB();
      vertexConsumer2.color(n3);
      VertexConsumer vertexConsumer3 = bufferBuilder.vertex(matrix4f, f4, f5, 0.0F);
      int n4 = color3.getRGB();
      vertexConsumer3.color(n4);
      VertexConsumer vertexConsumer4 = bufferBuilder.vertex(matrix4f, f4, f3, 0.0F);
      int n5 = color3.getRGB();
      vertexConsumer4.color(n5);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      RenderSystem.disableBlend();
   }

   public void XV(DrawContext context, int n2, int n3) {
      this.j = XR(this.j, this.i);
      Color color2 = new Color(-983868581, true);
      RenderSystem.enableBlend();
      XP(context.getMatrices(), this.c, this.d, this.e + 10.0F, this.f, -1072689136);
      XS(context.getMatrices(), this.c + 0.5F, this.d, this.e + 9.0F, 16.0F, new Color(1593835520, true));
      XU(
         context.getMatrices(),
         this.c + 2.0F,
         this.d + 16.0F,
         this.c + 2.0F + this.e / 2.0F - 2.0F,
         this.d + 16.5F,
         ColorUtils.iE(ColorHelper3.auO().auU(), 0),
         ColorHelper3.auO().auU()
      );
      XU(
         context.getMatrices(),
         this.c + 2.0F + this.e / 2.0F - 2.0F,
         this.d + 16.0F,
         this.c + 2.0F + this.e - 4.0F,
         this.d + 16.5F,
         ColorHelper3.auO().auU(),
         ColorUtils.iE(ColorHelper3.auO().auU(), 0)
      );
      FontLoader.fontRenderer.aaS(context.getMatrices(), this.text, this.c + 4.0F, this.d + 5.5F, -1);
      boolean flag = XN(n2, n3, this.c + this.e - 4.0F, this.d + 3.0F, 10.0, 10.0);
      XT(context.getMatrices(), this.c + this.e - 4.0F, this.d + 3.0F, 10.0F, 10.0F, flag ? new Color(-982026377, true) : new Color(-984131753, true), color2);
      float f2 = (this.Yh() - 35.0F) / this.k;
      boolean flag2 = XN(n2, n3, this.c + this.e, this.d + 19.0F, 6.0, this.Yh() - 34.0F);
      XT(context.getMatrices(), this.c + this.e, this.d + 19.0F, 6.0F, this.Yh() - 34.0F, flag2 ? new Color(1595085587, true) : new Color(1593835520, true), color2);
      XS(
         context.getMatrices(),
         this.c + this.e,
         Math.max(this.d + 19.0F - this.i * f2, this.d + 19.0F),
         6.0F,
         Math.min((this.Yh() - 34.0F) * f2, this.Yh() - 34.0F),
         new Color(-1590611663, true)
      );
      XO(context.getMatrices(), this.c + this.e - 2.0F, this.d + 5.0F, this.c + this.e + 4.0F, this.d + 11.0F, -1);
      XO(context.getMatrices(), this.c + this.e - 2.0F, this.d + 11.0F, this.c + this.e + 4.0F, this.d + 5.0F, -1);
      RenderSystem.disableBlend();
      if (this.o) {
         float f3 = (n3 - this.d - 19.0F) / (this.Yh() - 34.0F);
         this.i = -(f3 * this.k);
         this.i = MathHelper.clamp(this.i, -this.k + (this.Yh() - 40.0F), 0.0F);
      }

      this.m = XN(n2, n3, this.Yb(), this.Yd(), this.Yf(), this.Yh());
      XO(context.getMatrices(), this.Yb() + this.Yf(), this.Yd() + this.Yh() - 3.0F, this.Yb() + this.Yf() + 7.0F, this.Yd() + this.Yh() - 10.0F, color2.getRGB());
      XO(
         context.getMatrices(),
         this.Yb() + this.Yf() + 5.0F,
         this.Yd() + this.Yh() - 3.0F,
         this.Yb() + this.Yf() + 7.0F,
         this.Yd() + this.Yh() - 5.0F,
         color2.getRGB()
      );
      if (Event.d() == null) {
         ConfigContainer.b();
      }
   }

   public void XW(double d2, double d3) {
      if (XN(d2, d3, this.c + this.e - 4.0F, this.d + 3.0F, 10.0, 10.0)) {
         UI.atK().awb();
      } else if (XN(d2, d3, this.c, this.d, this.e, 10.0)) {
         this.l = true;
         ScreenDrawScreen.bufferVertexHelper2 = this;
         ScreenDrawScreen.bufferVertexHelper = this;
         this.g = (int)(d2 - this.Yb());
         this.h = (int)(d3 - this.Yd());
      } else if (XN(d2, d3, this.c + this.e, this.d + this.f - 10.0F, 10.0, 10.0)) {
         ScreenDrawScreen.bufferVertexHelper = this;
         this.g = (int)(d2 - this.Yf());
         this.h = (int)(d3 - this.Yh());
         this.n = true;
      } else {
         if (XN(d2, d3, this.c + this.e, this.d + 19.0F, 6.0, this.Yh() - 34.0F)) {
            ScreenDrawScreen.bufferVertexHelper = this;
            this.g = (int)(d2 - this.Yf());
            this.h = (int)(d3 - this.Yh());
            this.o = true;
         }
      }
   }

   public void XX(int n2) {
   }

   public void XY(char c2) {
   }

   public void XZ(int n2) {
      if (this.m) {
         this.i += n2 * 2;
         this.i = MathHelper.clamp(this.i, -this.k + (this.Yh() - 40.0F), 0.0F);
      }
   }

   public void Ya() {
      this.l = false;
      this.n = false;
      this.o = false;
      ScreenDrawScreen.bufferVertexHelper2 = null;
   }

   protected float Yb() {
      return this.c;
   }

   protected void Yc(float f2) {
      this.c = f2;
   }

   protected float Yd() {
      return this.d;
   }

   protected void Ye(float f2) {
      this.d = f2;
   }

   protected float Yf() {
      return this.e;
   }

   protected void Yg() {
      this.e = 200.0F;
   }

   protected float Yh() {
      return this.f;
   }

   protected void Yi() {
      this.f = 300.0F;
   }

   protected float Yj() {
      return this.j;
   }

   protected void Yk() {
      this.j = 0.0F;
      this.i = 0.0F;
   }

   protected void Yl(float f2) {
      this.k = f2;
   }

   public boolean Ym() {
      return this.p;
   }

   public void Yn(boolean flag) {
      this.p = flag;
   }

   public Identifier Yo() {
      return this.id;
   }
}
