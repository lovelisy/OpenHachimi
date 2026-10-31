package pw.hachimi.client.event.render;

import net.minecraft.entity.Entity;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class GetTeamColorValueEvent extends Event {
   private final Entity entity;
   private int b;
   public static boolean eb;

   public GetTeamColorValueEvent(Entity entity2) {
      this.entity = entity2;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public int oH() {
      return this.b;
   }

   public void oI(int n) {
      this.b = n;
   }
}
