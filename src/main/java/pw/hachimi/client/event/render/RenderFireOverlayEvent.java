package pw.hachimi.client.event.render;

import net.minecraft.client.gui.DrawContext;

import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class RenderFireOverlayEvent extends DrawContextEvent {
   public RenderFireOverlayEvent(DrawContext context) {
      super(context);
   }

}
