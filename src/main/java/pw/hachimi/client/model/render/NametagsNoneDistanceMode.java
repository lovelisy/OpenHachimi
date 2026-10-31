package pw.hachimi.client.model.render;

public enum NametagsNoneDistanceMode {
   None(false, false),
   Distance(false, true),
   Count(true, false),
   Both(true, true);

   private final boolean e;
   private final boolean f;
   private static final NametagsNoneDistanceMode[] g = ahM();

   NametagsNoneDistanceMode(boolean flag, boolean flag2) {
      this.e = flag;
      this.f = flag2;
   }

   public boolean ahK() {
      return this.e;
   }

   public boolean ahL() {
      return this.f;
   }

   private static NametagsNoneDistanceMode[] ahM() {
      return new NametagsNoneDistanceMode[]{None, Distance, Count, Both};
   }
}
