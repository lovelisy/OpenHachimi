package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.utils.UnidentifiedUtils5;
import pw.hachimi.client.utils.chat.ChatUtils;

public class PrefixCommand extends ClientCommand {
   public PrefixCommand() {
      super("Prefix", "Allows you to change the chat command prefix", aep("prefix"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      LiteralArgumentBuilder temp = (LiteralArgumentBuilder)literalArgumentBuilder.then(aer("prefix", StringArgumentType.string()).executes(PrefixCommand::avl));
      Command command2 = PrefixCommand::avk;
      temp.executes(command2);
   }

   private static int avk(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Please provide a new prefix!");
      return 1;
   }

   private static int avl(CommandContext commandContext) throws CommandSyntaxException {
      String str = StringArgumentType.getString(commandContext, "prefix");
      if (str.length() > 1) {
         ChatUtils.UL("Prefix can only be one character!");
         return 0;
      } else {
         int n = UnidentifiedUtils5.Xf(str);
         Managers.commandSourceChatScreenHelper.adJ(str, n);
         ChatUtils.UB("Command prefix changed to §s" + str);
         return 1;
      }
   }
}
