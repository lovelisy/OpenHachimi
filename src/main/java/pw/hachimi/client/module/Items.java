package pw.hachimi.client.module;

import java.util.Iterator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class Items extends RenderModule {
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("HideEmpty", "", true));
   Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("Crystal", "", true));
   Setting booleanSetting3 = this.registerSetting(new BooleanSetting("XP", "", true));
   Setting booleanSetting4 = this.registerSetting(new BooleanSetting("Pearl", "", true));
   Setting booleanSetting5 = this.registerSetting(new BooleanSetting("Obsidian", "", true));
   Setting booleanSetting6 = this.registerSetting(new BooleanSetting("E-GApple", "", true));
   Setting booleanSetting7 = this.registerSetting(new BooleanSetting("GApple", "", true));
   Setting booleanSetting8 = this.registerSetting(new BooleanSetting("Totem", "", true));
   Setting booleanSetting9 = this.registerSetting(new BooleanSetting("Web", "", true));
   Setting booleanSetting10 = this.registerSetting(new BooleanSetting("Anchor", "", true));
   Setting booleanSetting11 = this.registerSetting(new BooleanSetting("Glowstone", "", true));
   Setting booleanSetting12 = this.registerSetting(new BooleanSetting("Piston", "", true));
   Setting booleanSetting13 = this.registerSetting(new BooleanSetting("RedStone", "", true));
   Setting booleanSetting14 = this.registerSetting(new BooleanSetting("EnderChest", "", true));
   Setting booleanSetting15 = this.registerSetting(new BooleanSetting("Firework", "", true));
   Setting numberSetting1_1 = this.registerSetting(new NumberSetting("Offset", "", 0, 18, 30));
   float q;
   int Y;
   int Z;
   ItemStack stack = new ItemStack(net.minecraft.item.Items.END_CRYSTAL);
   ItemStack stack2 = new ItemStack(net.minecraft.item.Items.EXPERIENCE_BOTTLE);
   ItemStack stack3 = new ItemStack(net.minecraft.item.Items.ENDER_PEARL);
   ItemStack stack4 = new ItemStack(net.minecraft.item.Items.OBSIDIAN);
   ItemStack stack5 = new ItemStack(net.minecraft.item.Items.ENCHANTED_GOLDEN_APPLE);
   ItemStack stack6 = new ItemStack(net.minecraft.item.Items.GOLDEN_APPLE);
   ItemStack stack7 = new ItemStack(net.minecraft.item.Items.TOTEM_OF_UNDYING);
   ItemStack stack8 = new ItemStack(net.minecraft.item.Items.COBWEB);
   ItemStack stack9 = new ItemStack(net.minecraft.item.Items.RESPAWN_ANCHOR);
   ItemStack stack10 = new ItemStack(net.minecraft.item.Items.GLOWSTONE);
   ItemStack stack11 = new ItemStack(net.minecraft.item.Items.PISTON);
   ItemStack stack12 = new ItemStack(net.minecraft.item.Items.REDSTONE_BLOCK);
   ItemStack stack13 = new ItemStack(net.minecraft.item.Items.ENDER_CHEST);
   ItemStack stack14 = new ItemStack(net.minecraft.item.Items.FIREWORK_ROCKET);

   public Items() {
      super("Items", "");
   }

   @EventListener
   public void anH(TickEntitiesPreEvent tickEntitiesPreEvent) {
      this.q = 0.0F;
      if ((Boolean)this.booleanSetting2_1.atx()) {
         ItemStack temp = this.stack;
         int n = this.anK(net.minecraft.item.Items.END_CRYSTAL);
         temp.setCount(n);
      }

      if ((Boolean)this.booleanSetting3.atx()) {
         ItemStack stack15 = this.stack2;
         int n2 = this.anK(net.minecraft.item.Items.EXPERIENCE_BOTTLE);
         stack15.setCount(n2);
      }

      if ((Boolean)this.booleanSetting4.atx()) {
         ItemStack stack16 = this.stack3;
         int n3 = this.anK(net.minecraft.item.Items.ENDER_PEARL);
         stack16.setCount(n3);
      }

      if ((Boolean)this.booleanSetting5.atx()) {
         ItemStack stack17 = this.stack4;
         int n4 = this.anK(net.minecraft.item.Items.OBSIDIAN);
         stack17.setCount(n4);
      }

      if ((Boolean)this.booleanSetting6.atx()) {
         ItemStack stack18 = this.stack5;
         int n5 = this.anK(net.minecraft.item.Items.ENCHANTED_GOLDEN_APPLE);
         stack18.setCount(n5);
      }

      if ((Boolean)this.booleanSetting7.atx()) {
         ItemStack stack19 = this.stack6;
         int n6 = this.anK(net.minecraft.item.Items.GOLDEN_APPLE);
         stack19.setCount(n6);
      }

      if ((Boolean)this.booleanSetting8.atx()) {
         ItemStack stack20 = this.stack7;
         int n7 = this.anK(net.minecraft.item.Items.TOTEM_OF_UNDYING);
         stack20.setCount(n7);
      }

      if ((Boolean)this.booleanSetting9.atx()) {
         ItemStack stack21 = this.stack8;
         int n8 = this.anK(net.minecraft.item.Items.COBWEB);
         stack21.setCount(n8);
      }

      if ((Boolean)this.booleanSetting10.atx()) {
         ItemStack stack22 = this.stack9;
         int n9 = this.anK(net.minecraft.item.Items.RESPAWN_ANCHOR);
         stack22.setCount(n9);
      }

      if ((Boolean)this.booleanSetting11.atx()) {
         ItemStack stack23 = this.stack10;
         int n10 = this.anK(net.minecraft.item.Items.GLOWSTONE);
         stack23.setCount(n10);
      }

      if ((Boolean)this.booleanSetting12.atx()) {
         ItemStack stack24 = this.stack11;
         int n11 = this.anJ();
         stack24.setCount(n11);
      }

      if ((Boolean)this.booleanSetting13.atx()) {
         ItemStack stack25 = this.stack12;
         int n12 = this.anK(net.minecraft.item.Items.REDSTONE_BLOCK);
         stack25.setCount(n12);
      }

      if ((Boolean)this.booleanSetting14.atx()) {
         ItemStack stack26 = this.stack13;
         int n13 = this.anK(net.minecraft.item.Items.ENDER_CHEST);
         stack26.setCount(n13);
      }

      if ((Boolean)this.booleanSetting15.atx()) {
         ItemStack stack27 = this.stack14;
         int n14 = this.anK(net.minecraft.item.Items.FIREWORK_ROCKET);
         stack27.setCount(n14);
      }

      if (this.q > 16.0F && (Integer)this.numberSetting1_1.atx() > 16) {
         this.q = this.q - ((Integer)this.numberSetting1_1.atx() - 16);
      }
   }

   @Override
   public void adb() {
   }

   @Override
   public void adc(DrawContext context) {
      this.Y = (int)(this.add() - ((Integer)this.numberSetting1_1.atx()).intValue());
      this.Z = (int)this.ade();
      if ((Boolean)this.booleanSetting2_1.atx()) {
         this.anL(this.stack, context);
      }

      if ((Boolean)this.booleanSetting3.atx()) {
         this.anL(this.stack2, context);
      }

      if ((Boolean)this.booleanSetting4.atx()) {
         this.anL(this.stack3, context);
      }

      if ((Boolean)this.booleanSetting5.atx()) {
         this.anL(this.stack4, context);
      }

      if ((Boolean)this.booleanSetting6.atx()) {
         this.anL(this.stack5, context);
      }

      if ((Boolean)this.booleanSetting7.atx()) {
         this.anL(this.stack6, context);
      }

      if ((Boolean)this.booleanSetting8.atx()) {
         this.anL(this.stack7, context);
      }

      if ((Boolean)this.booleanSetting9.atx()) {
         this.anL(this.stack8, context);
      }

      if ((Boolean)this.booleanSetting10.atx()) {
         this.anL(this.stack9, context);
      }

      if ((Boolean)this.booleanSetting11.atx()) {
         this.anL(this.stack10, context);
      }

      if ((Boolean)this.booleanSetting12.atx()) {
         this.anL(this.stack11, context);
      }

      if ((Boolean)this.booleanSetting13.atx()) {
         this.anL(this.stack12, context);
      }

      if ((Boolean)this.booleanSetting14.atx()) {
         this.anL(this.stack13, context);
      }

      if ((Boolean)this.booleanSetting15.atx()) {
         this.anL(this.stack14, context);
      }
   }

   @Override
   public float adl() {
      return this.q + super.adl();
   }

   @Override
   public float adm() {
      return 16.0F + super.adm();
   }

   public int anI(Item item2) {
      int n = 0;
      Iterator iterator2 = client.player.playerScreenHandler.slots.iterator();

      while (iterator2.hasNext()) {
         ItemStack stack15 = ((Slot)iterator2.next()).getStack();
         if (stack15.getItem() == item2) {
            n += stack15.getCount();
         }
      }

      return n;
   }

   private int anJ() {
      int n = 0;
      Iterator iterator2 = client.player.playerScreenHandler.slots.iterator();

      while (iterator2.hasNext()) {
         ItemStack stack15 = ((Slot)iterator2.next()).getStack();
         if (stack15.getItem() == net.minecraft.item.Items.PISTON || stack15.getItem() == net.minecraft.item.Items.STICKY_PISTON) {
            n += stack15.getCount();
         }
      }

      return Math.max(n, 0);
   }

   private int anK(Item item2) {
      int n = this.anI(item2);
      if (n == 0) {
         if ((Boolean)this.booleanSetting1_1.atx()) {
            return n;
         }

         this.q = this.q + ((Integer)this.numberSetting1_1.atx()).intValue();
         return 114514;
      } else {
         this.q = this.q + ((Integer)this.numberSetting1_1.atx()).intValue();
         return n;
      }
   }

   private void anL(ItemStack stack15, DrawContext context) {
      if (stack15.getCount() != 0) {
         this.Y = this.Y + (Integer)this.numberSetting1_1.atx();
         if (stack15.getCount() == 114514) {
            int n = this.Z;
            int n2 = this.Y;
            context.drawItem(stack15, n2, n);
         } else {
            int n3 = this.Z;
            int n4 = this.Y;
            context.drawItem(stack15, n4, n3);
            context.getMatrices().push();
            context.getMatrices().translate(0.0F, 0.0F, 200.0F);
            this.adr(context, String.valueOf(stack15.getCount()), this.Y + 17 - RenderUtils.acF(String.valueOf(stack15.getCount())), this.Z + 9, -1);
            context.getMatrices().pop();
         }
      }
   }
}
