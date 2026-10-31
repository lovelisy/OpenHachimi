package pw.hachimi.client.combat;

import java.awt.Color;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.model.render.SliderFadeMode;
import pw.hachimi.client.module.combat.AutoCrystal;
import pw.hachimi.client.module.render.PhantomView;
import pw.hachimi.client.render.BoxRenderer;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.combat.UnidentifiedUtils3;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoCrystalVec3dBlockPosManager {
   public Vec3d vec;
   public Vec3d vec2;
   public double c = 0.0;

   @EventListener
   public void arq(Render3DContext render3DContext) {
      AutoCrystal autoCrystal = AutoCrystal.sc();
      if (((SliderFadeMode)autoCrystal.render.atx()).VG()) {
         if (((SliderFadeMode)autoCrystal.render.atx()).VI()) {
            Iterator iterator2 = autoCrystal.map.entrySet().iterator();

            label124:
            while (true) {
               if (!iterator2.hasNext()) {
                  if ((Boolean)autoCrystal.renderDamage.atx()) {
                  }
                  break;
               }

               Entry entry = (Entry)iterator2.next();
               if (entry.getKey() != autoCrystal.pos) {
                  if (((UnidentifiedHelper7)entry.getValue()).qW() > 0.0) {
                     label119:
                     while (true) {
                        BlockPos pos2 = (BlockPos)entry.getKey();
                        double d = ((UnidentifiedHelper7)entry.getValue()).qW();

                        label117:
                        while (true) {
                           ((UnidentifiedHelper7)entry.getValue()).qV(false);
                           int n = (int)(((Color)autoCrystal.side.atx()).getAlpha() * ((UnidentifiedHelper7)entry.getValue()).qW());
                           int n2 = (int)(((Color)autoCrystal.line.atx()).getAlpha() * ((UnidentifiedHelper7)entry.getValue()).qW());
                           Color color = ColorUtils.iE((Color)autoCrystal.side.atx(), n);
                           Color color2 = ColorUtils.iE((Color)autoCrystal.line.atx(), n2);
                           BoxRenderer boxRenderer2 = render3DContext.boxRenderer;
                           BlockPos pos3 = (BlockPos)entry.getKey();
                           Setting setting2 = autoCrystal.boxMode;
                           BlockPos pos4 = pos3;
                           boxRenderer2 = boxRenderer2;
                           pos3 = pos4;
                           ShapeMode shapeMode = (ShapeMode)setting2.atx();
                           pos4 = pos3;
                           boxRenderer2.YZ(pos4, color, color2, shapeMode);

                           while (iterator2.hasNext()) {
                              entry = (Entry)iterator2.next();
                              if (entry.getKey() != autoCrystal.pos) {
                                 if (((UnidentifiedHelper7)entry.getValue()).qW() > d) {
                                    continue label119;
                                 }
                                 continue label117;
                              }
                           }

                           if ((Boolean)autoCrystal.renderDamage.atx() && pos2 != null) {
                              StringBuilder stringBuilder = new StringBuilder();
                              if ((Boolean)autoCrystal.renderDamage.atx()) {
                                 if ((Boolean)autoCrystal.targetDamage.atx()) {
                                    StringBuilder stringBuilder2 = stringBuilder;
                                    double d2 = UnidentifiedUtils3.rx(autoCrystal.o, 1);
                                    stringBuilder2.append(d2);
                                 }

                                 if ((Boolean)autoCrystal.targetDamage.atx() && (Boolean)autoCrystal.selfDamage.atx()) {
                                    stringBuilder.append("/");
                                 }

                                 if ((Boolean)autoCrystal.selfDamage.atx()) {
                                    StringBuilder stringBuilder3 = stringBuilder;
                                    double d3 = UnidentifiedUtils3.rx(autoCrystal.p, 1);
                                    stringBuilder3.append(d3);
                                 }
                              }

                              MatrixStack matrices2 = render3DContext.matrices;
                              String str = stringBuilder.toString();
                              MatrixStack matrices3 = matrices2;
                              String str2 = str;
                              Vec3d temp = pos2.toCenterPos();
                              int n3 = a((int)(255.0 * d)).getRGB();
                              Vec3d vec3 = temp;
                              str = str2;
                              RenderUtils.acv(matrices3, str, vec3, n3);
                           }
                           break label124;
                        }
                     }
                  }

                  ((UnidentifiedHelper7)entry.getValue()).qV(false);
                  int n4 = (int)(((Color)autoCrystal.side.atx()).getAlpha() * ((UnidentifiedHelper7)entry.getValue()).qW());
                  int n5 = (int)(((Color)autoCrystal.line.atx()).getAlpha() * ((UnidentifiedHelper7)entry.getValue()).qW());
                  Color color3 = ColorUtils.iE((Color)autoCrystal.side.atx(), n4);
                  Color color4 = ColorUtils.iE((Color)autoCrystal.line.atx(), n5);
                  BoxRenderer temp2 = render3DContext.boxRenderer;
                  BlockPos temp3 = (BlockPos)entry.getKey();
                  Setting setting3 = autoCrystal.boxMode;
                  BlockPos pos5 = temp3;
                  temp2 = temp2;
                  temp3 = pos5;
                  ShapeMode shapeMode2 = (ShapeMode)setting3.atx();
                  pos5 = temp3;
                  temp2.YZ(pos5, color3, color4, shapeMode2);
               }
            }
         } else if (((SliderFadeMode)autoCrystal.render.atx()).VH()) {
            BlockPos pos6 = autoCrystal.pos;
            if (pos6 != null) {
               this.vec = pos6.toCenterPos();
            }

            if (this.vec == null) {
               return;
            }

            if ((Double)autoCrystal.fadeSpeed.atx() >= 1.0) {
               double d4;
               if (autoCrystal.stopwatch.afq((long)((Double)autoCrystal.startFade.atx() * 1000.0))) {
                  d4 = 0.0;
               } else {
                  d4 = 0.5;
               }

               this.c = d4;
            } else {
               double d5 = this.c;
               boolean flag = autoCrystal.stopwatch.afq((long)((Double)autoCrystal.startFade.atx() * 1000.0));
               double d6 = d5;
               double d7;
               if (flag) {
                  d6 = 0.0;
                  d7 = d6;
               } else {
                  d6 = 0.5;
                  d7 = d6;
               }

               this.c = PhantomView.DH(d7, d6, (Double)autoCrystal.fadeSpeed.atx() / 10.0);
            }

            if (this.c == 0.0) {
               this.vec2 = null;
               return;
            }

            if (this.vec2 != null && !((Double)autoCrystal.sliderSpeed.atx() >= 1.0)) {
               this.vec2 = new Vec3d(
                  PhantomView.DH(this.vec2.x, this.vec.x, (Double)autoCrystal.sliderSpeed.atx() / 10.0),
                  PhantomView.DH(this.vec2.y, this.vec.y, (Double)autoCrystal.sliderSpeed.atx() / 10.0),
                  PhantomView.DH(this.vec2.z, this.vec.z, (Double)autoCrystal.sliderSpeed.atx() / 10.0)
               );
            } else {
               this.vec2 = this.vec;
            }

            Box box = new Box(this.vec2, this.vec2);
            if ((Boolean)autoCrystal.shrink.atx()) {
               Box box2 = box;
               double d8 = this.c;
               box = box2.expand(d8);
            } else {
               box = box.expand(0.5);
            }

            Setting setting4 = autoCrystal.side;
            BoxRenderer boxRenderer3 = render3DContext.boxRenderer;
            Color color5 = ColorUtils.iE((Color)setting4.atx(), (int)(((Color)autoCrystal.side.atx()).getAlpha() * this.c * 2.0));
            setting4 = autoCrystal.line;
            Color color6 = color5;
            boxRenderer3 = boxRenderer3;
            color5 = color6;
            Color color7 = ColorUtils.iE((Color)setting4.atx(), (int)(((Color)autoCrystal.line.atx()).getAlpha() * this.c * 2.0));
            setting4 = autoCrystal.boxMode;
            color6 = color7;
            Color color8 = color5;
            boxRenderer3 = boxRenderer3;
            color5 = color8;
            color7 = color6;
            ShapeMode shapeMode3 = (ShapeMode)setting4.atx();
            color6 = color7;
            color8 = color5;
            boxRenderer3.Za(box, color8, color6, shapeMode3, 0);
            if ((Boolean)autoCrystal.renderDamage.atx() && this.vec2 != null && !autoCrystal.stopwatch.afq((long)((Double)autoCrystal.startFade.atx() * 1000.0))) {
               StringBuilder stringBuilder4 = new StringBuilder();
               if ((Boolean)autoCrystal.renderDamage.atx()) {
                  if ((Boolean)autoCrystal.targetDamage.atx()) {
                     StringBuilder stringBuilder5 = stringBuilder4;
                     double d9 = UnidentifiedUtils3.rx(autoCrystal.o, 1);
                     stringBuilder5.append(d9);
                  }

                  if ((Boolean)autoCrystal.targetDamage.atx() && (Boolean)autoCrystal.selfDamage.atx()) {
                     stringBuilder4.append("/");
                  }

                  if ((Boolean)autoCrystal.selfDamage.atx()) {
                     StringBuilder stringBuilder6 = stringBuilder4;
                     double d10 = UnidentifiedUtils3.rx(autoCrystal.p, 1);
                     stringBuilder6.append(d10);
                  }
               }

               MatrixStack matrices4 = render3DContext.matrices;
               String str3 = stringBuilder4.toString();
               Vec3d vec4 = this.vec2;
               Color color9 = a((int)(255.0 * this.c * 2.0));
               Vec3d vec5 = vec4;
               String str4 = str3;
               MatrixStack matrices5 = matrices4;
               int n6 = color9.getRGB();
               RenderUtils.acv(matrices5, str4, vec5, n6);
            }
         }
      }

      if (((SliderFadeMode)autoCrystal.render.atx()).VI()) {
         Set set = autoCrystal.map.entrySet();
         Predicate predicate = new Predicate() {
            @Override
            public boolean test(Object render3DContext) {
               return AutoCrystalVec3dBlockPosManager.arr((Entry)render3DContext);
            }
         };
         set.removeIf(predicate);
         if (autoCrystal.pos != null && autoCrystal.sU()) {
            UnidentifiedHelper7 unidentifiedHelper7 = a((float)((Integer)autoCrystal.fadeTime.atx()).intValue());
            BlockPos pos7 = autoCrystal.pos;
            autoCrystal.map.put(pos7, unidentifiedHelper7);
         }
      }
   }

   static boolean arr(Entry entry) {
      boolean flag;
      if (((UnidentifiedHelper7)entry.getValue()).qW() == 0.0) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static UnidentifiedHelper7 a(float f) {
      return new UnidentifiedHelper7(true, f);
   }

   private static Color a(int n) {
      return new Color(255, 255, 255, n);
   }
}
