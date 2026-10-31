package pw.hachimi.client.module;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.module.client.HUD;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;

public class Watermark extends RenderModule {
   private static final Identifier a = Identifier.of("hachimiclient", "textures/logo.png");
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Logo", "Replace watermark with logo", false));
   Setting numberSetting1_1 = this.registerSetting(new NumberSetting("Scale", "", 0.1F, 1.0F, 3.0F, this::isNumberSetting1Visible));
   public String text;

   public Watermark() {
      super("Watermark", "");
      Object[] objs = new Object[]{"Hachimi", "2.0"};
      this.text = String.format("%s §fv%s", objs);
      this.awb();
      this.numberSetting1_1.aty(0.0F);
      this.numberSetting2.aty(0.0F);
   }

   @Override
   public void adb() {
   }

   @Override
   public void adc(DrawContext context) {
      if ((Boolean)this.booleanSetting1_1.atx()) {
         int n = HUD.zg().zo((this.ade() + 18.0) / RenderUtils.acG());
         RenderSystem.setShaderColor((n >> 16 & 0xFF) / 255.0F, (n >> 8 & 0xFF) / 255.0F, (n & 0xFF) / 255.0F, 1.0F);
         RenderSystem.enableBlend();
         Identifier temp = a;
         int temp2 = (int)this.add();
         int temp3 = (int)this.ade();
         int temp4 = (int)(75.0F * (Float)this.numberSetting1_1.atx());
         int temp5 = (int)(27.0F * (Float)this.numberSetting1_1.atx());
         int temp6 = (int)(75.0F * (Float)this.numberSetting1_1.atx());
         int n2 = (int)(27.0F * (Float)this.numberSetting1_1.atx());
         int n3 = temp6;
         int n4 = temp5;
         int n5 = temp4;
         int n6 = temp3;
         int n7 = temp2;
         Identifier id = temp;
         context.drawTexture(id, n7, n6, 0.0F, 0.0F, n5, n4, n3, n2);
         RenderSystem.disableBlend();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      } else {
         this.adq(context, this.text, this.add(), this.ade());
      }
   }

   @Override
   public float adl() {
      return super.adl() + ((Boolean)this.booleanSetting1_1.atx() ? 75.0F * (Float)this.numberSetting1_1.atx() : RenderUtils.acF(this.text));
   }

   @Override
   public float adm() {
      return super.adm() + ((Boolean)this.booleanSetting1_1.atx() ? 27.0F * (Float)this.numberSetting1_1.atx() : RenderUtils.acG());
   }

   private Boolean isNumberSetting1Visible() {
      return (Boolean)this.booleanSetting1_1.atx();
   }
}
