package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.client.particle.FireworksSparkParticle.Explosion;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.player.UpdateDeathEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.KillEffectsVec3dBoxHelper;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.eventbus.annotation.EventListener;

public class KillEffects extends ClientModule {
   private final Map map = new HashMap();
   Setting ignoreSelf = this.registerSetting(new BooleanSetting("IgnoreSelf", "Dont adds effects when you die", true));
   Setting onlyAttacked = this.registerSetting(new BooleanSetting("OnlyAttacked", "Only adds effects to attacked player's death", true));
   Setting firework = this.registerSetting(new BooleanSetting("Firework", "Explodes a firework at the player's death", true));
   Setting lightning = this.registerSetting(new BooleanSetting("Lightning", "Summon lightning at the player's death", true));
   Setting strikes = this.registerSetting(new NumberSetting("Strikes", "The number of lightning strikes", 1, 1, 5, this::isStrikesVisible));
   Setting hive = this.registerSetting(new BooleanSetting("Hive", "", true));
   Setting hiveColor = this.registerSetting(createColorSetting("HiveColor", "", Color.WHITE, this::isHiveColorVisible));
   Setting hiveDuration = this.registerSetting(new NumberSetting("HiveDuration", "The duration of the hive", 0.1F, 0.7F, 1.0F, this::isHiveDurationVisible));
   ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();

   public KillEffects() {
      super("KillEffects", "Adds effects to player deaths", ModuleCategory.RENDER);
   }

   @EventListener
   public void auF(Render3DContext render3DContext) {
      if ((Boolean)this.hive.atx()) {
         Iterator iterator2 = this.concurrentLinkedQueue.iterator();
         if (iterator2.hasNext()) {
            while (iterator2.hasNext()) {
               KillEffectsVec3dBoxHelper killEffectsVec3dBoxHelper = (KillEffectsVec3dBoxHelper)iterator2.next();
               killEffectsVec3dBoxHelper.lJ(render3DContext);
               if (killEffectsVec3dBoxHelper.lI_()) {
                  iterator2.remove();
               }
            }
         }
      }
   }

   @EventListener
   public void auG(UpdateDeathEvent updateDeathEvent) {
      if (!(Boolean)this.ignoreSelf.atx() || updateDeathEvent.getLiving() != client.player) {
         LivingEntity living = updateDeathEvent.getLiving();
         if (living instanceof PlayerEntity && (!(Boolean)this.onlyAttacked.atx() || this.auK((PlayerEntity)living))) {
            if ((Boolean)this.lightning.atx()) {
               for (int n = 0; n < (Integer)this.strikes.atx(); n++) {
                  LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, client.world);
                  LightningEntity temp = lightningEntity;
                  double temp2 = ((PlayerEntity)living).getX();
                  double d = ((PlayerEntity)living).getZ();
                  double d2 = ((PlayerEntity)living).getY();
                  double d3 = temp2;
                  temp.setPos(d3, d2, d);
                  client.world.addEntity((Entity)lightningEntity);
               }
            }

            if ((Boolean)this.firework.atx()) {
               this.auI(((PlayerEntity)living).getX(), ((PlayerEntity)living).getY(), ((PlayerEntity)living).getZ());
            }

            if ((Boolean)this.hive.atx()) {
               double d4 = ((PlayerEntity)living).getX();
               double d5 = ((PlayerEntity)living).getY();
               double d6 = ((PlayerEntity)living).getZ();
               ConcurrentLinkedQueue concurrentLinkedQueue2 = this.concurrentLinkedQueue;
               Vec3d vec = new Vec3d(d4, d5, d6);
               Setting setting2 = this.hiveColor;
               Vec3d vec2 = vec;
               concurrentLinkedQueue2 = concurrentLinkedQueue2;
               vec = vec2;
               Color temp3 = (Color)setting2.atx();
               setting2 = this.hiveDuration;
               Color color = temp3;
               Vec3d vec3 = vec;
               ConcurrentLinkedQueue concurrentLinkedQueue3 = concurrentLinkedQueue2;
               KillEffectsVec3dBoxHelper killEffectsVec3dBoxHelper = (new KillEffectsVec3dBoxHelper(vec3, color, (long)(1000.0F * (Float)setting2.atx()))).lK();
               concurrentLinkedQueue3.add(killEffectsVec3dBoxHelper);
            }

            return;
         }
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.world != null && client.player != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof EntityDamageS2CPacket && ((EntityDamageS2CPacket)packet2).sourceCauseId() == client.player.getId()) {
            Set temp = this.map.entrySet();
            Predicate predicate = new Predicate() {
               @Override
               public boolean test(Object channelRead0Event) {
                  return KillEffects.auL((Entry)channelRead0Event);
               }
            };
            temp.removeIf(predicate);
            ClientWorld world2 = client.world;
            int n = ((EntityDamageS2CPacket)packet2).entityId();
            Entity entity2 = world2.getEntityById(n);
            if (entity2 == null) {
               return;
            }

            Map map2 = this.map;
            Long longValue = System.currentTimeMillis();
            map2.put(entity2, longValue);
         }
      }
   }

   private void auI(double d, double d2, double d3) {
      for (int n = -4; n <= 4; n++) {
         for (int n2 = -4; n2 <= 4; n2++) {
            for (int n3 = -4; n3 <= 4; n3++) {
               double d4 = n2 + (random2.nextDouble() - random2.nextDouble()) * 0.5;
               double d5 = n + (random2.nextDouble() - random2.nextDouble()) * 0.5;
               double d6 = n3 + (random2.nextDouble() - random2.nextDouble()) * 0.5;
               double d7 = Math.sqrt(d4 * d4 + d5 * d5 + d6 * d6) / 0.5 + random2.nextGaussian() * 0.05;
               this.auJ(d, d2, d3, d4 / d7, d5 / d7, d6 / d7);
               if (n != -4 && n != 4 && n2 != -4 && n2 != 4) {
                  n3 += 7;
               }
            }
         }
      }
   }

   private void auJ(double d, double d2, double d3, double d4, double d5, double d6) {
      if (client.particleManager != null && client.world != null) {
         SimpleParticleType simpleParticleType = ParticleTypes.FIREWORK;
         Explosion explosion = (Explosion)client.particleManager.addParticle((ParticleEffect)simpleParticleType, d, d2, d3, d4, d5, d6);
         if (explosion != null) {
            explosion.setTrail(false);
            explosion.setFlicker(false);
            Explosion temp = explosion;
            int n = ColorHelper3.auO().auX();
            temp.setColor(n);
         }
      }
   }

   private boolean auK(Entity entity2) {
      Long longValue = (Long)this.map.get(entity2);
      boolean flag;
      if (longValue != null && System.currentTimeMillis() - longValue < 5000L) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static boolean auL(Entry entry) {
      boolean flag;
      if (System.currentTimeMillis() - (Long)entry.getValue() > 5000L) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isHiveDurationVisible() {
      return (Boolean)this.hive.atx();
   }

   private Boolean isHiveColorVisible() {
      return (Boolean)this.hive.atx();
   }

   private Boolean isStrikesVisible() {
      return (Boolean)this.lightning.atx();
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color, Supplier supplier) {
      return new ColorSetting(str, str2, color, true, false, supplier);
   }
}
