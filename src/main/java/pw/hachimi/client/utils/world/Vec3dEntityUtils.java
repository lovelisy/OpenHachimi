package pw.hachimi.client.utils.world;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ArmorItem.Type;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import org.apache.commons.lang3.mutable.MutableInt;
import pw.hachimi.client.Mc;
import pw.hachimi.client.model.world.AllBlastMode;
import pw.hachimi.client.model.world.StartEnd;
import pw.hachimi.client.model.world.UnidentifiedInterface17;
import java.util.function.BiFunction;
import net.minecraft.entity.attribute.EntityAttributes;
import pw.hachimi.client.config.ConfigContainer;
import pw.hachimi.client.utils.world.EntityVec3dUtils;
import pw.hachimi.client.utils.world.UnidentifiedHelper23;

public class Vec3dEntityUtils {
   public static double vK(Entity entity2, Vec3d vec, boolean flag) {
      return vL(entity2, vec, flag);
   }

   public static double vL(Entity entity2, Vec3d vec, boolean flag) {
      return vX(entity2, vec, false, 12.0F, 0, flag);
   }

   public static double vM(Entity entity2, Vec3d vec, boolean flag, int n, boolean flag2) {
      return vX(entity2, vec, flag, 12.0F, n, flag2);
   }

   public static double vN(Entity entity2, Vec3d vec, boolean flag, int n, boolean flag2, BlockPos pos) {
      return vW(entity2, vec, flag, n, flag2, pos);
   }

   public static double vO(long l, Entity entity2, Vec3d vec, AllBlastMode allBlastMode, boolean flag) {
      long temp = 40967044983599L ^ l ^ 95775599744135L;
      int n = (int)((40967044983599L ^ l ^ 95775599744135L) >>> 48);
      int n2 = (int)((40967044983599L ^ l ^ 95775599744135L) << 16 >>> 32);
      int n3 = (int)(temp << 48 >>> 48);
      return vQ((short)n, entity2, vec, allBlastMode, 12.0F, n2, (char)n3, 0, flag);
   }

   public static double vP(Entity entity2, Vec3d vec, AllBlastMode allBlastMode, int n, boolean flag, long l) {
      long temp = 40967044983599L ^ l ^ 28838512845505L;
      int n2 = (int)((40967044983599L ^ l ^ 28838512845505L) >>> 48);
      int n3 = (int)((40967044983599L ^ l ^ 28838512845505L) << 16 >>> 32);
      int n4 = (int)(temp << 48 >>> 48);
      return vQ((short)n2, entity2, vec, allBlastMode, 12.0F, n3, (char)n4, n, flag);
   }

   public static double vQ(short s, Entity entity2, Vec3d vec, AllBlastMode allBlastMode, float f6, int n, char c, int n2, boolean flag) {
      double d2 = entity2.getX();
      double d3 = entity2.getY();
      double d4 = entity2.getZ();
      Vec3d vec2 = EntityVec3dUtils.nT(entity2, n2);
      double d5 = Math.sqrt((new Vec3d(d2, d3, d4)).squaredDistanceTo(vec));
      double d6 = wg(vec, entity2.getBoundingBox().offset(vec2), allBlastMode);
      double d7 = d5 / f6;
      double d8 = (1.0 - d7) * d6;
      double d9 = (float)(int)((d8 * d8 + d8) / 2.0 * 7.0 * 12.0 + 1.0);
      d9 = vZ(entity2, Mc.client.world.getDamageSources().explosion((Explosion)null), d9, flag);
      return Math.max(0.0, d9);
   }

   public static double vR(Entity entity2, Vec3d vec, boolean flag, Set set, boolean flag2) {
      return vT(entity2, vec, flag, 12.0F, set, 0, flag2);
   }

   public static double vS(Entity entity2, Vec3d vec, boolean flag, Set set, int n, boolean flag2) {
      return vT(entity2, vec, flag, 12.0F, set, n, flag2);
   }

