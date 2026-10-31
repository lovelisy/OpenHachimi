package pw.hachimi.client.handler;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface BlockHandler {
   boolean jo(BlockState var1, World var2, BlockPos var3, PlayerEntity var4);
}
