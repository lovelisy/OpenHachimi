package pw.hachimi.client.module.misc;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.invoke.MethodHandles.Lookup;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import pw.hachimi.client.utils.world.BoxUtils;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.world.RemoveEntityEvent;
import pw.hachimi.client.mixin.StatusEffectInstanceMixin;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class PotionTracker extends ClientModule {
   private static PotionTracker INSTANCE;
   public static Map map = new HashMap();
   Setting radar = this.registerSetting(new BooleanSetting("Radar", "Show potion duration on TextRadar", true));
   Setting nametags = this.registerSetting(new BooleanSetting("Nametags", "Show potion duration on Nametags", true));
   Setting versionFix = this.registerSetting(new BooleanSetting("VersionFix", "On 1.21.6+", true, PotionTracker::isVersionFixVisible));
   Map map2 = new ConcurrentHashMap();
   private static final long a = 0L;
   private static final String[] b = null;
   private static final String[] d = null;
   private static final Map e = new HashMap();

   public PotionTracker() {
      super("PotionTracker", "Prediction of thrown potion effects", ModuleCategory.MISC);
      INSTANCE = this;
   }

   public static PotionTracker Ec() {
      return INSTANCE;
   }

   @EventListener
   public void Ed(TickEntitiesPreEvent tickEntitiesPreEvent) {
      Iterator iterator2 = client.world.getPlayers().iterator();

      while (iterator2.hasNext()) {
         Object obj = iterator2.next();
         if (((PlayerEntity)obj).getMainHandStack().getItem() == Items.GLASS_BOTTLE) {
            Map temp = map;
            Integer integer = ((PlayerEntity)obj).getId();
            Object obj2 = temp.get(integer);
            if (obj2 != null) {
               Iterator iterator3 = ((Iterable)obj2).iterator();

               while (iterator3.hasNext()) {
                  Object obj3 = iterator3.next();
                  if (!((StatusEffect)((StatusEffectInstance)obj3).getEffectType().value()).isInstant()) {
                     temp = this.map2;
                     ArrayList arrayList = new ArrayList();
                     Object obj4 = temp.getOrDefault(obj, arrayList);
                     ((List)obj4).add(obj3);
                     this.map2.put(obj, obj4);
                  }
               }
            }
         }
      }

      map.clear();
      iterator2 = client.world.getPlayers().iterator();

      while (iterator2.hasNext()) {
         Object obj5 = iterator2.next();
         if (((PlayerEntity)obj5).getMainHandStack().getItem() == Items.POTION) {
            Map map3 = map;
            Integer temp2 = ((PlayerEntity)obj5).getId();
            Iterable iterable = this.El(((PlayerEntity)obj5).getMainHandStack());
            Integer integer2 = temp2;
            map3.put(integer2, iterable);
         }
      }

      Collection collection = this.map2.values();
      Consumer consumer = new Consumer() {
         @Override
         public void accept(Object tickEntitiesPreEvent) {
            PotionTracker.En((List)tickEntitiesPreEvent);
         }
      };
      collection.forEach(consumer);
      collection = this.map2.values();
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object tickEntitiesPreEvent) {
            return ((List)tickEntitiesPreEvent).isEmpty();
         }
      };
      collection.removeIf(predicate);
   }

   @Override
   public void awe() {
      this.map2.clear();
      map.clear();
   }

   public boolean Ee() {
      boolean flag;
      if (this.awg() && (Boolean)this.nametags.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean Ef() {
      boolean flag;
      if (this.awg() && (Boolean)this.radar.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public Map getMap2() {
      return this.map2;
   }

   public String Eh(PlayerEntity player2) {
      StringBuilder stringBuilder = new StringBuilder();
      List list = (List)this.map2.get(player2);
      if (list != null && !list.isEmpty()) {
         HashMap hashMap = new HashMap();
         Iterator iterator2 = list.iterator();

         while (iterator2.hasNext()) {
            StatusEffectInstance statusEffectInstance = (StatusEffectInstance)iterator2.next();
            HashMap temp = hashMap;
            RegistryEntry registryEntry = statusEffectInstance.getEffectType();
            StatusEffectInstance statusEffectInstance2 = (StatusEffectInstance)temp.get(registryEntry);
            if (statusEffectInstance2 == null
               || statusEffectInstance.getAmplifier() >= statusEffectInstance2.getAmplifier() && (statusEffectInstance.getAmplifier() != statusEffectInstance2.getAmplifier() || statusEffectInstance.getDuration() >= statusEffectInstance2.getDuration())) {
               temp = hashMap;
               RegistryEntry registryEntry2 = statusEffectInstance.getEffectType();
               temp.put(registryEntry2, statusEffectInstance);
            }
         }

         iterator2 = hashMap.values().iterator();

         while (iterator2.hasNext()) {
            StatusEffectInstance statusEffectInstance3 = (StatusEffectInstance)iterator2.next();
            if (statusEffectInstance3.getEffectType() == StatusEffects.RESISTANCE) {
               stringBuilder.append("§9");
               StringBuilder stringBuilder2 = stringBuilder;
               DecimalFormat temp2 = bo;
               double d2 = statusEffectInstance3.getDuration() / 20.0;
               String str = temp2.format(d2);
               stringBuilder2.append(str);
               stringBuilder.append(" §r");
            }
         }
      }

      return stringBuilder.toString();
   }

   @EventListener
   public void onRemoveEntity(RemoveEntityEvent removeEntityEvent) {
      if (client.world != null) {
         Entity entity2 = removeEntityEvent.getEntity();
         if (entity2 instanceof PotionEntity) {
            PotionEntity potionEntity = (PotionEntity)entity2;
            Vec3d vec = potionEntity.getPos();
            Box box = potionEntity.getBoundingBox().expand(4.0, 2.0, 4.0);
            List list = client.world.getNonSpectatingEntities(LivingEntity.class, box);
            Iterator iterator2 = list.iterator();

            while (iterator2.hasNext()) {
               LivingEntity living = (LivingEntity)iterator2.next();
               if (living.isAffectedBySplashPotions()) {
                  double d;
                  if ((Boolean)this.versionFix.atx()) {
                     Box box2 = potionEntity.getBoundingBox();
                     Box box3 = living.getBoundingBox().expand((double)Ej(potionEntity));
                     Vec3d vec2 = BoxUtils.jy(vec, box3);
                     d = Math.sqrt(box2.squaredMagnitude(vec2));
                  } else {
                     d = living.getPos().distanceTo(vec);
                  }

                  if (d < 4.0) {
                     Predicate predicate = obj -> ((Entity)obj).canHit();
                     HitResult hitResult = ProjectileUtil.getCollision(potionEntity, predicate);
                     Entity entity3 = null;
                     if (hitResult.getType() == Type.ENTITY) {
                        entity3 = ((EntityHitResult)hitResult).getEntity();
                     }

                     Iterable iterable = this.El(potionEntity.getStack());
                     if (iterable == null) {
                        return;
                     }

                     Iterator iterator3 = iterable.iterator();

                     while (iterator3.hasNext()) {
                        StatusEffectInstance statusEffectInstance = (StatusEffectInstance)iterator3.next();
                        RegistryEntry registryEntry = statusEffectInstance.getEffectType();
                        if (!((StatusEffect)registryEntry.value()).isInstant()) {
                           double d2 = entity3 != null && living == entity3 ? 1.0 : 1.0 - d / 4.0;
                           Int2IntFunction int2IntFunction = n -> Em(d2, n);
                           int n2 = statusEffectInstance.mapDuration(int2IntFunction);
                           StatusEffectInstance statusEffectInstance2 = new StatusEffectInstance(
                              registryEntry, n2, statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles()
                           );
                           if (!statusEffectInstance2.isDurationBelow(1)) {
                              List list2 = (List)this.map2.getOrDefault(living, new ArrayList());
                              list2.add(statusEffectInstance2);
                              this.map2.put(living, list2);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static float Ej(Entity entity2) {
      return Math.clamp((entity2.age - 2) / 20.0F, 0.0F, 0.3F);
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null && client.world != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof EntityStatusS2CPacket && ((EntityStatusS2CPacket)packet2).getStatus() == 35) {
            EntityStatusS2CPacket temp = (EntityStatusS2CPacket)packet2;
            ClientWorld world2 = client.world;
            Entity entity2 = temp.getEntity((World)world2);
            if (!(entity2 instanceof LivingEntity)) {
               return;
            }

            this.map2.remove(entity2);
         }
      }
   }

   private Iterable El(ItemStack stack) {
      PotionContentsComponent potionContentsComponent = PotionContentsComponent.DEFAULT;
      ComponentType componentType = DataComponentTypes.POTION_CONTENTS;
      PotionContentsComponent potionContentsComponent2 = (PotionContentsComponent)stack.getOrDefault(componentType, potionContentsComponent);
      return potionContentsComponent2.hasEffects() ? potionContentsComponent2.getEffects() : null;
   }

   static int Em(double d2, int n) {
      return (int)(d2 * n + 0.5);
   }

   static void En(List list) {
      list.forEach(obj -> Ep((StatusEffectInstance)obj));
      list.removeIf(obj -> Eo((StatusEffectInstance)obj));
   }

   static boolean Eo(StatusEffectInstance statusEffectInstance) {
      boolean flag;
      if (statusEffectInstance.getDuration() <= 0) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static void Ep(StatusEffectInstance statusEffectInstance) {
      ((StatusEffectInstanceMixin)statusEffectInstance).setDuration(statusEffectInstance.getDuration() - 1);
   }

   private static Boolean isVersionFixVisible() {
      return true;
   }

   private static String a(byte[] bs) {
      int n = 0;
      int n2;
      char[] cs = new char[n2 = bs.length];

      for (int n3 = 0; n3 < n2; n3++) {
         int n4;
         if ((n4 = 255 & bs[n3]) < 192) {
            cs[n++] = (char)n4;
         } else if (n4 < 224) {
            char c = (char)((char)(n4 & 31) << 6);
            byte b2 = bs[++n3];
            c = (char)(c | (char)(b2 & 63));
            cs[n++] = c;
         } else if (n3 < n2 - 2) {
            char c2 = (char)((char)(n4 & 15) << '\f');
            byte b3 = bs[++n3];
            c2 = (char)(c2 | (char)(b3 & 63) << 6);
            b3 = bs[++n3];
            c2 = (char)(c2 | (char)(b3 & 63));
            cs[n++] = c2;
         }
      }

      return new String(cs, 0, n);
   }

   private static String d(int n, long l) throws InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
      int n2 = n ^ (int)(l & 32767L) ^ 20410;
      if (d[n2] == null) {
         Object[] objs;
         try {
            Long longValue = Thread.currentThread().threadId();
            objs = (Object[])e.get(longValue);
            if (objs == null) {
               objs = new Object[]{Cipher.getInstance("DES/CBC/PKCS5Padding"), SecretKeyFactory.getInstance("DES"), new IvParameterSpec(new byte[8])};
               e.put(longValue, objs);
            }
         } catch (Exception exception) {
            throw new RuntimeException("pw/hachimi/client/\u0000gS\u200e", exception);
         }

         byte[] bs = new byte[8];
         bs[0] = (byte)(l >>> 56);

         for (int n3 = 1; n3 < 8; n3++) {
            bs[n3] = (byte)(l << n3 * 8 >>> 56);
         }

         DESKeySpec desKeySpec = new DESKeySpec(bs);
         SecretKey secretKey = ((SecretKeyFactory)objs[1]).generateSecret(desKeySpec);
         ((Cipher)objs[0]).init(2, secretKey, (IvParameterSpec)objs[2]);
         byte[] bs2 = b[n2].getBytes("ISO-8859-1");
         d[n2] = a(((Cipher)objs[0]).doFinal(bs2));
      }

      return d[n2];
   }

   private static Object d(MutableCallSite mutableCallSite, Object[] objs) throws InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
      int n = (Integer)objs[0];
      long l = (Long)objs[1];
      String str = d(n, l);
      MethodHandle methodHandle = MethodHandles.constant(String.class, str);
      mutableCallSite.setTarget(MethodHandles.dropArguments(methodHandle, 0, int.class, long.class));
      return str;
   }

   private static CallSite d(Lookup lookup, String str, MethodType methodType) {
      MutableCallSite mutableCallSite = new MutableCallSite(methodType);

      try {
         MethodHandle methodHandle = lookup.findStatic(PotionTracker.class, "d", MethodType.methodType(Object.class, MutableCallSite.class, Object[].class));
         methodHandle = methodHandle.asCollector(Object[].class, methodType.parameterCount());
         methodHandle = MethodHandles.insertArguments(methodHandle, 0, mutableCallSite);
         mutableCallSite.setTarget(MethodHandles.explicitCastArguments(methodHandle, methodType));
      } catch (Exception exception) {
         throw new RuntimeException("pw/hachimi/client/\u0000gS\u200e : " + str + " : " + methodType, exception);
      }

      return mutableCallSite;
   }
}
