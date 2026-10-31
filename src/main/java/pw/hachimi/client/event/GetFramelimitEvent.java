package pw.hachimi.client.event;

import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class GetFramelimitEvent extends Event {
   private int a;
   public static boolean gk;

   public int arH() {
      return this.a;
   }

   public void arI(int n) {
      this.a = n;
   }
}
