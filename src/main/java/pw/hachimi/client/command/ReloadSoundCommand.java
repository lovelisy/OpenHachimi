package pw.hachimi.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import pw.hachimi.client.utils.chat.ChatUtils;

public class ReloadSoundCommand extends ClientCommand {
   public static boolean o;

   public ReloadSoundCommand() {
      super("ReloadSound", "Reloads the Minecraft sound system", aep("reloadsound"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      literalArgumentBuilder.executes(ReloadSoundCommand::eU);
   }

   private static int eU(CommandContext commandContext) throws CommandSyntaxException {
      client.getSoundManager().reloadSounds();
      ChatUtils.UB("Reloaded the SoundSystem");
      return 1;
   }
}
