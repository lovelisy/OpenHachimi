package pw.hachimi.client.module;

import pw.hachimi.client.event.CanInsertEvent;
import pw.hachimi.eventbus.annotation.EventListener;

public class Module4 extends ClientModule {
   public static boolean cn;

   public Module4() {
      super("ShulkerInsert", "Allows any item to be inserted into shulker boxes", ModuleCategory.MISC);
   }

   @EventListener
   public void onCanInsert(CanInsertEvent canInsertEvent) {
      canInsertEvent.aim();
   }
}
