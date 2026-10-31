package pw.hachimi.client.event;

import net.minecraft.client.gui.screen.Screen;

public class SetScreenEvent extends Event {
   private final Screen screen;
   public static boolean fe;

   public SetScreenEvent(Screen screen2) {
      this.screen = screen2;
   }

   public Screen getScreen() {
      return this.screen;
   }
}
