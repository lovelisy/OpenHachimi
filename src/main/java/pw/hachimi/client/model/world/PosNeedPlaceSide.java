package pw.hachimi.client.model.world;

import net.minecraft.util.math.BlockPos;

public record PosNeedPlaceSide(BlockPos pos, boolean needPlaceSide) {
   public BlockPos getPos() {
      return this.pos;
   }
}
