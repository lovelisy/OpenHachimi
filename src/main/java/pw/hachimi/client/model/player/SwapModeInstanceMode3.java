package pw.hachimi.client.model.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import pw.hachimi.client.Mc;
import pw.hachimi.client.api.UnidentifiedInterface;

public enum SwapModeInstanceMode3 implements UnidentifiedInterface {
   INSTANCE;

   private static final SwapModeInstanceMode3[] a_ = new SwapModeInstanceMode3[]{INSTANCE};
   private static final long a = 0L;

   @Override
   public boolean ba(int n, int n2) {
      ClientPlayerEntity player2 = Mc.client.player;
      SlotActionType slotActionType = SlotActionType.SWAP;
      int n3 = Mc.client.player.currentScreenHandler.syncId;
      Mc.client.interactionManager.clickSlot(n3, n, n2, slotActionType, (PlayerEntity)player2);
      return true;
   }

   @Override
   public boolean bc() {
      return false;
   }

   @Override
   public void bb(int n, int n2) {
      ClientPlayerEntity player2 = Mc.client.player;
      SlotActionType slotActionType = SlotActionType.SWAP;
      int n3 = Mc.client.player.currentScreenHandler.syncId;
      Mc.client.interactionManager.clickSlot(n3, n, n2, slotActionType, (PlayerEntity)player2);
   }

   private static SwapModeInstanceMode3[] aiJ() {
      return new SwapModeInstanceMode3[]{INSTANCE};
   }
}
