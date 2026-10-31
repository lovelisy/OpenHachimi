package pw.hachimi.client.event;

import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class UpdateEvent3 extends Event {
   private int a;

   public UpdateEvent3(int n) {
      this.a = n;
   }

   public int aiX() {
      return this.a;
   }

   public void aiY() {
      this.a = -1;
   }
}
