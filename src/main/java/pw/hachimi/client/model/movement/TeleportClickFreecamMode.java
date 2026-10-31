package pw.hachimi.client.model.movement;

public enum TeleportClickFreecamMode {
   Click,
   Freecam;

   private static final TeleportClickFreecamMode[] c = PC();

   private static TeleportClickFreecamMode[] PC() {
      return new TeleportClickFreecamMode[]{Click, Freecam};
   }
}
