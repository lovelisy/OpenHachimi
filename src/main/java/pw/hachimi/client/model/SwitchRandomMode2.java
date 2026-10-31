package pw.hachimi.client.model;

public enum SwitchRandomMode2 {
   Switch,
   Random,
   Multi;

   private static final SwitchRandomMode2[] d = apV();

   private static SwitchRandomMode2[] apV() {
      return new SwitchRandomMode2[]{Switch, Random, Multi};
   }
}
