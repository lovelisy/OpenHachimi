package pw.hachimi.client.model;

public enum CheatPlaceMode {
   CHEAT,
   PLACE,
   ATTACK,
   ROTATE;

   private static final CheatPlaceMode[] e = xp();

   public boolean xl() {
      boolean flag;
      if (this == CHEAT) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean xm() {
      boolean flag;
      if (this == PLACE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean xn() {
      boolean flag;
      if (this == ATTACK) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean xo() {
      boolean flag;
      if (this == ROTATE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static CheatPlaceMode[] xp() {
      return new CheatPlaceMode[]{CHEAT, PLACE, ATTACK, ROTATE};
   }
}
