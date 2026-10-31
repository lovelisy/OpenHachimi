package pw.hachimi.client.model.movement;

public enum VelocityNormalGrimMode {
   NORMAL,
   GRIM;

   private static final VelocityNormalGrimMode[] c = Lg();

   private static VelocityNormalGrimMode[] Lg() {
      return new VelocityNormalGrimMode[]{NORMAL, GRIM};
   }
}
