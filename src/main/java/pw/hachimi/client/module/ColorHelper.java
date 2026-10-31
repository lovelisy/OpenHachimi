package pw.hachimi.client.module;

import java.awt.Color;
import java.util.function.Supplier;
import pw.hachimi.client.event.network.VisitFormatted_1Event;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class ColorHelper extends Module3 {
   private static ColorHelper INSTANCE;
   Setting booleanSetting1 = this.registerSetting(new BooleanSetting("Friends", "Allows friend system to function", true));
   Setting booleanSetting2 = this.registerSetting(new BooleanSetting("AddNotify", "Notifies players when you add them as a friend", false, this::isBooleanSetting2Visible));
   Setting colorSetting1 = this.registerSetting(createColorSetting("FriendsColor", "The color for friends in the client", a(), this::isColorSetting1Visible));

   public ColorHelper() {
      super("Socials", "The client socials system", ModuleCategory.CLIENT);
      INSTANCE = this;
   }

   public static ColorHelper wx() {
      return INSTANCE;
   }

   @EventListener
   public void onVisitFormatted_1(VisitFormatted_1Event visitFormatted1Event) {
      visitFormatted1Event.HD(this.wC());
   }

   public boolean wz() {
      return (Boolean)this.booleanSetting1.atx();
   }

   public boolean wA() {
      return (Boolean)this.booleanSetting2.atx();
   }

   public Color wB() {
      return (Color)this.colorSetting1.atx();
   }

   public int wC() {
      return this.wB().getRGB();
   }

   private Boolean isColorSetting1Visible() {
      return (Boolean)this.booleanSetting1.atx();
   }

   private Boolean isBooleanSetting2Visible() {
      return (Boolean)this.booleanSetting1.atx();
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color, Supplier supplier) {
      return new ColorSetting(str, str2, color, false, false, supplier);
   }

   private static Color a() {
      return new Color(-10027009);
   }
}
