package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import pw.hachimi.client.gui.StackComponentTypeShulkerBoxScreen;

public class PeekCommand extends ClientCommand {
   private static final ItemStack[] a = new ItemStack[27];

   public PeekCommand() {
      super("Peek", "Shows the contents of held shulkers", aep("peek"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(PeekCommand::eM);
   }

   private static int eM(CommandContext commandContext) throws CommandSyntaxException {
      StackComponentTypeShulkerBoxScreen.YC(client.player.getMainHandStack(), a);
      return 1;
   }
}
