package pw.hachimi.client.event.movement;

import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.UnknownEvent8;

@UnidentifiedInterface2
public class TravelHeadEvent extends UnknownEvent8 {
   private final Vec3d vec;
   public static boolean r;

   public TravelHeadEvent(Vec3d vec2) {
      this.vec = vec2;
   }

   public Vec3d getVec() {
      return this.vec;
   }
}
