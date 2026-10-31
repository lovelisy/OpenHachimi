package pw.hachimi.client.utils.world;

import pw.hachimi.client.model.player.SwapMode;

public class UnidentifiedHelper25 {
   static final int[] a = new int[SwapMode.values().length];

   static {
      try {
         a[SwapMode.Pick.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[SwapMode.Hotbar.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[SwapMode.Screen.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {
      }
   }
}
