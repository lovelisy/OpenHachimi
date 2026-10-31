package pw.hachimi.client.model.combat;

public enum CriticalsPacketPacketStrictMode {
   PACKET,
   PACKET_STRICT,
   RIGID,
   VANILLA,
   GRIM,
   GRIM_V3,
   LOW_HOP;

   private static final CriticalsPacketPacketStrictMode[] h = Uj();

   private static CriticalsPacketPacketStrictMode[] Uj() {
      return new CriticalsPacketPacketStrictMode[]{PACKET, PACKET_STRICT, RIGID, VANILLA, GRIM, GRIM_V3, LOW_HOP};
   }
}
