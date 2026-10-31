package pw.hachimi.client.model.world;

import net.minecraft.util.math.Vec3d;

public record StartEnd(Vec3d start, Vec3d end) {
   public Vec3d getEnd() {
      return this.end;
   }
}
