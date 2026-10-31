package pw.hachimi.client.event.render;

import net.minecraft.client.gui.DrawContext;

public class RenderEvent extends DrawContextEvent {
   private final float a;

   public RenderEvent(DrawContext context, float f) {
      super(context);
      this.a = f;
   }

   public float gA() {
      return this.a;
   }
}
