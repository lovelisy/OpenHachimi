package pw.hachimi.client.event;

import pw.hachimi.client.setting.Setting;

public class UnknownEvent3 extends UnknownEvent8 {
   private final Setting setting;

   public UnknownEvent3(Setting setting2) {
      this.setting = setting2;
   }

   public Setting getSetting() {
      return this.setting;
   }
}
