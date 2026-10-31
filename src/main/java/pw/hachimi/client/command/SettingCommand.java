package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import pw.hachimi.client.utils.UnidentifiedUtils5;
import pw.hachimi.client.utils.UnidentifiedHelper17;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.ColorSetting;
import java.util.function.IntFunction;
import java.util.function.Function;
import java.util.Arrays;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.setting.BlockListSetting;
import pw.hachimi.client.setting.EntityTypeSetting;
import pw.hachimi.client.setting.ItemListSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper18;
import pw.hachimi.client.utils.chat.ChatUtils;

public class SettingCommand extends ClientCommand {
   private final ModuleBase moduleBase;

   public SettingCommand(ModuleBase moduleBase2) {
      super(moduleBase2.getText2(), moduleBase2.getText3(), aep(moduleBase2.getText2().toLowerCase()));
      this.moduleBase = moduleBase2;
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      RequiredArgumentBuilder temp = aer("setting", UnidentifiedHelper18.akN(this.moduleBase));
      RequiredArgumentBuilder temp2 = aer("value", StringArgumentType.string());
      SuggestionProvider suggestionProvider = SettingCommand::Gi;
      RequiredArgumentBuilder requiredArgumentBuilder = temp2;
      temp = temp;
      temp2 = requiredArgumentBuilder.suggests(suggestionProvider);
      Command command2 = this::Gh;
      requiredArgumentBuilder = temp2;
      temp = temp;
      requiredArgumentBuilder = (RequiredArgumentBuilder)requiredArgumentBuilder.executes(command2);
      temp = temp;
      temp2 = requiredArgumentBuilder;
      RequiredArgumentBuilder temp3 = aer("list", SettingCommandDefaultedRegistryIdentifierHelper.MO());
      Command command3 = this::Gg;
      requiredArgumentBuilder = temp3;
      RequiredArgumentBuilder requiredArgumentBuilder2 = temp2;
      RequiredArgumentBuilder requiredArgumentBuilder3 = temp;
      ArgumentBuilder argumentBuilder = requiredArgumentBuilder.executes(command3);
      ArgumentBuilder argumentBuilder2 = requiredArgumentBuilder2.then(argumentBuilder);
      temp = (RequiredArgumentBuilder)requiredArgumentBuilder3.then(argumentBuilder2);
      Command command4 = SettingCommand::Gf;
      ArgumentBuilder argumentBuilder3 = temp.executes(command4);
      LiteralArgumentBuilder literalArgumentBuilder2 = (LiteralArgumentBuilder)literalArgumentBuilder.then(argumentBuilder3);
      Command command5 = this::Ge;
      literalArgumentBuilder2.executes(command5);
   }

   private int FZ(Setting setting2, String str, Object obj) {
      if (setting2 instanceof ItemListSetting) {
         if (!(obj instanceof Item)) {
            ChatUtils.UL("Not an item!");
            return 0;
         }

         if (str.equalsIgnoreCase("add")) {
            ((ItemListSetting)setting2).ai((Item)obj);
            ChatUtils.UB("Added §s" + ((Item)obj).getName().getString() + "§f to §7" + setting2.atu());
         } else if (str.equalsIgnoreCase("del") || str.equalsIgnoreCase("remove")) {
            ((ItemListSetting)setting2).aj((Item)obj);
            ChatUtils.UB("Removed §c" + ((Item)obj).getName().getString() + "§f from §7" + setting2.atu());
         }
      } else if (setting2 instanceof BlockListSetting) {
         if (obj instanceof Item) {
            obj = Block.getBlockFromItem((Item)obj);
         }

         if (!(obj instanceof Block)) {
            ChatUtils.UL("Not a block!");
            return 0;
         }

         if (str.equalsIgnoreCase("add")) {
            ((BlockListSetting)setting2).afT((Block)obj);
            ChatUtils.UB("Added §s" + ((Block)obj).getName().getString() + "§f to §7" + setting2.atu());
         } else if (str.equalsIgnoreCase("del") || str.equalsIgnoreCase("remove")) {
            ((BlockListSetting)setting2).afU((Block)obj);
            ChatUtils.UB("Removed §c" + ((Block)obj).getName().getString() + "§f from §7" + setting2.atu());
         }
      } else if (setting2 instanceof EntityTypeSetting) {
         if (!(obj instanceof EntityType)) {
            ChatUtils.UL("Not an entity type!");
            return 0;
         }

         if (str.equalsIgnoreCase("add")) {
            ((EntityTypeSetting)setting2).mf((EntityType)obj);
            ChatUtils.UB("Added §s" + ((EntityType)obj).getName().getString() + "§f to §7" + setting2.atu());
         } else if (str.equalsIgnoreCase("del") || str.equalsIgnoreCase("remove")) {
            ((EntityTypeSetting)setting2).mg((EntityType)obj);
            ChatUtils.UB("Removed §c" + ((EntityType)obj).getName().getString() + "§f from §7" + setting2.atu());
         }
      }

      return 1;
   }

