package pw.hachimi.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.awt.Color;
import java.awt.Font;
import java.io.Closeable;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import pw.hachimi.client.Mc;
import pw.hachimi.client.model.render.DisableNormalMode;
import pw.hachimi.client.model.render.GlyphMatrix4f;
import pw.hachimi.client.model.render.TextureWidthTextureHeight;
import pw.hachimi.client.module.ColorHelper;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.utils.render.UnidentifiedUtils4;

public final class FontRenderer implements Closeable {
   private final float a;
   private final ObjectList objectList = new ObjectArrayList();
   private final Char2ObjectOpenHashMap char2ObjectOpenHashMap = new Char2ObjectOpenHashMap();
   private final Map map = new Object2ObjectOpenHashMap();
   private Font font;
   private int f;
   private int g;
   private boolean h;
   private Tessellator tessellator;

   public FontRenderer(long l, InputStream inputStream, float f2) {
      FontRenderer temp = this;
      byte temp2 = 0;
      InputStream temp3 = inputStream;

      try {
         temp.font = Font.createFont(temp2, temp3);
      } catch (Throwable error) {
         error.printStackTrace();
         this.font = new Font("Tahoma", 0, Math.round(f2));
      }

      this.a = f2;
      this.aaQ(this.font, f2);
   }

   private void aaQ(Font font2, float f2) {
      this.g = (int)Mc.client.getWindow().getScaleFactor();
      this.f = this.g;
      float f3 = f2 * this.f;
      this.font = font2.deriveFont(f3);
   }

   public void aaR(MatrixStack matrices, String str, double d2, double d3, int n2) {
      this.aaT(matrices, str, d2, d3, n2, true);
   }

   public void aaS(MatrixStack matrices, String str, double d2, double d3, int n2) {
      this.aaT(matrices, str, d2, d3, n2, false);
   }

   private void aaT(MatrixStack matrices, String str, double d2, double d3, int n2, boolean flag) {
      float f2 = (n2 >> 16 & 0xFF) / 255.0F;
      float f3 = (n2 >> 8 & 0xFF) / 255.0F;
      float f4 = (n2 & 0xFF) / 255.0F;
      float f5 = (n2 >> 24 & 0xFF) / 255.0F;
      this.aaW(matrices, str, (float)d2, (float)d3, f2, f3, f4, f5, flag);
   }

   public void aaU(MatrixStack matrices, String str, double d2, double d3, Color color2) {
      this.aaV(matrices, str, d2, d3, color2);
   }

   public void aaV(MatrixStack matrices, String str, double d2, double d3, Color color2) {
      this.aaW(matrices, str, (float)d2, (float)d3, color2.getRed() / 255.0F, color2.getGreen() / 255.0F, color2.getBlue() / 255.0F, color2.getAlpha(), false);
   }

   public void aaW(MatrixStack matrices, String str, float f2, float f3, float f4, float f5, float f6, float f7, boolean flag) {
      DisableNormalMode disableNormalMode = flag ? DisableNormalMode.NORMAL : DisableNormalMode.DISABLE;
      this.aaZ(matrices, str, f2, f3, f4, f5, f6, f7, disableNormalMode);
   }

   public void aaX(MatrixStack matrices, String str, float f2, float f3, float f4, float f5, float f6, float f7) {
      this.aaZ(matrices, str, f2, f3, f4, f5, f6, f7, DisableNormalMode.OUTLINE);
   }

   public void aaY() {
      this.h = true;
      if ((int)Mc.client.getWindow().getScaleFactor() != this.g) {
         this.close();
         this.aaQ(this.font, this.a);
      }
   }

