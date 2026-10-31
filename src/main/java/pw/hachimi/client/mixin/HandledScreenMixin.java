package pw.hachimi.client.mixin;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen.Contents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.MouseDraggedEvent;
import pw.hachimi.client.event.render.RenderHookEvent;
import pw.hachimi.client.gui.StackComponentTypeShulkerBoxScreen;
import pw.hachimi.client.module.misc.Tooltips;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {
   @Unique
   private static final ItemStack[] h = new ItemStack[27];
   @Shadow
   @Nullable
   protected Slot focusedSlot;
   @Shadow
   private boolean doubleClicking;

   protected HandledScreenMixin(Text text2) {
      super(text2);
   }

   @Shadow
   @Nullable
   protected abstract Slot getSlotAt(double var1, double var3);

   @Shadow
   protected abstract void onMouseClick(Slot var1, int var2, int var3, SlotActionType var4);

   @Inject(method = "mouseClicked(DDI)Z", at = @At("HEAD"), cancellable = true)
   private void mouseClicked(double d, double d2, int n, CallbackInfoReturnable callbackInfoReturnable) {
      if (Tooltips.aez().awg()
         && Tooltips.aez().aeC()
         && n == 2
         && this.focusedSlot != null
         && !this.focusedSlot.getStack().isEmpty()
         && this.client.player.currentScreenHandler.getCursorStack().isEmpty()) {
         ItemStack stack = this.focusedSlot.getStack();
         if (!StackComponentTypeShulkerBoxScreen.YD(stack)) {
            Item item2 = stack.getItem();
            if (!(item2 instanceof BlockItem) || !(((BlockItem)item2).getBlock() instanceof ShulkerBoxBlock)) {
               if (stack.get(DataComponentTypes.WRITTEN_BOOK_CONTENT) != null || stack.get(DataComponentTypes.WRITABLE_BOOK_CONTENT) != null) {
                  this.close();
                  Mc.client.setScreen(new BookScreen(Contents.create(stack)));
                  callbackInfoReturnable.setReturnValue(true);
               }

               return;
            }
         }

         callbackInfoReturnable.setReturnValue(StackComponentTypeShulkerBoxScreen.YC(this.focusedSlot.getStack(), h));
      }
   }

   @Inject(method = "mouseDragged(DDIDD)Z", at = @At("TAIL"))
   private void hookMouseDragged(double d, double d2, int n, double d3, double d4, CallbackInfoReturnable callbackInfoReturnable) {
      MouseDraggedEvent mouseDraggedEvent = new MouseDraggedEvent();
      EventBusImpl.INSTANCE.rC(mouseDraggedEvent);
      if (n == 0 && !this.doubleClicking && mouseDraggedEvent.aik()) {
         Slot slot2 = this.getSlotAt(d, d2);
         if (slot2 != null && slot2.hasStack() && hasShiftDown()) {
            this.onMouseClick(slot2, slot2.id, n, SlotActionType.QUICK_MOVE);
         }
      }
   }

   @Inject(method = "tick()V", at = @At("HEAD"))
   private void tick(CallbackInfo callbackInfo) {
      if (Tooltips.aez().aeA()) {
         Tooltips.aez().aeQ();
      }
   }

   @Inject(method = "drawMouseoverTooltip(Lnet/minecraft/client/gui/DrawContext;II)V", at = @At("HEAD"), cancellable = true)
   private void renderHook(DrawContext context, int n, int n2, CallbackInfo callbackInfo) {
      if (Tooltips.aez().aeA()) {
         Tooltips.aez().aeF(context, n, n2);
      }

      if (this.focusedSlot != null) {
         RenderHookEvent renderHookEvent = new RenderHookEvent(context, this.focusedSlot.getStack(), n, n2);
         EventBusImpl.INSTANCE.rC(renderHookEvent);
         if (renderHookEvent.aik()) {
            callbackInfo.cancel();
         }
      }
   }

   @Inject(method = "mouseClicked(DDI)Z", at = @At("HEAD"))
   private void mouseClickedHook(double d, double d2, int n, CallbackInfoReturnable callbackInfoReturnable) {
      if (Tooltips.aez().aeA()) {
         Tooltips.aez().aeH(d, d2, n);
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (Tooltips.aez().aeA()) {
         Tooltips.aez().aeI(this.focusedSlot, verticalAmount);
      }

      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }
}
