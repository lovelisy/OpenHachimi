package pw.hachimi.client.event.world;

import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class BreakBlockEvent extends Event {
   private final BlockPos pos;

   public BreakBlockEvent(BlockPos pos2) {
      this.pos = pos2;
   }

   public BlockPos getPos() {
      return this.pos;
   }
}
