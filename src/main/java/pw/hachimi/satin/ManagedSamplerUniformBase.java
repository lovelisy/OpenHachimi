package pw.hachimi.satin;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.ShaderProgram;
import pw.hachimi.satin.uniform.SamplerUniform;

public abstract class ManagedSamplerUniformBase extends ManagedUniformBase implements SamplerUniform {
   protected SamplerAccess[] targets = new SamplerAccess[0];
   protected int[] locations = new int[0];
   protected Object cachedValue;

   public ManagedSamplerUniformBase(String str) {
      super(str);
   }

   @Override
   public boolean findUniformTargets(List list) {
      ArrayList arrayList = new ArrayList(list.size());
      IntArrayList intArrayList = new IntArrayList(list.size());
      Iterator iterator2 = list.iterator();

      while (iterator2.hasNext()) {
         JsonEffectShaderProgram jsonEffectShaderProgram = ((PostEffectPass)iterator2.next()).getProgram();
         String str = this.name;
         if (((SamplerAccess)jsonEffectShaderProgram).hasSampler(str)) {
            arrayList.add(jsonEffectShaderProgram);
            intArrayList.add(this.getSamplerLoc((SamplerAccess)jsonEffectShaderProgram));
         }
      }

      this.targets = (SamplerAccess[])arrayList.toArray(new SamplerAccess[0]);
      this.locations = intArrayList.toArray(new int[0]);
      this.syncCurrentValues();
      return this.targets.length > 0;
   }

   private int getSamplerLoc(SamplerAccess samplerAccess) {
      return (Integer)samplerAccess.getSamplerShaderLocs().get(samplerAccess.getSamplerNames().indexOf(this.name));
   }

   @Override
   public boolean findUniformTarget(ShaderProgram shaderProgram) {
      LogUtils.getLogger().warn(shaderProgram.getName());
      return this.findUniformTarget1((SamplerAccess)shaderProgram);
   }

   private boolean findUniformTarget1(SamplerAccess samplerAccess) {
      if (samplerAccess.hasSampler(this.name)) {
         this.targets = new SamplerAccess[]{samplerAccess};
         this.locations = new int[]{this.getSamplerLoc(samplerAccess)};
         this.syncCurrentValues();
         return true;
      } else {
         return false;
      }
   }

   private void syncCurrentValues() {
      Object obj = this.cachedValue;
      if (obj != null) {
         this.cachedValue = null;
         this.set(obj);
      }
   }

   protected abstract void set(Object var1);
}
