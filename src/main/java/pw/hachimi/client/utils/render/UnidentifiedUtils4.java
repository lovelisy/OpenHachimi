package pw.hachimi.client.utils.render;

import java.util.Random;
import pw.hachimi.client.Mc;

public class UnidentifiedUtils4 {
   private static final String a = "0123456789abcdef";

   public static String TY(int n) {
      StringBuilder stringBuilder = new StringBuilder(n);

      for (int n2 = 0; n2 < n; n2++) {
         Random temp = Mc.random;
         int n3 = "0123456789abcdef".length();
         int n4 = temp.nextInt(n3);
         StringBuilder stringBuilder2 = stringBuilder;
         char c = "0123456789abcdef".charAt(n4);
         stringBuilder2.append(c);
      }

      return stringBuilder.toString();
   }
}
