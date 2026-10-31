package pw.hachimi.client.event.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.Event;

public final class GetCameraPosVecEvent extends Event {
   private final Entity entity;
   private final float b;
   private Vec3d vec;
   public static boolean gu;

   public GetCameraPosVecEvent(float f, Entity entity2, Vec3d vec2) {
      this.entity = entity2;
      this.b = f;
      this.vec = vec2;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public float Oq() {
      return this.b;
   }

   public Vec3d getVec() {
      return this.vec;
   }

   public void setVec(Vec3d vec2) {
      this.vec = vec2;
   }
}