   private void aaZ(MatrixStack matrices, String str, float f2, float f3, float f4, float f5, float f6, float f7, DisableNormalMode disableNormalMode) {
      boolean flag = this.h;
      if (!flag) {
         this.aaY();
      }

      float f8 = f4;
      float f9 = f5;
      float f10 = f6;
      matrices.push();
      f3 -= 3.0F;
      matrices.translate(f2, f3, 0.0F);
      matrices.scale(1.0F / this.f, 1.0F / this.f, 0.0F);
      this.tessellator = Tessellator.getInstance();
      Matrix4f matrix4f2 = new Matrix4f(matrices.peek().getPositionMatrix());
      char[] cs = str.toCharArray();
      float f11 = 0.0F;
      float f12 = 0.0F;
      int n2 = 0;
      synchronized (this.map) {
         for (int n3 = 0; n3 < cs.length; n3++) {
            char c2 = cs[n3];
            if (c2 == 167) {
               if (++n3 >= cs.length) {
                  break;
               }

               c2 = cs[n3];
               if (c2 == 'r') {
                  f8 = f4;
                  f9 = f5;
                  f10 = f6;
               } else {
                  int n4 = this.abi(c2);
                  f8 = (n4 >> 16 & 0xFF) / 255.0F;
                  f9 = (n4 >> 8 & 0xFF) / 255.0F;
                  f10 = (n4 & 0xFF) / 255.0F;
               }
            } else if (c2 == '\n') {
               f12 += this.abd(str.substring(n2, n3)) * this.f;
               f11 = 0.0F;
               n2 = n3 + 1;
            } else {
               TextureWidthTextureHeight textureWidthTextureHeight = this.abf(c2);
               if (textureWidthTextureHeight != null) {
                  if (textureWidthTextureHeight.value() != ' ') {
                     Identifier id = textureWidthTextureHeight.owner().qm();
                     GlyphMatrix4f glyphMatrix4f = new GlyphMatrix4f(f11, f12, f8, f9, f10, textureWidthTextureHeight, matrix4f2, f7, disableNormalMode);
                     ((ObjectList)this.map.computeIfAbsent(id, k -> abj((Identifier)k))).add(glyphMatrix4f);
                  }

                  f11 += textureWidthTextureHeight.width();
               }
            }
         }

         if (!flag) {
            this.aba();
         }
      }

      matrices.pop();
   }

