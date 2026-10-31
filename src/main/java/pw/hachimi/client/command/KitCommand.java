package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.module.StackPotionContentsComponentManager;
import pw.hachimi.client.utils.chat.ChatUtils;

public class KitCommand extends ClientCommand {
   public static boolean ak;

   public KitCommand() {
      super("Kit", "Loads/Creates/Removes a kit", aep("kit"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      RequiredArgumentBuilder temp = aer("create/del/load", StringArgumentType.string());
      String[] strs = new String[]{"create", "del", "load", "delete", "remove", "list", "save", "saved"};
      temp = (RequiredArgumentBuilder)temp.suggests(aes(strs)).then(aer("kit_name", StringArgumentType.string()).executes(KitCommand::Wo));
      Command command2 = this::Wn;
      ArgumentBuilder argumentBuilder = temp.executes(command2);
      LiteralArgumentBuilder literalArgumentBuilder2 = (LiteralArgumentBuilder)literalArgumentBuilder.then(argumentBuilder);
      Command command3 = this::Wm;
      literalArgumentBuilder2.executes(command3);
   }

   private int Wm(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Invalid usage! Usage: " + this.aex());
      return 1;
   }

   private int Wn(CommandContext commandContext) throws CommandSyntaxException {
      String str = StringArgumentType.getString(commandContext, "create/del/load");
      if (!str.equalsIgnoreCase("list") && !str.equalsIgnoreCase("saved")) {
         ChatUtils.UL("Invalid usage! Usage: " + this.aex());
         return 1;
      }

      ChatUtils.UB("Kit list");
      ArrayList arrayList = new ArrayList();
      File[] files = Bootstrap.clientPaths.file.listFiles();
      int n = files.length;

      for (int n2 = 0; n2 < n; n2++) {
         File file2 = files[n2];
         if (file2.getName().endsWith(".data")) {
            String str2 = file2.getName().replace(".data", "");
            String temp = str2;
            String str3 = StackPotionContentsComponentManager.OY().Pg();
            String str4;
            if (temp.equalsIgnoreCase(str3)) {
               str4 = "§s";
            } else {
               str4 = "§7";
            }

            ArrayList arrayList2 = arrayList;
            String str5 = str4 + str2;
            arrayList2.add(str5);
         }
      }

      Iterator iterator2 = arrayList.iterator();
      n = 0;
      StringBuilder stringBuilder = new StringBuilder();

      while (iterator2.hasNext()) {
         if (n == 0) {
            stringBuilder = new StringBuilder((String)iterator2.next());
         } else {
            StringBuilder stringBuilder2 = stringBuilder.append("§f, ");
            String str6 = (String)iterator2.next();
            stringBuilder2.append(str6);
         }

         if (++n >= 3 || !iterator2.hasNext()) {
            ChatUtils.UE(stringBuilder.toString());
            n = 0;
         }
      }

      return 1;
   }

   private static int Wo(CommandContext commandContext) throws CommandSyntaxException {
      String str = StringArgumentType.getString(commandContext, "kit_name");
      String str2 = StringArgumentType.getString(commandContext, "create/del/load");
      if (str2.equalsIgnoreCase("create") || str2.equalsIgnoreCase("save")) {
         StackPotionContentsComponentManager.OY().Pj(str);
      } else if (str2.equalsIgnoreCase("remove") || str2.equalsIgnoreCase("del") || str2.equalsIgnoreCase("delete")) {
         StackPotionContentsComponentManager.OY().Pk(str);
      } else if (str2.equalsIgnoreCase("load")) {
         StackPotionContentsComponentManager.OY().Pi(str);
      }

      return 1;
   }
}
