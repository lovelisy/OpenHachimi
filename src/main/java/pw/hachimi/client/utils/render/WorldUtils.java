package pw.hachimi.client.utils.render;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.CobwebBlock;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.GameMode;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import pw.hachimi.client.Mc;
import pw.hachimi.client.mixin.Vec3dMixin;
import pw.hachimi.client.utils.world.BoxUtils;

public final class WorldUtils {
   public static boolean a;

   public static BlockPos MX(PlayerEntity player2, boolean flag) {
      return (BlockPos)(flag ? player2.getBlockPos() : new Mutable(Math.floor(player2.getX()), Math.round(player2.getY()), Math.floor(player2.getZ())));
   }

   public static List MY() {
      return MZ(Mc.client.player.getPos());
   }

   public static List MZ(Vec3d vec) {
      return BoxUtils.jF(new Box(vec.getX() - 0.3, vec.getY(), vec.getZ() - 0.3, vec.getX() + 0.3, vec.getY() + 0.4, vec.getZ() + 0.3));
   }

   public static List Na() {
      return BoxUtils.jF(
         new Box(
            Mc.client.player.getX() - 0.3,
            Mc.client.player.getY() + 1.5,
            Mc.client.player.getZ() - 0.3,
            Mc.client.player.getX() + 0.3,
            Mc.client.player.getY() + 1.5,
            Mc.client.player.getZ() + 0.3
         )
      );
   }

   public static boolean Nb(PlayerEntity player2, BlockPos pos, boolean flag, boolean flag2) {
      Box temp = player2.getBoundingBox();
      Box box = new Box(pos);
      if (temp.intersects(box)) {
         return false;
      }

      Direction[] directions;
      if (flag2) {
         directions = BoxUtils.a_;
         if (directions == null) {
            return false;
         }
      } else {
         directions = BoxUtils.d;
      }

      int n = directions.length;

      for (int n2 = 0; n2 < n; n2++) {
         Direction direction = directions[n2];
         BlockPos pos2 = pos.offset(direction);
         BlockPos pos3 = MX(player2, false);
         if (flag || pos.getY() != pos3.up().getY()) {
            temp = player2.getBoundingBox();
            Box box2 = new Box(pos2);
            if (temp.intersects(box2)) {
               return true;
            }
         }
      }

      return false;
   }

   public static float Nc() {
      return Mc.client.player.getHealth() + Mc.client.player.getAbsorptionAmount();
   }

   public static int Nd(float f, float f2) {
      EntityType temp = Mc.client.player.getType();
      TagKey tagKey = EntityTypeTags.FALL_DAMAGE_IMMUNE;
      if (temp.isIn(tagKey)) {
         return 0;
      }

      RegistryEntry registryEntry = StatusEffects.JUMP_BOOST;
      StatusEffectInstance statusEffectInstance = Mc.client.player.getStatusEffect(registryEntry);
      float f3;
      if (statusEffectInstance == null) {
         f3 = 0.0F;
      } else {
         f3 = statusEffectInstance.getAmplifier() + 1;
      }

      float f4 = f3;
      return MathHelper.ceil((f - 3.0F - f4) * f2);
   }

   public static boolean Ne(Item item2) {
      ItemStack stack = Mc.client.player.getMainHandStack();
      if (!stack.isEmpty() && stack.getItem() == item2) {
         return true;
      }

      ItemStack stack2 = Mc.client.player.getOffHandStack();
      return !stack2.isEmpty() && stack2.getItem() == item2;
   }

   public static boolean Nf() {
      KeyBinding[] keyBindings = Mc.client.options.hotbarKeys;
      int n = keyBindings.length;

      for (int n2 = 0; n2 < n; n2++) {
         if (keyBindings[n2].isPressed()) {
            return true;
         }
      }

      return false;
   }

   public static boolean Ng(double d2) {
      Iterator iterator2 = BoxUtils.jE(Mc.client.player.getBoundingBox().expand(d2)).iterator();

      while (iterator2.hasNext()) {
         BlockPos pos = (BlockPos)iterator2.next();
         if (Mc.client.world.getBlockState(pos).getBlock() instanceof CobwebBlock) {
            return true;
         }
      }

      return false;
   }

   public static List Nh(Box box) {
      ArrayList arrayList = new ArrayList();
      Iterator iterator2 = BoxUtils.jE(box).iterator();

      while (iterator2.hasNext()) {
         BlockPos pos = (BlockPos)iterator2.next();
         if (Mc.client.world.getBlockState(pos).getBlock() instanceof CobwebBlock) {
            arrayList.add(pos);
         }
      }

      return arrayList;
   }