   public static double vT(Entity entity2, Vec3d vec, boolean flag, float f6, Set set, int n, boolean flag2) {
      double d2 = entity2.getX();
      double d3 = entity2.getY();
      double d4 = entity2.getZ();
      Vec3d vec2 = EntityVec3dUtils.nT(entity2, n);
      double d5 = Math.sqrt((new Vec3d(d2, d3, d4)).squaredDistanceTo(vec));
      Box box = entity2.getBoundingBox().offset(vec2);
      Box box2;
      AllBlastMode allBlastMode;
      if (flag) {
         Box temp = box;
         allBlastMode = AllBlastMode.BLAST;
         box2 = temp;
      } else {
         Box box3 = box;
         allBlastMode = AllBlastMode.NONE;
         box2 = box3;
      }

      double d6 = we(vec, box2, allBlastMode, set);
      double d7 = d5 / f6;
      double d8 = (1.0 - d7) * d6;
      double d9 = (int)((d8 * d8 + d8) / 2.0 * 7.0 * 12.0 + 1.0);
      d9 = vZ(entity2, Mc.client.world.getDamageSources().explosion((Explosion)null), d9, flag2);
      return Math.max(0.0, d9);
   }

   public static double vU(long l, Entity entity2, Vec3d vec, AllBlastMode allBlastMode, Set set, boolean flag) {
      long temp = 40967044983599L ^ l ^ 113554525153367L;
      int temp2 = (int)((40967044983599L ^ l ^ 113554525153367L) >>> 48);
      int n = (int)((40967044983599L ^ l ^ 113554525153367L) << 16 >>> 48);
      int n2 = (int)(temp << 32 >>> 32);
      return vV(entity2, vec, (short)n, n2, allBlastMode, 12.0F, set, 0, flag);
   }

   public static double vV(Entity entity2, Vec3d vec, short s, int n, AllBlastMode allBlastMode, float f6, Set set, int n2, boolean flag) {
      double d2 = entity2.getX();
      double d3 = entity2.getY();
      double d4 = entity2.getZ();
      Vec3d vec2 = EntityVec3dUtils.nT(entity2, n2);
      double d5 = Math.sqrt((new Vec3d(d2, d3, d4)).squaredDistanceTo(vec));
      double d6 = we(vec, entity2.getBoundingBox().offset(vec2), allBlastMode, set);
      double d7 = d5 / f6;
      double d8 = (1.0 - d7) * d6;
      double d9 = (float)(int)((d8 * d8 + d8) / 2.0 * 7.0 * 12.0 + 1.0);
      d9 = vZ(entity2, Mc.client.world.getDamageSources().explosion((Explosion)null), d9, flag);
      return Math.max(0.0, d9);
   }

   public static double vW(Entity entity2, Vec3d vec, boolean flag, int n, boolean flag2, BlockPos pos) {
      double d2 = entity2.getX();
      double d3 = entity2.getY();
      double d4 = entity2.getZ();
      Vec3d vec2 = EntityVec3dUtils.nT(entity2, n);
      double d5 = Math.sqrt((new Vec3d(d2, d3, d4)).squaredDistanceTo(vec));
      Box box = entity2.getBoundingBox().offset(vec2);
      Box box2;
      AllBlastMode allBlastMode;
      if (flag) {
         Box temp = box;
         allBlastMode = AllBlastMode.BLAST;
         box2 = temp;
      } else {
         Box box3 = box;
         allBlastMode = AllBlastMode.NONE;
         box2 = box3;
      }

      double d6 = wf(vec, box2, allBlastMode, pos);
      double d7 = d5 / 12.0;
      double d8 = (1.0 - d7) * d6;
      double d9 = (int)((d8 * d8 + d8) / 2.0 * 7.0 * 12.0 + 1.0);
      d9 = vZ(entity2, Mc.client.world.getDamageSources().explosion((Explosion)null), d9, flag2);
      return Math.max(0.0, d9);
   }

