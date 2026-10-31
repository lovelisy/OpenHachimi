package pw.hachimi.client.model.render;

public enum LeftMiddleMode {
   LEFT,
   MIDDLE,
   RIGHT,
   LEFT_OFFSET,
   RIGHT_OFFSET;

   private static final LeftMiddleMode[] f = Yq();

   private static LeftMiddleMode[] Yq() {
      return new LeftMiddleMode[]{LEFT, MIDDLE, RIGHT, LEFT_OFFSET, RIGHT_OFFSET};
   }
}
