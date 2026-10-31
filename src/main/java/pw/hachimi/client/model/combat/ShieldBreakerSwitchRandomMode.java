package pw.hachimi.client.model.combat;

public enum ShieldBreakerSwitchRandomMode {
   Switch,
   Random,
   Multi;

   private static final ShieldBreakerSwitchRandomMode[] d = PF();

   private static ShieldBreakerSwitchRandomMode[] PF() {
      return new ShieldBreakerSwitchRandomMode[]{Switch, Random, Multi};
   }
}
