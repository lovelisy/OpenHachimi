package pw.hachimi.client.model.render;

public enum TopMiddleMode {
   TOP,
   MIDDLE,
   BOTTOM,
   TOP_OFFSET,
   BOTTOM_OFFSET;

   private static final TopMiddleMode[] f = Li();

   private static TopMiddleMode[] Li() {
      return new TopMiddleMode[]{TOP, MIDDLE, BOTTOM, TOP_OFFSET, BOTTOM_OFFSET};
   }
}
