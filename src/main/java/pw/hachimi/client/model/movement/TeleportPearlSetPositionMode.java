package pw.hachimi.client.model.movement;

public enum TeleportPearlSetPositionMode {
   Pearl,
   SetPosition,
   Vanilla,
   Path;

   private static final TeleportPearlSetPositionMode[] e = aP();

   private static TeleportPearlSetPositionMode[] aP() {
      return new TeleportPearlSetPositionMode[]{Pearl, SetPosition, Vanilla, Path};
   }
}
