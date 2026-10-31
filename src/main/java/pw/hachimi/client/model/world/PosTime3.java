package pw.hachimi.client.model.world;

import net.minecraft.util.math.BlockPos;

public record PosTime3(BlockPos pos, long time) {
   public BlockPos getPos() {
      return this.pos;
   }

   public long getTime() {
      return this.time;
   }
}
