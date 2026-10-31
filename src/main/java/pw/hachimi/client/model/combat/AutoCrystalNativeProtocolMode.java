package pw.hachimi.client.model.combat;

public enum AutoCrystalNativeProtocolMode {
   NATIVE,
   PROTOCOL;

   private static final AutoCrystalNativeProtocolMode[] c = XE();

   private static AutoCrystalNativeProtocolMode[] XE() {
      return new AutoCrystalNativeProtocolMode[]{NATIVE, PROTOCOL};
   }
}
