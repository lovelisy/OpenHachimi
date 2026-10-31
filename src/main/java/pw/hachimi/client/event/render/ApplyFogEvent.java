package pw.hachimi.client.event.render;

import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class ApplyFogEvent extends Event {
   private final float a;
   private float b;
   private float c;

   public ApplyFogEvent(float f) {
      this.a = f;
   }

   public float VR() {
      return this.a;
   }

   public float VS() {
      return this.b;
   }

   public void VT(float f) {
      this.b = f;
   }

   public float VU() {
      return this.c;
   }

   public void VV(float f) {
      this.c = f;
   }
}
