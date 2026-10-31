package pw.hachimi.client.model.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public record EntityTicks(Entity entity, int ticks, Vec3d playerPos, Vec3d offset, double speed) {
   public static boolean g;
}
