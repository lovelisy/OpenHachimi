package pw.hachimi.client.event.render;

import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class RenderUnderwaterOverlayEvent extends DrawContextEvent {
   public RenderUnderwaterOverlayEvent(DrawContext context) {
      super(context);
   }

}
