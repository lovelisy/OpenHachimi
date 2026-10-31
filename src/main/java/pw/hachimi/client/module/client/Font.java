package pw.hachimi.client.module.client;

import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.UnknownEvent3;
import pw.hachimi.client.model.render.DisableNormalMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.FontLoader;
import pw.hachimi.eventbus.annotation.EventListener;

public class Font extends ClientModule {
   private static Font INSTANCE;
   Setting shadow = this.registerSetting(new EnumSetting("Shadow", "Shadow mode", DisableNormalMode.NORMAL, DisableNormalMode.values()));
   Setting antiAlias = this.registerSetting(new BooleanSetting("AntiAlias", "Applies antialiasing to font", true));
   Setting fractionalMetrics = this.registerSetting(new BooleanSetting("FractionalMetrics", "Applies fractional metrics to font", false));
   Setting size = this.registerSetting(new NumberSetting("Size", "The font size", 5, 9, 12));
   Setting vanillaShadow = this.registerSetting(new NumberSetting("VanillaShadow", "The vanilla shadow offset", 0.1F, 1.0F, 1.5F));
   Setting shadowOffset = this.registerSetting(new NumberSetting("ShadowOffset", "The font shadow offset", 0.1F, 0.8F, 1.5F));
   Setting shadowBrightness = this.registerSetting(new NumberSetting("ShadowBrightness", "The font shadow brightness", 0.0F, 0.25F, 0.7F));
   public static boolean eh;

   public Font() {
      super("Font", "Changes the client text to custom font rendering", ModuleCategory.CLIENT);
      INSTANCE = this;
      this.awb();
      this.WT(true);
   }

   public static Font kT() {
      return INSTANCE;
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE && FontLoader.g != ((Integer)this.size.atx()).intValue()) {
         FontLoader.aou(((Integer)this.size.atx()).intValue());
      }
   }

   @EventListener
   public void kV(UnknownEvent3 unknownEvent3) {
      if (!FontLoader.aov()) {
         return;
      }

      if (unknownEvent3.getEventPhase() == EventPhase.POST && (unknownEvent3.getSetting() == this.antiAlias || unknownEvent3.getSetting() == this.fractionalMetrics)) {
         FontLoader.aot();
      }
   }

   public boolean kW() {
      return (Boolean)this.antiAlias.atx();
   }

   public boolean kX() {
      return (Boolean)this.fractionalMetrics.atx();
   }

   public float kY() {
      return (Float)this.vanillaShadow.atx();
   }

   public float kZ() {
      return (Float)this.shadowOffset.atx();
   }

   public float la() {
      return (Float)this.shadowBrightness.atx();
   }

   public DisableNormalMode lb() {
      return (DisableNormalMode)this.shadow.atx();
   }
}
