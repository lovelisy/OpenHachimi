package pw.hachimi.client.event.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.Event;

public class CameraPositionVecEvent extends Event {
   private final float a;
   private final Entity entity;
   private Vec3d vec;

   public CameraPositionVecEvent(Vec3d vec2, Entity entity2, float f) {
      this.vec = vec2;
      this.a = f;
      this.entity = entity2;
   }

   public float yM() {
      return this.a;
   }

   public Vec3d getVec() {
      return this.vec;
   }

   public void setVec(Vec3d vec2) {
      this.vec = vec2;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
