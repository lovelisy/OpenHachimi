package pw.hachimi.client.module.misc;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.joml.Vector2d;
import pw.hachimi.client.event.render.RenderHookEvent;
import pw.hachimi.client.gui.StackComponentTypeShulkerBoxScreen;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.mixin.ContainerComponentMixin;
import pw.hachimi.client.model.ShulkerCompact;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class Tooltips extends ClientModule {
   private static final ItemStack[] a = new ItemStack[27];
   public static Tooltips INSTANCE;
   Setting shulkers = this.registerSetting(new BooleanSetting("Shulkers", "Renders all the contents of shulkers in tooltips", true));
   Setting shulkersIcon = this.registerSetting(new BooleanSetting("ShulkersIcon", "Draw the first item in the Shulker Box on the Shulker Box", true));
   Setting multi = this.registerSetting(new BooleanSetting("Multi", " ", true, this::isMultiVisible));
   Setting shulkersPeek = this.registerSetting(new BooleanSetting("ShulkersPeek", "Peek the shulkerbox when you middle click", true));
   Setting maps = this.registerSetting(new BooleanSetting("Maps", "Renders a preview of maps in tooltips", false));
   Setting shulkerScreen = this.registerSetting(new BooleanSetting("ShulkerScreen", "Renders all the contents of shulkers on screen", true));
   public Setting compact = this.registerSetting(new BooleanSetting("Compact", "Compact shulkers items", true, this::isCompactVisible));
   Setting allowRightSide = this.registerSetting(new BooleanSetting("AllowRightSide", "When the left side is full, render on the right side", true, this::isAllowRightSideVisible));
   Setting tooltips = this.registerSetting(new BooleanSetting("Tooltips", "Render item info when you hovering", true, this::isTooltipsVisible));
   private static final int k = 2;
   private static final int l = 20;
   private static final int m = 18;
   private final Vector2d vector2d = new Vector2d();
   private int o;
   private int p;
   private int q;
   private int r;
   private int s;
   private int t;
   private float u;
   private static final int v = new Color(0, 0, 0, 100).getRGB();
   private final ObjectList objectList = ObjectLists.synchronize(new ObjectArrayList());

   public Tooltips() {
      super("Tooltips", "Renders detailed tooltips showing items", ModuleCategory.MISC);
      INSTANCE = this;
   }

   public static Tooltips aez() {
      return INSTANCE;
   }

   public boolean aeA() {
      boolean flag;
      if (this.awg() && (Boolean)this.shulkerScreen.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean aeB() {
      return (Boolean)this.shulkersIcon.atx();
   }

   public boolean aeC() {
      return (Boolean)this.shulkersPeek.atx();
   }

   public void aeD(DrawContext context2, ItemStack stack, int n, int n2) {
      if (this.awg() && this.aeB()) {
         Item item2 = stack.getItem();
         if (item2 instanceof BlockItem && ((BlockItem)item2).getBlock() instanceof ShulkerBoxBlock) {
            ComponentMap componentMap = stack.getComponents();
            ComponentMap temp = componentMap;
            ComponentType componentType = DataComponentTypes.CONTAINER;
            if (temp.contains(componentType)) {
               temp = componentMap;
               ComponentType componentType2 = DataComponentTypes.CONTAINER;
               DefaultedList defaultedList = ((ContainerComponentMixin)temp.get(componentType2)).getStacks();
               ArrayList arrayList = new ArrayList();
               int n3 = 0;
               Iterator iterator2 = defaultedList.iterator();

               while (iterator2.hasNext()) {
                  ItemStack stack2 = (ItemStack)iterator2.next();
                  if (!stack2.isEmpty()) {
                     if (!(Boolean)this.multi.atx()) {
                        context2.getMatrices().push();
                        context2.getMatrices().scale(0.5F, 0.5F, 1.0F);
                        context2.getMatrices().translate(0.0F, 0.0F, 100.0F);
                        int n4 = n * 2 + 17;
                        int n5 = n2 * 2 + 15;
                        int n6 = n4;
                        context2.drawItem(stack2, n6, n5);
                        context2.getMatrices().pop();
                        return;
                     }

                     ArrayList arrayList2 = arrayList;
                     Item item3 = stack2.getItem();
                     if (!arrayList2.contains(item3)) {
                        if (++n3 == 5) {
                           return;
                        }

                        context2.getMatrices().push();
                        MatrixStack matrices = context2.getMatrices();
                        double temp2 = n;
                        int n7 = n3 % 2;
                        double d = temp2;
                        MatrixStack matrices2 = matrices;
                        double d2;
                        if (n7 == 1) {
                           MatrixStack matrices3 = matrices2;
                           d = 0.5;
                           d2 = d;
                           matrices2 = matrices3;
                        } else {
                           MatrixStack matrices4 = matrices2;
                           d = 8.5;
                           d2 = d;
                           matrices2 = matrices4;
                        }

                        MatrixStack matrices5 = matrices2;
                        temp2 = d2 + d;
                        d = n2;
                        d2 = temp2;
                        matrices2 = matrices5;
                        double d3;
                        if (n3 <= 2) {
                           MatrixStack matrices6 = matrices2;
                           d = -0.5;
                           d2 = d;
                           d3 = d2;
                           matrices2 = matrices6;
                        } else {
                           MatrixStack matrices7 = matrices2;
                           d = 7.5;
                           d2 = d;
                           d3 = d2;
                           matrices2 = matrices7;
                        }

                        MatrixStack matrices8 = matrices2;
                        double d4 = d2 + d;
                        matrices8.translate(d3, d4, 100.0);
                        context2.getMatrices().scale(0.5F, 0.5F, 1.0F);
                        context2.drawItem(stack2, 0, 0);
                        context2.getMatrices().pop();
                        ArrayList arrayList3 = arrayList;
                        Item item4 = stack2.getItem();
                        arrayList3.add(item4);
                     }
                  }
               }
            }
         }
      }
   }

   @EventListener
   public void aeE(RenderHookEvent renderHookEvent) {
      ItemStack stack = renderHookEvent.getStack();
      if (!stack.isEmpty()) {
         label40:
         if ((Boolean)this.shulkers.atx()) {
            int n;
            if (!StackComponentTypeShulkerBoxScreen.YD(stack)) {
               Item item2 = stack.getItem();
               if (!(item2 instanceof BlockItem) || !(((BlockItem)item2).getBlock() instanceof ShulkerBoxBlock)) {
                  break label40;
               }

               StackComponentTypeShulkerBoxScreen.YE(stack, a);
               renderHookEvent.aim();
               renderHookEvent.context.getMatrices().push();
               renderHookEvent.context.getMatrices().translate(0.0F, 0.0F, 600.0F);
               MatrixStack temp = renderHookEvent.context.getMatrices();
               double d = renderHookEvent.aih() + 8.0;
               MatrixStack matrices = temp;
               double d2 = renderHookEvent.aii() - 21.0;
               int n2 = ColorHelper3.auO().auY(170);
               double d3 = d2;
               RenderUtils.acz(matrices, d, d3, 150.0, 13.0, n2);
               RenderUtils.acB(renderHookEvent.aih() + 8.0, renderHookEvent.aii() - 21.0, renderHookEvent.aih() + 158.0, renderHookEvent.aii() - 8.0);
               DrawContext context2 = renderHookEvent.getContext();
               String str = stack.getName().getString();
               DrawContext context3 = context2;
               float f = renderHookEvent.aih() + 11.0F;
               float f2 = renderHookEvent.aii() - 18.0F;
               float f3 = f;
               RenderUtils.acE(context3, str, f3, f2, -1);
               RenderUtils.acC();
               temp = renderHookEvent.context.getMatrices();
               d = renderHookEvent.aih() + 8.0;
               matrices = temp;
               d3 = renderHookEvent.aii() - 8.0;
               RenderUtils.acz(matrices, d, d3, 150.0, 55.0, 1996488704);
               n = 0;
               int n3 = a.length;
               DrawContext context4 = renderHookEvent.context;
               ItemStack temp2 = a[0];
               int temp3 = renderHookEvent.aih() + 0 % 9 * 16 + 9;
               int n4 = renderHookEvent.aii() + 0 / 9 * 16 - 5;
               int n5 = temp3;
               ItemStack stack2 = temp2;
               context4.drawItem(stack2, n5, n4);
               ItemStack stack3 = a[0];
               TextRenderer textRenderer2 = client.textRenderer;
               DrawContext context5 = renderHookEvent.context;
               int n6 = renderHookEvent.aih() + 0 % 9 * 16 + 9;
               n2 = renderHookEvent.aii() + 0 / 9 * 16 - 5;
               int n7 = n6;
               context5.drawItemInSlot(textRenderer2, stack3, n7, n2);
               n++;
            } else {
               StackComponentTypeShulkerBoxScreen.YE(stack, a);
               renderHookEvent.aim();
               renderHookEvent.context.getMatrices().push();
               renderHookEvent.context.getMatrices().translate(0.0F, 0.0F, 600.0F);
               MatrixStack matrices2 = renderHookEvent.context.getMatrices();
               double d4 = renderHookEvent.aih() + 8.0;
               MatrixStack matrices3 = matrices2;
               double d5 = renderHookEvent.aii() - 21.0;
               int n8 = ColorHelper3.auO().auY(170);
               double d6 = d5;
               RenderUtils.acz(matrices3, d4, d6, 150.0, 13.0, n8);
               RenderUtils.acB(renderHookEvent.aih() + 8.0, renderHookEvent.aii() - 21.0, renderHookEvent.aih() + 158.0, renderHookEvent.aii() - 8.0);
               DrawContext context6 = renderHookEvent.getContext();
               String str2 = stack.getName().getString();
               DrawContext context7 = context6;
               float f4 = renderHookEvent.aih() + 11.0F;
               float f5 = renderHookEvent.aii() - 18.0F;
               float f6 = f4;
               RenderUtils.acE(context7, str2, f6, f5, -1);
               RenderUtils.acC();
               matrices2 = renderHookEvent.context.getMatrices();
               d4 = renderHookEvent.aih() + 8.0;
               matrices3 = matrices2;
               d6 = renderHookEvent.aii() - 8.0;
               RenderUtils.acz(matrices3, d4, d6, 150.0, 55.0, 1996488704);
               n = 0;
            }

            while (n < a.length) {
               DrawContext context8 = renderHookEvent.context;
               ItemStack stack4 = a[n];
               int n9 = renderHookEvent.aih() + n % 9 * 16 + 9;
               int n10 = renderHookEvent.aii() + n / 9 * 16 - 5;
               int n11 = n9;
               ItemStack stack5 = stack4;
               context8.drawItem(stack5, n11, n10);
               ItemStack stack6 = a[n];
               TextRenderer textRenderer3 = client.textRenderer;
               DrawContext context9 = renderHookEvent.context;
               int n12 = renderHookEvent.aih() + n % 9 * 16 + 9;
               int n13 = renderHookEvent.aii() + n / 9 * 16 - 5;
               int n14 = n12;
               context9.drawItemInSlot(textRenderer3, stack6, n14, n13);
               n++;
            }

            renderHookEvent.context.getMatrices().pop();
            return;
         }

         if ((Boolean)this.maps.atx() && stack.getItem() instanceof FilledMapItem) {
            renderHookEvent.aim();
            renderHookEvent.context.getMatrices().push();
            renderHookEvent.context.getMatrices().translate(0.0F, 0.0F, 600.0F);
            MatrixStack matrices4 = renderHookEvent.context.getMatrices();
            double d7 = renderHookEvent.aih() + 8.0;
            MatrixStack matrices5 = matrices4;
            double d8 = renderHookEvent.aii() - 21.0;
            int n15 = ColorHelper3.auO().auY(170);
            double d9 = d8;
            RenderUtils.acz(matrices5, d7, d9, 128.0, 13.0, n15);
            RenderUtils.acB(renderHookEvent.aih() + 8.0, renderHookEvent.aii() - 21.0, renderHookEvent.aih() + 132.0, renderHookEvent.aii() - 8.0);
            DrawContext context10 = renderHookEvent.getContext();
            String str3 = stack.getName().getString();
            DrawContext context11 = context10;
            float f7 = renderHookEvent.aih() + 11.0F;
            float f8 = renderHookEvent.aii() - 18.0F;
            float f9 = f7;
            RenderUtils.acE(context11, str3, f9, f8, -1);
            RenderUtils.acC();
            matrices4 = renderHookEvent.getContext().getMatrices();
            float f10 = renderHookEvent.aii() - 8.0F;
            float f11 = renderHookEvent.aih() + 8.0F;
            matrices4.translate(f11, f10, 0.0F);
            ItemStack stack7 = stack;
            ComponentType componentType = DataComponentTypes.MAP_ID;
            MapIdComponent mapIdComponent = (MapIdComponent)stack7.get(componentType);
            MapIdComponent mapIdComponent2 = mapIdComponent;
            ClientWorld world2 = client.world;
            MapState mapState = FilledMapItem.getMapState(mapIdComponent2, (World)world2);
            if (mapState != null) {
               MapRenderer mapRenderer = client.gameRenderer.getMapRenderer();
               Immediate immediate = renderHookEvent.getContext().getVertexConsumers();
               MatrixStack matrices6 = renderHookEvent.getContext().getMatrices();
               mapRenderer.draw(matrices6, (VertexConsumerProvider)immediate, mapIdComponent, mapState, true, 1);
            }

            renderHookEvent.context.getMatrices().pop();
         }
      }
   }

   public void aeF(DrawContext context2, double d, double d2) {
      this.u = 1.0F;
      this.s = 2;
      int n;
      if ((Boolean)this.allowRightSide.atx()) {
         n = 2;
      } else {
         n = 2 + this.p;
      }

      this.t = n + 9;
      context2.getMatrices().push();
      MatrixStack temp = context2.getMatrices();
      float f = this.u;
      float f2 = this.u;
      temp.scale(f2, f, 1.0F);
      ObjectListIterator objectListIterator = this.getObjectList().iterator();

      label33:
      while (objectListIterator.hasNext()) {
         ShulkerCompact shulkerCompact = (ShulkerCompact)objectListIterator.next();
         this.aeN(shulkerCompact);
         if (this.t >= context2.getScaledWindowHeight() / this.u && (Boolean)this.allowRightSide.atx()) {
            this.t = 2 + this.p + 9;

            while (true) {
               float f3 = 20.0F * this.u;
               this.s = (int)((context2.getScaledWindowWidth() - this.r * f3 - 2.0F) / this.u);
               this.aeG(context2, shulkerCompact, d, d2);
               if (!objectListIterator.hasNext()) {
                  break label33;
               }

               shulkerCompact = (ShulkerCompact)objectListIterator.next();
               this.aeN(shulkerCompact);
               if (this.t >= context2.getScaledWindowHeight() / this.u && (Boolean)this.allowRightSide.atx()) {
               }
            }
         }

         this.aeG(context2, shulkerCompact, d, d2);
      }

      context2.draw();
      context2.getMatrices().pop();
      this.o = this.t - this.p;
      this.vector2d.set(0.0);
   }

   private void aeG(DrawContext context2, ShulkerCompact shulkerCompact, double d, double d2) {
      int n = this.r * 18;
      int n2 = this.q * 20;
      int n3 = n + 2 * this.r;
      int n4 = this.s;
      int n5 = this.t;
      int n6 = 0;
      if (this.t * this.u < context2.getScaledWindowHeight()) {
         label39: {
            label38: {
               this.aeL(context2, n4, n5, n3, shulkerCompact.color());
               RenderUtils.acE(context2, shulkerCompact.shulker().getName().getString(), n4, n5 - 9, -1);
               Iterator iterator2 = shulkerCompact.stacks().iterator();
               if (iterator2.hasNext()) {
                  ItemStack stack = (ItemStack)iterator2.next();
                  if (shulkerCompact.compact() && stack.isEmpty()) {
                     break label38;
                  }

                  do {
                     int n7 = n4 + n6 % 9 * 20 + 2;
                     int n8 = n5 + n6 / 9 * 18 + 2;
                     this.aeJ(context2, stack, n7, n8);
                     if (this.aeP(d, d2, n7, n8)) {
                        this.aeK(context2, stack, d, d2);
                     }

                     n6++;
                     if (!iterator2.hasNext()) {
                        break;
                     }

                     stack = (ItemStack)iterator2.next();
                  } while (!shulkerCompact.compact() || !stack.isEmpty());
               }

               if (n6 != 0) {
                  break label39;
               }
            }

            if (shulkerCompact.compact()) {
               ItemStack temp = shulkerCompact.shulker();
               int temp2 = n4 + 2;
               int n9 = this.t + 2;
               int n10 = temp2;
               ItemStack stack2 = temp;
               context2.drawItem(stack2, n10, n9);
            }
         }

         if (this.vector2d.lengthSquared() != 0.0 && this.aeO(this.vector2d.x, this.vector2d.y, n3, n2)) {
            this.aeM(shulkerCompact);
         }
      }

      this.t += n2 + 2 + 10;
   }

   public void aeH(double d, double d2, int n) {
      if (n == 0) {
         this.vector2d.set(d, d2);
      }
   }

   public void aeI(Slot slot2, double d) {
      if (d != 0.0) {
         if (slot2 != null) {
            ItemStack temp = slot2.getStack();
            Item item2 = Items.BUNDLE;
            if (temp.isOf(item2)) {
               return;
            }
         }

         float f = Math.min(-this.o + client.getWindow().getScaledHeight() / this.u, 0.0F);
         this.p = (int)MathHelper.clamp(this.p + Math.ceil(d) * 10.0, f, 0.0);
      }
   }

   private void aeJ(DrawContext context2, ItemStack stack, int n, int n2) {
      context2.drawItem(stack, n, n2);
      if (stack.getCount() > 999) {
         Object[] objs = new Object[]{stack.getCount() / 1000.0F};
         String str = "%.1fk".formatted(objs);
         TextRenderer textRenderer2 = client.textRenderer;
         context2.drawItemInSlot(textRenderer2, stack, n, n2, str);
      } else {
         TextRenderer textRenderer3 = client.textRenderer;
         context2.drawItemInSlot(textRenderer3, stack, n, n2);
      }
   }

   private void aeK(DrawContext context2, ItemStack stack, double d, double d2) {
      if ((Boolean)this.tooltips.atx() && !stack.isEmpty()) {
         float f = 1.0F / this.u;
         context2.getMatrices().push();
         context2.getMatrices().scale(f, f, 1.0F);
         int temp = (int)d;
         int n = (int)d2;
         int n2 = temp;
         TextRenderer textRenderer2 = client.textRenderer;
         context2.drawItemTooltip(textRenderer2, stack, n2, n);
         context2.getMatrices().pop();
      }
   }

   private void aeL(DrawContext context2, int n, int n2, int n3, int n4) {
      int temp = n + n3;
      int temp2 = n2 + this.q * 18 + 4;
      int n5 = v;
      int n6 = temp2;
      int n7 = temp;
      context2.fill(n, n2, n7, n6, n5);
      temp = n2 - 11;
      int n8 = n + n3;
      int n9 = temp;
      context2.fill(n, n9, n8, n2, n4);
   }

   private void aeM(ShulkerCompact shulkerCompact) {
      int n = client.player.currentScreenHandler.syncId;
      ClientPlayerInteractionManager temp = client.interactionManager;
      int temp2 = shulkerCompact.getSlot();
      ClientPlayerEntity player2 = client.player;
      SlotActionType slotActionType = SlotActionType.PICKUP;
      int n2 = temp2;
      temp.clickSlot(n, n2, 0, slotActionType, (PlayerEntity)player2);
      this.vector2d.set(0.0);
   }

   private void aeN(ShulkerCompact shulkerCompact) {
      int n = shulkerCompact.stacks().size();
      if (shulkerCompact.compact()) {
         n = 0;
         Iterator iterator2 = shulkerCompact.stacks().iterator();

         while (iterator2.hasNext() && !((ItemStack)iterator2.next()).isEmpty()) {
            n++;
         }

         n = Math.max(1, n);
      }

      this.q = (int)Math.ceil(n / 9.0F);
      this.r = MathHelper.clamp(n, 1, 9);
   }

   private boolean aeO(double d, double d2, float f, float f2) {
      d /= this.u;
      d2 /= this.u;
      boolean flag;
      if (d >= this.s && d <= this.s + f && d2 >= this.t && d2 <= this.t + f2) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private boolean aeP(double d, double d2, int n, int n2) {
      d /= this.u;
      d2 /= this.u;
      boolean flag;
      if (d >= n && d <= n + 20 && d2 >= n2 && d2 <= n2 + 18) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public void aeQ() {
      Screen screen2 = client.currentScreen;
      if (screen2 instanceof HandledScreen) {
         this.objectList.clear();
         Iterator iterator2 = ((HandledScreen)screen2).getScreenHandler().slots.iterator();

         while (iterator2.hasNext()) {
            Slot slot2 = (Slot)iterator2.next();
            ShulkerCompact shulkerCompact = ShulkerCompact.DO(slot2.getStack(), slot2.id);
            if (shulkerCompact != null) {
               this.objectList.add(shulkerCompact);
            }
         }
      }
   }

   public ObjectList getObjectList() {
      return this.objectList;
   }

   private Boolean isTooltipsVisible() {
      return (Boolean)this.shulkerScreen.atx();
   }

   private Boolean isAllowRightSideVisible() {
      return (Boolean)this.shulkerScreen.atx();
   }

   private Boolean isCompactVisible() {
      return (Boolean)this.shulkerScreen.atx();
   }

   private Boolean isMultiVisible() {
      return (Boolean)this.shulkersIcon.atx();
   }
}
