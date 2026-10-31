package pw.hachimi.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.LerpNauseaEvent;
import pw.hachimi.client.event.combat.UpdateTargetedEntity_1Event;
import pw.hachimi.client.event.combat.UpdateTargetedEntity_2Event;
import pw.hachimi.client.event.player.ShowFloatingItemEvent;
import pw.hachimi.client.event.render.BobViewEvent;
import pw.hachimi.client.event.render.GetFovEvent;
import pw.hachimi.client.event.render.RenderWorldEvent;
import pw.hachimi.client.event.render.RenderWorldEvent2;
import pw.hachimi.client.event.render.RenderWorld_2Event;
import pw.hachimi.client.event.render.ShouldRenderBlockOutlineEvent;
import pw.hachimi.client.event.render.TiltViewWhenHurtEvent;
import pw.hachimi.client.event.world.GetBlockInteractionRangeEvent;
import pw.hachimi.client.event.world.GetEntityInteractionRangeEvent;
import pw.hachimi.client.module.player.BlockPosBoxMultitask;
import pw.hachimi.client.module.player.GhostHand;
import pw.hachimi.client.module.render.AntiAliasing;
import pw.hachimi.client.module.render.AspectRatio;
import pw.hachimi.client.render.BoxRenderer;
import pw.hachimi.client.render.FramebufferFramebuffer;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.satin.ReloadableShaderEffectManager;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin2 {
   @Shadow
   @Final
   MinecraftClient client;
   @Shadow
   private float lastFovMultiplier;
   @Shadow
   private float fovMultiplier;
   @Shadow
   private float zoom;
   @Shadow
   private float zoomX;
   @Shadow
   private float zoomY;
   @Shadow
   private float viewDistance;
   @Shadow
   @Final
   private Camera camera;
   @Unique
   private BoxRenderer g;
   @Unique
   private final MatrixStack e = new MatrixStack();

   @Inject(
      method = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V",
      at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = "ldc=hand"),
      locals = LocalCapture.CAPTURE_FAILEXCEPTION
   )
   private void onRenderWorld(
      RenderTickCounter renderTickCounter, CallbackInfo callbackInfo, @Local(ordinal = 1) Matrix4f matrix4f, @Local(ordinal = 1) float f, @Local MatrixStack matrices
   ) {
      if (Mc.client.world != null && Mc.client.player != null) {
         Mc.client.getProfiler().push("Hachimi_render");
         if (this.g == null) {
            this.g = new BoxRenderer();
         }

         Render3DContext render3DContext = Render3DContext.afa(matrices, this.g, f, this.camera.getPos().x, this.camera.getPos().y, this.camera.getPos().z);
         RenderSystem.getModelViewStack().pushMatrix().mul(matrix4f);
         this.e.push();
         this.tiltViewWhenHurt(this.e, this.camera.getLastTickDelta());
         if ((Boolean)this.client.options.getBobView().getValue()) {
            this.bobView(this.e, this.camera.getLastTickDelta());
         }

         RenderSystem.getModelViewStack().mul(this.e.peek().getPositionMatrix().invert());
         this.e.pop();
         RenderSystem.applyModelViewMatrix();
         EventBusImpl.INSTANCE.rC(new RenderWorldEvent(matrices, f));
         if (AntiAliasing.Dj().awg()) {
            FramebufferFramebuffer.adP(AntiAliasing.Dj().Dk(), () -> {
               this.g.YF();
               EventBusImpl.INSTANCE.rB(render3DContext);
               this.g.YH(matrices);
            });
         } else {
            this.g.YF();
            EventBusImpl.INSTANCE.rB(render3DContext);
            this.g.YH(matrices);
         }

         EventBusImpl.INSTANCE.rC(new RenderWorldEvent2(this.e, f));
         RenderSystem.getModelViewStack().popMatrix();
         RenderSystem.applyModelViewMatrix();
         this.client.getProfiler().pop();
      }
   }

   @Inject(
      method = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/render/Camera;FLorg/joml/Matrix4f;)V", shift = Shift.AFTER)
   )
   public void hookRenderWorld$2(
      RenderTickCounter renderTickCounter, CallbackInfo callbackInfo, @Local(ordinal = 1) Matrix4f matrix4f, @Local(ordinal = 1) float f, @Local MatrixStack matrices
   ) {
      RenderWorld_2Event renderWorld2Event = new RenderWorld_2Event(matrices, f);
      EventBusImpl.INSTANCE.rC(renderWorld2Event);
   }

   @WrapOperation(method = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"))
   private float hookLerpNausea(float f, float f2, float f3, Operation operation) {
      LerpNauseaEvent lerpNauseaEvent = new LerpNauseaEvent();
      EventBusImpl.INSTANCE.rC(lerpNauseaEvent);
      return lerpNauseaEvent.aik() ? 0.0F : (Float)operation.call(new Object[]{f, f2, f3});
   }

   @Inject(method = "updateCrosshairTarget(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", shift = Shift.AFTER))
   private void hookUpdateTargetedEntity$1(float f, CallbackInfo callbackInfo) {
      UpdateTargetedEntity_1Event updateTargetedEntity1Event = new UpdateTargetedEntity_1Event(f, this.client.getCameraEntity());
      EventBusImpl.INSTANCE.rC(updateTargetedEntity1Event);
   }

   @Inject(method = "tiltViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At("HEAD"), cancellable = true)
   private void hookTiltViewWhenHurt(MatrixStack matrices, float f, CallbackInfo callbackInfo) {
      TiltViewWhenHurtEvent tiltViewWhenHurtEvent = new TiltViewWhenHurtEvent();
      EventBusImpl.INSTANCE.rC(tiltViewWhenHurtEvent);
      if (tiltViewWhenHurtEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "showFloatingItem(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
   private void hookShowFloatingItem(ItemStack stack, CallbackInfo callbackInfo) {
      ShowFloatingItemEvent showFloatingItemEvent = new ShowFloatingItemEvent(stack);
      EventBusImpl.INSTANCE.rC(showFloatingItemEvent);
      if (showFloatingItemEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "getBasicProjectionMatrix(D)Lorg/joml/Matrix4f;", at = @At("TAIL"), cancellable = true)
   public void getBasicProjectionMatrixHook(double d, CallbackInfoReturnable callbackInfoReturnable) {
      if (AspectRatio.YA().awg()) {
         MatrixStack matrices = new MatrixStack();
         matrices.peek().getPositionMatrix().identity();
         if (this.zoom != 1.0F) {
            matrices.translate(this.zoomX, -this.zoomY, 0.0F);
            matrices.scale(this.zoom, this.zoom, 1.0F);
         }

         matrices.peek()
            .getPositionMatrix()
            .mul(new Matrix4f().setPerspective((float)(d * (float) (Math.PI / 180.0)), (Float)AspectRatio.YA().ratio.atx(), 0.05F, this.viewDistance * 4.0F));
         callbackInfoReturnable.setReturnValue(matrices.peek().getPositionMatrix());
      }
   }

   @Inject(method = "renderNausea(Lnet/minecraft/client/gui/DrawContext;F)V", at = @At("HEAD"), cancellable = true)
   private void hookRenderNausea(DrawContext context, float f, CallbackInfo callbackInfo) {
      LerpNauseaEvent lerpNauseaEvent = new LerpNauseaEvent();
      EventBusImpl.INSTANCE.rC(lerpNauseaEvent);
      if (lerpNauseaEvent.aik()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "shouldRenderBlockOutline()Z", at = @At("HEAD"), cancellable = true)
   private void hookShouldRenderBlockOutline(CallbackInfoReturnable callbackInfoReturnable) {
      ShouldRenderBlockOutlineEvent shouldRenderBlockOutlineEvent = new ShouldRenderBlockOutlineEvent();
      EventBusImpl.INSTANCE.rC(shouldRenderBlockOutlineEvent);
      if (shouldRenderBlockOutlineEvent.aik()) {
         callbackInfoReturnable.setReturnValue(false);
         callbackInfoReturnable.cancel();
      }
   }

   @Inject(
      method = "updateCrosshairTarget(F)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;findCrosshairTarget(Lnet/minecraft/entity/Entity;DDF)Lnet/minecraft/util/hit/HitResult;"),
      cancellable = true
   )
   private void hookUpdateTargetedEntity$2(float f, CallbackInfo callbackInfo) {
      UpdateTargetedEntity_2Event updateTargetedEntity2Event = new UpdateTargetedEntity_2Event();
      EventBusImpl.INSTANCE.rC(updateTargetedEntity2Event);
      if (updateTargetedEntity2Event.aik() && this.client.crosshairTarget != null && this.client.crosshairTarget.getType() == Type.BLOCK) {
         this.client.getProfiler().pop();
         callbackInfo.cancel();
      }
   }

   @Redirect(method = "updateCrosshairTarget(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getEntityInteractionRange()D"))
   private double updateTargetedEntityModifySquaredMaxReach(ClientPlayerEntity player2) {
      GetEntityInteractionRangeEvent getEntityInteractionRangeEvent = new GetEntityInteractionRangeEvent();
      EventBusImpl.INSTANCE.rC(getEntityInteractionRangeEvent);
      return getEntityInteractionRangeEvent.aik() ? getEntityInteractionRangeEvent.MD() : player2.getEntityInteractionRange();
   }

   @Redirect(method = "updateCrosshairTarget(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getBlockInteractionRange()D"))
   private double updateTargetedEntityModifySquaredMaxReach$1(ClientPlayerEntity player2) {
      GetBlockInteractionRangeEvent getBlockInteractionRangeEvent = new GetBlockInteractionRangeEvent();
      EventBusImpl.INSTANCE.rC(getBlockInteractionRangeEvent);
      return getBlockInteractionRangeEvent.aik() ? getBlockInteractionRangeEvent.MD() : player2.getBlockInteractionRange();
   }

   @Inject(method = "bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At("HEAD"), cancellable = true)
   private void hookBobView(MatrixStack matrices, float f, CallbackInfo callbackInfo) {
      BobViewEvent bobViewEvent = new BobViewEvent(matrices, f);
      EventBusImpl.INSTANCE.rC(bobViewEvent);
      if (bobViewEvent.aik()) {
         callbackInfo.cancel();
         matrices.translate(0.0F, bobViewEvent.JY(), 0.0F);
      }
   }

   @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At("HEAD"), cancellable = true)
   private void hookGetFov(Camera camera, float f, boolean flag, CallbackInfoReturnable callbackInfoReturnable) {
      GetFovEvent getFovEvent = new GetFovEvent();
      EventBusImpl.INSTANCE.rC(getFovEvent);
      if (getFovEvent.aik()) {
         callbackInfoReturnable.cancel();
         callbackInfoReturnable.setReturnValue(getFovEvent.Ab());
      }
   }

   @Inject(method = "loadPrograms(Lnet/minecraft/resource/ResourceFactory;)V", at = @At("RETURN"))
   private void loadSatinPrograms(ResourceFactory resourceFactory, CallbackInfo callbackInfo) {
      ReloadableShaderEffectManager.INSTANCE.reload(resourceFactory);
   }

   @Inject(method = "findCrosshairTarget(Lnet/minecraft/entity/Entity;DDF)Lnet/minecraft/util/hit/HitResult;", at = @At("HEAD"), cancellable = true)
   public void findCrosshairTarget(Entity entity2, double d, double d2, float f, CallbackInfoReturnable callbackInfoReturnable) {
      if (GhostHand.acR().awg()) {
         double d3 = Math.max(d, d2);
         double d4 = MathHelper.square(d3);
         Vec3d vec = entity2.getCameraPosVec(f);
         HitResult hitResult = raycast(entity2, d3, f);
         double d5 = hitResult.getPos().squaredDistanceTo(vec);
         if (hitResult.getType() != Type.MISS) {
            d4 = d5;
            d3 = Math.sqrt(d5);
         }

         Vec3d vec2 = entity2.getRotationVec(f);
         Vec3d vec3 = vec.add(vec2.x * d3, vec2.y * d3, vec2.z * d3);
         Box box = entity2.getBoundingBox().stretch(vec2.multiply(d3)).expand(1.0, 1.0, 1.0);
         EntityHitResult entityHitResult = ProjectileUtil.raycast(entity2, vec, vec3, box, entity -> !entity.isSpectator() && entity.canHit(), d4);
         callbackInfoReturnable.setReturnValue(
            entityHitResult != null && entityHitResult.getPos().squaredDistanceTo(vec) < d5 ? ensureTargetInRange(entityHitResult, vec, d2) : ensureTargetInRange(hitResult, vec, d)
         );
      }
   }

   @Shadow
   private static HitResult ensureTargetInRange(HitResult hitResult, Vec3d vec, double d) {
      return null;
   }

   @Shadow
   protected abstract void tiltViewWhenHurt(MatrixStack var1, float var2);

   @Shadow
   protected abstract void bobView(MatrixStack var1, float var2);

   @Unique
   private static HitResult raycast(Entity entity2, double d, float f) {
      Vec3d vec = entity2.getCameraPosVec(f);
      Vec3d vec2 = entity2.getRotationVec(f);
      Vec3d vec3 = vec.add(vec2.x * d, vec2.y * d, vec2.z * d);
      return raycast(entity2.getWorld(), new RaycastContext(vec, vec3, ShapeType.OUTLINE, FluidHandling.NONE, entity2));
   }

   @Unique
   private static BlockHitResult raycast(BlockView blockView, RaycastContext raycastContext) {
      Vec3d temp = raycastContext.getStart();
      Vec3d temp2 = raycastContext.getEnd();
      return (BlockHitResult)raycast(temp, temp2, raycastContext, (context, blockPos) -> {
         BlockPos pos = (BlockPos)blockPos;
         BlockState state = blockView.getBlockState(pos);
         if (GhostHand.acR().awg() && GhostHand.acR().acS(state.getBlock())) {
            return null;
         }

         if (BlockPosBoxMultitask.alv().alQ() && BlockPosBoxMultitask.list.contains(pos)) {
            return null;
         }

         RaycastContext raycastContext2 = (RaycastContext)context;
         FluidState fluidState = blockView.getFluidState(pos);
         Vec3d vec = raycastContext2.getStart();
         Vec3d vec2 = raycastContext2.getEnd();
         VoxelShape voxelShape = raycastContext2.getBlockShape(state, blockView, pos);
         BlockHitResult blockHitResult = raycastBlock(blockView, vec, vec2, pos, voxelShape, state);
         VoxelShape voxelShape2 = raycastContext2.getFluidShape(fluidState, blockView, pos);
         BlockHitResult blockHitResult2 = raycast(voxelShape2, vec, vec2, pos);
         double d = blockHitResult == null ? Double.MAX_VALUE : raycastContext2.getStart().squaredDistanceTo(blockHitResult.getPos());
         double d2 = blockHitResult2 == null ? Double.MAX_VALUE : raycastContext2.getStart().squaredDistanceTo(blockHitResult2.getPos());
         return d <= d2 ? blockHitResult : blockHitResult2;
      }, context -> {
         RaycastContext raycastContext2 = (RaycastContext)context;
         Vec3d vec = raycastContext2.getStart().subtract(raycastContext2.getEnd());
         return BlockHitResult.createMissed(raycastContext2.getEnd(), Direction.getFacing(vec.x, vec.y, vec.z), BlockPos.ofFloored(raycastContext2.getEnd()));
      });
   }

   @Unique
   private static BlockHitResult raycastBlock(BlockView blockView, Vec3d vec, Vec3d vec2, BlockPos pos, VoxelShape voxelShape, BlockState state) {
      BlockHitResult blockHitResult = raycast(voxelShape, vec, vec2, pos);
      if (blockHitResult != null) {
         BlockHitResult blockHitResult2 = raycast(state.getRaycastShape(blockView, pos), vec, vec2, pos);
         if (blockHitResult2 != null && blockHitResult2.getPos().subtract(vec).lengthSquared() < blockHitResult.getPos().subtract(vec).lengthSquared()) {
            return blockHitResult.withSide(blockHitResult2.getSide());
         }
      }

      return blockHitResult;
   }

   @Unique
   private static BlockHitResult raycast(VoxelShape voxelShape, Vec3d vec, Vec3d vec2, BlockPos pos) {
      if (voxelShape.isEmpty()) {
         return null;
      }

      Vec3d vec3 = vec2.subtract(vec);
      return vec3.lengthSquared() < 1.0E-7 ? null : raycast(voxelShape.getBoundingBoxes(), vec, vec2, pos);
   }

   @Unique
   private static BlockHitResult raycast(Iterable iterable, Vec3d vec, Vec3d vec2, BlockPos pos) {
      double[] ds = new double[]{1.0};
      Direction direction = null;
      double d = vec2.x - vec.x;
      double d2 = vec2.y - vec.y;
      double d3 = vec2.z - vec.z;

      for (Box box : (Iterable<Box>)iterable) {
         direction = traceCollisionSide(box.offset(pos), vec, ds, direction, d, d2, d3);
      }

      if (direction == null) {
         return null;
      }

      double d4 = ds[0];
      return new BlockHitResult(vec.add(d4 * d, d4 * d2, d4 * d3), direction, pos, false);
   }

   @Unique
   @Nullable
   private static Direction traceCollisionSide(Box box, Vec3d vec, double[] ds, @Nullable Direction direction, double d, double d2, double d3) {
      return traceCollisionSide(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, vec, ds, direction, d, d2, d3);
   }

   @Unique
   @Nullable
   private static Direction traceCollisionSide(
      double d,
      double d2,
      double d3,
      double d4,
      double d5,
      double d6,
      Vec3d vec,
      double[] ds,
      @Nullable Direction direction,
      double d7,
      double d8,
      double d9
   ) {
      if (d7 > 1.0E-7) {
         direction = traceCollisionSide(ds, direction, d7, d8, d9, d, d2, d5, d3, d6, Direction.WEST, vec.x, vec.y, vec.z);
      } else if (d7 < -1.0E-7) {
         direction = traceCollisionSide(ds, direction, d7, d8, d9, d4, d2, d5, d3, d6, Direction.EAST, vec.x, vec.y, vec.z);
      }

      if (d8 > 1.0E-7) {
         direction = traceCollisionSide(ds, direction, d8, d9, d7, d2, d3, d6, d, d4, Direction.DOWN, vec.y, vec.z, vec.x);
      } else if (d8 < -1.0E-7) {
         direction = traceCollisionSide(ds, direction, d8, d9, d7, d5, d3, d6, d, d4, Direction.UP, vec.y, vec.z, vec.x);
      }

      if (d9 > 1.0E-7) {
         direction = traceCollisionSide(ds, direction, d9, d7, d8, d3, d, d4, d2, d5, Direction.NORTH, vec.z, vec.x, vec.y);
      } else if (d9 < -1.0E-7) {
         direction = traceCollisionSide(ds, direction, d9, d7, d8, d6, d, d4, d2, d5, Direction.SOUTH, vec.z, vec.x, vec.y);
      }

      return direction;
   }

   @Unique
   @Nullable
   private static Direction traceCollisionSide(
      double[] ds,
      @Nullable Direction direction,
      double d,
      double d2,
      double d3,
      double d4,
      double d5,
      double d6,
      double d7,
      double d8,
      Direction direction2,
      double d9,
      double d10,
      double d11
   ) {
      double d12 = (d4 - d9) / d;
      double d13 = d10 + d12 * d2;
      double d14 = d11 + d12 * d3;
      if (0.0 < d12 && d12 < ds[0] && d5 - 1.0E-7 < d13 && d13 < d6 + 1.0E-7 && d7 - 1.0E-7 < d14 && d14 < d8 + 1.0E-7) {
         ds[0] = d12;
         return direction2;
      } else {
         return direction;
      }
   }

   @Unique
   private static Object raycast(Vec3d vec, Vec3d vec2, Object obj, BiFunction biFunction, Function function2) {
      if (vec.equals(vec2)) {
         return function2.apply(obj);
      }

      double d = MathHelper.lerp(-1.0E-7, vec2.x, vec.x);
      double d2 = MathHelper.lerp(-1.0E-7, vec2.y, vec.y);
      double d3 = MathHelper.lerp(-1.0E-7, vec2.z, vec.z);
      double d4 = MathHelper.lerp(-1.0E-7, vec.x, vec2.x);
      double d5 = MathHelper.lerp(-1.0E-7, vec.y, vec2.y);
      double d6 = MathHelper.lerp(-1.0E-7, vec.z, vec2.z);
      int n = MathHelper.floor(d4);
      int n2 = MathHelper.floor(d5);
      int n3 = MathHelper.floor(d6);
      Mutable mutable = new Mutable(n, n2, n3);
      Object obj2 = biFunction.apply(obj, mutable);
      if (obj2 != null) {
         return obj2;
      }

      double d7 = d - d4;
      double d8 = d2 - d5;
      double d9 = d3 - d6;
      int n4 = MathHelper.sign(d7);
      int n5 = MathHelper.sign(d8);
      int n6 = MathHelper.sign(d9);
      double d10 = n4 == 0 ? Double.MAX_VALUE : n4 / d7;
      double d11 = n5 == 0 ? Double.MAX_VALUE : n5 / d8;
      double d12 = n6 == 0 ? Double.MAX_VALUE : n6 / d9;
      double d13 = d10 * (n4 > 0 ? 1.0 - MathHelper.fractionalPart(d4) : MathHelper.fractionalPart(d4));
      double d14 = d11 * (n5 > 0 ? 1.0 - MathHelper.fractionalPart(d5) : MathHelper.fractionalPart(d5));
      double d15 = d12 * (n6 > 0 ? 1.0 - MathHelper.fractionalPart(d6) : MathHelper.fractionalPart(d6));

      while (d13 <= 1.0 || d14 <= 1.0 || d15 <= 1.0) {
         if (d13 < d14) {
            if (d13 < d15) {
               n += n4;
               d13 += d10;
            } else {
               n3 += n6;
               d15 += d12;
            }
         } else if (d14 < d15) {
            n2 += n5;
            d14 += d11;
         } else {
            n3 += n6;
            d15 += d12;
         }

         Object obj3 = biFunction.apply(obj, mutable.set(n, n2, n3));
         if (obj3 != null) {
            return obj3;
         }
      }

      return function2.apply(obj);
   }

}
