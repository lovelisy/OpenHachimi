package pw.hachimi.client.manager;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.DataTracker.SerializedEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;

public class EntityEntityTrackerUpdateS2CPacketHelper {
   private final List list = new CopyOnWriteArrayList();
   public static boolean a;

   public EntityEntityTrackerUpdateS2CPacketHelper() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   // restored with CFR (compiles, call/field/constant fingerprint equals the deobf jar)
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
       List list;
       int n;
       if (Mc.client.player == null || Mc.client.world == null) {
           return;
       }
       Packet packet = channelRead0Event.getPacket();
       if (!(packet instanceof EntityTrackerUpdateS2CPacket)) return;
       try {
           n = ((EntityTrackerUpdateS2CPacket)packet).id();
       }
       catch (Throwable throwable) {
           throw new MatchException(throwable.toString(), throwable);
       }
       final List list2 = list = ((EntityTrackerUpdateS2CPacket)packet).trackedValues();
       final int n2 = n;
       EntityEntityTrackerUpdateS2CPacketHelper entityEntityTrackerUpdateS2CPacketHelper = this;
       Runnable runnable = new Runnable(){

           @Override
           public void run() {
               EntityEntityTrackerUpdateS2CPacketHelper.this.PM(n2, list2);
           }
       };
       Mc.client.execute(runnable);
   }

   public boolean PK(Entity entity2) {
      return this.list.contains(entity2);
   }

   public Box PL(Entity entity2) {
      EntityPose entityPose = EntityPose.SWIMMING;
      EntityDimensions temp = entity2.getDimensions(entityPose);
      Vec3d vec = entity2.getPos();
      return temp.getBoxAt(vec);
   }

   void PM(int n3, List list3) {
      Entity entity2 = Mc.client.world.getEntityById(n3);
      if (entity2 instanceof PlayerEntity) {
         Iterator iterator2 = list3.iterator();

         while (iterator2.hasNext()) {
            SerializedEntry serializedEntry = (SerializedEntry)iterator2.next();
            TrackedData temp = entity2.getDataTracker().entries[serializedEntry.id()].getData();
            TrackedData trackedData = Entity.POSE;
            if (temp.equals(trackedData)) {
               if (!serializedEntry.value().equals(EntityPose.SWIMMING)) {
                  this.list.remove(entity2);
               } else if (!this.list.contains(entity2)) {
                  this.list.add(entity2);
               }
            }
         }
      }
   }
}
