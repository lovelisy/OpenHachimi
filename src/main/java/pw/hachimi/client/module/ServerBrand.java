package pw.hachimi.client.module;

public class ServerBrand extends DrawRenderModule {
   public ServerBrand() {
      super("ServerBrand", "");
   }

   @Override
   public void adb() {
      this.text = "Brand §f" + client.player.networkHandler.getBrand();
      super.adb();
   }
}
