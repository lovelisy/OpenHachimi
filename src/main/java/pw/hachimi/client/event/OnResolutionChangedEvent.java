package pw.hachimi.client.event;

import net.minecraft.client.util.Window;

public class OnResolutionChangedEvent extends Event {
   private final Window window;
   public static boolean do_;

   public OnResolutionChangedEvent(Window window2) {
      this.window = window2;
   }

   public Window getWindow() {
      return this.window;
   }
}
