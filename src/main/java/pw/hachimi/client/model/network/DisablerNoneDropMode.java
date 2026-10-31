package pw.hachimi.client.model.network;

public enum DisablerNoneDropMode {
   None,
   Drop,
   Spam;

   private static final DisablerNoneDropMode[] d = qN();

   private static DisablerNoneDropMode[] qN() {
      return new DisablerNoneDropMode[]{None, Drop, Spam};
   }
}
