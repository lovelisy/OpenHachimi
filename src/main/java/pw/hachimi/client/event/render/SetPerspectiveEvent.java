package pw.hachimi.client.event.render;

import net.minecraft.client.option.Perspective;
import pw.hachimi.client.event.Event;

public class SetPerspectiveEvent extends Event {
   private final Perspective perspective;
   public static boolean gx;

   public SetPerspectiveEvent(Perspective perspective2) {
      this.perspective = perspective2;
   }

   public Perspective getPerspective() {
      return this.perspective;
   }
}
