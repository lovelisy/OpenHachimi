package pw.hachimi.client.utils.player;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.network.PlayerListEntry;
import pw.hachimi.client.Mc;

public class PlayerListEntryPlayerHelper implements ArgumentType {
   public static boolean a;

   public static PlayerListEntryPlayerHelper aic() {
      return new PlayerListEntryPlayerHelper();
   }

   public static String aid(CommandContext commandContext, String str) {
      return (String)commandContext.getArgument(str, String.class);
   }

   public String aie(StringReader stringReader) throws CommandSyntaxException {
      return stringReader.readString();
   }

   public CompletableFuture listSuggestions(CommandContext commandContext, SuggestionsBuilder suggestionsBuilder) {
      String[] strs = commandContext.getInput().split(" ");
      Iterator iterator2 = Mc.client.player.networkHandler.getPlayerList().iterator();

      label26:
      while (iterator2.hasNext()) {
         String str = ((PlayerListEntry)iterator2.next()).getProfile().getName();
         int n = strs.length;
         int n2 = 0;

         while (true) {
            if (n2 >= n) {
               continue label26;
            }

            String str2 = strs[n2];
            if (str2.isBlank()) {
               break;
            }

            String temp = str.toLowerCase();
            String str3 = str2.toLowerCase();
            if (temp.startsWith(str3)) {
               break;
            }

            n2++;
         }

         suggestionsBuilder.suggest(str);
      }

      return suggestionsBuilder.buildFuture();
   }

   public Object parse(StringReader stringReader) throws CommandSyntaxException {
      return this.aie(stringReader);
   }
}
