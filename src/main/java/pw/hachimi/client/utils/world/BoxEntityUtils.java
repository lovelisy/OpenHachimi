package pw.hachimi.client.utils.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.entity.EntityLookup;
import net.minecraft.world.entity.EntityTrackingSection;
import net.minecraft.world.entity.SectionedEntityCache;
import net.minecraft.world.entity.SimpleEntityLookup;
import pw.hachimi.client.Mc;
import pw.hachimi.client.mixin.EntityTrackingSectionMixin;
import pw.hachimi.client.mixin.SectionedEntityCacheMixin;
import pw.hachimi.client.mixin.SimpleEntityLookupMixin;
import pw.hachimi.client.mixin.WorldMixin;

public class BoxEntityUtils {
   public static float kE(Entity entity2) {
      return entity2 instanceof LivingEntity ? ((LivingEntity)entity2).getHealth() + ((LivingEntity)entity2).getAbsorptionAmount() : 0.0F;
   }

   public static boolean kF(Entity entity2) {
      boolean flag;
      if (entity2 instanceof Monster && !kG(entity2)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static boolean kG(Entity entity2) {
      boolean flag;
      if ((!(entity2 instanceof EndermanEntity) || ((EndermanEntity)entity2).isAttacking())
         && (!(entity2 instanceof ZombifiedPiglinEntity) || ((ZombifiedPiglinEntity)entity2).isAttacking())
         && (!(entity2 instanceof WolfEntity) || ((WolfEntity)entity2).isAttacking())
         && (!(entity2 instanceof IronGolemEntity) || ((IronGolemEntity)entity2).isAttacking())
         && (!(entity2 instanceof BeeEntity) || ((BeeEntity)entity2).isAttacking())) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public static boolean kH(Entity entity2) {
      boolean flag;
      if (!(entity2 instanceof EndermanEntity) && !(entity2 instanceof ZombifiedPiglinEntity) && !(entity2 instanceof WolfEntity) && !(entity2 instanceof IronGolemEntity)) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public static boolean kI(Entity entity2) {
      boolean flag;
      if (!(entity2 instanceof PassiveEntity) && !(entity2 instanceof AmbientEntity) && !(entity2 instanceof SquidEntity)) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public static boolean kJ(Entity entity2) {
      boolean flag;
      if (!(entity2 instanceof BoatEntity)
         && !(entity2 instanceof MinecartEntity)
         && !(entity2 instanceof FurnaceMinecartEntity)
         && !(entity2 instanceof ChestMinecartEntity)) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public static boolean kK(Box box, Predicate predicate) {
      EntityLookup entityLookup = ((WorldMixin)Mc.client.world).getEntityLookup();
      if (!(entityLookup instanceof SimpleEntityLookup)) {
         AtomicBoolean atomicBoolean = a();
         EntityLookup temp = entityLookup;
         AtomicBoolean temp2 = atomicBoolean;
         final Predicate predicate2 = predicate;
         final AtomicBoolean atomicBoolean2 = temp2;
         Consumer consumer = new Consumer() {
            @Override
            public void accept(Object predicate) {
               BoxEntityUtils.kL(atomicBoolean2, predicate2, (Entity)predicate);
            }
         };
         temp.forEachIntersects(box, consumer);
         return atomicBoolean.get();
      }

      SectionedEntityCache sectionedEntityCache = ((SimpleEntityLookupMixin)((SimpleEntityLookup)entityLookup)).getCache();
      LongSortedSet longSortedSet = ((SectionedEntityCacheMixin)sectionedEntityCache).getTrackedPositions();
      Long2ObjectMap long2ObjectMap = ((SectionedEntityCacheMixin)sectionedEntityCache).getTrackingSections();
      int n = ChunkSectionPos.getSectionCoord(box.minX - 2.0);
      int n2 = ChunkSectionPos.getSectionCoord(box.minY - 2.0);
      int n3 = ChunkSectionPos.getSectionCoord(box.minZ - 2.0);
      int n4 = ChunkSectionPos.getSectionCoord(box.maxX + 2.0);
      int n5 = ChunkSectionPos.getSectionCoord(box.maxY + 2.0);
      int n6 = ChunkSectionPos.getSectionCoord(box.maxZ + 2.0);

      for (int n7 = n; n7 <= n4; n7++) {
         long l = ChunkSectionPos.asLong(n7, 0, 0);
         long l2 = ChunkSectionPos.asLong(n7, -1, -1);
         LongBidirectionalIterator longBidirectionalIterator = longSortedSet.subSet(l, l2 + 1L).iterator();

         while (longBidirectionalIterator.hasNext()) {
            long l3 = longBidirectionalIterator.nextLong();
            int n8 = ChunkSectionPos.unpackY(l3);
            int n9 = ChunkSectionPos.unpackZ(l3);
            if (n8 >= n2 && n8 <= n5 && n9 >= n3 && n9 <= n6) {
               EntityTrackingSection entityTrackingSection = (EntityTrackingSection)long2ObjectMap.get(l3);
               if (entityTrackingSection != null && entityTrackingSection.getStatus().shouldTrack()) {
                  Iterator iterator2 = ((EntityTrackingSectionMixin)entityTrackingSection).getCollection().iterator();

                  while (iterator2.hasNext()) {
                     Entity entity2 = (Entity)iterator2.next();
                     if (entity2.getBoundingBox().intersects(box) && predicate.test(entity2)) {
                        return true;
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   static void kL(AtomicBoolean atomicBoolean, Predicate predicate, Entity entity2) {
      if (!atomicBoolean.get() && predicate.test(entity2)) {
         atomicBoolean.set(true);
      }
   }

   private static AtomicBoolean a() {
      return new AtomicBoolean(false);
   }
}
