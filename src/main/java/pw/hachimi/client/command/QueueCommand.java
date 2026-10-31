package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.concurrent.ExecutorService;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.utils.chat.ChatUtils;

public class QueueCommand extends ClientCommand {
   public static boolean ad;

   public QueueCommand() {
      super("Queue", "Checks the 2b2t queue", aep("queue"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(QueueCommand::avu);
   }

   private static int avu(CommandContext commandContext) throws CommandSyntaxException {
      ExecutorService temp = Bootstrap.executorService;
      Runnable runnable = QueueCommand::avv;
      temp.submit(runnable);
      return 1;
   }

   private static void avv() {
      String str = Managers.fileManager.acM();
      if (str == null) {
         ChatUtils.UL("Could not fetch 2b2t queue size!");
      } else {
         ChatUtils.UB(str);
      }
   }
}
