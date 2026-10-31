package pw.hachimi.client.gui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.api.UnidentifiedInterface12;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.manager.SoundManager;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.render.DrawBase;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.render.DrawHelper2;
import pw.hachimi.client.utils.render.DrawMatrixHelper;
import pw.hachimi.client.utils.render.FontLoader;
import pw.hachimi.client.utils.render.MathHelperIdentifierHelper;
import pw.hachimi.client.utils.render.RenderHelper2;
import pw.hachimi.client.utils.world.DirectionUtils;
import pw.hachimi.client.utils.world.SoundEventHelper;

public class CategoryPanel extends RenderHelper2 implements UnidentifiedInterface12 {
   private final String text;
   private final ModuleCategory moduleCategory;
   private final List list = new CopyOnWriteArrayList();
   private final UnidentifiedHelper7 unidentifiedHelper7 = new UnidentifiedHelper7(false, '\u0000', 200.0F, 496665058, '楚', Easing.CUBIC_OUT);
   private float e;
   private boolean f;
   private boolean g;
   private DrawHelper2 drawHelper2;

   public CategoryPanel(ModuleCategory moduleCategory2, float f2) {
      super(f2);
      this.moduleCategory = moduleCategory2;
      this.text = DirectionUtils.apW(moduleCategory2);
      Iterator iterator2 = Managers.moduleManager.XM().iterator();

      while (iterator2.hasNext()) {
         ModuleBase moduleBase = (ModuleBase)iterator2.next();
         if (moduleBase.getModuleCategory() == moduleCategory2) {
            List temp = this.list;
            DrawMatrixHelper drawMatrixHelper = new DrawMatrixHelper(moduleBase, this, f2, 15.0F);
            temp.add(drawMatrixHelper);
         }
      }

      this.bH(true);
   }

   public CategoryPanel(ModuleCategory moduleCategory2, float f2, float f3, long l2) {
      this(moduleCategory2, f2);
   }

