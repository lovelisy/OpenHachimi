package pw.hachimi.client.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.RenderLabelIfPresentEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
   @Inject(method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V", at = @At("HEAD"), cancellable = true)
   public void hookRenderLabelIfPresent(Entity entity2, Text text2, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, float f, CallbackInfo callbackInfo) {
      RenderLabelIfPresentEvent renderLabelIfPresentEvent = new RenderLabelIfPresentEvent(entity2);
      EventBusImpl.INSTANCE.rC(renderLabelIfPresentEvent);
      if (renderLabelIfPresentEvent.aik()) {
         callbackInfo.cancel();
      }
   }
}
