package pw.hachimi.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import pw.hachimi.client.mixin.EntityMixin;
import pw.hachimi.client.utils.chat.ChatUtils;

public class VanishCommand extends ClientCommand {
   private Entity entity;

   public VanishCommand() {
      super("Vanish", "Desyncs the riding entity", aep("vanish"));
   }

   @Override
   public void aet(LiteralArgumentBuilder literalArgumentBuilder) {
      RequiredArgumentBuilder temp = aer("mount", StringArgumentType.string());
      String[] strs = new String[]{"mount", "remount"};
      ArgumentBuilder argumentBuilder = temp.suggests(aes(strs)).executes(this::CT);
      LiteralArgumentBuilder literalArgumentBuilder2 = (LiteralArgumentBuilder)literalArgumentBuilder.then(argumentBuilder);
      Command command2 = this::CS;
      literalArgumentBuilder2.executes(command2);
   }

   private int CS(CommandContext commandContext) throws CommandSyntaxException {
      ChatUtils.UL("Invalid usage! Usage: " + this.aex());
      return 1;
   }

   private int CT(CommandContext commandContext) throws CommandSyntaxException {
      String str = StringArgumentType.getString(commandContext, "mount");
      if (str.equalsIgnoreCase("dismount")) {
         if (client.player.isRiding() && client.player.getVehicle() != null) {
            if (this.entity != null) {
               ChatUtils.UL("Entity vanished, must remount before mounting!");
               return 0;
            }

            this.entity = client.player.getVehicle();
            client.player.dismountVehicle();
            ClientWorld temp = client.world;
            int temp2 = this.entity.getId();
            RemovalReason removalReason = RemovalReason.DISCARDED;
            int n = temp2;
            temp.removeEntity(n, removalReason);
         }
      } else if (str.equalsIgnoreCase("remount")) {
         if (this.entity == null) {
            ChatUtils.UL("No vanished entity!");
            return 0;
         }

         ((EntityMixin)this.entity).hookUnsetRemoved();
         Entity entity2 = this.entity;
         client.world.addEntity(entity2);
         Entity entity3 = this.entity;
         client.player.startRiding(entity3, true);
         this.entity = null;
      }

      return 1;
   }
}
