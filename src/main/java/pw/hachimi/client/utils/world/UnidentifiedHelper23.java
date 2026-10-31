package pw.hachimi.client.utils.world;

import net.minecraft.world.Difficulty;

public class UnidentifiedHelper23 {
   static final int[] a = new int[Difficulty.values().length];

   static {
      try {
         a[Difficulty.EASY.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[Difficulty.HARD.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {
      }
   }
}
