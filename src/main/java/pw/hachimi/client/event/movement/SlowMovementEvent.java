package pw.hachimi.client.event.movement;

import net.minecraft.block.BlockState;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class SlowMovementEvent extends Event {
   private final BlockState state;
   private float b;
   public static boolean gn;

   public SlowMovementEvent(BlockState state2) {
      this.state = state2;
   }

   public BlockState getState() {
      return this.state;
   }

   public float WV() {
      return this.b;
   }

   public void WW(float f) {
      this.b = f;
   }
}
