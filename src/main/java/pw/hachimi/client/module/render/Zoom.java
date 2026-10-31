package pw.hachimi.client.module.render;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.OnKeyEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.KeybindSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper17;
import pw.hachimi.eventbus.annotation.EventListener;

public class Zoom extends ClientModule {
   Setting zoom = this.registerSetting(new NumberSetting("Zoom", "The zoom value", 10, 30, 50));
   Setting smoothCamera = this.registerSetting(new BooleanSetting("SmoothCamera", "Adds motion reduction to the camera", true));
   Setting zoomKey = this.registerSetting(new KeybindSetting("ZoomKey", "The zoom key bind", l(this.Kn() + "-zoomkey", Zoom::isZoomKeyVisible)));
   private boolean d;
   private boolean e = true;
   private boolean f;
   private int g = 100;
   public static boolean fl;

   public Zoom() {
      super("Zoom", "Zooms in the camera perspective", ModuleCategory.RENDER);
   }

   @EventListener
   public void onOnKey(OnKeyEvent onKeyEvent) {
      if (onKeyEvent.KD() != 2 && onKeyEvent.KC() == ((UnidentifiedHelper17)this.zoomKey.atx()).agt()) {
         boolean flag;
         if (onKeyEvent.KD() == 1) {
            flag = true;
         } else {
            flag = false;
         }

         this.f = flag;
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE && client.currentScreen == null) {
         if (this.f) {
            if (this.e) {
               this.g = (Integer)client.options.getFov().getValue();
               this.e = false;
            }

            GameOptions temp = client.options;
            boolean flag = (Boolean)this.smoothCamera.atx();
            temp.smoothCameraEnabled = flag;
            client.options.hudHidden = true;
            SimpleOption simpleOption = client.options.getFov();
            Integer integer = (Integer)this.zoom.atx();
            simpleOption.setValue(integer);
            this.d = true;
         } else if (this.d) {
            client.options.smoothCameraEnabled = false;
            client.options.hudHidden = false;
            SimpleOption simpleOption2 = client.options.getFov();
            Integer integer2 = this.g;
            simpleOption2.setValue(integer2);
            this.d = false;
            this.e = true;
         }
      }
   }

   private static void isZoomKeyVisible() {
   }

   private static UnidentifiedHelper17 l(String str, Runnable runnable) {
      return new UnidentifiedHelper17(str, 67, runnable);
   }
}
