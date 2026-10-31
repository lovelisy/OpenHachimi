package pw.hachimi.client.model.combat;

public enum SpearAuraBabyAdultMode {
   Baby,
   Adult,
   Both;

   private static final SpearAuraBabyAdultMode[] d = Rm();

   private static SpearAuraBabyAdultMode[] Rm() {
      return new SpearAuraBabyAdultMode[]{Baby, Adult, Both};
   }
}
