package pw.hachimi.client.event.render;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class RenderEvent3 extends Event {
   public final EndCrystalEntity endCrystalEntity;
   public final float b;
   public final float c;
   public final MatrixStack matrices;
   public final int e_;
   public final ModelPart modelPart;
   public final ModelPart modelPart2;
   public float h;
   public boolean l;
   private final EntityRenderDispatcher entityRenderDispatcher;
   public float n = 1.0F;
   public float o = 1.0F;
   public boolean p = true;
   public static boolean e;

   public RenderEvent3(EndCrystalEntity endCrystalEntity2, float f, float f2, MatrixStack matrices2, int n2, ModelPart modelPart3, ModelPart modelPart4, EntityRenderDispatcher entityRenderDispatcher2) {
      this.endCrystalEntity = endCrystalEntity2;
      this.b = f;
      this.c = f2;
      this.matrices = matrices2;
      this.e_ = n2;
      this.modelPart = modelPart3;
      this.modelPart2 = modelPart4;
      this.entityRenderDispatcher = entityRenderDispatcher2;
   }

   public float oS() {
      return this.n;
   }

   public void oT(float f) {
      this.n = f;
   }

   public float oU() {
      return this.o;
   }

   public void oV(float f) {
      this.o = f;
   }

   public boolean oW() {
      return this.p;
   }

   public void oX(boolean flag) {
      this.p = flag;
   }

   public EntityRenderDispatcher getEntityRenderDispatcher() {
      return this.entityRenderDispatcher;
   }
}
