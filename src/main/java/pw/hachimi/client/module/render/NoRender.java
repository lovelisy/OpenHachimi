package pw.hachimi.client.module.render;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.IsEmptyEvent;
import pw.hachimi.client.event.LerpNauseaEvent;
import pw.hachimi.client.event.RecalculateLevelEvent;
import pw.hachimi.client.event.TickEvent3;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.player.ShowFloatingItemEvent;
import pw.hachimi.client.event.render.ApplyFogEvent;
import pw.hachimi.client.event.render.DoesRenderOnFireEvent;
import pw.hachimi.client.event.render.DrawEvent;
import pw.hachimi.client.event.render.GetFogModifierEvent;
import pw.hachimi.client.event.render.RenderArmorGlintEvent;
import pw.hachimi.client.event.render.RenderArmorPartsEvent;
import pw.hachimi.client.event.render.RenderArmorTrimEvent;
import pw.hachimi.client.event.render.RenderEntityEvent;
import pw.hachimi.client.event.render.RenderEvent10;
import pw.hachimi.client.event.render.RenderEvent12;
import pw.hachimi.client.event.render.RenderEvent15;
import pw.hachimi.client.event.render.RenderEvent4;
import pw.hachimi.client.event.render.RenderEvent6;
import pw.hachimi.client.event.render.RenderEvent7;
import pw.hachimi.client.event.render.RenderFireOverlayEvent;
import pw.hachimi.client.event.render.RenderFireOverlayEvent2;
import pw.hachimi.client.event.render.RenderOverlayEvent;
import pw.hachimi.client.event.render.RenderOverlayEvent2;
import pw.hachimi.client.event.render.RenderPortalOverlayEvent;
import pw.hachimi.client.event.render.RenderScoreboardSidebarEvent;
import pw.hachimi.client.event.render.RenderSpyglassOverlayEvent;
import pw.hachimi.client.event.render.RenderTextEvent;
import pw.hachimi.client.event.render.RenderUnderwaterOverlayEvent;
import pw.hachimi.client.event.render.RenderWorldBorderEvent;
import pw.hachimi.client.event.render.TiltViewWhenHurtEvent;
import pw.hachimi.client.event.world.TickEntityEvent;
import pw.hachimi.client.mixin.FireworkRocketEntityMixin;
import pw.hachimi.client.model.render.FastHideMode;
import pw.hachimi.client.model.render.NoRenderClearLiquidVisionMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.exploit.GodMode;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EntityTypeSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class NoRender extends ClientModule {
   private static NoRender INSTANCE;
   public Setting items = this.registerSetting(new EnumSetting("Items", "Prevents dropped items from rendering", FastHideMode.OFF, FastHideMode.values()));
   public Setting castShadow = this.registerSetting(new BooleanSetting("CastShadow", "Make a layer of shadow under the texture", false, this::isCastShadowVisible));
   public Setting sidesOfItems = this.registerSetting(new BooleanSetting("SidesOfItems", "Make the texture 3D", false, this::isSidesOfItemsVisible));
   Setting ticks = this.registerSetting(new BooleanSetting("Ticks", "Entity ticks", false, this::isTicksVisible));
   Setting age = this.registerSetting(new BooleanSetting("Age", "Increase age", false, this::isAgeVisible));
   Setting noHurtCam = this.registerSetting(new BooleanSetting("NoHurtCam", "Prevents the hurt camera shake effect from rendering", true));
   Setting armor = this.registerSetting(new BooleanSetting("Armor", "Prevents armor pieces from rendering", false));
   Setting armorTrim = this.registerSetting(new BooleanSetting("ArmorTrim", "Prevents armor pieces from rendering", false));
   Setting armorGlint = this.registerSetting(new BooleanSetting("ArmorGlint", "Prevents armor pieces from rendering", false));
   Setting overlayFire = this.registerSetting(new BooleanSetting("Overlay-Fire", "Prevents the fire Hud overlay from rendering", true));
   Setting overlayPortal = this.registerSetting(new BooleanSetting("Overlay-Portal", "Prevents the portal Hud overlay from rendering", true));
   Setting overlayWater = this.registerSetting(new BooleanSetting("Overlay-Water", "Prevents the water Hud overlay from rendering", true));
   Setting overlayBlock = this.registerSetting(new BooleanSetting("Overlay-Block", "Prevents the block Hud overlay from rendering", true));
   Setting overlaySpyglass = this.registerSetting(new BooleanSetting("Overlay-Spyglass", "Prevents the spyglass Hud overlay from rendering", false));
   Setting overlayPumpkin = this.registerSetting(new BooleanSetting("Overlay-Pumpkin", "Prevents the pumpkin Hud overlay from rendering", true));
   Setting overlayBossBar = this.registerSetting(new BooleanSetting("Overlay-BossBar", "Prevents the boss bar Hud overlay from rendering", true));
   Setting nausea = this.registerSetting(new BooleanSetting("Nausea", "Prevents nausea effect from rendering (includes portal effect)", false));
   Setting blindness = this.registerSetting(new BooleanSetting("Blindness", "Prevents blindness effect from rendering", false));
   Setting frostbite = this.registerSetting(new BooleanSetting("Frostbite", "Prevents frostbite effect from rendering", false));
   Setting skylight = this.registerSetting(new BooleanSetting("Skylight", "Prevents skylight from rendering", true));
   Setting witherSkulls = this.registerSetting(new BooleanSetting("WitherSkulls", "Prevents flying wither skulls from rendering", false));
   Setting itemFrames = this.registerSetting(new BooleanSetting("ItemFrames", "Prevents items on item frames from rendering", false));
   Setting tridents = this.registerSetting(new BooleanSetting("Tridents", "Prevents tridents from rendering", false));
   Setting entities = this.registerSetting(new BooleanSetting("Entities", "Prevents entities from rendering", false));
   Setting entityList = this.registerSetting(new EntityTypeSetting("EntityList", "The render entity list"));
   Setting tileEntities = this.registerSetting(new BooleanSetting("TileEntities", "Prevents special tile entity properties from rendering (i.e. enchantment table books or cutting table saws)", false));
   Setting signText = this.registerSetting(new BooleanSetting("SignText", "Prevents the text on signs from rendering", false));
   Setting fireEntities = this.registerSetting(new BooleanSetting("FireEntities", "Prevents fire from rendering on entities", false));
   Setting fireworks = this.registerSetting(new BooleanSetting("Fireworks", "Prevents firework entities from rendering", true));
   Setting totems = this.registerSetting(new BooleanSetting("Totems", "Prevents totem pop overlay from rendering", false));
   Setting bob = this.registerSetting(new BooleanSetting("Bob", "Prevents items from bobbing when walking", false));
   Setting worldBorder = this.registerSetting(new BooleanSetting("WorldBorder", "Prevents world border from rendering", false));
   Setting fog = this.registerSetting(new EnumSetting("Fog", "Prevents fog from rendering in the world", NoRenderClearLiquidVisionMode.OFF, NoRenderClearLiquidVisionMode.values()));
   Setting guiToast = this.registerSetting(new BooleanSetting("GuiToast", "Prevents advancements from rendering", true));
   Setting scoreboard = this.registerSetting(new BooleanSetting("Scoreboard", "Disable rendering of the scoreboard", false));
   Setting terrainScreen = this.registerSetting(new BooleanSetting("TerrainScreen", "Prevents downloading terrain screen from rendering", false));
   public static boolean fm;

   public NoRender() {
      super("NoRender", "Prevents certain game elements from rendering", ModuleCategory.RENDER);
      INSTANCE = this;
   }

   public static NoRender ff() {
      return INSTANCE;
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if (this.items.atx() == FastHideMode.REMOVE) {
            Iterator iterator2 = Lists.newArrayList(client.world.getEntities()).iterator();

            while (iterator2.hasNext()) {
               Entity entity2 = (Entity)iterator2.next();
               if (entity2 instanceof ItemEntity) {
                  ClientWorld temp = client.world;
                  int temp2 = entity2.getId();
                  RemovalReason removalReason = RemovalReason.DISCARDED;
                  int n = temp2;
                  temp.removeEntity(n, removalReason);
               }
            }
         }

         if ((Boolean)this.terrainScreen.atx() && client.currentScreen instanceof DownloadingTerrainScreen && !GodMode.ac().ah()) {
            client.currentScreen = null;
         }
      }
   }

   @EventListener
   public void onTiltViewWhenHurt(TiltViewWhenHurtEvent tiltViewWhenHurtEvent) {
      if ((Boolean)this.noHurtCam.atx()) {
         tiltViewWhenHurtEvent.aim();
      }
   }

   @EventListener
   public void onRenderArmorParts(RenderArmorPartsEvent renderArmorPartsEvent) {
      if ((Boolean)this.armor.atx()) {
         renderArmorPartsEvent.aim();
      }
   }

   @EventListener
   public void onRenderArmorGlint(RenderArmorGlintEvent renderArmorGlintEvent) {
      if ((Boolean)this.armorGlint.atx()) {
         renderArmorGlintEvent.aim();
      }
   }

   @EventListener
   public void onRenderArmorTrim(RenderArmorTrimEvent renderArmorTrimEvent) {
      if ((Boolean)this.armorTrim.atx()) {
         renderArmorTrimEvent.aim();
      }
   }

   @EventListener
   public void onRenderFireOverlayEvent2(RenderFireOverlayEvent2 renderFireOverlayEvent2) {
      if ((Boolean)this.overlayFire.atx()) {
         renderFireOverlayEvent2.aim();
      }
   }

   @EventListener
   public void onRenderPortalOverlay(RenderPortalOverlayEvent renderPortalOverlayEvent) {
      if ((Boolean)this.overlayPortal.atx()) {
         renderPortalOverlayEvent.aim();
      }
   }

   @EventListener
   public void onRenderUnderwaterOverlay(RenderUnderwaterOverlayEvent renderUnderwaterOverlayEvent) {
      if ((Boolean)this.overlayWater.atx()) {
         renderUnderwaterOverlayEvent.aim();
      }
   }

   @EventListener
   public void onRenderFireOverlay(RenderFireOverlayEvent renderFireOverlayEvent) {
      if ((Boolean)this.overlayBlock.atx()) {
         renderFireOverlayEvent.aim();
      }
   }

   @EventListener
   public void onRenderSpyglassOverlay(RenderSpyglassOverlayEvent renderSpyglassOverlayEvent) {
      if ((Boolean)this.overlaySpyglass.atx()) {
         renderSpyglassOverlayEvent.aim();
      }
   }

   @EventListener
   public void onRenderOverlayEvent2(RenderOverlayEvent2 renderOverlayEvent2) {
      if ((Boolean)this.overlayPumpkin.atx()) {
         renderOverlayEvent2.aim();
      }
   }

   @EventListener
   public void onRenderEvent4(RenderEvent4 renderEvent4) {
      if ((Boolean)this.overlayBossBar.atx()) {
         renderEvent4.aim();
      }
   }

   @EventListener
   public void onRenderOverlay(RenderOverlayEvent renderOverlayEvent) {
      if ((Boolean)this.frostbite.atx()) {
         renderOverlayEvent.aim();
      }
   }

   @EventListener
   public void onLerpNausea(LerpNauseaEvent lerpNauseaEvent) {
      if ((Boolean)this.nausea.atx()) {
         lerpNauseaEvent.aim();
      }
   }

   @EventListener
   public void onGetFogModifier(GetFogModifierEvent getFogModifierEvent) {
      if ((Boolean)this.blindness.atx()) {
         getFogModifierEvent.aim();
      }
   }

   @EventListener
   public void onRecalculateLevel(RecalculateLevelEvent recalculateLevelEvent) {
      if ((Boolean)this.skylight.atx()) {
         recalculateLevelEvent.aim();
      }
   }

   @EventListener
   public void onRenderEvent6(RenderEvent6 renderEvent6) {
      if ((Boolean)this.witherSkulls.atx()) {
         renderEvent6.aim();
      }
   }

   @EventListener
   public void onIsEmpty(IsEmptyEvent isEmptyEvent) {
      if ((Boolean)this.itemFrames.atx()) {
         isEmptyEvent.aim();
      }
   }

   @EventListener
   public void onRenderEvent10(RenderEvent10 renderEvent10) {
      if ((Boolean)this.tileEntities.atx()) {
         renderEvent10.aim();
      }
   }

   @EventListener
   public void onRenderEntity(RenderEntityEvent renderEntityEvent) {
      if (this.fM(renderEntityEvent.getEntity())) {
         renderEntityEvent.aim();
      }
   }

   @EventListener
   public void onRenderText(RenderTextEvent renderTextEvent) {
      if ((Boolean)this.signText.atx()) {
         renderTextEvent.aim();
      }
   }

   @EventListener
   public void onTickEntity(TickEntityEvent tickEntityEvent) {
      if (tickEntityEvent.getEntity() instanceof TridentEntity && (Boolean)this.tridents.atx()) {
         tickEntityEvent.aim();
      } else if (!(tickEntityEvent.getEntity() instanceof ItemEntity)
         || this.items.atx() != FastHideMode.HIDE
            && this.items.atx() != FastHideMode.REMOVE
            && (!(Boolean)this.ticks.atx() || this.items.atx() != FastHideMode.FAST)) {
         if (tickEntityEvent.getEntity() instanceof WitherSkullEntity && (Boolean)this.witherSkulls.atx()) {
            tickEntityEvent.aim();
         } else if (this.fM(tickEntityEvent.getEntity())) {
            tickEntityEvent.aim();
         }
      } else {
         tickEntityEvent.aim();
      }

      if (tickEntityEvent.aik() && (Boolean)this.age.atx()) {
         int n = tickEntityEvent.getEntity().age + 1;
         tickEntityEvent.getEntity().age = n;
      }
   }

   @EventListener
   public void onRenderEvent12(RenderEvent12 renderEvent12) {
      if ((Boolean)this.tridents.atx()) {
         renderEvent12.aim();
      }
   }

   @EventListener
   public void onRenderEvent15(RenderEvent15 renderEvent15) {
      if ((Boolean)this.fireworks.atx()) {
         renderEvent15.aim();
      }
   }

   @EventListener
   public void onTickEvent3(TickEvent3 tickEvent3) {
      if ((Boolean)this.fireworks.atx() && !((FireworkRocketEntityMixin)tickEvent3.getFireworkRocketEntity()).hookWasShotByEntity()) {
         tickEvent3.aim();
      }
   }

   @EventListener
   public void onShowFloatingItem(ShowFloatingItemEvent showFloatingItemEvent) {
      if ((Boolean)this.totems.atx() && showFloatingItemEvent.afu() == Items.TOTEM_OF_UNDYING) {
         showFloatingItemEvent.aim();
      }
   }

   @EventListener
   public void onRenderWorldBorder(RenderWorldBorderEvent renderWorldBorderEvent) {
      if ((Boolean)this.worldBorder.atx()) {
         renderWorldBorderEvent.aim();
      }
   }

   @EventListener(priority = Integer.MIN_VALUE)
   public void onApplyFog(ApplyFogEvent applyFogEvent) {
      if (this.fog.atx() == NoRenderClearLiquidVisionMode.LIQUID_VISION && client.player != null) {
         TagKey tagKey = FluidTags.LAVA;
         if (client.player.isSubmergedIn(tagKey)) {
            applyFogEvent.aim();
            applyFogEvent.VT(applyFogEvent.VR() * 4.0F);
            applyFogEvent.VV(applyFogEvent.VR() * 4.25F);
            return;
         }
      }

      if (this.fog.atx() == NoRenderClearLiquidVisionMode.CLEAR) {
         applyFogEvent.aim();
         applyFogEvent.VT(applyFogEvent.VR() * 4.0F);
         applyFogEvent.VV(applyFogEvent.VR() * 4.25F);
      }
   }

   @EventListener
   public void onRenderEvent7(RenderEvent7 renderEvent7) {
      if (this.items.atx() == FastHideMode.HIDE) {
         renderEvent7.aim();
      }
   }

   @EventListener
   public void onDraw(DrawEvent drawEvent) {
      if ((Boolean)this.guiToast.atx()) {
         drawEvent.aim();
      }
   }

   @EventListener
   public void onRenderScoreboardSidebar(RenderScoreboardSidebarEvent renderScoreboardSidebarEvent) {
      if ((Boolean)this.scoreboard.atx()) {
         renderScoreboardSidebarEvent.aim();
      }
   }

   @EventListener
   public void onDoesRenderOnFire(DoesRenderOnFireEvent doesRenderOnFireEvent) {
      if ((Boolean)this.fireEntities.atx()) {
         doesRenderOnFireEvent.aim();
      }
   }

   public boolean fM(Entity entity2) {
      if ((Boolean)this.entities.atx() && entity2 != null) {
         List temp = (List)this.entityList.atx();
         EntityType entityType = entity2.getType();
         if (temp.contains(entityType)) {
            return true;
         }
      }

      return false;
   }

   private Boolean isAgeVisible() {
      boolean flag;
      if (this.items.atx() == FastHideMode.FAST) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isTicksVisible() {
      boolean flag;
      if (this.items.atx() == FastHideMode.FAST) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSidesOfItemsVisible() {
      boolean flag;
      if (this.items.atx() == FastHideMode.FAST) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isCastShadowVisible() {
      boolean flag;
      if (this.items.atx() == FastHideMode.FAST) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
