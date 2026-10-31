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

public class ConfigCommand extends ClientCommand {
   public static boolean ah;

   public ConfigCommand() {
      super("Config", "Creates a new configuration preset", aep("config"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      RequiredArgumentBuilder temp = aer("save/load", StringArgumentType.string()).suggests(aes("save", "load"));
      RequiredArgumentBuilder requiredArgumentBuilder = aer("config_name", StringArgumentType.string());
      temp = temp;
      RequiredArgumentBuilder temp2 = (RequiredArgumentBuilder)requiredArgumentBuilder.then(
         aer("visual/module/all", StringArgumentType.string()).suggests(aes("visual", "module", "all")).executes(ConfigCommand::aoG)
      );
      Command command2 = ConfigCommand::aoF;
      requiredArgumentBuilder = temp2;
      RequiredArgumentBuilder requiredArgumentBuilder2 = temp;
      ArgumentBuilder argumentBuilder = requiredArgumentBuilder.executes(command2);
      temp = (RequiredArgumentBuilder)requiredArgumentBuilder2.then(argumentBuilder);
      command2 = ConfigCommand::aoE;
      ArgumentBuilder argumentBuilder2 = temp.executes(command2);
      LiteralArgumentBuilder literalArgumentBuilder2 = (LiteralArgumentBuilder)literalArgumentBuilder.then(argumentBuilder2);
      command2 = this::aoD;
      literalArgumentBuilder2.executes(command2);
   }

   private int aoD(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Invalid usage! Usage: " + this.aex());
      return 1;
   }

   private static int aoE(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Must provide a config name!");
      return 1;
   }

   private static int aoF(CommandContext commandContext) throws CommandSyntaxException {
      String str = StringArgumentType.getString(commandContext, "save/load");
      String str2 = StringArgumentType.getString(commandContext, "config_name");
      if (str.equalsIgnoreCase("save")) {
         Bootstrap.clientPaths.aoa(str2, "all");
         ChatUtils.UB("Saved config: §s" + str2);
      } else if (str.equalsIgnoreCase("load") && Bootstrap.clientPaths.aob(str2, "all")) {
         ChatUtils.UB("Loaded config: §s" + str2);
      }

      return 1;
   }

   private static int aoG(CommandContext commandContext) throws CommandSyntaxException {
      String str = StringArgumentType.getString(commandContext, "save/load");
      String str2 = StringArgumentType.getString(commandContext, "config_name");
      String str3 = StringArgumentType.getString(commandContext, "visual/module/all");
      if (str.equalsIgnoreCase("save")) {
         Bootstrap.clientPaths.aoa(str2, str3);
         ChatUtils.UB("Saved config: §s" + str2);
      } else if (str.equalsIgnoreCase("load") && Bootstrap.clientPaths.aob(str2, str3)) {
         ChatUtils.UB("Loaded config: §s" + str2);
      }

      return 1;
   }
}
