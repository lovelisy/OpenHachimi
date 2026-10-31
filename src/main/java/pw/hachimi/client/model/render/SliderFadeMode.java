package pw.hachimi.client.model.render;

public enum SliderFadeMode {
   SLIDER,
   FADE,
   OFF;

   private static final SliderFadeMode[] d = VJ();

   public boolean VG() {
      boolean flag;
      if (this != OFF) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean VH() {
      boolean flag;
      if (this == SLIDER) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean VI() {
      boolean flag;
      if (this == FADE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static SliderFadeMode[] VJ() {
      return new SliderFadeMode[]{SLIDER, FADE, OFF};
   }
}
