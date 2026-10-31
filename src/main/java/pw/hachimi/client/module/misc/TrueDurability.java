package pw.hachimi.client.module.misc;

import pw.hachimi.client.event.player.InitItemEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.eventbus.annotation.EventListener;

public class TrueDurability extends ClientModule {
   public static boolean ba;

   public TrueDurability() {
      super("TrueDurability", "Displays the true durability of unbreakable items", ModuleCategory.MISC);
   }

   @EventListener
   public void onInitItem(InitItemEvent initItemEvent) {
      int n = initItemEvent.abL();
      if (initItemEvent.abM() < 0) {
         n = initItemEvent.abM();
      }

      initItemEvent.aim();
      initItemEvent.abN(n);
   }
}
