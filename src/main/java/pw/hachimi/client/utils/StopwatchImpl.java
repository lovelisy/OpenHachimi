package pw.hachimi.client.utils;

import java.util.concurrent.TimeUnit;

public class StopwatchImpl implements Stopwatch {
   private long a = System.nanoTime();
   private long b;

   @Override
   public boolean afq(Number number) {
      return this.afs() > number.longValue();
   }

   public boolean afO(Number number, TimeUnit timeUnit) {
      long l = number.longValue();
      return this.afq(timeUnit.toMillis(l));
   }

   @Override
   public long afs() {
      return this.afS(System.nanoTime() - this.a);
   }

   @Override
   public void aft(Number number) {
      long l;
      if (number.longValue() == -255L) {
         l = 0L;
      } else {
         l = System.nanoTime() - number.longValue();
      }

      this.a = l;
   }

   public void afP(Number number) {
      this.a = this.a + number.longValue();
   }

   public long afQ(TimeUnit timeUnit) {
      long temp = this.afs();
      TimeUnit timeUnit2 = TimeUnit.MILLISECONDS;
      long l = temp;
      return timeUnit.convert(l, timeUnit2);
   }

   public long afR() {
      return this.b;
   }

   @Override
   public void afr(int n, int n2, byte b2) {
      if (n > 0) {
         label59:
         while (b2 >= 0) {
            if (n > 0) {
               long l = System.nanoTime();
               if (n2 <= 0) {
                  return;
               }

               do {
                  if (n > 0) {
                     this.b = l - this.a;
                     if (n <= 0) {
                        return;
                     }

                     while (n <= 0) {
                        if (b2 < 0) {
                           break label59;
                        }
                     }

                     this.a = l;
                     if (n >= 0 || n > 0) {
                        while (n <= 0) {
                           if (b2 < 0) {
                              break label59;
                           }
                        }

                        return;
                     }
                     break label59;
                  }
               } while (b2 >= 0);
               break;
            }
         }

         while (n <= 0) {
            if (b2 >= 0) {
            }
         }
      }
   }

   private long afS(long l) {
      return l / 1000000L;
   }
}
