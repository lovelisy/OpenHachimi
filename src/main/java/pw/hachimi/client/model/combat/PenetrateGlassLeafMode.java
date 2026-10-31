package pw.hachimi.client.model.combat;

public enum PenetrateGlassLeafMode {
   Glass,
   Leaf,
   ICE,
   Anchor;

   private static final PenetrateGlassLeafMode[] e = ajX();

   private static PenetrateGlassLeafMode[] ajX() {
      return new PenetrateGlassLeafMode[]{Glass, Leaf, ICE, Anchor};
   }
}
