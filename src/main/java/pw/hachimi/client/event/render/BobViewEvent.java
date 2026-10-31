package pw.hachimi.client.event.render;

import net.minecraft.client.util.math.MatrixStack;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class BobViewEvent extends Event {
   private final MatrixStack matrices;
   private final float b;
   private float c;

   public BobViewEvent(MatrixStack matrices2, float f) {
      this.matrices = matrices2;
      this.b = f;
   }

   public float JY() {
      return this.c;
   }

   public void JZ(float f) {
      this.c = f;
   }

   public MatrixStack getMatrices() {
      return this.matrices;
   }

   public float Kb() {
      return this.b;
   }
}
