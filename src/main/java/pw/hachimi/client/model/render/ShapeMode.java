package pw.hachimi.client.model.render;

public enum ShapeMode {
   Lines,
   Sides,
   Both,
   None;

   private static final ShapeMode[] e = KM();

   public boolean KK() {
      return this == Lines || this == Both;
   }

   public boolean KL() {
      return this == Sides || this == Both;
   }

   private static ShapeMode[] KM() {
      return new ShapeMode[]{Lines, Sides, Both, None};
   }
}
