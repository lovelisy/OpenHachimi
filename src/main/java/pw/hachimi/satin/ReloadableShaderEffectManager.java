package pw.hachimi.satin;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import pw.hachimi.client.event.OnResolutionChangedEvent;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public final class ReloadableShaderEffectManager implements ShaderEffectManager {
   public static final ReloadableShaderEffectManager INSTANCE = new ReloadableShaderEffectManager();
   private final Set managedShaders = new ReferenceOpenHashSet();

   public ReloadableShaderEffectManager() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   public void onOnResolutionChanged(OnResolutionChangedEvent onResolutionChangedEvent) {
      this.onResolutionChanged(onResolutionChangedEvent.getWindow().getFramebufferWidth(), onResolutionChangedEvent.getWindow().getFramebufferHeight());
   }

   @Override
   public ManagedShaderEffect manage(Identifier id) {
      return this.manage(id, new Consumer() {
         @Override
         public void accept(Object id) {
            ReloadableShaderEffectManager.lambda$manage$0((ManagedShaderEffect)id);
         }
      });
   }

   @Override
   public ManagedShaderEffect manage(Identifier id, Consumer consumer) {
      ResettableManagedShaderEffect resettableManagedShaderEffect = new ResettableManagedShaderEffect(id, consumer);
      this.managedShaders.add(resettableManagedShaderEffect);
      return resettableManagedShaderEffect;
   }

   @Override
   public ManagedCoreShader manageCoreShader(Identifier id) {
      return this.manageCoreShader(id, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL);
   }

   @Override
   public ManagedCoreShader manageCoreShader(Identifier id, VertexFormat vertexFormat) {
      return this.manageCoreShader(id, vertexFormat, new Consumer() {
         @Override
         public void accept(Object id) {
            ReloadableShaderEffectManager.lambda$manageCoreShader$0((ManagedCoreShader)id);
         }
      });
   }

   @Override
   public ManagedCoreShader manageCoreShader(Identifier id, VertexFormat vertexFormat, Consumer consumer) {
      ResettableManagedCoreShader resettableManagedCoreShader = new ResettableManagedCoreShader(id, vertexFormat, consumer);
      this.managedShaders.add(resettableManagedCoreShader);
      return resettableManagedCoreShader;
   }

   public void reload(ResourceFactory resourceFactory) {
      Iterator iterator2 = this.managedShaders.iterator();

      while (iterator2.hasNext()) {
         ((ResettableManagedShaderBase)iterator2.next()).initializeOrLog(resourceFactory);
      }
   }

   public void onResolutionChanged(int n, int n2) {
      this.runShaderSetup(n, n2);
   }

   private void runShaderSetup(int n, int n2) {
      if (!this.managedShaders.isEmpty()) {
         for (ResettableManagedShaderBase resettableManagedShaderBase : (Iterable<ResettableManagedShaderBase>)this.managedShaders) {
            if (resettableManagedShaderBase.isInitialized()) {
               resettableManagedShaderBase.setup(n, n2);
            }
         }
      }
   }

   static void lambda$manageCoreShader$0(ManagedCoreShader managedCoreShader) {
   }

   static void lambda$manage$0(ManagedShaderEffect managedShaderEffect) {
   }
}
