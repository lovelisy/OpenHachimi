package pw.hachimi.client.mixin;

import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.hachimi.client.event.render.RenderEvent10;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(EnchantingTableBlockEntityRenderer.class)
public class EnchantingTableBlockEntityRendererMixin {
   @Shadow
   @Final
   private BookModel book;

   @Inject(
      method = "render(Lnet/minecraft/block/entity/EnchantingTableBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/entity/model/BookModel;renderBook(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V",
         shift = Shift.BEFORE
      ),
      cancellable = true
   )
   private void hookRender(EnchantingTableBlockEntity enchantingTableBlockEntity, float f, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int n, int n2, CallbackInfo callbackInfo) {
      RenderEvent10 renderEvent10 = new RenderEvent10();
      EventBusImpl.INSTANCE.rC(renderEvent10);
      if (renderEvent10.aik()) {
         callbackInfo.cancel();
         matrices.pop();
      }
   }
}
