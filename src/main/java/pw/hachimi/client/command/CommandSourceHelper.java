package pw.hachimi.client.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.command.CommandSource;
import pw.hachimi.client.config.ConfigContainer;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ModuleBase;

public class CommandSourceHelper implements ArgumentType {
   public static boolean e;
   private static final String b = "%s-module";

   public static CommandSourceHelper Sc() {
      return new CommandSourceHelper();
   }

   public static ModuleBase Sd(CommandContext commandContext) {
      return (ModuleBase)commandContext.getArgument("module", ModuleBase.class);
   }

   public ModuleBase Se(long l, StringReader stringReader) throws CommandSyntaxException {
      String str = stringReader.readString();
      Object[] objs = new Object[]{str.toLowerCase()};
      String str2 = String.format("%s-module", objs);
      ModuleBase moduleBase = Managers.moduleManager.XL(str2);
      if (moduleBase == null) {
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(stringReader, null);
      } else {
         return moduleBase;
      }
   }

   public CompletableFuture listSuggestions(CommandContext commandContext, SuggestionsBuilder suggestionsBuilder) {
      Stream temp = Managers.moduleManager.XM().stream();
      Function function2 = new Function() {
         @Override
         public Object apply(Object commandContext) {
            return ((ConfigContainer)commandContext).getText2();
         }
      };
      return CommandSource.suggestMatching(temp.map(function2), suggestionsBuilder);
   }

   public Collection getExamples() {
      Stream temp = Managers.moduleManager.XM().stream();
      Function function2 = new Function() {
         @Override
         public Object apply(Object obj) {
            return ((ConfigContainer)obj).getText2();
         }
      };
      return temp.map(function2).limit(10L).toList();
   }

   public Object parse(StringReader stringReader) throws CommandSyntaxException {
      return this.Se(40074129044734L, stringReader);
   }
}
