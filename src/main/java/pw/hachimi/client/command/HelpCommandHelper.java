package pw.hachimi.client.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import pw.hachimi.client.manager.Managers;

public class HelpCommandHelper implements ArgumentType {
   public static HelpCommandHelper avw() {
      return new HelpCommandHelper();
   }

   public static ClientCommand avx(CommandContext commandContext) {
      return (ClientCommand)commandContext.getArgument("command", ClientCommand.class);
   }

   public ClientCommand avy(StringReader stringReader, long l) throws CommandSyntaxException {
      String str = stringReader.readString();
      ClientCommand clientCommand = Managers.commandSourceChatScreenHelper.adH(str.toLowerCase());
      if (clientCommand == null) {
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(stringReader, null);
      } else {
         return clientCommand;
      }
   }

   public CompletableFuture listSuggestions(CommandContext commandContext, SuggestionsBuilder suggestionsBuilder) {
      Iterator iterator2 = Managers.commandSourceChatScreenHelper.getList().iterator();

      while (iterator2.hasNext()) {
         ClientCommand clientCommand = (ClientCommand)iterator2.next();
         suggestionsBuilder.suggest(clientCommand.getText().toLowerCase());
      }

      return suggestionsBuilder.buildFuture();
   }

   public Object parse(StringReader stringReader) throws CommandSyntaxException {
      return this.avy(stringReader, 41277685980762L);
   }
}
