package pw.hachimi.client.module.movement;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.movement.MoveEvent;
import pw.hachimi.client.event.player.GetRotationVectorHookEvent;
import pw.hachimi.client.mixin.FireworkRocketEntityMixin;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.utils.UnidentifiedHelper12;
import pw.hachimi.client.utils.movement.UnidentifiedHelper20;
import pw.hachimi.client.utils.movement.UnidentifiedUtils7;
import pw.hachimi.client.utils.world.BlockPosHelper;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public final class PlayerVec3dModule extends ClientModule {
   private static PlayerVec3dModule INSTANCE;
   UnidentifiedHelper12 unidentifiedHelper12 = p(0.0, 0.0, 0.0);
   UnidentifiedHelper12 unidentifiedHelper122 = p(0.0, 0.0, 0.0);
   public static boolean cl;

   public PlayerVec3dModule() {
      super("GrimTest", "Allows you to change the acceleration of your firework", ModuleCategory.MOVEMENT);
      INSTANCE = this;
   }

   public static PlayerVec3dModule FM() {
      return INSTANCE;
   }

   @EventListener(priority = -99999)
   public void FN(UnidentifiedHelper20 unidentifiedHelper20) {
      double temp = unidentifiedHelper20.aqO() - client.player.lastX;
      double d2 = unidentifiedHelper20.aqQ() - client.player.lastBaseY;
      double d3 = temp;
      this.unidentifiedHelper12 = p(d3, d2, unidentifiedHelper20.aqS() - client.player.lastZ);
      this.unidentifiedHelper122 = p(client.player.getVelocity().x, client.player.getVelocity().y, client.player.getVelocity().z);
   }

   private float FO() {
      GetRotationVectorHookEvent getRotationVectorHookEvent = new GetRotationVectorHookEvent(client.player, client.player.getYaw(), client.player.getPitch());
      EventBusImpl.INSTANCE.rC(getRotationVectorHookEvent);
      return getRotationVectorHookEvent.aik() ? getRotationVectorHookEvent.c : client.player.getPitch();
   }

   private Vec3d FP() {
      GetRotationVectorHookEvent getRotationVectorHookEvent = new GetRotationVectorHookEvent(client.player, client.player.getYaw(), client.player.getPitch());
      EventBusImpl.INSTANCE.rC(getRotationVectorHookEvent);
      return getRotationVectorHookEvent.aik() ? getRotationVectorHookEvent.nF() : client.player.getRotationVector();
   }

   public UnidentifiedHelper12 FQ(float f2, float f3) {
      float f4 = f3 * (float) (Math.PI / 180.0);
      float f5 = -f2 * (float) (Math.PI / 180.0);
      float f6 = MathHelper.cos(f5);
      float f7 = MathHelper.sin(f5);
      float f8 = MathHelper.cos(f4);
      float f9 = MathHelper.sin(f4);
      return p(f7 * f8, -f9, f6 * f8);
   }

   @EventListener
   public void onMove(MoveEvent moveEvent) {
      if (this.FS()) {
         moveEvent.aim();
         Vec3d vec = this.FP();
         UnidentifiedHelper12 unidentifiedHelper123 = this.FT(this.FU(this.unidentifiedHelper122, p(vec.x, vec.y, vec.z)));
         moveEvent.ym(unidentifiedHelper123.a_);
         moveEvent.yo(unidentifiedHelper123.b_);
         moveEvent.yq(unidentifiedHelper123.c);
         moveEvent.aim();
      }
   }

   private boolean FS() {
      Iterator iterator2 = client.world.getEntities().iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (entity2 instanceof FireworkRocketEntity
            && ((FireworkRocketEntityMixin)entity2).hookWasShotByEntity()
            && ((FireworkRocketEntityMixin)entity2).hookGetShooter() == client.player) {
            return true;
         }
      }

      return false;
   }

   public UnidentifiedHelper12 FT(UnidentifiedHelper12 unidentifiedHelper123) {
      Vec3d vec = this.FP();
      UnidentifiedHelper12 unidentifiedHelper124 = this.FQ(client.player.lastYaw, client.player.lastPitch);
      double d2 = Math.min(-0.05, vec.getX()) + Math.min(-0.05, unidentifiedHelper124.Qg());
      double d3 = Math.min(-0.05, vec.getY()) + Math.min(-0.05, unidentifiedHelper124.Qh());
      double d4 = Math.min(-0.05, vec.getZ()) + Math.min(-0.05, unidentifiedHelper124.Qi());
      double d5 = Math.max(0.05, vec.getX()) + Math.max(0.05, unidentifiedHelper124.Qg());
      double d6 = Math.max(0.05, vec.getY()) + Math.max(0.05, unidentifiedHelper124.Qh());
      double d7 = Math.max(0.05, vec.getZ()) + Math.max(0.05, unidentifiedHelper124.Qi());
      d2 *= 1.7;
      d3 *= 1.7;
      d4 *= 1.7;
      d5 *= 1.7;
      d6 *= 1.7;
      d7 *= 1.7;
      d2 = Math.max(-1.7, d2);
      d3 = Math.max(-1.7, d3);
      d4 = Math.max(-1.7, d4);
      d5 = Math.min(1.7, d5);
      d6 = Math.min(1.7, d6);
      d7 = Math.min(1.7, d7);
      BlockPosHelper blockPosHelper = n(d2, d3, d4, d5, d6, d7);
      UnidentifiedHelper12 unidentifiedHelper125 = unidentifiedHelper123.Qq(49902194132505L);
      UnidentifiedHelper12 unidentifiedHelper126 = unidentifiedHelper123.Qq(49902194132505L);
      BlockPosHelper blockPosHelper2 = new BlockPosHelper(unidentifiedHelper125, unidentifiedHelper126);
      blockPosHelper2.pU();
      double d8 = Math.min(0.0, blockPosHelper.a_ - unidentifiedHelper123.Qg());
      double d9 = Math.min(0.0, blockPosHelper.b - unidentifiedHelper123.Qh());
      double d10 = Math.min(0.0, blockPosHelper.c - unidentifiedHelper123.Qi());
      double d11 = Math.max(0.0, blockPosHelper.d - unidentifiedHelper123.Qg());
      double d12 = Math.max(0.0, blockPosHelper.e - unidentifiedHelper123.Qh());
      double d13 = Math.max(0.0, blockPosHelper.f - unidentifiedHelper123.Qi());
      blockPosHelper2.pV(d8, d9, d10);
      blockPosHelper2.pW(d11, d12, d13);
      Vec3d vec2 = this.FP().multiply(10.0);
      return UnidentifiedUtils7.akD(2363386304566L, p(vec2.x, vec2.y, vec2.z), blockPosHelper2.pY(), blockPosHelper2.pX());
   }

   public UnidentifiedHelper12 FU(UnidentifiedHelper12 unidentifiedHelper123, UnidentifiedHelper12 unidentifiedHelper124) {
      float f2 = UnidentifiedUtils7.akG(this.FO());
      double d2 = Math.sqrt(unidentifiedHelper124.Qg() * unidentifiedHelper124.Qg() + unidentifiedHelper124.Qi() * unidentifiedHelper124.Qi());
      double d3 = unidentifiedHelper123.Qq(49902194132505L).Qk().Qt();
      double d4 = unidentifiedHelper124.Qt();
      double d5 = Math.cos(f2);
      d5 = (float)(d5 * d5 * Math.min(1.0, d4 / 0.4));
      double d6 = 0.08;
      if (client.player.getVelocity().getY() <= 0.0) {
         RegistryEntry registryEntry = StatusEffects.SLOW_FALLING;
         if (client.player.hasStatusEffect(registryEntry)) {
            d6 = Math.min(0.08, 0.01);
         }
      }

      unidentifiedHelper123.Qn(0.0, d6 * (-1.0 + d5 * 0.75), '\u0000', 72181216, 0.0, (short)20956);
      if (unidentifiedHelper123.Qh() < 0.0 && d2 > 0.0) {
         double d7 = unidentifiedHelper123.Qh() * -0.1 * d5;
         unidentifiedHelper123.Qn(unidentifiedHelper124.Qg() * d7 / d2, d7, '\u0000', 72181216, unidentifiedHelper124.Qi() * d7 / d2, (short)20956);
      }

      if (f2 < 0.0F && d2 > 0.0) {
         double d8 = d3 * -UnidentifiedUtils7.akI(f2) * 0.04;
         unidentifiedHelper123.Qn(-unidentifiedHelper124.Qg() * d8 / d2, d8 * 3.2, '\u0000', 72181216, -unidentifiedHelper124.Qi() * d8 / d2, (short)20956);
      }

      if (d2 > 0.0) {
         unidentifiedHelper123.Qn((unidentifiedHelper124.Qg() / d2 * d3 - unidentifiedHelper123.Qg()) * 0.1, 0.0, '\u0000', 72181216, (unidentifiedHelper124.Qi() / d2 * d3 - unidentifiedHelper123.Qi()) * 0.1, (short)20956);
      }

      return unidentifiedHelper123;
   }

   private static BlockPosHelper n(double d2, double d3, double d4, double d5, double d6, double d7) {
      return new BlockPosHelper(d2, d3, d4, d5, d6, 585733871, d7);
   }

   private static UnidentifiedHelper12 p(double d2, double d3, double d4) {
      return new UnidentifiedHelper12(d2, d3, d4);
   }
}
