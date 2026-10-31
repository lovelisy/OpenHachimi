package pw.hachimi.client.event.movement;

import net.minecraft.entity.Entity;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class PushAwayFromEvent extends Event {
   private final Entity entity;
   private final Entity entity2;
   public static boolean ak;

   public PushAwayFromEvent(Entity entity3, Entity entity4) {
      this.entity = entity3;
      this.entity2 = entity4;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public Entity getEntity2() {
      return this.entity2;
   }
}
