package pw.hachimi.satin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import pw.hachimi.satin.uniform.Uniform1f;
import pw.hachimi.satin.uniform.Uniform1i;
import pw.hachimi.satin.uniform.Uniform2f;
import pw.hachimi.satin.uniform.Uniform2i;
import pw.hachimi.satin.uniform.Uniform3f;
import pw.hachimi.satin.uniform.Uniform3i;
import pw.hachimi.satin.uniform.Uniform4f;
import pw.hachimi.satin.uniform.Uniform4i;
import pw.hachimi.satin.uniform.UniformMat4;

public final class ManagedUniform
   extends ManagedUniformBase
   implements Uniform1i,
   Uniform2i,
   Uniform3i,
   Uniform4i,
   Uniform1f,
   Uniform2f,
   Uniform3f,
   Uniform4f,
   UniformMat4 {
   private static final GlUniform[] NO_TARGETS = new GlUniform[0];
   private final int count;
   private GlUniform[] targets = NO_TARGETS;
   private int i0;
   private int i1;
   private int i2;
   private int i3;
   private float f0;
   private float f1;
   private float f2;
   private float f3;
   private boolean firstUpload = true;

   public ManagedUniform(String str, int n) {
      super(str);
      this.count = n;
   }

   @Override
   public boolean findUniformTargets(List list) {
      ArrayList arrayList = new ArrayList();
      Iterator iterator2 = list.iterator();

      while (iterator2.hasNext()) {
         GlUniform glUniform = ((PostEffectPass)iterator2.next()).getProgram().getUniformByName(this.name);
         if (glUniform != null) {
            if (glUniform.getCount() != this.count) {
               throw new IllegalStateException("Mismatched number of values, expected " + this.count + " but JSON definition declares " + glUniform.getCount());
            }

            arrayList.add(glUniform);
         }
      }

      if (!arrayList.isEmpty()) {
         this.targets = (GlUniform[])arrayList.toArray(new GlUniform[0]);
         this.syncCurrentValues();
         return true;
      } else {
         this.targets = NO_TARGETS;
         return false;
      }
   }

   @Override
   public boolean findUniformTarget(ShaderProgram shaderProgram) {
      GlUniform glUniform = shaderProgram.getUniform(this.name);
      if (glUniform != null) {
         this.targets = new GlUniform[]{glUniform};
         this.syncCurrentValues();
         return true;
      } else {
         this.targets = NO_TARGETS;
         return false;
      }
   }

   private void syncCurrentValues() {
      if (!this.firstUpload) {
         for (GlUniform glUniform : this.targets) {
            if (glUniform.getIntData() != null) {
               glUniform.setForDataType(this.i0, this.i1, this.i2, this.i3);
            } else {
               assert glUniform.getFloatData() != null;
               glUniform.setForDataType(this.f0, this.f1, this.f2, this.f3);
            }
         }
      }
   }

   @Override
   public void set(int n) {
      GlUniform[] glUniforms = this.targets;
      if (glUniforms.length > 0 && (this.firstUpload || this.i0 != n)) {
         int n2 = glUniforms.length;

         for (int n3 = 0; n3 < n2; n3++) {
            glUniforms[n3].set(n);
         }

         this.i0 = n;
         this.firstUpload = false;
      }
   }

   @Override
   public void set(int n, int n2) {
      GlUniform[] glUniforms = this.targets;
      if (glUniforms.length > 0 && (this.firstUpload || this.i0 != n || this.i1 != n2)) {
         int n3 = glUniforms.length;

         for (int n4 = 0; n4 < n3; n4++) {
            glUniforms[n4].set(n, n2);
         }

         this.i0 = n;
         this.i1 = n2;
         this.firstUpload = false;
      }
   }

   @Override
   public void set(int n, int n2, int n3) {
      GlUniform[] glUniforms = this.targets;
      if (glUniforms.length > 0 && (this.firstUpload || this.i0 != n || this.i1 != n2 || this.i2 != n3)) {
         int n4 = glUniforms.length;

         for (int n5 = 0; n5 < n4; n5++) {
            glUniforms[n5].set(n, n2, n3);
         }

         this.i0 = n;
         this.i1 = n2;
         this.i2 = n3;
         this.firstUpload = false;
      }
   }

   @Override
   public void set(int n, int n2, int n3, int n4) {
      GlUniform[] glUniforms = this.targets;
      if (glUniforms.length > 0 && (this.firstUpload || this.i0 != n || this.i1 != n2 || this.i2 != n3 || this.i3 != n4)) {
         int n5 = glUniforms.length;

         for (int n6 = 0; n6 < n5; n6++) {
            glUniforms[n6].set(n, n2, n3, n4);
         }

         this.i0 = n;
         this.i1 = n2;
         this.i2 = n3;
         this.i3 = n4;
         this.firstUpload = false;
      }
   }

   @Override
   public void set(float f) {
      GlUniform[] glUniforms = this.targets;
      if (glUniforms.length > 0 && (this.firstUpload || this.f0 != f)) {
         int n = glUniforms.length;

         for (int n2 = 0; n2 < n; n2++) {
            glUniforms[n2].set(f);
         }

         this.f0 = f;
         this.firstUpload = false;
      }
   }

   @Override
   public void set(float f, float f4) {
      GlUniform[] glUniforms = this.targets;
      if (glUniforms.length > 0 && (this.firstUpload || this.f0 != f || this.f1 != f4)) {
         int n = glUniforms.length;

         for (int n2 = 0; n2 < n; n2++) {
            glUniforms[n2].set(f, f4);
         }

         this.f0 = f;
         this.f1 = f4;
         this.firstUpload = false;
      }
   }

   @Override
   public void set(Vector2f vector2f) {
      this.set(vector2f.x(), vector2f.y());
   }

   @Override
   public void set(float f, float f4, float f5) {
      GlUniform[] glUniforms = this.targets;
      if (glUniforms.length > 0 && (this.firstUpload || this.f0 != f || this.f1 != f4 || this.f2 != f5)) {
         int n = glUniforms.length;

         for (int n2 = 0; n2 < n; n2++) {
            glUniforms[n2].set(f, f4, f5);
         }

         this.f0 = f;
         this.f1 = f4;
         this.f2 = f5;
         this.firstUpload = false;
      }
   }

   @Override
   public void set(Vector3f vector3f) {
      this.set(vector3f.x(), vector3f.y(), vector3f.z());
   }

   @Override
   public void set(float f, float f4, float f5, float f6) {
      GlUniform[] glUniforms = this.targets;
      if (glUniforms.length > 0 && (this.firstUpload || this.f0 != f || this.f1 != f4 || this.f2 != f5 || this.f3 != f6)) {
         int n = glUniforms.length;

         for (int n2 = 0; n2 < n; n2++) {
            glUniforms[n2].set(f, f4, f5, f6);
         }

         this.f0 = f;
         this.f1 = f4;
         this.f2 = f5;
         this.f3 = f6;
         this.firstUpload = false;
      }
   }

   @Override
   public void set(Vector4f vector4f) {
      this.set(vector4f.x(), vector4f.y(), vector4f.z(), vector4f.w());
   }

   @Override
   public void set(Matrix4f matrix4f) {
      GlUniform[] glUniforms = this.targets;
      if (glUniforms.length > 0) {
         int n = glUniforms.length;

         for (int n2 = 0; n2 < n; n2++) {
            glUniforms[n2].set(matrix4f);
         }
      }
   }

   @Override
   public void setFromArray(float[] fs) {
      if (this.count != fs.length) {
         throw new IllegalArgumentException("Mismatched values size, expected " + this.count + " but got " + fs.length);
      }

      GlUniform[] glUniforms = this.targets;
      if (glUniforms.length > 0) {
         int n = glUniforms.length;

         for (int n2 = 0; n2 < n; n2++) {
            glUniforms[n2].set(fs);
         }
      }
   }
}
