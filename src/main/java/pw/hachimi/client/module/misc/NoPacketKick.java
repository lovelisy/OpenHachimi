package pw.hachimi.client.module.misc;

import pw.hachimi.client.event.network.DecodeEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.eventbus.annotation.EventListener;

public class NoPacketKick extends ClientModule {
   public static boolean ca;

   public NoPacketKick() {
      super("NoPacketKick", "Prevents getting kicked by packets", ModuleCategory.MISC);
   }

   @EventListener
   public void onDecode(DecodeEvent decodeEvent) {
      decodeEvent.aim();
   }
}
