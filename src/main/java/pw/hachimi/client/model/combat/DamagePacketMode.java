package pw.hachimi.client.model.combat;

public enum DamagePacketMode {
   Damage,
   Packet;

   private static final DamagePacketMode[] c = ajL();

   private static DamagePacketMode[] ajL() {
      return new DamagePacketMode[]{Damage, Packet};
   }
}
