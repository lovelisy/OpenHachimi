package pw.hachimi.client.model.chat;

public enum BetterChatNormalGrayMode {
   NORMAL,
   GRAY,
   COLOR,
   OFF;

   private static final BetterChatNormalGrayMode[] e = RY();

   private static BetterChatNormalGrayMode[] RY() {
      return new BetterChatNormalGrayMode[]{NORMAL, GRAY, COLOR, OFF};
   }
}