   private int Ga(Setting setting2, String str) {
      if (setting2 instanceof ItemListSetting) {
         List list2 = (List)setting2.atx();
         if (str.equalsIgnoreCase("list")) {
            if (list2.isEmpty()) {
               ChatUtils.UL("There are no items in the list!");
               return 1;
            }

            ArrayList arrayList = new ArrayList();
            Iterator iterator2 = list2.iterator();

            while (iterator2.hasNext()) {
               Item item2 = (Item)iterator2.next();
               ArrayList temp = arrayList;
               String str2 = item2.getName().getString();
               temp.add(str2);
            }

            ChatUtils.UB("§7" + setting2.atu() + "§f: " + String.join(", ", arrayList));
         }
      } else if (setting2 instanceof BlockListSetting) {
         List list3 = (List)setting2.atx();
         if (str.equalsIgnoreCase("list")) {
            if (list3.isEmpty()) {
               ChatUtils.UL("There are no blocks in the list!");
               return 1;
            }

            ArrayList arrayList2 = new ArrayList();
            Iterator iterator3 = list3.iterator();

            while (iterator3.hasNext()) {
               Block block2 = (Block)iterator3.next();
               ArrayList arrayList3 = arrayList2;
               String str3 = block2.getName().getString();
               arrayList3.add(str3);
            }

            ChatUtils.UB("§7" + setting2.atu() + "§f: " + String.join(", ", arrayList2));
         }
      } else if (setting2 instanceof EntityTypeSetting) {
         List list4 = (List)setting2.atx();
         if (str.equalsIgnoreCase("list")) {
            if (list4.isEmpty()) {
               ChatUtils.UL("There are no entities in the list!");
               return 1;
            }

            ArrayList arrayList4 = new ArrayList();
            Iterator iterator4 = list4.iterator();

            while (iterator4.hasNext()) {
               EntityType entityType = (EntityType)iterator4.next();
               ArrayList arrayList5 = arrayList4;
               String str4 = entityType.getName().getString();
               arrayList5.add(str4);
            }

            ChatUtils.UB("§7" + setting2.atu() + "§f: " + String.join(", ", arrayList4));
         }
      }

      return 1;
   }

