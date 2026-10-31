package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.utils.chat.ChatUtils;

public class HideAllCommand extends ClientCommand {
   public HideAllCommand() {
      super("HideAll", "Hides all modules from the arraylist", aep("hideall"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(HideAllCommand::EN);
   }

   private static int EN(CommandContext commandContext) throws CommandSyntaxException {
      Iterator iterator2 = Managers.moduleManager.XM().iterator();

      while (iterator2.hasNext()) {
         ModuleBase moduleBase = (ModuleBase)iterator2.next();
         if (moduleBase instanceof ClientModule && !((ClientModule)moduleBase).WS()) {
            ((ClientModule)moduleBase).WT(true);
         }
      }

      ChatUtils.UB("All modules are hidden");
      return 1;
   }
}
