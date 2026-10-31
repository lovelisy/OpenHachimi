package pw.hachimi.client.event.player;

import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class CollectPlayerEntriesEvent extends Event {
   private int a;
   public static boolean eh;

   public int Au() {
      return this.a;
   }

   public void Av(int n) {
      this.a = n;
   }
}
