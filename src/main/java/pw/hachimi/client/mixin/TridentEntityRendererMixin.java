package pw.hachimi.client.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.TridentEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.RenderEvent12;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(TridentEntityRenderer.class)
public class TridentEntityRendererMixin {
   @Inject(method = "render(Lnet/minecraft/entity/projectile/TridentEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
   private void hookRender(TridentEntity tridentEntity, float f, float f2, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, CallbackInfo callbackInfo) {
      RenderEvent12 renderEvent12 = new RenderEvent12();
      EventBusImpl.INSTANCE.rC(renderEvent12);
      if (renderEvent12.aik()) {
         callbackInfo.cancel();
      }
   }
}
