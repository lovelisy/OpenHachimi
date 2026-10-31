package pw.hachimi.client.model.player;

import pw.hachimi.client.api.UnidentifiedInterface;

public enum SwapMode {
   Screen,
   Hotbar,
   Pick;

   private static final SwapMode[] d = es();

   public UnidentifiedInterface er() {
      return (UnidentifiedInterface)(switch (this) {
         case Screen -> SwapModeInstanceMode3.INSTANCE;
         case Pick -> SwapModeInstanceMode.INSTANCE;
         default -> SwapModeInstanceMode2.INSTANCE;
      });
   }

   private static SwapMode[] es() {
      return new SwapMode[]{Screen, Hotbar, Pick};
   }
}
