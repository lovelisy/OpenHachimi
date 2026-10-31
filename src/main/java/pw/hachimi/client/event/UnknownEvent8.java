package pw.hachimi.client.event;

public class UnknownEvent8 extends Event {
   private EventPhase eventPhase;

   public EventPhase getEventPhase() {
      return this.eventPhase;
   }

   public UnknownEvent8 aqo(EventPhase eventPhase2) {
      this.eventPhase = eventPhase2;
      return this;
   }
}
