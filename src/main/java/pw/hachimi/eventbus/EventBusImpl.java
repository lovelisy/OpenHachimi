package pw.hachimi.eventbus;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import pw.hachimi.client.api.UnidentifiedInterface10;
import pw.hachimi.client.api.UnidentifiedInterface4;
import pw.hachimi.client.event.Event;
import pw.hachimi.eventbus.annotation.EventListener;

public class EventBusImpl implements EventBus {
   public static final EventBus INSTANCE = new EventBusImpl();
   private static final UnidentifiedInterface10 a_ = EventBusImpl::EM;
   private static final UnidentifiedInterface4[] b = new UnidentifiedInterface4[0];
   private final IdentityHashMap identityHashMap = new IdentityHashMap();
   private final Map map = new ConcurrentHashMap();
   private final ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
   private static final long a = 0L;

   @Override
   public boolean rA(Class classValue) {
      UnidentifiedInterface4[] unidentifiedInterface4s = (UnidentifiedInterface4[])this.concurrentHashMap.get(classValue);
      return unidentifiedInterface4s != null && unidentifiedInterface4s.length > 0;
   }

   @Override
   public Object rB(Object obj) {
      ConcurrentHashMap temp = this.concurrentHashMap;
      Class classValue = obj.getClass();
      UnidentifiedInterface4[] unidentifiedInterface4s = (UnidentifiedInterface4[])temp.get(classValue);
      if (unidentifiedInterface4s != null) {
         int n = unidentifiedInterface4s.length;

         for (int n2 = 0; n2 < n; n2++) {
            unidentifiedInterface4s[n2].cv(obj);
         }
      }

      return obj;
   }

   @Override
   public Event rC(Event event2) {
      ConcurrentHashMap temp = this.concurrentHashMap;
      Class classValue = event2.getClass();
      UnidentifiedInterface4[] unidentifiedInterface4s = (UnidentifiedInterface4[])temp.get(classValue);
      if (unidentifiedInterface4s != null) {
         int n = unidentifiedInterface4s.length;

         for (int n2 = 0; n2 < n; n2++) {
            unidentifiedInterface4s[n2].cv(event2);
            if (event2.aik()) {
               break;
            }
         }
      }

      return event2;
   }

   @Override
   public void rD(Object obj) {
      Iterator iterator2 = this.EI(obj.getClass(), obj).iterator();

      while (iterator2.hasNext()) {
         UnidentifiedInterface4 unidentifiedInterface4 = (UnidentifiedInterface4)iterator2.next();
         this.EG(unidentifiedInterface4, false);
      }
   }

   @Override
   public void rE(Class classValue) {
      Iterator iterator2 = this.EI(classValue, null).iterator();

      while (iterator2.hasNext()) {
         UnidentifiedInterface4 unidentifiedInterface4 = (UnidentifiedInterface4)iterator2.next();
         this.EG(unidentifiedInterface4, true);
      }
   }

   @Override
   public void rF(UnidentifiedInterface4 unidentifiedInterface4) {
      this.EG(unidentifiedInterface4, false);
   }

   @Override
   public void rG(Object obj) {
      Iterator iterator2 = this.EI(obj.getClass(), obj).iterator();

      while (iterator2.hasNext()) {
         UnidentifiedInterface4 unidentifiedInterface4 = (UnidentifiedInterface4)iterator2.next();
         this.EH(506, (short)2895, unidentifiedInterface4, false);
      }
   }

   @Override
   public void rH(Class classValue) {
      Iterator iterator2 = this.EI(classValue, null).iterator();

      while (iterator2.hasNext()) {
         UnidentifiedInterface4 unidentifiedInterface4 = (UnidentifiedInterface4)iterator2.next();
         this.EH(506, (short)2895, unidentifiedInterface4, true);
      }
   }

   @Override
   public void rI(UnidentifiedInterface4 unidentifiedInterface4, char c, char c2, int n) {
      long temp = ((long)c << 48 | (long)c2 << 48 >>> 16 | (long)n << 32 >>> 32) ^ 14992928370484L;
      int n2 = (int)((((long)c << 48 | (long)c2 << 48 >>> 16 | (long)n << 32 >>> 32) ^ 14992928370484L) >>> 32);
      int n3 = (int)((((long)c << 48 | (long)c2 << 48 >>> 16 | (long)n << 32 >>> 32) ^ 14992928370484L) << 32 >>> 48);
      int temp2 = (int)(temp << 48 >>> 48);
      this.EH(n2, (short)n3, unidentifiedInterface4, false);
   }

