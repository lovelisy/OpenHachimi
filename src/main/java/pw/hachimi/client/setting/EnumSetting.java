package pw.hachimi.client.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Supplier;

public class EnumSetting extends Setting {
   private final Enum[] enumValues;
   private int b;
   public static boolean fv;

   public EnumSetting(String str, String str2, Enum enumValue, Enum[] enums) {
      super(str, str2, enumValue);
      this.enumValues = enums;
   }

   public EnumSetting(String str, String str2, Enum enumValue, Enum[] enums, Supplier supplier) {
      super(str, str2, enumValue, supplier);
      this.enumValues = enums;
   }

   public String ms() {
      return ((Enum)this.atx()).name();
   }

   public Enum[] getEnumValues() {
      return this.enumValues;
   }

   public Enum mu() {
      int n = this.enumValues.length - 1;
      int n2 = this.b + 1;
      if (n2 > n) {
         n2 = 0;
      }

      this.b = n2;
      return this.enumValues[this.b];
   }

   public Enum mv() {
      int n = this.b - 1;
      if (n < 0) {
         n = this.enumValues.length - 1;
      }

      this.b = n;
      return this.enumValues[this.b];
   }

   @Override
   public JsonObject LT(int n, int n2, char c) {
      JsonObject jsonObject = super.LT(25681, 44222, '\u218e');
      jsonObject.addProperty("value", this.ms());
      return jsonObject;
   }

   public Enum mw(JsonObject jsonObject) {
      if (!jsonObject.has("value")) {
         return (Enum)this.atx();
      }

      JsonElement jsonElement = jsonObject.get("value");
      EnumSetting temp = this;

      try {
         Class classValue = ((Enum)temp.atx()).getDeclaringClass();
         String str = jsonElement.getAsString();
         return Enum.valueOf(classValue, str);
      } catch (IllegalArgumentException illegalArgumentException) {
         return null;
      }
   }

   @Override
   public Object LU(JsonObject jsonObject) {
      return this.mw(jsonObject);
   }
}
