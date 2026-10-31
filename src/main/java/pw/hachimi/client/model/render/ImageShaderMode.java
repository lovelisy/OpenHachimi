package pw.hachimi.client.model.render;

public enum ImageShaderMode {
   Image,
   Shader,
   VANILLA;

   private static final ImageShaderMode[] d = ho();

   private static ImageShaderMode[] ho() {
      return new ImageShaderMode[]{Image, Shader, VANILLA};
   }
}
