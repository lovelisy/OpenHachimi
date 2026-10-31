package pw.hachimi.client.module;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.UnknownEvent3;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.mixin.PlayerMoveC2SPacketMixin;
import pw.hachimi.client.model.CheatPlaceMode;
import pw.hachimi.client.model.GrimNCPMode;
import pw.hachimi.client.model.UnidentifiedOption5;
import pw.hachimi.client.model.network.NormalPacketMode;
import pw.hachimi.client.model.player.DisableLimitMode;
import pw.hachimi.client.model.player.SwingMode;
import pw.hachimi.client.model.render.NormalHeadOnlyMode;
import pw.hachimi.client.model.world.VanillaNcpMode;
import pw.hachimi.client.module.exploit.Disabler;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class HitResultWorldHelper extends Module3 {
   private static HitResultWorldHelper INSTANCE;
   private final Stopwatch stopwatch = new StopwatchImpl();
   Setting cheatPlaceMode = this.registerSetting(new EnumSetting("Page", "The pages", CheatPlaceMode.CHEAT, CheatPlaceMode.values()));
   Setting grimNCPMode = this.registerSetting(new EnumSetting("Mode", "Applies anticheat optimizations", GrimNCPMode.VANILLA, GrimNCPMode.values(), this::isGrimNCPModeVisible));
   Setting booleanSetting1 = this.registerSetting(new BooleanSetting("Pick", "Use pick to swap item (SwapAction-Pick)", true, this::isBooleanSetting1Visible));
   Setting numberSetting1 = this.registerSetting(new NumberSetting("Damage", "Multiplier for mining progress required", 0.0, 1.0, 2.0, this::isNumberSetting1Visible));
   Setting vanillaNcpMode = this.registerSetting(new EnumSetting("Placement", "Check if the direction can be clicked (used to bypass anti cheat)", VanillaNcpMode.Vanilla, VanillaNcpMode.values(), this::isVanillaNcpModeVisible));
   Setting normalPacketMode = this.registerSetting(new EnumSetting("Place", "Place mode", NormalPacketMode.PACKET, NormalPacketMode.values(), this::isNormalPacketModeVisible));
   Setting swingMode = this.registerSetting(new EnumSetting("PlaceSwing", "Swing side for block interaction", SwingMode.Normal, SwingMode.values(), this::isSwingModeVisible));
   Setting booleanSetting2 = this.registerSetting(new BooleanSetting("MultiPlace", "Allows placement on unconfirmed blocks", true, this::isBooleanSetting2Visible));
   Setting booleanSetting3 = this.registerSetting(new BooleanSetting("MultiPoint", "Use the closest hit vector for block interaction", true, this::isBooleanSetting3Visible));
   Setting booleanSetting4 = this.registerSetting(new BooleanSetting("ClosestDirection", "Use the closest direction for block interaction", true, this::isBooleanSetting4Visible));
   Setting booleanSetting5 = this.registerSetting(new BooleanSetting("GrimAirplace", "Allow you air place on Grim AntiCheat (Version restrictions)", false, this::isBooleanSetting5Visible));
   Setting booleanSetting6 = this.registerSetting(new BooleanSetting("SneakingSpoof", "When interact block is clickable send sneak packet", false, this::isBooleanSetting6Visible));
   Setting swingMode2 = this.registerSetting(new EnumSetting("AttackSwing", "Swing side for attack entity", SwingMode.Normal, SwingMode.values(), this::isSwingMode2Visible));
   Setting numberSetting2 = this.registerSetting(new NumberSetting("AttackDelay", "Delay (tick) for break crystal", 0.0F, 1.0F, 20.0F, this::isNumberSetting2Visible));
   Setting unidentifiedOption5Mode = this.registerSetting(
      new EnumSetting("Attack", "Attack mode", UnidentifiedOption5.a_, UnidentifiedOption5.values(), this::isUnidentifiedOption5ModeVisible)
   );
   Setting booleanSetting7 = this.registerSetting(new BooleanSetting("MovementSync", "Fixes movement on Grim when rotating", false, this::isBooleanSetting7Visible));
   Setting booleanSetting8 = this.registerSetting(new BooleanSetting("MouseSensFix", "Fixes movement on Grim when applying mouse sensitivity", false, this::isBooleanSetting8Visible));
   Setting normalHeadOnlyMode = this.registerSetting(new EnumSetting("Animation", "Rotation apply player animation", NormalHeadOnlyMode.Normal, NormalHeadOnlyMode.values(), this::isNormalHeadOnlyModeVisible));
   Setting disableLimitMode = this.registerSetting(new EnumSetting("RotateBack", "Snap back to bypass the grim check", DisableLimitMode.Disable, DisableLimitMode.values(), this::isDisableLimitModeVisible));
   Setting booleanSetting9 = this.registerSetting(new BooleanSetting("RaytraceFix", "Allows you to spoof your raytrace", false, this::isBooleanSetting9Visible));
   private float v = Float.NaN;
   public static boolean bu;

   public HitResultWorldHelper() {
      super("AntiCheat", "Settings for anticheat configs", ModuleCategory.CLIENT);
      INSTANCE = this;
   }

   public static HitResultWorldHelper Sq() {
      return INSTANCE;
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         PlacementUtils.EZ();
      }
   }

   @EventListener(priority = 10000)
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (client.player != null && client.world != null) {
         if (this.SJ() && (Boolean)this.booleanSetting9.atx()) {
            Packet packet2 = sendImmediatelyEvent.getPacket();
            if (packet2 instanceof PlayerInteractBlockC2SPacket && this.stopwatch.afq(250)) {
               BlockHitResult blockHitResult = ((PlayerInteractBlockC2SPacket)packet2).getBlockHitResult();
               BlockPos pos = blockHitResult.getBlockPos();
               ClientPlayerEntity player2 = client.player;
               ClientWorld temp = client.world;
               Vec3d temp2 = player2.getEyePos();
               Vec3d temp3 = pos.toCenterPos();
               net.minecraft.util.math.Direction direction = blockHitResult.getSide();
               RaycastContext raycastContext = new RaycastContext(temp2, temp3.offset(direction, 0.5), ShapeType.COLLIDER, FluidHandling.NONE, client.player);
               BlockHitResult blockHitResult2 = temp.raycast(raycastContext);
               temp = client.world;
               Box box = client.player.getBoundingBox().stretch(0.0, 0.15, 0.0);
               if (temp.isSpaceEmpty(box) && blockHitResult2 != null && blockHitResult2.getType() == Type.BLOCK && !blockHitResult2.getBlockPos().equals(pos)) {
                  this.v = -75.0F;
                  this.stopwatch.afr(29432, 5265492, (byte)40);
               }
            }
         }

         Packet packet3 = sendImmediatelyEvent.getPacket();
         if (packet3 instanceof PlayerMoveC2SPacket && ((PlayerMoveC2SPacket)packet3).changesLook() && !Float.isNaN(this.v)) {
            ((PlayerMoveC2SPacketMixin)((PlayerMoveC2SPacket)packet3)).hookSetPitch(this.v);
            this.v = Float.NaN;
         }
      }
   }

   @EventListener
   public void St(UnknownEvent3 unknownEvent3) {
      if (unknownEvent3.getEventPhase() == EventPhase.POST && unknownEvent3.getSetting() == this.booleanSetting9 && !(Boolean)this.booleanSetting9.atx()) {
         this.v = Float.NaN;
      }
   }

   public boolean Su() {
      return (Boolean)this.booleanSetting1.atx();
   }

   public boolean Sv() {
      return (Boolean)this.booleanSetting6.atx();
   }

   public VanillaNcpMode Sw() {
      return (VanillaNcpMode)this.vanillaNcpMode.atx();
   }

   public SwingMode Sx() {
      return (SwingMode)this.swingMode2.atx();
   }

   public SwingMode Sy() {
      return (SwingMode)this.swingMode.atx();
   }

   public Float Sz() {
      return (Float)this.numberSetting2.atx();
   }

   public Boolean SA() {
      boolean flag;
      if (this.unidentifiedOption5Mode.atx() == UnidentifiedOption5.b) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public Boolean SB() {
      boolean flag;
      if (this.normalPacketMode.atx() != NormalPacketMode.PACKET && this.normalPacketMode.atx() != NormalPacketMode.SOUND_PACKET) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public Boolean SC() {
      boolean flag;
      if (this.normalPacketMode.atx() == NormalPacketMode.SOUND_PACKET) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public Boolean SD() {
      return (Boolean)this.booleanSetting2.atx();
   }

   public Boolean SE() {
      return (Boolean)this.booleanSetting3.atx();
   }

   public Boolean SF() {
      return (Boolean)this.booleanSetting4.atx();
   }

   public Boolean SG() {
      return (Boolean)this.booleanSetting5.atx();
   }

   public Double SH() {
      return (Double)this.numberSetting1.atx();
   }

   public boolean SI() {
      boolean flag;
      if (this.grimNCPMode.atx() == GrimNCPMode.GRIM) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean SJ() {
      boolean flag;
      if (this.grimNCPMode.atx() == GrimNCPMode.N_C_P) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean SK() {
      boolean flag;
      if (!(Boolean)this.booleanSetting7.atx() || Disabler.kt().awg() && Disabler.kt().kz()) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public DisableLimitMode SL() {
      return (DisableLimitMode)this.disableLimitMode.atx();
   }

   public boolean SM() {
      return (Boolean)this.booleanSetting8.atx();
   }

   public NormalHeadOnlyMode SN() {
      return (NormalHeadOnlyMode)this.normalHeadOnlyMode.atx();
   }

   private Boolean isBooleanSetting9Visible() {
      boolean flag;
      if (this.grimNCPMode.atx() == GrimNCPMode.N_C_P && ((CheatPlaceMode)this.cheatPlaceMode.atx()).xo()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isDisableLimitModeVisible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xo();
   }

   private Boolean isNormalHeadOnlyModeVisible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xo();
   }

   private Boolean isBooleanSetting8Visible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xo();
   }

   private Boolean isBooleanSetting7Visible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xo();
   }

   private Boolean isUnidentifiedOption5ModeVisible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xn();
   }

   private Boolean isNumberSetting2Visible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xn();
   }

   private Boolean isSwingMode2Visible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xn();
   }

   private Boolean isBooleanSetting6Visible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xm();
   }

   private Boolean isBooleanSetting5Visible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xm();
   }

   private Boolean isBooleanSetting4Visible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xm();
   }

   private Boolean isBooleanSetting3Visible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xm();
   }

   private Boolean isBooleanSetting2Visible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xm();
   }

   private Boolean isSwingModeVisible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xm();
   }

   private Boolean isNormalPacketModeVisible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xm();
   }

   private Boolean isVanillaNcpModeVisible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xm();
   }

   private Boolean isNumberSetting1Visible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xl();
   }

   private Boolean isBooleanSetting1Visible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xl();
   }

   private Boolean isGrimNCPModeVisible() {
      return ((CheatPlaceMode)this.cheatPlaceMode.atx()).xl();
   }
}
