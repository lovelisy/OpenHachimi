package pw.hachimi.client.model.movement;

public enum FollowerNormalMaceMode {
   Normal,
   Mace;

   private static final FollowerNormalMaceMode[] c = acl();

   private static FollowerNormalMaceMode[] acl() {
      return new FollowerNormalMaceMode[]{Normal, Mace};
   }
}
