package pw.hachimi.client.event.render;

import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.UnknownEvent9;

@UnidentifiedInterface2
public class GetFogColorOverrideEvent extends UnknownEvent9 {
   private final float a;

   public GetFogColorOverrideEvent(float f) {
      this.a = f;
   }

   public float nG() {
      return this.a;
   }
}
