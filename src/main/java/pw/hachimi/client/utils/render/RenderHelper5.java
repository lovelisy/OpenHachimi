package pw.hachimi.client.utils.render;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.ArrayUtils;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.manager.SoundManager;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.render.DrawBase;
import pw.hachimi.client.gui.CategoryPanel;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;

public class RenderHelper5 extends DrawBase {
   private final Stopwatch stopwatch;
   private char[] b;
   private boolean c;
   private boolean d;

   public RenderHelper5(CategoryPanel categoryPanel, DrawMatrixHelper drawMatrixHelper2, Setting setting2, float f, float f2) {
      super(categoryPanel, drawMatrixHelper2, setting2, f, f2);
      this.stopwatch = new StopwatchImpl();
      this.b = ((String)setting2.atx()).toCharArray();
   }

   @Override
   public void Cs(DrawContext context, float f, float f2, int n, int n2) {
      this.vy(RenderUtils.acG() + UI.atK().atT());
      this.j = f;
      this.k = f2;
      int n3 = UI.auf() ? -16777216 : -1;
      boolean flag = this.vC(n, n2);
      this.vg(context, this.jj(false, flag).getRGB());
      String str = this.c ? new String(this.b) + this.afH() : this.setting.atu() + Formatting.GRAY + " " + new String(this.b);
      this.ve(context, str, f + 2.0F, f2 + UI.atK().atU(), n3);
   }

   @Override
   public void UU(int n, int n2, int n3) {
      if (this.vC(n, n2)) {
         if (n3 == 0) {
            if (this.c) {
               this.setting.aty(new String(this.b));
               this.c = false;
            } else {
               this.c = true;
            }

            if (UI.atK().auc()) {
               Managers.soundManager.jg(SoundManager.a);
            }
         }
      } else {
         this.afF();
      }
   }

   @Override
   public void UV() {
   }

   @Override
   public void UW(int n) {
      if (this.c) {
         switch (n) {
            case 256:
               this.b = ((String)this.setting.atx()).toCharArray();
               this.c = false;
               break;
            case 257:
               this.setting.aty(new String(this.b));
               this.c = false;
            case 258:
            default:
               break;
            case 259:
               if (this.b.length != 0) {
                  this.b = ArrayUtils.remove(this.b, this.b.length - 1);
               }
         }
      }
   }

   @Override
   public void UX(char c2) {
      if (this.c) {
         this.b = ArrayUtils.add(this.b, c2);
      }
   }

   public void afF() {
      this.c = false;
   }

   public boolean afG() {
      return this.c;
   }

   public String afH() {
      if (this.stopwatch.afq(250)) {
         this.d = !this.d;
         this.stopwatch.afr(29432, 5265492, (byte)40);
      }

      return this.d && this.c ? "_" : "";
   }
}
