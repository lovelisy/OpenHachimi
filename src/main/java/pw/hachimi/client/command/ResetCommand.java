package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.chat.ChatUtils;

public class ResetCommand extends ClientCommand {
   public ResetCommand() {
      super("Reset", "Resets the settings of a module to default", aep("reset"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      LiteralArgumentBuilder temp = (LiteralArgumentBuilder)literalArgumentBuilder.then(aer("module", CommandSourceHelper.Sc()).executes(ResetCommand::au));
      Command command2 = ResetCommand::at;
      temp.executes(command2);
   }

   private static int at(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Must provide module to reset!");
      return 1;
   }

   private static int au(CommandContext commandContext) throws CommandSyntaxException {
      ModuleBase moduleBase = CommandSourceHelper.Sd(commandContext);
      if (moduleBase == null) {
         ChatUtils.UL("Invalid module!");
         return 0;
      }

      Iterator iterator2 = moduleBase.OQ().iterator();

      while (iterator2.hasNext()) {
         Setting setting2 = (Setting)iterator2.next();
         if (!setting2.getText().equalsIgnoreCase("Enabled") && !setting2.getText().equalsIgnoreCase("Keybind") && !setting2.getText().equalsIgnoreCase("Hidden")) {
            setting2.atD();
         }
      }

      ChatUtils.UB("§7" + moduleBase.OO() + "§f settings were reset to default values");
      return 1;
   }
}
