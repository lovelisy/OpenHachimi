package pw.hachimi.client.render;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase.TextureBase;
import net.minecraft.util.Identifier;
import pw.hachimi.client.mixin.RenderPhaseTextureBaseMixin;

public class RenderLayerTextureBaseManager implements Function {
   private final Map map;
   final Function function;

   public RenderLayerTextureBaseManager(Function function2) {
      this.function = function2;
      this.map = new HashMap();
   }

   public RenderLayer lh(TextureBase textureBase) {
      Map temp = this.map;
      Identifier temp2 = (Identifier)((RenderPhaseTextureBaseMixin)textureBase).hookGetId().get();
      final TextureBase textureBase2 = textureBase;
      final Function function2 = this.function;
      Function function3 = new Function() {
         @Override
         public Object apply(Object textureBase) {
            return RenderLayerTextureBaseManager.li(function2, textureBase2, (Identifier)textureBase);
         }
      };
      Identifier id = temp2;
      return (RenderLayer)temp.computeIfAbsent(id, function3);
   }

   @Override
   public Object apply(Object obj) {
      return this.lh((TextureBase)obj);
   }

   static RenderLayer li(Function function2, TextureBase textureBase, Identifier id) {
      return (RenderLayer)function2.apply(textureBase);
   }
}
