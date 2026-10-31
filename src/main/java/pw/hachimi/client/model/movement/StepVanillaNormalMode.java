package pw.hachimi.client.model.movement;

public enum StepVanillaNormalMode {
   VANILLA,
   NORMAL,
   INSTANT;

   private static final StepVanillaNormalMode[] d = Xw();

   private static StepVanillaNormalMode[] Xw() {
      return new StepVanillaNormalMode[]{VANILLA, NORMAL, INSTANT};
   }
}