   private int Gb(Setting setting2, String str) {
      if (setting2 == null) {
         return 0;
      }

      if (str == null) {
         return 0;
      }

      try {
         if (setting2.atx() instanceof Integer) {
            Integer integer = Integer.parseInt(str);
            if (integer.doubleValue() < ((NumberSetting)setting2).abk().doubleValue()) {
               ChatUtils.UL("Value less than min!");
               return 0;
            }

            if (integer.doubleValue() > ((NumberSetting)setting2).abl().doubleValue()) {
               ChatUtils.UL("Value greater than max!");
               return 0;
            }

            setting2.aty(integer);
            Object[] objs = new Object[]{setting2.atu(), integer.toString()};
            ChatUtils.UD("§7%s§f was set to §s%s", objs);
         } else if (setting2.atx() instanceof Float) {
            Float floatValue = Float.parseFloat(str);
            if (floatValue.doubleValue() < ((NumberSetting)setting2).abk().doubleValue()) {
               ChatUtils.UL("Value less than min!");
               return 0;
            }

            if (floatValue.doubleValue() > ((NumberSetting)setting2).abl().doubleValue()) {
               ChatUtils.UL("Value greater than max!");
               return 0;
            }

            setting2.aty(floatValue);
            Object[] objs2 = new Object[]{setting2.atu(), floatValue.toString()};
            ChatUtils.UD("§7%s§f was set to §s%s", objs2);
         } else if (setting2.atx() instanceof Double) {
            Double doubleValue = Double.parseDouble(str);
            if (doubleValue < ((NumberSetting)setting2).abk().doubleValue()) {
               ChatUtils.UL("Value less than min!");
               return 0;
            }

            if (doubleValue > ((NumberSetting)setting2).abl().doubleValue()) {
               ChatUtils.UL("Value greater than max!");
               return 0;
            }

            setting2.aty(doubleValue);
            Object[] objs3 = new Object[]{setting2.atu(), doubleValue.toString()};
            ChatUtils.UD("§7%s§f was set to §s%s", objs3);
         }
      } catch (NumberFormatException numberFormatException) {
         ChatUtils.UL("Not a number!");
      }

      if (setting2.atx() instanceof Boolean) {
         Boolean boolean2 = this.Gc(str);
         if (boolean2 == null) {
            ChatUtils.UL("Invalid value!");
            return 0;
         }

         setting2.aty(boolean2);
         Object[] objs4 = new Object[]{setting2.atu(), boolean2 ? "True" : "False"};
         ChatUtils.UD("§7%s§f was set to §s%s", objs4);
         return 1;
      }

      if (setting2.atx() instanceof Enum) {
         Function function = new Function() {
            @Override
            public Object apply(Object obj) {
               return ((Enum)obj).name();
            }
         };
         IntFunction intFunction = SettingCommand::Gd;
         String[] strs = (String[])Arrays.stream((Enum[])((Enum)setting2.atx()).getDeclaringClass().getEnumConstants()).map(function).toArray(intFunction);
         int n = -1;

         for (int n2 = 0; n2 < strs.length; n2++) {
            if (strs[n2].replace("_", "").equalsIgnoreCase(str)) {
               n = n2;
               break;
            }
         }

         if (n == -1) {
            ChatUtils.UL("Not a valid mode!");
            return 0;
         }

         setting2.aty(Enum.valueOf(((Enum)setting2.atx()).getDeclaringClass(), strs[n]));
         Object[] objs5 = new Object[]{setting2.atu(), str};
         ChatUtils.UD("§7%s§f was set to mode §s%s", objs5);
         return 1;
      }

      Object obj = setting2.atx();
      if (obj instanceof UnidentifiedHelper17) {
         if (setting2.getText().equalsIgnoreCase("Keybind")) {
            ChatUtils.UL("Use the 'bind' command to keybind modules!");
            return 0;
         }

         setting2.aty(new UnidentifiedHelper17(setting2.Kn(), UnidentifiedUtils5.Xf(str), ((UnidentifiedHelper17)obj).getRunnable()));
         Object[] objs6 = new Object[]{setting2.atu(), str};
         ChatUtils.UD("§7%s§f was set to key §s%s", objs6);
         return 1;
      }

      if (setting2.atx() instanceof String) {
         setting2.aty(str);
         return 1;
      }

      if (setting2.atx() instanceof Color) {
         try {
            Color color = ((ColorSetting)setting2).TL(str);
            setting2.aty(color);
            Object[] objs7 = new Object[]{setting2.atu(), str};
            ChatUtils.UD("§7%s§f was set to §s%s", objs7);
         } catch (IllegalArgumentException illegalArgumentException) {
            ChatUtils.UL("Invalid color!");
         }
      }

      return 1;
   }

