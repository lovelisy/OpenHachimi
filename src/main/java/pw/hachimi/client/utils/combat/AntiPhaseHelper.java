package pw.hachimi.client.utils.combat;

import net.minecraft.util.math.Direction;

public class AntiPhaseHelper {
   public static final int[] a = new int[Direction.values().length];

   static {
      try {
         a[Direction.EAST.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.WEST.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.SOUTH.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.NORTH.ordinal()] = 4;
      } catch (NoSuchFieldError noSuchFieldError) {
      }
   }
}
