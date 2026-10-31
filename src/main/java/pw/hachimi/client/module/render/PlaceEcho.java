package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.FadeShrinkMode;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.PlaceEchoBoxBlockPosHelper;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.eventbus.annotation.EventListener;

public class PlaceEcho extends ClientModule {
   private static PlaceEcho INSTANCE;
   public Setting easing = this.registerSetting(new EnumSetting("Easing", "Easing", Easing.LINEAR, Easing.values()));
   public Setting mode = this.registerSetting(new EnumSetting("Mode", "Echo mode", FadeShrinkMode.Fade, FadeShrinkMode.values()));
   public Setting fadeTime = this.registerSetting(new NumberSetting("Fade-Time", "Time to fade", 0, 250, 3000));
   public Setting noFail = this.registerSetting(new BooleanSetting("NoFail", "Do not wait for server response", false));
   public Setting timeOut = this.registerSetting(new NumberSetting("TimeOut", "Waiting time for server response", 0, 500, 3000, this::isTimeOutVisible));
   public Setting boxMode = this.registerSetting(new EnumSetting("BoxMode", "Box rendering mode", ShapeMode.Both, ShapeMode.values()));
   public Setting line = this.registerSetting(new ColorSetting("Line", "Outline color", new Color(255, 255, 255, 255)));
   public Setting side = this.registerSetting(new ColorSetting("Side", "Fill color", new Color(255, 255, 255, 100)));
   public Setting blockedLine = this.registerSetting(createColorSetting("BlockedLine", "Outline color of waiting for server response", new Color(160, 0, 0, 100)));
   public Setting blockedSide = this.registerSetting(createColorSetting("BlockedSide", "Fill color of waiting for server response", new Color(160, 0, 0, 100)));
   Setting postEffect = this.registerSetting(new BooleanSetting("PostEffect", "", false));
   Setting blurRadius = this.registerSetting(new NumberSetting("BlurRadius", "", 1.0F, 8.0F, 20.0F, this::isBlurRadiusVisible));
   Map map = new ConcurrentHashMap();

   public PlaceEcho() {
      super("PlaceEcho", "Render for your placement", ModuleCategory.RENDER);
      INSTANCE = this;
   }

   public static PlaceEcho Mz() {
      return INSTANCE;
   }

   @EventListener
   public void MA(Render3DContext render3DContext) {
      if (!(Boolean)this.postEffect.atx()) {
         if (!this.map.isEmpty()) {
            Collection temp = this.map.values();
            final Render3DContext render3DContext2 = render3DContext;
            Predicate predicate = new Predicate() {
               @Override
               public boolean test(Object render3DContext) {
                  return PlaceEcho.MC(render3DContext2, (PlaceEchoBoxBlockPosHelper)render3DContext);
               }
            };
            temp.removeIf(predicate);
         }
      }
   }

   public void MB(BlockPos pos) {
      if (this.awg()) {
         Map temp = this.map;
         PlaceEchoBoxBlockPosHelper placeEchoBoxBlockPosHelper = new PlaceEchoBoxBlockPosHelper(this, pos);
         temp.put(pos, placeEchoBoxBlockPosHelper);
      }
   }

   static boolean MC(Render3DContext render3DContext, PlaceEchoBoxBlockPosHelper placeEchoBoxBlockPosHelper) {
      return placeEchoBoxBlockPosHelper.aZ(render3DContext);
   }

   private Boolean isBlurRadiusVisible() {
      return (Boolean)this.postEffect.atx();
   }

   private Boolean isTimeOutVisible() {
      boolean flag;
      if (!(Boolean)this.noFail.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color) {
      return new ColorSetting(str, str2, color, true, false);
   }
}
