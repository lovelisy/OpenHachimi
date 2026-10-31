package pw.hachimi.client.model.render;

public enum NearestLinearMode {
   Nearest,
   Linear,
   LinearMipmapLinear;

   private static final NearestLinearMode[] d = wU();

   public int wT() {
      return switch (this) {
         case Nearest -> 9728;
         case Linear -> 9729;
         case LinearMipmapLinear -> 9987;
      };
   }

   private static NearestLinearMode[] wU() {
      return new NearestLinearMode[]{Nearest, Linear, LinearMipmapLinear};
   }

   private static MatchException a() {
      return new MatchException(null, null);
   }
}
