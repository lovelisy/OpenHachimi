package pw.hachimi.client.event.render;

import net.minecraft.client.util.math.MatrixStack;
import pw.hachimi.client.event.Event;

public class RenderWorld_2Event extends Event {
   private final MatrixStack matrices;
   private final float b;

   public RenderWorld_2Event(MatrixStack matrices2, float f) {
      this.matrices = matrices2;
      this.b = f;
   }

   public MatrixStack getMatrices() {
      return this.matrices;
   }

   public float acs() {
      return this.b;
   }
}