   public static double vX(Entity entity2, Vec3d vec, boolean flag, float f6, int n, boolean flag2) {
      double d2 = entity2.getX();
      double d3 = entity2.getY();
      double d4 = entity2.getZ();
      Vec3d vec2 = EntityVec3dUtils.nT(entity2, n);
      double d5 = Math.sqrt((new Vec3d(d2, d3, d4)).squaredDistanceTo(vec));
      Box box = entity2.getBoundingBox().offset(vec2);
      Box box2;
      AllBlastMode allBlastMode;
      if (flag) {
         Box temp = box;
         allBlastMode = AllBlastMode.BLAST;
         box2 = temp;
      } else {
         Box box3 = box;
         allBlastMode = AllBlastMode.NONE;
         box2 = box3;
      }

      double d6 = wg(vec, box2, allBlastMode);
      double d7 = d5 / f6;
      double d8 = (1.0 - d7) * d6;
      double d9 = (int)((d8 * d8 + d8) / 2.0 * 7.0 * 12.0 + 1.0);
      d9 = vZ(entity2, Mc.client.world.getDamageSources().explosion((Explosion)null), d9, flag2);
      return Math.max(0.0, d9);
   }

   public static double vY(Vec3d vec, Entity entity2, Vec3d vec2, boolean flag, boolean flag2) {
      Box box = entity2.getBoundingBox();
      double d2 = vec.getX() - box.minX;
      double d3 = vec.getY() - box.minY;
      double d4 = vec.getZ() - box.minZ;
      Box box2 = box.offset(d2, d3, d4);
      AllBlastMode allBlastMode;
      if (flag) {
         allBlastMode = AllBlastMode.BLAST;
      } else {
         allBlastMode = AllBlastMode.NONE;
      }

      UnidentifiedInterface17 unidentifiedInterface17 = wk(allBlastMode);
      double d5 = wh(vec2, box2, unidentifiedInterface17);
      double d6 = Math.sqrt(vec.squaredDistanceTo(vec2)) / 12.0;
      double d7 = (1.0 - d6) * d5;
      double d8 = (int)((d7 * d7 + d7) / 2.0 * 7.0 * 12.0 + 1.0);
      d8 = vZ(entity2, Mc.client.world.getDamageSources().explosion((Explosion)null), d8, flag2);
      return Math.max(0.0, d8);
   }

   // restored with CFR (compiles, call/field/constant fingerprint equals the deobf jar)
   private static double vZ(Entity entity, DamageSource damageSource, double d, boolean bl) {
       if (damageSource.isScaledWithDifficulty()) {
           switch (UnidentifiedHelper23.a[Mc.client.world.getDifficulty().ordinal()]) {
               case 2: {
                   d *= 1.5;
               }
               default: {
                   break;
               }
               case 1: {
                   d = Math.min(d / 2.0 + 1.0, d);
               }
           }
       }
       if (entity instanceof LivingEntity) {
           float f;
           float f2;
           LivingEntity livingEntity = (LivingEntity)entity;
           try {
               RegistryEntry registryEntry = EntityAttributes.GENERIC_ARMOR_TOUGHNESS;
               f2 = (float)((LivingEntity)entity).getAttributeValue(registryEntry);
               f = ((LivingEntity)entity).getArmor();
           }
           catch (Exception exception) {
               exception.printStackTrace();
               f = 20.0f;
               f2 = 12.0f;
           }
           float f3 = f2;
           float f4 = f;
           float f5 = (float)d;
           d = DamageUtil.getDamageLeft((LivingEntity)livingEntity, (float)f5, (DamageSource)damageSource, (float)f4, (float)f3);
           d = Vec3dEntityUtils.wc(livingEntity, d);
           d = Vec3dEntityUtils.wa((Entity)livingEntity, d, bl);
       }
       return Math.max(d, 0.0);
   }

   private static float wa(Entity entity2, double d2, boolean flag) {
      if (entity2 instanceof LivingEntity) {
         float f6 = wb(((LivingEntity)entity2).getArmorItems(), flag);
         return DamageUtil.getInflictedDamage((float)d2, f6);
      } else {
         return 0.0F;
      }
   }

