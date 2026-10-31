package pw.hachimi.client.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Supplier;

public class StringSetting extends Setting {
   public static boolean bc;

   public StringSetting(String str, String str2, String str3) {
      super(str, str2, str3);
   }

   public StringSetting(String str, String str2, String str3, Supplier supplier) {
      super(str, str2, str3, supplier);
   }

   @Override
   public JsonObject LT(int n, int n2, char c) {
      JsonObject jsonObject = super.LT(25681, 44222, '↎');
      jsonObject.addProperty("value", (String)this.atx());
      return jsonObject;
   }

   public String nC(JsonObject jsonObject) {
      if (!jsonObject.has("value")) {
         return null;
      }

      JsonElement jsonElement = jsonObject.get("value");
      return jsonElement.getAsString();
   }

   @Override
   public Object LU(JsonObject jsonObject) {
      return this.nC(jsonObject);
   }
}
