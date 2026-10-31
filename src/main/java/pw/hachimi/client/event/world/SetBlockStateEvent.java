package pw.hachimi.client.event.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class SetBlockStateEvent extends Event {
   private final int a;
   private final BlockPos pos;
   private final BlockState state;

   public SetBlockStateEvent(int n, BlockPos pos2, BlockState state2) {
      this.a = n;
      this.pos = pos2;
      this.state = state2;
   }

   public int apA() {
      return this.a;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public BlockState getState() {
      return this.state;
   }
}
