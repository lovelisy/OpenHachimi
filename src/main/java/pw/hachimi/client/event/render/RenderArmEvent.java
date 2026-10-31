package pw.hachimi.client.event.render;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class RenderArmEvent extends Event {
   public final MatrixStack matrices;
   public final VertexConsumerProvider vertexConsumerProvider;
   public final int c;
   public final Arm arm;
   public final float e;
   public final float f;
   public final PlayerEntityRenderer playerEntityRenderer;

   public RenderArmEvent(MatrixStack matrices2, VertexConsumerProvider vertexConsumerProvider2, int n, float f2, float f3, Arm arm2, PlayerEntityRenderer playerEntityRenderer2) {
      this.matrices = matrices2;
      this.vertexConsumerProvider = vertexConsumerProvider2;
      this.c = n;
      this.e = f2;
      this.f = f3;
      this.arm = arm2;
      this.playerEntityRenderer = playerEntityRenderer2;
   }
}
