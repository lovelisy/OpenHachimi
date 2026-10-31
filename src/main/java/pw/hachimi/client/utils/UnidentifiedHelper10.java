package pw.hachimi.client.utils;

import pw.hachimi.client.model.combat.CurrentAverageMode;

public class UnidentifiedHelper10 {
   public static final int[] a = new int[CurrentAverageMode.values().length];

   static {
      try {
         a[CurrentAverageMode.AVERAGE.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[CurrentAverageMode.CURRENT.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[CurrentAverageMode.MINIMAL.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[CurrentAverageMode.NONE.ordinal()] = 4;
      } catch (NoSuchFieldError noSuchFieldError) {
      }
   }
}
