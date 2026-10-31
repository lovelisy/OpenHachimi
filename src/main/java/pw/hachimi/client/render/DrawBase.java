package pw.hachimi.client.render;

import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.gui.CategoryPanel;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.DrawMatrixHelper;

public abstract class DrawBase extends ColorBase {
   public final Setting setting;
   public final DrawMatrixHelper drawMatrixHelper;

   public DrawBase(CategoryPanel categoryPanel, DrawMatrixHelper drawMatrixHelper2, Setting setting2, float f, float f2) {
      super(categoryPanel, f, f2, 100.0F);
      this.drawMatrixHelper = drawMatrixHelper2;
      this.setting = setting2;
   }

   @Override
   public void aeU(DrawContext context, int n, int n2) {
      this.Cs(context, this.j, this.k, n, n2);
   }

   public abstract void Cs(DrawContext var1, float var2, float var3, int var4, int var5);

   public Setting getSetting() {
      return this.setting;
   }
}