   private void EG(UnidentifiedInterface4 unidentifiedInterface4, boolean flag) {
      if (flag && !unidentifiedInterface4.cy()) {
         return;
      }

      Class classValue = unidentifiedInterface4.cw();
      synchronized (this) {
         UnidentifiedInterface4[] unidentifiedInterface4s = (UnidentifiedInterface4[])this.concurrentHashMap.getOrDefault(classValue, b);

         int n = 0;
         while (n < unidentifiedInterface4s.length && unidentifiedInterface4.cx() <= unidentifiedInterface4s[n].cx()) {
            n++;
         }

         UnidentifiedInterface4[] unidentifiedInterface4s2 = new UnidentifiedInterface4[unidentifiedInterface4s.length + 1];
         System.arraycopy(unidentifiedInterface4s, 0, unidentifiedInterface4s2, 0, n);
         unidentifiedInterface4s2[n] = unidentifiedInterface4;
         System.arraycopy(unidentifiedInterface4s, n, unidentifiedInterface4s2, n + 1, unidentifiedInterface4s.length - n);
         this.concurrentHashMap.put(classValue, unidentifiedInterface4s2);
      }
   }

   private void EH(int n, short s, UnidentifiedInterface4 unidentifiedInterface4, boolean flag) {
      if (flag && !unidentifiedInterface4.cy()) {
         return;
      }

      Class classValue = unidentifiedInterface4.cw();
      synchronized (this) {
         UnidentifiedInterface4[] unidentifiedInterface4s = (UnidentifiedInterface4[])this.concurrentHashMap.get(classValue);
         if (unidentifiedInterface4s == null) {
            return;
         }

         int n2 = -1;
         for (int n3 = 0; n3 < unidentifiedInterface4s.length; n3++) {
            if (unidentifiedInterface4s[n3] == unidentifiedInterface4) {
               n2 = n3;
               break;
            }
         }

         if (n2 < 0) {
            return;
         }

         if (unidentifiedInterface4s.length == 1) {
            this.concurrentHashMap.put(classValue, b);
            return;
         }

         UnidentifiedInterface4[] unidentifiedInterface4s2 = new UnidentifiedInterface4[unidentifiedInterface4s.length - 1];
         System.arraycopy(unidentifiedInterface4s, 0, unidentifiedInterface4s2, 0, n2);
         System.arraycopy(unidentifiedInterface4s, n2 + 1, unidentifiedInterface4s2, n2, unidentifiedInterface4s.length - n2 - 1);
         this.concurrentHashMap.put(classValue, unidentifiedInterface4s2);
      }
   }

   private List EI(Class classValue, Object obj) {
      if (obj == null) {
         return (List)this.map.computeIfAbsent(classValue, key -> this.EL((Class)key));
      }

      synchronized (this.identityHashMap) {
         List list = (List)this.identityHashMap.get(obj);
         if (list != null) {
            return list;
         }

         ArrayList arrayList = new ArrayList();
         this.EJ(arrayList, classValue, obj);
         this.identityHashMap.put(obj, arrayList);
         return arrayList;
      }
   }

   private void EJ(List list, Class classValue, Object obj) {
      for (Method method : classValue.getDeclaredMethods()) {
         if (this.EK(method)) {
            EventBusImplHelper eventBusImplHelper = new EventBusImplHelper(a_, classValue, obj, method);
            list.add(eventBusImplHelper);
         }
      }

      if (classValue.getSuperclass() != null) {
         this.EJ(list, classValue.getSuperclass(), obj);
      }
   }

   private boolean EK(Method method) {
      Class<EventListener> classValue = EventListener.class;
      if (!method.isAnnotationPresent(classValue)) {
         return false;
      } else if (method.getReturnType() != void.class) {
         return false;
      } else {
         return method.getParameterCount() != 1 ? false : !method.getParameters()[0].getType().isPrimitive();
      }
   }

   List EL(Class classValue) {
      ArrayList arrayList = new ArrayList();
      this.EJ(arrayList, classValue, null);
      return arrayList;
   }

   private static Lookup EM(Method method, Class classValue) throws InvocationTargetException, IllegalAccessException {
      return (Lookup)method.invoke(null, classValue, MethodHandles.lookup());
   }
}
