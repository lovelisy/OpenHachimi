package pw.hachimi.client.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import pw.hachimi.client.setting.BlockListSetting;
import pw.hachimi.client.setting.EntityTypeSetting;
import pw.hachimi.client.setting.ItemListSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper18;

public class SettingCommandDefaultedRegistryIdentifierHelper implements ArgumentType {
   public static SettingCommandDefaultedRegistryIdentifierHelper MO() {
      return new SettingCommandDefaultedRegistryIdentifierHelper();
   }

   public static Object MP(CommandContext commandContext) {
      return commandContext.getArgument("list", Object.class);
   }

   public Object parse(StringReader stringReader) throws CommandSyntaxException {
      String str = stringReader.readString();
      DefaultedRegistry temp = Registries.ITEM;
      Identifier id = Identifier.of("minecraft", str);
      Item item2 = (Item)temp.get(id);
      if (item2 != Items.AIR) {
         return item2;
      } else {
         temp = Registries.BLOCK;
         Identifier id2 = Identifier.of("minecraft", str);
         Block block2 = (Block)temp.get(id2);
         if (block2 != Blocks.AIR) {
            return block2;
         } else {
            temp = Registries.ENTITY_TYPE;
            Identifier id3 = Identifier.of("minecraft", str);
            EntityType entityType = (EntityType)temp.get(id3);
            if (!str.equalsIgnoreCase("pig") && entityType == EntityType.PIG) {
               throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(stringReader, null);
            } else {
               return entityType;
            }
         }
      }
   }

   public CompletableFuture listSuggestions(CommandContext commandContext, SuggestionsBuilder suggestionsBuilder) {
      Setting setting2 = UnidentifiedHelper18.akO(commandContext);
      String str = StringArgumentType.getString(commandContext, "value");
      if (!str.equalsIgnoreCase("add") && !str.equalsIgnoreCase("remove") && !str.equalsIgnoreCase("del")) {
         return suggestionsBuilder.buildFuture();
      }

      if (setting2 instanceof ItemListSetting) {
         Iterator iterator2 = Registries.ITEM.iterator();

         while (iterator2.hasNext()) {
            Item item2 = (Item)iterator2.next();
            suggestionsBuilder.suggest(Registries.ITEM.getId(item2).getPath());
         }
      }

      if (setting2 instanceof BlockListSetting) {
         Iterator iterator3 = Registries.BLOCK.iterator();

         while (iterator3.hasNext()) {
            Block block2 = (Block)iterator3.next();
            suggestionsBuilder.suggest(Registries.BLOCK.getId(block2).getPath());
         }
      }

      if (setting2 instanceof EntityTypeSetting) {
         Iterator iterator4 = Registries.ENTITY_TYPE.iterator();

         while (iterator4.hasNext()) {
            EntityType entityType = (EntityType)iterator4.next();
            suggestionsBuilder.suggest(Registries.ENTITY_TYPE.getId(entityType).getPath());
         }
      }

      return suggestionsBuilder.buildFuture();
   }
}
