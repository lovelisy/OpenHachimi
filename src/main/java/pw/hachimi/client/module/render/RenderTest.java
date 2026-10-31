package pw.hachimi.client.module.render;

import java.awt.Color;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.model.render.RenderTestOption;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.setting.StringSetting;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.eventbus.annotation.EventListener;

public class RenderTest extends ClientModule {
   Setting booleanSetting1_1;
   Setting colorSetting1;
   Setting numberSetting1;
   Setting numberSetting2;
   Setting numberSetting3;
   Setting optionMode;
   Setting textSetting1;
   public static boolean bk;

   public RenderTest(int n, short s, char c) {
      super("RenderTest", "", ModuleCategory.RENDER);
      if (s >= 0) {
         this.booleanSetting1_1 = this.registerSetting(new BooleanSetting("Boolean", "", false));
         if ((s >= 0 || c > 0) && c > 0) {
            this.colorSetting1 = this.registerSetting(createColorSetting("Color", "", -1));
            this.numberSetting1 = this.registerSetting(new NumberSetting("Double", "", 0.0, 5.0, 10.0));
            if (n > 0) {
            }

            if (c > 0) {
               this.numberSetting2 = this.registerSetting(new NumberSetting("Integer", "", 0, 5, 10));
               if (c > 0) {
                  this.numberSetting3 = this.registerSetting(new NumberSetting("Float", "", 0.0F, 5.0F, 10.0F));
                  if (c > 0 && n >= 0) {
                     this.optionMode = this.registerSetting(new EnumSetting("Enum", "", RenderTestOption.a_, RenderTestOption.values()));
                     if (s > 0) {
                        this.textSetting1 = this.registerSetting(createStringSetting("String", ""));
                        if (n >= 0) {
                           return;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventListener
   public void HC(Render3DContext render3DContext) {
      for (int n = -20; n <= 20; n++) {
         for (int n2 = -20; n2 <= 20; n2++) {
            BlockPos temp = client.player.getBlockPos().down();
            int n3 = n2;
            int n4 = n;
            BlockPos pos = temp.add(n4, 0, n3).withY(0);
            render3DContext.boxRenderer.YZ(pos, (Color)this.colorSetting1.atx(), (Color)this.colorSetting1.atx(), ShapeMode.Lines);
         }
      }
   }

   private static StringSetting createStringSetting(String str, String str2) {
      return new StringSetting(str, str2, "value");
   }

   private static ColorSetting createColorSetting(String str, String str2, Integer integer) {
      return new ColorSetting(str, str2, integer);
   }
}
