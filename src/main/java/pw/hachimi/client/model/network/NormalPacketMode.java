package pw.hachimi.client.model.network;

public enum NormalPacketMode {
   NORMAL,
   PACKET,
   SOUND_PACKET;

   private static final NormalPacketMode[] d = yu();

   private static NormalPacketMode[] yu() {
      return new NormalPacketMode[]{NORMAL, PACKET, SOUND_PACKET};
   }
}
