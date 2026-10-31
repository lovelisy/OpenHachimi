package pw.hachimi.client.utils.render;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.block.Block;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.StringHelper;
import pw.hachimi.client.Mc;
import pw.hachimi.client.model.player.OffsetItem;
import pw.hachimi.client.render.FontRenderer;

public class DrawBufferHelper extends BufferVertexHelper {
   private final ArrayList arrayList = new ArrayList();
   private final ArrayList arrayList2 = new ArrayList();
   private List list;
   private List list2;
   private boolean u = true;
   private boolean v = false;
   private String text = "Search";

   public DrawBufferHelper(List list3, boolean flag) {
      this(Mc.client.getWindow().getScaledWidth() / 2.0F - 100.0F, Mc.client.getWindow().getScaledHeight() / 2.0F - 150.0F, list3, flag);
   }

   public DrawBufferHelper(float f, float f2, List list3, boolean flag) {
      super(f, f2);
      if (flag) {
         this.list2 = list3;
      } else {
         this.list = list3;
      }

      this.ahD();
      int n = 0;

      for (Iterator iterator2 = Registries.BLOCK.iterator(); iterator2.hasNext(); n++) {
         Block block2 = (Block)iterator2.next();
         ArrayList temp = this.arrayList2;
         OffsetItem offsetItem = new OffsetItem(n, n * 20, block2.asItem(), block2.getTranslationKey());
         temp.add(offsetItem);
      }

      if (this.list2 == null) {
         for (Iterator iterator3 = Registries.ITEM.iterator(); iterator3.hasNext(); n++) {
            Item item2 = (Item)iterator3.next();
            ArrayList arrayList3 = this.arrayList2;
            OffsetItem offsetItem2 = new OffsetItem(n, n * 20, item2, item2.getTranslationKey());
            arrayList3.add(offsetItem2);
         }
      }
   }

   public static String ahC(String str) {
      String str2 = "";
      if (str != null && !str.isEmpty()) {
         int n = str.length() - 1;
         str2 = str.substring(0, n);
      }

      return str2;
   }

