package pw.hachimi.client.event.world;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;

public record PosSoundEvent(Vec3d pos, SoundEvent soundEvent) {
   public static boolean e;

   public Vec3d getPos() {
      return this.pos;
   }
}
