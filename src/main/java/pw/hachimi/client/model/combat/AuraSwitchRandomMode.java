package pw.hachimi.client.model.combat;

public enum AuraSwitchRandomMode {
   Switch,
   Random,
   Multi;

   private static final AuraSwitchRandomMode[] d = Cd();

   private static AuraSwitchRandomMode[] Cd() {
      return new AuraSwitchRandomMode[]{Switch, Random, Multi};
   }
}
