package pw.hachimi.client.manager;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.world.World;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.player.UpdateDeathEvent;
import pw.hachimi.client.model.combat.LastPopTimePops;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class EntityEntityStatusS2CPacketManager {
   private final ConcurrentMap concurrentMap = new ConcurrentHashMap();
   public static boolean a;

   public EntityEntityStatusS2CPacketManager() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (Mc.client.world != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof EntityStatusS2CPacket && ((EntityStatusS2CPacket)packet2).getStatus() == 35) {
            EntityStatusS2CPacket temp = (EntityStatusS2CPacket)packet2;
            ClientWorld world2 = Mc.client.world;
            Entity entity2 = temp.getEntity((World)world2);
            if (entity2 != null && entity2.isAlive()) {
               ConcurrentMap concurrentMap2 = this.concurrentMap;
               UUID uuid = entity2.getUuid();
               if (concurrentMap2.containsKey(uuid)) {
                  ConcurrentMap concurrentMap3 = this.concurrentMap;
                  UUID temp2 = entity2.getUuid();
                  long temp3 = System.currentTimeMillis();
                  ConcurrentMap concurrentMap4 = this.concurrentMap;
                  long l = temp3;
                  UUID uuid2 = temp2;
                  ConcurrentMap concurrentMap5 = concurrentMap3;
                  ConcurrentMap temp4 = concurrentMap4;
                  UUID uuid3 = entity2.getUuid();
                  LastPopTimePops lastPopTimePops = new LastPopTimePops(l, ((LastPopTimePops)temp4.get(uuid3)).getPops() + 1);
                  concurrentMap5.replace(uuid2, lastPopTimePops);
               } else {
                  ConcurrentMap concurrentMap6 = this.concurrentMap;
                  UUID uuid4 = entity2.getUuid();
                  LastPopTimePops lastPopTimePops2 = new LastPopTimePops(System.currentTimeMillis(), 1);
                  UUID uuid5 = uuid4;
                  concurrentMap6.put(uuid5, lastPopTimePops2);
               }
            }
         }
      }
   }

   @EventListener(priority = Integer.MIN_VALUE)
   public void acn(UpdateDeathEvent updateDeathEvent) {
      ConcurrentMap temp = this.concurrentMap;
      UUID uuid = updateDeathEvent.getLiving().getUuid();
      temp.remove(uuid);
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      this.concurrentMap.clear();
   }

   public int acp(Entity entity2) {
      return ((LastPopTimePops)this.concurrentMap.getOrDefault(entity2.getUuid(), new LastPopTimePops(0L, 0))).getPops();
   }

   public long acq(Entity entity2) {
      return ((LastPopTimePops)this.concurrentMap.getOrDefault(entity2.getUuid(), new LastPopTimePops(-1L, 0))).lastPopTime();
   }
}
