package pw.hachimi.client.event.render;

import net.minecraft.client.render.Camera;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class RenderEvent14 extends Event {
   public Camera camera;
   public static boolean dn;

   public RenderEvent14(Camera camera2) {
      this.camera = camera2;
   }

   public Camera getCamera() {
      return this.camera;
   }
}
