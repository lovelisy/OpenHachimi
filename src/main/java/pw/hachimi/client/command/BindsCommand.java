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

public class BindsCommand extends ClientCommand {
   public static boolean af;

   public BindsCommand() {
      super("Binds", "show keybinds of modules", aep("binds"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(BindsCommand::lg);
   }

   private static int lg(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UB("§s[Keybinding]");
      ArrayList arrayList = new ArrayList();
      Iterator iterator2 = Managers.moduleManager.XM().iterator();

      StringBuilder stringBuilder;
      int n;
      label59:
      while (true) {
         if (!iterator2.hasNext()) {
            iterator2 = arrayList.iterator();
            n = 0;
            stringBuilder = new StringBuilder();
            break;
         }

         ModuleBase moduleBase = (ModuleBase)iterator2.next();
         if (moduleBase instanceof ClientModule) {
            label57:
            while (true) {
               if (((ClientModule)moduleBase).awi().agt() != -1) {
                  ArrayList temp = arrayList;
                  String str = "§7" + ((ClientModule)moduleBase).OO() + "§s " + ((ClientModule)moduleBase).awi().agv();
                  temp.add(str);
               }

               while (iterator2.hasNext()) {
                  moduleBase = (ModuleBase)iterator2.next();
                  if (moduleBase instanceof ClientModule) {
                     continue label57;
                  }
               }

               iterator2 = arrayList.iterator();
               n = 0;
               new StringBuilder();
               if (!iterator2.hasNext()) {
                  return 1;
               }

               stringBuilder = new StringBuilder((String)iterator2.next());
               if (++n >= 3 || !iterator2.hasNext()) {
                  ChatUtils.UE(stringBuilder.toString());
                  n = 0;
               }
               break label59;
            }
         }
      }

      while (iterator2.hasNext()) {
         if (n == 0) {
            stringBuilder = new StringBuilder((String)iterator2.next());
         } else {
            StringBuilder stringBuilder2 = stringBuilder.append("§f, ");
            String str2 = (String)iterator2.next();
            stringBuilder2.append(str2);
         }

         if (++n >= 3 || !iterator2.hasNext()) {
            ChatUtils.UE(stringBuilder.toString());
            n = 0;
         }
      }

      return 1;
   }
}
