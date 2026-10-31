package pw.hachimi.client.model.combat;

public enum ArmorBreakerBabyAdultMode {
   Baby,
   Adult,
   Both;

   private static final ArmorBreakerBabyAdultMode[] d = qE();

   private static ArmorBreakerBabyAdultMode[] qE() {
      return new ArmorBreakerBabyAdultMode[]{Baby, Adult, Both};
   }
}
