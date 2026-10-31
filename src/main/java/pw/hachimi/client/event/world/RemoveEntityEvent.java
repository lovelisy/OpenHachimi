package pw.hachimi.client.event.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import pw.hachimi.client.event.Event;

public class RemoveEntityEvent extends Event {
   private final Entity entity;
   private final RemovalReason removalReason;
   public static boolean gf;

   public RemoveEntityEvent(Entity entity2, RemovalReason removalReason2) {
      this.entity = entity2;
      this.removalReason = removalReason2;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public RemovalReason getRemovalReason() {
      return this.removalReason;
   }
}
