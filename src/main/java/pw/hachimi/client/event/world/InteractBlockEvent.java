package pw.hachimi.client.event.world;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class InteractBlockEvent extends Event {
   private final ClientPlayerEntity player;
   private final Hand hand;
   private final BlockHitResult blockHitResult;
   public static boolean ab;

   public InteractBlockEvent(ClientPlayerEntity player2, Hand hand2, BlockHitResult blockHitResult2) {
      this.player = player2;
      this.hand = hand2;
      this.blockHitResult = blockHitResult2;
   }

   public ClientPlayerEntity getPlayer() {
      return this.player;
   }

   public Hand getHand() {
      return this.hand;
   }

   public BlockHitResult getBlockHitResult() {
      return this.blockHitResult;
   }
}