   @Override
   public void aeU(DrawContext context, int n2, int n3) {
      this.vy(RenderUtils.acG() + 6.0F);
      if (this.g) {
         this.j = this.j + (DrawScreenScreen.c - this.n);
         this.k = this.k + (DrawScreenScreen.d - this.o);
      }

      this.p = 1.5F;
      if (this.drawHelper2 != null) {
         this.p = this.p + this.drawHelper2.vx();
      }

      Iterator iterator2 = this.list.iterator();

      while (true) {
         DrawMatrixHelper drawMatrixHelper;
         String str;
         String temp;
         do {
            if (!iterator2.hasNext()) {
               int n4 = UI.atK().atW(this.k);
               int n5 = UI.auf() ? -16777216 : -1;
               double d2 = 0.0;
               double d3 = 0.0;
               if (UI.aum().QC()) {
                  d2 = 1.5;
               } else if (UI.aum().Qz() || !UI.aum().Qx()) {
                  d3 = 0.5;
               }

               if (this.unidentifiedHelper7.qW() > 0.01F) {
                  float f2 = this.k;
                  if (this.drawHelper2 != null) {
                     f2 += this.drawHelper2.vx();
                  }

                  this.vF(this.j, this.k + this.m, this.j + this.l, this.k + this.m + this.p * this.unidentifiedHelper7.qW());
                  vi(context, this.j + 1.5 - d3, this.k + this.m, this.l - 3.0F + d3 * 2.0, this.p - 1.5 + d3, UI.aum().QA().getRGB());
                  if (this.drawHelper2 != null) {
                     this.drawHelper2.afb(context, this.j + 2.0F, this.k + this.m);
                  }

                  this.e = f2 + this.m;
                  if (UI.aum().Qz()) {
                     this.e += 0.5F;
                  }

                  Iterator iterator3 = this.list.iterator();

                  while (iterator3.hasNext()) {
                     DrawMatrixHelper drawMatrixHelper2 = (DrawMatrixHelper)iterator3.next();
                     if (DrawHelper2.text != null) {
                        temp = drawMatrixHelper2.getModuleBase().OO().toLowerCase();
                        String str2 = DrawHelper2.text.toLowerCase();
                        if (!temp.contains(str2)) {
                           continue;
                        }
                     }

                     drawMatrixHelper2.anQ(context, this.j + 1.5F, this.e, n2, n3);
                     this.e = this.e + drawMatrixHelper2.vx();
                     if (UI.aum().Qz()) {
                        this.e += 0.5F;
                     }
                  }

                  this.vG();
                  if (!UI.aum().QC()) {
                     vi(context, this.j + this.l - 1.5 + d3, this.k + this.m, 1.5 - d3, this.p * this.unidentifiedHelper7.qW(), UI.atP().getRGB());
                     vi(context, this.j, this.k + this.m, 1.5 - d3, this.p * this.unidentifiedHelper7.qW(), UI.atP().getRGB());
                     vi(
                        context,
                        this.j + 1.5 - d3,
                        this.k + this.m + this.p * this.unidentifiedHelper7.qW() - 1.5 + d3,
                        this.l - 3.0F + d3 * 2.0,
                        1.5 - d3,
                        UI.atP().getRGB()
                     );
                  }

                  if (UI.aum().Qv()) {
                     vi(context, this.j + this.l - d2, this.k + this.m, 0.5, (this.p - d2) * this.unidentifiedHelper7.qW(), n4);
                     vi(context, this.j - 0.5 + d2, this.k + this.m, 0.5, (this.p - d2) * this.unidentifiedHelper7.qW(), n4);
                  }
               }

               vi(context, this.j + d2, this.k, this.l - d2 * 2.0, this.m, UI.atP().getRGB());
               if (UI.aum().Qv()) {
                  vi(context, this.j + this.l - d2, this.k, 0.5, this.m + 0.5, n4);
                  vi(context, this.j - 0.5 + d2, this.k, 0.5, this.m + 0.5, n4);
                  vi(context, this.j - 0.5 + d2, this.k - 0.5, this.l + 1.0F - d2 * 2.0, 0.5, n4);
                  vi(context, this.j - 0.5 + d2, this.k + this.m + (this.p - d2) * this.unidentifiedHelper7.qW(), this.l + 1.0F - d2 * 2.0, 0.5, n4);
               }

               if (!UI.aum().Qw()) {
                  this.ve(context, vf(this.text), this.j + 5.0F, this.k + 4.0F, n5);
               } else {
                  FontLoader.fontRenderer3.aaS(context.getMatrices(), this.moduleCategory.Wk(), this.j + 4.0F, this.k + 5.0F, UI.atK().atV(n5));
                  this.ve(context, vf(this.text), this.j + this.l - RenderUtils.acF(vf(this.text)) - 5.0F, this.k + 4.0F, n5);
               }

               this.n = DrawScreenScreen.c;
               this.o = DrawScreenScreen.d;
               return;
            }

            drawMatrixHelper = (DrawMatrixHelper)iterator2.next();
            if (DrawHelper2.text == null) {
               break;
            }

            temp = drawMatrixHelper.getModuleBase().OO().toLowerCase();
            str = DrawHelper2.text.toLowerCase();
         } while (!temp.contains(str));

         this.p = this.p + drawMatrixHelper.vx();
         if (UI.aum().Qz()) {
            this.p += 0.5F;
         }

         if (!(drawMatrixHelper.anT() < 0.01F)) {
            this.p = this.p + 2.0F * drawMatrixHelper.anT();
            Iterator iterator4 = drawMatrixHelper.getList().iterator();

            while (iterator4.hasNext()) {
               DrawBase drawBase = (DrawBase)iterator4.next();
               if (drawBase.getSetting().isVisible()) {
                  this.p = this.p + drawBase.vx() * drawMatrixHelper.anT();
                  if (UI.aum().Qz()) {
                     this.p = this.p + 0.5F * drawMatrixHelper.anT();
                  }

                  if (drawBase instanceof MathHelperIdentifierHelper && ((MathHelperIdentifierHelper)drawBase).aB() > 0.01F) {
                     this.p = this.p + ((MathHelperIdentifierHelper)drawBase).ax() * ((MathHelperIdentifierHelper)drawBase).aB() * drawMatrixHelper.anT();
                  } else if (drawBase instanceof SoundEventHelper && ((SoundEventHelper)drawBase).xS() > 0.01F) {
                     this.p = this.p + ((SoundEventHelper)drawBase).xT() * ((SoundEventHelper)drawBase).xS() * drawMatrixHelper.anT();
                  }
               }
            }
         }
      }
   }

