package pw.hachimi.client.utils.world;

import net.minecraft.util.math.Direction;

public class BoxRendererDirectionUtils {
   public static final byte a_ = 2;
   public static final byte b_ = 4;
   public static final byte c = 8;
   public static final byte d = 16;
   public static final byte e = 32;
   public static final byte f = 64;
   public static boolean a;
   private static final long b = 0L;

   private BoxRendererDirectionUtils() {
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static byte Xj(Direction direction) {
      return switch (UnidentifiedHelper8.a[direction.ordinal()]) {
         case 1 -> 2;
         case 2 -> 4;
         case 3 -> 8;
         case 4 -> 16;
         case 5 -> 32;
         case 6 -> 64;
         default -> throw a();
      };
   }

   public static boolean Xk(int n, byte b2) {
      boolean flag;
      if ((n & b2) == b2) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static boolean Xl(int n, byte b2) {
      return !Xk(n, b2);
   }

   private static MatchException a() {
      return new MatchException(null, null);
   }
}
