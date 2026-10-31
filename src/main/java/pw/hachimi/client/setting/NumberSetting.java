package pw.hachimi.client.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Supplier;
import pw.hachimi.client.model.DefaultDegreesMode;

public class NumberSetting extends Setting {
   private final Number a;
   private final Number b;
   private final DefaultDegreesMode defaultDegreesMode;
   private final int d;
   public static boolean ai;

   public NumberSetting(String str, String str2, Number number, Number number2, Number number3, DefaultDegreesMode defaultDegreesMode2) {
      super(str, str2, number2);
      this.a = number;
      this.b = number3;
      this.defaultDegreesMode = defaultDegreesMode2;
      String str3 = String.valueOf(number2);
      int n = str3.indexOf(".") + 1;
      this.d = str3.substring(n).length();
   }

   public NumberSetting(String str, String str2, Number number, Number number2, Number number3, DefaultDegreesMode defaultDegreesMode2, Supplier supplier) {
      super(str, str2, number2, supplier);
      this.a = number;
      this.b = number3;
      this.defaultDegreesMode = defaultDegreesMode2;
      String str3 = String.valueOf(number2);
      int n = str3.indexOf(".") + 1;
      this.d = str3.substring(n).length();
   }

   public NumberSetting(String str, String str2, Number number, Number number2, Number number3) {
      this(str, str2, number, number2, number3, DefaultDegreesMode.DEFAULT);
   }

   public NumberSetting(String str, String str2, Number number, Number number2, Number number3, Supplier supplier) {
      this(str, str2, number, number2, number3, DefaultDegreesMode.DEFAULT, supplier);
   }

   public Number abk() {
      return this.a;
   }

   public Number abl() {
      return this.b;
   }

   public boolean abm() {
      return this.a.doubleValue() == ((Number)this.atx()).doubleValue();
   }

   public boolean abn() {
      return this.b.doubleValue() == ((Number)this.atx()).doubleValue();
   }

   public int abo() {
      return this.d;
   }

   public DefaultDegreesMode getDefaultDegreesMode() {
      return this.defaultDegreesMode;
   }

   public double abq() {
      Number number = (Number)this.atx();
      return number.doubleValue() * number.doubleValue();
   }

   public void abr(Number number) {
      super.aty(number);
   }

   @Override
   public JsonObject LT(int n, int n2, char c) {
      JsonObject jsonObject = super.LT(25681, 44222, '\u218e');
      if (!(this.atx() instanceof Integer)) {
         if (!(this.atx() instanceof Float)) {
            if (this.atx() instanceof Double) {
               jsonObject.addProperty("value", (Number)this.atx());
            }
         } else {
            jsonObject.addProperty("value", (Number)this.atx());
         }
      } else {
         jsonObject.addProperty("value", (Number)this.atx());
      }

      return jsonObject;
   }

   public Number abs(JsonObject jsonObject) {
      if (jsonObject.has("value")) {
         JsonElement jsonElement = jsonObject.get("value");
         if (this.atx() instanceof Integer) {
            return jsonElement.getAsInt();
         }

         if (this.atx() instanceof Float) {
            return jsonElement.getAsFloat();
         }

         if (this.atx() instanceof Double) {
            return jsonElement.getAsDouble();
         }
      }

      return null;
   }

   @Override
   public void aty(Object obj) {
      this.abr((Number)obj);
   }

   @Override
   public Object LU(JsonObject jsonObject) {
      return this.abs(jsonObject);
   }
}