   @Override
   public void XV(DrawContext context, int n, int n2) {
      super.XV(context, n, n2);
      boolean flag = XN(n, n2, this.Yb() + this.Yf() - 90.0F, this.Yd() + 3.0F, 70.0, 10.0);
      MatrixStack temp = context.getMatrices();
      float temp2 = this.Yb() + this.Yf() - 90.0F;
      float temp3 = this.Yd() + 3.0F;
      Color color2 = flag ? new Color(-981236861, true) : new Color(-984131753, true);
      XS(temp, temp2, temp3, 70.0F, 10.0F, color2);
      FontLoader.fontRenderer.aaS(context.getMatrices(), this.text, this.Yb() + this.Yf() - 86.0F, this.Yd() + 5.0F, (new Color(14013909)).getRGB());
      RenderSystem.setShader(GameRenderer::getPositionColorProgram);
      int n3 = this.u ? (new Color(14013909)).getRGB() : Color.GRAY.getRGB();
      int n4 = this.u ? Color.GRAY.getRGB() : (new Color(12434877)).getRGB();
      Tessellator tessellator = Tessellator.getInstance();
      VertexFormat vertexFormat = VertexFormats.POSITION_COLOR;
      DrawMode drawMode = DrawMode.DEBUG_LINE_STRIP;
      BufferBuilder bufferBuilder = tessellator.begin(drawMode, vertexFormat);
      temp2 = this.Yb() + 1.5F;
      float f = this.Yd() + 29.0F;
      float f2 = temp2;
      VertexConsumer vertexConsumer = bufferBuilder.vertex(f2, f, 0.0F);
      int n5 = Color.DARK_GRAY.getRGB();
      vertexConsumer.color(n5);
      temp2 = this.Yb() + 8.0F;
      float f3 = this.Yd() + 29.0F;
      float f4 = temp2;
      bufferBuilder.vertex(f4, f3, 0.0F).color(n3);
      temp2 = this.Yb() + 8.0F;
      float f5 = this.Yd() + 19.0F;
      float f6 = temp2;
      bufferBuilder.vertex(f6, f5, 0.0F).color(n3);
      temp2 = this.Yb() + 48.0F;
      float f7 = this.Yd() + 19.0F;
      float f8 = temp2;
      bufferBuilder.vertex(f8, f7, 0.0F).color(n3);
      temp2 = this.Yb() + 54.0F;
      float f9 = this.Yd() + 29.0F;
      float f10 = temp2;
      bufferBuilder.vertex(f10, f9, 0.0F).color(n3);
      temp2 = this.Yb() + 52.0F;
      float f11 = this.Yd() + 25.0F;
      float f12 = temp2;
      bufferBuilder.vertex(f12, f11, 0.0F).color(n4);
      temp2 = this.Yb() + 52.0F;
      float f13 = this.Yd() + 19.0F;
      float f14 = temp2;
      bufferBuilder.vertex(f14, f13, 0.0F).color(n4);
      temp2 = this.Yb() + 92.0F;
      float f15 = this.Yd() + 19.0F;
      float f16 = temp2;
      bufferBuilder.vertex(f16, f15, 0.0F).color(n4);
      temp2 = this.Yb() + 100.0F;
      float f17 = this.Yd() + 29.0F;
      float f18 = temp2;
      VertexConsumer vertexConsumer2 = bufferBuilder.vertex(f18, f17, 0.0F);
      int n6 = Color.GRAY.getRGB();
      vertexConsumer2.color(n6);
      temp2 = this.Yb() + this.Yf() - 1.0F;
      float f19 = this.Yd() + 29.0F;
      float f20 = temp2;
      VertexConsumer vertexConsumer3 = bufferBuilder.vertex(f20, f19, 0.0F);
      int n7 = Color.DARK_GRAY.getRGB();
      vertexConsumer3.color(n7);
      BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
      FontLoader.fontRenderer.aaS(context.getMatrices(), "All", this.Yb() + 25.0F, this.Yd() + 22.0F, n3);
      FontLoader.fontRenderer.aaS(context.getMatrices(), "Selected", this.Yb() + 58.0F, this.Yd() + 22.0F, n4);
      if (!this.u && this.arrayList.isEmpty()) {
         FontLoader.fontRenderer
            .aaS(
               context.getMatrices(),
               "It's empty here yet",
               this.Yb() + this.Yf() / 2.0F - FontLoader.fontRenderer.abc("It's empty here yet") / 2.0F,
               this.Yd() + this.Yh() / 2.0F,
               (new Color(12434877)).getRGB()
            );
      }

      int n8 = (int)this.Yb();
      int n9 = (int)(this.Yd() + 30.0F);
      int temp4 = (int)(this.Yb() + this.Yf());
      int n10 = (int)(this.Yd() + this.Yh() - 1.0F);
      int n11 = temp4;
      int n12 = n9;
      int n13 = n8;
      context.enableScissor(n13, n12, n11, n10);
      Iterator iterator2 = (this.u ? this.arrayList2 : this.arrayList).iterator();

      while (iterator2.hasNext()) {
         OffsetItem offsetItem = (OffsetItem)iterator2.next();
         if (!(offsetItem.offset() + this.Yd() + 25.0F + this.Yj() > this.Yd() + this.Yh()) && !(offsetItem.offset() + this.Yj() + this.Yd() + 10.0F < this.Yd())) {
            context.getMatrices().push();
            temp = context.getMatrices();
            temp2 = this.Yb() + 6.0F;
            float f21 = offsetItem.offset() + this.Yd() + 32.0F + this.Yj();
            float f22 = temp2;
            temp.translate(f22, f21, 0.0F);
            ItemStack stack = offsetItem.getItem().getDefaultStack();
            context.drawItem(stack, 0, 0);
            context.getMatrices().pop();
            FontRenderer fontRenderer2 = FontLoader.fontRenderer;
            MatrixStack matrices = context.getMatrices();
            String str = offsetItem.getKey();
            Object[] objs = new Object[0];
            fontRenderer2.aaS(matrices, I18n.translate(str, objs), this.Yb() + 26.0F, offsetItem.offset() + this.Yd() + 38.0F + this.Yj(), (new Color(12434877)).getRGB());
            boolean flag2 = XN(n, n2, this.Yb() + this.Yf() - 20.0F, offsetItem.offset() + this.Yd() + 35.0F + this.Yj(), 11.0, 11.0);
            temp = context.getMatrices();
            temp2 = this.Yb() + this.Yf() - 20.0F;
            temp3 = offsetItem.offset() + this.Yd() + 35.0F + this.Yj();
            color2 = flag2 ? new Color(-981828998, true) : new Color(-984131753, true);
            XS(temp, temp2, temp3, 11.0F, 11.0F, color2);
            Stream stream2 = this.arrayList.stream();
            final OffsetItem offsetItem2 = offsetItem;
            Predicate predicate = new Predicate() {
               @Override
               public boolean test(Object context) {
                  return DrawBufferHelper.ahG(offsetItem2, (OffsetItem)context);
               }
            };
            boolean flag3 = stream2.anyMatch(predicate);
            if (this.u && !flag3) {
               TextRenderer textRenderer2 = Mc.client.textRenderer;
               int n14 = (int)(this.Yb() + this.Yf() - 17.0F);
               int n15 = (int)(offsetItem.offset() + this.Yd() + 37.0F + this.Yj());
               int n16 = n14;
               TextRenderer textRenderer3 = textRenderer2;
               context.drawText(textRenderer3, "+", n16, n15, -1, true);
            } else {
               TextRenderer textRenderer4 = Mc.client.textRenderer;
               int n17 = (int)(this.Yb() + this.Yf() - 16.5);
               int n18 = (int)(offsetItem.offset() + this.Yd() + 37.5 + this.Yj());
               int n19 = n17;
               TextRenderer textRenderer5 = textRenderer4;
               context.drawText(textRenderer5, "-", n19, n18, -1, true);
            }
         }
      }

      this.Yl((this.u ? this.arrayList2 : this.arrayList).size() * 20);
      context.disableScissor();
   }

