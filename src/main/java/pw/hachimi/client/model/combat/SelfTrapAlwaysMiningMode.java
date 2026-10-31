package pw.hachimi.client.model.combat;

public enum SelfTrapAlwaysMiningMode {
   Always,
   Mining,
   Disable;

   private static final SelfTrapAlwaysMiningMode[] d = agg();

   private static SelfTrapAlwaysMiningMode[] agg() {
      return new SelfTrapAlwaysMiningMode[]{Always, Mining, Disable};
   }
}
