package pw.hachimi.client.setting;

import com.google.gson.JsonObject;
import java.util.function.Supplier;

public class BooleanSetting extends Setting {
   public static boolean fe;

   public BooleanSetting(String str, String str2, Boolean flag) {
      super(str, str2, flag);
   }

   public BooleanSetting(String str, String str2, Boolean flag, Supplier supplier) {
      super(str, str2, flag, supplier);
   }

   public void Kl(Boolean flag) {
      super.aty(flag);
   }

   @Override
   public JsonObject LT(int n, int n2, char c) {
      JsonObject jsonObject = super.LT(25681, 44222, '\u218e');
      jsonObject.addProperty("value", (Boolean)this.atx());
      return jsonObject;
   }

   public Boolean Km(JsonObject jsonObject) {
      return !jsonObject.has("value") ? null : jsonObject.get("value").getAsBoolean();
   }

   @Override
   public void aty(Object obj) {
      this.Kl((Boolean)obj);
   }

   @Override
   public Object LU(JsonObject jsonObject) {
      return this.Km(jsonObject);
   }
}