   @Override
   public void XW(double d, double d2) {
      super.XW(d, d2);
      if (XN(d, d2, this.Yb() + 8.0F, this.Yd() + 19.0F, 52.0, 19.0)) {
         this.u = true;
         this.Yk();
      }

      if (XN(d, d2, this.Yb() + 54.0F, this.Yd() + 19.0F, 70.0, 19.0)) {
         this.u = false;
         this.Yk();
      }

      if (XN(d, d2, this.Yb() + this.Yf() - 90.0F, this.Yd() + 3.0F, 70.0, 10.0)) {
         this.v = true;
         this.text = "";
      }

      Iterator iterator2 = Lists.newArrayList(this.u ? this.arrayList2 : this.arrayList).iterator();

      while (iterator2.hasNext()) {
         OffsetItem offsetItem = (OffsetItem)iterator2.next();
         if (!((int)(offsetItem.offset() + this.Yd() + 50.0F) + this.Yj() > this.Yd() + this.Yh())
            && XN(d, d2, this.Yb() + this.Yf() - 20.0F, offsetItem.offset() + this.Yd() + 35.0F + this.Yj(), 10.0, 10.0)) {
            Stream temp = this.arrayList.stream();
            final OffsetItem offsetItem2 = offsetItem;
            Predicate predicate = new Predicate() {
               @Override
               public boolean test(Object d) {
                  return DrawBufferHelper.ahF(offsetItem2, (OffsetItem)d);
               }
            };
            boolean flag = temp.anyMatch(predicate);
            if (this.u && !flag) {
               if (this.list2 != null) {
                  Item item2 = offsetItem.getItem();
                  if (item2 instanceof BlockItem) {
                     List list3 = this.list2;
                     Block block2 = ((BlockItem)item2).getBlock();
                     if (!list3.contains(block2)) {
                        List list4 = this.list2;
                        Block block3 = ((BlockItem)item2).getBlock();
                        list4.add(block3);
                        this.ahD();
                     }
                  }
               } else {
                  List list5 = this.list;
                  Item item3 = offsetItem.getItem();
                  if (!list5.contains(item3)) {
                     List list6 = this.list;
                     Item item4 = offsetItem.getItem();
                     list6.add(item4);
                     this.ahD();
                  }
               }
            } else if (this.list2 != null) {
               Item item5 = offsetItem.getItem();
               if (item5 instanceof BlockItem) {
                  List list7 = this.list2;
                  Block block4 = ((BlockItem)item5).getBlock();
                  list7.remove(block4);
                  this.ahD();
               }
            } else {
               List list8 = this.list;
               Item item6 = offsetItem.getItem();
               list8.remove(item6);
               this.ahD();
            }
         }
      }
   }

