package pw.hachimi.client.event;

import java.awt.Color;

public class UnknownEvent6 extends Event {
   private int a;

   public int MQ() {
      return this.a;
   }

   public void MR(Color color) {
      this.a = color.getRGB();
   }

   public void MS(int n) {
      this.a = n;
   }
}
