package pw.hachimi.client.model.world;

import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;

public record PosTime2(Vec3d pos, long time) implements Position {
   public static boolean m;

   public double getX() {
      return this.pos.getX();
   }

   public double getY() {
      return this.pos.getY();
   }

   public double getZ() {
      return this.pos.getZ();
   }

   public Vec3d getPos() {
      return this.pos;
   }

   public long getTime() {
      return this.time;
   }
}
