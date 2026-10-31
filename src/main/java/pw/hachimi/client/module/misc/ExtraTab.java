package pw.hachimi.client.module.misc;

import net.minecraft.text.Text;
import pw.hachimi.client.event.player.CollectPlayerEntriesEvent;
import pw.hachimi.client.event.player.GetPlayerNameEvent;
import pw.hachimi.client.event.render.RenderEvent2;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ColorHelper;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class ExtraTab extends ClientModule {
   Setting size = this.registerSetting(new NumberSetting("Size", "The number of players to show", 80, 200, 1000));
   Setting columns = this.registerSetting(new NumberSetting("Columns", "The number columns to show.", 1, 20, 100));
   Setting self = this.registerSetting(new BooleanSetting("Self", "Highlights yourself in the tab list.", false));
   Setting friends = this.registerSetting(new BooleanSetting("Friends", "Highlights friends in the tab list.", true));

   public ExtraTab() {
      super("ExtraTab", "Expands the tab list size to allow for more players", ModuleCategory.MISC);
   }

   @EventListener
   public void onGetPlayerName(GetPlayerNameEvent getPlayerNameEvent) {
      String[] strs = getPlayerNameEvent.getText().getString().split(" ");
      if ((Boolean)this.self.atx()) {
         int n = strs.length;

         for (int n2 = 0; n2 < n; n2++) {
            String str = strs[n2];
            String temp = this.gO(str);
            String str2 = client.getGameProfile().getName();
            if (temp.equals(str2)) {
               getPlayerNameEvent.aim();
               getPlayerNameEvent.setText(Text.of("§s" + getPlayerNameEvent.getText().getString()));
               return;
            }
         }
      }

      if ((Boolean)this.friends.atx() && ColorHelper.wx().wz()) {
         int n3 = strs.length;

         for (int n4 = 0; n4 < n3; n4++) {
            String str3 = strs[n4];
            String str4 = this.gO(str3);
            if (Managers.friendManager.awq(str4)) {
               getPlayerNameEvent.aim();
               getPlayerNameEvent.setText(Text.of("§g" + getPlayerNameEvent.getText().getString()));
               break;
            }
         }
      }
   }

   @EventListener
   public void onCollectPlayerEntries(CollectPlayerEntriesEvent collectPlayerEntriesEvent) {
      collectPlayerEntriesEvent.aim();
      collectPlayerEntriesEvent.Av((Integer)this.size.atx());
   }

   @EventListener
   public void onRenderEvent2(RenderEvent2 renderEvent2) {
      renderEvent2.aim();
      renderEvent2.gJ((Integer)this.columns.atx());
   }

   private String gO(String str) {
      StringBuilder stringBuilder = new StringBuilder();
      char[] cs = str.toCharArray();
      int n = cs.length;

      label23:
      for (int n2 = 0; n2 < n; n2++) {
         char c = cs[n2];
         if (c == 167) {
            while (++n2 < n) {
               if (cs[n2] != 167) {
                  continue label23;
               }
            }
            break;
         } else {
            stringBuilder.append(c);
         }
      }

      return stringBuilder.toString();
   }
}
