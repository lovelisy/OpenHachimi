package pw.hachimi.client.module.render;

import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;

public class AspectRatio extends ClientModule {
   private static AspectRatio INSTANCE;
   public final Setting ratio = this.registerSetting(new NumberSetting("Ratio", "Ratio for aspect", 0.0F, 1.78F, 5.0F));

   public AspectRatio() {
      super("AspectRatio", "Change your screen aspect ratio", ModuleCategory.RENDER);
      INSTANCE = this;
   }

   public static AspectRatio YA() {
      return INSTANCE;
   }
}
