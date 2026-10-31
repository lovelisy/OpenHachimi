package pw.hachimi.client.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.RenderEvent5;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
   @Inject(method = "render(Lnet/minecraft/entity/Entity;DDDFFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("RETURN"))
   private void hookRender(
      Entity entity2,
      double d,
      double d2,
      double d3,
      float f,
      float f2,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumerProvider,
      int n,
      CallbackInfo callbackInfo
   ) {
      RenderEvent5 renderEvent5 = new RenderEvent5(entity2, d, d2, d3, f, f2, matrices, vertexConsumerProvider);
      EventBusImpl.INSTANCE.rC(renderEvent5);
   }
}
