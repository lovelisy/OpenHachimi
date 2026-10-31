package pw.hachimi.client.event;

import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class ClipToSpaceEvent extends Event {
   private float a;
   public static boolean ft;

   public ClipToSpaceEvent(float f) {
      this.a = f;
   }

   public float TW() {
      return this.a;
   }

   public void TX(float f) {
      this.a = f;
   }
}
