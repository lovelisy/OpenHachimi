package pw.hachimi.client.manager;

import java.util.Arrays;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.model.world.PositionTimeMS;
import pw.hachimi.client.module.HitResultWorldHelper;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public final class AntiCheatManager {
   public final StopwatchImpl stopwatchImpl = new StopwatchImpl();
   private final int[] b = new int[4];
   private PositionTimeMS positionTimeMS;
   private int d;
   private boolean e;
   private static final String c = "Server is running GrimAC.";

   public AntiCheatManager() {
      EventBusImpl.INSTANCE.rD(this);
      Arrays.fill(this.b, -1);
   }

   public boolean Uc() {
      return this.stopwatchImpl.afq(HitResultWorldHelper.Sq().Sz() * 50.0F);
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      Packet packet2 = channelRead0Event.getPacket();
      if (packet2 instanceof CommonPingS2CPacket) {
         if (this.d > 3) {
            return;
         }

         int n = ((CommonPingS2CPacket)packet2).getParameter();
         this.b[this.d] = n;
         this.d++;
         if (this.d == 4) {
            this.Uf();
         }
      } else {
         packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof PlayerPositionLookS2CPacket) {
            Vec3d vec = new Vec3d(
               ((PlayerPositionLookS2CPacket)packet2).getX(), ((PlayerPositionLookS2CPacket)packet2).getY(), ((PlayerPositionLookS2CPacket)packet2).getZ()
            );
            this.positionTimeMS = new PositionTimeMS(vec, System.currentTimeMillis(), ((PlayerPositionLookS2CPacket)packet2).getTeleportId());
         }
      }
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      Arrays.fill(this.b, -1);
      this.d = 0;
      this.e = false;
   }

   private void Uf() {
      int n = 0;

      while (this.b[n] == -n) {
         if (++n >= 4) {
            break;
         }
      }

      this.e = true;
      Bootstrap.NZ("Server is running GrimAC.");
   }

   public boolean Ug() {
      return this.e;
   }

   public boolean Uh(long l) {
      boolean flag;
      if (this.positionTimeMS != null && this.positionTimeMS.bf() >= l) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
