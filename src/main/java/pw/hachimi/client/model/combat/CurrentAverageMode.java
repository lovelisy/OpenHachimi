package pw.hachimi.client.model.combat;

public enum CurrentAverageMode {
   CURRENT,
   AVERAGE,
   MINIMAL,
   NONE;

   private static final CurrentAverageMode[] e = ahX();

   private static CurrentAverageMode[] ahX() {
      return new CurrentAverageMode[]{CURRENT, AVERAGE, MINIMAL, NONE};
   }
}
