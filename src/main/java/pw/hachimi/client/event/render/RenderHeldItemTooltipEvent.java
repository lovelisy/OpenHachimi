package pw.hachimi.client.event.render;

import net.minecraft.client.gui.DrawContext;

import net.minecraft.text.Text;
import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class RenderHeldItemTooltipEvent extends DrawContextEvent {
   private int a;
   private int c;
   private Text text;
   public static boolean bt;

   public RenderHeldItemTooltipEvent(DrawContext context, Text text2) {
      super(context);
      this.text = text2;
   }

   public boolean gB() {
      boolean flag;
      if (this.a != 0 && this.c != 0) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public int gC() {
      return this.a;
   }

   public void gD(int n) {
      this.a = n;
   }

   public int gE() {
      return this.c;
   }

   public void gF(int n) {
      this.c = n;
   }

   public Text getText() {
      return this.text;
   }

   public void setText(Text text2) {
      this.text = text2;
   }
}
