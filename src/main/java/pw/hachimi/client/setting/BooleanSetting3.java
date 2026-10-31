package pw.hachimi.client.setting;

public class BooleanSetting3 extends BooleanSetting4 {
   public BooleanSetting3(String str, String str2, Boolean flag) {
      super(str, str2, flag);
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