   @Override
   public void XX(int n) {
      if (n != 70 || !InputUtil.isKeyPressed(Mc.client.getWindow().getHandle(), 341) && !InputUtil.isKeyPressed(Mc.client.getWindow().getHandle(), 345)) {
         if (this.v) {
            switch (n) {
               case 32:
                  this.text = this.text + " ";
                  break;
               case 256:
                  this.v = false;
                  this.text = "Search";
                  this.ahE();
                  break;
               case 259:
                  this.text = ahC(this.text);
                  this.ahE();
                  if (Objects.equals(this.text, "")) {
                     this.v = false;
                     this.text = "Search";
                  }
            }
         }
      } else {
         this.v = !this.v;
      }
   }

   @Override
   public void XY(char c) {
      if (StringHelper.isValidChar(c) && this.v) {
         this.text = this.text + c;
         this.ahE();
      }
   }

   private void ahD() {
      this.arrayList.clear();
      int n = 0;
      if (this.list2 == null) {
         Iterator iterator2 = Registries.ITEM.iterator();

         while (iterator2.hasNext()) {
            Item item2 = (Item)iterator2.next();
            if (this.list.contains(item2)) {
               ArrayList temp = this.arrayList;
               OffsetItem offsetItem = new OffsetItem(n, n * 20, item2.asItem(), item2.getTranslationKey());
               temp.add(offsetItem);
               n++;
            }
         }
      } else {
         Iterator iterator3 = Registries.BLOCK.iterator();

         while (iterator3.hasNext()) {
            Block block2 = (Block)iterator3.next();
            if (this.list2.contains(block2)) {
               ArrayList arrayList3 = this.arrayList;
               OffsetItem offsetItem2 = new OffsetItem(n, n * 20, block2.asItem(), block2.getTranslationKey());
               arrayList3.add(offsetItem2);
               n++;
            }
         }
      }
   }

   private void ahE() {
      this.arrayList2.clear();
      this.Yk();
      int n = 0;
      if (this.list2 == null) {
         Iterator iterator2 = Registries.ITEM.iterator();

         while (iterator2.hasNext()) {
            Item item2 = (Item)iterator2.next();
            if (!this.text.equals("Search") && !this.text.isEmpty()) {
               String temp = item2.getTranslationKey();
               String str = this.text;
               if (!temp.contains(str)) {
                  temp = item2.getName().getString().toLowerCase();
                  String str2 = this.text.toLowerCase();
                  if (!temp.contains(str2)) {
                     continue;
                  }
               }
            }

            ArrayList arrayList3 = this.arrayList2;
            OffsetItem offsetItem = new OffsetItem(n, n * 20, item2, item2.getTranslationKey());
            arrayList3.add(offsetItem);
            n++;
         }
      } else {
         Iterator iterator3 = Registries.BLOCK.iterator();

         while (iterator3.hasNext()) {
            Block block2 = (Block)iterator3.next();
            if (!this.text.equals("Search") && !this.text.isEmpty()) {
               String str3 = block2.getTranslationKey();
               String str4 = this.text;
               if (!str3.contains(str4)) {
                  str3 = block2.getName().getString().toLowerCase();
                  String str5 = this.text.toLowerCase();
                  if (!str3.contains(str5)) {
                     continue;
                  }
               }
            }

            ArrayList arrayList4 = this.arrayList2;
            OffsetItem offsetItem2 = new OffsetItem(n, n * 20, block2.asItem(), block2.getTranslationKey());
            arrayList4.add(offsetItem2);
            n++;
         }
      }
   }

   static boolean ahF(OffsetItem offsetItem, OffsetItem offsetItem2) {
      return Objects.equals(offsetItem2.getKey(), offsetItem.getKey());
   }

   static boolean ahG(OffsetItem offsetItem, OffsetItem offsetItem2) {
      return Objects.equals(offsetItem2.getKey(), offsetItem.getKey());
   }
}
