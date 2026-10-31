package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import pw.hachimi.client.mixin.MinecraftClientMixin2;
import pw.hachimi.client.utils.chat.ChatUtils;

public class LeaveCommand extends ClientCommand {
   public static boolean aj;

   public LeaveCommand() {
      super("Leave", "Leaves the game without disconnecting from the server", aep("leave"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(LeaveCommand::aqm);
   }

   private static int aqm(CommandContext commandContext) throws CommandSyntaxException {
      if (client.isInSingleplayer()) {
         ChatUtils.UL("Not connected to a server!");
         return 0;
      } else {
         client.getNetworkHandler().unloadWorld();
         client.world = null;
         client.player = null;
         ((MinecraftClientMixin2)client).hookSetWorld(null);
         MinecraftClient temp = client;
         TitleScreen titleScreen = new TitleScreen();
         temp.setScreenAndRender((Screen)titleScreen);
         return 1;
      }
   }
}
