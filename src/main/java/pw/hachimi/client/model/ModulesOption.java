package pw.hachimi.client.model;

public enum ModulesOption {
   LENGTH,
   ALPHABETICAL;

   private static final ModulesOption[] c = MN();

   private static ModulesOption[] MN() {
      return new ModulesOption[]{LENGTH, ALPHABETICAL};
   }
}
