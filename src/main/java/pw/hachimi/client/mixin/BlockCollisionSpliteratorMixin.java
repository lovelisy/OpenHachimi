package pw.hachimi.client.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.render.GetCollisionShapeEvent;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(BlockCollisionSpliterator.class)
public class BlockCollisionSpliteratorMixin {
   @Redirect(
      method = "computeNext",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/block/BlockState;getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"
      )
   )
   private VoxelShape hookGetCollisionShape(BlockState state, BlockView blockView, BlockPos pos, ShapeContext shapeContext) {
      VoxelShape voxelShape = state.getCollisionShape(blockView, pos, shapeContext);
      if (blockView != Mc.client.world) {
         return voxelShape;
      }

      GetCollisionShapeEvent getCollisionShapeEvent = new GetCollisionShapeEvent(voxelShape, pos, state);
      EventBusImpl.INSTANCE.rC(getCollisionShapeEvent);
      return getCollisionShapeEvent.aik() ? getCollisionShapeEvent.getVoxelShape() : voxelShape;
   }
}
