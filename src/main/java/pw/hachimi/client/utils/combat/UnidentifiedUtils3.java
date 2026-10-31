package pw.hachimi.client.utils.combat;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class UnidentifiedUtils3 {
   public static boolean a;

   public static double rx(double d, int n) {
      BigDecimal temp = new BigDecimal(d);
      RoundingMode roundingMode = RoundingMode.HALF_UP;
      return temp.setScale(n, roundingMode).doubleValue();
   }
}
