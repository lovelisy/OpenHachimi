package pw.hachimi.client.gui;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pw.hachimi.client.Mc;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.render.BufferVertexBase;
import pw.hachimi.client.render.ColorBase;
import pw.hachimi.client.render.DrawBase;
import pw.hachimi.client.render.ScreenRectManager;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.DrawHelper;
import pw.hachimi.client.utils.render.DrawHelper2;
import pw.hachimi.client.utils.render.DrawMatrixHelper;
import pw.hachimi.client.utils.render.MathHelperHelper;
import pw.hachimi.client.utils.render.MathHelperIdentifierHelper;
import pw.hachimi.client.utils.render.RenderHelper5;

public class DrawScreenScreen extends Screen {
   public static final ScreenRectManager a = new ScreenRectManager();
   public static float b;
   public static int c;
   public static int d;
   public static ColorBase colorBase;
   public static boolean f;
   public static boolean g;
   public static boolean h;
   public static boolean i;
   public static boolean j;
   private final List list = new CopyOnWriteArrayList();
   private final UI ui;
   private final UnidentifiedHelper7 unidentifiedHelper7 = new UnidentifiedHelper7(false, '\u0000', 200.0F, 496665058, '楚', Easing.LINEAR);
   private static final Identifier n = Identifier.of("hachimiclient", "textures/custom_hud_icon.png");
   private String text;
   private boolean p = true;

   public DrawScreenScreen(UI ui2) {
      super(Text.literal("ClickGui"));
      this.ui = ui2;
      float f2 = 15.0F;
      ModuleCategory[] moduleCategorys = ModuleCategory.values();
      int temp = moduleCategorys.length;
      int n2 = 0;

      do {
         ModuleCategory moduleCategory = moduleCategorys[n2];
         if (moduleCategory != ModuleCategory.HUD) {
            CategoryPanel categoryPanel = new CategoryPanel(moduleCategory, f2, 15.0F, 14936090835792L);
            this.list.add(categoryPanel);
            f2 += categoryPanel.vz() + 3.0F;
         }
      } while (++n2 < 8);
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (UI.atK().aub()) {
         Mc.client.gameRenderer.renderBlur(delta);
         Mc.client.getFramebuffer().beginWrite(false);
      }

      context.fillGradient(
         0,
         0,
         context.getScaledWindowWidth(),
         (int)(context.getScaledWindowHeight() / 2.0),
         UI.atK().atV(UI.atM().getRGB()),
         UI.atK().atV(UI.atN().getRGB())
      );
      context.fillGradient(
         0,
         (int)(context.getScaledWindowHeight() * 0.5),
         context.getScaledWindowWidth(),
         context.getScaledWindowHeight(),
         UI.atK().atV(UI.atN().getRGB()),
         UI.atK().atV(UI.atO().getRGB())
      );
      mouseX = (int)(mouseX / b);
      mouseY = (int)(mouseY / b);
      context.getMatrices().push();
      context.getMatrices().scale(b, b, 1.0F);
      context.getMatrices().push();
      int n = UI.atK().atV(-1);
      if (UI.atK().auh()) {
         RenderUtils.acE(context, "shift + f1 to reset clickgui", 1.0F, 1.0F, n);
         RenderUtils.acE(context, "shift + scroll to scroll horizontally", 1.0F, 1 + RenderUtils.acG(), n);
         RenderUtils.acE(context, "shift + click a bind to switch between hold and toggle", 1.0F, 1 + RenderUtils.acG() * 2, n);
         RenderUtils.acE(context, "shift + click a module to hide it in the module list", 1.0F, 1 + RenderUtils.acG() * 3, n);
      }

      this.KE(context);
      boolean found = false;
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         CategoryPanel categoryPanel = (CategoryPanel)iterator2.next();
         if (categoryPanel.vC((float)mouseX, (float)mouseY) && i && this.KF()) {
            categoryPanel.bL(true);
         }

         categoryPanel.aeU(context, mouseX, mouseY);
         if (!categoryPanel.bG()) {
            continue;
         }

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
         this.unidentifiedHelper7.qV(found && !j && colorBase == null);
         if (this.unidentifiedHelper7.qW() > 0.01F && this.text != null) {
            int n2 = RenderUtils.acF(this.text);
            int n3 = (int)(60.0 * this.unidentifiedHelper7.qW());
            int n4 = UI.atK().atV(new Color(0, 0, 0, 50 + n3).getRGB());
            BufferVertexBase.vi(context, (double)(mouseX + 10.0F), (double)(mouseY - 8.0F), (double)(n2 + 4.0F), (double)(RenderUtils.acG() + 2.0F), n4);
            RenderUtils.acE(context, this.text, mouseX + 12.0F, mouseY - 6.0F, ColorUtils.iH(-1, (float)this.unidentifiedHelper7.qW()));
         }
      }

      h = false;
      f = false;
      c = mouseX;
      d = mouseY;
      context.getMatrices().pop();
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      mouseX /= b;
      mouseY /= b;
      if (button == 0) {
         h = true;
         i = true;
      } else if (button == 1) {
         f = true;
         g = true;
      }

      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         ((CategoryPanel)iterator2.next()).UU((int)mouseX, (int)mouseY, button);
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      mouseX /= b;
      mouseY /= b;
      if (button == 0) {
         i = false;
         colorBase = null;
      } else if (button == 1) {
         g = false;
      }

      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         ((CategoryPanel)iterator2.next()).UV();
      }

      return super.mouseReleased(mouseX, mouseY, button);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         CategoryPanel categoryPanel = (CategoryPanel)iterator2.next();
         if (j) {
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
         j = true;
      }

      if (keyCode == 290 && j) {
         UI.drawScreenScreen = new DrawScreenScreen(this.ui);
         this.ui.awc();
         this.ui.awb();
      }

      this.p = true;
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         CategoryPanel categoryPanel = (CategoryPanel)iterator2.next();
         if (categoryPanel.getDrawHelper2() != null && categoryPanel.getDrawHelper2().afc()) {
            this.p = false;
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
                     this.p = false;
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
         j = false;
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
      this.ui.awc();
      super.close();
   }

   public void onDisplayed() {
      h = false;
      i = false;
      f = false;
      g = false;
      j = false;
      colorBase = null;
   }

   public boolean shouldCloseOnEsc() {
      return this.p;
   }

   private void KE(DrawContext context2) {
      float f2 = UI.atK().auk();
      if (f2 != 1.0F) {
         float f3 = context2.getScaledWindowHeight() / 8.0F * (f2 - 1.0F);
         context2.getMatrices().translate(0.0F, f3, 0.0F);
      }
   }

   private boolean KF() {
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         if (((CategoryPanel)iterator2.next()).bK()) {
            return false;
         }
      }

      return true;
   }

   public List getList() {
      return this.list;
   }
}
