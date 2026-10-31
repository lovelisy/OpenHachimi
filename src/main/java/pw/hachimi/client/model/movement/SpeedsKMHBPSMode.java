package pw.hachimi.client.model.movement;

public enum SpeedsKMHBPSMode {
   K_M_H,
   B_P_S;

   private static final SpeedsKMHBPSMode[] c = ahZ();

   private static SpeedsKMHBPSMode[] ahZ() {
      return new SpeedsKMHBPSMode[]{K_M_H, B_P_S};
   }
}
