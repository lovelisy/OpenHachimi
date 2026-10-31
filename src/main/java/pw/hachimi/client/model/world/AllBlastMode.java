package pw.hachimi.client.model.world;

public enum AllBlastMode {
   ALL,
   BLAST,
   NONE;

   private static final AllBlastMode[] d = jw();

   private static AllBlastMode[] jw() {
      return new AllBlastMode[]{ALL, BLAST, NONE};
   }
}
