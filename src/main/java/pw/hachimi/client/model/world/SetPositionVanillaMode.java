package pw.hachimi.client.model.world;

public enum SetPositionVanillaMode {
   SetPosition,
   Vanilla,
   Path;

   private static final SetPositionVanillaMode[] d = ahS();

   private static SetPositionVanillaMode[] ahS() {
      return new SetPositionVanillaMode[]{SetPosition, Vanilla, Path};
   }
}
