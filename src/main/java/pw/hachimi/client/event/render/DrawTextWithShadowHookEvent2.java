package pw.hachimi.client.event.render;

import net.minecraft.client.gui.hud.ChatHudLine.Visible;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class DrawTextWithShadowHookEvent2 extends Event {
   private final Visible visible;
   private double b;
   private boolean c;
   public static boolean cc;

   public DrawTextWithShadowHookEvent2(Visible visible2) {
      this.visible = visible2;
   }

   public double Hk() {
      return this.b;
   }

   public void Hl(double d) {
      this.b = d;
   }

   public void Hm(boolean flag) {
      this.c = flag;
   }

   public Visible getVisible() {
      return this.visible;
   }

   public boolean Ho() {
      return this.c;
   }
}
