package pw.hachimi.client.utils.render;

import net.minecraft.client.gui.DrawContext;
import org.apache.commons.lang3.ArrayUtils;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.manager.SoundManager;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.render.ColorBase;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;

public class DrawHelper2 extends ColorBase {
   public static String text = null;
   private final Stopwatch stopwatch;
   private char[] c;
   private boolean d;
   private boolean e;
   private boolean f;

   public DrawHelper2(RenderHelper2 renderHelper2, float f, float f2) {
      super(renderHelper2, f, f2, 107.0F);
      this.stopwatch = new StopwatchImpl();
      this.c = "".toCharArray();
   }

   @Override
   public void aeU(DrawContext context, int n, int n2) {
   }

   public void afb(DrawContext context, float f2, float f3) {
      this.vy(RenderUtils.acG() + UI.atK().atT());
      this.j = f2;
      this.k = f3;
      int n = UI.atK().atV(-1);
      String str = this.d ? new String(this.c) + this.afd() : vf("Search..");
      if (this.e && str.isEmpty()) {
         str = vf("Search..");
      }

      this.ve(context, str, f2 + 5.0F, f3 + UI.atK().atU(), n);
   }

   @Override
   public void vg(DrawContext context, int n) {
      vi(context, this.j + 1.0F, this.k, this.l - 1.0F, this.m, n);
   }

   @Override
   public void UU(int n, int n2, int n3) {
      if (this.vC(n, n2)) {
         if (n3 == 0) {
            if (!this.d || !this.e) {
               this.d = !this.d;
               if (!this.d) {
                  text = null;
                  this.c = "".toCharArray();
               }
            }

            this.e = false;
            if (UI.atK().auc()) {
               Managers.soundManager.jg(SoundManager.a);
            }
         }
      } else {
         this.e = true;
      }
   }

   @Override
   public void UV() {
   }

   @Override
   public void UW(int n) {
      if (this.d) {
         switch (n) {
            case 32:
               this.e = true;
               break;
            case 256:
            case 257:
               this.d = false;
               text = null;
               this.c = "".toCharArray();
               break;
            case 259:
               if (this.c.length != 0 && !this.e) {
                  this.c = ArrayUtils.remove(this.c, this.c.length - 1);
                  text = new String(this.c);
               }
         }
      }
   }

   @Override
   public void UX(char c2) {
      if (this.d && !this.e) {
         this.c = ArrayUtils.add(this.c, c2);
         text = new String(this.c);
      }
   }

   public boolean afc() {
      return this.d;
   }

   public String afd() {
      if (this.stopwatch.afq(250)) {
         this.f = !this.f;
         this.stopwatch.afr(29432, 5265492, (byte)40);
      }

      return this.f && this.d && !this.e ? "_" : "";
   }
}
