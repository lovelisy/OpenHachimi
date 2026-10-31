package pw.hachimi.client.mixin;

import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.event.OnStoppedUsingEvent;
import pw.hachimi.client.event.player.UseEvent2;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(TridentItem.class)
public abstract class TridentItemMixin {
   @Shadow
   private static boolean isAboutToBreak(ItemStack stack) {
      return false;
   }

   @Shadow
   public abstract int getMaxUseTime(ItemStack var1, LivingEntity var2);

   @Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", at = @At("HEAD"), cancellable = true)
   private void hookUse(World world2, PlayerEntity player2, Hand hand, CallbackInfoReturnable callbackInfoReturnable) {
      UseEvent2 useEvent2 = new UseEvent2();
      EventBusImpl.INSTANCE.rC(useEvent2);
      if (useEvent2.aik()) {
         callbackInfoReturnable.cancel();
         ItemStack stack = player2.getStackInHand(hand);
         if (stack.getDamage() >= stack.getMaxDamage() - 1) {
            callbackInfoReturnable.setReturnValue(TypedActionResult.fail(stack));
            return;
         }

         player2.setCurrentHand(hand);
         callbackInfoReturnable.setReturnValue(TypedActionResult.consume(stack));
      }
   }

   @Inject(method = "onStoppedUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V", at = @At("HEAD"), cancellable = true)
   private void hookOnStoppedUsing(ItemStack stack, World world2, LivingEntity living, int n, CallbackInfo callbackInfo) {
      if (living instanceof PlayerEntity) {
         int n2 = this.getMaxUseTime(stack, living) - n;
         OnStoppedUsingEvent onStoppedUsingEvent = new OnStoppedUsingEvent();
         EventBusImpl.INSTANCE.rC(onStoppedUsingEvent);
         if (onStoppedUsingEvent.aik() || n2 >= 10) {
            UseEvent2 useEvent2 = new UseEvent2();
            EventBusImpl.INSTANCE.rC(useEvent2);
            if (useEvent2.aik()) {
               callbackInfo.cancel();
               if (n2 >= 10) {
                  float f = EnchantmentHelper.getTridentSpinAttackStrength(stack, living);
                  if ((!(f > 0.0F) || ((PlayerEntity)living).isTouchingWaterOrRain()) && !isAboutToBreak(stack)) {
                     RegistryEntry registryEntry = EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.TRIDENT_SOUND)
                        .orElse(SoundEvents.ITEM_TRIDENT_THROW);
                     if (!world2.isClient) {
                        stack.damage(1, living, LivingEntity.getSlotForHand(living.getActiveHand()));
                        if (f == 0.0F) {
                           TridentEntity tridentEntity = new TridentEntity(world2, living, stack);
                           tridentEntity.setVelocity(living, ((PlayerEntity)living).getPitch(), ((PlayerEntity)living).getYaw(), 0.0F, 2.5F, 1.0F);
                           if (((PlayerEntity)living).isInCreativeMode()) {
                              tridentEntity.pickupType = PickupPermission.CREATIVE_ONLY;
                           }

                           world2.spawnEntity(tridentEntity);
                           world2.playSoundFromEntity(null, tridentEntity, (SoundEvent)registryEntry.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                           if (!((PlayerEntity)living).isInCreativeMode()) {
                              ((PlayerEntity)living).getInventory().removeOne(stack);
                           }
                        }
                     }

                     Stat stat2 = Stats.USED.getOrCreateStat((TridentItem)(Object)this);
                     ((PlayerEntity)living).incrementStat(stat2);
                     if (f > 0.0F) {
                        float f2 = ((PlayerEntity)living).getYaw();
                        float f3 = ((PlayerEntity)living).getPitch();
                        float f4 = -MathHelper.sin(f2 * (float) (Math.PI / 180.0)) * MathHelper.cos(f3 * (float) (Math.PI / 180.0));
                        float f5 = -MathHelper.sin(f3 * (float) (Math.PI / 180.0));
                        float f6 = MathHelper.cos(f2 * (float) (Math.PI / 180.0)) * MathHelper.cos(f3 * (float) (Math.PI / 180.0));
                        float f7 = MathHelper.sqrt(f4 * f4 + f5 * f5 + f6 * f6);
                        f4 *= f / f7;
                        f5 *= f / f7;
                        f6 *= f / f7;
                        double temp = f4;
                        double temp2 = f5;
                        double d = f6;
                        double d2 = temp2;
                        double d3 = temp;
                        ((PlayerEntity)living).addVelocity(d3, d2, d);
                        ItemStack stack2 = stack;
                        float f8 = 8.0F;
                        byte b = 20;
                        ((PlayerEntity)living).useRiptide(b, f8, stack2);
                        if (((PlayerEntity)living).isOnGround()) {
                           MovementType movementType = MovementType.SELF;
                           Vec3d vec = new Vec3d(0.0, 1.1999999F, 0.0);
                           MovementType movementType2 = movementType;
                           ((PlayerEntity)living).move(movementType2, vec);
                        }

                        world2.playSoundFromEntity(null, living, (SoundEvent)registryEntry.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                     }
                  }
               }
            }
         }
      }
   }
}
