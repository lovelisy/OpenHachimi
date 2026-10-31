package pw.hachimi.client.event.render;

import net.minecraft.client.gui.DrawContext;

import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class RenderOverlayEvent2 extends DrawContextEvent {
   public RenderOverlayEvent2(DrawContext context) {
      super(context);
   }

}
