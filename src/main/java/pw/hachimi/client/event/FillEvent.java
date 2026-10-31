package pw.hachimi.client.event;

import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class FillEvent extends Event {
   private float a;
   public static boolean ga;

   public float x() {
      return this.a;
   }

   public void y(float f) {
      this.a = f;
   }
}
