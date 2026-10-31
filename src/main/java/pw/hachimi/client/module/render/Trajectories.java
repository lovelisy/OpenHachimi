package pw.hachimi.client.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import java.util.Iterator;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.utils.world.EntityBoxUtils;
import pw.hachimi.client.world.ProjectileEntityVec3dProjectileEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.EggItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.TridentItem;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.eventbus.annotation.EventListener;

public class Trajectories extends ClientModule {
   Setting hand = this.registerSetting(new BooleanSetting("Hand", "Render the trajectory of the projectile you’re holding", true));
   Setting pearl = this.registerSetting(new BooleanSetting("Pearl", "Render the trajectory path of the pearls", true));

   public Trajectories() {
      super("Trajectories", "Renders the trajectory path of projectiles", ModuleCategory.RENDER);
   }

   @EventListener
   public void Uw(Render3DContext render3DContext) {
      MatrixStack matrixStack = new MatrixStack();
      if ((Boolean)this.pearl.atx()) {
         Iterator iterator2 = client.world.getEntities().iterator();
         while (iterator2.hasNext()) {
            Entity entity2 = (Entity)iterator2.next();
            if (!(entity2 instanceof EnderPearlEntity) || !(Boolean)this.pearl.atx()) {
               continue;
            }

            ProjectileEntityVec3dProjectileEntity projectileEntityVec3dProjectileEntity = new ProjectileEntityVec3dProjectileEntity((ProjectileEntity)entity2);
            int n = 0;
            for (int n2 = 0; n2 < 500; n2++) {
               HitResult hitResult = projectileEntityVec3dProjectileEntity.ajv();
               if (hitResult == null) {
                  render3DContext.boxRenderer.YJ(
                     projectileEntityVec3dProjectileEntity.prevX,
                     projectileEntityVec3dProjectileEntity.prevY,
                     projectileEntityVec3dProjectileEntity.prevZ,
                     projectileEntityVec3dProjectileEntity.getX(),
                     projectileEntityVec3dProjectileEntity.getY(),
                     projectileEntityVec3dProjectileEntity.getZ(),
                     ColorHelper3.auO().auW(n)
                  );
                  n += 40;
                  continue;
               }

               if (hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult) {
                  BlockHitResult blockHitResult = (BlockHitResult)hitResult;
                  int n3 = blockHitResult.getBlockPos().getX();
                  int n4 = blockHitResult.getBlockPos().getY();
                  int n5 = blockHitResult.getBlockPos().getZ();
                  render3DContext.boxRenderer.YY(n3, n4, n5, n3 + 1, n4 + 1, n5 + 1, ColorHelper3.auO().auW(40), ColorHelper3.auO().auW(140), ShapeMode.Both, 0);
               } else if (hitResult.getType() == HitResult.Type.ENTITY && hitResult instanceof EntityHitResult) {
                  EntityHitResult entityHitResult = (EntityHitResult)hitResult;
                  Box box = EntityBoxUtils.BE(entityHitResult.getEntity());
                  render3DContext.boxRenderer.Za(box, ColorHelper3.auO().auW(40), ColorHelper3.auO().auW(140), ShapeMode.Both, 0);
               }

               break;
            }
         }
      }

      if (!(Boolean)this.hand.atx()) {
         return;
      }

      double d = (double)((Integer)client.options.getFov().getValue()).intValue();
      ItemStack stack = client.player.getMainHandStack();
      Item item2 = stack.getItem();
      matrixStack.multiplyPositionMatrix(client.gameRenderer.getBasicProjectionMatrix(d));
      Matrix4f matrix4f = RenderSystem.getProjectionMatrix();
      RenderSystem.setProjectionMatrix(matrixStack.peek().getPositionMatrix(), VertexSorter.BY_DISTANCE);
      boolean flag = (item2 instanceof BowItem || item2 instanceof CrossbowItem || item2 instanceof TridentItem) && client.player.getItemUseTime() > 0 || this.Uy(item2);
      if (!flag) {
         RenderSystem.setProjectionMatrix(matrix4f, VertexSorter.BY_DISTANCE);
         return;
      }

      float f2 = client.player.getYaw();
      double d2 = EntityBoxUtils.BC(client.player.prevX, client.player.getX(), (double)client.getRenderTickCounter().getTickDelta(true));
      double d3 = EntityBoxUtils.BC(client.player.prevY, client.player.getY(), (double)client.getRenderTickCounter().getTickDelta(true));
      double d4 = EntityBoxUtils.BC(client.player.prevZ, client.player.getZ(), (double)client.getRenderTickCounter().getTickDelta(true));
      d3 = d3 + (double)client.player.getEyeHeight(client.player.getPose()) - 0.1000000014901161;
      if (item2 == client.player.getMainHandStack().getItem()) {
         d2 -= (double)(MathHelper.cos(f2 / 180.0F * 3.1415927F) * 0.16F);
         d4 -= (double)(MathHelper.sin(f2 / 180.0F * 3.1415927F) * 0.16F);
      } else {
         d2 += (double)(MathHelper.cos(f2 / 180.0F * 3.1415927F) * 0.16F);
         d4 += (double)(MathHelper.sin(f2 / 180.0F * 3.1415927F) * 0.16F);
      }

      float f3 = item2 instanceof BowItem ? 1.0F : 0.4F;
      double d5 = (double)(-MathHelper.sin(f2 / 180.0F * 3.1415927F) * MathHelper.cos(client.player.getPitch() / 180.0F * 3.1415927F) * f3);
      float f4 = client.player.getPitch();
      int n6 = item2 instanceof SplashPotionItem || item2 instanceof LingeringPotionItem || item2 instanceof ExperienceBottleItem ? -20 : 0;
      double d6 = (double)(-MathHelper.sin((f4 + (float)n6) / 180.0F * 3.1415927F) * f3);
      double d7 = (double)(MathHelper.cos(f2 / 180.0F * 3.1415927F) * MathHelper.cos(client.player.getPitch() / 180.0F * 3.1415927F) * f3);
      float f5 = (float)client.player.getItemUseTime() / 20.0F;
      f5 = (f5 * f5 + f5 * 2.0F) / 3.0F;
      if (f5 > 1.0F) {
         f5 = 1.0F;
      }

      float f6 = MathHelper.sqrt((float)(d5 * d5 + d6 * d6 + d7 * d7));
      d5 /= (double)f6;
      d6 /= (double)f6;
      d7 /= (double)f6;
      float f7;
      if (item2 instanceof BowItem) {
         f7 = f5 * 2.0F;
      } else if (item2 instanceof CrossbowItem) {
         f7 = 2.2F;
      } else {
         f7 = 1.0F;
      }

      float f8 = f7 * this.Ux(item2);
      d5 *= (double)f8;
      d6 *= (double)f8;
      d7 *= (double)f8;
      if (!client.player.isOnGround()) {
         d6 += client.player.getVelocity().getY();
      }

      d5 += client.player.getVelocity().getX();
      d7 += client.player.getVelocity().getZ();
      float f9 = item2 instanceof BowItem || item2 instanceof CrossbowItem ? 0.3F : 0.25F;
      boolean flag2 = false;

      while (!flag2) {
         boolean flag3 = false;
         Vec3d vec = new Vec3d(d2, d3, d4);
         d2 += d5;
         d3 += d6;
         d4 += d7;
         BlockPos pos = BlockPos.ofFloored(d2, d3, d4);
         if (client.world.getBlockState(pos).getBlock() == Blocks.WATER) {
            d5 *= 0.8;
            d6 *= 0.8;
            d7 *= 0.8;
         } else {
            d5 *= 0.99;
            d6 *= 0.99;
            d7 *= 0.99;
         }

         if (item2 instanceof BowItem) {
            d6 -= 0.05F;
         } else if (client.player.getMainHandStack().getItem() instanceof CrossbowItem) {
            d6 -= 0.05F;
         } else {
            d6 -= 0.03F;
         }

         Box box2 = new Box(d2 - (double)f9, d3 - (double)f9, d4 - (double)f9, d2 + (double)f9, d3 + (double)f9, d4 + (double)f9);
         Iterator iterator3 = client.world.getOtherEntities((Entity)null, box2).iterator();
         while (iterator3.hasNext()) {
            Entity entity3 = (Entity)iterator3.next();
            if (entity3 == client.player || this.Uz(entity3)) {
               continue;
            }

            Box box3 = EntityBoxUtils.BE(entity3);
            render3DContext.boxRenderer.Za(box3, ColorHelper3.auO().auW(40), ColorHelper3.auO().auW(140), ShapeMode.Both, 0);
            flag3 = true;
         }

         Vec3d vec2 = new Vec3d(d2, d3, d4);
         BlockHitResult blockHitResult2 = client.world.raycast(new RaycastContext(vec, vec2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, client.player));
         if (blockHitResult2 != null && blockHitResult2.getType() == HitResult.Type.BLOCK) {
            flag3 = true;
            render3DContext.boxRenderer.YY(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25, ColorHelper3.auO().auW(40), ColorHelper3.auO().auW(140), ShapeMode.Both, 0);
         }

         if (d3 < (double)client.world.getBottomY()) {
            RenderSystem.setProjectionMatrix(matrix4f, VertexSorter.BY_DISTANCE);
            return;
         }

         if (d5 == 0.0 && d6 == 0.0 && d7 == 0.0) {
            continue;
         }

         render3DContext.boxRenderer.YJ(vec.getX(), vec.getY(), vec.getZ(), vec2.getX(), vec2.getY(), vec2.getZ(), ColorHelper3.auO().auU());
         if (flag3) {
            flag2 = true;
         }
      }

      RenderSystem.setProjectionMatrix(matrix4f, VertexSorter.BY_DISTANCE);
   }

   private float Ux(Item item2) {
      if (item2 instanceof SplashPotionItem || item2 instanceof LingeringPotionItem) {
         return 0.4F;
      } else if (item2 instanceof ExperienceBottleItem) {
         return 0.5F;
      } else {
         return item2 instanceof TridentItem ? 2.0F : 1.5F;
      }
   }

   private boolean Uy(Item item2) {
      boolean flag;
      if (!(item2 instanceof EnderPearlItem)
         && !(item2 instanceof ExperienceBottleItem)
         && !(item2 instanceof SnowballItem)
         && !(item2 instanceof EggItem)
         && !(item2 instanceof SplashPotionItem)
         && !(item2 instanceof LingeringPotionItem)) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private boolean Uz(Entity entity2) {
      boolean flag;
      if (!(entity2 instanceof ExperienceBottleEntity)
         && !(entity2 instanceof EnderPearlEntity)
         && !(entity2 instanceof ExperienceOrbEntity)
         && !(entity2 instanceof PotionEntity)
         && !(entity2 instanceof ArrowEntity)
         && !(entity2 instanceof TridentEntity)
         && !(entity2 instanceof SnowballEntity)
         && !(entity2 instanceof EggEntity)) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }
}
