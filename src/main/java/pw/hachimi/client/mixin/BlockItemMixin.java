package pw.hachimi.client.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.utils.world.BlockPosBlockUtils;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(BlockItem.class)
public class BlockItemMixin {
   @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z", at = @At("RETURN"))
   private void onPlace(@NotNull ItemPlacementContext itemPlacementContext, BlockState state, CallbackInfoReturnable callbackInfoReturnable) {
      if (itemPlacementContext.getWorld().isClient) {
         EventBusImpl.INSTANCE.rB(BlockPosBlockUtils.hm(itemPlacementContext.getBlockPos(), state.getBlock()));
      }
   }
}
