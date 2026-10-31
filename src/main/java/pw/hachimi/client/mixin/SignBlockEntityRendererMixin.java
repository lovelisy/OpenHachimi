package pw.hachimi.client.mixin;

import net.minecraft.block.entity.SignText;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.RenderTextEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(SignBlockEntityRenderer.class)
public class SignBlockEntityRendererMixin {
   @Inject(method = "renderText(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/SignText;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IIIZ)V", at = @At("HEAD"), cancellable = true)
   private void hookRenderText(
      BlockPos pos, SignText signText, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, int n2, int n3, boolean flag, CallbackInfo callbackInfo
   ) {
      RenderTextEvent renderTextEvent = new RenderTextEvent();
      EventBusImpl.INSTANCE.rC(renderTextEvent);
      if (renderTextEvent.aik()) {
         callbackInfo.cancel();
      }
   }
}
