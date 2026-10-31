package pw.hachimi.client.model.movement;

public enum LongJumpNormalGlideMode {
   NORMAL,
   GLIDE;

   private static final LongJumpNormalGlideMode[] c = aeZ();

   private static LongJumpNormalGlideMode[] aeZ() {
      return new LongJumpNormalGlideMode[]{NORMAL, GLIDE};
   }
}
