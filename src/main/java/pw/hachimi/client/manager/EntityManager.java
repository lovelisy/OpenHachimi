package pw.hachimi.client.manager;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.render.BeginRenderTickEvent;
import pw.hachimi.client.model.combat.CurrentAverageMode;
import pw.hachimi.client.utils.ConcurrentLinkedDeque;
import pw.hachimi.client.utils.UnidentifiedHelper10;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class EntityManager {
   private final Deque deque = new ConcurrentLinkedDeque();
   private long b;
   private float c = 1.0F;

   public EntityManager() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      this.deque.clear();
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (Mc.client.player != null && Mc.client.world != null) {
         if (channelRead0Event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            float f = 20000.0F / (float)(System.currentTimeMillis() - this.b);
            Deque temp = this.deque;
            Float floatValue = f;
            temp.addFirst(floatValue);
            this.b = System.currentTimeMillis();
         }
      }
   }

   public void rT(float f) {
      this.c = f;
   }

   @EventListener
   public void onBeginRenderTick(BeginRenderTickEvent beginRenderTickEvent) {
      if (this.c != 1.0F) {
         beginRenderTickEvent.aim();
         beginRenderTickEvent.qT(this.c);
      }
   }

   public Queue getDeque() {
      return this.deque;
   }

   public float rW() {
      float f = 0.0F;

      try {
         ArrayList arrayList = Lists.newArrayList(this.deque);
         if (!arrayList.isEmpty()) {
            Iterator iterator2 = arrayList.iterator();

            while (iterator2.hasNext()) {
               f += (Float)iterator2.next();
            }

            f /= Math.max((float)arrayList.size(), 1.0F);
         }
      } catch (NullPointerException nullPointerException) {
      }

      return Math.min(100.0F, f);
   }

   public float rX() {
      EntityManager temp = this;

      try {
         if (!temp.deque.isEmpty()) {
            return Math.min(100.0F, (Float)this.deque.getFirst());
         }
      } catch (NoSuchElementException noSuchElementException) {
      }

      return 20.0F;
   }

   public float rY() {
      float f = 20.0F;

      try {
         Iterator iterator2 = this.deque.iterator();

         while (iterator2.hasNext()) {
            float f2 = (Float)iterator2.next();
            if (f2 < f) {
               f = f2;
            }
         }
      } catch (NullPointerException nullPointerException) {
      }

      return f;
   }

   public boolean rZ() {
      boolean flag;
      if (this.deque.size() >= 20) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public float sa(CurrentAverageMode currentAverageMode) {
      return switch (UnidentifiedHelper10.a[currentAverageMode.ordinal()]) {
         case 1 -> this.rW();
         case 2 -> this.rX();
         case 3 -> this.rY();
         case 4 -> 20.0F;
         default -> throw a();
      };
   }

   public float sb(CurrentAverageMode currentAverageMode) {
      return 20.0F / this.sa(currentAverageMode);
   }

   private static MatchException a() {
      return new MatchException(null, null);
   }
}
