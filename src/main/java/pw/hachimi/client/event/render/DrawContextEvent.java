package pw.hachimi.client.event.render;

import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.event.Event;

public class DrawContextEvent extends Event {
   private final DrawContext context;
   public static boolean ej;

   public DrawContextEvent(DrawContext context2) {
      this.context = context2;
   }

   public DrawContext getContext() {
      return this.context;
   }
}
