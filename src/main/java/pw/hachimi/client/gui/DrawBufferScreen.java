package pw.hachimi.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import pw.hachimi.client.Mc;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.LeftMiddleMode;
import pw.hachimi.client.model.render.TopMiddleMode;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.client.HUD;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.client.render.BufferVertexBase;
import pw.hachimi.client.render.DrawBase;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.DrawHelper;
import pw.hachimi.client.utils.render.DrawHelper2;
import pw.hachimi.client.utils.render.DrawMatrixHelper;
import pw.hachimi.client.utils.render.MathHelperHelper;
import pw.hachimi.client.utils.render.MathHelperIdentifierHelper;
import pw.hachimi.client.utils.render.RenderHelper5;

public class DrawBufferScreen extends Screen {
   private final List list = new CopyOnWriteArrayList();
   private final HUD hud;
   private final UnidentifiedHelper7 unidentifiedHelper7 = new UnidentifiedHelper7(false, '\u0000', 200.0F, 496665058, '楚', Easing.LINEAR);
   private RenderModule renderModule;
   private double e;
   private double f;
   private int g;
   private int h;
   private String text;
   private boolean j = true;
   private static final String b = "HUD";

   public DrawBufferScreen(HUD hud2) {
      super(Text.literal("HUD"));
      this.hud = hud2;
      float f2 = 15.0F;
      ModuleCategory[] moduleCategorys = ModuleCategory.values();
      int temp = moduleCategorys.length;
      int n = 0;

      do {
         ModuleCategory moduleCategory = moduleCategorys[n];
         if (moduleCategory == ModuleCategory.HUD) {
            CategoryPanel categoryPanel = new CategoryPanel(moduleCategory, f2, 15.0F, 14936090835792L);
            this.list.add(categoryPanel);
            f2 += categoryPanel.vz() + 2.0F;
         }
      } while (++n < 8);
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      mouseX = (int)(mouseX / DrawScreenScreen.b);
      mouseY = (int)(mouseY / DrawScreenScreen.b);
      context.getMatrices().push();
      context.getMatrices().scale(DrawScreenScreen.b, DrawScreenScreen.b, 1.0F);
      if (this.renderModule != null && (this.g != mouseX || this.h != mouseY)) {
         this.Cu(this.renderModule, (float)(mouseX - this.e), (float)(mouseY - this.f));
         this.g = mouseX;
         this.h = mouseY;
      }

      if (UI.atK().aub()) {
         Mc.client.gameRenderer.renderBlur(delta);
         Mc.client.getFramebuffer().beginWrite(false);
      }

      context.getMatrices().push();
      BufferVertexBase.vi(context, 0.0, 0.0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), 1711276032);
      Cx(context, 0.0F, Mc.client.getWindow().getScaledHeight() / 2.0F - 0.5F, Mc.client.getWindow().getScaledWidth(), 1.0F, Color.WHITE);
      Cx(context, Mc.client.getWindow().getScaledWidth() / 2.0F - 0.5F, 0.0F, 1.0F, Mc.client.getWindow().getScaledHeight(), Color.WHITE);
      boolean found = false;
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         CategoryPanel categoryPanel = (CategoryPanel)iterator2.next();
         if (categoryPanel.vC((float)mouseX, (float)mouseY) && DrawScreenScreen.i && this.Cy()) {
            categoryPanel.bL(true);
         }

         categoryPanel.aeU(context, mouseX, mouseY);
         Iterator iterator3 = categoryPanel.getList().iterator();

