package pw.hachimi.client.event.combat;

import net.minecraft.entity.Entity;
import pw.hachimi.client.event.Event;

public final class UpdateTargetedEntity_1Event extends Event {
   private final float a;
   private final Entity entity;

   public UpdateTargetedEntity_1Event(float f, Entity entity2) {
      this.a = f;
      this.entity = entity2;
   }

   public float Lj() {
      return this.a;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
