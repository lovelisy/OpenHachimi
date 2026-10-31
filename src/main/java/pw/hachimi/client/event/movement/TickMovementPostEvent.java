package pw.hachimi.client.event.movement;

import net.minecraft.client.input.Input;
import pw.hachimi.client.event.Event;

public class TickMovementPostEvent extends Event {
   public final Input input;
   public static boolean a;

   public TickMovementPostEvent(Input input2) {
      this.input = input2;
   }

   public Input getInput() {
      return this.input;
   }
}
