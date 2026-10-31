package pw.hachimi.client.utils.render;

import pw.hachimi.client.model.render.LeftMiddleMode;
import pw.hachimi.client.model.render.TopMiddleMode;

public class RenderModuleHelper {
   public static final int[] a = new int[LeftMiddleMode.values().length];
   public static final int[] b = new int[TopMiddleMode.values().length];

   static {
      try {
         a[LeftMiddleMode.LEFT.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[LeftMiddleMode.RIGHT.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[LeftMiddleMode.LEFT_OFFSET.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[LeftMiddleMode.RIGHT_OFFSET.ordinal()] = 4;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[LeftMiddleMode.MIDDLE.ordinal()] = 5;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         b[TopMiddleMode.TOP.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         b[TopMiddleMode.BOTTOM.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         b[TopMiddleMode.TOP_OFFSET.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         b[TopMiddleMode.BOTTOM_OFFSET.ordinal()] = 4;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         b[TopMiddleMode.MIDDLE.ordinal()] = 5;
      } catch (NoSuchFieldError noSuchFieldError) {
      }
   }
}
