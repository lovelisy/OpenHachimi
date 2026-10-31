package pw.hachimi.client.event.render;

import java.util.List;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class RenderPreEvent extends Event {
   public final LivingEntityRenderer livingEntityRenderer;
   public final LivingEntity living;
   public final float c;
   public final float d;
   public final MatrixStack matrices;
   public final VertexConsumerProvider vertexConsumerProvider;
   public final int g;
   public final RenderLayer renderLayer;
   public final EntityModel entityModel;
   public final List list;
   public static boolean y;

   public RenderPreEvent(
      LivingEntityRenderer livingEntityRenderer2,
      LivingEntity living2,
      float f,
      float f2,
      MatrixStack matrices2,
      VertexConsumerProvider vertexConsumerProvider2,
      int n,
      EntityModel entityModel2,
      RenderLayer renderLayer2,
      List list2
   ) {
      this.livingEntityRenderer = livingEntityRenderer2;
      this.living = living2;
      this.c = f;
      this.d = f2;
      this.matrices = matrices2;
      this.vertexConsumerProvider = vertexConsumerProvider2;
      this.g = n;
      this.entityModel = entityModel2;
      this.renderLayer = renderLayer2;
      this.list = list2;
   }
}
