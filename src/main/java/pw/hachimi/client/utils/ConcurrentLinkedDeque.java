package pw.hachimi.client.utils;

import org.jetbrains.annotations.NotNull;

public class ConcurrentLinkedDeque extends java.util.concurrent.ConcurrentLinkedDeque {
   private final int a = 20;
   public static boolean ba;

   @Override
   public boolean add(@NotNull Object obj) {
      boolean flag = super.add(obj);

      while (flag && this.size() > this.a) {
         super.remove();
      }

      return flag;
   }

   @Override
   public void addFirst(@NotNull Object obj) {
      super.addFirst(obj);

      while (this.size() > this.a) {
         super.removeLast();
      }
   }

   public int ats() {
      return this.a;
   }
}
