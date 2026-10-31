package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.utils.chat.ChatUtils;

public class ModulesCommand extends ClientCommand {
   public static boolean t;

   public ModulesCommand() {
      super("Modules", "Displays all client modules", aep("modules"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(ModulesCommand::Xx);
   }

   private static int Xx(CommandContext commandContext) throws CommandSyntaxException {
      ArrayList arrayList = new ArrayList();
      Iterator iterator2 = Managers.moduleManager.XM().iterator();

      while (iterator2.hasNext()) {
         ModuleBase moduleBase = (ModuleBase)iterator2.next();
         String str;
         if (moduleBase instanceof ClientModule && ((ClientModule)moduleBase).awg()) {
            str = "§s";
         } else {
            str = "§f";
         }

         String str2 = str;
         ArrayList temp = arrayList;
         String str3 = str2 + moduleBase.OO();
         temp.add(str3);
      }

      ChatUtils.UE(" §7Modules:§f " + String.join(", ", arrayList));
      return 1;
   }
}
