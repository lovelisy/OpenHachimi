package pw.hachimi.client.event.world;

import net.minecraft.entity.Entity;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class ChangeLookDirectionEvent extends Event {
   private final Entity entity;
   private final double b;
   private final double c;
   public static boolean dh;

   public ChangeLookDirectionEvent(Entity entity2, double d, double d2) {
      this.entity = entity2;
      this.b = d;
      this.c = d2;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public double kQ() {
      return this.b;
   }

   public double kR() {
      return this.c;
   }
}
