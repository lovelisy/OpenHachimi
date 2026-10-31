package pw.hachimi.client.utils.render;

import java.awt.Color;
import java.util.UUID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.render.PopChams;
import pw.hachimi.client.player.PlayerWorldOtherClientPlayerEntity;
import pw.hachimi.client.setting.Setting;

public class PopChamsVec3dPlayerHelper extends PlayerWorldOtherClientPlayerEntity {
   public final UUID uuid;
   private final UnidentifiedHelper9 unidentifiedHelper9;
   final PopChams popChams;
   private static final String e = "ghost";

   public PopChamsVec3dPlayerHelper(PopChams popChams2, PlayerEntity player2) {
      super(player2, "ghost", false);
      this.popChams = popChams2;
      this.uuid = player2.getUuid();
      this.unidentifiedHelper9 = new UnidentifiedHelper9();
   }

   public boolean asW(Render3DContext render3DContext) {
      double d = this.unidentifiedHelper9.MH(1.0, ((Integer)this.popChams.animationTime.atx()).intValue(), (Easing)this.popChams.easing.atx());
      if (d >= 1.0) {
         return true;
      }

      Color color;
      Color color2;
      if ((Boolean)this.popChams.fadeOut.atx()) {
         color = ColorUtils.iE((Color)this.popChams.side.atx(), (int)(((Color)this.popChams.side.atx()).getAlpha() * (1.0 - d)));
         color2 = ColorUtils.iE((Color)this.popChams.line.atx(), (int)(((Color)this.popChams.line.atx()).getAlpha() * (1.0 - d)));
      } else {
         color = (Color)this.popChams.side.atx();
         color2 = (Color)this.popChams.line.atx();
      }

      Vec3d vec = this.getPos();
      double temp = vec.getX();
      double temp2 = vec.getY() + ((Float)this.popChams.yadd.atx()).floatValue() * d;
      double d2 = vec.getZ();
      double d3 = temp2;
      double d4 = temp;
      this.setPos(d4, d3, d2);
      double temp3 = ((Float)this.popChams.scale.atx()).floatValue() + ((Float)this.popChams.scaleTo.atx() - (Float)this.popChams.scale.atx()) * d;
      ShapeMode shapeMode = (ShapeMode)this.popChams.shape.atx();
      Setting setting2 = this.popChams.spin;
      ShapeMode shapeMode2 = shapeMode;
      float f = (float)(((Float)setting2.atx()).floatValue() * d);
      setting2 = this.popChams.noSwing;
      float f2 = f;
      ShapeMode shapeMode3 = shapeMode2;
      boolean temp4 = (Boolean)setting2.atx();
      setting2 = this.popChams.sneaking;
      boolean flag = temp4;
      f2 = f2;
      ShapeMode shapeMode4 = shapeMode3;
      boolean flag2 = (Boolean)setting2.atx();
      boolean flag3 = flag;
      f2 = f2;
      double d5 = temp3;
      MatrixModelPartUtils.hY(render3DContext, 1.0F, this, d5, color, color2, shapeMode4, f2, flag3, flag2);
      temp = vec.getX();
      double d6 = vec.getZ();
      double d7 = vec.getY();
      double d8 = temp;
      this.setPos(d8, d7, d6);
      return false;
   }
}