         while (iterator3.hasNext()) {
            DrawMatrixHelper drawMatrixHelper = (DrawMatrixHelper)iterator3.next();
            if (DrawHelper2.text != null && !drawMatrixHelper.getModuleBase().Kn().toLowerCase().contains(DrawHelper2.text.toLowerCase())) {
               continue;
            }

            if (drawMatrixHelper.vC((float)mouseX, (float)mouseY) && !drawMatrixHelper.getModuleBase().hw().isEmpty()) {
               this.text = drawMatrixHelper.getModuleBase().hw();
               found = true;
               break;
            }

            if (!drawMatrixHelper.anS()) {
               continue;
            }

            Iterator iterator4 = drawMatrixHelper.getList().iterator();

            while (iterator4.hasNext()) {
               DrawBase drawBase = (DrawBase)iterator4.next();
               if (drawBase.getSetting().isVisible()
                  && !drawBase.getSetting().atw().isEmpty()
                  && drawBase.vC((float)mouseX, (float)mouseY)) {
                  this.text = drawBase.getSetting().atw();
                  found = true;
                  break;
               }
            }
         }
      }

      context.getMatrices().pop();
      if (UI.atK().aui()) {
         this.unidentifiedHelper7.qV(found && DrawScreenScreen.colorBase == null);
         if (this.unidentifiedHelper7.qW() > 0.01F && this.text != null) {
            int n = RenderUtils.acF(this.text);
            int n2 = (int)(60.0 * this.unidentifiedHelper7.qW());
            int n3 = new Color(0, 0, 0, 50 + n2).getRGB();
            BufferVertexBase.vi(context, (double)(mouseX + 10.0F), (double)(mouseY - 8.0F), (double)(n + 4.0F), (double)(RenderUtils.acG() + 2.0F), n3);
            RenderUtils.acE(context, this.text, mouseX + 12.0F, mouseY - 6.0F, ColorUtils.iH(-1, (float)this.unidentifiedHelper7.qW()));
         }
      }

      context.getMatrices().pop();
      DrawScreenScreen.h = false;
      DrawScreenScreen.f = false;
      DrawScreenScreen.c = mouseX;
      DrawScreenScreen.d = mouseY;
      Managers.drawMatrixManager.Hw(context, delta, true);
   }

   private void Cu(RenderModule renderModule2, float f2, float f3) {
      double d = renderModule2.adl();
      double d2 = renderModule2.adm();
      double d3 = Mc.client.getWindow().getScaledWidth();
      double d4 = Mc.client.getWindow().getScaledHeight();
      double d5 = d3 / 2.0;
      double d6 = d4 / 2.0;
      double d7 = d5 / 2.0;
      double d8 = d5 + d5 / 2.0;
      double d9 = d6 / 2.0;
      double d10 = d6 + d6 / 2.0;
      double d11 = f2 + d / 2.0;
      double d12 = f3 + d2 / 2.0;
      boolean flag = Math.abs(d11 - d5) <= 5.0;
      boolean flag2 = Math.abs(d12 - d6) <= 5.0;
      LeftMiddleMode leftMiddleMode;
      float f4;
      if (f2 < d7) {
         leftMiddleMode = LeftMiddleMode.LEFT;
         f4 = flag ? 0.0F : Math.max(f2, 0.0F);
      } else if (f2 + d > d8) {
         leftMiddleMode = LeftMiddleMode.RIGHT;
         f4 = flag ? 0.0F : (float)Math.min(f2 - (d3 - d), 0.0);
      } else {
         leftMiddleMode = LeftMiddleMode.MIDDLE;
         f4 = flag ? 0.0F : (float)(f2 - (d5 - d / 2.0));
      }

      TopMiddleMode topMiddleMode;
      float f5;
      if (f3 < d9) {
         topMiddleMode = TopMiddleMode.TOP;
         f5 = flag2 ? 0.0F : Math.max(f3, 0.0F);
      } else if (f3 + d2 > d10) {
         topMiddleMode = TopMiddleMode.BOTTOM;
         f5 = flag2 ? 0.0F : (float)Math.min(f3 - (d4 - d2), 0.0);
      } else {
         topMiddleMode = TopMiddleMode.MIDDLE;
         f5 = flag2 ? 0.0F : (float)(f3 - (d6 - d2 / 2.0));
      }

      renderModule2.adf(f4, f5, leftMiddleMode, topMiddleMode);
      Iterator iterator2 = Managers.drawMatrixManager.Hz().iterator();

      loop:
      while (iterator2.hasNext()) {
         RenderModule renderModule3 = (RenderModule)iterator2.next();
         if (!renderModule3.awg() || renderModule3 == renderModule2) {
            continue;
         }

         RenderModule renderModule4 = renderModule3;
         while (renderModule4 != null) {
            renderModule4 = Managers.drawMatrixManager.Hy(renderModule4.adi());
            if (renderModule4 == renderModule2) {
               continue loop;
            }
         }

         double d13 = renderModule3.add();
         double d14 = renderModule3.ade();
         double d15 = renderModule3.adl();
         double d16 = renderModule3.adm();
         if (!this.Cv(f2 - 1.0F, f3 - 1.0F, d + 2.0, d2 + 2.0, d13, d14, d15, d16)) {
            continue;
         }

         if (Math.abs(f2 + d - (d13 + d15)) <= 2.5) {
            renderModule2.adh(0.0F, LeftMiddleMode.RIGHT_OFFSET, renderModule3.Kn());
         } else if (Math.abs(f2 - d13) <= 2.5) {
            renderModule2.adh(0.0F, LeftMiddleMode.LEFT_OFFSET, renderModule3.Kn());
         } else if (Math.abs(f2 + d - d13) <= 10.0) {
            renderModule2.adh(0.0F, LeftMiddleMode.LEFT, renderModule3.Kn());
         } else if (Math.abs(f2 - (d13 + d15)) <= 10.0) {
            renderModule2.adh(0.0F, LeftMiddleMode.RIGHT, renderModule3.Kn());
         } else if (Math.abs(f2 + d / 2.0 - (d13 + d15 / 2.0)) <= 2.5) {
            renderModule2.adh(0.0F, LeftMiddleMode.MIDDLE, renderModule3.Kn());
         } else if (f2 <= d13 + d15 / 2.0) {
            renderModule2.adh((float)(f2 - d13), LeftMiddleMode.LEFT_OFFSET, renderModule3.Kn());
         } else {
            renderModule2.adh((float)(d13 + d15 - f2 - d), LeftMiddleMode.RIGHT_OFFSET, renderModule3.Kn());
         }

         if (Math.abs(f3 + d2 - (d14 + d16)) <= 2.5) {
            renderModule2.adg(0.0F, TopMiddleMode.BOTTOM_OFFSET, renderModule3.Kn());
         } else if (Math.abs(f3 - d14) <= 2.5) {
            renderModule2.adg(0.0F, TopMiddleMode.TOP_OFFSET, renderModule3.Kn());
         } else if (Math.abs(f3 + d2 - d14) <= 10.0) {
            renderModule2.adg(0.0F, TopMiddleMode.TOP, renderModule3.Kn());
         } else if (Math.abs(f3 - (d14 + d16)) <= 10.0) {
            renderModule2.adg(0.0F, TopMiddleMode.BOTTOM, renderModule3.Kn());
         } else if (Math.abs(f3 + d2 / 2.0 - (d14 + d16 / 2.0)) <= 2.5) {
            renderModule2.adg(0.0F, TopMiddleMode.MIDDLE, renderModule3.Kn());
         } else if (f3 <= d14 + d16 / 2.0) {
            renderModule2.adg((float)(f3 - d14), TopMiddleMode.TOP_OFFSET, renderModule3.Kn());
         } else {
            renderModule2.adg((float)(d14 + d16 - f3 - d2), TopMiddleMode.BOTTOM_OFFSET, renderModule3.Kn());
         }

         return;
      }
   }

   private boolean Cv(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
      return !(d + d3 <= d5) && !(d5 + d7 <= d) && !(d2 + d4 <= d6) && !(d6 + d8 <= d2);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      mouseX /= DrawScreenScreen.b;
      mouseY /= DrawScreenScreen.b;
      if (button == 0) {
         Iterator iterator2 = Managers.drawMatrixManager.Hz().iterator();

         while (iterator2.hasNext()) {
            RenderModule renderModule2 = (RenderModule)iterator2.next();
            if (renderModule2.awg() && renderModule2.acZ(mouseX, mouseY)) {
               this.renderModule = renderModule2;
               this.e = (int)mouseX - renderModule2.add();
               this.f = (int)mouseY - renderModule2.ade();
               return true;
            }
         }

         DrawScreenScreen.h = true;
         DrawScreenScreen.i = true;
      } else if (button == 1) {
         DrawScreenScreen.f = true;
         DrawScreenScreen.g = true;
      }

      Iterator iterator3 = this.list.iterator();

      while (iterator3.hasNext()) {
         ((CategoryPanel)iterator3.next()).UU((int)mouseX, (int)mouseY, button);
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      mouseX /= DrawScreenScreen.b;
      mouseY /= DrawScreenScreen.b;
      if (button == 0) {
         this.renderModule = null;
         DrawScreenScreen.colorBase = null;
         DrawScreenScreen.i = false;
      } else if (button == 1) {
         DrawScreenScreen.g = false;
      }

      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         ((CategoryPanel)iterator2.next()).UV();
      }

      return super.mouseReleased(mouseX, mouseY, button);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      mouseX /= DrawScreenScreen.b;
      mouseY /= DrawScreenScreen.b;
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         CategoryPanel categoryPanel = (CategoryPanel)iterator2.next();
         if (DrawScreenScreen.j) {
            float f3 = (float)(categoryPanel.vv() + verticalAmount * UI.atK().aul());
            categoryPanel.vE(f3, categoryPanel.vw());
         } else {
            float f2 = (float)(categoryPanel.vw() + verticalAmount * UI.atK().aul());
            categoryPanel.vE(categoryPanel.vv(), f2);
         }
      }

      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 340) {
         DrawScreenScreen.j = true;
      }

      this.j = true;
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         CategoryPanel categoryPanel = (CategoryPanel)iterator2.next();
         if (categoryPanel.getDrawHelper2() != null && categoryPanel.getDrawHelper2().afc()) {
            this.j = false;
         } else {
            Iterator iterator3 = categoryPanel.getList().iterator();

            while (iterator3.hasNext()) {
               Iterator iterator4 = ((DrawMatrixHelper)iterator3.next()).getList().iterator();

               while (iterator4.hasNext()) {
                  DrawBase drawBase = (DrawBase)iterator4.next();
                  if (drawBase instanceof DrawHelper && ((DrawHelper)drawBase).lW()
                     || drawBase instanceof RenderHelper5 && ((RenderHelper5)drawBase).afG()
                     || drawBase instanceof MathHelperIdentifierHelper && ((MathHelperIdentifierHelper)drawBase).aA()
                     || drawBase instanceof MathHelperHelper && ((MathHelperHelper)drawBase).nX()) {
                     this.j = false;
                     break;
                  }
               }
            }
         }
      }

      iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         ((CategoryPanel)iterator2.next()).UW(keyCode);
      }

      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 340) {
         DrawScreenScreen.j = false;
      }

      return super.keyReleased(keyCode, scanCode, modifiers);
   }

   public boolean charTyped(char chr, int modifiers) {
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         ((CategoryPanel)iterator2.next()).UX(chr);
      }

      return super.charTyped(chr, modifiers);
   }

   public boolean shouldPause() {
      return false;
   }

   public void close() {
      this.renderModule = null;
      super.close();
   }

   public void onDisplayed() {
      DrawScreenScreen.h = false;
      DrawScreenScreen.i = false;
      DrawScreenScreen.f = false;
      DrawScreenScreen.g = false;
      DrawScreenScreen.j = false;
   }

   public boolean shouldCloseOnEsc() {
      return this.j;
   }

   public HUD getHud() {
      return this.hud;
   }

   public static void Cx(DrawContext context2, float f2, float f3, float f4, float f5, Color color2) {
      f4 += f2;
      f5 += f3;
      Matrix4f matrix4f = context2.getMatrices().peek().getPositionMatrix();
      if (f2 < f4) {
         float f6 = f2;
         f2 = f4;
         f4 = f6;
      }

      if (f3 < f5) {
         float f7 = f3;
         f3 = f5;
         f5 = f7;
      }

      RenderSystem.enableBlend();
      if (RenderSystem.getShader() != GameRenderer.getPositionColorProgram()) {
         RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      }

      Tessellator temp = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_COLOR;
      DrawMode drawMode = DrawMode.QUADS;
      BufferBuilder bufferBuilder = temp.begin(drawMode, vertexFormat);
      VertexConsumer vertexConsumer = bufferBuilder.vertex(matrix4f, f2, f3, 0.0F);
      int n = color2.getRGB();
      vertexConsumer.color(n);
      VertexConsumer vertexConsumer2 = bufferBuilder.vertex(matrix4f, f2, f5, 0.0F);
      int n2 = color2.getRGB();
      vertexConsumer2.color(n2);
      VertexConsumer vertexConsumer3 = bufferBuilder.vertex(matrix4f, f4, f5, 0.0F);
      int n3 = color2.getRGB();
      vertexConsumer3.color(n3);
      VertexConsumer vertexConsumer4 = bufferBuilder.vertex(matrix4f, f4, f3, 0.0F);
      int n4 = color2.getRGB();
      vertexConsumer4.color(n4);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      RenderSystem.disableBlend();
   }

   private boolean Cy() {
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         if (((CategoryPanel)iterator2.next()).bK()) {
            return false;
         }
      }

      return true;
   }
}
