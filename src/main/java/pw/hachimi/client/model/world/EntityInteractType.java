package pw.hachimi.client.model.world;

public enum EntityInteractType {
   INTERACT,
   ATTACK,
   INTERACT_AT;

   private static final EntityInteractType[] d = apE();

   private static EntityInteractType[] apE() {
      return new EntityInteractType[]{INTERACT, ATTACK, INTERACT_AT};
   }
}