   private static float wb(Iterable iterable, boolean flag) {
      MutableInt mutableInt = new MutableInt();
      final MutableInt mutableInt2 = mutableInt;
      final boolean flag2 = flag;
      Consumer consumer = new Consumer() {
         @Override
         public void accept(Object flag) {
            Vec3dEntityUtils.ww(flag2, mutableInt2, (ItemStack)flag);
         }
      };
      iterable.forEach(consumer);
      return mutableInt.intValue();
   }

   private static double wc(LivingEntity living, double d2) {
      RegistryEntry registryEntry2 = StatusEffects.RESISTANCE;
      StatusEffectInstance statusEffectInstance = living.getStatusEffect(registryEntry2);
      if (statusEffectInstance != null) {
         int n = statusEffectInstance.getAmplifier() + 1;
         d2 *= 1.0F - n * 0.2F;
      }

      return Math.max(d2, 0.0);
   }

   private static DefaultAttributeContainer wd(LivingEntity living) {
      return DefaultAttributeRegistry.get((EntityType)living.getType());
   }

   private static float we(Vec3d vec, Box box, AllBlastMode allBlastMode, Set set) {
      UnidentifiedInterface17 unidentifiedInterface17 = wj(allBlastMode, set);
      return wh(vec, box, unidentifiedInterface17);
   }

   private static float wf(Vec3d vec, Box box, AllBlastMode allBlastMode, BlockPos pos) {
      UnidentifiedInterface17 unidentifiedInterface17 = wi(allBlastMode, pos);
      return wh(vec, box, unidentifiedInterface17);
   }

   private static float wg(Vec3d vec, Box box, AllBlastMode allBlastMode) {
      UnidentifiedInterface17 unidentifiedInterface17 = wk(allBlastMode);
      return wh(vec, box, unidentifiedInterface17);
   }

   private static float wh(Vec3d vec, Box box, UnidentifiedInterface17 unidentifiedInterface17) {
      double d2 = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
      double d3 = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
      double d4 = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);
      double d5 = (1.0 - Math.floor(1.0 / d2) * d2) / 2.0;
      double d6 = (1.0 - Math.floor(1.0 / d4) * d4) / 2.0;
      int n = 0;
      int n2 = 0;
      double d7 = 1.0 / d2;
      double d8 = 1.0 / d3;
      double d9 = 1.0 / d4;

      for (int n3 = 0; n3 <= d7; n3++) {
         double d10 = MathHelper.lerp(n3 * d2, box.minX, box.maxX);

         for (int n4 = 0; n4 <= d8; n4++) {
            double d11 = MathHelper.lerp(n4 * d3, box.minY, box.maxY);

            for (int n5 = 0; n5 <= d9; n5++) {
               double d12 = MathHelper.lerp(n5 * d4, box.minZ, box.maxZ);
               if (wl(new StartEnd(new Vec3d(d10 + d5, d11, d12 + d6), vec), unidentifiedInterface17) == null) {
                  n2++;
               }

               n++;
            }
         }
      }

