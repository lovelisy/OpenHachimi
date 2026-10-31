package pw.hachimi.client.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

public class RefreshEvent extends Event {
   private CommandDispatcher commandDispatcher;
   private CommandSource commandSource;
   private String text;
   public static boolean aj;

   public String getText() {
      return this.text;
   }

   public void setText(String str) {
      this.text = str;
   }

   public CommandSource getCommandSource() {
      return this.commandSource;
   }

   public void setCommandSource(CommandSource commandSource2) {
      this.commandSource = commandSource2;
   }

   public CommandDispatcher getCommandDispatcher() {
      return this.commandDispatcher;
   }

   public void setCommandDispatcher(CommandDispatcher commandDispatcher2) {
      this.commandDispatcher = commandDispatcher2;
   }
}
