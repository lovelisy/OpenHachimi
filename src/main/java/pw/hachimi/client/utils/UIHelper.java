package pw.hachimi.client.utils;

import pw.hachimi.client.model.render.OffGradientMode;

public class UIHelper {
   public static final int[] a = new int[OffGradientMode.values().length];

   static {
      a[OffGradientMode.OFF.ordinal()] = 1;
      a[OffGradientMode.STATIC.ordinal()] = 2;
      a[OffGradientMode.HUE.ordinal()] = 3;
      a[OffGradientMode.GRADIENT.ordinal()] = 4;
      a[OffGradientMode.ASTOLFO.ordinal()] = 5;
      a[OffGradientMode.GAY.ordinal()] = 6;
   }
}
