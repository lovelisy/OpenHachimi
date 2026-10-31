package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL32C;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.event.OnResolutionChangedEvent;
import pw.hachimi.client.event.render.RenderEvent3;
import pw.hachimi.client.event.render.RenderPreEvent;
import pw.hachimi.client.event.render.RenderWorldEvent;
import pw.hachimi.client.event.render.RenderWorld_2Event;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.player.Freecam;
import pw.hachimi.client.render.BoxRenderer;
import pw.hachimi.client.render.ColorManager;
import pw.hachimi.client.render.FramebufferFramebuffer;
import pw.hachimi.client.render.FramebufferFramebuffer2;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.ChamsFramebufferHelper;
import pw.hachimi.client.utils.render.MatrixModelPartUtils;
import pw.hachimi.client.utils.render.MatrixUtils;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.render.RenderUtils2;
import pw.hachimi.client.utils.render.Vec3dGameRendererHelper;
import pw.hachimi.client.utils.world.BoxEntityUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Chams extends ClientModule {
   public static Chams INSTANCE;
   Setting renderDistance = this.registerSetting(new NumberSetting("RenderDistance", "The shader render range", 0.0F, 256.0F, 512.0F));
   Setting shape = this.registerSetting(new EnumSetting("Shape", "How the shapes are rendered.", ShapeMode.Both, ShapeMode.values()));
   Setting side = this.registerSetting(new ColorSetting("Side", "The side color.", h(25)));
   Setting line = this.registerSetting(new ColorSetting("Line", "The line color.", h(127)));
   Setting shine = this.registerSetting(new BooleanSetting("Shine", "Enables model shine", false));
   Setting texture = this.registerSetting(new BooleanSetting("Texture", "Enables entity model textures", true));
   Setting throughWalls = this.registerSetting(new BooleanSetting("ThroughWalls", "Renders chams through walls", true, this::isThroughWallsVisible));
   Setting players = this.registerSetting(new BooleanSetting("Players", "Render chams on other players", true));
   Setting self = this.registerSetting(new BooleanSetting("Self", "Render chams on the player", true));
   Setting monsters = this.registerSetting(new BooleanSetting("Monsters", "Render chams on monsters", true));
   Setting animals = this.registerSetting(new BooleanSetting("Animals", "Render chams on animals", true));
   Setting crystals = this.registerSetting(new BooleanSetting("Crystals", "Render chams on crystals", true));
   Setting bloom = this.registerSetting(new BooleanSetting("Bloom", "Adds a bloom glow effect to wireframe mode", false));
   Setting bloomShape = this.registerSetting(new EnumSetting("BloomShape", "How the shapes are rendered.", ShapeMode.Both, ShapeMode.values(), this::isBloomShapeVisible));
   Setting intensity = this.registerSetting(new NumberSetting("Intensity", "The brightness multiplier of the bloom glow.", 0.1, 1.5, 20.0, this::isIntensityVisible));
   Setting blurLevel = this.registerSetting(new NumberSetting("BlurLevel", "How many blur iterations to apply. Higher = wider glow.", 1, 3, 5, this::isBlurLevelVisible));
   Setting blurOffset = this.registerSetting(new NumberSetting("BlurOffset", "The spread offset of the bloom blur.", 1.0, 4.25, 10.0, this::isBlurOffsetVisible));
   Set set = new HashSet();
   public static boolean s;
   private static final int t = 6;
   private final ChamsFramebufferHelper[] chamsFramebufferHelpers = new ChamsFramebufferHelper[6];
   private ColorManager colorManager;
   private ColorManager colorManager2;
   private ColorManager colorManager3;
   public BoxRenderer boxRenderer;
   private MatrixStack matrices;
   private boolean A = false;
   private boolean B = false;
   private boolean C = false;
   public static boolean bj;

   public Chams() {
      super("Chams", "Renders entity models through walls", ModuleCategory.RENDER);
      INSTANCE = this;
   }

   @Override
   public void awe() {
      this.set.clear();
   }

   @EventListener(priority = 1)
   public void onRenderWorld(RenderWorldEvent renderWorldEvent) {
      if ((Boolean)this.throughWalls.atx() && (Boolean)this.texture.atx()) {
         final RenderWorldEvent renderWorldEvent2 = renderWorldEvent;
         final Chams chams = this;
         FramebufferFramebuffer2.afe(new Runnable() {
            @Override
            public void run() {
               chams.CO(renderWorldEvent2);
            }
         });
      }
   }

   @EventListener
   public void onRenderEvent3(RenderEvent3 renderEvent3) {
      if (!s && !Shaders.ab && (Boolean)this.crystals.atx()) {
         if (!(Boolean)this.texture.atx() || (Boolean)this.throughWalls.atx()) {
            renderEvent3.aim();
         }

         EndCrystalEntity endCrystalEntity2 = renderEvent3.endCrystalEntity;
         this.set.add(endCrystalEntity2);
      }
   }

   @EventListener
   public void onRenderPre(RenderPreEvent renderPreEvent) {
      if (!s && !Shaders.ab && this.CG(renderPreEvent.living)) {
         if (!(Boolean)this.texture.atx() || (Boolean)this.throughWalls.atx()) {
            renderPreEvent.aim();
         }

         LivingEntity living2 = renderPreEvent.living;
         this.set.add(living2);
      }
   }

   @EventListener
   public void CD(Render3DContext render3DContext) {
      if (this.shape.atx() != ShapeMode.None) {
         BoxRenderer boxRenderer2 = render3DContext.boxRenderer;
         if ((Boolean)this.bloom.atx() && !this.CI()) {
            this.CH(render3DContext.matrices);
         }

         if ((Boolean)this.shine.atx()) {
            boxRenderer2.YH(render3DContext.matrices);
            boxRenderer2.YF();
            Vec3dGameRendererHelper.b = true;
         }

         Iterator iterator2 = this.set.iterator();

         while (iterator2.hasNext()) {
            Entity entity2 = (Entity)iterator2.next();
            MatrixModelPartUtils.hX(render3DContext, entity2, (Color)this.side.atx(), (Color)this.line.atx(), (ShapeMode)this.shape.atx());
            if (this.CI()) {
               MatrixModelPartUtils.hX(
                  Render3DContext.afa(render3DContext.matrices, this.boxRenderer, render3DContext.f, render3DContext.g, render3DContext.h, render3DContext.i),
                  entity2,
                  (Color)this.side.atx(),
                  (Color)this.line.atx(),
                  (ShapeMode)this.bloomShape.atx()
               );
               Render3DContext.afa(render3DContext.matrices, boxRenderer2, render3DContext.f, render3DContext.g, render3DContext.h, render3DContext.i);
            }
         }

         if ((Boolean)this.shine.atx()) {
            boxRenderer2.YH(render3DContext.matrices);
            Vec3dGameRendererHelper.b = false;
            boxRenderer2.YF();
         }

         if (this.CI()) {
            this.CJ();
         }
      }

      this.set.clear();
   }

   private void CE(float f2, MatrixStack matrices2) {
      matrices2.push();
      s = true;
      Camera camera = client.gameRenderer.getCamera();
      Vec3d vec = camera.getPos();
      double d2 = vec.getX();
      double d3 = vec.getY();
      double d4 = vec.getZ();
      Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();
      Iterator iterator2 = this.set.iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (!RenderUtils.acy(entity2.getBoundingBox())) {
            Entity temp = entity2;
            ClientPlayerEntity player2 = client.player;
            if (!temp.hasPassengerDeep((Entity)player2)) {
               continue;
            }
         }

         BlockPos pos = entity2.getBlockPos();
         ClientWorld world2 = client.world;
         int n = pos.getY();
         if ((world2.isOutOfHeightLimit(n) || client.worldRenderer.isRenderingReady(pos))
            && (
               entity2 != camera.getFocusedEntity()
                  || camera.isThirdPerson()
                  || camera.getFocusedEntity() instanceof LivingEntity && ((LivingEntity)camera.getFocusedEntity()).isSleeping()
            )
            && (!(entity2 instanceof ClientPlayerEntity) || camera.getFocusedEntity() == entity2)) {
            if (entity2.age == 0) {
               double d5 = entity2.getX();
               entity2.lastRenderX = d5;
               double d6 = entity2.getY();
               entity2.lastRenderY = d6;
               double d7 = entity2.getZ();
               entity2.lastRenderZ = d7;
            }

            this.CF(entity2, d2, d3, d4, f2, matrices2, immediate);
         }
      }

      if (Bootstrap.NX()) {
         immediate.draw();
      }

      s = false;
      matrices2.pop();
   }

   private void CF(Entity entity2, double d2, double d3, double d4, float f2, MatrixStack matrices2, VertexConsumerProvider vertexConsumerProvider) {
      double d5 = MathHelper.lerp(f2, entity2.lastRenderX, entity2.getX());
      double d6 = MathHelper.lerp(f2, entity2.lastRenderY, entity2.getY());
      double d7 = MathHelper.lerp(f2, entity2.lastRenderZ, entity2.getZ());
      float f3 = MathHelper.lerp(f2, entity2.prevYaw, entity2.getYaw());
      EntityRenderDispatcher temp = client.getEntityRenderDispatcher();
      double temp2 = d5 - d2;
      double temp3 = d6 - d3;
      double temp4 = d7 - d4;
      int n = client.getEntityRenderDispatcher().getLight(entity2, f2);
      double d8 = temp4;
      double d9 = temp3;
      double d10 = temp2;
      temp.render(entity2, d10, d9, d8, f3, f2, matrices2, vertexConsumerProvider, n);
   }

   public boolean CG(Entity entity2) {
      if (client.player.distanceTo(entity2) > (Float)this.renderDistance.atx()) {
         return false;
      }

      if (entity2 instanceof PlayerEntity) {
         if (entity2 != client.player) {
            return (Boolean)this.players.atx();
         }

         boolean flag;
         if (!(Boolean)this.self.atx() || client.options.getPerspective().isFirstPerson() && !Freecam.gP().awg()) {
            flag = false;
         } else {
            flag = true;
         }

         return flag;
      } else {
         boolean flag2;
         if ((!BoxEntityUtils.kF(entity2) || !(Boolean)this.monsters.atx())
            && (!BoxEntityUtils.kH(entity2) && !BoxEntityUtils.kI(entity2) || !(Boolean)this.animals.atx())) {
            flag2 = false;
         } else {
            flag2 = true;
         }

         return flag2;
      }
   }

   public void CH(MatrixStack matrices2) {
      if (this.awg() && (Boolean)this.bloom.atx()) {
         this.matrices = matrices2;
         if (this.boxRenderer == null) {
            this.boxRenderer = new BoxRenderer();
         }

         this.boxRenderer.YF();
         this.B = true;
      }
   }

   public boolean CI() {
      return this.B;
   }

   public void CJ() {
      if (this.B) {
         this.B = false;
         this.boxRenderer.YG();
         this.CM();
         int n = client.getFramebuffer().fbo;
         if (AntiAliasing.Dj().awg()) {
            n = FramebufferFramebuffer.e;
         }

         this.chamsFramebufferHelpers[0].Lq();
         GL32C.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
         GL32C.glClear(16384);
         this.chamsFramebufferHelpers[0].Lr();
         this.boxRenderer.YH(this.matrices);
         RenderUtils2.ID(n);
         int temp = MinecraftClient.getInstance().getFramebuffer().textureWidth;
         int n2 = MinecraftClient.getInstance().getFramebuffer().textureHeight;
         int n3 = temp;
         GL32C.glViewport(0, 0, n3, n2);
         this.C = true;
         this.matrices = null;
      }
   }

   @EventListener
   private void onOnResolutionChanged(OnResolutionChangedEvent onResolutionChangedEvent) {
      int n = 0;

      do {
         if (this.chamsFramebufferHelpers[n] != null) {
            this.chamsFramebufferHelpers[n].Lt();
         }
      } while (++n < 6);
   }

   @EventListener
   public void onRenderWorld_2(RenderWorld_2Event renderWorld2Event) {
      if (this.C) {
         this.C = false;
         int n = Math.min(5, (Integer)this.blurLevel.atx());
         double d2 = (Double)this.blurOffset.atx();
         MatrixUtils.To();
         this.CN(this.chamsFramebufferHelpers[1], this.chamsFramebufferHelpers[0].b, this.colorManager, d2);

         for (int n2 = 1; n2 < n; n2++) {
            this.CN(this.chamsFramebufferHelpers[n2 + 1], this.chamsFramebufferHelpers[n2].b, this.colorManager, d2);
         }

         for (int n3 = n; n3 >= 2; n3--) {
            this.CN(this.chamsFramebufferHelpers[n3 - 1], this.chamsFramebufferHelpers[n3].b, this.colorManager2, d2);
         }

         this.CN(this.chamsFramebufferHelpers[0], this.chamsFramebufferHelpers[1].b, this.colorManager2, d2);
         MinecraftClient.getInstance().getFramebuffer().beginWrite(true);
         this.colorManager3.ajB();
         RenderUtils2.Jt(this.chamsFramebufferHelpers[0].b);
         this.colorManager3.ajE("uTexture");
         this.colorManager3.ajF("uIntensity", (Double)this.intensity.atx());
         GL32C.glEnable(3042);
         GL32C.glBlendFunc(770, 1);
         MatrixUtils.Tp();
         GL32C.glBlendFunc(770, 771);
         MatrixUtils.Tq();
      }
   }

   private void CM() {
      if (!this.A) {
         this.A = true;
         this.colorManager = new ColorManager("blur.vert", "blur_down.frag");
         this.colorManager2 = new ColorManager("blur.vert", "blur_up.frag");
         this.colorManager3 = new ColorManager("passthrough.vert", "bloom_composite.frag");
         int n = 0;

         do {
            this.chamsFramebufferHelpers[n] = new ChamsFramebufferHelper(1.0 / Math.pow(2.0, n));
         } while (++n < 6);
      }
   }

   private void CN(ChamsFramebufferHelper chamsFramebufferHelper, int n, ColorManager colorManager4, double d2) {
      chamsFramebufferHelper.Lq();
      chamsFramebufferHelper.Lr();
      colorManager4.ajB();
      RenderUtils2.Jt(n);
      colorManager4.ajE("uTexture");
      colorManager4.ajG("uHalfTexelSize", 0.5 / chamsFramebufferHelper.d, 0.5 / chamsFramebufferHelper.e);
      colorManager4.ajF("uOffset", d2);
      MatrixUtils.Tp();
   }

   void CO(RenderWorldEvent renderWorldEvent) {
      this.CE(renderWorldEvent.ll(), renderWorldEvent.getMatrices());
   }

   private Boolean isBlurOffsetVisible() {
      return (Boolean)this.bloom.atx();
   }

   private Boolean isBlurLevelVisible() {
      return (Boolean)this.bloom.atx();
   }

   private Boolean isIntensityVisible() {
      return (Boolean)this.bloom.atx();
   }

   private Boolean isBloomShapeVisible() {
      return (Boolean)this.bloom.atx();
   }

   private Boolean isThroughWallsVisible() {
      return (Boolean)this.texture.atx();
   }

   private static Color h(int n) {
      return new Color(255, 255, 255, n);
   }
}
