package pw.hachimi.client.event;

import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class MouseButtonEvent extends Event {
   private final int a;
   private final int b;
   public static boolean cg;

   public MouseButtonEvent(int n, int n2) {
      this.a = n;
      this.b = n2;
   }

   public int WC() {
      return this.a;
   }

   public int WD() {
      return this.b;
   }
}
