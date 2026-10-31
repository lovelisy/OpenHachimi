package pw.hachimi.client.event.render;

import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class RenderCrosshairEvent extends Event {
   private final DrawContext context;
   public static boolean l;

   public RenderCrosshairEvent(DrawContext context2) {
      this.context = context2;
   }

   public DrawContext getContext() {
      return this.context;
   }
}
