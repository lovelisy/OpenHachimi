package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.utils.chat.ChatUtils;

public class SaveCommand extends ClientCommand {
   public SaveCommand() {
      super("Save", "Saves the current config", aep("save"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(SaveCommand::Cz);
   }

   private static int Cz(CommandContext commandContext) throws CommandSyntaxException {
      Bootstrap.clientPaths.anY();
      ChatUtils.UB("Saved client config!");
      return 1;
   }
}
