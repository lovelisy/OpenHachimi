package pw.hachimi.client.manager;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.module.HitResultWorldHelper;
import pw.hachimi.client.module.player.BlockPosBoxMultitask;
import pw.hachimi.client.utils.player.StackStateUtils;
import pw.hachimi.client.utils.world.BlockUtils;
import pw.hachimi.client.utils.world.PlayerBlockPosHelper;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class BreakManager {
   public static double a_ = 1.0;
   public final ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
   public final ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
   public static boolean a;
   private static final long b = 0L;

   public BreakManager() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      this.concurrentHashMap.clear();
      this.concurrentHashMap2.clear();
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() != EventPhase.PRE) {
         a_ = HitResultWorldHelper.Sq().SH();
         if (Mc.client.player != null && Mc.client.world != null) {
            Iterator iterator2 = this.concurrentHashMap2.keySet().iterator();

            while (iterator2.hasNext()) {
               int n = (Integer)iterator2.next();
               ConcurrentHashMap temp = this.concurrentHashMap2;
               Integer integer = n;
               PlayerBlockPosHelper playerBlockPosHelper = (PlayerBlockPosHelper)temp.get(integer);
               if (playerBlockPosHelper != null && playerBlockPosHelper.MT() != null) {
                  BlockPos pos2 = playerBlockPosHelper.pos;
                  if (!Mc.client.world.isAir(pos2) && !playerBlockPosHelper.stopwatchImpl.afq(playerBlockPosHelper.d + 400.0)) {
                     continue;
                  }
               }

               temp = this.concurrentHashMap2;
               Integer integer2 = n;
               temp.remove(integer2);
            }

            iterator2 = this.concurrentHashMap.values().iterator();

            while (iterator2.hasNext()) {
               PlayerBlockPosHelper playerBlockPosHelper2 = (PlayerBlockPosHelper)iterator2.next();
               playerBlockPosHelper2.d = Math.max(StackStateUtils.ald(playerBlockPosHelper2.pos, false), 50.0);
               if (BlockUtils.TP(playerBlockPosHelper2.pos)) {
                  playerBlockPosHelper2.f = false;
                  playerBlockPosHelper2.e = true;
               } else {
                  BlockPos pos3 = playerBlockPosHelper2.pos;
                  if (Mc.client.world.isAir(pos3)) {
                     playerBlockPosHelper2.f = true;
                     playerBlockPosHelper2.e = false;
                  } else if (!playerBlockPosHelper2.f && playerBlockPosHelper2.stopwatchImpl.afq(playerBlockPosHelper2.d + 400.0)) {
                     playerBlockPosHelper2.e = true;
                  }
               }
            }
         }
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (Mc.client.player != null && Mc.client.world != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof BlockBreakingProgressS2CPacket) {
            BlockBreakingProgressS2CPacket blockBreakingProgressS2CPacket = (BlockBreakingProgressS2CPacket)packet2;
            if (((BlockBreakingProgressS2CPacket)packet2).getPos() == null) {
               return;
            }

            PlayerBlockPosHelper playerBlockPosHelper = new PlayerBlockPosHelper(
               ((BlockBreakingProgressS2CPacket)packet2).getPos(), ((BlockBreakingProgressS2CPacket)packet2).getEntityId(), false
            );
            if (playerBlockPosHelper.MT() == null) {
               return;
            }

            Vec3d temp = playerBlockPosHelper.MT().getEyePos();
            Vec3d vec = blockBreakingProgressS2CPacket.getPos().toCenterPos();
            if (temp.squaredDistanceTo(vec) > 49.0) {
               return;
            }

            if (blockBreakingProgressS2CPacket.getProgress() != 255) {
               if (blockBreakingProgressS2CPacket.getProgress() != 0) {
                  ConcurrentHashMap concurrentHashMap3 = this.concurrentHashMap2;
                  Integer integer = blockBreakingProgressS2CPacket.getEntityId();
                  PlayerBlockPosHelper playerBlockPosHelper2 = (PlayerBlockPosHelper)concurrentHashMap3.get(integer);
                  if (playerBlockPosHelper2 != null) {
                     playerBlockPosHelper2.pos = blockBreakingProgressS2CPacket.getPos();
                  } else if (!BlockUtils.TP(blockBreakingProgressS2CPacket.getPos())) {
                     ConcurrentHashMap concurrentHashMap4 = this.concurrentHashMap2;
                     Integer integer2 = blockBreakingProgressS2CPacket.getEntityId();
                     PlayerBlockPosHelper playerBlockPosHelper3 = new PlayerBlockPosHelper(blockBreakingProgressS2CPacket.getPos(), blockBreakingProgressS2CPacket.getEntityId(), true);
                     Integer integer3 = integer2;
                     concurrentHashMap4.put(integer3, playerBlockPosHelper3);
                  }

                  return;
               }

               ConcurrentHashMap concurrentHashMap5 = this.concurrentHashMap2;
               Integer integer4 = blockBreakingProgressS2CPacket.getEntityId();
               PlayerBlockPosHelper playerBlockPosHelper4 = (PlayerBlockPosHelper)concurrentHashMap5.get(integer4);
               if (playerBlockPosHelper4 != null) {
                  BlockPos pos2 = playerBlockPosHelper4.pos;
                  BlockPos pos3 = blockBreakingProgressS2CPacket.getPos();
                  if (pos2.equals(pos3)) {
                     return;
                  }
               }
            }

            ConcurrentHashMap concurrentHashMap6 = this.concurrentHashMap;
            Integer integer5 = blockBreakingProgressS2CPacket.getEntityId();
            PlayerBlockPosHelper playerBlockPosHelper5 = (PlayerBlockPosHelper)concurrentHashMap6.get(integer5);
            if (playerBlockPosHelper5 != null && !playerBlockPosHelper5.e) {
               BlockPos pos4 = playerBlockPosHelper5.pos;
               BlockPos pos5 = blockBreakingProgressS2CPacket.getPos();
               if (pos4.equals(pos5)) {
                  return;
               }
            }

            ConcurrentHashMap concurrentHashMap7 = this.concurrentHashMap;
            Integer integer6 = blockBreakingProgressS2CPacket.getEntityId();
            concurrentHashMap7.put(integer6, playerBlockPosHelper);
            ConcurrentHashMap concurrentHashMap8 = this.concurrentHashMap2;
            Integer integer7 = blockBreakingProgressS2CPacket.getEntityId();
            if (!concurrentHashMap8.containsKey(integer7) && !BlockUtils.TP(blockBreakingProgressS2CPacket.getPos())) {
               ConcurrentHashMap concurrentHashMap9 = this.concurrentHashMap2;
               Integer temp2 = blockBreakingProgressS2CPacket.getEntityId();
               PlayerBlockPosHelper playerBlockPosHelper6 = new PlayerBlockPosHelper(blockBreakingProgressS2CPacket.getPos(), blockBreakingProgressS2CPacket.getEntityId(), true);
               Integer integer8 = temp2;
               concurrentHashMap9.put(integer8, playerBlockPosHelper6);
            }
         }
      }
   }

   public boolean Rs(BlockPos pos2) {
      Iterator iterator2 = this.concurrentHashMap.values().iterator();

      while (iterator2.hasNext()) {
         PlayerBlockPosHelper playerBlockPosHelper = (PlayerBlockPosHelper)iterator2.next();
         if (playerBlockPosHelper.MT() != null) {
            Vec3d temp = playerBlockPosHelper.MT().getEyePos();
            Vec3d vec = pos2.toCenterPos();
            if (!(temp.distanceTo(vec) > 7.0) && !playerBlockPosHelper.e && playerBlockPosHelper.pos.equals(pos2) && playerBlockPosHelper.stopwatchImpl.afs() / playerBlockPosHelper.d > 0.6F) {
               return true;
            }
         }
      }

      return false;
   }

   public PlayerBlockPosHelper Rt(BlockPos pos2) {
      Iterator iterator2 = this.concurrentHashMap.values().iterator();

      while (iterator2.hasNext()) {
         PlayerBlockPosHelper playerBlockPosHelper = (PlayerBlockPosHelper)iterator2.next();
         if (playerBlockPosHelper.MT() != null) {
            Vec3d temp = playerBlockPosHelper.MT().getEyePos();
            Vec3d vec = pos2.toCenterPos();
            if (!(temp.distanceTo(vec) > 7.0) && !playerBlockPosHelper.e && playerBlockPosHelper.pos.equals(pos2)) {
               return playerBlockPosHelper;
            }
         }
      }

      return null;
   }

   public boolean Ru(BlockPos pos2) {
      boolean flag;
      if (!this.Rv(pos2) && !this.Rx(pos2)) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public boolean Rv(BlockPos pos2) {
      Iterator iterator2 = this.concurrentHashMap.values().iterator();

      while (iterator2.hasNext()) {
         PlayerBlockPosHelper playerBlockPosHelper = (PlayerBlockPosHelper)iterator2.next();
         if (playerBlockPosHelper.MT() != null) {
            Vec3d temp = playerBlockPosHelper.MT().getEyePos();
            Vec3d vec = pos2.toCenterPos();
            if (!(temp.distanceTo(vec) > 7.0) && !playerBlockPosHelper.e && playerBlockPosHelper.pos.equals(pos2)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean Rw(BlockPos pos2, double d2) {
      boolean flag;
      if (this.Rx(pos2) && BlockPosBoxMultitask.alv().blockPosPlayerHelper2.d > d2) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean Rx(BlockPos pos2) {
      BlockPosBoxMultitask blockPosBoxMultitask = BlockPosBoxMultitask.alv();
      BlockPos pos3;
      boolean flag;
      if (blockPosBoxMultitask.awg() && (pos3 = blockPosBoxMultitask.alG()) != null && pos3.equals(pos2)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
