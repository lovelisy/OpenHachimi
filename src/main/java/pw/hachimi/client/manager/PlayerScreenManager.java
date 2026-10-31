package pw.hachimi.client.manager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.MouseButtonEvent;
import pw.hachimi.client.event.OnKeyEvent;
import pw.hachimi.client.utils.UnidentifiedHelper17;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class PlayerScreenManager {
   private final Set set = new HashSet();
   public static boolean a;

   public PlayerScreenManager() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      Iterator iterator2 = this.set.iterator();

      while (iterator2.hasNext()) {
         UnidentifiedHelper17 unidentifiedHelper17 = (UnidentifiedHelper17)iterator2.next();
         UnidentifiedHelper17 temp = unidentifiedHelper17;

         try {
            if (temp.agt() != -1 && unidentifiedHelper17.c) {
               unidentifiedHelper17.agy();
               if (unidentifiedHelper17.d) {
                  unidentifiedHelper17.agq();
               }
            }
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      }
   }

   @EventListener
   public void onOnKey(OnKeyEvent onKeyEvent) {
      if (Mc.client.player != null && Mc.client.world != null) {
         if (!this.set.isEmpty()) {
            Iterator iterator2 = this.set.iterator();

            while (iterator2.hasNext()) {
               UnidentifiedHelper17 unidentifiedHelper17 = (UnidentifiedHelper17)iterator2.next();
               if (unidentifiedHelper17.agt() < 1000 && onKeyEvent.KC() != -1 && onKeyEvent.KC() == unidentifiedHelper17.agt()) {
                  if (onKeyEvent.KD() == 1 && Mc.client.currentScreen == null) {
                     unidentifiedHelper17.agx();
                     unidentifiedHelper17.agq();
                  } else if (onKeyEvent.KD() == 0 && unidentifiedHelper17.c) {
                     unidentifiedHelper17.agy();
                     if (unidentifiedHelper17.d) {
                        unidentifiedHelper17.agq();
                     }
                  }
               }
            }
         }
      }
   }

   @EventListener
   public void onMouseButton(MouseButtonEvent mouseButtonEvent) {
      if (Mc.client.player != null && Mc.client.world != null) {
         if (!this.set.isEmpty()) {
            Iterator iterator2 = this.set.iterator();

            while (iterator2.hasNext()) {
               UnidentifiedHelper17 unidentifiedHelper17 = (UnidentifiedHelper17)iterator2.next();
               if (unidentifiedHelper17.agt() >= 1000 && mouseButtonEvent.WC() != -1 && mouseButtonEvent.WC() + 1000 == unidentifiedHelper17.agt()) {
                  if (mouseButtonEvent.WD() == 1 && Mc.client.currentScreen == null) {
                     unidentifiedHelper17.agx();
                     unidentifiedHelper17.agq();
                  } else if (mouseButtonEvent.WD() == 0 && unidentifiedHelper17.c) {
                     unidentifiedHelper17.agy();
                     if (unidentifiedHelper17.d) {
                        unidentifiedHelper17.agq();
                     }
                  }
               }
            }
         }
      }
   }

   public void akp(UnidentifiedHelper17 unidentifiedHelper17, int n) {
      final UnidentifiedHelper17 unidentifiedHelper172 = unidentifiedHelper17;
      UnidentifiedHelper17 unidentifiedHelper173 = this.akt(new Predicate() {
         @Override
         public boolean test(Object unidentifiedHelper17) {
            return PlayerScreenManager.akv(unidentifiedHelper172, (UnidentifiedHelper17)unidentifiedHelper17);
         }
      });
      if (unidentifiedHelper173 != null) {
         unidentifiedHelper173.agu(n);
      }
   }

   public void akq(UnidentifiedHelper17... value) {
      int n = value.length;

      for (int n2 = 0; n2 < n; n2++) {
         UnidentifiedHelper17 unidentifiedHelper17 = value[n2];
         this.akr(unidentifiedHelper17);
      }
   }

   public void akr(UnidentifiedHelper17 unidentifiedHelper17) {
      this.set.add(unidentifiedHelper17);
   }

   public void aks(UnidentifiedHelper17 unidentifiedHelper17) {
      this.set.remove(unidentifiedHelper17);
   }

   public UnidentifiedHelper17 akt(Predicate predicate) {
      return (UnidentifiedHelper17)this.set.stream().filter(predicate).findFirst().orElse(null);
   }

   public Collection getSet() {
      return this.set;
   }

   static boolean akv(UnidentifiedHelper17 unidentifiedHelper17, UnidentifiedHelper17 unidentifiedHelper172) {
      String temp = unidentifiedHelper172.Kn();
      String str = unidentifiedHelper17.Kn();
      return temp.equals(str);
   }
}
