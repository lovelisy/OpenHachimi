package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.utils.UnidentifiedUtils5;
import pw.hachimi.client.utils.chat.ChatUtils;

public class BindCommand extends ClientCommand {
   public BindCommand() {
      super("Bind", "Keybinds a module", aep("bind"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      RequiredArgumentBuilder temp = aer("module", CommandSourceHelper.Sc());
      RequiredArgumentBuilder temp2 = aer("key", StringArgumentType.string());
      Command command2 = BindCommand::aeX;
      RequiredArgumentBuilder requiredArgumentBuilder = temp2;
      RequiredArgumentBuilder requiredArgumentBuilder2 = temp;
      ArgumentBuilder argumentBuilder = requiredArgumentBuilder.executes(command2);
      temp = (RequiredArgumentBuilder)requiredArgumentBuilder2.then(argumentBuilder);
      command2 = BindCommand::aeW;
      ArgumentBuilder argumentBuilder2 = temp.executes(command2);
      LiteralArgumentBuilder literalArgumentBuilder2 = (LiteralArgumentBuilder)literalArgumentBuilder.then(argumentBuilder2);
      command2 = this::aeV;
      literalArgumentBuilder2.executes(command2);
   }

   private int aeV(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Invalid usage! Usage: " + this.aex());
      return 1;
   }

   private static int aeW(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Must provide a module to keybind!");
      return 1;
   }

   private static int aeX(CommandContext commandContext) throws CommandSyntaxException {
      ModuleBase moduleBase = CommandSourceHelper.Sd(commandContext);
      if (moduleBase instanceof ClientModule) {
         String str = StringArgumentType.getString(commandContext, "key");
         if (str == null) {
            ChatUtils.UL("Invalid key!");
            return 0;
         } else if (str.equalsIgnoreCase("none")) {
            ((ClientModule)moduleBase).awf(-1);
            Object[] objs = new Object[]{moduleBase.OO(), "NONE"};
            ChatUtils.UD("§7%s§f is now bound to §s%s", objs);
            return 1;
         } else {
            int n = UnidentifiedUtils5.Xf(str);
            if (n == -1) {
               ChatUtils.UL("Failed to parse key!");
               return 0;
            } else {
               ((ClientModule)moduleBase).awf(n);
               Object[] objs2 = new Object[]{moduleBase.OO(), str.toUpperCase()};
               ChatUtils.UD("§7%s§f is now bound to §s%s", objs2);
               return 1;
            }
         }
      } else {
         ChatUtils.UL("Module must be a toggleable module!");
         return 0;
      }
   }
}
