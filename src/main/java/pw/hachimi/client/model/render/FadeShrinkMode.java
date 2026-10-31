package pw.hachimi.client.model.render;

public enum FadeShrinkMode {
   Fade,
   Shrink,
   Both;

   private static final FadeShrinkMode[] d = ZX();

   private static FadeShrinkMode[] ZX() {
      return new FadeShrinkMode[]{Fade, Shrink, Both};
   }
}
