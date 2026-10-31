package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.utils.chat.ChatUtils;

public class NotifyCommand extends ClientCommand {
   public static boolean s;

   public NotifyCommand() {
      super("Notify", "Notifies in chat when a module is toggled", aep("notify"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.then(aer("module", CommandSourceHelper.Sc()).executes(NotifyCommand::auE));
   }

   private static int auE(CommandContext commandContext) throws CommandSyntaxException {
      ModuleBase moduleBase = CommandSourceHelper.Sd(commandContext);
      if (moduleBase instanceof ClientModule) {
         boolean flag;
         if (!((ClientModule)moduleBase).awj()) {
            flag = true;
         } else {
            flag = false;
         }

         ((ClientModule)moduleBase).awk(flag);
         if (((ClientModule)moduleBase).awj()) {
            ChatUtils.UB("Added §7" + ((ClientModule)moduleBase).OO() + "§f notifications to chat");
         } else {
            ChatUtils.UB("Removed §7" + ((ClientModule)moduleBase).OO() + "§f notifications from chat");
         }

         return 1;
      } else {
         ChatUtils.UL("Invalid module!");
         return 0;
      }
   }
}
