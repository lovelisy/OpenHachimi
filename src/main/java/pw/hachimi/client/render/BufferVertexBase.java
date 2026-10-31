package pw.hachimi.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.Window;
import net.minecraft.util.math.ColorHelper.Argb;
import org.joml.Matrix4f;
import pw.hachimi.client.gui.DrawScreenScreen;
import pw.hachimi.client.handler.RenderHandler;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.utils.render.UnidentifiedHelper15;

public abstract class BufferVertexBase implements RenderHandler {
   public static MinecraftClient client;
   public float j;
   public float k;
   public float l;
   public float m;

   @Override
   public abstract void aeU(DrawContext var1, int var2, int var3);

   public void ve(DrawContext context, String str, float f, float f2, int n) {
      n = UI.atK().atV(n);
      RenderUtils.acE(context, str, f, f2, n);
   }

   public static String vf(String str) {
      return UI.atK().aud() ? str.toLowerCase() : str;
   }

   public void vg(DrawContext context, int n) {
      vh(context, this.j, this.k, this.l, this.m, n);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static void vh(DrawContext context, double d, double d2, double d3, double d4, int n) {
      switch (UnidentifiedHelper15.a[UI.aum().Qy().ordinal()]) {
         case 1:
            vj(context, d, d2, d3, d4, n);
            break;
         case 2:
            int n2 = n >> 24 & 0xFF;
            int n3 = n >> 16 & 0xFF;
            int n4 = n >> 8 & 0xFF;
            int n5 = n & 0xFF;
            n3 = (int)(n3 * 0.76);
            n4 = (int)(n4 * 0.76);
            n5 = (int)(n5 * 0.76);
            vn(context, d, d2, d3, d4, n, n2 << 24 | n3 << 16 | n4 << 8 | n5);
            break;
         case 3:
            int n6 = n >> 24 & 0xFF;
            int n7 = n >> 16 & 0xFF;
            int n8 = n >> 8 & 0xFF;
            int n9 = n & 0xFF;
            n7 = (int)(n7 * 0.87);
            n8 = (int)(n8 * 0.87);
            n9 = (int)(n9 * 0.87);
            vk(context, d, d2, d3, d4, n, n6 << 24 | n7 << 16 | n8 << 8 | n9);
      }
   }

   public static void vi(DrawContext context, double d, double d2, double d3, double d4, int n) {
      vj(context, d, d2, d3, d4, n);
   }

   public static void vj(DrawContext context, double d, double d2, double d3, double d4, int n) {
      n = UI.atK().atV(n);
      float f = Argb.getAlpha(n) / 255.0F;
      float f2 = Argb.getRed(n) / 255.0F;
      float f3 = Argb.getGreen(n) / 255.0F;
      float f4 = Argb.getBlue(n) / 255.0F;
      if (!(f <= 0.01F)) {
         d3 += d;
         d4 += d2;
         Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
         if (d < d3) {
            double d5 = d;
            d = d3;
            d3 = d5;
         }

         if (d2 < d4) {
            double d6 = d2;
            d2 = d4;
            d4 = d6;
         }

         RenderSystem.enableBlend();
         if (RenderSystem.getShader() != GameRenderer.getPositionColorProgram()) {
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
         }

         Tessellator temp = Tessellator.getInstance();
         VertexFormat vertexFormat = VertexFormats.POSITION_COLOR;
         DrawMode drawMode = DrawMode.QUADS;
         BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
         float temp2 = (float)d;
         float f5 = (float)d2;
         float f6 = temp2;
         bufferBuilder.vertex(matrix4f, f6, f5, 0.0F).color(f2, f3, f4, f);
         temp2 = (float)d;
         float f7 = (float)d4;
         float f8 = temp2;
         bufferBuilder.vertex(matrix4f, f8, f7, 0.0F).color(f2, f3, f4, f);
         temp2 = (float)d3;
         float f9 = (float)d4;
         float f10 = temp2;
         bufferBuilder.vertex(matrix4f, f10, f9, 0.0F).color(f2, f3, f4, f);
         temp2 = (float)d3;
         float f11 = (float)d2;
         float f12 = temp2;
         bufferBuilder.vertex(matrix4f, f12, f11, 0.0F).color(f2, f3, f4, f);
         BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
         RenderSystem.disableBlend();
      }
   }

   protected static void vk(DrawContext context, double d, double d2, double d3, double d4, int n, int n2) {
      vl(context, d, d2, d3, d4, n, n2);
   }

   protected static void vl(DrawContext context, double d, double d2, double d3, double d4, int n, int n2) {
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      Tessellator temp = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_COLOR;
      DrawMode drawMode = DrawMode.QUADS;
      BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
      vm(context.getMatrices().peek().getPositionMatrix(), bufferBuilder, d, d2, d3, d4, n, n2);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      RenderSystem.disableBlend();
   }

   protected static void vm(Matrix4f matrix4f, BufferBuilder bufferBuilder, double d, double d2, double d3, double d4, int n, int n2) {
      d3 += d;
      d4 += d2;
      if (d < d3) {
         double d5 = d;
         d = d3;
         d3 = d5;
      }

      if (d2 < d4) {
         double d6 = d2;
         d2 = d4;
         d4 = d6;
      }

      n = UI.atK().atV(n);
      n2 = UI.atK().atV(n2);
      float f = Argb.getAlpha(n) / 255.0F;
      float f2 = Argb.getRed(n) / 255.0F;
      float f3 = Argb.getGreen(n) / 255.0F;
      float f4 = Argb.getBlue(n) / 255.0F;
      float f5 = Argb.getAlpha(n2) / 255.0F;
      float f6 = Argb.getRed(n2) / 255.0F;
      float f7 = Argb.getGreen(n2) / 255.0F;
      float f8 = Argb.getBlue(n2) / 255.0F;
      float temp = (float)d;
      float f9 = (float)d2;
      float f10 = temp;
      bufferBuilder.vertex(matrix4f, f10, f9, 0.0F).color(f6, f7, f8, f5);
      temp = (float)d;
      float f11 = (float)d4;
      float f12 = temp;
      bufferBuilder.vertex(matrix4f, f12, f11, 0.0F).color(f2, f3, f4, f);
      temp = (float)d3;
      float f13 = (float)d4;
      float f14 = temp;
      bufferBuilder.vertex(matrix4f, f14, f13, 0.0F).color(f2, f3, f4, f);
      temp = (float)d3;
      float f15 = (float)d2;
      float f16 = temp;
      bufferBuilder.vertex(matrix4f, f16, f15, 0.0F).color(f6, f7, f8, f5);
   }

   public static void vn(DrawContext context, double d, double d2, double d3, double d4, int n, int n2) {
      vo(context, d, d2, d3, d4, n, n2);
   }

   protected static void vo(DrawContext context, double d, double d2, double d3, double d4, int n, int n2) {
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      Tessellator temp = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_COLOR;
      DrawMode drawMode = DrawMode.QUADS;
      BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
      vp(context.getMatrices().peek().getPositionMatrix(), bufferBuilder, d, d2, d3, d4, n, n2);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      RenderSystem.disableBlend();
   }

   protected static void vp(Matrix4f matrix4f, BufferBuilder bufferBuilder, double d, double d2, double d3, double d4, int n, int n2) {
      d3 += d;
      d4 += d2;
      if (d < d3) {
         double d5 = d;
         d = d3;
         d3 = d5;
      }

      if (d2 < d4) {
         double d6 = d2;
         d2 = d4;
         d4 = d6;
      }

      n = UI.atK().atV(n);
      n2 = UI.atK().atV(n2);
      float f = Argb.getAlpha(n) / 255.0F;
      float f2 = Argb.getRed(n) / 255.0F;
      float f3 = Argb.getGreen(n) / 255.0F;
      float f4 = Argb.getBlue(n) / 255.0F;
      float f5 = Argb.getAlpha(n2) / 255.0F;
      float f6 = Argb.getRed(n2) / 255.0F;
      float f7 = Argb.getGreen(n2) / 255.0F;
      float f8 = Argb.getBlue(n2) / 255.0F;
      float temp = (float)d;
      float f9 = (float)d2;
      float f10 = temp;
      bufferBuilder.vertex(matrix4f, f10, f9, 0.0F).color(f6, f7, f8, f5);
      temp = (float)d;
      float f11 = (float)d4;
      float f12 = temp;
      bufferBuilder.vertex(matrix4f, f12, f11, 0.0F).color(f6, f7, f8, f5);
      temp = (float)d3;
      float f13 = (float)d4;
      float f14 = temp;
      bufferBuilder.vertex(matrix4f, f14, f13, 0.0F).color(f2, f3, f4, f);
      temp = (float)d3;
      float f15 = (float)d2;
      float f16 = temp;
      bufferBuilder.vertex(matrix4f, f16, f15, 0.0F).color(f2, f3, f4, f);
   }

   public void vq(DrawContext context, float f, float f2, float f3, float f4, int n, int n2, boolean flag) {
      n = UI.atK().atV(n);
      n2 = UI.atK().atV(n2);
      float f5 = (n >> 24 & 0xFF) / 255.0F;
      float f6 = (n >> 16 & 0xFF) / 255.0F;
      float f7 = (n >> 8 & 0xFF) / 255.0F;
      float f8 = (n & 0xFF) / 255.0F;
      float f9 = (n2 >> 24 & 0xFF) / 255.0F;
      float f10 = (n2 >> 16 & 0xFF) / 255.0F;
      float f11 = (n2 >> 8 & 0xFF) / 255.0F;
      float f12 = (n2 & 0xFF) / 255.0F;
      Tessellator temp = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_COLOR;
      DrawMode drawMode = DrawMode.QUADS;
      BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
      Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      if (flag) {
         bufferBuilder.vertex(matrix4f, f, f2, 0.0F).color(f6, f7, f8, f5);
         bufferBuilder.vertex(matrix4f, f, f4, 0.0F).color(f6, f7, f8, f5);
         bufferBuilder.vertex(matrix4f, f3, f4, 0.0F).color(f10, f11, f12, f9);
         bufferBuilder.vertex(matrix4f, f3, f2, 0.0F).color(f10, f11, f12, f9);
      } else {
         bufferBuilder.vertex(matrix4f, f3, f2, 0.0F).color(f6, f7, f8, f5);
         bufferBuilder.vertex(matrix4f, f, f2, 0.0F).color(f6, f7, f8, f5);
         bufferBuilder.vertex(matrix4f, f, f4, 0.0F).color(f10, f11, f12, f9);
         bufferBuilder.vertex(matrix4f, f3, f4, 0.0F).color(f10, f11, f12, f9);
      }

      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      RenderSystem.disableBlend();
   }

   public static void vr(DrawContext context, float f, float f2) {
      vs(context, f, f2);
   }

   public static void vs(DrawContext context, float f, float f2) {
      vt(context, f, f + 11.0F, f2, f2 + 10.0F);
   }

   private static void vt(DrawContext context, float f, float f2, float f3, float f4) {
      vu(context.getMatrices().peek().getPositionMatrix(), f, f2, f3, f4);
   }

   public static void vu(Matrix4f matrix4f, float f, float f2, float f3, float f4) {
      RenderSystem.setShader(GameRenderer::getPositionTexProgram);
      Tessellator temp = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_TEXTURE;
      DrawMode drawMode = DrawMode.QUADS;
      BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
      bufferBuilder.vertex(matrix4f, f, f3, 0.0F).texture(0.0F, 0.0F);
      bufferBuilder.vertex(matrix4f, f, f4, 0.0F).texture(0.0F, 1.0F);
      bufferBuilder.vertex(matrix4f, f2, f4, 0.0F).texture(1.0F, 1.0F);
      bufferBuilder.vertex(matrix4f, f2, f3, 0.0F).texture(1.0F, 0.0F);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
   }

   public float vv() {
      return this.j;
   }

   public float vw() {
      return this.k;
   }

   public float vx() {
      return this.m;
   }

   public void vy(float f) {
      this.m = f;
   }

   public float vz() {
      return this.l;
   }

   public void vA(float f) {
      this.l = f;
   }

   public boolean vB(double d, double d2) {
      return this.vC((float)d, (float)d2);
   }

   public boolean vC(float f, float f2) {
      return this.vD(f, f2, this.j, this.k, this.l, this.m);
   }

   public boolean vD(double d, double d2, double d3, double d4, double d5, double d6) {
      return d >= d3 && d <= d3 + d5 && d2 >= d4 && d2 < d4 + d6;
   }

   public void vE(float f, float f2) {
      this.j = f;
      this.k = f2;
   }

   public void vF(double d, double d2, double d3, double d4) {
      float f = UI.atK().auk();
      if (f != 1.0F) {
         d2 += client.getWindow().getScaledHeight() / 8.0F * (f - 1.0F);
         d4 += client.getWindow().getScaledHeight() / 8.0F * (f - 1.0F);
      }

      d = Math.floor(d * DrawScreenScreen.b);
      d2 = Math.floor(d2 * DrawScreenScreen.b);
      d3 = Math.ceil(d3 * DrawScreenScreen.b);
      d4 = Math.ceil(d4 * DrawScreenScreen.b);
      this.vH(DrawScreenScreen.a.KN(new ScreenRect((int)d, (int)d2, (int)(d3 - d), (int)(d4 - d2))));
   }

   public void vG() {
      this.vH(DrawScreenScreen.a.KO());
   }

   private void vH(ScreenRect screenRect) {
      if (screenRect != null) {
         Window window = client.getWindow();
         int n = window.getFramebufferHeight();
         double d = window.getScaleFactor();
         double d2 = screenRect.getLeft() * d;
         double d3 = n - screenRect.getBottom() * d;
         double d4 = screenRect.width() * d;
         double d5 = screenRect.height() * d;
         RenderSystem.enableScissor((int)d2, (int)d3, Math.max(0, (int)d4), Math.max(0, (int)d5));
      } else {
         RenderSystem.disableScissor();
      }
   }
}
