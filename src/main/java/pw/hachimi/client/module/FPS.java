package pw.hachimi.client.module;

import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.combat.UnidentifiedHelper11;

public class FPS extends DrawRenderModule {
   public FPS() {
      super("FPS", "");
   }

   Setting postEffect = this.registerSetting(new BooleanSetting("ShowLow", "0.1% low fps", true));
   private final UnidentifiedHelper11 unidentifiedHelper11 = new UnidentifiedHelper11();
   private static long c;
   private static long d;
   private static long e;
   private static long f;

   @Override
   public void adb() {
      this.unidentifiedHelper11.Ln();
      Object[] objs = new Object[]{this.unidentifiedHelper11.Lo()};
      this.text = String.format("FPS §f%d", objs);
      if ((Boolean)this.postEffect.atx()) {
         long l = System.nanoTime();
         long l2 = l - d;
         d = l;
         if (l2 > e) {
            e = l2;
         }

         if (l - c > 1000000000L) {
            c = l;
            f = e;
            e = 0L;
         }

         double d2 = 1.0E9 / f;
         this.text = this.text + "§7[" + (int)d2 + "]";
      }

      super.adb();
   }
}
