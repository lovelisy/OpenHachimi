package pw.hachimi.client.event;

import net.minecraft.client.input.Input;
import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class Tick_PreEvent extends UnknownEvent8 {
   private final Input input;

   public Tick_PreEvent(Input input2) {
      this.input = input2;
   }

   public Input getInput() {
      return this.input;
   }
}