   @Override
   public void UU(int n2, int n3, int n4) {
      super.UU(n2, n3, n4);
      if (n4 == 1 && this.vC(n2, n3)) {
         this.f = !this.f;
         this.unidentifiedHelper7.qV(this.f);
         if (UI.atK().auc()) {
            Managers.soundManager.jg(SoundManager.a);
         }
      } else if (this.bG()) {
         Iterator iterator2 = this.list.iterator();

         while (iterator2.hasNext()) {
            DrawMatrixHelper drawMatrixHelper = (DrawMatrixHelper)iterator2.next();
            if (DrawHelper2.text != null) {
               String temp = drawMatrixHelper.getModuleBase().OO().toLowerCase();
               String str = DrawHelper2.text.toLowerCase();
               if (!temp.contains(str)) {
                  continue;
               }
            }

            drawMatrixHelper.UU(n2, n3, n4);
         }
      }

      if (this.drawHelper2 != null) {
         this.drawHelper2.UU(n2, n3, n4);
      }
   }

   @Override
   public void UV() {
      super.UV();
      this.g = false;
      if (this.bG()) {
         Iterator iterator2 = this.list.iterator();

         while (iterator2.hasNext()) {
            DrawMatrixHelper drawMatrixHelper = (DrawMatrixHelper)iterator2.next();
            if (DrawHelper2.text != null) {
               String temp = drawMatrixHelper.getModuleBase().OO().toLowerCase();
               String str = DrawHelper2.text.toLowerCase();
               if (!temp.contains(str)) {
                  continue;
               }
            }

            drawMatrixHelper.UV();
         }
      }

      if (this.drawHelper2 != null) {
         this.drawHelper2.UV();
      }
   }

   @Override
   public void UW(int n2) {
      super.UW(n2);
      if (this.bG()) {
         Iterator iterator2 = this.list.iterator();

         while (iterator2.hasNext()) {
            DrawMatrixHelper drawMatrixHelper = (DrawMatrixHelper)iterator2.next();
            if (DrawHelper2.text != null) {
               String temp = drawMatrixHelper.getModuleBase().OO().toLowerCase();
               String str = DrawHelper2.text.toLowerCase();
               if (!temp.contains(str)) {
                  continue;
               }
            }

            drawMatrixHelper.UW(n2);
         }
      }

      if (this.drawHelper2 != null) {
         this.drawHelper2.UW(n2);
      }
   }

   @Override
   public void UX(char c2) {
      super.UX(c2);
      if (this.bG()) {
         Iterator iterator2 = this.list.iterator();

         while (iterator2.hasNext()) {
            DrawMatrixHelper drawMatrixHelper = (DrawMatrixHelper)iterator2.next();
            if (DrawHelper2.text != null) {
               String temp = drawMatrixHelper.getModuleBase().OO().toLowerCase();
               String str = DrawHelper2.text.toLowerCase();
               if (!temp.contains(str)) {
                  continue;
               }
            }

            drawMatrixHelper.UX(c2);
         }
      }

      if (this.drawHelper2 != null) {
         this.drawHelper2.UX(c2);
      }
   }

   @Override
   public JsonObject LT(int n2, int n3, char c2) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("category", this.moduleCategory.name());
      jsonObject.addProperty("x", this.vv());
      jsonObject.addProperty("y", this.vw());
      jsonObject.addProperty("open", this.bG());
      return jsonObject;
   }

   public CategoryPanel bC(JsonObject jsonObject) {
      if (!jsonObject.has("x") || !jsonObject.has("y") || !jsonObject.has("open")) {
         return null;
      }

      JsonElement jsonElement = jsonObject.get("x");
      JsonElement jsonElement2 = jsonObject.get("y");
      JsonElement jsonElement3 = jsonObject.get("open");
      this.vE(jsonElement.getAsFloat(), jsonElement2.getAsFloat());
      this.bH(jsonElement3.getAsBoolean());
      return null;
   }

   public boolean bD(float f2, float f3) {
      return this.vD(f2, f3, this.j, this.k, this.l, this.bI());
   }

   public void bE(float f2) {
      this.e += f2;
   }

   public ModuleCategory getModuleCategory() {
      return this.moduleCategory;
   }

   public boolean bG() {
      return this.f && this.unidentifiedHelper7.qW() > 0.1F;
   }

   public void bH(boolean flag) {
      this.f = flag;
      this.unidentifiedHelper7.qV(flag);
   }

   public float bI() {
      return this.m + this.p;
   }

   public List getList() {
      return this.list;
   }

   public boolean bK() {
      return this.g;
   }

   public void bL(boolean flag) {
      this.g = flag;
   }

   public DrawHelper2 getDrawHelper2() {
      return this.drawHelper2;
   }

   @Override
   public Object LU(JsonObject jsonObject) {
      return this.bC(jsonObject);
   }
}
