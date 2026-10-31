package pw.hachimi.client.event.network;

import pw.hachimi.client.event.UnknownEvent8;

public class SendMovementPacketsTailEvent extends UnknownEvent8 {
   private float a;
   private float b;
   public static boolean gy;

   public float afK() {
      return this.a;
   }

   public void afL(float f) {
      this.a = f;
   }

   public float afM() {
      return this.b;
   }

   public void afN(float f) {
      this.b = f;
   }
}
