package pw.hachimi.client.model.world;

public enum PMSoundTwitterIosMode {
   TWITTER,
   IOS,
   DISCORD,
   STEAM;

   private static final PMSoundTwitterIosMode[] e = mp();

   private static PMSoundTwitterIosMode[] mp() {
      return new PMSoundTwitterIosMode[]{TWITTER, IOS, DISCORD, STEAM};
   }
}
