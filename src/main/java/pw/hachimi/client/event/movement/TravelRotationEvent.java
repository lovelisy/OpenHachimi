package pw.hachimi.client.event.movement;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class TravelRotationEvent extends Event {
   public float a;
   public float b;

   public TravelRotationEvent(float f, float f2) {
      this.a = f;
      this.b = f2;
   }

   public final Vec3d KW() {
      float f = this.b * (float) (Math.PI / 180.0);
      float f2 = -this.a * (float) (Math.PI / 180.0);
      float f3 = MathHelper.cos(f2);
      float f4 = MathHelper.sin(f2);
      float f5 = MathHelper.cos(f);
      float f6 = MathHelper.sin(f);
      return new Vec3d(f4 * f5, -f6, f3 * f5);
   }
}
