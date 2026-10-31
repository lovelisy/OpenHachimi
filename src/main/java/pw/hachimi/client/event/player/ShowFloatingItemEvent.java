package pw.hachimi.client.event.player;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class ShowFloatingItemEvent extends Event {
   private final ItemStack stack;

   public ShowFloatingItemEvent(ItemStack stack2) {
      this.stack = stack2;
   }

   public Item afu() {
      return this.stack.getItem();
   }

   public ItemStack getStack() {
      return this.stack;
   }
}
