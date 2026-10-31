package pw.hachimi.client.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.RenderEvent9;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
   @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At("HEAD"))
   private void hookRender(BlockEntity blockEntity, float f, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, CallbackInfo callbackInfo) {
      RenderEvent9 renderEvent9 = new RenderEvent9(blockEntity);
      EventBusImpl.INSTANCE.rC(renderEvent9);
   }
}
