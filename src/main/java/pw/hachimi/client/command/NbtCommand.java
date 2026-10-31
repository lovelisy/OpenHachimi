package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import pw.hachimi.client.utils.chat.ChatUtils;

public class NbtCommand extends ClientCommand {
   public static boolean j;

   public NbtCommand() {
      super("Nbt", "Displays all nbt tags on the held item", aep("nbt"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(NbtCommand::Wv);
   }

   private static int Wv(CommandContext commandContext) throws CommandSyntaxException {
      ItemStack stack = client.player.getMainHandStack();
      if (!stack.getComponents().isEmpty()) {
         ChatUtils.UL("No Nbt tags on this item!");
         return 0;
      } else {
         ChatUtils.UB(stack.getComponents().toString());
         return 1;
      }
   }
}
