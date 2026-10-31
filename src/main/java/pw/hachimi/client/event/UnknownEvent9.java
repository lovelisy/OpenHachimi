package pw.hachimi.client.event;

import java.awt.Color;
import net.minecraft.util.math.Vec3d;

public class UnknownEvent9 extends Event {
   private Color color;
   public static boolean x;

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color color2) {
      this.color = color2;
   }

   public Vec3d arM() {
      return new Vec3d(this.color.getRed() / 255.0, this.color.getGreen() / 255.0, this.color.getBlue() / 255.0);
   }

   public int arN() {
      return this.color.getRGB();
   }
}
