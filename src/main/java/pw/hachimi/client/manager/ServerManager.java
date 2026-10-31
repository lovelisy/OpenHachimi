package pw.hachimi.client.manager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.packet.Packet;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.network.ConnectEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.handler.PacketHandler;
import pw.hachimi.client.utils.combat.UnidentifiedHelper11;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;
import net.minecraft.client.network.PendingUpdateManager;
import pw.hachimi.client.config.ConfigContainer;
import pw.hachimi.client.mixin.ClientWorldMixin2;

public class ServerManager {
   private static final Set a_ = new HashSet();
   private final UnidentifiedHelper11 unidentifiedHelper11 = new UnidentifiedHelper11();
   private final UnidentifiedHelper11 unidentifiedHelper112 = new UnidentifiedHelper11();
   private ServerAddress serverAddress;
   private ServerInfo serverInfo;

   public ServerManager() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   public void onConnect(ConnectEvent connectEvent) {
      this.serverAddress = connectEvent.getServerAddress();
      this.serverInfo = connectEvent.getServerInfo();
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      a_.clear();
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      this.unidentifiedHelper11.Ln();
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      this.unidentifiedHelper112.Ln();
   }

   public void iN(ServerAddress serverAddress2, ServerInfo serverInfo2) {
      if (Mc.client.getNetworkHandler() != null) {
         ClientConnection temp = Mc.client.getNetworkHandler().getConnection();
         String temp2 = serverAddress2.getAddress();
         int temp3 = serverAddress2.getPort();
         MinecraftClient client2 = Mc.client;
         int n2 = temp3;
         String str = temp2;
         temp = temp;
         temp2 = str;
         ClientLoginNetworkHandler clientLoginNetworkHandler = a(client2.getNetworkHandler().getConnection(), Mc.client, serverInfo2);
         n2 = n2;
         str = temp2;
         temp.connect(str, n2, (ClientLoginPacketListener)clientLoginNetworkHandler);
      }
   }

   public void iO(char c, Packet packet2) {
      if (Mc.client.getNetworkHandler() != null) {
         a_.add(packet2);
         Mc.client.getNetworkHandler().sendPacket(packet2);
      }
   }

   public void iP(Packet packet2) {
      if (Mc.client.getNetworkHandler() != null) {
         a_.add(packet2);
         ((PacketHandler)Mc.client.getNetworkHandler()).jG(packet2);
      }
   }

   // restored with CFR (compiles, call/field/constant fingerprint equals the deobf jar)
   public void iQ(SequencedPacketCreator sequencedPacketCreator) {
       if (Mc.client.world == null) return;
       PendingUpdateManager pendingUpdateManager = ((ClientWorldMixin2)Mc.client.world).hookGetPendingUpdateManager().incrementSequence();
       try {
           int n = pendingUpdateManager.getSequence();
           Packet packet = sequencedPacketCreator.predict(n);
           this.iO('\uf560', packet);
           if (pendingUpdateManager == null) return;
       }
       catch (Throwable throwable) {
           throwable.printStackTrace();
           if (pendingUpdateManager == null) throw throwable;
           try {
               pendingUpdateManager.close();
           }
           catch (Throwable throwable2) {
               throwable2.printStackTrace();
               throwable.addSuppressed(throwable2);
               throw throwable;
           }
           throw throwable;
       }
       pendingUpdateManager.close();
   }

   // restored with CFR (compiles, call/field/constant fingerprint equals the deobf jar)
   public void iR(SequencedPacketCreator sequencedPacketCreator) {
       if (Mc.client.world == null) return;
       PendingUpdateManager pendingUpdateManager = ((ClientWorldMixin2)Mc.client.world).hookGetPendingUpdateManager().incrementSequence();
       try {
           int n = pendingUpdateManager.getSequence();
           Packet packet = sequencedPacketCreator.predict(n);
           this.iP(packet);
           if (pendingUpdateManager == null) return;
       }
       catch (Throwable throwable) {
           throwable.printStackTrace();
           if (pendingUpdateManager == null) throw throwable;
           try {
               pendingUpdateManager.close();
           }
           catch (Throwable throwable2) {
               throwable2.printStackTrace();
               throwable.addSuppressed(throwable2);
               throw throwable;
           }
           throw throwable;
       }
       pendingUpdateManager.close();
   }

   public int iS() {
      if (Mc.client.getNetworkHandler() != null) {
         ClientPlayNetworkHandler temp = Mc.client.getNetworkHandler();
         UUID uuid = Mc.client.player.getGameProfile().getId();
         PlayerListEntry playerListEntry = temp.getPlayerListEntry(uuid);
         if (playerListEntry != null) {
            return playerListEntry.getLatency();
         }
      }

      return 0;
   }

   public ServerAddress getServerAddress() {
      return this.serverAddress;
   }

   public void setServerAddress(ServerAddress serverAddress2) {
      this.serverAddress = serverAddress2;
   }

   public ServerInfo getServerInfo() {
      return this.serverInfo;
   }

   public void setServerInfo(ServerInfo serverInfo2) {
      this.serverInfo = serverInfo2;
   }

   public boolean iX() {
      return this.jb().contains("9b9t.com");
   }

   public boolean iY() {
      return this.jb().contains("2b2t.org");
   }

   public int iZ() {
      return this.unidentifiedHelper11.Lo();
   }

   public int ja() {
      return this.unidentifiedHelper112.Lo();
   }

   public String jb() {
      return this.serverInfo != null ? this.serverInfo.address : "Singleplayer";
   }

   public boolean jc(Packet packet2) {
      return a_.contains(packet2);
   }

   private static ClientLoginNetworkHandler a(ClientConnection clientConnection, MinecraftClient client2, ServerInfo serverInfo2) {
      return new ClientLoginNetworkHandler(clientConnection, client2, serverInfo2, null, false, null, null, null);
   }
}
