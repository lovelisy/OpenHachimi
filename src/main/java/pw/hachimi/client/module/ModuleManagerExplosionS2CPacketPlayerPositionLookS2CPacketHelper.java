package pw.hachimi.client.module;

import java.time.Instant;
import java.util.BitSet;
import net.minecraft.network.encryption.NetworkEncryptionUtils.SecureRandomUtil;
import net.minecraft.network.message.LastSeenMessageList.Acknowledgment;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket.Status;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.event.DisconnectEvent2;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.ServerManager;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public final class ModuleManagerExplosionS2CPacketPlayerPositionLookS2CPacketHelper extends Module3 {
   public ModuleManagerExplosionS2CPacketPlayerPositionLookS2CPacketHelper() {
      super("Server", "Prevents servers actions on player", ModuleCategory.MISC);
   }

   Setting booleanSetting1 = this.registerSetting(new BooleanSetting("NoDemo", "Prevents servers from forcing you to a demo screen", true));
   Setting booleanSetting2 = this.registerSetting(new BooleanSetting("NoResourcePack", "Prevents server from forcing resource pack", false));
   Setting booleanSetting3 = this.registerSetting(new BooleanSetting("NoServerCrash", "Prevents server packets from crashing the client", false));
   Setting booleanSetting4 = this.registerSetting(new BooleanSetting("IllegalDisconnect", "Disconnects by getting kicked from server", false));
   public static boolean de;

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      Packet packet2 = channelRead0Event.getPacket();
      if (packet2 instanceof GameStateChangeS2CPacket
         && ((GameStateChangeS2CPacket)packet2).getReason() == GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN
         && !client.isDemo()
         && (Boolean)this.booleanSetting1.atx()) {
         Bootstrap.NZ("Server attempted to use Demo mode features on you!");
         channelRead0Event.aim();
      }

      if (channelRead0Event.getPacket() instanceof ResourcePackSendS2CPacket && (Boolean)this.booleanSetting2.atx()) {
         channelRead0Event.aim();
         Managers.serverManager.iO('\uf560', new ResourcePackStatusC2SPacket(client.player.getUuid(), Status.DECLINED));
      }

      if (client.world != null) {
         if ((Boolean)this.booleanSetting3.atx()) {
            Packet packet3 = channelRead0Event.getPacket();
            if (!(packet3 instanceof PlayerPositionLookS2CPacket)
               || !(((PlayerPositionLookS2CPacket)packet3).getX() > 3.0E7)
                  && !(((PlayerPositionLookS2CPacket)packet3).getY() > client.world.getTopY())
                  && !(((PlayerPositionLookS2CPacket)packet3).getZ() > 3.0E7)
                  && !(((PlayerPositionLookS2CPacket)packet3).getX() < -3.0E7)
                  && !(((PlayerPositionLookS2CPacket)packet3).getY() < client.world.getBottomY())
                  && !(((PlayerPositionLookS2CPacket)packet3).getZ() < -3.0E7)) {
               packet3 = channelRead0Event.getPacket();
               if (!(packet3 instanceof ExplosionS2CPacket)
                  || !(((ExplosionS2CPacket)packet3).getX() > 3.0E7)
                     && !(((ExplosionS2CPacket)packet3).getY() > client.world.getTopY())
                     && !(((ExplosionS2CPacket)packet3).getZ() > 3.0E7)
                     && !(((ExplosionS2CPacket)packet3).getX() < -3.0E7)
                     && !(((ExplosionS2CPacket)packet3).getY() < client.world.getBottomY())
                     && !(((ExplosionS2CPacket)packet3).getZ() < -3.0E7)
                     && !(((ExplosionS2CPacket)packet3).getRadius() > 1000.0F)
                     && ((ExplosionS2CPacket)packet3).getAffectedBlocks().size() <= 1000
                     && !(((ExplosionS2CPacket)packet3).getPlayerVelocityX() > 1000.0F)
                     && !(((ExplosionS2CPacket)packet3).getPlayerVelocityY() > 1000.0F)
                     && !(((ExplosionS2CPacket)packet3).getPlayerVelocityZ() > 1000.0F)
                     && !(((ExplosionS2CPacket)packet3).getPlayerVelocityX() < -1000.0F)
                     && !(((ExplosionS2CPacket)packet3).getPlayerVelocityY() < -1000.0F)
                     && !(((ExplosionS2CPacket)packet3).getPlayerVelocityZ() < -1000.0F)) {
                  packet3 = channelRead0Event.getPacket();
                  if (!(packet3 instanceof EntityVelocityUpdateS2CPacket)
                     || !(((EntityVelocityUpdateS2CPacket)packet3).getVelocityX() > 1000.0)
                        && !(((EntityVelocityUpdateS2CPacket)packet3).getVelocityY() > 1000.0)
                        && !(((EntityVelocityUpdateS2CPacket)packet3).getVelocityZ() > 1000.0)
                        && !(((EntityVelocityUpdateS2CPacket)packet3).getVelocityX() < -1000.0)
                        && !(((EntityVelocityUpdateS2CPacket)packet3).getVelocityY() < -1000.0)
                        && !(((EntityVelocityUpdateS2CPacket)packet3).getVelocityZ() < -1000.0)) {
                     packet3 = channelRead0Event.getPacket();
                     if (packet3 instanceof ParticleS2CPacket && ((ParticleS2CPacket)packet3).getCount() > 500) {
                        channelRead0Event.aim();
                     }
                  } else {
                     channelRead0Event.aim();
                  }
               } else {
                  channelRead0Event.aim();
               }
            } else {
               channelRead0Event.aim();
            }
         }
      }
   }

   @EventListener
   public void onDisconnectEvent2(DisconnectEvent2 disconnectEvent2) {
      if ((Boolean)this.booleanSetting4.atx()) {
         ServerManager temp = Managers.serverManager;
         Instant instant = Instant.now();
         ServerManager serverManager2 = temp;
         ChatMessageC2SPacket chatMessageC2SPacket = b(instant, SecureRandomUtil.nextLong(), b(g()));
         serverManager2.iO('\uf560', chatMessageC2SPacket);
      }
   }

   private static BitSet g() {
      return new BitSet(2);
   }

   private static ChatMessageC2SPacket b(Instant instant, long l, Acknowledgment acknowledgment) {
      return new ChatMessageC2SPacket("§", instant, l, null, acknowledgment);
   }

   private static Acknowledgment b(BitSet bitSet) {
      return new Acknowledgment(1, bitSet);
   }
}
