package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import net.minecraft.client.MinecraftClient;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.player.PlayerListEntryPlayerHelper;

public class StatsCommand extends ClientCommand {
   public StatsCommand() {
      super("Stats", "Shows 2b2t player stats", aep("stats"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.then(aer("player", PlayerListEntryPlayerHelper.aic()).executes(StatsCommand::hj));
   }

   private static int hj(CommandContext commandContext) throws CommandSyntaxException {
      ExecutorService temp = Bootstrap.executorService;
      final CommandContext commandContext2 = commandContext;
      Runnable runnable = new Runnable() {
         @Override
         public void run() {
            StatsCommand.hk(commandContext2);
         }
      };
      temp.submit(runnable);
      return 1;
   }

   static void hk(CommandContext commandContext) {
      String str = PlayerListEntryPlayerHelper.aid(commandContext, "player");
      Map map = Managers.fileManager.acL(str);
      MinecraftClient temp = client;
      Map temp2 = map;
      final String str2 = str;
      final Map map2 = temp2;
      Runnable runnable = new Runnable() {
         @Override
         public void run() {
            StatsCommand.hl(map2, str2);
         }
      };
      temp.executeSync(runnable);
   }

   static void hl(Map map, String str) {
      if (map == null) {
         ChatUtils.UL("Could not find player 2b2t stats!");
      } else {
         ChatUtils.UB(str + "'s Stats");
         Iterator iterator2 = map.entrySet().iterator();

         while (iterator2.hasNext()) {
            Entry entry = (Entry)iterator2.next();
            ChatUtils.UE("§7" + (String)entry.getKey() + ": §f" + (String)entry.getValue());
         }
      }
   }
}
