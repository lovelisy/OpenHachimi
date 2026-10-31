package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.utils.chat.ChatUtils;

public class DisableAllCommand extends ClientCommand {
   public DisableAllCommand() {
      super("DisableAll", "Disables all enabled modules", aep("disableall"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(DisableAllCommand::hH);
   }

   private static int hH(CommandContext commandContext) throws CommandSyntaxException {
      Iterator iterator2 = Managers.moduleManager.XM().iterator();

      while (iterator2.hasNext()) {
         ModuleBase moduleBase = (ModuleBase)iterator2.next();
         if (moduleBase instanceof ClientModule && ((ClientModule)moduleBase).awg()) {
            ((ClientModule)moduleBase).awc();
         }
      }

      ChatUtils.UB("All modules are disabled");
      return 1;
   }
}
