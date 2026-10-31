package pw.hachimi.client.event.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.UnknownEvent8;

@UnidentifiedInterface2
public class BlockPosEvent extends UnknownEvent8 {
   private final BlockPos pos;
   private final Direction direction;
   public static boolean bo;

   public BlockPosEvent(BlockPos pos2, Direction direction2) {
      this.pos = pos2;
      this.direction = direction2;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public Direction getDirection() {
      return this.direction;
   }
}
