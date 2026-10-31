package pw.hachimi.client.model.render;

public enum ShadersDefaultGradientMode {
   DEFAULT {
      @Override
      public int ZZ() {
         return 0;
      }
   },
   GRADIENT {
      public static boolean af;

      @Override
      public int ZZ() {
         return 1;
      }
   },
   MARBLE {
      public static boolean bc;

      @Override
      public int ZZ() {
         return 2;
      }
   },
   RAINBOW {
      @Override
      public int ZZ() {
         return 3;
      }
   };

   private static final ShadersDefaultGradientMode[] e = aaa();

   public abstract int ZZ();

   private static ShadersDefaultGradientMode[] aaa() {
      return new ShadersDefaultGradientMode[]{DEFAULT, GRADIENT, MARBLE, RAINBOW};
   }
}
