package pw.hachimi.client.model.player;

public enum ChestStealerEarlyLateMode {
   Early,
   Late,
   None;

   private static final ChestStealerEarlyLateMode[] d = QF();

   private static ChestStealerEarlyLateMode[] QF() {
      return new ChestStealerEarlyLateMode[]{Early, Late, None};
   }
}
