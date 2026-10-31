package pw.hachimi.client.event.player;

import net.minecraft.item.ItemStack;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class UseEvent extends Event {
   private ItemStack stack;
   public static boolean en;

   public void setStack(ItemStack stack2) {
      this.stack = stack2;
   }

   public ItemStack getStack() {
      return this.stack;
   }
}
