package pw.hachimi.client.model.combat;

public enum SurroundAlwaysMiningMode {
   Always,
   Mining,
   Disable;

   private static final SurroundAlwaysMiningMode[] d = abQ();

   private static SurroundAlwaysMiningMode[] abQ() {
      return new SurroundAlwaysMiningMode[]{Always, Mining, Disable};
   }
}
