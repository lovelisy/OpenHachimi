package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.world.World;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.PopChamsVec3dPlayerHelper;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.render.Vec3dGameRendererHelper;
import pw.hachimi.eventbus.annotation.EventListener;

public class PopChams extends ClientModule {
   Setting onlyOne = this.registerSetting(new BooleanSetting("OnlyOne", "Only allow one ghost per player.", false));
   public Setting fadeOut = this.registerSetting(new BooleanSetting("FadeOut", "Fades out the color.", true));
   Setting ignoreSelf = this.registerSetting(new BooleanSetting("IgnoreSelf", "Ignore your self pops.", true));
   public Setting shape = this.registerSetting(new EnumSetting("Shape", "How the shapes are rendered.", ShapeMode.Both, ShapeMode.values()));
   public Setting side = this.registerSetting(new ColorSetting("Side", "The side color.", o(25)));
   public Setting line = this.registerSetting(new ColorSetting("Line", "The line color.", o(127)));
   public Setting scale = this.registerSetting(new NumberSetting("Scale", "Model scale.", 0.0F, 1.0F, 3.0F));
   public Setting scaleTo = this.registerSetting(new NumberSetting("ScaleTo", "Scale animation", 0.0F, 1.0F, 3.0F));
   public Setting yadd = this.registerSetting(new NumberSetting("Yadd", "Position animation", -0.0F, 5.0F, -5.0F));
   public Setting spin = this.registerSetting(new NumberSetting("Spin", "Add yaw", 0.0F, 0.0F, 720.0F));
   public Setting easing = this.registerSetting(new EnumSetting("Easing", "Animation easing", Easing.LINEAR, Easing.values()));
   public Setting animationTime = this.registerSetting(new NumberSetting("AnimationTime", "Animation length.", 0, 1000, 2000));
   public Setting noSwing = this.registerSetting(new BooleanSetting("NoSwing", "Ghost will never swing.", false));
   public Setting sneaking = this.registerSetting(new BooleanSetting("Sneaking", "Ghost will press shift.", false));
   Setting shine = this.registerSetting(new BooleanSetting("Shine", "Enables model shine", false));
   private final List list = new ArrayList();
   public static boolean dt;

   public PopChams() {
      super("PopChams", "Renders a ghost where players pop totem.", ModuleCategory.RENDER);
   }

   @Override
   public void awe() {
      synchronized (this.list) {
         this.list.clear();
      }
   }

   @EventListener
   private void onChannelRead0(ChannelRead0Event channelRead0Event) {
      Packet packet2 = channelRead0Event.getPacket();
      if (packet2 instanceof EntityStatusS2CPacket && ((EntityStatusS2CPacket)packet2).getStatus() == 35) {
         EntityStatusS2CPacket temp = (EntityStatusS2CPacket)packet2;
         Entity entity2 = temp.getEntity((World)client.world);
         if (entity2 instanceof PlayerEntity) {
            PlayerEntity player2 = (PlayerEntity)entity2;
            if (!(Boolean)this.ignoreSelf.atx() || entity2 != client.player) {
               synchronized (this.list) {
                  if ((Boolean)this.onlyOne.atx()) {
                     this.list.removeIf(obj -> KT(entity2, (PopChamsVec3dPlayerHelper)obj));
                  }

                  PopChamsVec3dPlayerHelper popChamsVec3dPlayerHelper = new PopChamsVec3dPlayerHelper(this, player2);
                  this.list.add(popChamsVec3dPlayerHelper);
               }
            }
         }
      }
   }

   @EventListener(priority = -2)
   private void KQ(Render3DContext render3DContext) {
      synchronized (this.list) {
         if ((Boolean)this.shine.atx()) {
            render3DContext.boxRenderer.YH(render3DContext.matrices);
            render3DContext.boxRenderer.YF();
            Vec3dGameRendererHelper.b = true;
            this.list.removeIf(obj -> KR(render3DContext, (PopChamsVec3dPlayerHelper)obj));
            render3DContext.boxRenderer.YH(render3DContext.matrices);
            Vec3dGameRendererHelper.b = false;
            render3DContext.boxRenderer.YF();
         } else {
            this.list.removeIf(obj -> KS(render3DContext, (PopChamsVec3dPlayerHelper)obj));
         }
      }
   }

   static boolean KR(Render3DContext render3DContext, PopChamsVec3dPlayerHelper popChamsVec3dPlayerHelper) {
      return popChamsVec3dPlayerHelper.asW(render3DContext);
   }

   static boolean KS(Render3DContext render3DContext, PopChamsVec3dPlayerHelper popChamsVec3dPlayerHelper) {
      return popChamsVec3dPlayerHelper.asW(render3DContext);
   }

   static boolean KT(Entity entity2, PopChamsVec3dPlayerHelper popChamsVec3dPlayerHelper) {
      UUID temp = popChamsVec3dPlayerHelper.uuid;
      UUID uuid2 = entity2.getUuid();
      return temp.equals(uuid2);
   }

   private static Color o(int n) {
      return new Color(255, 255, 255, n);
   }
}
