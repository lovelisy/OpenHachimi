package pw.hachimi.client.utils.combat;

import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.combat.AutoAnchor;
import pw.hachimi.client.module.render.PhantomView;
import pw.hachimi.client.render.BoxRenderer;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoAnchorVec3dBoxHelper {
   public Vec3d vec;
   public Vec3d vec2;
   public double c = 0.0;

   @EventListener
   public void a(Render3DContext render3DContext) {
      AutoAnchor autoAnchor = AutoAnchor.aaj();
      if ((Boolean)autoAnchor.render.atx()) {
         BlockPos pos2 = autoAnchor.pos;
         if (pos2 != null) {
            this.vec = pos2.toCenterPos();
         }

         if (this.vec == null) {
            return;
         }

         if ((Double)autoAnchor.fadeSpeed.atx() >= 1.0) {
            double d;
            if (autoAnchor.stopwatch.afq((long)((Double)autoAnchor.startFade.atx() * 1000.0))) {
               d = 0.0;
            } else {
               d = 0.5;
            }

            this.c = d;
         } else {
            double temp = this.c;
            boolean flag = autoAnchor.stopwatch.afq((long)((Double)autoAnchor.startFade.atx() * 1000.0));
            double d2 = temp;
            double d3;
            if (flag) {
               d2 = 0.0;
               d3 = d2;
            } else {
               d2 = 0.5;
               d3 = d2;
            }

            this.c = PhantomView.DH(d3, d2, (Double)autoAnchor.fadeSpeed.atx() / 10.0);
         }

         if (this.c == 0.0) {
            this.vec2 = null;
            return;
         }

         if (this.vec2 != null && !((Double)autoAnchor.sliderSpeed.atx() >= 1.0)) {
            this.vec2 = new Vec3d(
               PhantomView.DH(this.vec2.x, this.vec.x, (Double)autoAnchor.sliderSpeed.atx() / 10.0),
               PhantomView.DH(this.vec2.y, this.vec.y, (Double)autoAnchor.sliderSpeed.atx() / 10.0),
               PhantomView.DH(this.vec2.z, this.vec.z, (Double)autoAnchor.sliderSpeed.atx() / 10.0)
            );
         } else {
            this.vec2 = this.vec;
         }

         Box box = new Box(this.vec2, this.vec2);
         if ((Boolean)autoAnchor.shrink.atx()) {
            Box box2 = box;
            double d4 = this.c;
            box = box2.expand(d4);
         } else {
            box = box.expand(0.5);
         }

         Setting setting2 = autoAnchor.side;
         BoxRenderer boxRenderer2 = render3DContext.boxRenderer;
         Color temp2 = ColorUtils.iE((Color)setting2.atx(), (int)(((Color)autoAnchor.side.atx()).getAlpha() * this.c * 2.0));
         setting2 = autoAnchor.line;
         Color color = temp2;
         boxRenderer2 = boxRenderer2;
         temp2 = color;
         Color temp3 = ColorUtils.iE((Color)setting2.atx(), (int)(((Color)autoAnchor.line.atx()).getAlpha() * this.c * 2.0));
         setting2 = autoAnchor.boxMode;
         color = temp3;
         Color color2 = temp2;
         boxRenderer2 = boxRenderer2;
         temp2 = color2;
         temp3 = color;
         ShapeMode shapeMode = (ShapeMode)setting2.atx();
         color = temp3;
         color2 = temp2;
         boxRenderer2.Za(box, color2, color, shapeMode, 0);
         if ((Boolean)autoAnchor.renderDamage.atx() && this.vec2 != null && !autoAnchor.stopwatch.afq((long)((Double)autoAnchor.startFade.atx() * 1000.0))) {
            StringBuilder stringBuilder = new StringBuilder();
            if ((Boolean)autoAnchor.renderDamage.atx()) {
               if ((Boolean)autoAnchor.targetDamage.atx()) {
                  StringBuilder stringBuilder2 = stringBuilder;
                  double d5 = UnidentifiedUtils3.rx(autoAnchor.e, 1);
                  stringBuilder2.append(d5);
               }

               if ((Boolean)autoAnchor.targetDamage.atx() && (Boolean)autoAnchor.selfDamage.atx()) {
                  stringBuilder.append("/");
               }

               if ((Boolean)autoAnchor.selfDamage.atx()) {
                  StringBuilder stringBuilder3 = stringBuilder;
                  double d6 = UnidentifiedUtils3.rx(autoAnchor.f, 1);
                  stringBuilder3.append(d6);
               }
            }

            MatrixStack matrices2 = render3DContext.matrices;
            String str = stringBuilder.toString();
            Vec3d vec3 = this.vec2;
            Color color3 = a((int)(255.0 * this.c * 2.0));
            Vec3d vec4 = vec3;
            String str2 = str;
            MatrixStack matrices3 = matrices2;
            int n = color3.getRGB();
            RenderUtils.acv(matrices3, str2, vec4, n);
         }
      }
   }

   private static Color a(int n) {
      return new Color(255, 255, 255, n);
   }
}
