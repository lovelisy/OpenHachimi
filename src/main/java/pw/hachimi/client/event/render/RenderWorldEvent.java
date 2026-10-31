package pw.hachimi.client.event.render;

import net.minecraft.client.util.math.MatrixStack;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class RenderWorldEvent extends Event {
   private final MatrixStack matrices;
   private final float b;
   public static boolean hb;

   public RenderWorldEvent(MatrixStack matrices2, float f) {
      this.matrices = matrices2;
      this.b = f;
   }

   public MatrixStack getMatrices() {
      return this.matrices;
   }

   public float ll() {
      return this.b;
   }
}
