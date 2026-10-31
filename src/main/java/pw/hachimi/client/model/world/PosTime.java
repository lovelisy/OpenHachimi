package pw.hachimi.client.model.world;

import net.minecraft.util.math.Vec3d;

public record PosTime(Vec3d pos, long time) {
   public Vec3d getPos() {
      return this.pos;
   }

   public long getTime() {
      return this.time;
   }
}
