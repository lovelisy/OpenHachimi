package pw.hachimi.client.model.movement;

public enum SprintLegitRageMode {
   LEGIT,
   RAGE,
   RAGE_STRICT,
   GRIM,
   OFF;

   private static final SprintLegitRageMode[] f = aoz();

   private static SprintLegitRageMode[] aoz() {
      return new SprintLegitRageMode[]{LEGIT, RAGE, RAGE_STRICT, GRIM, OFF};
   }
}
