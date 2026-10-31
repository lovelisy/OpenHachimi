package pw.hachimi.client.model.player;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import pw.hachimi.client.Mc;
import pw.hachimi.client.api.UnidentifiedInterface;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.HitResultWorldHelper;

public enum SwapModeInstanceMode implements UnidentifiedInterface {
   INSTANCE;

   private static final SwapModeInstanceMode[] a_ = new SwapModeInstanceMode[]{INSTANCE};
   private static final long a = 0L;

   @Override
   public boolean ba(int n, int n2) {
      if (n < 9) {
         Managers.inventoryTracker.JF(n);
      } else if (HitResultWorldHelper.Sq().Su()) {
         ClientPlayNetworkHandler temp = Mc.client.getNetworkHandler();
         PickFromInventoryC2SPacket pickFromInventoryC2SPacket = new PickFromInventoryC2SPacket(n);
         temp.sendPacket((Packet)pickFromInventoryC2SPacket);
      } else {
         Managers.inventoryTracker.JK(n, n2);
      }

      return true;
   }

   @Override
   public boolean bc() {
      return false;
   }

   @Override
   public void bb(int n, int n2) {
      if (n < 9) {
         Managers.inventoryTracker.JF(n2);
      } else if (HitResultWorldHelper.Sq().Su()) {
         ClientPlayNetworkHandler temp = Mc.client.getNetworkHandler();
         PickFromInventoryC2SPacket pickFromInventoryC2SPacket = new PickFromInventoryC2SPacket(n);
         temp.sendPacket((Packet)pickFromInventoryC2SPacket);
      } else {
         Managers.inventoryTracker.JK(n, n2);
      }
   }

   private static SwapModeInstanceMode[] lQ() {
      return new SwapModeInstanceMode[]{INSTANCE};
   }
}
