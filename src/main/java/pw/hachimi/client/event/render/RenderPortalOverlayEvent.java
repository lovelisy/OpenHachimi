package pw.hachimi.client.event.render;

import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class RenderPortalOverlayEvent extends DrawContextEvent {
   public RenderPortalOverlayEvent(DrawContext context) {
      super(context);
   }

   public static boolean ci;
}
