package pw.hachimi.client.event.network;

import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import pw.hachimi.client.event.Event;

public class ConnectEvent extends Event {
   private final ServerAddress serverAddress;
   private final ServerInfo serverInfo;
   public static boolean bq;

   public ConnectEvent(ServerAddress serverAddress2, ServerInfo serverInfo2) {
      this.serverAddress = serverAddress2;
      this.serverInfo = serverInfo2;
   }

   public ServerAddress getServerAddress() {
      return this.serverAddress;
   }

   public ServerInfo getServerInfo() {
      return this.serverInfo;
   }
}
