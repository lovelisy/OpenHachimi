package pw.hachimi.client.manager;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.ColorHelper.Argb;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.render.RenderWorldEvent2;
import pw.hachimi.client.mixin.TextRendererMixin;
import pw.hachimi.client.mixin.WorldRendererMixin;
import pw.hachimi.client.module.client.Font;
import pw.hachimi.client.module.render.Nametags;
import pw.hachimi.client.render.ScreenRectManager;
import pw.hachimi.client.utils.chat.StyleFormattingUtils;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.FontLoader;
import pw.hachimi.client.utils.render.RenderUtilsHelper;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;
import java.util.HashSet;

public class RenderUtils {
   public static final Tessellator a_ = RenderSystem.renderThreadTesselator();
   public static final ScreenRectManager b = new ScreenRectManager();
   private static final Set c = new HashSet();
   private static Immediate immediate;
   private static final long a = 0L;

   public RenderUtils() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   public void onRenderWorldEvent2(RenderWorldEvent2 renderWorldEvent2) {
      if (!c.isEmpty()) {
         if (immediate == null) {
            immediate = Mc.client.getBufferBuilders().getEntityVertexConsumers();
         }

         if (Font.kT().awg()) {
            FontLoader.fontRenderer2.aaY();
         }

         RenderSystem.disableDepthTest();
         RenderSystem.enablePolygonOffset();
         RenderSystem.polygonOffset(1.0F, -2000000.0F);
         Iterator iterator2 = c.iterator();

         while (iterator2.hasNext()) {
            ((Runnable)iterator2.next()).run();
         }

         if (Font.kT().awg()) {
            FontLoader.fontRenderer2.aba();
         } else {
            immediate.draw();
         }

         RenderSystem.enableDepthTest();
         RenderSystem.polygonOffset(1.0F, 2000000.0F);
         RenderSystem.disablePolygonOffset();
         c.clear();
      }
   }

   public static void acu(Runnable runnable) {
      c.add(runnable);
   }

   public static void acv(MatrixStack matrices, String str, Vec3d vec, int n) {
      acw(matrices, str, vec.getX(), vec.getY(), vec.getZ(), n);
   }

   public static void acw(MatrixStack matrices, String str, double d, double d2, double d3, int n) {
      Vec3d vec = Mc.client.gameRenderer.getCamera().getPos();
      Nametags nametags = Nametags.HF();
      float f = (float)(
         -0.025F * nametags.HV()
            + (
               nametags.HW() && vec.squaredDistanceTo(d, d2, d3) > nametags.HY() * nametags.HY()
                  ? (Math.sqrt(vec.squaredDistanceTo(d, d2, d3)) - nametags.HY().floatValue()) * -0.0025F * nametags.HX().floatValue()
                  : 0.0
            )
      );
      Set temp = c;
      final int n2 = n;
      final float f2 = f;
      final double d4 = d3;
      final double d5 = d2;
      final double d6 = d;
      final String str2 = str;
      final MatrixStack matrices2 = matrices;
      Runnable runnable = new Runnable() {
         @Override
         public void run() {
            RenderUtils.acK(matrices2, str2, d6, d5, d4, f2, n2);
         }
      };
      temp.add(runnable);
   }

   public static void acx(MatrixStack matrices, String str, double d, double d2, double d3, float f, int n) {
      Camera camera = Mc.client.gameRenderer.getCamera();
      matrices.push();
      double temp = d - camera.getPos().x;
      double temp2 = d2 + (f / -0.025F - 1.0F) / 16.0F - camera.getPos().y;
      double d4 = d3 - camera.getPos().z;
      double d5 = temp2;
      double d6 = temp;
      matrices.translate(d6, d5, d4);
      RotationAxis rotationAxis = RotationAxis.POSITIVE_Y;
      float f2 = -camera.getYaw();
      Quaternionf quaternionf = rotationAxis.rotationDegrees(f2);
      matrices.multiply(quaternionf);
      RotationAxis rotationAxis2 = RotationAxis.POSITIVE_X;
      float f3 = camera.getPitch();
      Quaternionf quaternionf2 = rotationAxis2.rotationDegrees(f3);
      matrices.multiply(quaternionf2);
      matrices.scale(f, f, -1.0F);
      float f4 = Font.kT().awg() ? FontLoader.fontRenderer2.abc(str) / 4.0F : Mc.client.textRenderer.getWidth(str) / 2.0F;
      float f5 = -f4;
      matrices.translate(f5, 0.0F, 0.0F);
      if (Font.kT().awg()) {
         matrices.scale(0.5F, 0.5F, 0.5F);
         FontLoader.fontRenderer2.aaR(matrices, str, 0.0, 0.0, n);
      } else {
         ((TextRendererMixin)Mc.client.textRenderer)
            .hookDrawLayer(str, 0.0F, 0.0F, Nametags.HG(n), true, matrices.peek().getPositionMatrix(), immediate, TextLayerType.SEE_THROUGH, 0, 15728880);
         ((TextRendererMixin)Mc.client.textRenderer)
            .hookDrawLayer(str, 0.0F, 0.0F, Nametags.HG(n), false, matrices.peek().getPositionMatrix(), immediate, TextLayerType.SEE_THROUGH, 0, 15728880);
      }

      matrices.pop();
   }

