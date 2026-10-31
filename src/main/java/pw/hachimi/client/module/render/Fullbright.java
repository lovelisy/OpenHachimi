package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.OnGameJoinEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.UnknownEvent3;
import pw.hachimi.client.event.UpdateEvent2;
import pw.hachimi.client.event.UpdateEvent3;
import pw.hachimi.client.model.render.FullbrightLightGammaMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class Fullbright extends ClientModule {
   Setting mode = this.registerSetting(new EnumSetting("Mode", "Mode for world brightness", FullbrightLightGammaMode.GAMMA, FullbrightLightGammaMode.values()));
   Setting lightColor = this.registerSetting(createColorSetting("LightColor", "The color of the light", Color.WHITE, this::isLightColorVisible));
   public static boolean b;

   public Fullbright() {
      super("Fullbright", "Brightens the world", ModuleCategory.RENDER);
   }

   @Override
   public void awd() {
      if (client.player != null && client.world != null && this.mode.atx() == FullbrightLightGammaMode.POTION) {
         ClientPlayerEntity temp = client.player;
         StatusEffectInstance statusEffectInstance = a(StatusEffects.NIGHT_VISION);
         temp.addStatusEffect(statusEffectInstance);
      }
   }

   @Override
   public void awe() {
      if (client.player != null && client.world != null && this.mode.atx() == FullbrightLightGammaMode.POTION) {
         RegistryEntry registryEntry = StatusEffects.NIGHT_VISION;
         client.player.removeStatusEffect(registryEntry);
      }
   }

   @EventListener
   public void onOnGameJoin(OnGameJoinEvent onGameJoinEvent) {
      this.awe();
      this.awd();
   }

   @EventListener
   public void onUpdateEvent3(UpdateEvent3 updateEvent3) {
      if (this.mode.atx() == FullbrightLightGammaMode.GAMMA) {
         updateEvent3.aim();
         updateEvent3.aiY();
      }
   }

   @EventListener
   public void onUpdateEvent2(UpdateEvent2 updateEvent2) {
      if (this.mode.atx() == FullbrightLightGammaMode.LIGHT) {
         updateEvent2.aim();
         Color color = (Color)this.lightColor.atx();
         updateEvent2.setColor(new Color(color.getBlue(), color.getGreen(), color.getRed()));
      }
   }

   @EventListener
   public void ajk(UnknownEvent3 unknownEvent3) {
      if (client.player != null
         && this.mode == unknownEvent3.getSetting()
         && unknownEvent3.getEventPhase() == EventPhase.POST
         && this.mode.atx() != FullbrightLightGammaMode.POTION) {
         RegistryEntry registryEntry = StatusEffects.NIGHT_VISION;
         client.player.removeStatusEffect(registryEntry);
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (this.mode.atx() == FullbrightLightGammaMode.POTION) {
         RegistryEntry registryEntry = StatusEffects.NIGHT_VISION;
         if (!client.player.hasStatusEffect(registryEntry)) {
            ClientPlayerEntity temp = client.player;
            StatusEffectInstance statusEffectInstance = a(StatusEffects.NIGHT_VISION);
            temp.addStatusEffect(statusEffectInstance);
         }
      }
   }

   private Boolean isLightColorVisible() {
      boolean flag;
      if (this.mode.atx() == FullbrightLightGammaMode.LIGHT) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color, Supplier supplier) {
      return new ColorSetting(str, str2, color, false, false, supplier);
   }

   private static StatusEffectInstance a(RegistryEntry registryEntry) {
      return new StatusEffectInstance(registryEntry, -1, 0);
   }
}
