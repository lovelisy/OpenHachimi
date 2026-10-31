package pw.hachimi.client.utils.render;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class ItemRendererBakedModelDirectionHelper implements BakedModel {
   private final List list = new ObjectArrayList();
   private BakedModel bakedModel;
   public static boolean a;

   public void setBakedModel(BakedModel bakedModel2) {
      this.bakedModel = bakedModel2;
   }

   private boolean pn(Direction direction) {
      boolean flag;
      if (direction == Direction.SOUTH) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public List getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
      if (face != null) {
         Object obj;
         if (this.pn(face)) {
            obj = this.bakedModel.getQuads(state, face, random);
         } else {
            obj = ImmutableList.of();
         }

         return (List)obj;
      } else {
         this.list.clear();
         Iterator iterator2 = this.bakedModel.getQuads(state, (Direction)null, random).iterator();

         while (iterator2.hasNext()) {
            BakedQuad bakedQuad = (BakedQuad)iterator2.next();
            if (this.pn(bakedQuad.getFace())) {
               this.list.add(bakedQuad);
            }
         }

         return this.list;
      }
   }

   public boolean useAmbientOcclusion() {
      return this.bakedModel.useAmbientOcclusion();
   }

   public boolean hasDepth() {
      return this.bakedModel.hasDepth();
   }

   public boolean isSideLit() {
      return this.bakedModel.isSideLit();
   }

   public boolean isBuiltin() {
      return this.bakedModel.isBuiltin();
   }

   public Sprite getParticleSprite() {
      return this.bakedModel.getParticleSprite();
   }

   public ModelTransformation getTransformation() {
      return this.bakedModel.getTransformation();
   }

   public ModelOverrideList getOverrides() {
      return this.bakedModel.getOverrides();
   }
}
