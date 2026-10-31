package pw.hachimi.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen.Contents;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.Mc;
import pw.hachimi.client.mixin.ContainerComponentMixin;

public class StackComponentTypeShulkerBoxScreen extends ShulkerBoxScreen {
   private final Identifier id = Identifier.of("textures/gui/container/shulker_box.png");
   private final ItemStack[] stacks;
   private final ItemStack stack;

   public StackComponentTypeShulkerBoxScreen(ItemStack stack2, ItemStack[] stacks2) {
      super(
         new ShulkerBoxScreenHandler(Bootstrap.NW() ? 999 : 0, Mc.client.player.getInventory(), new SimpleInventory(stacks2)),
         Mc.client.player.getInventory(),
         stack2.getName()
      );
      this.stacks = stacks2;
      this.stack = stack2;
   }

   public static Color YB(ItemStack stack2) {
      Item item2 = stack2.getItem();
      if (item2 instanceof BlockItem) {
         Block block2 = ((BlockItem)item2).getBlock();
         if (block2 instanceof ShulkerBoxBlock) {
            DyeColor dyeColor = ((ShulkerBoxBlock)block2).getColor();
            if (dyeColor == null) {
               return Color.WHITE;
            }

            int n = dyeColor.getEntityColor();
            return new Color(n);
         }
      }

      return Color.WHITE;
   }

   public static boolean YC(ItemStack stack2, ItemStack[] stacks2) {
      if (!YD(stack2)) {
         Item item2 = stack2.getItem();
         if (!(item2 instanceof BlockItem) || !(((BlockItem)item2).getBlock() instanceof ShulkerBoxBlock)) {
            return false;
         }
      }

      YE(stack2, stacks2);
      MinecraftClient temp = Mc.client;
      StackComponentTypeShulkerBoxScreen stackComponentTypeShulkerBoxScreen = new StackComponentTypeShulkerBoxScreen(stack2, stacks2);
      temp.setScreen((Screen)stackComponentTypeShulkerBoxScreen);
      return true;
   }

   public static boolean YD(ItemStack stack2) {
      ComponentType componentType = DataComponentTypes.CONTAINER;
      ContainerComponentMixin containerComponentMixin = (ContainerComponentMixin)stack2.get(componentType);
      if (containerComponentMixin != null && !containerComponentMixin.getStacks().isEmpty()) {
         return true;
      }

      NbtComponent nbtComponent = NbtComponent.DEFAULT;
      ComponentType componentType2 = DataComponentTypes.BLOCK_ENTITY_DATA;
      NbtCompound nbtCompound = ((NbtComponent)stack2.getOrDefault(componentType2, nbtComponent)).getNbt();
      return nbtCompound != null && nbtCompound.contains("Items", 9);
   }

   public static void YE(ItemStack stack2, ItemStack[] stacks2) {
      ItemStack stack3 = ItemStack.EMPTY;
      Arrays.fill(stacks2, stack3);
      ComponentMap componentMap = stack2.getComponents();
      ComponentType componentType = DataComponentTypes.CONTAINER;
      if (componentMap.contains(componentType)) {
         ComponentType componentType2 = DataComponentTypes.CONTAINER;
         DefaultedList defaultedList = ((ContainerComponentMixin)componentMap.get(componentType2)).getStacks();
         int n = 0;
         if (0 < defaultedList.size()) {
            while (true) {
               if (n < stacks2.length) {
                  int n2 = n;
                  stacks2[n] = (ItemStack)defaultedList.get(n2);
               }

               do {
                  if (++n >= defaultedList.size()) {
                     return;
                  }
               } while (n < 0);
            }
         }
      } else {
         ComponentType componentType3 = DataComponentTypes.BLOCK_ENTITY_DATA;
         if (componentMap.contains(componentType3)) {
            ComponentType componentType4 = DataComponentTypes.BLOCK_ENTITY_DATA;
            NbtComponent nbtComponent = (NbtComponent)componentMap.get(componentType4);
            if (nbtComponent.contains("Items")) {
               NbtList nbtList = (NbtList)nbtComponent.getNbt().get("Items");

               for (int n3 = 0; n3 < nbtList.size(); n3++) {
                  int n4 = n3;
                  byte b = nbtList.getCompound(n4).getByte("Slot");
                  if (b >= 0 && b < stacks2.length) {
                     DynamicRegistryManager temp = Mc.client.player.getRegistryManager();
                     int n5 = n3;
                     NbtCompound nbtCompound = nbtList.getCompound(n5);
                     stacks2[b] = ItemStack.fromNbtOrEmpty((WrapperLookup)temp, nbtCompound);
                  }
               }
            }
         }
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 2 && this.focusedSlot != null && !this.focusedSlot.getStack().isEmpty() && Mc.client.player.currentScreenHandler.getCursorStack().isEmpty()
         )
       {
         ItemStack stack2 = this.focusedSlot.getStack();
         if (YD(stack2)) {
            return YC(this.focusedSlot.getStack(), this.stacks);
         }

         Item item2 = stack2.getItem();
         if (item2 instanceof BlockItem && ((BlockItem)item2).getBlock() instanceof ShulkerBoxBlock) {
            return YC(this.focusedSlot.getStack(), this.stacks);
         }

         ComponentType componentType = DataComponentTypes.WRITTEN_BOOK_CONTENT;
         if (stack2.get(componentType) == null) {
            ComponentType componentType2 = DataComponentTypes.WRITABLE_BOOK_CONTENT;
            if (stack2.get(componentType2) == null) {
               return true;
            }
         }

         this.close();
         MinecraftClient temp = Mc.client;
         BookScreen bookScreen = new BookScreen(Contents.create(stack2));
         temp.setScreen((Screen)bookScreen);
         return true;
      } else {
         return true;
      }
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      return false;
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode != 256 && !Mc.client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
         return false;
      }

      this.close();
      return true;
   }

   public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 256) {
         this.close();
         return true;
      } else {
         return false;
      }
   }

   protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
      Color color = YB(this.stack);
      RenderSystem.setShader(GameRenderer::getPositionTexProgram);
      RenderSystem.setShaderColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      int n = (this.width - this.backgroundWidth) / 2;
      int n2 = (this.height - this.backgroundHeight) / 2;
      int n3 = this.backgroundHeight;
      int n4 = this.backgroundWidth;
      Identifier id2 = this.id;
      context.drawTexture(id2, n, n2, 0, 0, n4, n3);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
