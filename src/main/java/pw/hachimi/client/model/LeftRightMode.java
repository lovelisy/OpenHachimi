package pw.hachimi.client.model;

public enum LeftRightMode {
   Left,
   Right,
   Auto;

   private static final LeftRightMode[] d = WJ();

   private static LeftRightMode[] WJ() {
      return new LeftRightMode[]{Left, Right, Auto};
   }
}
