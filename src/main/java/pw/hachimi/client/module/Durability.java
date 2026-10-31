package pw.hachimi.client.module;

public class Durability extends DrawRenderModule {
   public Durability() {
      super("Durability", "");
   }

   @Override
   public void adb() {
      int n = client.player.getMainHandStack().getMaxDamage();
      int n2 = client.player.getMainHandStack().getDamage();
      this.text = "Durability §f" + (n - n2);
      super.adb();
   }
}
