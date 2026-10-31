package pw.hachimi.client.model.render;

public enum TargetESPGhostTapeMode {
   Ghost,
   Tape,
   Jello,
   PVZ;

   private static final TargetESPGhostTapeMode[] e = hJ();

   private static TargetESPGhostTapeMode[] hJ() {
      return new TargetESPGhostTapeMode[]{Ghost, Tape, Jello, PVZ};
   }
}
