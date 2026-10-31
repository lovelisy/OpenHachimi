package pw.hachimi.client.event.render;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import pw.hachimi.client.event.Event;

public class RenderEvent5 extends Event {
   public final Entity entity;
   public final double b;
   public final double c;
   public final double d;
   public final float e;
   public final float f;
   public final MatrixStack matrices;
   public final VertexConsumerProvider vertexConsumerProvider;

   public RenderEvent5(Entity entity2, double d2, double d3, double d4, float f2, float f3, MatrixStack matrices2, VertexConsumerProvider vertexConsumerProvider2) {
      this.entity = entity2;
      this.b = d2;
      this.c = d3;
      this.d = d4;
      this.e = f2;
      this.f = f3;
      this.matrices = matrices2;
      this.vertexConsumerProvider = vertexConsumerProvider2;
   }
}
