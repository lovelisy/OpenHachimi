package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.player.PlayerListEntryPlayerHelper;

public class FriendCommand extends ClientCommand {
   public FriendCommand() {
      super("Friend", "Adds/Removes a friend from the player list", aep("friend"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      RequiredArgumentBuilder temp = aer("add/del", StringArgumentType.string());
      String[] strs = new String[]{"add", "del", "delete", "remove", "list"};
      temp = (RequiredArgumentBuilder)temp.suggests(aes(strs))
         .then(aer("friend_name", PlayerListEntryPlayerHelper.aic()).executes(FriendCommand::avh));
      Command command2 = FriendCommand::avg;
      ArgumentBuilder argumentBuilder = temp.executes(command2);
      LiteralArgumentBuilder literalArgumentBuilder2 = (LiteralArgumentBuilder)literalArgumentBuilder.then(argumentBuilder);
      Command command3 = this::avf;
      literalArgumentBuilder2.executes(command3);
   }

   private int avf(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Invalid usage! Usage: " + this.aex());
      return 1;
   }

   private static int avg(CommandContext commandContext) throws CommandSyntaxException {
      if (StringArgumentType.getString(commandContext, "add/del").equalsIgnoreCase("list")) {
         if (Managers.friendManager.awA().isEmpty()) {
            ChatUtils.UL("No friends in list!");
            return 0;
         } else {
            Collection collection = Managers.friendManager.awA();
            ChatUtils.UB("§7Friends: §f" + String.join(", ", collection));
            return 1;
         }
      } else {
         ChatUtils.UL("Must provide player to friend!");
         return 1;
      }
   }

   private static int avh(CommandContext commandContext) throws CommandSyntaxException {
      String str = PlayerListEntryPlayerHelper.aid(commandContext, "friend_name");
      String str2 = StringArgumentType.getString(commandContext, "add/del");
      if (str2.equalsIgnoreCase("add")) {
         if (Managers.friendManager.aws(str)) {
            ChatUtils.UL("Player is already friended!");
            return 0;
         }

         ChatUtils.UB("Added friend with name §s" + str);
         Managers.friendManager.awv(str);
      } else if (str2.equalsIgnoreCase("remove") || str2.equalsIgnoreCase("del") || str2.equalsIgnoreCase("delete")) {
         if (!Managers.friendManager.aws(str)) {
            ChatUtils.UL("Player is not friended!");
            return 0;
         }

         ChatUtils.UB("Removed friend with name §c" + str);
         Managers.friendManager.awx(str);
      }

      return 1;
   }
}