   public void aba() {
      if (this.h) {
         if (!this.map.isEmpty()) {
            pw.hachimi.client.module.client.Font font2 = pw.hachimi.client.module.client.Font.kT();
            float f2 = font2.la();
            float f3 = font2.kZ();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.texParameter(3553, 10241, 9729);
            RenderSystem.texParameter(3553, 10240, 9729);
            RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
            Iterator iterator2 = this.map.entrySet().iterator();

            label44:
            while (true) {
               Entry entry;
               while (true) {
                  if (!iterator2.hasNext()) {
                     RenderSystem.disableBlend();
                     break label44;
                  }

                  entry = (Entry)iterator2.next();
                  byte temp = 0;
                  Entry temp2 = entry;

                  try {
                     RenderSystem.setShaderTexture(temp, (Identifier)temp2.getKey());
                     break;
                  } catch (Exception exception2) {
                  }
               }

               VertexFormat vertexFormat = VertexFormats.POSITION_TEXTURE_COLOR;
               DrawMode drawMode = DrawMode.QUADS;
               BufferBuilder bufferBuilder = this.tessellator.begin(drawMode, vertexFormat);
               ObjectListIterator objectListIterator = ((ObjectList)entry.getValue()).iterator();

               while (((Iterator)objectListIterator).hasNext()) {
                  GlyphMatrix4f glyphMatrix4f = (GlyphMatrix4f)((Iterator)objectListIterator).next();
                  float f4 = glyphMatrix4f.x();
                  float f5 = glyphMatrix4f.y();
                  float f6 = glyphMatrix4f.r();
                  float f7 = glyphMatrix4f.g();
                  float f8 = glyphMatrix4f.b();
                  TextureWidthTextureHeight textureWidthTextureHeight = glyphMatrix4f.glyph();
                  IdentifierNativeImageBackedTextureManager identifierNativeImageBackedTextureManager = textureWidthTextureHeight.owner();
                  float f9 = textureWidthTextureHeight.width();
                  float f10 = textureWidthTextureHeight.height();
                  float f11 = identifierNativeImageBackedTextureManager.qn();
                  float f12 = identifierNativeImageBackedTextureManager.qo();
                  float f13 = textureWidthTextureHeight.textureWidth() / f11;
                  float f14 = textureWidthTextureHeight.textureHeight() / f12;
                  float f15 = (textureWidthTextureHeight.textureWidth() + f9) / f11;
                  float f16 = (textureWidthTextureHeight.textureHeight() + f10) / f12;
                  if (glyphMatrix4f.getMode() == DisableNormalMode.NORMAL) {
                     abb(
                        bufferBuilder,
                        glyphMatrix4f.matrix4f(),
                        f4 + f3,
                        f5 + f3,
                        f9,
                        f10,
                        f13,
                        f14,
                        f15,
                        f16,
                        f6 * f2,
                        f7 * f2,
                        f8 * f2,
                        glyphMatrix4f.a()
                     );
                  } else if (glyphMatrix4f.getMode() == DisableNormalMode.OUTLINE) {
                     abb(
                        bufferBuilder,
                        glyphMatrix4f.matrix4f(),
                        f4 + f3,
                        f5 + f3,
                        f9,
                        f10,
                        f13,
                        f14,
                        f15,
                        f16,
                        f6 * f2,
                        f7 * f2,
                        f8 * f2,
                        glyphMatrix4f.a()
                     );
                     abb(
                        bufferBuilder,
                        glyphMatrix4f.matrix4f(),
                        f4 - f3,
                        f5 - f3,
                        f9,
                        f10,
                        f13,
                        f14,
                        f15,
                        f16,
                        f6 * f2,
                        f7 * f2,
                        f8 * f2,
                        glyphMatrix4f.a()
                     );
                     abb(
                        bufferBuilder,
                        glyphMatrix4f.matrix4f(),
                        f4 - f3,
                        f5 + f3,
                        f9,
                        f10,
                        f13,
                        f14,
                        f15,
                        f16,
                        f6 * f2,
                        f7 * f2,
                        f8 * f2,
                        glyphMatrix4f.a()
                     );
                     abb(
                        bufferBuilder,
                        glyphMatrix4f.matrix4f(),
                        f4 + f3,
                        f5 - f3,
                        f9,
                        f10,
                        f13,
                        f14,
                        f15,
                        f16,
                        f6 * f2,
                        f7 * f2,
                        f8 * f2,
                        glyphMatrix4f.a()
                     );
                  }

                  abb(bufferBuilder, glyphMatrix4f.matrix4f(), f4, f5, f9, f10, f13, f14, f15, f16, f6, f7, f8, glyphMatrix4f.a());
               }

               BuiltBuffer builtBuffer = bufferBuilder.endNullable();
               if (builtBuffer != null) {
                  BufferRenderer.drawWithGlobalProgram(builtBuffer);
               }
            }
         }

         this.tessellator = null;
         this.h = false;
         this.map.clear();
      }
   }

   private static void abb(
      BufferBuilder bufferBuilder,
      Matrix4f matrix4f2,
      float f2,
      float f3,
      float f4,
      float f5,
      float f6,
      float f7,
      float f8,
      float f9,
      float f10,
      float f11,
      float f12,
      float f13
   ) {
      float f14 = f3 + f5;
      bufferBuilder.vertex(matrix4f2, f2, f14, 0.0F).color(f10, f11, f12, f13).texture(f6, f9);
      float temp = f2 + f4;
      float f15 = f3 + f5;
      float f16 = temp;
      bufferBuilder.vertex(matrix4f2, f16, f15, 0.0F).color(f10, f11, f12, f13).texture(f8, f9);
      float f17 = f2 + f4;
      bufferBuilder.vertex(matrix4f2, f17, f3, 0.0F).color(f10, f11, f12, f13).texture(f8, f7);
      bufferBuilder.vertex(matrix4f2, f2, f3, 0.0F).color(f10, f11, f12, f13).texture(f6, f7);
   }

