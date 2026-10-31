package pw.hachimi.client.utils.render;

import java.awt.Color;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.utils.UnidentifiedHelper7;

public class KillEffectsVec3dBoxHelper {
   public final Vec3d vec;
   public static final Color b = new Color(0, 0, 0, 0);
   public final Color color;
   public boolean d = true;
   public final UnidentifiedHelper7 unidentifiedHelper7;
   private static final long a = 0L;

   public KillEffectsVec3dBoxHelper(Vec3d vec2, Color color2, long l) {
      this.unidentifiedHelper7 = a((float)l, Easing.QUINT_OUT);
      this.vec = vec2;
      this.color = color2;
   }

   public boolean lI_() {
      return this.d;
   }

   public void lJ(Render3DContext render3DContext) {
      boolean flag;
      if (this.unidentifiedHelper7.qW() >= 0.97) {
         flag = true;
      } else {
         flag = false;
      }

      this.d = flag;
      double d2 = this.unidentifiedHelper7.qW() * 8.0;
      double d3 = (1.0 - this.unidentifiedHelper7.qW()) * 1.2F;
      Box box = new Box(this.vec.x - d3, this.vec.y, this.vec.z - d3, this.vec.x + d3, this.vec.y + d2, this.vec.z + d3);
      render3DContext.boxRenderer.Ze(box, ShapeMode.Sides, b, this.color);
   }

   public KillEffectsVec3dBoxHelper lK() {
      this.unidentifiedHelper7.rc();
      return this;
   }

   private static UnidentifiedHelper7 a(float f, Easing easing) {
      return new UnidentifiedHelper7(true, '\u0000', f, 496665058, '楚', easing);
   }
}
