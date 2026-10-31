package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.event.AddEmitterEvent;
import pw.hachimi.client.event.InitEvent2;
import pw.hachimi.client.event.TickEvent22;
import pw.hachimi.client.event.render.AddExplosionParticleEvent;
import pw.hachimi.client.event.render.AddParticleEvent;
import pw.hachimi.client.event.world.AddBlockBreakParticlesEvent;
import pw.hachimi.client.model.render.ParticlesOffRemoveMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class Particles extends ClientModule {
   Setting totem = this.registerSetting(new EnumSetting("Totem", "Modify totem particle rendering", ParticlesOffRemoveMode.OFF, ParticlesOffRemoveMode.values()));
   Setting totemColor = this.registerSetting(createColorSetting("TotemColor", "Color of the totem particles", i(), this::isTotemColorVisible));
   Setting totemParticles = this.registerSetting(new NumberSetting("TotemParticles", "Max totem particles", 3, 16, 16, this::isTotemParticlesVisible));
   Setting totemParticleTicks = this.registerSetting(new NumberSetting("TotemParticleTicks", "Max totem particle ticks", 5, 30, 30, this::isTotemParticleTicksVisible));
   Setting explosions = this.registerSetting(new BooleanSetting("Explosions", "Prevents explosion particles from rendering", true));
   Setting firework = this.registerSetting(new BooleanSetting("Firework", "Prevents rendering of firework particles", false));
   Setting effects = this.registerSetting(new BooleanSetting("Effects", "Prevents rendering of potion effect particles", true));
   Setting items = this.registerSetting(new BooleanSetting("Items", "Prevents rendering of eating particles", false));
   Setting bottleSplash = this.registerSetting(new BooleanSetting("BottleSplash", "Prevents rendering of bottle splash particles", true));
   Setting portal = this.registerSetting(new BooleanSetting("Portal", "Prevents rendering of portal particles", true));
   Setting block = this.registerSetting(new BooleanSetting("Block", "Prevents block particles from rendering", false));
   Setting blockBreak = this.registerSetting(new BooleanSetting("BlockBreak", "Prevents block break particles from rendering", false));
   Setting campfires = this.registerSetting(new BooleanSetting("Campfires", "Prevents campfire particles from rendering", false));
   Setting cryingObsidian = this.registerSetting(new BooleanSetting("CryingObsidian", "Prevents obsidian tear particles from rendering", false));

   public Particles() {
      super("Particles", "Change the rendering of particles", ModuleCategory.RENDER);
   }

   @EventListener
   public void Up(AddExplosionParticleEvent addExplosionParticleEvent) {
      if ((Boolean)this.firework.atx()) {
         addExplosionParticleEvent.aim();
      }
   }

   @EventListener
   public void onAddParticle(AddParticleEvent addParticleEvent) {
      if ((Boolean)this.effects.atx() && addParticleEvent.Le() == ParticleTypes.ENTITY_EFFECT
         || (Boolean)this.explosions.atx() && (addParticleEvent.Le() == ParticleTypes.EXPLOSION || addParticleEvent.Le() == ParticleTypes.EXPLOSION_EMITTER)
         || (Boolean)this.items.atx() && addParticleEvent.Le() == ParticleTypes.ITEM
         || (Boolean)this.bottleSplash.atx() && (addParticleEvent.Le() == ParticleTypes.EFFECT || addParticleEvent.Le() == ParticleTypes.INSTANT_EFFECT)
         || (Boolean)this.portal.atx() && addParticleEvent.Le() == ParticleTypes.PORTAL
         || (Boolean)this.block.atx() && addParticleEvent.Le() == ParticleTypes.BLOCK
         || (Boolean)this.campfires.atx() && addParticleEvent.Le() == ParticleTypes.CAMPFIRE_COSY_SMOKE
         || (Boolean)this.cryingObsidian.atx()
            && (
               addParticleEvent.Le() == ParticleTypes.FALLING_OBSIDIAN_TEAR
                  || addParticleEvent.Le() == ParticleTypes.DRIPPING_OBSIDIAN_TEAR
                  || addParticleEvent.Le() == ParticleTypes.LANDING_OBSIDIAN_TEAR
            )) {
         addParticleEvent.aim();
      }
   }

   @EventListener
   public void onAddEmitter(AddEmitterEvent addEmitterEvent) {
      if (this.totem.atx() == ParticlesOffRemoveMode.REMOVE && addEmitterEvent.Le() == ParticleTypes.TOTEM_OF_UNDYING) {
         addEmitterEvent.aim();
      }
   }

   @EventListener
   public void onTickEvent2(TickEvent22 tickEvent22) {
      if (this.totem.atx() != ParticlesOffRemoveMode.REMOVE && tickEvent22.getParticleEffect() == ParticleTypes.TOTEM_OF_UNDYING) {
         tickEvent22.aim();
         tickEvent22.e((Integer)this.totemParticles.atx());
         tickEvent22.g((Integer)this.totemParticleTicks.atx());
      }
   }

   @EventListener
   public void onInitEvent2(InitEvent2 initEvent2) {
      if (this.totem.atx() == ParticlesOffRemoveMode.COLOR) {
         initEvent2.aim();
         Color color = (Color)this.totemColor.atx();
         float f = color.getRed() / 255.0F;
         float f2 = color.getGreen() / 255.0F;
         float f3 = color.getBlue() / 255.0F;
         if (random2.nextInt(4) == 0) {
            float f4 = color.getRed() * 0.7F / 255.0F;
            float f5 = color.getGreen() * 0.7F / 255.0F;
            float f6 = color.getBlue() * 0.7F / 255.0F;
            float temp = MathHelper.clamp(f4, 0.0F, 1.0F);
            float f7 = MathHelper.clamp(f5, 0.0F, 1.0F);
            float f8 = temp;
            Color color2 = new Color(f8, f7, MathHelper.clamp(f6, 0.0F, 1.0F));
            initEvent2.setColor(color2);
         } else {
            float f9 = MathHelper.clamp(f + random2.nextFloat() * 0.1F, 0.0F, 1.0F);
            Random random = random2;
            float temp2 = MathHelper.clamp(f2 + random.nextFloat() * 0.1F, 0.0F, 1.0F);
            random = random2;
            float f10 = temp2;
            float f11 = f9;
            Color color3 = new Color(f11, f10, MathHelper.clamp(f3 + random.nextFloat() * 0.1F, 0.0F, 1.0F));
            initEvent2.setColor(color3);
         }
      }
   }

   @EventListener
   public void onAddBlockBreakParticles(AddBlockBreakParticlesEvent addBlockBreakParticlesEvent) {
      if ((Boolean)this.blockBreak.atx()) {
         addBlockBreakParticlesEvent.aim();
      }
   }

   private Boolean isTotemParticleTicksVisible() {
      boolean flag;
      if (this.totem.atx() != ParticlesOffRemoveMode.REMOVE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isTotemParticlesVisible() {
      boolean flag;
      if (this.totem.atx() != ParticlesOffRemoveMode.REMOVE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isTotemColorVisible() {
      boolean flag;
      if (this.totem.atx() == ParticlesOffRemoveMode.COLOR) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color, Supplier supplier) {
      return new ColorSetting(str, str2, color, false, false, supplier);
   }

   private static Color i() {
      return new Color(25, 120, 0);
   }
}
