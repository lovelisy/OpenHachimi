package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pw.hachimi.client.event.IsEmptyEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(ItemFrameEntityRenderer.class)
public class ItemFrameEntityRendererMixin {
   @WrapOperation(method = "render(Lnet/minecraft/entity/decoration/ItemFrameEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
   private boolean redirectIsEmpty(ItemStack stack, Operation operation) {
      IsEmptyEvent isEmptyEvent = new IsEmptyEvent();
      EventBusImpl.INSTANCE.rC(isEmptyEvent);
      return isEmptyEvent.aik() ? true : (Boolean)operation.call(new Object[]{stack});
   }
}
