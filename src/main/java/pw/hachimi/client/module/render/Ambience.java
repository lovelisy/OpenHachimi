package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.function.Supplier;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.UnknownEvent3;
import pw.hachimi.client.event.render.GetDefaultColorEvent;
import pw.hachimi.client.event.render.GetGrassColorAtEvent;
import pw.hachimi.client.event.render.GetWaterColorEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class Ambience extends ClientModule {
   private static Ambience INSTANCE;
   Setting grass = this.registerSetting(new BooleanSetting("Grass", "Colors the grass", false));
   Setting grassColor = this.registerSetting(createColorSetting("GrassColor", "The color of the grass", Color.RED, this::isGrassColorVisible));
   Setting foliage = this.registerSetting(new BooleanSetting("Foliage", "Colors the foliage", false));
   Setting foliageColor = this.registerSetting(createColorSetting("FoliageColor", "The color of the foliage", Color.RED, this::isFoliageColorVisible));
   Setting water = this.registerSetting(new BooleanSetting("Water", "Colors the water", false));
   Setting waterColor = this.registerSetting(createColorSetting("WaterColor", "The color of the water", Color.RED, this::isWaterColorVisible));

   public Ambience() {
      super("Ambience", "Changes rendering of world", ModuleCategory.RENDER);
      INSTANCE = this;
   }

   public static Ambience Th() {
      return INSTANCE;
   }

   @Override
   public void awd() {
      if (client.world != null) {
         client.worldRenderer.reload();
      }
   }

   @Override
   public void awe() {
      if (client.world != null) {
         client.worldRenderer.reload();
      }
   }

   @EventListener
   public void Ti(UnknownEvent3 unknownEvent3) {
      if (client.world != null && unknownEvent3.getSetting().getConfigContainer() == this && unknownEvent3.getEventPhase() == EventPhase.POST) {
         client.worldRenderer.reload();
      }
   }

   @EventListener
   public void onGetGrassColorAt(GetGrassColorAtEvent getGrassColorAtEvent) {
      if ((Boolean)this.grass.atx()) {
         getGrassColorAtEvent.aim();
         getGrassColorAtEvent.MR((Color)this.grassColor.atx());
      }
   }

   @EventListener
   public void onGetDefaultColor(GetDefaultColorEvent getDefaultColorEvent) {
      if ((Boolean)this.foliage.atx()) {
         getDefaultColorEvent.aim();
         getDefaultColorEvent.MR((Color)this.foliageColor.atx());
      }
   }

   @EventListener
   public void onGetWaterColor(GetWaterColorEvent getWaterColorEvent) {
      if ((Boolean)this.water.atx()) {
         getWaterColorEvent.aim();
         getWaterColorEvent.MR((Color)this.waterColor.atx());
      }
   }

   private Boolean isWaterColorVisible() {
      return (Boolean)this.water.atx();
   }

   private Boolean isFoliageColorVisible() {
      return (Boolean)this.foliage.atx();
   }

   private Boolean isGrassColorVisible() {
      return (Boolean)this.grass.atx();
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color, Supplier supplier) {
      return new ColorSetting(str, str2, color, false, true, supplier);
   }
}
