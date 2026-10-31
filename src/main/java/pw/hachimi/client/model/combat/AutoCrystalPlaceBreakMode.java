package pw.hachimi.client.model.combat;

public enum AutoCrystalPlaceBreakMode {
   PLACE,
   BREAK,
   ROTATE,
   MISC,
   Calc,
   BASE,
   RENDER;

   private static final AutoCrystalPlaceBreakMode[] h = Ap();

   public boolean Ai() {
      boolean flag;
      if (this == PLACE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean Aj() {
      boolean flag;
      if (this == BREAK) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean Ak() {
      boolean flag;
      if (this == ROTATE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean Al() {
      boolean flag;
      if (this == MISC) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean Am() {
      boolean flag;
      if (this == Calc) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean An() {
      boolean flag;
      if (this == BASE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean Ao() {
      boolean flag;
      if (this == RENDER) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static AutoCrystalPlaceBreakMode[] Ap() {
      return new AutoCrystalPlaceBreakMode[]{PLACE, BREAK, ROTATE, MISC, Calc, BASE, RENDER};
   }
}
