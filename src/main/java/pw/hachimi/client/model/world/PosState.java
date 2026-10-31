package pw.hachimi.client.model.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public record PosState(BlockPos pos, BlockState state) {
   public BlockPos getPos() {
      return this.pos;
   }
}
