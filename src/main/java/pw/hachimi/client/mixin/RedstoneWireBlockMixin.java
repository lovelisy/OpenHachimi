package pw.hachimi.client.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pw.hachimi.client.handler.BlockHandler;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin extends Block implements BlockHandler {
   @Shadow
   @Final
   private BlockState dotState;
   @Shadow
   @Final
   public static IntProperty POWER;

   public RedstoneWireBlockMixin(Settings settings) {
      super(settings);
   }

   @Shadow
   private static boolean isNotConnected(BlockState state2) {
      return false;
   }

   @Shadow
   private static boolean isFullyConnected(BlockState state2) {
      return false;
   }

   @Shadow
   protected abstract BlockState getPlacementState(BlockView var1, BlockState var2, BlockPos var3);

   @Override
   public boolean jo(BlockState state2, World world2, BlockPos pos, PlayerEntity player2) {
      if (player2.getAbilities().allowModifyWorld && (isFullyConnected(state2) || isNotConnected(state2))) {
         BlockState state3 = isFullyConnected(state2) ? this.getDefaultState() : this.dotState;
         state3 = (BlockState)state3.with(POWER, (Integer)state2.get(POWER));
         state3 = this.getPlacementState(world2, state3, pos);
         return state3 != state2;
      } else {
         return false;
      }
   }
}
