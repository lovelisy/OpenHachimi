package pw.hachimi.client.setting;

import pw.hachimi.client.utils.UnidentifiedHelper17;

public class ValueSetting extends KeybindSetting {
   public ValueSetting(String str, String str2, UnidentifiedHelper17 unidentifiedHelper17) {
      super(str, str2, unidentifiedHelper17);
   }

   @Override
   public String atu() {
      return this.getText();
   }

   @Override
   public String atw() {
      return "";
   }
}
