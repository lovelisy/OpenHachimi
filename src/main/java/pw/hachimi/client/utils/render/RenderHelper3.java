package pw.hachimi.client.utils.render;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.gui.CategoryPanel;
import pw.hachimi.client.gui.ScreenDrawScreen;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.manager.SoundManager;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.render.DrawBase;
import pw.hachimi.client.setting.BlockListSetting;
import pw.hachimi.client.setting.Setting;

public class RenderHelper3 extends DrawBase {
   public RenderHelper3(CategoryPanel categoryPanel, DrawMatrixHelper drawMatrixHelper, Setting setting2, float f, float f2) {
      super(categoryPanel, drawMatrixHelper, setting2, f, f2);
   }

   @Override
   public void Cs(DrawContext context, float f, float f2, int n, int n2) {
      this.vy(RenderUtils.acG() + UI.atK().atT());
      this.j = f;
      this.k = f2;
      boolean flag = this.vC(n, n2);
      this.vg(context, this.jj(true, flag).getRGB());
      int n3 = UI.auf() ? -16777216 : -1;
      String str = vf(this.setting.atu());
      this.ve(context, str, f + 2.0F, f2 + UI.atK().atU(), n3);
      this.ve(context, "§7[edit]", f + this.l - 2.0F - RenderUtils.acF("[edit]"), f2 + UI.atK().atU(), n3);
   }

   @Override
   public void UU(int n, int n2, int n3) {
      if (this.vC(n, n2) && n3 == 0) {
         if (UI.atK().auc()) {
            Managers.soundManager.jg(SoundManager.a);
         }

         MinecraftClient temp = client;
         ScreenDrawScreen screenDrawScreen = new ScreenDrawScreen(new DrawBufferHelper((List)this.setting.atx(), this.setting instanceof BlockListSetting));
         temp.setScreen(screenDrawScreen);
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
