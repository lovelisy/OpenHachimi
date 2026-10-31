package pw.hachimi.client.module.render;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import pw.hachimi.client.event.movement.GetSpeedEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.player.ApplyEatOrDrinkTransformationEvent;
import pw.hachimi.client.event.player.EquipProgressMainhandEvent;
import pw.hachimi.client.event.player.GetHandSwingDurationEvent;
import pw.hachimi.client.event.player.UpdateHeldItemsEvent;
import pw.hachimi.client.event.world.UpdateTrackedPositionAndAnglesEvent;
import pw.hachimi.client.mixin.BundlePacketMixin;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public final class Animations extends ClientModule {
   Setting noSwitchAnimation = this.registerSetting(new BooleanSetting("NoSwitchAnimation", "Removes the animation when switching items", false));
   Setting oldSwingAnimation = this.registerSetting(new BooleanSetting("OldSwingAnimation", "Reverts to the 1.8 swinging animations", false));
   Setting swingSpeed = this.registerSetting(new BooleanSetting("SwingSpeed", "Allows you to modify your swing speed.", false));
   Setting swingFactor = this.registerSetting(new NumberSetting("SwingFactor", "The speed of your swing.", 1, 6, 20, this::isSwingFactorVisible));
   Setting selfOnly = this.registerSetting(new BooleanSetting("SelfOnly", "Make the module only affect yourself", true));
   Setting eatTransform = this.registerSetting(new BooleanSetting("EatTransform", "Transforms the first person eating animation", false));
   Setting eatTransformFactor = this.registerSetting(new NumberSetting("EatTransform-Factor", "Factor for the first person eating animation", 0.0F, 0.99F, 4.0F, this::isEatTransformFactorVisible));
   Setting noLimbSwing = this.registerSetting(new BooleanSetting("NoLimbSwing", "Allows you to cancel limb swing animations", false));
   Setting noInterpolation = this.registerSetting(new BooleanSetting("NoInterpolation", "Entities will be rendered at their server positions", false, this::isNoInterpolationVisible));

   public Animations() {
      super("Animations", "Allows you to modify vanilla animations", ModuleCategory.RENDER);
   }

   @EventListener
   public void onUpdateHeldItems(UpdateHeldItemsEvent updateHeldItemsEvent) {
      if ((Boolean)this.noSwitchAnimation.atx()) {
         updateHeldItemsEvent.aim();
      }
   }

   @EventListener
   public void onGetHandSwingDuration(GetHandSwingDurationEvent getHandSwingDurationEvent) {
      if ((Boolean)this.swingSpeed.atx()) {
         getHandSwingDurationEvent.aim();
         getHandSwingDurationEvent.Mw((Integer)this.swingFactor.atx());
         getHandSwingDurationEvent.My((Boolean)this.selfOnly.atx());
      }
   }

   @EventListener
   public void onApplyEatOrDrinkTransformation(ApplyEatOrDrinkTransformationEvent applyEatOrDrinkTransformationEvent) {
      if ((Boolean)this.eatTransform.atx()) {
         applyEatOrDrinkTransformationEvent.aim();
         applyEatOrDrinkTransformationEvent.Fy((Float)this.eatTransformFactor.atx());
      }
   }

   @EventListener
   public void onGetSpeed(GetSpeedEvent getSpeedEvent) {
      if ((Boolean)this.noLimbSwing.atx()) {
         getSpeedEvent.aim();
         getSpeedEvent.as();
      }
   }

   @EventListener
   public void onUpdateTrackedPositionAndAngles(UpdateTrackedPositionAndAnglesEvent updateTrackedPositionAndAnglesEvent) {
      if ((Boolean)this.noLimbSwing.atx() && (Boolean)this.noInterpolation.atx()) {
         LivingEntity temp = updateTrackedPositionAndAnglesEvent.getLiving();
         double temp2 = updateTrackedPositionAndAnglesEvent.aD();
         double d = updateTrackedPositionAndAnglesEvent.aF();
         double d2 = updateTrackedPositionAndAnglesEvent.aE();
         double d3 = temp2;
         temp.setPos(d3, d2, d);
         float f = updateTrackedPositionAndAnglesEvent.aG();
         updateTrackedPositionAndAnglesEvent.getLiving().setYaw(f);
         float f2 = updateTrackedPositionAndAnglesEvent.aH();
         updateTrackedPositionAndAnglesEvent.getLiving().setPitch(f2);
      }
   }

   @EventListener
   public void onEquipProgressMainhand(EquipProgressMainhandEvent equipProgressMainhandEvent) {
      if ((Boolean)this.oldSwingAnimation.atx()) {
         equipProgressMainhandEvent.aim();
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof BundleS2CPacket) {
            BundleS2CPacket bundleS2CPacket = (BundleS2CPacket)packet2;
            ArrayList arrayList = new ArrayList();
            Iterator iterator2 = bundleS2CPacket.getPackets().iterator();

            while (iterator2.hasNext()) {
               Packet packet3 = (Packet)iterator2.next();
               if (!(packet3 instanceof EntityAnimationS2CPacket)
                  || !(Boolean)this.oldSwingAnimation.atx()
                  || ((EntityAnimationS2CPacket)packet3).getEntityId() != client.player.getId()
                  || ((EntityAnimationS2CPacket)packet3).getAnimationId() != 0 && ((EntityAnimationS2CPacket)packet3).getAnimationId() != 3) {
                  arrayList.add(packet3);
               }
            }

            ((BundlePacketMixin)bundleS2CPacket).setIterable(arrayList);
         } else {
            packet2 = channelRead0Event.getPacket();
            if (packet2 instanceof EntityAnimationS2CPacket
               && (Boolean)this.oldSwingAnimation.atx()
               && ((EntityAnimationS2CPacket)packet2).getEntityId() == client.player.getId()
               && (((EntityAnimationS2CPacket)packet2).getAnimationId() == 0 || ((EntityAnimationS2CPacket)packet2).getAnimationId() == 3)) {
               channelRead0Event.aim();
            }
         }
      }
   }

   private Boolean isNoInterpolationVisible() {
      return (Boolean)this.noLimbSwing.atx();
   }

   private Boolean isEatTransformFactorVisible() {
      return (Boolean)this.eatTransform.atx();
   }

   private Boolean isSwingFactorVisible() {
      return (Boolean)this.swingSpeed.atx();
   }
}
