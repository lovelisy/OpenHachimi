package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.utils.chat.ChatUtils;

public class HelpCommand extends ClientCommand {
   public static boolean ac;

   public HelpCommand() {
      super("Help", "Displays command functionality", aep("help"));
   }

   private static String ael(ClientCommand clientCommand) {
      if (clientCommand.aex().isEmpty()) {
         Object[] objs = new Object[]{clientCommand.getText(), clientCommand.getText2()};
         return String.format("§s%s §7%s", objs);
      } else {
         Object[] objs2;
         Object[] objs3 = objs2 = new Object[]{clientCommand.getText(), null, null};
         Object[] temp = objs3;
         objs2[1] = clientCommand.aex();
         temp[2] = clientCommand.getText2();
         return String.format("§s%s §f%s §7%s", objs3);
      }
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      LiteralArgumentBuilder temp = (LiteralArgumentBuilder)literalArgumentBuilder.then(aer("command", HelpCommandHelper.avw()).executes(HelpCommand::aen));
      Command command2 = HelpCommand::aem;
      temp.executes(command2);
   }

   private static int aem(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UE("§s[Commands Help]");
      Iterator iterator2 = Managers.commandSourceChatScreenHelper.getList().iterator();

      while (iterator2.hasNext()) {
         ClientCommand clientCommand = (ClientCommand)iterator2.next();
         if (!(clientCommand instanceof SettingCommand)) {
            ChatUtils.UE(ael(clientCommand));
         }
      }

      return 1;
   }

   private static int aen(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UB(ael(HelpCommandHelper.avx(commandContext)));
      return 1;
   }
}
