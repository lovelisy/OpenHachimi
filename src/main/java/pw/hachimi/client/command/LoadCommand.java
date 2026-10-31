package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.utils.chat.ChatUtils;

public class LoadCommand extends ClientCommand {
   public static boolean d;

   public LoadCommand() {
      super("Load", "Loads the current config", aep("load"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(LoadCommand::HB);
   }

   private static int HB(CommandContext commandContext) throws CommandSyntaxException {
      Bootstrap.clientPaths.anZ();
      ChatUtils.UB("Loaded client config!");
      return 1;
   }
}
