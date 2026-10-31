package pw.hachimi.client.model;

public enum SwitchRandomMode {
   Switch,
   Random,
   Multi;

   private static final SwitchRandomMode[] d = Yt();

   private static SwitchRandomMode[] Yt() {
      return new SwitchRandomMode[]{Switch, Random, Multi};
   }
}
