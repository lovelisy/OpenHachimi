package pw.hachimi.client.model.network;

public enum DisablerDisabledGrimMode {
   Disabled,
   Grim,
   GrimV3;

   private static final DisablerDisabledGrimMode[] d = vd();

   private static DisablerDisabledGrimMode[] vd() {
      return new DisablerDisabledGrimMode[]{Disabled, Grim, GrimV3};
   }
}
