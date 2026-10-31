package pw.hachimi.client.model.combat;

public enum AuraBabyAdultMode {
   Baby,
   Adult,
   Both;

   private static final AuraBabyAdultMode[] d = be();

   private static AuraBabyAdultMode[] be() {
      return new AuraBabyAdultMode[]{Baby, Adult, Both};
   }
}
