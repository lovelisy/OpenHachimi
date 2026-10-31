package pw.hachimi.client.utils.combat;

import java.util.LinkedList;

public class UnidentifiedHelper11 {
   private final LinkedList linkedList = new LinkedList();
   public static boolean a;

   public void Ln() {
      LinkedList temp = this.linkedList;
      Long longValue = System.currentTimeMillis() + 1000L;
      temp.add(longValue);
   }

   public int Lo() {
      long l = System.currentTimeMillis();

      while (true) {
         UnidentifiedHelper11 temp = this;

         try {
            if (temp.linkedList.isEmpty() || this.linkedList.peek() == null || (Long)this.linkedList.peek() >= l) {
               break;
            }

            this.linkedList.remove();
         } catch (Exception exception) {
            this.linkedList.clear();
            exception.printStackTrace();
            break;
         }
      }

      return this.linkedList.size();
   }
}
