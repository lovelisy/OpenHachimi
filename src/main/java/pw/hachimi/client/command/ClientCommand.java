package pw.hachimi.client.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import pw.hachimi.client.manager.Managers;

public abstract class ClientCommand {
   public static MinecraftClient client;
   private final String text;
   private final String text2;
   private final List list = new ArrayList();

   public ClientCommand(String str, String str2, LiteralArgumentBuilder literalArgumentBuilder) {
      this.text = str;
      this.text2 = str2;
      this.list.add(literalArgumentBuilder);
   }

   public ClientCommand(String str, String str2, List list2) {
      this.text = str;
      this.text2 = str2;
      this.list.addAll(list2);
   }

   protected static LiteralArgumentBuilder aep(String str) {
      return LiteralArgumentBuilder.literal(str);
   }

   protected static List aeq(String... value) {
      ArrayList arrayList = Lists.newArrayList();
      int n = value.length;

      for (int n2 = 0; n2 < n; n2++) {
         String str = value[n2];
         ArrayList temp = arrayList;
         LiteralArgumentBuilder literalArgumentBuilder = LiteralArgumentBuilder.literal(str);
         temp.add(literalArgumentBuilder);
      }

      return arrayList;
   }

   protected static RequiredArgumentBuilder aer(String str, ArgumentType argumentType) {
      return RequiredArgumentBuilder.argument(str, argumentType);
   }

   protected static SuggestionProvider aes(String... value) {
      final String[] strs = value;
      return new SuggestionProvider() {
         public CompletableFuture getSuggestions(CommandContext var1x, SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException {
            return ClientCommand.aey(strs, var1x, suggestionsBuilder);
         }
      };
   }

   public abstract void aet(LiteralArgumentBuilder var1);

   public List getList() {
      return this.list;
   }

   public String getText() {
      return this.text;
   }

   public String getText2() {
      return this.text2;
   }

   public String aex() {
      LiteralArgumentBuilder literalArgumentBuilder = (LiteralArgumentBuilder)this.list.getFirst();
      String[] strs = Managers.commandSourceChatScreenHelper
         .getCommandDispatcher()
         .getAllUsage(literalArgumentBuilder.build(), Managers.commandSourceChatScreenHelper.getCommandSource(), false);
      return strs[((String[])strs).length - 1];
   }

   static CompletableFuture aey(String[] strs, CommandContext commandContext, SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException {
      return CommandSource.suggestMatching(Lists.newArrayList(strs), suggestionsBuilder);
   }
}
