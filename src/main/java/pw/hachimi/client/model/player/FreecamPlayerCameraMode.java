package pw.hachimi.client.model.player;

public enum FreecamPlayerCameraMode {
   PLAYER,
   CAMERA;

   private static final FreecamPlayerCameraMode[] c = Ag();

   private static FreecamPlayerCameraMode[] Ag() {
      return new FreecamPlayerCameraMode[]{PLAYER, CAMERA};
   }
}
