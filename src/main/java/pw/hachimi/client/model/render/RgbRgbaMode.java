package pw.hachimi.client.model.render;

public enum RgbRgbaMode {
   A,
   RGB,
   RGBA;

   private static final RgbRgbaMode[] d = ajz();

   public int ajy() {
      return switch (this) {
         case A -> 6403;
         case RGB -> 6407;
         case RGBA -> 6408;
      };
   }

   private static RgbRgbaMode[] ajz() {
      return new RgbRgbaMode[]{A, RGB, RGBA};
   }

   private static MatchException b() {
      return new MatchException(null, null);
   }
}
