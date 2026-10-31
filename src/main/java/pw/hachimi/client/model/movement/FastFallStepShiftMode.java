package pw.hachimi.client.model.movement;

public enum FastFallStepShiftMode {
   STEP,
   SHIFT;

   private static final FastFallStepShiftMode[] c = hq();

   private static FastFallStepShiftMode[] hq() {
      return new FastFallStepShiftMode[]{STEP, SHIFT};
   }
}
