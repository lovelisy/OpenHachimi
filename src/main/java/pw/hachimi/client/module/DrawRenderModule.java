package pw.hachimi.client.module;

import java.awt.Color;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.UnidentifiedHelper9;
import java.util.HashMap;

public abstract class DrawRenderModule extends RenderModule {
   UnidentifiedHelper9 unidentifiedHelper9_1;
   UnidentifiedHelper9 unidentifiedHelper92_1;
   Setting setting;
   public String text;
   float k;
   private static final long b = 0L;
   private static final String i = "Animation";
   private static final Map p = new HashMap();

   public DrawRenderModule(String str, String str2) {
      super(str, "");
      this.unidentifiedHelper9_1 = new UnidentifiedHelper9();
      this.unidentifiedHelper92_1 = new UnidentifiedHelper9();
      this.setting = this.registerSetting(new BooleanSetting(i, "", true));
   }

   @Override
   public void adb() {
      if ((Boolean)this.setting.atx()) {
         this.k = (float)this.unidentifiedHelper9_1.MH((double)this.add(), 300L, Easing.BACK_OUT);
      } else {
         this.k = this.add();
      }
   }

   @Override
   public void adc(DrawContext context) {
      if (this.text != null) {
         this.adq(context, this.text, this.k, this.ade());
      }
   }

   @Override
   public void adn(DrawContext context) {
      RenderUtils.acH(context.getMatrices(), this.k, this.ade(), this.adl(), this.adm(), (Float)this.numberSetting3.atx(), (Color)this.colorSetting1.atx());
   }

   @Override
   public float adl() {
      if (this.text == null) {
         return 1.0F;
      }

      if ((Boolean)this.setting.atx()) {
         return (float)this.unidentifiedHelper92_1.MH((double)(RenderUtils.acF(this.text) + super.adl()), 300L, Easing.BACK_OUT);
      }

      return RenderUtils.acF(this.text) + super.adl();
   }

   @Override
   public float adm() {
      return RenderUtils.acG() + super.adm();
   }
}
