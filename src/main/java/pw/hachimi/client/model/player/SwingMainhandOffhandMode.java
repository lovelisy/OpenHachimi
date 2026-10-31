package pw.hachimi.client.model.player;

public enum SwingMainhandOffhandMode {
   MAINHAND,
   OFFHAND,
   SWAP,
   PACKET;

   private static final SwingMainhandOffhandMode[] e = atG();

   private static SwingMainhandOffhandMode[] atG() {
      return new SwingMainhandOffhandMode[]{MAINHAND, OFFHAND, SWAP, PACKET};
   }
}
