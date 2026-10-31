package pw.hachimi.client.setting;

import java.util.function.Supplier;

public class BooleanSetting2 extends BooleanSetting {
   public BooleanSetting2(String str, String str2, Boolean flag, Supplier supplier) {
      super(str, str2, flag, supplier);
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
