package pw.hachimi.client.utils.world;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.utils.world.BlockPosBlockUtils;

public class BlockPosBlockUtils {
   private static final BlockPosBlockUtils INSTANCE = new BlockPosBlockUtils();
   public BlockPos pos;
   public Block block;
   private static final long a = 0L;

   public static BlockPosBlockUtils hm(BlockPos pos2, Block block2) {
      INSTANCE.pos = pos2;
      INSTANCE.block = block2;
      return INSTANCE;
   }
}
