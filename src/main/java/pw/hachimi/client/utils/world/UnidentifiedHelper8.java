package pw.hachimi.client.utils.world;

import net.minecraft.util.math.Direction;

public class UnidentifiedHelper8 {
   static final int[] a = new int[Direction.values().length];

   static {
      try {
         a[Direction.UP.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.DOWN.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.NORTH.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.SOUTH.ordinal()] = 4;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.WEST.ordinal()] = 5;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.EAST.ordinal()] = 6;
      } catch (NoSuchFieldError noSuchFieldError) {
      }
   }
}
