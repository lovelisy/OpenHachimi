package pw.hachimi.client.utils.render;

import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.gui.CategoryPanel;
import pw.hachimi.client.gui.DrawScreenScreen;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.manager.SoundManager;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.render.DrawBase;
import pw.hachimi.client.setting.KeybindSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper17;

public class DrawHelper extends DrawBase {
   private boolean a;

   public DrawHelper(CategoryPanel categoryPanel, DrawMatrixHelper drawMatrixHelper, Setting setting2, float f, float f2) {
      super(categoryPanel, drawMatrixHelper, setting2, f, f2);
   }

   @Override
   public void Cs(DrawContext context, float f, float f2, int n, int n2) {
      this.vy(RenderUtils.acG() + UI.atK().atT());
      this.j = f;
      this.k = f2;
      UnidentifiedHelper17 unidentifiedHelper17 = (UnidentifiedHelper17)this.setting.atx();
      String str = this.a ? "..." : unidentifiedHelper17.agv();
      boolean flag = this.vC(n, n2);
      this.vg(context, this.jj(false, flag).getRGB());
      int n3 = UI.auf() ? -16777216 : -1;
      FontLoader.fontRenderer3.aaS(context.getMatrices(), "A", f + 2.0F, f2 + UI.atK().atU(), n3);
      String str2;
      if (flag && DrawScreenScreen.j) {
         str2 = unidentifiedHelper17.d ? "Hold" : "Toggle";
      } else {
         str2 = vf(this.setting.atu());
      }

      String str3 = str.length() != 1 ? vf(str) : str;
      float f3 = RenderUtils.acF(str3);
      this.ve(context, str2, f + 13.0F, f2 + UI.atK().atU(), n3);
      this.ve(context, str3, f + this.l - f3 - 2.0F, f2 + UI.atK().atU(), -5592406);
   }

   @Override
   public void UU(int n, int n2, int n3) {
      if (this.a) {
         ((KeybindSetting)this.setting).aT(1000 + n3);
         this.a = false;
      } else if (this.vC(n, n2)) {
         if (this.a) {
            if (n3 == 0) {
               this.a = false;
            }
         } else if (n3 == 0) {
            if (DrawScreenScreen.j) {
               UnidentifiedHelper17 unidentifiedHelper17 = (UnidentifiedHelper17)this.setting.atx();
               unidentifiedHelper17.d = !unidentifiedHelper17.d;
            } else {
               this.a = true;
            }
         } else if (n3 == 2) {
            ((KeybindSetting)this.setting).aT(-1);
         }

         if (UI.atK().auc()) {
            Managers.soundManager.jg(SoundManager.a);
         }
      }
   }

   @Override
   public void UV() {
   }

   @Override
   public void UW(int n) {
      if (this.a) {
         if (n != 256 && n != 259 && n != 261) {
            ((KeybindSetting)this.setting).aT(n);
         } else {
            ((KeybindSetting)this.setting).aT(-1);
         }

         this.a = false;
      }
   }

   @Override
   public void UX(char c) {
   }

   public boolean lW() {
      return this.a;
   }

   public void lX() {
      this.a = false;
   }
}
