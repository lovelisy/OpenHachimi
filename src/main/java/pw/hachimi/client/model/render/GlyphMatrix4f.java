package pw.hachimi.client.model.render;

import org.joml.Matrix4f;

public record GlyphMatrix4f(float x, float y, float r, float g, float b, TextureWidthTextureHeight glyph, Matrix4f matrix4f, float a, DisableNormalMode mode) {
   public static boolean w;

   public DisableNormalMode getMode() {
      return this.mode;
   }
}
