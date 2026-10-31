package pw.hachimi.client.event.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class RenderFirstPersonItem_2Event extends RenderFirstPersonItemEvent {
   public RenderFirstPersonItem_2Event(Hand hand, ItemStack stack, float f, MatrixStack matrices) {
      super(hand, stack, f, matrices);
   }
}
