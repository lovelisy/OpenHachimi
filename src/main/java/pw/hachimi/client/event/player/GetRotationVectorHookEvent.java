package pw.hachimi.client.event.player;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class GetRotationVectorHookEvent extends Event {
   public LivingEntity living;
   public float b;
   public float c;

   public GetRotationVectorHookEvent(LivingEntity living2, float f, float f2) {
      this.living = living2;
      this.b = f;
      this.c = f2;
   }

   public final Vec3d nF() {
      float f = this.c * (float) (Math.PI / 180.0);
      float f2 = -this.b * (float) (Math.PI / 180.0);
      float f3 = MathHelper.cos(f2);
      float f4 = MathHelper.sin(f2);
      float f5 = MathHelper.cos(f);
      float f6 = MathHelper.sin(f);
      return new Vec3d(f4 * f5, -f6, f3 * f5);
   }
}
