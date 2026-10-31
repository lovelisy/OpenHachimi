package pw.hachimi.client.event.render;

import net.minecraft.entity.Entity;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class RenderLabelIfPresentEvent extends Event {
   private final Entity entity;
   public static boolean bh;

   public RenderLabelIfPresentEvent(Entity entity2) {
      this.entity = entity2;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
