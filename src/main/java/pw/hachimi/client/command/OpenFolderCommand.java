package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.utils.chat.ChatUtils;

public class OpenFolderCommand extends ClientCommand {
   public static boolean b;

   public OpenFolderCommand() {
      super("OpenFolder", "Opens the client configurations folder", aep("openfolder"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(OpenFolderCommand::arl);
   }

   private static int arl(CommandContext commandContext) throws CommandSyntaxException {
      try {
         OpenFolderCommandFileUtils.Do(Bootstrap.clientPaths.getPath().toFile());
      } catch (Exception exception) {
         exception.printStackTrace();
         ChatUtils.UL("Failed to open client folder!");
      }

      return 1;
   }
}
