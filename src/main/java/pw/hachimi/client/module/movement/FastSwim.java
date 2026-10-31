package pw.hachimi.client.module.movement;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Vec2f;
import pw.hachimi.client.event.movement.MoveEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.movement.FastSwimVanillaNormalMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.player.EnchantmentUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class FastSwim extends ClientModule {
   Setting mode = this.registerSetting(new EnumSetting("Mode", "The mode for swimming", FastSwimVanillaNormalMode.VANILLA, FastSwimVanillaNormalMode.values()));
   Setting waterSpeed = this.registerSetting(new NumberSetting("WaterSpeed", "Base speed for moving through water", 0.1F, 1.0F, 10.0F));
   Setting lavaSpeed = this.registerSetting(new NumberSetting("LavaSpeed", "Base speed for moving through lava", 0.1F, 1.0F, 10.0F));
   Setting vertical = this.registerSetting(new BooleanSetting("Vertical", "Allows moving vertically", true));
   Setting elytra = this.registerSetting(new BooleanSetting("Elytra", "Applies elytra speed when moving through liquids", false));
   Setting elytraSpeed = this.registerSetting(new NumberSetting("ElytraSpeed", "Base speed for moving through lava", 0.1F, 1.0F, 10.0F, this::isElytraSpeedVisible));
   Setting depthStrider = this.registerSetting(new BooleanSetting("DepthStrider", "Prefers depth strider", false));
   public static boolean db;

   public FastSwim() {
      super("FastSwim", "Move faster in liquids", ModuleCategory.MOVEMENT);
   }

   @EventListener
   public void onMove(MoveEvent moveEvent) {
      if (!(Boolean)this.depthStrider.atx()) {
         EquipmentSlot equipmentSlot = EquipmentSlot.FEET;
         if (EnchantmentUtils.VN(client.player.getEquippedStack(equipmentSlot), Enchantments.DEPTH_STRIDER) > 0) {
            return;
         }
      }

      TagKey tagKey = FluidTags.WATER;
      if (!client.player.isSubmergedIn(tagKey)) {
         TagKey tagKey2 = FluidTags.LAVA;
         if (!client.player.isSubmergedIn(tagKey2)) {
            return;
         }
      }

      moveEvent.aim();
      EquipmentSlot equipmentSlot2 = EquipmentSlot.CHEST;
      if (client.player.getEquippedStack(equipmentSlot2).getItem() instanceof ElytraItem && (Boolean)this.elytra.atx()) {
         moveEvent.ym(moveEvent.yl() * ((Float)this.elytraSpeed.atx()).floatValue());
         moveEvent.yq(moveEvent.yp() * ((Float)this.elytraSpeed.atx()).floatValue());
         if ((Boolean)this.vertical.atx()) {
            if (client.options.jumpKey.isPressed()) {
               moveEvent.yo(moveEvent.yn() + 0.16);
               Managers.playerStateManager.rJ(moveEvent.yn() + 0.16);
            } else if (client.options.sneakKey.isPressed()) {
               moveEvent.yo(moveEvent.yn() - 0.12);
               Managers.playerStateManager.rJ(moveEvent.yn() - 0.12);
            }
         }
      } else {
         TagKey tagKey3 = FluidTags.WATER;
         Float floatValue2;
         if (client.player.isSubmergedIn(tagKey3)) {
            floatValue2 = (Float)this.waterSpeed.atx();
         } else {
            floatValue2 = (Float)this.lavaSpeed.atx();
         }

         float f = floatValue2;
         switch ((FastSwimVanillaNormalMode)this.mode.atx()) {
            case VANILLA:
               moveEvent.ym(moveEvent.yl() * f);
               moveEvent.yq(moveEvent.yp() * f);
               break;
            case NORMAL:
               Vec2f vec2f = Speed.lq().lu(f / 10.0F);
               moveEvent.ym(vec2f.x);
               moveEvent.yq(vec2f.y);
         }

         if ((Boolean)this.vertical.atx()) {
            if (client.options.jumpKey.isPressed()) {
               moveEvent.yo(moveEvent.yn() + 0.16);
               Managers.playerStateManager.rJ(moveEvent.yn() + 0.16);
            } else if (client.options.sneakKey.isPressed()) {
               moveEvent.yo(moveEvent.yn() - 0.12);
               Managers.playerStateManager.rJ(moveEvent.yn() - 0.12);
            }
         }
      }
   }

   private Boolean isElytraSpeedVisible() {
      return (Boolean)this.elytra.atx();
   }
}
