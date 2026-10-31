package pw.hachimi.client.utils;

import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class UnidentifiedHelper22 implements Stopwatch {
   private long a = 0L;

   public UnidentifiedHelper22() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener(priority = Integer.MAX_VALUE)
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         this.a++;
      }
   }

   @Override
   public boolean afq(Number number) {
      boolean flag;
      if (this.a >= number.longValue()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   @Override
   public void afr(int n, int n2, byte b) {
      this.aft(0);
   }

   @Override
   public long afs() {
      return this.a;
   }

   @Override
   public void aft(Number number) {
      this.a = number.longValue();
   }
}
