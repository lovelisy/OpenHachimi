package pw.hachimi.client.model.movement;

public enum PhaseNormalSandMode {
   NORMAL,
   SAND,
   CLIMB,
   PEARL,
   CLIP;

   private static final PhaseNormalSandMode[] f = WF();

   private static PhaseNormalSandMode[] WF() {
      return new PhaseNormalSandMode[]{NORMAL, SAND, CLIMB, PEARL, CLIP};
   }
}
