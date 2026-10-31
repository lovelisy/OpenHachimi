package pw.hachimi.client.event.world;

import net.minecraft.entity.Entity;
import pw.hachimi.client.event.Event;

public class AddEntityEvent extends Event {
   private final Entity entity;

   public AddEntityEvent(Entity entity2) {
      this.entity = entity2;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
