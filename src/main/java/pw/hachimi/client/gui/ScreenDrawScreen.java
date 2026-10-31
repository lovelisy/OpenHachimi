package pw.hachimi.client.gui;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.utils.render.BufferVertexHelper;

public class ScreenDrawScreen extends Screen {
   public static BufferVertexHelper bufferVertexHelper;
   public static BufferVertexHelper bufferVertexHelper2;
   private final List list;
   private static final String b = "CustomWindows";

   public ScreenDrawScreen(BufferVertexHelper... value) {
      super(Text.of("CustomWindows"));
      bufferVertexHelper = null;
      this.list = Arrays.stream(value).toList();
   }

   public static boolean RQ(double d, double d2, double d3, double d4, double d5, double d6) {
      return d >= d3 && d - d5 <= d3 && d2 >= d4 && d2 - d6 <= d4;
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (this.client.player == null) {
         this.renderBackground(context, mouseX, mouseY, delta);
      }

      Stream temp = this.list.stream();
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return ((BufferVertexHelper)obj).Ym();
         }
      };
      temp = temp.filter(predicate);
      final int n = mouseY;
      final int n2 = mouseX;
      final DrawContext context2 = context;
      Consumer consumer = new Consumer() {
         @Override
         public void accept(Object obj) {
            ScreenDrawScreen.RW(context2, n2, n, (BufferVertexHelper)obj);
         }
      };
      temp.forEach(consumer);
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      List temp = this.list;
      Consumer consumer = new Consumer() {
         @Override
         public void accept(Object obj) {
            ScreenDrawScreen.RV((BufferVertexHelper)obj);
         }
      };
      temp.forEach(consumer);
      return super.mouseReleased(mouseX, mouseY, button);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      Stream temp = this.list.stream();
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return ((BufferVertexHelper)obj).Ym();
         }
      };
      temp = temp.filter(predicate);
      final double d = mouseY;
      final double d2 = mouseX;
      Consumer consumer = new Consumer() {
         @Override
         public void accept(Object obj) {
            ScreenDrawScreen.RU(d2, d, (BufferVertexHelper)obj);
         }
      };
      temp.forEach(consumer);
      int n = this.client.getWindow().getScaledWidth() / 2;
      float f = this.list.size() * 20.0F / -2.0F - 23.0F;
      if (RQ(mouseX, mouseY, n + f + 1.0F, this.client.getWindow().getScaledHeight() - 23, 15.0, 15.0)) {
         UI.atK().awb();
      }

      f += 23.0F;

      for (Iterator iterator2 = this.list.iterator(); iterator2.hasNext(); f += 20.0F) {
         BufferVertexHelper bufferVertexHelper3 = (BufferVertexHelper)iterator2.next();
         if (RQ(mouseX, mouseY, n + f, this.client.getWindow().getScaledHeight() - 24, 17.0, 17.0)) {
            bufferVertexHelper3.Yn(!bufferVertexHelper3.Ym());
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      Stream temp = this.list.stream();
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return ((BufferVertexHelper)obj).Ym();
         }
      };
      temp = temp.filter(predicate);
      final int n = keyCode;
      Consumer consumer = new Consumer() {
         @Override
         public void accept(Object obj) {
            ScreenDrawScreen.RT(n, (BufferVertexHelper)obj);
         }
      };
      temp.forEach(consumer);
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   public boolean charTyped(char chr, int modifiers) {
      Stream temp = this.list.stream();
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return ((BufferVertexHelper)obj).Ym();
         }
      };
      temp = temp.filter(predicate);
      final char c = chr;
      Consumer consumer = new Consumer() {
         @Override
         public void accept(Object obj) {
            ScreenDrawScreen.RS(c, (BufferVertexHelper)obj);
         }
      };
      temp.forEach(consumer);
      return super.charTyped(chr, modifiers);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      Stream temp = this.list.stream();
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return ((BufferVertexHelper)obj).Ym();
         }
      };
      temp = temp.filter(predicate);
      final double d = verticalAmount;
      Consumer consumer = new Consumer() {
         @Override
         public void accept(Object obj) {
            ScreenDrawScreen.RR(d, (BufferVertexHelper)obj);
         }
      };
      temp.forEach(consumer);
      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   static void RR(double d, BufferVertexHelper bufferVertexHelper3) {
      bufferVertexHelper3.XZ((int)(d * 5.0));
   }

   static void RS(char c, BufferVertexHelper bufferVertexHelper3) {
      bufferVertexHelper3.XY(c);
   }

   static void RT(int n, BufferVertexHelper bufferVertexHelper3) {
      bufferVertexHelper3.XX(n);
   }

   static void RU(double d, double d2, BufferVertexHelper bufferVertexHelper3) {
      bufferVertexHelper3.XW(d, d2);
   }

   static void RV(BufferVertexHelper bufferVertexHelper3) {
      bufferVertexHelper3.Ya();
   }

   static void RW(DrawContext context2, int n, int n2, BufferVertexHelper bufferVertexHelper3) {
      if (bufferVertexHelper3 != bufferVertexHelper) {
         bufferVertexHelper3.XV(context2, n, n2);
      }
   }
}
