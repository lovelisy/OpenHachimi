package pw.hachimi.client.event.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import pw.hachimi.client.event.Event;

public class RenderFirstPersonItemEvent extends Event {
   public final Hand hand;
   public final ItemStack stack;
   public final float c;
   public final MatrixStack matrices;

   public RenderFirstPersonItemEvent(Hand hand2, ItemStack stack2, float f, MatrixStack matrices2) {
      this.hand = hand2;
      this.stack = stack2;
      this.c = f;
      this.matrices = matrices2;
   }
}
