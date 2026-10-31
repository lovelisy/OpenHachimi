package pw.hachimi.client.utils.render;

import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.gui.CategoryPanel;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.manager.SoundManager;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.render.DrawBase;
import pw.hachimi.client.setting.Setting;

public class RenderHelper extends DrawBase {
   public RenderHelper(CategoryPanel categoryPanel, DrawMatrixHelper drawMatrixHelper, Setting setting2, float f, float f2) {
      super(categoryPanel, drawMatrixHelper, setting2, f, f2);
   }

   @Override
   public void Cs(DrawContext context, float f, float f2, int n, int n2) {
      this.vy(RenderUtils.acG() + UI.atK().atT());
      this.j = f;
      this.k = f2;
      boolean flag = this.vC(n, n2);
      this.vg(context, this.jj((Boolean)this.setting.atx(), flag).getRGB());
      int n3 = UI.auf() ? -16777216 : -1;
      this.ve(context, vf(this.setting.atu()), f + 2.0F, f2 + UI.atK().atU(), n3);
   }

   @Override
   public void UU(int n, int n2, int n3) {
      if (this.vC(n, n2) && n3 == 0) {
         boolean flag = (Boolean)this.setting.atx();
         this.setting.aty(!flag);
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
   }

   @Override
   public void UX(char c) {
   }
}
