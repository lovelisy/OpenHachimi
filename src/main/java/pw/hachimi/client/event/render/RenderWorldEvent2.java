package pw.hachimi.client.event.render;

import net.minecraft.client.util.math.MatrixStack;
import pw.hachimi.client.event.Event;

public class RenderWorldEvent2 extends Event {
   private final MatrixStack matrices;
   private final float b;
   public static boolean dz;

   public RenderWorldEvent2(MatrixStack matrices2, float f) {
      this.matrices = matrices2;
      this.b = f;
   }

   public MatrixStack getMatrices() {
      return this.matrices;
   }

   public float UQ() {
      return this.b;
   }
}