   public float abc(String str) {
      float f2 = 0.0F;
      float f3 = 0.0F;

      for (int n2 = 0; n2 < str.length(); n2++) {
         int n3 = n2;
         char c2 = str.charAt(n3);
         if (c2 == 167) {
            if (++n2 >= str.length()) {
               break;
            }

            str.charAt(n2);
         } else if (c2 == '\n') {
            f3 = Math.max(f2, f3);
            f2 = 0.0F;
         } else {
            TextureWidthTextureHeight textureWidthTextureHeight = this.abf(c2);
            if (textureWidthTextureHeight != null) {
               f2 += (float)textureWidthTextureHeight.width() / this.f;
            }
         }
      }

      return Math.max(f2, f3);
   }

   public float abd(String str) {
      if (str.isEmpty()) {
         TextureWidthTextureHeight textureWidthTextureHeight = this.abf(' ');
         return textureWidthTextureHeight != null ? (float)textureWidthTextureHeight.height() / this.f : this.a;
      }

      float f2 = 0.0F;
      float f3 = 0.0F;
      int n2 = 0;
      if (0 < str.length()) {
         label155: {
            char c2;
            label154: {
               label163: {
                  c2 = str.charAt(0);
                  if (c2 == 167) {
                     while (true) {
                        if (++n2 >= str.length()) {
                           return 0.0F + 0.0F;
                        }

                        str.charAt(n2);
                        if (++n2 >= str.length()) {
                           return 0.0F + 0.0F;
                        }

                        c2 = str.charAt(n2);
                        if (c2 != 167) {
                           if (c2 != '\n') {
                              break label154;
                           }
                           break;
                        }
                     }
                  } else {
                     if (c2 != '\n') {
                        break label154;
                     }

                     if (0.0F != 0.0F) {
                        break label163;
                     }
                  }

                  TextureWidthTextureHeight textureWidthTextureHeight2 = this.abf(' ');
                  f2 = textureWidthTextureHeight2 != null ? (float)textureWidthTextureHeight2.height() / this.f : this.a;
               }

               label140:
               while (true) {
                  f3 += f2;

                  while (++n2 < str.length()) {
                     c2 = str.charAt(n2);
                     if (c2 == 167) {
                        if (++n2 >= str.length()) {
                           return 0.0F + f3;
                        }

                        str.charAt(n2);
                     } else {
                        if (c2 == '\n') {
                           TextureWidthTextureHeight textureWidthTextureHeight3 = this.abf(' ');
                           f2 = textureWidthTextureHeight3 != null ? (float)textureWidthTextureHeight3.height() / this.f : this.a;
                           continue label140;
                        }

                        TextureWidthTextureHeight textureWidthTextureHeight4 = this.abf(c2);
                        if (textureWidthTextureHeight4 != null) {
                           f2 = Math.max((float)textureWidthTextureHeight4.height() / this.f, 0.0F);
                           break label155;
                        }
                     }
                  }

                  return 0.0F + f3;
               }
            }

            TextureWidthTextureHeight textureWidthTextureHeight5 = this.abf(c2);
            if (textureWidthTextureHeight5 != null) {
               f2 = Math.max((float)textureWidthTextureHeight5.height() / this.f, 0.0F);
            }
         }

         label118:
         while (++n2 < str.length()) {
            char c3 = str.charAt(n2);
            if (c3 != 167) {
               if (c3 != '\n') {
                  TextureWidthTextureHeight textureWidthTextureHeight6 = this.abf(c3);
                  if (textureWidthTextureHeight6 != null) {
                     f2 = Math.max((float)textureWidthTextureHeight6.height() / this.f, f2);
                  }
               } else {
                  if (f2 == 0.0F) {
                     TextureWidthTextureHeight textureWidthTextureHeight7 = this.abf(' ');
                     f2 = textureWidthTextureHeight7 != null ? (float)textureWidthTextureHeight7.height() / this.f : this.a;
                  }

                  label111:
                  while (true) {
                     f3 += f2;

                     while (++n2 < str.length()) {
                        c3 = str.charAt(n2);
                        if (c3 == 167) {
                           if (++n2 >= str.length()) {
                              return 0.0F + f3;
                           }

                           str.charAt(n2);
                        } else {
                           if (c3 == '\n') {
                              TextureWidthTextureHeight textureWidthTextureHeight8 = this.abf(' ');
                              f2 = textureWidthTextureHeight8 != null ? (float)textureWidthTextureHeight8.height() / this.f : this.a;
                              continue label111;
                           }

                           TextureWidthTextureHeight textureWidthTextureHeight9 = this.abf(c3);
                           if (textureWidthTextureHeight9 != null) {
                              f2 = Math.max((float)textureWidthTextureHeight9.height() / this.f, 0.0F);
                              continue label118;
                           }
                        }
                     }

                     return 0.0F + f3;
                  }
               }
            } else {
               if (++n2 < str.length()) {
                  str.charAt(n2);
                  continue;
               }
               break;
            }
         }

         return f2 + f3;
      } else {
         return 0.0F + 0.0F;
      }
   }

