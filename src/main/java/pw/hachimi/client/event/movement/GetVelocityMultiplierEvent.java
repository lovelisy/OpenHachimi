package pw.hachimi.client.event.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class GetVelocityMultiplierEvent extends Event {
   private final BlockState state;
   public static boolean fh;

   public GetVelocityMultiplierEvent(BlockState state2) {
      this.state = state2;
   }

   public Block aqh() {
      return this.state.getBlock();
   }
}
