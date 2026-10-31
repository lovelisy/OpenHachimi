package pw.hachimi.client.model.player;

import pw.hachimi.client.api.UnidentifiedInterface;
import pw.hachimi.client.manager.Managers;

public enum SwapModeInstanceMode2 implements UnidentifiedInterface {
   INSTANCE;

   private static final SwapModeInstanceMode2[] a_ = new SwapModeInstanceMode2[]{INSTANCE};
   private static final long a = 0L;

   @Override
   public boolean ba(int n, int n2) {
      Managers.inventoryTracker.JF(n);
      return true;
   }

   @Override
   public boolean bc() {
      return false;
   }

   @Override
   public void bb(int n, int n2) {
      Managers.inventoryTracker.JF(n2);
   }

   private static SwapModeInstanceMode2[] agn() {
      return new SwapModeInstanceMode2[]{INSTANCE};
   }
}
