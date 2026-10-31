package pw.hachimi.client.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FireworkRocketEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.RenderEvent15;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(FireworkRocketEntityRenderer.class)
public class FireworkRocketEntityRendererMixin {
   @Inject(method = "render(Lnet/minecraft/entity/projectile/FireworkRocketEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
   private void hookRender(FireworkRocketEntity fireworkRocketEntity, float f, float f2, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, CallbackInfo callbackInfo) {
      RenderEvent15 renderEvent15 = new RenderEvent15();
      EventBusImpl.INSTANCE.rC(renderEvent15);
      if (renderEvent15.aik()) {
         callbackInfo.cancel();
      }
   }
}
