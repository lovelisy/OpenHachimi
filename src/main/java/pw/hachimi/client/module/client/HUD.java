package pw.hachimi.client.module.client;

import java.awt.Color;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.Window;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.event.FillEvent;
import pw.hachimi.client.event.SetScreenEvent;
import pw.hachimi.client.event.render.RenderHeldItemTooltipEvent;
import pw.hachimi.client.event.render.RenderStatusEffectOverlayEvent;
import pw.hachimi.client.gui.DrawBufferScreen;
import pw.hachimi.client.gui.DrawScreenScreen;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.HUDMoveHideMode;
import pw.hachimi.client.model.render.OffGradientMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.BooleanSetting5;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class HUD extends ClientModule {
   private static HUD INSTANCE;
   private final UnidentifiedHelper7 unidentifiedHelper7 = a(Easing.CUBIC_OUT);
   private static DrawBufferScreen drawBufferScreen;
   Setting editor;
   Setting potionHud;
   Setting itemName;
   Setting dynamicY;
   Setting xOffset;
   Setting yOffset;
   Setting blurRadius;
   Setting rainbow;
   Setting speed;
   Setting saturation;
   Setting brightness;
   Setting difference;
   public static boolean cc;

   public HUD() {
      super("HUD", "Displays the HUD (heads up display) screen.", ModuleCategory.CLIENT);
      BooleanSetting5 booleanSetting5 = c("Editor", "Open hud editor screen", this::isEditorVisible);
      this.editor = this.registerSetting(booleanSetting5);
      this.potionHud = this.registerSetting(new EnumSetting("PotionHud", "Renders the Minecraft potion Hud", HUDMoveHideMode.HIDE, HUDMoveHideMode.values()));
      this.itemName = this.registerSetting(new EnumSetting("ItemName", "Renders the Minecraft item name display", HUDMoveHideMode.HIDE, HUDMoveHideMode.values()));
      this.dynamicY = this.registerSetting(new BooleanSetting("DynamicY", "Display above hotbar", false, this::isDynamicYVisible));
      this.xOffset = this.registerSetting(new NumberSetting("XOffset", "Item name display offset", -200, 0, 200, this::isXOffsetVisible));
      this.yOffset = this.registerSetting(new NumberSetting("YOffset", "Item name display offset", -200, 0, 200, this::isYOffsetVisible));
      this.blurRadius = this.registerSetting(new NumberSetting("BlurRadius", "", 1.0F, 8.0F, 20.0F));
      this.rainbow = this.registerSetting(new EnumSetting("Rainbow", "The rendering mode for rainbow", OffGradientMode.OFF, OffGradientMode.values()));
      this.speed = this.registerSetting(new NumberSetting("Speed", "The speed for the rainbow color cycling", 0.1F, 50.0F, 100.0F, this::isSpeedVisible));
      this.saturation = this.registerSetting(new NumberSetting("Saturation", "The saturation of rainbow colors", 0, 35, 100, this::isSaturationVisible));
      this.brightness = this.registerSetting(new NumberSetting("Brightness", "The brightness of rainbow colors", 0, 100, 100, this::isBrightnessVisible));
      this.difference = this.registerSetting(new NumberSetting("Difference", "The difference offset for rainbow colors", 0.1F, 10.0F, 40.0F, this::isDifferenceVisible));
      INSTANCE = this;
      this.awb();
   }

   public static HUD zg() {
      return INSTANCE;
   }

   public float zh() {
      return (Float)this.blurRadius.atx();
   }

   public void isEditorVisible() {
      if (client.player != null && client.world != null) {
         DrawScreenScreen.h = false;
         DrawScreenScreen.i = false;
         DrawScreenScreen.f = false;
         DrawScreenScreen.g = false;
         if (drawBufferScreen == null) {
            drawBufferScreen = new DrawBufferScreen(this);
         }

         DrawBufferScreen drawBufferScreen2 = drawBufferScreen;
         client.setScreen(drawBufferScreen2);
      } else {
         this.awc();
      }
   }

   public double zj() {
      return 14.0 * this.unidentifiedHelper7.qW();
   }

   @EventListener
   public void onFill(FillEvent fillEvent) {
      fillEvent.aim();
      fillEvent.y((float)this.unidentifiedHelper7.qW());
   }

   @EventListener
   public void onSetScreen(SetScreenEvent setScreenEvent) {
      if (setScreenEvent.getScreen() == null && this.unidentifiedHelper7.qU()) {
         this.unidentifiedHelper7.qV(false);
      } else if (setScreenEvent.getScreen() instanceof ChatScreen) {
         this.unidentifiedHelper7.qV(true);
      }
   }

   @EventListener
   public void onRenderStatusEffectOverlay(RenderStatusEffectOverlayEvent renderStatusEffectOverlayEvent) {
      if (this.potionHud.atx() == HUDMoveHideMode.HIDE) {
         renderStatusEffectOverlayEvent.aim();
      }
   }

   @EventListener
   public void onRenderHeldItemTooltip(RenderHeldItemTooltipEvent renderHeldItemTooltipEvent) {
      if (this.itemName.atx() != HUDMoveHideMode.KEEP) {
         renderHeldItemTooltipEvent.aim();
      }

      if (this.itemName.atx() == HUDMoveHideMode.MOVE) {
         Window window = client.getWindow();
         int temp = window.getScaledWidth() / 2;
         TextRenderer temp2 = client.textRenderer;
         String str = renderHeldItemTooltipEvent.getText().getString();
         int n = temp - temp2.getWidth(str) / 2 + (Integer)this.xOffset.atx();
         int n2 = window.getScaledHeight() - 9 - 59 - (Integer)this.yOffset.atx();
         if ((Boolean)this.dynamicY.atx()) {
            if (!client.player.getInventory().armor.isEmpty()) {
               if (client.player.getAbsorptionAmount() > 0.0F) {
                  n2 -= 9;
               }

               n2 -= 9;
            } else if (client.player.getAbsorptionAmount() > 0.0F) {
               n2 -= 9;
            }
         }

         renderHeldItemTooltipEvent.gD(n);
         renderHeldItemTooltipEvent.gF(n2);
      }
   }

   public int zo(double d) {
      return switch ((OffGradientMode)this.rainbow.atx()) {
         case OFF -> ColorHelper3.auO().auX();
         case GRADIENT -> this.zp(d);
         case HUE -> this.zs(d);
         case STATIC -> this.zs(1.0);
         case ASTOLFO -> this.zr(d);
         case GAY -> this.zq(d);
      };
   }

   private int zp(double d) {
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

   private int zq(double d) {
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

   private int zr(double d) {
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

   private int zs(double d) {
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

   private Boolean isYOffsetVisible() {
      boolean flag;
      if (this.itemName.atx() == HUDMoveHideMode.MOVE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isXOffsetVisible() {
      boolean flag;
      if (this.itemName.atx() == HUDMoveHideMode.MOVE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isDynamicYVisible() {
      boolean flag;
      if (this.itemName.atx() == HUDMoveHideMode.MOVE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static MatchException f() {
      return new MatchException(null, null);
   }

   private static UnidentifiedHelper7 a(Easing easing) {
      return new UnidentifiedHelper7(false, '\u0000', 200.0F, 496665058, '楚', easing);
   }

   private static BooleanSetting5 c(String str, String str2, Runnable runnable) {
      return new BooleanSetting5(str, str2, runnable);
   }
}
