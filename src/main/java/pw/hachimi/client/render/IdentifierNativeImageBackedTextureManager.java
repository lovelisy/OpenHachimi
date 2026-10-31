package pw.hachimi.client.render;

import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.lwjgl.system.MemoryUtil;
import pw.hachimi.client.Mc;
import pw.hachimi.client.mixin.NativeImageMixin;
import pw.hachimi.client.model.render.TextureWidthTextureHeight;

public class IdentifierNativeImageBackedTextureManager {
   private final char a;
   private final char b_;
   private final Font font;
   private final Identifier id;
   private final int e;
   private final Char2ObjectArrayMap char2ObjectArrayMap = new Char2ObjectArrayMap();
   private final boolean g;
   private final boolean h;
   private int i;
   private int j;
   private boolean k;
   private static final String b = "Unknown data buffer type: ";

   public IdentifierNativeImageBackedTextureManager(char c, char c2, Font font2, Identifier id2, boolean flag, boolean flag2) {
      this.a = c;
      this.b_ = c2;
      this.font = font2;
      this.id = id2;
      this.e = 5;
      this.g = flag;
      this.h = flag2;
   }

   public TextureWidthTextureHeight qi(char c) {
      if (!this.k) {
         this.ql();
      }

      return (TextureWidthTextureHeight)this.char2ObjectArrayMap.get(c);
   }

   public void qj() {
      TextureManager temp = Mc.client.getTextureManager();
      Identifier id2 = this.id;
      temp.destroyTexture(id2);
      this.char2ObjectArrayMap.clear();
      this.k = false;
   }

   public boolean qk(char c) {
      return c >= this.a && c < this.b_;
   }

   public void ql() {
      if (!this.k) {
         ArrayList arrayList = new ArrayList();
         int n = this.b_ - this.a - 1;
         int n2 = (int)(Math.ceil(Math.sqrt(n)) * 1.5);
         int n3 = 0;
         int n4 = 0;
         int n5 = 0;
         int n6 = 0;
         int n7 = 0;
         int n8 = 0;
         int n9 = 0;
         FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), this.g, this.h);

         while (n3 <= n) {
            char c = (char)(this.a + n3);
            Font temp = this.font;
            String str = String.valueOf(c);
            Rectangle2D rectangle2D = temp.getStringBounds(str, fontRenderContext);
            int n10 = (int)Math.ceil(rectangle2D.getWidth());
            int n11 = (int)Math.ceil(rectangle2D.getHeight());
            n3++;
            n5 = Math.max(n5, n7 + n10);
            n6 = Math.max(n6, n8 + n11);
            if (n4 >= n2) {
               n7 = 0;
               n8 += n9 + this.e;
               n4 = 0;
               n9 = 0;
            }

            n9 = Math.max(n9, n11);
            TextureWidthTextureHeight textureWidthTextureHeight = new TextureWidthTextureHeight(n7, n8, n10, n11, c, this);
            arrayList.add(textureWidthTextureHeight);
            n7 += n10 + this.e;
            n4++;
         }

         BufferedImage bufferedImage = new BufferedImage(Math.max(n5 + this.e, 1), Math.max(n6 + this.e, 1), 2);
         this.i = bufferedImage.getWidth();
         this.j = bufferedImage.getHeight();
         Graphics2D graphics2D = bufferedImage.createGraphics();
         Color color = new Color(255, 255, 255, 0);
         graphics2D.setColor(color);
         int n12 = this.j;
         int n13 = this.i;
         graphics2D.fillRect(0, 0, n13, n12);
         Color color2 = Color.WHITE;
         graphics2D.setColor(color2);
         Object obj = this.h ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
         Key key = RenderingHints.KEY_FRACTIONALMETRICS;
         graphics2D.setRenderingHint(key, obj);
         Object obj2 = this.g ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF;
         Key key2 = RenderingHints.KEY_ANTIALIASING;
         graphics2D.setRenderingHint(key2, obj2);
         Object obj3 = this.g ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
         Key key3 = RenderingHints.KEY_TEXT_ANTIALIASING;
         graphics2D.setRenderingHint(key3, obj3);
         Iterator iterator2 = arrayList.iterator();

         while (iterator2.hasNext()) {
            TextureWidthTextureHeight textureWidthTextureHeight2 = (TextureWidthTextureHeight)iterator2.next();
            Font font2 = this.font;
            graphics2D.setFont(font2);
            FontMetrics fontMetrics = graphics2D.getFontMetrics();
            String temp2 = String.valueOf(textureWidthTextureHeight2.value());
            int n14 = textureWidthTextureHeight2.textureHeight() + fontMetrics.getAscent();
            int n15 = textureWidthTextureHeight2.textureWidth();
            String str2 = temp2;
            graphics2D.drawString(str2, n15, n14);
            this.char2ObjectArrayMap.put(textureWidthTextureHeight2.value(), textureWidthTextureHeight2);
         }

         this.qp(this.id, bufferedImage);
         this.k = true;
      }
   }

   public Identifier qm() {
      return this.id;
   }

   public int qn() {
      return this.i;
   }

   public int qo() {
      return this.j;
   }

   public void qp(Identifier id2, BufferedImage bufferedImage) {
      try {
         int width = bufferedImage.getWidth();
         int height = bufferedImage.getHeight();
         NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
         long pointer = ((NativeImageMixin)(Object)nativeImage).hookGetPointer();
         IntBuffer intBuffer = MemoryUtil.memIntBuffer(pointer, nativeImage.getWidth() * nativeImage.getHeight());
         WritableRaster raster = bufferedImage.getRaster();
         ColorModel colorModel = bufferedImage.getColorModel();
         int bands = raster.getNumBands();
         int dataType = raster.getDataBuffer().getDataType();
         Object pixel = switch (dataType) {
            case 0 -> new byte[bands];
            case 1 -> new short[bands];
            case 3 -> new int[bands];
            case 4 -> new float[bands];
            case 5 -> new double[bands];
            default -> throw new IllegalArgumentException("Unknown data buffer type: " + dataType);
         };

         for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
               raster.getDataElements(x, y, pixel);
               int alpha = colorModel.getAlpha(pixel);
               int red = colorModel.getRed(pixel);
               int green = colorModel.getGreen(pixel);
               int blue = colorModel.getBlue(pixel);
               intBuffer.put(alpha << 24 | blue << 16 | green << 8 | red);
            }
         }

         NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);
         texture.upload();
         texture.setFilter(true, true);
         if (RenderSystem.isOnRenderThread()) {
            Mc.client.getTextureManager().registerTexture(id2, (AbstractTexture)texture);
         } else {
            RenderSystem.recordRenderCall(() -> qq(id2, texture));
         }
      } catch (Throwable t) {
         t.printStackTrace();
      }
   }

   static void qq(Identifier id2, NativeImageBackedTexture nativeImageBackedTexture) {
      Mc.client.getTextureManager().registerTexture(id2, (AbstractTexture)nativeImageBackedTexture);
   }
}
