package pw.hachimi.client.event;

import net.minecraft.client.gui.hud.ChatHudLine.Visible;

public class InitEvent extends Event {
   private final Visible visible;
   private final double b;

   public InitEvent(Visible visible2, double d) {
      this.visible = visible2;
      this.b = d;
   }

   public Visible getVisible() {
      return this.visible;
   }

   public double Db() {
      return this.b;
   }
}