      return (float)n2 / n;
   }

   private static UnidentifiedInterface17 wi(AllBlastMode allBlastMode, BlockPos pos) {
      if (allBlastMode == AllBlastMode.BLAST) {
         final BlockPos pos2 = pos;
         return new UnidentifiedInterface17() {
            @Override
            public Object apply(Object pos, Object var2x) {
               return Vec3dEntityUtils.wv(pos2, (StartEnd)pos, (BlockPos)var2x);
            }
         };
      }

      if (allBlastMode == AllBlastMode.ALL) {
         return new UnidentifiedInterface17() {
            @Override
            public Object apply(Object pos, Object pos2) {
               return Vec3dEntityUtils.wu((StartEnd)pos, (BlockPos)pos2);
            }
         };
      }

      final BlockPos pos3 = pos;
      return new UnidentifiedInterface17() {
         @Override
         public Object apply(Object pos, Object pos2) {
            return Vec3dEntityUtils.wt(pos3, (StartEnd)pos, (BlockPos)pos2);
         }
      };
   }

   private static UnidentifiedInterface17 wj(AllBlastMode allBlastMode, Set set) {
      if (allBlastMode == AllBlastMode.BLAST) {
         final Set set2 = set;
         return new UnidentifiedInterface17() {
            @Override
            public Object apply(Object set, Object var2x) {
               return Vec3dEntityUtils.ws(set2, (StartEnd)set, (BlockPos)var2x);
            }
         };
      }

      if (allBlastMode == AllBlastMode.ALL) {
         return new UnidentifiedInterface17() {
            @Override
            public Object apply(Object set, Object set2) {
               return Vec3dEntityUtils.wr((StartEnd)set, (BlockPos)set2);
            }
         };
      }

      final Set set3 = set;
      return new UnidentifiedInterface17() {
         @Override
         public Object apply(Object set, Object set2) {
            return Vec3dEntityUtils.wq(set3, (StartEnd)set, (BlockPos)set2);
         }
      };
   }

   private static UnidentifiedInterface17 wk(AllBlastMode allBlastMode) {
      if (allBlastMode == AllBlastMode.BLAST) {
         return new UnidentifiedInterface17() {
            @Override
            public Object apply(Object obj, Object obj2) {
               return Vec3dEntityUtils.wp((StartEnd)obj, (BlockPos)obj2);
            }
         };
      } else {
         return allBlastMode == AllBlastMode.ALL ? new UnidentifiedInterface17() {
            @Override
            public Object apply(Object obj, Object obj2) {
               return Vec3dEntityUtils.wo((StartEnd)obj, (BlockPos)obj2);
            }
         } : new UnidentifiedInterface17() {
            @Override
            public Object apply(Object obj, Object obj2) {
               return Vec3dEntityUtils.wn((StartEnd)obj, (BlockPos)obj2);
            }
         };
      }
   }

   private static BlockHitResult wl(StartEnd startEnd, UnidentifiedInterface17 unidentifiedInterface17) {
      Vec3d temp = startEnd.start();
      Vec3d temp2 = startEnd.getEnd();
      Function function2 = new Function() {
         @Override
         public Object apply(Object unidentifiedInterface17) {
            return Vec3dEntityUtils.wm((StartEnd)unidentifiedInterface17);
         }
      };
      Vec3d vec = temp2;
      return (BlockHitResult)BlockView.raycast(temp, vec, startEnd, unidentifiedInterface17, function2);
   }

   static BlockHitResult wm(StartEnd startEnd) {
      return null;
   }

   static BlockHitResult wn(StartEnd startEnd, BlockPos pos) {
      BlockState temp = Mc.client.world.getBlockState(pos);
      ClientWorld world2 = Mc.client.world;
      VoxelShape voxelShape = temp.getCollisionShape((BlockView)world2, pos);
      Vec3d vec = startEnd.getEnd();
      Vec3d vec2 = startEnd.start();
      return voxelShape.raycast(vec2, vec, pos);
   }

   static BlockHitResult wo(StartEnd startEnd, BlockPos pos) {
      return null;
   }

   static BlockHitResult wp(StartEnd startEnd, BlockPos pos) {
      BlockState state = Mc.client.world.getBlockState(pos);
      if (state.getBlock().getBlastResistance() < 600.0F) {
         return null;
      }

      BlockState temp = state;
      ClientWorld world2 = Mc.client.world;
      VoxelShape voxelShape = temp.getCollisionShape((BlockView)world2, pos);
      Vec3d vec = startEnd.getEnd();
      Vec3d vec2 = startEnd.start();
      return voxelShape.raycast(vec2, vec, pos);
   }

   static BlockHitResult wq(Set set, StartEnd startEnd, BlockPos pos) {
      if (set.contains(pos)) {
         return null;
      }

      BlockState temp = Mc.client.world.getBlockState(pos);
      ClientWorld world2 = Mc.client.world;
      VoxelShape voxelShape = temp.getCollisionShape((BlockView)world2, pos);
      Vec3d vec = startEnd.getEnd();
      Vec3d vec2 = startEnd.start();
      return voxelShape.raycast(vec2, vec, pos);
   }

   static BlockHitResult wr(StartEnd startEnd, BlockPos pos) {
      return null;
   }

   static BlockHitResult ws(Set set, StartEnd startEnd, BlockPos pos) {
      if (set.contains(pos)) {
         return null;
      }

      BlockState state = Mc.client.world.getBlockState(pos);
      if (state.getBlock().getBlastResistance() < 600.0F) {
         return null;
      }

      BlockState temp = state;
      ClientWorld world2 = Mc.client.world;
      VoxelShape voxelShape = temp.getCollisionShape((BlockView)world2, pos);
      Vec3d vec = startEnd.getEnd();
      Vec3d vec2 = startEnd.start();
      return voxelShape.raycast(vec2, vec, pos);
   }

   static BlockHitResult wt(BlockPos pos, StartEnd startEnd, BlockPos pos2) {
      BlockState state = Mc.client.world.getBlockState(pos2);
      if (pos.equals(pos2)) {
         state = Blocks.OBSIDIAN.getDefaultState();
      }

      BlockState temp = state;
      ClientWorld world2 = Mc.client.world;
      VoxelShape voxelShape = temp.getCollisionShape((BlockView)world2, pos2);
      Vec3d vec = startEnd.getEnd();
      Vec3d vec2 = startEnd.start();
      return voxelShape.raycast(vec2, vec, pos2);
   }

   static BlockHitResult wu(StartEnd startEnd, BlockPos pos) {
      return null;
   }

   static BlockHitResult wv(BlockPos pos, StartEnd startEnd, BlockPos pos2) {
      BlockState state = Mc.client.world.getBlockState(pos2);
      if (pos.equals(pos2)) {
         state = Blocks.OBSIDIAN.getDefaultState();
      }

      if (state.getBlock().getBlastResistance() < 600.0F) {
         return null;
      }

      BlockState temp = state;
      ClientWorld world2 = Mc.client.world;
      VoxelShape voxelShape = temp.getCollisionShape((BlockView)world2, pos2);
      Vec3d vec = startEnd.getEnd();
      Vec3d vec2 = startEnd.start();
      return voxelShape.raycast(vec2, vec, pos2);
   }

   static void ww(boolean flag, MutableInt mutableInt, ItemStack stack) {
      if (flag) {
         Item item2 = stack.getItem();
         byte b;
         if (item2 instanceof ArmorItem && ((ArmorItem)item2).getType() == Type.LEGGINGS) {
            b = 8;
         } else {
            b = 4;
         }

         mutableInt.add(b);
      } else {
         DynamicRegistryManager temp = Mc.client.world.getRegistryManager();
         RegistryKey registryKey = Enchantments.BLAST_PROTECTION.getRegistryRef();
         Registry registry2 = temp.get(registryKey);
         RegistryKey registryKey2 = Enchantments.BLAST_PROTECTION;
         int n = EnchantmentHelper.getLevel((RegistryEntry)registry2.getEntry(registryKey2).get(), stack);
         temp = Mc.client.world.getRegistryManager();
         RegistryKey registryKey3 = Enchantments.PROTECTION.getRegistryRef();
         Registry registry3 = temp.get(registryKey3);
         RegistryKey registryKey4 = Enchantments.PROTECTION;
         int n2 = EnchantmentHelper.getLevel((RegistryEntry)registry3.getEntry(registryKey4).get(), stack);
         mutableInt.add(n * 2 + n2);
      }
   }

   private static Vec3d a(double d2, double d3, double d4) {
      return new Vec3d(d2, d3, d4);
   }

   private static Exception a(Exception exception2) {
      return exception2;
   }
}
