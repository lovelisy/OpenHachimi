package pw.hachimi.client.setting;

import com.google.gson.JsonObject;
import java.util.function.Supplier;

public class BooleanSetting5 extends BooleanSetting {
   Runnable runnable;
   public static boolean s;

   public BooleanSetting5(String str, String str2, Runnable runnable2) {
      super(str, str2, true);
      this.runnable = runnable2;
   }

   public BooleanSetting5(String str, String str2, Runnable runnable2, Supplier supplier) {
      super(str, str2, true, supplier);
      this.runnable = runnable2;
   }

   @Override
   public void Kl(Boolean flag) {
      if (!flag) {
         this.runnable.run();
      }
   }

   @Override
   public JsonObject LT(int n, int n2, char c) {
      return null;
   }

   @Override
   public Boolean Km(JsonObject jsonObject) {
      return true;
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
