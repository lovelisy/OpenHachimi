package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.utils.chat.ChatUtils;

public class MacroCommand extends ClientCommand {
   public MacroCommand() {
      super("Macro", "Creates a new macro preset", aep("macro"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      RequiredArgumentBuilder temp = (RequiredArgumentBuilder)aer("save/load", StringArgumentType.string())
         .suggests(aes("save", "load"))
         .then(aer("macro_name", StringArgumentType.string()).executes(MacroCommand::oc));
      Command command2 = MacroCommand::ob;
      ArgumentBuilder argumentBuilder = temp.executes(command2);
      LiteralArgumentBuilder literalArgumentBuilder2 = (LiteralArgumentBuilder)literalArgumentBuilder.then(argumentBuilder);
      command2 = this::oa;
      literalArgumentBuilder2.executes(command2);
   }

   private int oa(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Invalid usage! Usage: " + this.aex());
      return 1;
   }

   private static int ob(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Must provide a macro to load!");
      return 1;
   }

   private static int oc(CommandContext commandContext) throws CommandSyntaxException {
      String str = StringArgumentType.getString(commandContext, "save/load");
      String str2 = StringArgumentType.getString(commandContext, "macro_name");
      if (str.equalsIgnoreCase("save")) {
         Bootstrap.clientPaths.aoc(str2);
         ChatUtils.UB("Saved macro: §s" + str2);
      } else if (str.equalsIgnoreCase("load") && Bootstrap.clientPaths.aod(str2)) {
         ChatUtils.UB("Loaded macro: §s" + str2);
      }

      return 1;
   }
}
