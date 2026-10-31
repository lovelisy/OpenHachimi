package pw.hachimi.client.module;

import java.text.DecimalFormat;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.model.movement.SpeedsKMHBPSMode;
import pw.hachimi.client.module.misc.Timer;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.StopwatchImpl;

public class Speeds extends DrawRenderModule {
   public Speeds() {
      super("Speeds", "");
   }

   Setting kMHBPSMode = this.registerSetting(new EnumSetting("Speed", "Displays the current movement speed of the player", SpeedsKMHBPSMode.K_M_H, SpeedsKMHBPSMode.values()));
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Correct", "Display real speed", true));
   Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("WithY", "With y speed", false));
   private final DecimalFormat decimalFormat = new DecimalFormat("0.0#");
   Vec3d vec;
   Vec3d vec2;
   StopwatchImpl stopwatchImpl = new StopwatchImpl();

   @Override
   public void adb() {
      double d;
      if ((Boolean)this.booleanSetting1_1.atx()) {
         if (this.stopwatchImpl.afq(500)) {
            this.vec = this.vec2;
            this.vec2 = client.player.getPos();
            this.stopwatchImpl.afr(29432, 5265492, (byte)40);
         }

         if (this.vec != null && this.vec2 != null) {
            double d2 = (this.vec.getX() - this.vec2.getX()) / 10.0;
            double d3 = (this.vec.getY() - this.vec2.getY()) / 10.0 * ((Boolean)this.booleanSetting2_1.atx() ? 1 : 0);
            double d4 = (this.vec.getZ() - this.vec2.getZ()) / 10.0;
            if (this.kMHBPSMode.atx() == SpeedsKMHBPSMode.K_M_H) {
               d = Math.sqrt(d2 * d2 + d4 * d4 + d3 * d3) / 1000.0 / 1.388888888888889E-5;
            } else {
               d2 *= 20.0;
               d3 *= 20.0;
               d4 *= 20.0;
               d = Math.abs(Math.sqrt(d2 * d2 + d4 * d4 + d3 * d3));
            }
         } else {
            d = 0.0;
         }
      } else {
         double d5 = client.player.getX() - client.player.prevX;
         double d6 = (client.player.getY() - client.player.prevY) * ((Boolean)this.booleanSetting2_1.atx() ? 1 : 0);
         double d7 = client.player.getZ() - client.player.prevZ;
         float f2 = Timer.fP().awg() ? Timer.fP().fS() : 1.0F;
         if (this.kMHBPSMode.atx() == SpeedsKMHBPSMode.K_M_H) {
            d = Math.sqrt(d5 * d5 + d7 * d7 + d6 * d6) / 1000.0 / 1.388888888888889E-5 * f2;
         } else {
            d5 *= 20.0;
            d6 *= 20.0;
            d7 *= 20.0;
            d = Math.abs(Math.sqrt(d5 * d5 + d7 * d7 + d6 * d6)) * f2;
         }
      }

      Object[] temp = new Object[2];
      double d8 = d;
      temp[0] = this.decimalFormat.format(d8);
      temp[1] = this.kMHBPSMode.atx() == SpeedsKMHBPSMode.K_M_H ? "km/h" : "b/s";
      Object[] objs = temp;
      this.text = String.format("Speed §f%s%s", objs);
      super.adb();
   }
}
