package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import net.minecraft.client.network.PlayerListEntry;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.Mc;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.player.PlayerListEntryPlayerHelper;

public class SkinGrabCommand extends ClientCommand {
   public static boolean y;

   public SkinGrabCommand() {
      super("SkinGrab", "Downloads player skins", aep("skingrab"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.then(aer("player", PlayerListEntryPlayerHelper.aic()).executes(SkinGrabCommand::iI));
   }

   private static int iI(CommandContext commandContext) throws CommandSyntaxException {
      String str = PlayerListEntryPlayerHelper.aid(commandContext, "player");
      String str2 = null;
      Iterator iterator2 = Mc.client.player.networkHandler.getPlayerList().iterator();

      while (iterator2.hasNext()) {
         PlayerListEntry playerListEntry = (PlayerListEntry)iterator2.next();
         if (str.equals(playerListEntry.getProfile().getName())) {
            str2 = playerListEntry.getSkinTextures().textureUrl();
            break;
         }
      }

      if (str2 == null) {
         ChatUtils.UL("Failed to find skin texture for " + str);
         return 0;
      }

      try {
         URL url = new URL(str2);
         HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
         InputStream inputStream = httpURLConnection.getInputStream();
         FileOutputStream fileOutputStream = new FileOutputStream(Bootstrap.clientPaths.getPath().toString() + "/" + str + ".png");
         if (httpURLConnection.getResponseCode() != 200) {
            return 0;
         }

         fileOutputStream.write(inputStream.readAllBytes());
         ChatUtils.UB(str + " skin downloaded to client folder");
      } catch (IOException iOException) {
         ChatUtils.UL("Failed to download skin texture for " + str);
      }

      return 1;
   }
}
