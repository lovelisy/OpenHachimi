package pw.hachimi.client.model.network;

public enum DisablerNewDisablerMode {
   Disabler,
   NewDisabler,
   Strict,
   Time,
   None;

   private static final DisablerNewDisablerMode[] f = FY();

   public boolean FW() {
      boolean flag;
      if (this != Strict && this != Time && this != NewDisabler) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public boolean FX() {
      boolean flag;
      if (this != Strict && this != Time) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private static DisablerNewDisablerMode[] FY() {
      return new DisablerNewDisablerMode[]{Disabler, NewDisabler, Strict, Time, None};
   }
}