   private Boolean Gc(String str) {
      if (str.equalsIgnoreCase("True") || str.equalsIgnoreCase("On")) {
         return true;
      } else {
         return !str.equalsIgnoreCase("False") && !str.equalsIgnoreCase("Off") ? null : false;
      }
   }

   private static String[] Gd(int n) {
      return new String[n];
   }

   private int Ge(CommandContext commandContext) throws CommandSyntaxException {
      ModuleBase moduleBase2 = this.moduleBase;
      if (moduleBase2 instanceof ClientModule) {
         ((ClientModule)moduleBase2).awa();
         Object[] temp = new Object[]{"§7" + ((ClientModule)moduleBase2).OO() + "§f", null};
         boolean flag = ((ClientModule)moduleBase2).awg();
         Object[] objs = temp;
         Object[] objs2 = temp;
         Object[] objs3;
         String str;
         if (flag) {
            temp = objs2;
            Object[] temp2 = objs;
            str = "§senabled§f";
            objs2 = temp2;
            objs3 = temp;
         } else {
            temp = objs2;
            Object[] objs4 = objs;
            str = "§cdisabled§f";
            objs2 = objs4;
            objs3 = temp;
         }

         String str2 = "%s is now %s";
         Object[] objs5 = objs3;
         objs2[1] = str;
         ChatUtils.UD(str2, objs5);
      }

      return 1;
   }

   private static int Gf(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Must provide a value!");
      return 1;
   }

   private int Gg(CommandContext commandContext) throws CommandSyntaxException {
      Setting setting2 = UnidentifiedHelper18.akO(commandContext);
      if (!(setting2 instanceof BlockListSetting) && !(setting2 instanceof ItemListSetting) && !(setting2 instanceof EntityTypeSetting)) {
         return 0;
      }

      String str = StringArgumentType.getString(commandContext, "value");
      Object obj = SettingCommandDefaultedRegistryIdentifierHelper.MP(commandContext);
      return this.FZ(setting2, str, obj);
   }

   private int Gh(CommandContext commandContext) throws CommandSyntaxException {
      Setting setting2 = UnidentifiedHelper18.akO(commandContext);
      String str = StringArgumentType.getString(commandContext, "value");
      if (str.equalsIgnoreCase("list")) {
         return this.Ga(setting2, str);
      }

      if (str.equalsIgnoreCase("reset")) {
         setting2.atD();
         Object[] objs = new Object[]{setting2.atu()};
         ChatUtils.UD("§7%s§f was reset to default value", objs);
         return 1;
      }

      if (str.equalsIgnoreCase("clear")) {
         if (setting2 instanceof ItemListSetting) {
            ((ItemListSetting)setting2).ak();
         } else if (setting2 instanceof BlockListSetting) {
            ((BlockListSetting)setting2).afV();
         } else if (setting2 instanceof EntityTypeSetting) {
            ((EntityTypeSetting)setting2).mh();
         }

         Object[] objs2 = new Object[]{setting2.atu()};
         ChatUtils.UD("§7%s§f was cleared", objs2);
         return 1;
      } else {
         return this.Gb(setting2, str);
      }
   }

   private static CompletableFuture Gi(CommandContext commandContext, SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException {
      Setting setting2 = UnidentifiedHelper18.akO(commandContext);
      if (setting2 instanceof BlockListSetting || setting2 instanceof ItemListSetting || setting2 instanceof EntityTypeSetting) {
         suggestionsBuilder.suggest("add");
         suggestionsBuilder.suggest("del");
         suggestionsBuilder.suggest("remove");
         suggestionsBuilder.suggest("clear");
         suggestionsBuilder.suggest("list");
      }

      return suggestionsBuilder.buildFuture();
   }
}
