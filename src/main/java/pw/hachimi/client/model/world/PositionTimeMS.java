package pw.hachimi.client.model.world;

import net.minecraft.util.math.Vec3d;

public record PositionTimeMS(Vec3d position, long timeMS, int teleportID) {
   public long bf() {
      return System.currentTimeMillis() - this.timeMS;
   }
}
