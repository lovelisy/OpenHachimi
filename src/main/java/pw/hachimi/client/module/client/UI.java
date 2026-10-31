package pw.hachimi.client.module.client;

import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.gui.DrawScreenScreen;
import pw.hachimi.client.model.UnidentifiedOption4;
import pw.hachimi.client.model.render.AstolfoMioMode;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.OffGradientMode;
import pw.hachimi.client.model.render.UnidentifiedOption3;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UIHelper;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class UI extends ClientModule {
   public static int a_;
   public static float b;
   public static DrawScreenScreen drawScreenScreen;
   public static UI INSTANCE;
   private UnidentifiedHelper7 unidentifiedHelper7 = b(Easing.BACK_OUT);
   Setting style = this.registerSetting(new EnumSetting("Style", "Theme", AstolfoMioMode.Astolfo, AstolfoMioMode.values()));
   Setting blur = this.registerSetting(new BooleanSetting("Blur", "Adds a blur background to the panels", false));
   Setting scale = this.registerSetting(new NumberSetting("Scale", "UI Scale", 0.5F, 1.0F, 2.0F));
   Setting buttonHeight = this.registerSetting(new NumberSetting("ButtonHeight", "The button height", 0, 4, 6));
   Setting scrollSpeed = this.registerSetting(new NumberSetting("ScrollSpeed", "The speed of GUI scrolling", 5, 30, 100));
   Setting sounds = this.registerSetting(new BooleanSetting("Sounds", "Click sounds", true));
   Setting descriptions = this.registerSetting(new BooleanSetting("Descriptions", "Shows feature descriptions", true));
   Setting tips = this.registerSetting(new BooleanSetting("Tips", "Shows tips", true));
   Setting lowerCase = this.registerSetting(new BooleanSetting("LowerCase", "All text to lowercase", false));
   Setting gear = this.registerSetting(new BooleanSetting("Gear", "Draw the gear", false));
   Setting grayText = this.registerSetting(new BooleanSetting("GrayText", "Gray text for disabled module", false));
   Setting lightTheme = this.registerSetting(new BooleanSetting("LightTheme", "Black text", false, UI::isLightThemeVisible));
   public Setting backGround = this.registerSetting(createColorSetting("BackGround", "The background color", new Color(37, 37, 37), this::isBackGroundVisible));
   public Setting frame = this.registerSetting(createColorSetting("Frame", "The frame color", new Color(26, 26, 26), this::isFrameVisible));
   public Setting outline = this.registerSetting(new BooleanSetting("Outline", "Draw frame outline", false, this::isOutlineVisible));
   public Setting icon = this.registerSetting(new BooleanSetting("Icon", "Draw module category icon", false, this::isIconVisible));
   public Setting interval = this.registerSetting(new BooleanSetting("Interval", "Add interval", false, this::isIntervalVisible));
   public Setting moduleButton = this.registerSetting(new BooleanSetting("ModuleButton", "Draw module button", true, this::isModuleButtonVisible));
   public Setting fadeButton = this.registerSetting(new EnumSetting("FadeButton", "Gradient", UnidentifiedOption3.a_, UnidentifiedOption3.values(), this::isFadeButtonVisible));
   public Setting frameStyle = this.registerSetting(new EnumSetting("FrameStyle", "Style", UnidentifiedOption4.a_, UnidentifiedOption4.values(), this::isFrameStyleVisible));
   Setting tintStart = this.registerSetting(createColorSetting("TintStart", "Tint 1", p(50), false));
   Setting tintMid = this.registerSetting(createColorSetting("TintMid", "Tint 2", p(100), false));
   Setting tintEnd = this.registerSetting(createColorSetting("TintEnd", "Tint 3", p(255), true));
   Setting rainbow = this.registerSetting(new EnumSetting("Rainbow", "The rendering mode for rainbow", OffGradientMode.OFF, OffGradientMode.values()));
   Setting speed = this.registerSetting(new NumberSetting("Speed", "The speed for the rainbow color cycling", 0.1F, 50.0F, 100.0F, this::isSpeedVisible));
   Setting saturation = this.registerSetting(new NumberSetting("Saturation", "The saturation of rainbow colors", 0, 35, 100, this::isSaturationVisible));
   Setting brightness = this.registerSetting(new NumberSetting("Brightness", "The brightness of rainbow colors", 0, 100, 100, this::isBrightnessVisible));
   Setting difference = this.registerSetting(new NumberSetting("Difference", "The difference offset for rainbow colors", 0.1F, 5.0F, 40.0F, this::isDifferenceVisible));
   public static final Color G = new Color(37, 37, 37);
   public static final Color H = new Color(26, 26, 26);

   public UI() {
      super("UI", "Opens the clickgui screen", ModuleCategory.CLIENT, 344);
      INSTANCE = this;
   }

   public static UI atK() {
      return INSTANCE;
   }

   @Override
   public void awd() {
      if (client.player != null && client.world != null) {
         DrawScreenScreen.b = (Float)this.scale.atx();
         a_ = (Integer)this.buttonHeight.atx() + 1;
         b = 1.5F + ((Integer)this.buttonHeight.atx()).intValue() / 2.0F;
         this.unidentifiedHelper7 = new UnidentifiedHelper7(false, '\u0000', 300.0F, 496665058, '楚', Easing.BACK_OUT);
         this.unidentifiedHelper7.qV(true);
         this.unidentifiedHelper7.rc();
         DrawScreenScreen.h = false;
         DrawScreenScreen.i = false;
         DrawScreenScreen.f = false;
         DrawScreenScreen.g = false;
         if (drawScreenScreen == null) {
            drawScreenScreen = new DrawScreenScreen(this);
            Bootstrap.clientPaths.aof();
         }

         DrawScreenScreen drawScreenScreen2 = drawScreenScreen;
         client.setScreen(drawScreenScreen2);
      } else {
         this.awc();
      }
   }

   @EventListener
   public void atL(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (!(client.currentScreen instanceof DrawScreenScreen)) {
         this.awc();
      }
   }

   @Override
   public void awe() {
      if (client.player != null && client.world != null) {
         if (drawScreenScreen != null) {
            Bootstrap.clientPaths.aoe();
         }

         MinecraftClient temp = client;
         Runnable runnable = UI::aun;
         temp.execute(runnable);
      }
   }

   public static Color atM() {
      return (Color)INSTANCE.tintStart.atx();
   }

   public static Color atN() {
      return (Color)INSTANCE.tintMid.atx();
   }

   public static Color atO() {
      return (Color)INSTANCE.tintEnd.atx();
   }

   public static Color atP() {
      return aum().QB();
   }

   public int atQ(int n) {
      return ColorHelper3.auO().auW((int)(n * this.auj())).getRGB();
   }

   public int atR(float f2) {
      return ColorHelper3.auO().auW((int)(100.0F * f2 * this.auj())).getRGB();
   }

   public int atS(int n, float f) {
      return ColorHelper3.auO().auW((int)(n * f * this.auj())).getRGB();
   }

   public int atT() {
      return a_;
   }

   public float atU() {
      return b;
   }

   public int atV(int n) {
      float f2 = this.auj();
      if (f2 == 1.0F) {
         return n;
      }

      float f3 = n >> 24 & 0xFF;
      f2 = Math.clamp(f2, 0.0F, 1.0F);
      return Math.max(0, (int)(f3 * f2)) << 24 | n & 16777215;
   }

   public int atW(double d) {
      switch (UIHelper.a[((OffGradientMode)this.rainbow.atx()).ordinal()]) {
         case 1:
            return ColorHelper3.auO().auX();
         case 2:
            return this.aua(1.0);
         case 3:
            return this.aua(d);
         case 4:
            return this.atX(d);
         case 5:
            return this.atZ(d);
         case 6:
            return this.atY(d);
         default:
            throw new MatchException(null, null);
      }
   }

   private int atX(double d) {
      double temp = (double)System.currentTimeMillis() * ((Float)this.speed.atx()).floatValue() + d * 500.0;
      Setting setting2 = this.difference;
      temp %= 30000.0F / ((Float)setting2.atx() / 100.0F);
      setting2 = this.difference;
      float f2 = (float)(temp / (30000.0F / ((Float)setting2.atx() / 16.6667F))) % 2.0F;
      if (f2 < 0.0F) {
         f2 += 2.0F;
      }

      return ColorUtils.iA(MathHelper.clamp(1.0F - Math.abs(f2 - 1.0F), 0.0F, 1.0F), ColorHelper3.auO().auU(), ColorHelper3.auO().auT()).getRGB();
   }

   private int atY(double d) {
      double temp = (double)System.currentTimeMillis() * ((Float)this.speed.atx()).floatValue() + d * 500.0;
      Setting setting2 = this.difference;
      temp %= 30000.0F / ((Float)setting2.atx() / 200.0F);
      setting2 = this.difference;
      float f2 = (float)(temp / (30000.0F / ((Float)setting2.atx() / 40.0F))) % 1.0F;
      if (f2 > 0.5) {
         f2 = 0.5F - (f2 - 0.5F);
      }

      float f3 = ((Integer)this.saturation.atx()).intValue() / 100.0F;
      float f4 = ((Integer)this.brightness.atx()).intValue() / 100.0F;
      float f5 = f3;
      return Color.HSBtoRGB(f2, f5, f4);
   }

   private int atZ(double d) {
      double temp = (double)System.currentTimeMillis() * ((Float)this.speed.atx()).floatValue() + d * 500.0;
      Setting setting2 = this.difference;
      temp %= 30000.0F / ((Float)setting2.atx() / 200.0F);
      setting2 = this.difference;
      float f2 = (float)(temp / (30000.0F / ((Float)setting2.atx() / 40.0F))) % 1.0F;
      if (f2 > 0.5) {
         f2 = 0.5F - (f2 - 0.5F);
      }

      f2 += 0.5F;
      float f3 = ((Integer)this.saturation.atx()).intValue() / 100.0F;
      float f4 = ((Integer)this.brightness.atx()).intValue() / 100.0F;
      float f5 = f3;
      return Color.HSBtoRGB(f2, f5, f4);
   }

   private int aua(double d) {
      double temp = (double)System.currentTimeMillis() * ((Float)this.speed.atx()).floatValue() + d * 500.0;
      Setting setting2 = this.difference;
      temp %= 30000.0F / ((Float)setting2.atx() / 100.0F);
      setting2 = this.difference;
      float f2 = (float)(temp / (30000.0F / ((Float)setting2.atx() / 20.0F)));
      float f3 = ((Integer)this.saturation.atx()).intValue() / 100.0F;
      float f4 = ((Integer)this.brightness.atx()).intValue() / 100.0F;
      float f5 = f3;
      return Color.HSBtoRGB(f2, f5, f4);
   }

   public boolean aub() {
      return (Boolean)this.blur.atx();
   }

   public boolean auc() {
      return (Boolean)this.sounds.atx();
   }

   public boolean aud() {
      return (Boolean)this.lowerCase.atx();
   }

   public boolean aue() {
      return (Boolean)this.grayText.atx();
   }

   public static boolean auf() {
      boolean flag;
      if (aum() == AstolfoMioMode.Custom && (Boolean)INSTANCE.lightTheme.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean aug() {
      return (Boolean)this.gear.atx();
   }

   public boolean auh() {
      return (Boolean)this.tips.atx();
   }

   public boolean aui() {
      return (Boolean)this.descriptions.atx();
   }

   public float auj() {
      return (float)this.unidentifiedHelper7.qW();
   }

   public float auk() {
      return (float)this.unidentifiedHelper7.qW();
   }

   public int aul() {
      return (Integer)this.scrollSpeed.atx();
   }

   public static AstolfoMioMode aum() {
      return (AstolfoMioMode)INSTANCE.style.atx();
   }

   private static void aun() {
      if (client.currentScreen instanceof DrawScreenScreen) {
         client.player.closeScreen();
      }
   }

   private Boolean isDifferenceVisible() {
      boolean flag;
      if (this.rainbow.atx() != OffGradientMode.OFF) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBrightnessVisible() {
      boolean flag;
      if (this.rainbow.atx() != OffGradientMode.OFF && this.rainbow.atx() != OffGradientMode.GRADIENT) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSaturationVisible() {
      boolean flag;
      if (this.rainbow.atx() != OffGradientMode.OFF && this.rainbow.atx() != OffGradientMode.GRADIENT) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSpeedVisible() {
      boolean flag;
      if (this.rainbow.atx() != OffGradientMode.OFF) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isFrameStyleVisible() {
      boolean flag;
      if (this.style.atx() == AstolfoMioMode.Custom) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isFadeButtonVisible() {
      boolean flag;
      if (this.style.atx() == AstolfoMioMode.Custom && (Boolean)this.moduleButton.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isModuleButtonVisible() {
      boolean flag;
      if (this.style.atx() == AstolfoMioMode.Custom) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isIntervalVisible() {
      boolean flag;
      if (this.style.atx() == AstolfoMioMode.Custom) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isIconVisible() {
      boolean flag;
      if (this.style.atx() == AstolfoMioMode.Custom) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isOutlineVisible() {
      boolean flag;
      if (this.style.atx() == AstolfoMioMode.Custom) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isFrameVisible() {
      boolean flag;
      if (this.style.atx() == AstolfoMioMode.Custom) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBackGroundVisible() {
      boolean flag;
      if (this.style.atx() == AstolfoMioMode.Custom) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static Boolean isLightThemeVisible() {
      return false;
   }

   private static MatchException p() {
      return new MatchException(null, null);
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color, Supplier supplier) {
      return new ColorSetting(str, str2, color, true, false, supplier);
   }

   private static UnidentifiedHelper7 b(Easing easing) {
      return new UnidentifiedHelper7(false, '\u0000', 300.0F, 496665058, '楚', easing);
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color, boolean flag) {
      return new ColorSetting(str, str2, color, true, flag);
   }

   private static Color p(int n) {
      return new Color(0, 0, 0, n);
   }
}
