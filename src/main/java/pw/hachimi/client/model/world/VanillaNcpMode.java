package pw.hachimi.client.model.world;

public enum VanillaNcpMode {
   Vanilla,
   NCP,
   Grim;

   private static final VanillaNcpMode[] d = AG();

   private static VanillaNcpMode[] AG() {
      return new VanillaNcpMode[]{Vanilla, NCP, Grim};
   }
}