   public float abe() {
      return this.a;
   }

   private TextureWidthTextureHeight abf(char c2) {
      TextureWidthTextureHeight textureWidthTextureHeight = (TextureWidthTextureHeight)this.char2ObjectOpenHashMap.get(c2);
      if (textureWidthTextureHeight != null) {
         return textureWidthTextureHeight;
      }

      TextureWidthTextureHeight textureWidthTextureHeight2 = this.abg(c2);
      if (textureWidthTextureHeight2 != null) {
         this.char2ObjectOpenHashMap.put(c2, textureWidthTextureHeight2);
      }

      return textureWidthTextureHeight2;
   }

   private TextureWidthTextureHeight abg(char c2) {
      ObjectListIterator objectListIterator = this.objectList.iterator();

      while (((Iterator)objectListIterator).hasNext()) {
         IdentifierNativeImageBackedTextureManager identifierNativeImageBackedTextureManager = (IdentifierNativeImageBackedTextureManager)((Iterator)objectListIterator).next();
         if (identifierNativeImageBackedTextureManager.qk(c2)) {
            return identifierNativeImageBackedTextureManager.qi(c2);
         }
      }

      int n2 = c2 / 256 * 256;
      pw.hachimi.client.module.client.Font font2 = pw.hachimi.client.module.client.Font.kT();
      IdentifierNativeImageBackedTextureManager identifierNativeImageBackedTextureManager2 = new IdentifierNativeImageBackedTextureManager(
         (char)n2, (char)(n2 + 256), this.font, this.abh(), font2.kW(), font2.kX()
      );
      this.objectList.add(identifierNativeImageBackedTextureManager2);
      return identifierNativeImageBackedTextureManager2.qi(c2);
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   @Override
   public void close() {
      FontRenderer temp = this;

      try {
         ObjectListIterator objectListIterator = temp.objectList.iterator();

         while (((Iterator)objectListIterator).hasNext()) {
            ((IdentifierNativeImageBackedTextureManager)((Iterator)objectListIterator).next()).qj();
         }
      } catch (Exception exception2) {
         exception2.printStackTrace();
         return;
      }

      temp = this;

      try {
         temp.objectList.clear();
         this.char2ObjectOpenHashMap.clear();
      } catch (Exception exception3) {
         exception3.printStackTrace();
      }
   }

   public Identifier abh() {
      String str = "font/storage/" + UnidentifiedUtils4.TY(32);
      return Identifier.of("hachimiclient", str);
   }

   private int abi(char c2) {
      return switch (c2) {
         case '0' -> Color.BLACK.getRGB();
         case '1' -> -16777046;
         case '2' -> -16733696;
         case '3' -> -16733526;
         case '4' -> -5636096;
         case '5' -> -5635926;
         case '6' -> -22016;
         case '7' -> -5592406;
         case '8' -> -11184811;
         case '9' -> -11184641;
         default -> -1;
         case 'a' -> -11141291;
         case 'b' -> -11141121;
         case 'c' -> -43691;
         case 'd' -> -43521;
         case 'e' -> -171;
         case 'f' -> -1;
         case 'g' -> ColorHelper.wx().wC();
         case 's' -> ColorHelper3.auO().auX();
      };
   }

   static ObjectList abj(Identifier id) {
      return new ObjectArrayList();
   }
}
