package pw.hachimi.client.module;

import java.awt.Color;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.event.network.VisitFormatted_1Event2;
import pw.hachimi.client.model.render.ImageShaderMode;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class ColorHelper3 extends Module3 {
   private static ColorHelper3 INSTANCE;
   Setting colorSetting1 = this.registerSetting(createColorSetting("Theme", "The primary client color", c()));
   Setting colorSetting2 = this.registerSetting(createColorSetting("Gradient", "The color of the rainbow gradient", Color.WHITE));
   Setting booleanSetting1 = this.registerSetting(new BooleanSetting("ToggleSound", "Play audio when toggle module", true));
   Setting imageShaderMode = this.registerSetting(new EnumSetting("GameContext", "Replace game menu background", ImageShaderMode.Shader, ImageShaderMode.values()));
   public static boolean o;

   public ColorHelper3() {
      super("Settings", "Client settings", ModuleCategory.CLIENT);
      INSTANCE = this;
   }

   public static ColorHelper3 auO() {
      return INSTANCE;
   }

   public boolean auP() {
      boolean flag;
      if (this.imageShaderMode.atx() == ImageShaderMode.Shader) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean auQ() {
      boolean flag;
      if (this.imageShaderMode.atx() != ImageShaderMode.VANILLA) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   @EventListener
   public void onVisitFormatted_1Event2(VisitFormatted_1Event2 visitFormatted1Event2) {
      visitFormatted1Event2.HD(this.auX());
   }

   public boolean auS() {
      return (Boolean)this.booleanSetting1.atx();
   }

   public Color auT() {
      return (Color)this.colorSetting2.atx();
   }

   public Color auU() {
      return (Color)this.colorSetting1.atx();
   }

   public Color auV(float f) {
      ColorSetting colorSetting = (ColorSetting)this.colorSetting1;
      return new Color(colorSetting.TC() / 255.0F, colorSetting.TD() / 255.0F, colorSetting.TE() / 255.0F, MathHelper.clamp(f, 0.0F, 1.0F));
   }

   public Color auW(int n) {
      ColorSetting colorSetting = (ColorSetting)this.colorSetting1;
      return new Color(colorSetting.TC(), colorSetting.TD(), colorSetting.TE(), MathHelper.clamp(n, 0, 255));
   }

   public Integer auX() {
      return this.auU().getRGB();
   }

   public int auY(int n) {
      return this.auW(n).getRGB();
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color) {
      return new ColorSetting(str, str2, color, false, false);
   }

   private static Color c() {
      return new Color(181, 156, 255);
   }
}
