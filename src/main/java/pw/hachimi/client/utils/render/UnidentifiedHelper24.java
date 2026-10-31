package pw.hachimi.client.utils.render;

import pw.hachimi.client.model.render.NormalHeadOnlyMode;

public class UnidentifiedHelper24 {
   static final int[] a = new int[NormalHeadOnlyMode.values().length];

   static {
      try {
         a[NormalHeadOnlyMode.Normal.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[NormalHeadOnlyMode.BodyFixed.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[NormalHeadOnlyMode.HeadOnly.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {
      }
   }
}
