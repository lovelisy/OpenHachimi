package pw.hachimi.client.event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import pw.hachimi.client.api.UnidentifiedInterface2;

public class Event {
   private static final ConcurrentHashMap i = new ConcurrentHashMap();
   private final boolean j;
   private boolean k;
   private static String[] texts;
   private static final long B = 0L;
   private static final String C = null;

   public Event() {
      ConcurrentHashMap temp = i;
      Class temp2 = this.getClass();
      Function function2 = new Function() {
         @Override
         public Object apply(Object obj) {
            return Event.ain((Class)obj);
         }
      };
      Class classValue = temp2;
      this.j = (Boolean)temp.computeIfAbsent(classValue, function2);
   }

   public boolean aij() {
      return this.j;
   }

   public boolean aik() {
      return this.k;
   }

   public void ail(boolean flag) {
      if (this.aij()) {
         this.k = flag;
      } else {
         throw new IllegalStateException(C);
      }
   }

   public void aim() {
      this.ail(true);
   }

   static Boolean ain(Class classValue) {
      Class<UnidentifiedInterface2> classValue2 = UnidentifiedInterface2.class;
      return classValue.isAnnotationPresent(classValue2);
   }

   public static void b(String[] strs) {
      texts = strs;
   }

   public static String[] d() {
      return texts;
   }
}
