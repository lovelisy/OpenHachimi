package pw.hachimi.client.model.movement;

public enum SpeedStrafeStrafeStrictMode {
   STRAFE,
   STRAFE_STRICT,
   STRAFE_B_HOP,
   LOW_HOP,
   GAY_HOP,
   V_HOP,
   B_HOP,
   VANILLA,
   GRIM_COLLIDE;

   private static final SpeedStrafeStrafeStrictMode[] j = PW();

   private static SpeedStrafeStrafeStrictMode[] PW() {
      return new SpeedStrafeStrafeStrictMode[]{STRAFE, STRAFE_STRICT, STRAFE_B_HOP, LOW_HOP, GAY_HOP, V_HOP, B_HOP, VANILLA, GRIM_COLLIDE};
   }
}
