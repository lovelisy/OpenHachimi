package pw.hachimi.client.module.player;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.event.world.InteractBlockEvent;
import pw.hachimi.client.event.world.InteractBlock_2Event;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BlockListSetting;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class NoInteract extends ClientModule {
   private static NoInteract INSTANCE;
   Setting blacklist = this.registerSetting(new BlockListSetting("Blacklist", "Valid block blacklist"));
   Setting border = this.registerSetting(new BooleanSetting("Border", "Prevents interacting with the world border", true));

   public NoInteract() {
      super("NoInteract", "Prevents player from interacting with certain objects", ModuleCategory.PLAYER);
      INSTANCE = this;
   }

   public static NoInteract Xz() {
      return INSTANCE;
   }

   @EventListener
   public void onInteractBlock(InteractBlockEvent interactBlockEvent) {
      BlockPos pos = interactBlockEvent.getBlockHitResult().getBlockPos();
      BlockState state = client.world.getBlockState(pos);
      if (((BlockListSetting)this.blacklist).afW(state.getBlock())) {
         interactBlockEvent.aim();
      }
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (client.player != null && client.world != null) {
         Packet packet2 = sendImmediatelyEvent.getPacket();
         if (packet2 instanceof PlayerInteractBlockC2SPacket) {
            BlockPos pos = ((PlayerInteractBlockC2SPacket)packet2).getBlockHitResult().getBlockPos();
            BlockState state = client.world.getBlockState(pos);
            if (((BlockListSetting)this.blacklist).afW(state.getBlock())) {
               sendImmediatelyEvent.aim();
            }
         }
      }
   }

   @EventListener
   public void onInteractBlock_2(InteractBlock_2Event interactBlock2Event) {
      if ((Boolean)this.border.atx() && !(client.player.getMainHandStack().getItem() instanceof BlockItem)) {
         interactBlock2Event.aim();
      }
   }
}
