package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.biome.BiomeParticleConfig;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.render.ApplyFogEvent;
import pw.hachimi.client.event.render.GetCloudsColorEvent;
import pw.hachimi.client.event.render.GetFogColorOverrideEvent;
import pw.hachimi.client.event.render.GetParticleConfigEvent;
import pw.hachimi.client.event.render.GetSkyColorEvent;
import pw.hachimi.client.model.render.SkyboxOption;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class Skybox extends ClientModule {
   Setting weather = this.registerSetting(new EnumSetting("Weather", "The world weather", SkyboxOption.a_, SkyboxOption.values()));
   Setting worldTime = this.registerSetting(new BooleanSetting("WorldTime", "Custom the world time", false));
   Setting time = this.registerSetting(new NumberSetting("Time", "The world time of day", 0, 6000, 24000, this.worldTime::atx));
   Setting sky = this.registerSetting(new BooleanSetting("Sky", "Changes the world skybox color", true));
   Setting skyColor = this.registerSetting(createColorSetting("SkyColor", "The color for the world skybox", o(), false, this::isSkyColorVisible));
   Setting cloud = this.registerSetting(new BooleanSetting("Cloud", "Changes the world cloud color", false));
   Setting cloudColor = this.registerSetting(createColorSetting("CloudColor", "The color for the world clouds", o(), false, this::isCloudColorVisible));
   Setting fog = this.registerSetting(new BooleanSetting("Fog", "Changes the world fog color", false));
   Setting fogStart = this.registerSetting(new NumberSetting("FogStart", "The fog start distance", 0.0F, 0.0F, 256.0F, this::isFogStartVisible));
   Setting fogEnd = this.registerSetting(new NumberSetting("FogEnd", "The fog start distance", 10.0F, 64.0F, 256.0F, this::isFogEndVisible));
   Setting fogColor = this.registerSetting(createColorSetting("FogColor", "The color for the world fog", o(), true, this::isFogColorVisible));
   private SkyboxOption skyboxOption;
   public static boolean fh;

   public Skybox() {
      super("Skybox", "Changes the rendering of the world skybox", ModuleCategory.RENDER);
   }

   @Override
   public void awd() {
      if (client.world != null) {
         if (client.world.isThundering()) {
            this.skyboxOption = SkyboxOption.c;
         } else if (client.world.isRaining()) {
            this.skyboxOption = SkyboxOption.b;
         } else {
            this.skyboxOption = SkyboxOption.a_;
         }

         this.asM((SkyboxOption)this.weather.atx());
      }
   }

   @Override
   public void awe() {
      if (client.world != null && this.skyboxOption != null) {
         this.asM(this.skyboxOption);
      }
   }

   @EventListener
   public void onGetParticleConfig(GetParticleConfigEvent getParticleConfigEvent) {
      if (this.weather.atx() == SkyboxOption.d) {
         getParticleConfigEvent.aim();
         getParticleConfigEvent.setBiomeParticleConfig(a(ParticleTypes.WHITE_ASH));
      }
   }

   private void asM(SkyboxOption skyboxOption2) {
      switch ((SkyboxOption)skyboxOption2) {
         case a_:
         case d:
            client.world.getLevelProperties().setRaining(false);
            client.world.setRainGradient(0.0F);
            client.world.setThunderGradient(0.0F);
            break;
         case b:
            client.world.getLevelProperties().setRaining(true);
            client.world.setRainGradient(1.0F);
            client.world.setThunderGradient(0.0F);
            break;
         case c:
            client.world.getLevelProperties().setRaining(true);
            client.world.setRainGradient(2.0F);
            client.world.setThunderGradient(1.0F);
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.POST) {
         this.asM((SkyboxOption)this.weather.atx());
         if ((Boolean)this.worldTime.atx()) {
            ClientWorld temp = client.world;
            long l = ((Integer)this.time.atx()).intValue();
            temp.setTimeOfDay(l);
         }
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (channelRead0Event.getPacket() instanceof WorldTimeUpdateS2CPacket && (Boolean)this.worldTime.atx()) {
         channelRead0Event.aim();
      }

      Packet packet2 = channelRead0Event.getPacket();
      if (packet2 instanceof GameStateChangeS2CPacket && this.weather.atx() != SkyboxOption.e) {
         if (((GameStateChangeS2CPacket)packet2).getReason() == GameStateChangeS2CPacket.RAIN_STARTED
            || ((GameStateChangeS2CPacket)packet2).getReason() == GameStateChangeS2CPacket.RAIN_GRADIENT_CHANGED) {
            this.skyboxOption = SkyboxOption.b;
            channelRead0Event.aim();
         } else if (((GameStateChangeS2CPacket)packet2).getReason() == GameStateChangeS2CPacket.RAIN_STOPPED) {
            this.skyboxOption = SkyboxOption.a_;
            channelRead0Event.aim();
         } else if (((GameStateChangeS2CPacket)packet2).getReason() == GameStateChangeS2CPacket.THUNDER_GRADIENT_CHANGED) {
            this.skyboxOption = SkyboxOption.c;
            channelRead0Event.aim();
         }
      }
   }

   @EventListener
   public void onGetSkyColor(GetSkyColorEvent getSkyColorEvent) {
      if ((Boolean)this.sky.atx()) {
         getSkyColorEvent.aim();
         getSkyColorEvent.setColor((Color)this.skyColor.atx());
      }
   }

   @EventListener
   public void onGetCloudsColor(GetCloudsColorEvent getCloudsColorEvent) {
      if ((Boolean)this.cloud.atx()) {
         getCloudsColorEvent.aim();
         getCloudsColorEvent.setColor((Color)this.cloudColor.atx());
      }
   }

   @EventListener
   public void onGetFogColorOverride(GetFogColorOverrideEvent getFogColorOverrideEvent) {
      if ((Boolean)this.fog.atx()) {
         getFogColorOverrideEvent.aim();
         getFogColorOverrideEvent.setColor((Color)this.fogColor.atx());
      }
   }

   @EventListener
   public void onApplyFog(ApplyFogEvent applyFogEvent) {
      if ((Boolean)this.fog.atx()) {
         applyFogEvent.aim();
         applyFogEvent.VT((Float)this.fogStart.atx());
         applyFogEvent.VV((Float)this.fogEnd.atx());
      }
   }

   private Boolean isFogColorVisible() {
      return (Boolean)this.fog.atx();
   }

   private Boolean isFogEndVisible() {
      return (Boolean)this.fog.atx();
   }

   private Boolean isFogStartVisible() {
      return (Boolean)this.fog.atx();
   }

   private Boolean isCloudColorVisible() {
      return (Boolean)this.cloud.atx();
   }

   private Boolean isSkyColorVisible() {
      return (Boolean)this.sky.atx();
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color, boolean flag, Supplier supplier) {
      return new ColorSetting(str, str2, color, flag, true, supplier);
   }

   private static BiomeParticleConfig a(ParticleEffect particleEffect) {
      return new BiomeParticleConfig(particleEffect, 0.118093334F);
   }

   private static Color o() {
      return new Color(255, 0, 0);
   }
}
