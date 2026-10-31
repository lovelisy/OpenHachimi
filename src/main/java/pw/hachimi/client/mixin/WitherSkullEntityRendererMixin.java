package pw.hachimi.client.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.WitherSkullEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.WitherSkullEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.RenderEvent6;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(WitherSkullEntityRenderer.class)
public class WitherSkullEntityRendererMixin {
   @Inject(method = "render(Lnet/minecraft/entity/projectile/WitherSkullEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
   private void hookRender(WitherSkullEntity witherSkullEntity, float f, float f2, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, CallbackInfo callbackInfo) {
      RenderEvent6 renderEvent6 = new RenderEvent6();
      EventBusImpl.INSTANCE.rC(renderEvent6);
      if (renderEvent6.aik()) {
         callbackInfo.cancel();
      }
   }
}
