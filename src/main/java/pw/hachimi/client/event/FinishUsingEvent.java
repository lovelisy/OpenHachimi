package pw.hachimi.client.event;

import net.minecraft.item.ItemStack;

public class FinishUsingEvent extends Event {
   private final ItemStack stack;

   public FinishUsingEvent(ItemStack stack2) {
      this.stack = stack2;
   }

   public ItemStack getStack() {
      return this.stack;
   }
}
