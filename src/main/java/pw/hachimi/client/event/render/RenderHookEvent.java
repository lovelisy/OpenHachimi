package pw.hachimi.client.event.render;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.UnknownEvent8;

@UnidentifiedInterface2
public class RenderHookEvent extends UnknownEvent8 {
   public final DrawContext context;
   private final ItemStack stack;
   private final int c;
   private final int d;

   public RenderHookEvent(DrawContext context2, ItemStack stack2, int n, int n2) {
      this.context = context2;
      this.stack = stack2;
      this.c = n;
      this.d = n2;
   }

   public DrawContext getContext() {
      return this.context;
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public int aih() {
      return this.c;
   }

   public int aii() {
      return this.d;
   }
}
