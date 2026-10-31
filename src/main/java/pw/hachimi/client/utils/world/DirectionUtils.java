package pw.hachimi.client.utils.world;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;

public class DirectionUtils {
   public static String apW(Enum enumValue) {
      return apX(enumValue.name());
   }

   public static String apX(String str) {
      if (!str.contains("_")) {
         char c = str.charAt(0);
         String str2 = String.valueOf(c);
         String str3 = str.split(str2, 2)[1];
         return String.valueOf(c).toUpperCase() + str3.toLowerCase();
      }

      String[] strs = str.split("_");
      StringBuilder stringBuilder = new StringBuilder();
      int n = strs.length;

      for (int n2 = 0; n2 < n; n2++) {
         String str4 = strs[n2];
         char c2 = str4.charAt(0);
         String temp = str4;
         String str5 = String.valueOf(c2);
         String str6 = temp.split(str5, 2)[1];
         StringBuilder stringBuilder2 = stringBuilder;
         String str7 = String.valueOf(c2).toUpperCase();
         StringBuilder stringBuilder3 = stringBuilder2.append(str7);
         String str8 = str6.toLowerCase();
         stringBuilder3.append(str8);
      }

      return stringBuilder.toString();
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static String apY(Direction direction) {
      return switch (DirectionUtilsHelper.a[direction.ordinal()]) {
         case 1 -> "Up";
         case 2 -> "Down";
         case 3 -> "North";
         case 4 -> "South";
         case 5 -> "East";
         case 6 -> "West";
         default -> throw a();
      };
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static String apZ(Axis axis) {
      return switch (DirectionUtilsHelper.b[axis.ordinal()]) {
         case 1 -> "X";
         case 2 -> "Y";
         case 3 -> "Z";
         default -> throw a();
      };
   }

   private static MatchException a() {
      return new MatchException(null, null);
   }
}
