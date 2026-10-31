package pw.hachimi.client.utils.render;

import pw.hachimi.client.model.render.DisableNormalMode;

public class RenderUtilsHelper {
   public static final int[] a = new int[DisableNormalMode.values().length];

   static {
      try {
         a[DisableNormalMode.OUTLINE.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[DisableNormalMode.NORMAL.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[DisableNormalMode.DISABLE.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {
      }
   }
}
