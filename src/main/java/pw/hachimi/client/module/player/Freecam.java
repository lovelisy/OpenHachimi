package pw.hachimi.client.module.player;

import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.OnKeyEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.UpdateEvent;
import pw.hachimi.client.event.player.UpdateDeathEvent;
import pw.hachimi.client.event.player.UpdateRotationEvent;
import pw.hachimi.client.event.render.BobViewEvent;
import pw.hachimi.client.event.render.RenderEvent14;
import pw.hachimi.client.event.render.RenderFirstPersonItem_2Event;
import pw.hachimi.client.event.world.CameraPositionVecEvent;
import pw.hachimi.client.event.world.GetCameraPosVecEvent;
import pw.hachimi.client.event.world.UpdatePositionEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.player.FreecamPlayerCameraMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.player.GameOptionsKeyBindingKeyboardInput;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.KeybindSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper17;
import pw.hachimi.client.utils.movement.UnidentifiedHelper21;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.world.Vec3dEntityUtils2;
import pw.hachimi.eventbus.annotation.EventListener;

public class Freecam extends ClientModule {
   private static Freecam INSTANCE;
   public Vec3d vec;
   public Vec3d vec2;
   public float c;
   public float d;
   public boolean e = false;
   public Setting speed = this.registerSetting(new NumberSetting("Speed", "The move speed of the camera", 0.1F, 4.0F, 10.0F));
   Setting controlKey = this.registerSetting(new KeybindSetting("ControlKey", "", m(this.Kn() + "-control", Freecam::isControlKeyVisible)));
   Setting toggleControl = this.registerSetting(new BooleanSetting("ToggleControl", "Allows toggling control key instead of holding", false));
   Setting interact = this.registerSetting(new EnumSetting("Interact", "The interaction type of the camera", FreecamPlayerCameraMode.CAMERA, FreecamPlayerCameraMode.values()));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Rotate to the point of interaction", false));
   public static boolean fo;

   public Freecam() {
      super("Freecam", "Allows you to control the camera separately from the player", ModuleCategory.PLAYER);
      INSTANCE = this;
   }

   public static Freecam gP() {
      return INSTANCE;
   }

   @Override
   protected void awd() {
      if (client.player != null) {
         this.e = false;
         this.vec = client.gameRenderer.getCamera().getPos();
         this.vec2 = this.vec;
         this.c = client.player.getYaw();
         this.d = client.player.getPitch();
         ClientPlayerEntity temp = client.player;
         GameOptionsKeyBindingKeyboardInput gameOptionsKeyBindingKeyboardInput = new GameOptionsKeyBindingKeyboardInput(this, client.options);
         temp.input = gameOptionsKeyBindingKeyboardInput;
      }
   }

   @Override
   public void awe() {
      if (client.player != null) {
         ClientPlayerEntity temp = client.player;
         KeyboardInput keyboardInput = new KeyboardInput(client.options);
         temp.input = keyboardInput;
      }
   }

   @EventListener
   public void gQ(UpdateDeathEvent updateDeathEvent) {
      if (updateDeathEvent.getLiving() == client.player) {
         this.awc();
      }
   }

   @EventListener
   public void onOnKey(OnKeyEvent onKeyEvent) {
      if (onKeyEvent.KD() != 2 && onKeyEvent.KC() == ((UnidentifiedHelper17)this.controlKey.atx()).agt()) {
         if (!(Boolean)this.toggleControl.atx()) {
            boolean flag;
            if (onKeyEvent.KD() == 1) {
               flag = true;
            } else {
               flag = false;
            }

            this.e = flag;
         } else if (onKeyEvent.KD() == 1) {
            boolean flag2;
            if (!this.e) {
               flag2 = true;
            } else {
               flag2 = false;
            }

            this.e = flag2;
         }
      }
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      this.awc();
   }

