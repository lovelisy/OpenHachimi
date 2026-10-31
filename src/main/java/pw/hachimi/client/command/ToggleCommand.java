package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.utils.chat.ChatUtils;

public class ToggleCommand extends ClientCommand {
   public ToggleCommand() {
      super("Toggle", "Enables/Disables a module", aep("toggle"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      LiteralArgumentBuilder temp = (LiteralArgumentBuilder)literalArgumentBuilder.then(aer("module", CommandSourceHelper.Sc()).executes(ToggleCommand::Ok));
      Command command2 = ToggleCommand::Oj;
      temp.executes(command2);
   }

   private static int Oj(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Must provide module to toggle!");
      return 1;
   }

   private static int Ok(CommandContext commandContext) throws CommandSyntaxException {
      ModuleBase moduleBase = CommandSourceHelper.Sd(commandContext);
      if (moduleBase instanceof ClientModule) {
         ((ClientModule)moduleBase).awa();
         Object[] temp = new Object[]{"§7" + ((ClientModule)moduleBase).OO() + "§f", null};
         boolean flag = ((ClientModule)moduleBase).awg();
         Object[] objs = temp;
         Object[] objs2 = temp;
         Object[] objs3;
         String str;
         if (flag) {
            temp = objs2;
            Object[] temp2 = objs;
            str = "§senabled§f";
            objs2 = temp2;
            objs3 = temp;
         } else {
            temp = objs2;
            Object[] objs4 = objs;
            str = "§cdisabled§f";
            objs2 = objs4;
            objs3 = temp;
         }

         String str2 = "%s is now %s";
         Object[] objs5 = objs3;
         objs2[1] = str;
         ChatUtils.UD(str2, objs5);
      }

      return 1;
   }
}
