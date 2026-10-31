package pw.hachimi.client.model.render;

public enum UnidentifiedOption {
   Float(1, 4, false),
   Vec2(2, 4, false),
   Vec3(3, 4, false),
   Color(4, 1, true);

   public final int e;
   public final int f;
   public final boolean g;
   private static final UnidentifiedOption[] h = Ex();

   UnidentifiedOption(int n, int n2, boolean flag) {
      this.e = n;
      this.f = n * n2;
      this.g = flag;
   }

   public int Ew() {
      short s;
      if (this == Color) {
         s = 5121;
      } else {
         s = 5126;
      }

      return s;
   }

   private static UnidentifiedOption[] Ex() {
      return new UnidentifiedOption[]{Float, Vec2, Vec3, Color};
   }
}
