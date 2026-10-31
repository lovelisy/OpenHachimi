package pw.hachimi.client.module.misc;

import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;

public class AutoReconnect extends ClientModule {
   Setting delay = this.registerSetting(new NumberSetting("Delay", "The delay between reconnects to a server", 0, 5, 100));

   public AutoReconnect() {
      super("AutoReconnect", "Automatically reconnects to a server immediately after disconnecting", ModuleCategory.MISC);
   }

   public int agA() {
      return (Integer)this.delay.atx();
   }
}