   @EventListener
   public void onUpdatePosition(UpdatePositionEvent updatePositionEvent) {
      Vec3d vec3;
      if (this.e) {
         vec3 = this.vec;
      } else {
         Vec3d temp = this.vec2;
         Vec3d temp2 = this.vec;
         double d2 = updatePositionEvent.DD();
         Vec3d vec4 = temp2;
         vec3 = temp.lerp(vec4, d2);
      }

      updatePositionEvent.DF(vec3);
   }

   @EventListener
   public void onUpdateRotation(UpdateRotationEvent updateRotationEvent) {
      updateRotationEvent.EX(new Vec2f(this.c, this.d));
   }

   @EventListener
   public void onUpdate(UpdateEvent updateEvent) {
      if (!this.e) {
         updateEvent.aim();
         this.he(updateEvent.lR(), updateEvent.lS());
      }
   }

   @EventListener
   public void onCameraPositionVec(CameraPositionVecEvent cameraPositionVecEvent) {
      if (cameraPositionVecEvent.getEntity() == client.player) {
         if (!this.e && this.interact.atx() == FreecamPlayerCameraMode.CAMERA) {
            cameraPositionVecEvent.setVec(this.vec);
         }
      }
   }

   @EventListener
   public void onGetCameraPosVec(GetCameraPosVecEvent getCameraPosVecEvent) {
      if (getCameraPosVecEvent.getEntity() == client.player) {
         if (!this.e && this.interact.atx() == FreecamPlayerCameraMode.CAMERA) {
            getCameraPosVecEvent.setVec(RotationUtils.nS(this.d, this.c));
         }
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if (!this.e && (Boolean)this.rotate.atx()) {
            float[] fs = new float[]{this.c, this.d};
            Vec3d vec3 = this.vec;
            HitResult hitResult = Vec3dEntityUtils2.RB(client.player.getBlockInteractionRange(), vec3, fs);
            if (hitResult.getType() == Type.BLOCK) {
               float[] fs2 = RotationUtils.nM(hitResult.getPos());
               Managers.movementTracker.Mh(new UnidentifiedHelper21(1, fs2[0], fs2[1]));
            }
         }
      }
   }

   @EventListener
   public void onRenderEvent14(RenderEvent14 renderEvent14) {
      renderEvent14.aim();
   }

   @EventListener
   public void onRenderFirstPersonItem_2(RenderFirstPersonItem_2Event renderFirstPersonItem2Event) {
      renderFirstPersonItem2Event.aim();
   }

   @EventListener
   public void onBobView(BobViewEvent bobViewEvent) {
      if (this.e) {
         bobViewEvent.aim();
      }
   }

   public float hc(boolean flag, boolean flag2) {
      if (flag == flag2) {
         return 0.0F;
      }

      float f;
      if (flag) {
         f = 1.0F;
      } else {
         f = -1.0F;
      }

      return f;
   }

   public Vec2f hd(float f, float f2, float f3) {
      if (f2 == 0.0F && f3 == 0.0F) {
         return Vec2f.ZERO;
      }

      if (f2 != 0.0F && f3 != 0.0F) {
         Math.sin(Math.PI / 4);
         f2 *= 0.70710677F;
         Math.cos(Math.PI / 4);
         f3 *= 0.70710677F;
      }

      return new Vec2f(
         (float)(f2 * f * -Math.sin(Math.toRadians(this.c)) + f3 * f * Math.cos(Math.toRadians(this.c))),
         (float)(f2 * f * Math.cos(Math.toRadians(this.c)) - f3 * f * -Math.sin(Math.toRadians(this.c)))
      );
   }

   public void he(double d2, double d3) {
      float f = (float)d3 * 0.15F;
      float f2 = (float)d2 * 0.15F;
      this.d += f;
      this.c += f2;
      this.d = MathHelper.clamp(this.d, -90.0F, 90.0F);
   }

   public Vec3d getVec() {
      return this.vec;
   }

   public Vec3d getVec2() {
      return this.vec2;
   }

   public float[] hh() {
      return new float[]{this.c, this.d};
   }

   private static void isControlKeyVisible() {
   }

   private static UnidentifiedHelper17 m(String str, Runnable runnable) {
      return new UnidentifiedHelper17(str, 342, runnable);
   }
}
