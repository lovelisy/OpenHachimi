package pw.hachimi.client.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Supplier;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.utils.UnidentifiedHelper17;

public class KeybindSetting extends Setting {
   public KeybindSetting(String str, String str2, UnidentifiedHelper17 unidentifiedHelper17) {
      super(str, str2, unidentifiedHelper17);
   }

   public KeybindSetting(String str, String str2, UnidentifiedHelper17 unidentifiedHelper17, Supplier supplier) {
      super(str, str2, unidentifiedHelper17, supplier);
   }

   public void aT(int n) {
      ((UnidentifiedHelper17)this.h).agu(n);
      Managers.playerScreenManager.akp((UnidentifiedHelper17)this.atx(), n);
   }

   public UnidentifiedHelper17 aU(JsonObject jsonObject) {
      boolean flag = false;
      if (jsonObject.has("runOnRelease")) {
         flag = jsonObject.getAsJsonPrimitive("runOnRelease").getAsBoolean();
      }

      if (!jsonObject.has("value")) {
         return null;
      }

      JsonElement jsonElement = jsonObject.get("value");
      return new UnidentifiedHelper17(this.aV(), jsonElement.getAsInt(), this.aW(), flag);
   }

   @Override
   public JsonObject LT(int n, int n2, char c) {
      JsonObject jsonObject = super.LT(25681, 44222, '\u218e');
      jsonObject.addProperty("value", this.aX());
      jsonObject.addProperty("runOnRelease", ((UnidentifiedHelper17)this.h).d);
      return jsonObject;
   }

   public String aV() {
      return ((UnidentifiedHelper17)this.h).getText();
   }

   public Runnable aW() {
      return ((UnidentifiedHelper17)this.h).getRunnable();
   }

   public int aX() {
      return ((UnidentifiedHelper17)this.h).agt();
   }

   public String aY() {
      return ((UnidentifiedHelper17)this.h).agv();
   }

   @Override
   public Object LU(JsonObject jsonObject) {
      return this.aU(jsonObject);
   }
}
