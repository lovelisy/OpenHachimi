package pw.hachimi.client.model;

public enum ModulesTopBottomMode {
   Top,
   Bottom,
   Auto;

   private static final ModulesTopBottomMode[] d = abE();

   private static ModulesTopBottomMode[] abE() {
      return new ModulesTopBottomMode[]{Top, Bottom, Auto};
   }
}
