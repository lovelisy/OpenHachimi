package pw.hachimi.client.module.render;

import pw.hachimi.client.model.render.AntiAliasingOption;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;

public class AntiAliasing extends ClientModule {
   private static AntiAliasing INSTANCE;
   Setting mode = this.registerSetting(new EnumSetting("Mode", "mode", AntiAliasingOption.a_, AntiAliasingOption.values()));
   Setting sample = this.registerSetting(new NumberSetting("Sample", "MSAA sampling number", 0, 8, 32));
   public static boolean bi;

   public AntiAliasing() {
      super("AntiAliasing", "Combat the problems of aliasing in a sampled signal such as a digital image", ModuleCategory.RENDER);
      INSTANCE = this;
      this.WT(true);
   }

   public static AntiAliasing Dj() {
      return INSTANCE;
   }

   public int Dk() {
      return (Integer)this.sample.atx();
   }
}