   public static boolean acy(Box box) {
      return ((WorldRendererMixin)Mc.client.worldRenderer).getFrustum().isVisible(box);
   }

   public static void acz(MatrixStack matrices, double d, double d2, double d3, double d4, int n) {
      acA(matrices, d, d2, d3, d4, n);
   }

   public static void acA(MatrixStack matrices, double d, double d2, double d3, double d4, int n) {
      d3 += d;
      d4 += d2;
      Matrix4f matrix4f = matrices.peek().getPositionMatrix();
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

      float f = Argb.getAlpha(n) / 255.0F;
      float f2 = Argb.getRed(n) / 255.0F;
      float f3 = Argb.getGreen(n) / 255.0F;
      float f4 = Argb.getBlue(n) / 255.0F;
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      VertexFormat vertexFormat = VertexFormats.POSITION_COLOR;
      DrawMode drawMode = DrawMode.QUADS;
      BufferBuilder bufferBuilder = a_.begin(drawMode, vertexFormat);
      float temp = (float)d;
      float f5 = (float)d2;
      float f6 = temp;
      bufferBuilder.vertex(matrix4f, f6, f5, 0.0F).color(f2, f3, f4, f);
      temp = (float)d;
      float f7 = (float)d4;
      float f8 = temp;
      bufferBuilder.vertex(matrix4f, f8, f7, 0.0F).color(f2, f3, f4, f);
      temp = (float)d3;
      float f9 = (float)d4;
      float f10 = temp;
      bufferBuilder.vertex(matrix4f, f10, f9, 0.0F).color(f2, f3, f4, f);
      temp = (float)d3;
      float f11 = (float)d2;
      float f12 = temp;
      bufferBuilder.vertex(matrix4f, f12, f11, 0.0F).color(f2, f3, f4, f);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      RenderSystem.disableBlend();
   }

   public static void acB(double d, double d2, double d3, double d4) {
      d = Math.floor(d);
      d2 = Math.floor(d2);
      d3 = Math.ceil(d3);
      d4 = Math.ceil(d4);
      acD(b.KN(new ScreenRect((int)d, (int)d2, (int)(d3 - d), (int)(d4 - d2))));
   }

   public static void acC() {
      acD(b.KO());
   }