   public static void Ni() {
      double d = MathHelper.floor(Mc.client.player.getX()) + 0.5;
      double d2 = MathHelper.floor(Mc.client.player.getZ()) + 0.5;
      Mc.client.player.setPosition(d, Mc.client.player.getY(), d2);
      Mc.client.player.networkHandler.sendPacket(
         new PlayerMoveC2SPacket.PositionAndOnGround(Mc.client.player.getX(), Mc.client.player.getY(), Mc.client.player.getZ(), Mc.client.player.isOnGround())
      );
   }

   public static boolean Nj(Vec3d vec) {
      ClientWorld temp = Mc.client.world;
      RaycastContext raycastContext = new RaycastContext(Mc.client.player.getEyePos(), vec, ShapeType.COLLIDER, FluidHandling.NONE, Mc.client.player);
      BlockHitResult blockHitResult = temp.raycast(raycastContext);
      boolean flag;
      if (blockHitResult != null && blockHitResult.getType() != Type.MISS) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public static boolean Nk(Entity entity2) {
      Vec3d vec = b();
      Vec3d vec2 = b();
      ((Vec3dMixin)vec).setX(Mc.client.player.getX());
      ((Vec3dMixin)vec).setY(Mc.client.player.getY() + Mc.client.player.getStandingEyeHeight());
      ((Vec3dMixin)vec).setZ(Mc.client.player.getZ());
      ((Vec3dMixin)vec2).setX(entity2.getX());
      ((Vec3dMixin)vec2).setY(entity2.getY());
      ((Vec3dMixin)vec2).setZ(entity2.getZ());
      ClientWorld temp = Mc.client.world;
      RaycastContext raycastContext = new RaycastContext(vec, vec2, ShapeType.COLLIDER, FluidHandling.NONE, Mc.client.player);
      if (temp.raycast(raycastContext).getType() == Type.MISS) {
         ((Vec3dMixin)vec2).setX(entity2.getX());
         ((Vec3dMixin)vec2).setY(entity2.getY() + entity2.getStandingEyeHeight());
         ((Vec3dMixin)vec2).setZ(entity2.getZ());
         temp = Mc.client.world;
         RaycastContext raycastContext2 = new RaycastContext(vec, vec2, ShapeType.COLLIDER, FluidHandling.NONE, Mc.client.player);
         if (temp.raycast(raycastContext2).getType() == Type.MISS) {
         }
      } else {
         ((Vec3dMixin)vec2).setX(entity2.getX());
         ((Vec3dMixin)vec2).setY(entity2.getY() + entity2.getStandingEyeHeight());
         ((Vec3dMixin)vec2).setZ(entity2.getZ());
         temp = Mc.client.world;
         RaycastContext raycastContext3 = new RaycastContext(vec, vec2, ShapeType.COLLIDER, FluidHandling.NONE, Mc.client.player);
         if (temp.raycast(raycastContext3).getType() != Type.MISS) {
            return false;
         }
      }

      return true;
   }

   public static double Nl(double d2, double d3, double d4, double d5) {
      return Math.sqrt(Nt(d2, 0.0, d3, d4, 0.0, d5));
   }

   public static double Nm(Entity entity2) {
      return Np(entity2.getX(), entity2.getY(), entity2.getZ());
   }

   public static double Nn(BlockPos pos) {
      return Np(pos.getX(), pos.getY(), pos.getZ());
   }

   public static double No(Vec3d vec) {
      return Np(vec.getX(), vec.getY(), vec.getZ());
   }

   public static double Np(double d2, double d3, double d4) {
      return Math.sqrt(Ns(d2, d3, d4));
   }

   public static double Nq(Entity entity2) {
      return Ns(entity2.getX(), entity2.getY(), entity2.getZ());
   }

   public static double Nr(BlockPos pos) {
      return Ns(pos.getX(), pos.getY(), pos.getZ());
   }

   public static double Ns(double d2, double d3, double d4) {
      return Nt(Mc.client.player.getX(), Mc.client.player.getY(), Mc.client.player.getZ(), d2, d3, d4);
   }

   public static double Nt(double d2, double d3, double d4, double d5, double d6, double d7) {
      double d8 = d2 - d5;
      double d9 = d3 - d6;
      double d10 = d4 - d7;
      return org.joml.Math.fma(d8, d8, org.joml.Math.fma(d9, d9, d10 * d10));
   }

   public static boolean Nu(Entity entity2, double d2) {
      boolean flag;
      if (Ns(entity2.getX(), entity2.getY(), entity2.getZ()) <= d2 * d2) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static boolean Nv(Vec3d vec, double d2) {
      boolean flag;
      if (Ns(vec.getX(), vec.getY(), vec.getZ()) <= d2 * d2) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static boolean Nw(BlockPos pos, double d2) {
      boolean flag;
      if (Ns(pos.getX(), pos.getY(), pos.getZ()) <= d2 * d2) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static boolean Nx(double d2, double d3, double d4, double d5) {
      boolean flag;
      if (Ns(d2, d3, d4) <= d5 * d5) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static double Ny(double d2, double d3, double d4) {
      return Math.sqrt(NA(d2, d3, d4));
   }

   public static double Nz(Entity entity2) {
      EntityPose entityPose = entity2.getPose();
      return Ny(entity2.getX(), entity2.getY() + entity2.getEyeHeight(entityPose), entity2.getZ());
   }

   public static double NA(double d2, double d3, double d4) {
      Vec3d vec = Mc.client.gameRenderer.getCamera().getPos();
      return Nt(vec.x, vec.y, vec.z, d2, d3, d4);
   }

   public static double NB(Entity entity2) {
      EntityPose entityPose = entity2.getPose();
      return NA(entity2.getX(), entity2.getY() + entity2.getEyeHeight(entityPose), entity2.getZ());
   }

   public static boolean NC(Entity entity2, double d2) {
      boolean flag;
      if (NA(entity2.getX(), entity2.getY(), entity2.getZ()) <= d2 * d2) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static boolean ND(Vec3d vec, double d2) {
      boolean flag;
      if (NA(vec.getX(), vec.getY(), vec.getZ()) <= d2 * d2) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static boolean NE(BlockPos pos, double d2) {
      boolean flag;
      if (NA(pos.getX(), pos.getY(), pos.getZ()) <= d2 * d2) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static boolean NF(double d2, double d3, double d4, double d5) {
      return NA(d2, d3, d4) <= d5 * d5;
   }

   public static boolean NG(Entity entity2) {
      return NJ(entity2.getX(), entity2.getY(), entity2.getZ());
   }

   public static boolean NH(Vec3d vec) {
      return NJ(vec.getX(), vec.getY(), vec.getZ());
   }

   public static boolean NI(BlockPos pos) {
      return NJ(pos.getX(), pos.getY(), pos.getZ());
   }

   public static boolean NJ(double d2, double d3, double d4) {
      boolean flag;
      if (Nt(Mc.client.player.getX(), Mc.client.player.getEyeY(), Mc.client.player.getZ(), d2, d3, d4)
         <= Mc.client.player.getBlockInteractionRange() * Mc.client.player.getBlockInteractionRange()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static GameMode NK(short s, char c, int n) {
      if (s >= 0 && c > 0) {
         if (Mc.client.player == null) {
            return null;
         }

         if (n >= 0) {
            UUID uuid = Mc.client.player.getUuid();
            PlayerListEntry playerListEntry = Mc.client.getNetworkHandler().getPlayerListEntry(uuid);
            if (n >= 0 && n >= 0) {
               if (playerListEntry == null) {
                  return null;
               }

               if (s >= 0) {
                  return playerListEntry.getGameMode();
               }
            }
         }
      }

      return null;
   }

   public static float NL() {
      return Mc.client.player.getHealth() + Mc.client.player.getAbsorptionAmount();
   }

   public static boolean NM() {
      boolean flag;
      if (Mc.client.player.isAlive() && !Mc.client.player.isDead()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static int NN() {
      if (Mc.client.getNetworkHandler() == null) {
         return 0;
      }

      UUID uuid = Mc.client.player.getUuid();
      PlayerListEntry playerListEntry = Mc.client.getNetworkHandler().getPlayerListEntry(uuid);
      return (PlayerListEntry)playerListEntry == null ? 0 : playerListEntry.getLatency();
   }

   public static boolean NO(LivingEntity living) {
      Iterator iterator2 = living.getArmorItems().iterator();

      while (iterator2.hasNext()) {
         ItemStack stack = (ItemStack)iterator2.next();
         if (stack != null && !stack.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private static Vec3d b() {
      return new Vec3d(0.0, 0.0, 0.0);
   }
}
