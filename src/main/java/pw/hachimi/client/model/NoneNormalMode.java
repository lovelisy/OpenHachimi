package pw.hachimi.client.model;

public enum NoneNormalMode {
   None,
   Normal,
   SetAir;

   private static final NoneNormalMode[] d = cW();

   public boolean cU() {
      boolean flag;
      if (this == Normal) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean cV() {
      boolean flag;
      if (this == SetAir) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static NoneNormalMode[] cW() {
      return new NoneNormalMode[]{None, Normal, SetAir};
   }
}
