package pw.hachimi.satin;

import net.minecraft.client.gl.ShaderProgram;
import pw.hachimi.satin.uniform.UniformFinder;

public interface ManagedCoreShader extends UniformFinder {
   ShaderProgram getProgram();

   void release();
}
