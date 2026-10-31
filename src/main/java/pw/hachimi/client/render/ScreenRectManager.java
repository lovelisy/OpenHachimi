package pw.hachimi.client.render;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import net.minecraft.client.gui.ScreenRect;

public class ScreenRectManager {
   private final Deque deque = new ArrayDeque();
   private static final String b = "Scissor stack underflow";

   public ScreenRect KN(ScreenRect screenRect) {
      ScreenRect screenRect2 = (ScreenRect)this.deque.peekLast();
      if (screenRect2 != null) {
         ScreenRect screenRect3 = Objects.requireNonNullElse(screenRect.intersection(screenRect2), ScreenRect.empty());
         this.deque.addLast(screenRect3);
         return screenRect3;
      } else {
         this.deque.addLast(screenRect);
         return screenRect;
      }
   }

   public ScreenRect KO() {
      if (this.deque.isEmpty()) {
         throw new IllegalStateException("Scissor stack underflow");
      }

      this.deque.removeLast();
      return (ScreenRect)this.deque.peekLast();
   }
}
