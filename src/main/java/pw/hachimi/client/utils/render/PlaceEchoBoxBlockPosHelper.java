package pw.hachimi.client.utils.render;

import java.awt.Color;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import pw.hachimi.client.config.ConfigContainer;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.FadeShrinkMode;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.render.PlaceEcho;
import pw.hachimi.client.render.BoxRenderer;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.StopwatchImpl;

public class PlaceEchoBoxBlockPosHelper {
   public final BlockPos pos;
   public final StopwatchImpl stopwatchImpl;
   public boolean c;
   public Box box;
   final PlaceEcho placeEcho;
   public static boolean a;

   public PlaceEchoBoxBlockPosHelper(PlaceEcho placeEcho2, BlockPos pos2) {
      this.placeEcho = placeEcho2;
      this.pos = pos2;
      this.stopwatchImpl = new StopwatchImpl();
      this.c = (Boolean)placeEcho2.noFail.atx();
      this.box = new Box(this.pos);
   }

   public boolean aZ(Render3DContext render3DContext) {
      if (!this.c) {
         BlockPos pos2 = this.pos;
         if (ConfigContainer.client.world.isAir(pos2)) {
            Setting setting2 = this.placeEcho.blockedSide;
            Box box2 = this.box;
            BoxRenderer boxRenderer2 = render3DContext.boxRenderer;
            Box box3 = box2;
            Color color = (Color)setting2.atx();
            setting2 = this.placeEcho.blockedLine;
            Color color2 = color;
            Box box4 = box3;
            boxRenderer2 = boxRenderer2;
            box3 = box4;
            color = color2;
            Color temp = (Color)setting2.atx();
            setting2 = this.placeEcho.boxMode;
            Color color3 = temp;
            Color color4 = color;
            Box box5 = box3;
            boxRenderer2 = boxRenderer2;
            box3 = box5;
            color = color4;
            temp = color3;
            ShapeMode shapeMode = (ShapeMode)setting2.atx();
            Color color5 = temp;
            Color color6 = color;
            box5 = box3;
            boxRenderer2.Za(box5, color6, color5, shapeMode, 0);
            return this.stopwatchImpl.afq((Number)this.placeEcho.timeOut.atx());
         }

         this.c = true;
         this.stopwatchImpl.afr(29432, 5265492, (byte)40);
      }

      double d = (double)this.stopwatchImpl.afs() / ((Integer)this.placeEcho.fadeTime.atx()).intValue();
      if (d >= 1.0) {
         return true;
      }

      d = ((Easing)this.placeEcho.easing.atx()).Yy(d);
      double d2;
      if (this.placeEcho.mode.atx() != FadeShrinkMode.Fade && this.placeEcho.mode.atx() != FadeShrinkMode.Both) {
         d2 = 1.0;
      } else {
         d2 = 1.0 - d;
      }

      double d3 = d2;
      if (this.placeEcho.mode.atx() != FadeShrinkMode.Shrink && this.placeEcho.mode.atx() != FadeShrinkMode.Both) {
         d2 = 0.0;
      } else {
         d2 = d;
      }

      double d4 = d2;
      double d5;
      double d6 = d5 = -d4 * 0.5;
      double d7 = -d4 * 0.5;
      Box box6 = this.box.expand(d7, d6, d5);
      Setting setting3 = this.placeEcho.side;
      BoxRenderer temp2 = render3DContext.boxRenderer;
      Color temp3 = ColorUtils.iE((Color)setting3.atx(), (int)(((Color)this.placeEcho.side.atx()).getAlpha() * d3));
      setting3 = this.placeEcho.line;
      Color color7 = temp3;
      temp2 = temp2;
      temp3 = color7;
      Color temp4 = ColorUtils.iE((Color)setting3.atx(), (int)(((Color)this.placeEcho.line.atx()).getAlpha() * d3));
      setting3 = this.placeEcho.boxMode;
      color7 = temp4;
      Color color8 = temp3;
      temp2 = temp2;
      temp3 = color8;
      temp4 = color7;
      ShapeMode shapeMode2 = (ShapeMode)setting3.atx();
      color7 = temp4;
      color8 = temp3;
      temp2.Za(box6, color8, color7, shapeMode2, 0);
      return false;
   }
}
