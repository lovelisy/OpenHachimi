package pw.hachimi.client.event.world;

import net.minecraft.entity.Entity;
import pw.hachimi.client.event.Event;

public class AddEntity_2Event extends Event {
   private final Entity entity;

   public AddEntity_2Event(Entity entity2) {
      this.entity = entity2;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
