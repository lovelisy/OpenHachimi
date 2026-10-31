package pw.hachimi.client.model.combat;

public enum MaceSpoofNormalAirMode {
   Normal,
   Air,
   Ground;

   private static final MaceSpoofNormalAirMode[] d = bO();

   private static MaceSpoofNormalAirMode[] bO() {
      return new MaceSpoofNormalAirMode[]{Normal, Air, Ground};
   }
}
