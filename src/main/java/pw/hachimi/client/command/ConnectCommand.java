package pw.hachimi.client.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.network.ServerInfo.ServerType;
import net.minecraft.client.resource.language.I18n;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.ServerManager;
import pw.hachimi.client.utils.chat.ChatUtils;

public class ConnectCommand extends ClientCommand {
   public static boolean v;

   public ConnectCommand() {
      super("Connect", "Connects you to a server", aep("connect"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      ((LiteralArgumentBuilder)literalArgumentBuilder.then(aer("server", StringArgumentType.string()))).executes(ConnectCommand::PI);
   }

   private static int PI(CommandContext commandContext) throws CommandSyntaxException {
      String str = StringArgumentType.getString(commandContext, "server");
      if (client.currentScreen instanceof ConnectScreen) {
         ChatUtils.UL("Attempt to connect while already connecting");
         return 0;
      } else {
         client.disconnect();
         client.loadBlockList();
         ServerManager temp = Managers.serverManager;
         ServerAddress temp2 = ServerAddress.parse(str);
         Object[] objs = new Object[0];
         ServerAddress serverAddress = temp2;
         ServerManager serverManager2 = temp;
         ServerInfo serverInfo = new ServerInfo(I18n.translate("selectServer.defaultName", objs), str, ServerType.OTHER);
         serverManager2.iN(serverAddress, serverInfo);
         return 1;
      }
   }
}
