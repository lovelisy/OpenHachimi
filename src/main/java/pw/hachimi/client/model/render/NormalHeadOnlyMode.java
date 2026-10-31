package pw.hachimi.client.model.render;

public enum NormalHeadOnlyMode {
   Normal,
   HeadOnly,
   BodyFixed,
   None;

   private static final NormalHeadOnlyMode[] e = acQ();

   private static NormalHeadOnlyMode[] acQ() {
      return new NormalHeadOnlyMode[]{Normal, HeadOnly, BodyFixed, None};
   }
}
