package pw.hachimi.client.model.combat;

public enum AutoWebAllOnGroundMode {
   All,
   OnGround,
   InAir;

   private static final AutoWebAllOnGroundMode[] d = abz();

   private static AutoWebAllOnGroundMode[] abz() {
      return new AutoWebAllOnGroundMode[]{All, OnGround, InAir};
   }
}
