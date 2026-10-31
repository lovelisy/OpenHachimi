package pw.hachimi.client.manager;

import pw.hachimi.client.event.OnInitFinishedEvent;
import pw.hachimi.client.utils.render.FontLoader;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class UnidentifiedHelper3 {
   public UnidentifiedHelper3() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   public void onOnInitFinished(OnInitFinishedEvent onInitFinishedEvent) {
      FontLoader.aor();
   }
}
