package pw.hachimi.client.model.render;

public enum LinesTrianglesMode {
   Lines(2),
   Triangles(3);

   public final int c;
   private static final LinesTrianglesMode[] d = eZ();

   LinesTrianglesMode(int n) {
      this.c = n;
   }

   public int eY() {
      return this == Lines ? 1 : 4;
   }

   private static LinesTrianglesMode[] eZ() {
      return new LinesTrianglesMode[]{Lines, Triangles};
   }
}
