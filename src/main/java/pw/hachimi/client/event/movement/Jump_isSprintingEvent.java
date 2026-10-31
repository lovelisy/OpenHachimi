package pw.hachimi.client.event.movement;

import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public final class Jump_isSprintingEvent extends Event {
   private boolean a;

   public Jump_isSprintingEvent(boolean flag) {
      this.a = flag;
   }

   public boolean Ca() {
      return this.a;
   }

   public void Cb(boolean flag) {
      this.a = flag;
   }
}
