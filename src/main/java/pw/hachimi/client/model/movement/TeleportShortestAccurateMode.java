package pw.hachimi.client.model.movement;

public enum TeleportShortestAccurateMode {
   Shortest,
   Accurate,
   Longest;

   private static final TeleportShortestAccurateMode[] d = ajV();

   private static TeleportShortestAccurateMode[] ajV() {
      return new TeleportShortestAccurateMode[]{Shortest, Accurate, Longest};
   }
}
