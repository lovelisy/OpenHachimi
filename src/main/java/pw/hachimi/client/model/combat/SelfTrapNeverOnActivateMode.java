package pw.hachimi.client.model.combat;

public enum SelfTrapNeverOnActivateMode {
   Never,
   OnActivate,
   Always;

   private static final SelfTrapNeverOnActivateMode[] d = ajZ();

   private static SelfTrapNeverOnActivateMode[] ajZ() {
      return new SelfTrapNeverOnActivateMode[]{Never, OnActivate, Always};
   }
}
