package pw.hachimi.client.module;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.client.utils.player.StackPlayerUtils2;

public class DrawStackRenderModule extends RenderModule {
   private static final long a = 0L;
   private static final String b = "Totem";

   public DrawStackRenderModule() {
      super(b, "");
   }

   @Override
   public void adb() {
   }

   @Override
   public void adc(DrawContext context) {
      int n = (int)this.add();
      int n2 = (int)this.ade();
      ItemStack stack = new ItemStack(net.minecraft.item.Items.TOTEM_OF_UNDYING);
      context.drawItem(stack, n, n2);
      String str = String.valueOf(StackPlayerUtils2.abK(net.minecraft.item.Items.TOTEM_OF_UNDYING));
      context.getMatrices().translate(0.0F, 0.0F, 200.0F);
      this.adr(context, str, n + 17 - RenderUtils.acF(str), n2 + 9, -1);
   }

   @Override
   public float adl() {
      return 16.0F + super.adl();
   }

   @Override
   public float adm() {
      return 16.0F + super.adm();
   }
}
