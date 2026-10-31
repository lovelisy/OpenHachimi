package pw.hachimi.client.utils.world;

import net.minecraft.util.math.Direction;

public class UnidentifiedHelper4 {
   static final int[] a = new int[Direction.values().length];

   static {
      try {
         a[Direction.NORTH.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.SOUTH.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.EAST.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.WEST.ordinal()] = 4;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.UP.ordinal()] = 5;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.DOWN.ordinal()] = 6;
      } catch (NoSuchFieldError noSuchFieldError) {
      }
   }
}