   private static void acD(ScreenRect screenRect) {
      if (screenRect != null) {
         Window window = Mc.client.getWindow();
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

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static void acE(DrawContext context, String str, float f, float f2, int n) {
      switch (RenderUtilsHelper.a[Font.kT().lb().ordinal()]) {
         case 1:
            if (Font.kT().awg() && FontLoader.fontRenderer != null) {
               FontLoader.fontRenderer
                  .aaX(
                     context.getMatrices(),
                     str,
                     f,
                     f2,
                     ColorUtils.iv(n) / 255.0F,
                     ColorUtils.iw(n) / 255.0F,
                     ColorUtils.ix(n) / 255.0F,
                     ColorUtils.iy(n) / 255.0F
                  );
            } else {
               double d = Font.kT().la();
               int n2 = (new Color(
                     (int)(ColorUtils.iv(n) * d), (int)(ColorUtils.iw(n) * d), (int)(ColorUtils.ix(n) * d), ColorUtils.iy(n)
                  ))
                  .getRGB();
               float f3 = Font.kT().kY();
               int n3 = -1;
               int n4 = -1;

               do {
                  context.getMatrices().push();
                  MatrixStack temp = context.getMatrices();
                  float temp2 = f3 * -1;
                  float f4 = f3 * n4;
                  float f5 = temp2;
                  temp.translate(f5, f4, 0.0F);
                  ((TextRendererMixin)Mc.client.textRenderer)
                     .hookDrawLayer(
                        StyleFormattingUtils.Eq(str),
                        (int)f,
                        (int)f2,
                        n2,
                        false,
                        context.getMatrices().peek().getPositionMatrix(),
                        context.getVertexConsumers(),
                        TextLayerType.NORMAL,
                        0,
                        15728880
                     );
                  context.getMatrices().pop();
               } while (++n4 <= 1);

               while (++n3 <= 1) {
                  for (int n5 = -1; n5 <= 1; n5++) {
                     if (n3 != 0 || n5 != 0) {
                        context.getMatrices().push();
                        MatrixStack matrices = context.getMatrices();
                        float f6 = f3 * n3;
                        float f7 = f3 * n5;
                        float f8 = f6;
                        matrices.translate(f8, f7, 0.0F);
                        ((TextRendererMixin)Mc.client.textRenderer)
                           .hookDrawLayer(
                              StyleFormattingUtils.Eq(str),
                              (int)f,
                              (int)f2,
                              n2,
                              false,
                              context.getMatrices().peek().getPositionMatrix(),
                              context.getVertexConsumers(),
                              TextLayerType.NORMAL,
                              0,
                              15728880
                           );
                        context.getMatrices().pop();
                     }
                  }
               }

               context.getMatrices().push();
               context.getMatrices().translate(0.0F, 0.0F, 0.0F);
               TextRenderer textRenderer2 = Mc.client.textRenderer;
               float f9 = (int)f;
               float f10 = (int)f2;
               Matrix4f temp3 = context.getMatrices().peek().getPositionMatrix();
               Immediate temp4 = context.getVertexConsumers();
               TextLayerType textLayerType = TextLayerType.NORMAL;
               Immediate immediate2 = temp4;
               Matrix4f matrix4f = temp3;
               float f11 = f10;
               float f12 = f9;
               textRenderer2.draw(str, f12, f11, n, false, matrix4f, (VertexConsumerProvider)immediate2, textLayerType, 0, 15728880, false);
               context.draw();
               context.getMatrices().pop();
            }
            break;
         case 2:
            if (Font.kT().awg() && FontLoader.fontRenderer != null) {
               FontLoader.fontRenderer.aaR(context.getMatrices(), str, f, f2, n);
            } else {
               int n6 = (int)f;
               int n7 = (int)f2;
               int n8 = n6;
               TextRenderer textRenderer3 = Mc.client.textRenderer;
               context.drawText(textRenderer3, str, n8, n7, n, true);
            }
            break;
         case 3:
            if (Font.kT().awg() && FontLoader.fontRenderer != null) {
               FontLoader.fontRenderer.aaS(context.getMatrices(), str, f, f2, n);
            } else {
               int temp5 = (int)f;
               int n9 = (int)f2;
               int n10 = temp5;
               TextRenderer textRenderer4 = Mc.client.textRenderer;
               context.drawText(textRenderer4, str, n10, n9, n, false);
            }
      }
   }

   public static int acF(String str) {
      return Font.kT().awg() && FontLoader.fontRenderer != null ? (int)FontLoader.fontRenderer.abc(str) : Mc.client.textRenderer.getWidth(str);
   }

   public static int acG() {
      return Font.kT().awg() && FontLoader.fontRenderer != null ? (int)FontLoader.fontRenderer.abe() : 9;
   }

   public static void acH(MatrixStack matrices, float f, float f2, float f3, float f4, float f5, Color color2) {
      acI(matrices, color2, f, f2, f3 + f, f4 + f2, f5);
   }

   public static void acI(MatrixStack matrices, Color color2, double d, double d2, double d3, double d4, double d5) {
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      RenderSystem.enableBlend();
      acJ(
         matrices.peek().getPositionMatrix(),
         color2.getRed() / 255.0F,
         color2.getGreen() / 255.0F,
         color2.getBlue() / 255.0F,
         color2.getAlpha() / 255.0F,
         d,
         d2,
         d3,
         d4,
         d5
      );
      RenderSystem.disableBlend();
   }

   public static void acJ(Matrix4f matrix4f, float f, float f2, float f3, float f4, double d, double d2, double d3, double d4, double d5) {
      Tessellator temp = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_COLOR;
      DrawMode drawMode = DrawMode.TRIANGLE_FAN;
      BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
      double[][] ds = new double[][]{
         {d3 - d5, d4 - d5, d5}, {d3 - d5, d2 + d5, d5}, {d + d5, d2 + d5, d5}, {d + d5, d4 - d5, d5}
      };
      int n = 0;
      double[] ds2 = ds[0];
      double d6 = ds2[2];
      double d7 = 0 * 90.0;

      while (true) {
         float f5 = (float)Math.toRadians(d7);
         float f6 = (float)(Math.sin(f5) * d6);
         float f7 = (float)(Math.cos(f5) * d6);
         float temp2 = (float)ds2[0] + f6;
         float f8 = (float)ds2[1] + f7;
         float f9 = temp2;
         bufferBuilder.vertex(matrix4f, f9, f8, 0.0F).color(f, f2, f3, f4);

         for (d7 += 22.5; !(d7 < 90.0 + n * 90.0); d7 = n * 90.0) {
            float f10 = (float)Math.toRadians(90.0 + n * 90.0);
            float f11 = (float)(Math.sin(f10) * d6);
            f5 = (float)(Math.cos(f10) * d6);
            temp2 = (float)ds2[0] + f11;
            float f12 = (float)ds2[1] + f5;
            float f13 = temp2;
            bufferBuilder.vertex(matrix4f, f13, f12, 0.0F).color(f, f2, f3, f4);
            if (++n >= 4) {
               BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
               return;
            }

            ds2 = ds[n];
            d6 = ds2[2];
         }
      }
   }

   static void acK(MatrixStack matrices, String str, double d, double d2, double d3, float f, int n) {
      acx(matrices, str, d, d2, d3, f, n);
   }
}
