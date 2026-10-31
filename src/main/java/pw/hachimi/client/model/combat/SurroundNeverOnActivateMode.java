package pw.hachimi.client.model.combat;

public enum SurroundNeverOnActivateMode {
   Never,
   OnActivate,
   Always;

   private static final SurroundNeverOnActivateMode[] d = arU();

   private static SurroundNeverOnActivateMode[] arU() {
      return new SurroundNeverOnActivateMode[]{Never, OnActivate, Always};
   }
}
