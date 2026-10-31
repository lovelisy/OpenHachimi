package pw.hachimi.client.module.misc;

import pw.hachimi.client.event.network.VisitFormattedEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.setting.StringSetting;
import pw.hachimi.eventbus.annotation.EventListener;

public class NameProtect extends ClientModule {
   Setting placeholder = this.registerSetting(createStringSetting("Placeholder", "The placeholder name for the player"));

   public NameProtect() {
      super("NameProtect", "Hides the player name in chat and tablist", ModuleCategory.MISC);
   }

   @EventListener
   public void onVisitFormatted(VisitFormattedEvent visitFormattedEvent) {
      if (client.player != null) {
         String str = client.getSession().getUsername();
         String str2 = visitFormattedEvent.getText();
         if (str2.contains(str)) {
            visitFormattedEvent.aim();
            String temp = str2;
            CharSequence charSequence = (CharSequence)this.placeholder.atx();
            visitFormattedEvent.setText(temp.replace(str, charSequence));
         }
      }
   }

   private static StringSetting createStringSetting(String str, String str2) {
      return new StringSetting(str, str2, "Player");
   }
}
