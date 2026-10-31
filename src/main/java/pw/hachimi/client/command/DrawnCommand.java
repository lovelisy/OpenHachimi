package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.Formatting;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.utils.chat.ChatUtils;

public class DrawnCommand extends ClientCommand {
   public DrawnCommand() {
      super("Drawn", "Toggles the drawn state of the module", aep("drawn"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      RequiredArgumentBuilder temp = aer("module", CommandSourceHelper.Sc());
      Command command2 = DrawnCommand::KI;
      ArgumentBuilder argumentBuilder = temp.executes(command2);
      LiteralArgumentBuilder literalArgumentBuilder2 = (LiteralArgumentBuilder)literalArgumentBuilder.then(argumentBuilder);
      command2 = DrawnCommand::KH;
      literalArgumentBuilder2.executes(command2);
   }

   private static int KH(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Must provide module to draw!");
      return 1;
   }

   private static int KI(CommandContext commandContext) throws CommandSyntaxException {
      ModuleBase moduleBase = CommandSourceHelper.Sd(commandContext);
      if (moduleBase instanceof ClientModule) {
         String str;
         String str2;
         if (!((ClientModule)moduleBase).WS()) {
            ((ClientModule)moduleBase).WT(true);
            String temp = moduleBase.OO();
            str = "§chidden§f";
            str2 = temp;
         } else {
            ((ClientModule)moduleBase).WT(false);
            String str3 = moduleBase.OO();
            str = "§svisible§f";
            str2 = str3;
         }

         String str4 = str;
         String str5 = String.valueOf(Formatting.RESET);
         String str6 = str4;
         ChatUtils.UB("§7" + str2 + "§f is now " + str6 + str5 + " in the Hud");
      }

      return 1;
   }
}
