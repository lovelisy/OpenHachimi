package pw.hachimi.client.model.combat;

public enum AutoEZBotCustomMode {
   Bot,
   Custom,
   Mesugaki;

   private static final AutoEZBotCustomMode[] d = aqj();

   private static AutoEZBotCustomMode[] aqj() {
      return new AutoEZBotCustomMode[]{Bot, Custom, Mesugaki};
   }
}
