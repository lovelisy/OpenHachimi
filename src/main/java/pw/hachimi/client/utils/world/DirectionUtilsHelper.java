package pw.hachimi.client.utils.world;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;

public class DirectionUtilsHelper {
   static final int[] a = new int[Direction.values().length];
   static final int[] b = new int[Axis.values().length];

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
         a[Direction.EAST.ordinal()] = 5;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Direction.WEST.ordinal()] = 6;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         b[Axis.X.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         b[Axis.Y.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         b[Axis.Z.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {
      }
   }
}
