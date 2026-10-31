package pw.hachimi.client.module.movement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction.AxisDirection;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.BlockView;
import net.minecraft.world.CollisionView;
import net.minecraft.world.World;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.movement.IsPushedByFluidsEvent;
import pw.hachimi.client.event.movement.PushAwayFromEvent;
import pw.hachimi.client.event.movement.PushOutOfBlocksEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.ServerManager;
import pw.hachimi.client.mixin.BundlePacketMixin;
import pw.hachimi.client.mixin.ClientWorldMixin2;
import pw.hachimi.client.mixin.EntityVelocityUpdateS2CPacketMixin;
import pw.hachimi.client.mixin.ExplosionS2CPacketMixin;
import pw.hachimi.client.model.DefaultDegreesMode;
import pw.hachimi.client.model.movement.VelocityNormalGrimMode;
import pw.hachimi.client.module.Module5;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.movement.UnidentifiedHelper21;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BlockUtils;
import pw.hachimi.client.utils.world.BoxUtils;
import pw.hachimi.client.utils.world.DirectionUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Velocity extends Module5 {
   private static final int[] a = new int[]{1, 0, -1};
   private static final double[] b = new double[]{0.3, -0.3};
   private static final double c = 0.001;
   private static Velocity INSTANCE;
   Setting knockback = this.registerSetting(new BooleanSetting("Knockback", "Removes player knockback velocity", true));
   Setting explosion = this.registerSetting(new BooleanSetting("Explosion", "Removes player explosion velocity", true));
   Setting mode = this.registerSetting(new EnumSetting("Mode", "The mode for velocity", VelocityNormalGrimMode.NORMAL, VelocityNormalGrimMode.values()));
   Setting horizontal = this.registerSetting(new NumberSetting("Horizontal", "How much horizontal knock-back to take", 0.0F, 0.0F, 100.0F, DefaultDegreesMode.PERCENT, this::isHorizontalVisible));
   Setting vertical = this.registerSetting(new NumberSetting("Vertical", "How much vertical knock-back to take", 0.0F, 0.0F, 100.0F, DefaultDegreesMode.PERCENT, this::isVerticalVisible));
   Setting resetVelocity = this.registerSetting(new BooleanSetting("ResetVelocity", "Reset velocity when get knockback velocity", false, this::isResetVelocityVisible));
   Setting staticSetting = this.registerSetting(new BooleanSetting("Static", "Works when you static", true, this::isStaticSettingVisible));
   Setting grimPacket = this.registerSetting(new BooleanSetting("GrimPacket", "Magic", false, this::isGrimPacketVisible));
   Setting allowLag = this.registerSetting(new BooleanSetting("AllowLag", "Allow use the lag back to prevents velocity", false, this::isAllowLagVisible));
   Setting lagPause = this.registerSetting(new NumberSetting("LagPause", "How long does velocity pause during lag", 0, 50, 200, this::isLagPauseVisible));
   Setting pearlPass = this.registerSetting(new NumberSetting("PearlPass", "How long does velocity work during phase", 0, 100, 500, this::isPearlPassVisible));
   Setting onlyPhased = this.registerSetting(new BooleanSetting("OnlyPhased", "Only prevents velocity in walls", false));
   Setting groundOnly = this.registerSetting(new BooleanSetting("GroundOnly", "Only applies velocity in walls while on ground", false, this::isGroundOnlyVisible));
   Setting trapped = this.registerSetting(new BooleanSetting("Trapped", "Applies velocity while player head is trapped", false, this::isTrappedVisible));
   Setting noRotation = this.registerSetting(new BooleanSetting("NoRotation", "Fix your head", false, this::isNoRotationVisible));
   Setting noPushEntities = this.registerSetting(new BooleanSetting("NoPush-Entities", "Prevents being pushed away from entities", true));
   Setting noPushBlocks = this.registerSetting(new BooleanSetting("NoPush-Blocks", "Prevents being pushed out of blocks", true));
   Setting noPushLiquids = this.registerSetting(new BooleanSetting("NoPush-Liquids", "Prevents being pushed by flowing liquids", true));
   Setting noPushFishhook = this.registerSetting(new BooleanSetting("NoPush-Fishhook", "Prevents being pulled by fishing rod hooks", true));
   private boolean w;
   private boolean x;
   public Stopwatch stopwatch = new StopwatchImpl();

   public Velocity() {
      super("Velocity", "Reduces the amount of player knockback velocity", ModuleCategory.MOVEMENT, 60);
      INSTANCE = this;
   }

   public static Velocity amX() {
      return INSTANCE;
   }

   public static boolean amY() {
      return amZ(client.player, client.player.getBoundingBox());
   }

   public static boolean amZ(Entity entity2, Box box) {
      BlockCollisionSpliterator blockCollisionSpliterator = a(client.world, entity2, box, new BiFunction() {
         @Override
         public Object apply(Object box, Object var2x) {
            return Velocity.anv((Mutable)box, (VoxelShape)var2x);
         }
      });

      while (blockCollisionSpliterator.hasNext()) {
         if (!((VoxelShape)blockCollisionSpliterator.next()).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   @Override
   public String hy() {
      if (this.mode.atx() == VelocityNormalGrimMode.NORMAL) {
         Object[] temp = new Object[2];
         Setting setting2 = this.horizontal;
         DecimalFormat decimalFormat = bo;
         Object[] objs = temp;
         temp = temp;
         Object[] temp2 = objs;
         DecimalFormat temp3 = decimalFormat;
         Object obj = setting2.atx();
         temp2[0] = temp3.format(obj);
         setting2 = this.vertical;
         decimalFormat = bo;
         objs = temp;
         Object[] objs2 = temp;
         temp = objs;
         DecimalFormat temp4 = decimalFormat;
         Object obj2 = setting2.atx();
         temp[1] = temp4.format(obj2);
         return String.format("H:%s%%, V:%s%%", objs2);
      } else {
         return DirectionUtils.apW((Enum)this.mode.atx());
      }
   }

   @Override
   public void awe() {
      if (this.w) {
         if (this.mode.atx() == VelocityNormalGrimMode.GRIM) {
            ServerManager temp = Managers.serverManager;
            Full full = new Full(
               client.player.getX(), client.player.getY(), client.player.getZ(), client.player.lastYaw, client.player.lastPitch, client.player.isOnGround()
            );
            temp.iO('\uf560', full);
            BlockPos pos = this.ani();
            if (pos != null) {
               PlayerActionC2SPacket playerActionC2SPacket = new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, pos, client.player.getHorizontalFacing().getOpposite());
               client.getNetworkHandler().sendPacket((Packet)playerActionC2SPacket);
            }
         }

         this.w = false;
      }
   }

   public boolean ana(ExplosionS2CPacket explosionS2CPacket) {
      if ((Boolean)this.explosion.atx()) {
         if (explosionS2CPacket.getPlayerVelocityX() == 0.0F
            && explosionS2CPacket.getPlayerVelocityZ() == 0.0F
            && (explosionS2CPacket.getPlayerVelocityY() == 0.0F || explosionS2CPacket.getPlayerVelocityY() < 0.0F && client.player.isOnGround())) {
            return false;
         }

         if ((Boolean)this.onlyPhased.atx() && !amY()) {
            return false;
         }

         int n;
         if ((n = ((VelocityNormalGrimMode)this.mode.atx()).ordinal()) != 0) {
            if (n == 1) {
               if (!Managers.antiCheatManager.Uh(((Integer)this.lagPause.atx()).intValue())) {
                  return false;
               }

               if (amY() || this.ani() != null || (Boolean)this.staticSetting.atx() && anr()) {
                  if (!this.x) {
                     this.w = true;
                  }

                  return true;
               }

               return false;
            }
         } else {
            if ((Float)this.horizontal.atx() == 0.0F && (Float)this.vertical.atx() == 0.0F) {
               return true;
            }

            ((ExplosionS2CPacketMixin)explosionS2CPacket).setPlayerVelocityX(explosionS2CPacket.getPlayerVelocityX() * ((Float)this.horizontal.atx() / 100.0F));
            ((ExplosionS2CPacketMixin)explosionS2CPacket).setPlayerVelocityY(explosionS2CPacket.getPlayerVelocityY() * ((Float)this.vertical.atx() / 100.0F));
            ((ExplosionS2CPacketMixin)explosionS2CPacket).setPlayerVelocityZ(explosionS2CPacket.getPlayerVelocityZ() * ((Float)this.horizontal.atx() / 100.0F));
         }
      }

      return false;
   }

   public boolean anb(EntityVelocityUpdateS2CPacket entityVelocityUpdateS2CPacket) {
      if (!(Boolean)this.knockback.atx()) {
         return false;
      }

      if (entityVelocityUpdateS2CPacket.getEntityId() != client.player.getId()) {
         return false;
      }

      if (entityVelocityUpdateS2CPacket.getVelocityX() != 0.0
         || entityVelocityUpdateS2CPacket.getVelocityZ() != 0.0
         || entityVelocityUpdateS2CPacket.getVelocityY() != 0.0 && (!(entityVelocityUpdateS2CPacket.getVelocityY() < 0.0) || !client.player.isOnGround())) {
         if ((Boolean)this.onlyPhased.atx()) {
            if (!amY() && (!(Boolean)this.trapped.atx() || !this.anl())) {
               return false;
            }

            if ((Boolean)this.groundOnly.atx() && !client.player.isOnGround()) {
               return false;
            }
         }

         int n;
         if ((n = ((VelocityNormalGrimMode)this.mode.atx()).ordinal()) != 0) {
            if (n != 1) {
               return false;
            }

            if (!Managers.antiCheatManager.Uh(((Integer)this.lagPause.atx()).intValue()) && this.stopwatch.afq((Number)this.pearlPass.atx())) {
               return false;
            }

            if (amY() || this.ani() != null || (Boolean)this.staticSetting.atx() && anr()) {
               if (!this.x) {
                  this.w = true;
               }

               if ((Boolean)this.resetVelocity.atx()) {
                  ((EntityVelocityUpdateS2CPacketMixin)entityVelocityUpdateS2CPacket).setVelocityX(0);
                  ((EntityVelocityUpdateS2CPacketMixin)entityVelocityUpdateS2CPacket).setVelocityY(0);
                  ((EntityVelocityUpdateS2CPacketMixin)entityVelocityUpdateS2CPacket).setVelocityZ(0);
                  return false;
               } else {
                  return true;
               }
            } else {
               return false;
            }
         } else {
            if ((Float)this.horizontal.atx() != 0.0F || (Float)this.vertical.atx() != 0.0F) {
               ((EntityVelocityUpdateS2CPacketMixin)entityVelocityUpdateS2CPacket).setVelocityX((int)(entityVelocityUpdateS2CPacket.getVelocityX() * ((Float)this.horizontal.atx() / 100.0F)));
               ((EntityVelocityUpdateS2CPacketMixin)entityVelocityUpdateS2CPacket).setVelocityY((int)(entityVelocityUpdateS2CPacket.getVelocityY() * ((Float)this.vertical.atx() / 100.0F)));
               ((EntityVelocityUpdateS2CPacketMixin)entityVelocityUpdateS2CPacket).setVelocityZ((int)(entityVelocityUpdateS2CPacket.getVelocityZ() * ((Float)this.horizontal.atx() / 100.0F)));
               return false;
            }

            if (!(Boolean)this.resetVelocity.atx()) {
               return true;
            }

            ((EntityVelocityUpdateS2CPacketMixin)entityVelocityUpdateS2CPacket).setVelocityX(0);
            ((EntityVelocityUpdateS2CPacketMixin)entityVelocityUpdateS2CPacket).setVelocityY(0);
            ((EntityVelocityUpdateS2CPacketMixin)entityVelocityUpdateS2CPacket).setVelocityZ(0);
            return false;
         }
      } else {
         return !(Boolean)this.resetVelocity.atx();
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null && client.world != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof EntityVelocityUpdateS2CPacket) {
            if (this.anb((EntityVelocityUpdateS2CPacket)packet2)) {
               channelRead0Event.aim();
            }
         } else {
            packet2 = channelRead0Event.getPacket();
            if (packet2 instanceof ExplosionS2CPacket) {
               if (this.ana((ExplosionS2CPacket)packet2)) {
                  MinecraftClient temp = client;
                  final ExplosionS2CPacket explosionS2CPacket = (ExplosionS2CPacket)packet2;
                  Runnable runnable = new Runnable() {
                     @Override
                     public void run() {
                        Velocity.anu(explosionS2CPacket);
                     }
                  };
                  temp.executeSync(runnable);
                  channelRead0Event.aim();
               }
            } else {
               packet2 = channelRead0Event.getPacket();
               if (packet2 instanceof EntityStatusS2CPacket && ((EntityStatusS2CPacket)packet2).getStatus() == 31 && (Boolean)this.noPushFishhook.atx()) {
                  EntityStatusS2CPacket entityStatusS2CPacket = (EntityStatusS2CPacket)packet2;
                  ClientWorld world2 = client.world;
                  Entity entity2 = entityStatusS2CPacket.getEntity((World)world2);
                  if (entity2 instanceof FishingBobberEntity && ((FishingBobberEntity)entity2).getHookedEntity() == client.player) {
                     channelRead0Event.aim();
                  }
               } else {
                  packet2 = channelRead0Event.getPacket();
                  if (packet2 instanceof BundleS2CPacket) {
                     BundleS2CPacket bundleS2CPacket = (BundleS2CPacket)packet2;
                     ArrayList arrayList = new ArrayList();
                     Iterator iterator2 = bundleS2CPacket.getPackets().iterator();

                     while (iterator2.hasNext()) {
                        Packet packet3 = (Packet)iterator2.next();
                        if (packet3 instanceof EntityVelocityUpdateS2CPacket) {
                           if (this.anb((EntityVelocityUpdateS2CPacket)packet3)) {
                              continue;
                           }
                        } else if (packet3 instanceof ExplosionS2CPacket && this.ana((ExplosionS2CPacket)packet3)) {
                           MinecraftClient client2 = client;
                           final ExplosionS2CPacket explosionS2CPacket2 = (ExplosionS2CPacket)packet3;
                           Runnable runnable2 = new Runnable() {
                              @Override
                              public void run() {
                                 Velocity.ant(explosionS2CPacket2);
                              }
                           };
                           client2.executeSync(runnable2);
                           continue;
                        }

                        arrayList.add(packet3);
                     }

                     ((BundlePacketMixin)bundleS2CPacket).setIterable(arrayList);
                  }
               }
            }
         }
      }
   }

   @EventListener
   public void and(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if ((Boolean)this.onlyPhased.atx() && (Boolean)this.noRotation.atx() && amY()) {
         Managers.movementTracker.Mh(new UnidentifiedHelper21(999, client.player.lastYaw, 89.0F));
      }

      if (this.w) {
         if (this.mode.atx() == VelocityNormalGrimMode.GRIM && (Boolean)this.grimPacket.atx()) {
            ServerManager temp = Managers.serverManager;
            Full full = new Full(
               client.player.getX(), client.player.getY(), client.player.getZ(), client.player.lastYaw, client.player.lastPitch, client.player.isOnGround()
            );
            temp.iO('\uf560', full);
            BlockPos pos = this.ani();
            if (pos != null) {
               PlayerActionC2SPacket playerActionC2SPacket = new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, pos, client.player.getHorizontalFacing().getOpposite());
               client.getNetworkHandler().sendPacket((Packet)playerActionC2SPacket);
            }
         }

         this.w = false;
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.POST) {
         this.x = false;
         this.anp(client.player.getX() - client.player.getWidth() * 0.35, client.player.getZ() + client.player.getWidth() * 0.35);
         this.anp(client.player.getX() - client.player.getWidth() * 0.35, client.player.getZ() - client.player.getWidth() * 0.35);
         this.anp(client.player.getX() + client.player.getWidth() * 0.35, client.player.getZ() - client.player.getWidth() * 0.35);
         this.anp(client.player.getX() + client.player.getWidth() * 0.35, client.player.getZ() + client.player.getWidth() * 0.35);
      }
   }

   @EventListener
   public void onPushAwayFrom(PushAwayFromEvent pushAwayFromEvent) {
      if ((Boolean)this.noPushEntities.atx()) {
         Entity temp = pushAwayFromEvent.getEntity();
         ClientPlayerEntity player2 = client.player;
         if (temp.equals(player2)) {
            pushAwayFromEvent.aim();
         }
      }
   }

   @EventListener
   public void onPushOutOfBlocks(PushOutOfBlocksEvent pushOutOfBlocksEvent) {
      if ((Boolean)this.noPushBlocks.atx()) {
         pushOutOfBlocksEvent.aim();
      }
   }

   @EventListener
   public void onIsPushedByFluids(IsPushedByFluidsEvent isPushedByFluidsEvent) {
      if ((Boolean)this.noPushLiquids.atx()) {
         isPushedByFluidsEvent.aim();
      }
   }

   private BlockPos ani() {
      if (!(Boolean)this.grimPacket.atx()) {
         return null;
      }

      Box box = client.player.getBoundingBox().expand(0.001);
      Mutable mutable = new Mutable();
      double[] ds = b;
      int temp = ds.length;
      int n = 0;
      double d = ds[0];
      double[] ds2 = b;
      temp = ds2.length;
      int n2 = 0;

      while (true) {
         double d2 = ds2[n2];
         Mutable mutable2 = mutable;
         double temp2 = client.player.getX() + d;
         double d3 = client.player.getZ() + d2;
         double d4 = client.player.getY();
         double d5 = temp2;
         mutable2.set(d5, d4, d3);
         byte b2;
         if ((Boolean)this.allowLag.atx()) {
            b2 = -1;
         } else {
            b2 = 0;
         }

         for (int n3 = b2; n3 <= 2; n3++) {
            BlockPos pos = this.anj(mutable, n3, box);
            if (pos != null) {
               return pos;
            }
         }

         n2++;

         while (n2 >= 2) {
            if (++n >= 2) {
               return null;
            }

            d = ds[n];
            ds2 = b;
            temp = ds2.length;
            n2 = 0;
         }
      }
   }

   private BlockPos anj(BlockPos pos, int n, Box box) {
      Mutable mutable = new Mutable();
      int[] ns = a;
      int temp = ns.length;
      int n2 = 0;
      int n3 = ns[0];
      int[] ns2 = a;
      temp = ns2.length;
      int n4 = 0;

      while (true) {
         int n5 = ns2[n4];
         Mutable mutable2 = mutable;
         int temp2 = pos.getX() + n3;
         int temp3 = pos.getY() + n;
         int n6 = pos.getZ() + n5;
         int n7 = temp3;
         int n8 = temp2;
         mutable2.set(n8, n7, n6);
         if (this.ank(mutable, box)) {
            return mutable.toImmutable();
         }

         n4++;

         while (n4 >= 3) {
            if (++n2 >= 3) {
               return null;
            }

            n3 = ns[n2];
            ns2 = a;
            temp = ns2.length;
            n4 = 0;
         }
      }
   }

   private boolean ank(BlockPos pos, Box box) {
      if (!client.world.isAir(pos) && (new Box(pos)).intersects(box)) {
         BlockState temp = client.world.getBlockState(pos);
         ClientWorld world2 = client.world;
         if (temp.isFullCube((BlockView)world2, pos) && !BlockUtils.TP(pos)) {
            return true;
         }
      }

      return false;
   }

   private boolean anl() {
      BlockPos pos = client.player.getBlockPos();
      int n = client.player.isCrawling() ? 1 : 2;
      BlockPos pos2 = pos.up(n);
      if (client.world.getBlockState(pos2).isReplaceable()) {
         return false;
      }

      Predicate predicate = obj -> ans((BlockPos)obj);
      return this.anm(client.player).stream().noneMatch(predicate);
   }

   public List anm(PlayerEntity player2) {
      return this.ann(player2);
   }

   public List ann(PlayerEntity player2) {
      ArrayList arrayList = new ArrayList();
      List list = this.ano(player2);
      Iterator iterator2 = list.iterator();

      while (iterator2.hasNext()) {
         BlockPos pos = (BlockPos)iterator2.next();
         Direction[] directions = Direction.values();
         int n = directions.length;

         for (int n2 = 0; n2 < n; n2++) {
            Direction direction = directions[n2];
            if (direction.getAxis().isHorizontal()) {
               BlockPos pos2 = pos.offset(direction);
               if (!arrayList.contains(pos2) && !list.contains(pos2)) {
                  arrayList.add(pos2);
               }
            }
         }
      }

      return arrayList;
   }

   public List ano(PlayerEntity player2) {
      BlockPos pos = WorldUtils.MX(player2, false);
      return new ArrayList(BoxUtils.jD(player2.getBoundingBox(), pos));
   }

   private void anp(double d, double d2) {
      BlockPos pos = BlockPos.ofFloored(d, client.player.getY(), d2);
      if (this.anq(pos)) {
         double d3 = d - pos.getX();
         double d4 = d2 - pos.getZ();
         Direction[] directions = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH};
         int n = directions.length;

         for (int n2 = 0; n2 < n; n2++) {
            Direction direction = directions[n2];
            double d5 = direction.getAxis().choose(d3, 0.0, d4);
            double d6;
            if (direction.getDirection() == AxisDirection.POSITIVE) {
               d6 = 1.0 - d5;
            } else {
               d6 = d5;
            }

            if (d6 < Double.MAX_VALUE && !this.anq(pos.offset(direction))) {
               label38:
               while (true) {
                  double d7 = d6;
                  Direction direction2 = direction;

                  while (++n2 < n) {
                     direction = directions[n2];
                     d5 = direction.getAxis().choose(d3, 0.0, d4);
                     if (direction.getDirection() == AxisDirection.POSITIVE) {
                        d6 = 1.0 - d5;
                     } else {
                        d6 = d5;
                     }

                     if (d6 < d7 && !this.anq(pos.offset(direction))) {
                        continue label38;
                     }
                  }

                  if (direction2 != null) {
                     this.x = true;
                  }

                  return;
               }
            }
         }
      }
   }

   private boolean anq(BlockPos pos) {
      Box box = client.player.getBoundingBox();
      Box box2 = (new Box(pos.getX(), box.minY, pos.getZ(), pos.getX() + 1.0, box.maxY, pos.getZ() + 1.0)).contract(1.0E-7);
      World temp = client.player.getWorld();
      ClientPlayerEntity player2 = client.player;
      return temp.canCollide((Entity)player2, box2);
   }

   public static boolean anr() {
      boolean flag;
      if (client.player.getVelocity().getX() == 0.0 && client.player.isOnGround() && client.player.getVelocity().getZ() == 0.0) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static boolean ans(BlockPos pos) {
      ClientWorld temp = client.world;
      boolean flag = client.player.isCrawling();
      ClientWorld world2 = temp;
      ClientWorld world3;
      BlockPos pos2;
      if (flag) {
         temp = world2;
         pos2 = pos;
         world3 = temp;
      } else {
         temp = world2;
         pos2 = pos.up();
         world3 = temp;
      }

      temp = world3;
      BlockPos pos3 = pos2;
      return temp.getBlockState(pos3).isReplaceable();
   }

   static void ant(ExplosionS2CPacket explosionS2CPacket) {
      ClientWorldMixin2 temp = (ClientWorldMixin2)client.world;
      double temp2 = explosionS2CPacket.getX();
      double temp3 = explosionS2CPacket.getY();
      double temp4 = explosionS2CPacket.getZ();
      Reference reference = SoundEvents.ENTITY_GENERIC_EXPLODE;
      double d = temp4;
      double d2 = temp3;
      double d3 = temp2;
      temp = temp;
      SoundEvent temp5 = (SoundEvent)reference.value();
      Random random = random2;
      SoundCategory soundCategory = SoundCategory.BLOCKS;
      SoundEvent soundEvent = temp5;
      d = d;
      d2 = d2;
      d3 = d3;
      ClientWorldMixin2 clientWorldMixin2 = temp;
      float f = (1.0F + (random.nextFloat() - random2.nextFloat()) * 0.2F) * 0.7F;
      long l = random2.nextLong();
      float f2 = f;
      d = d;
      d2 = d2;
      d3 = d3;
      clientWorldMixin2.hookPlaySound(d3, d2, d, soundEvent, soundCategory, 4.0F, f2, false, l);
   }

   static void anu(ExplosionS2CPacket explosionS2CPacket) {
      ClientWorldMixin2 temp = (ClientWorldMixin2)client.world;
      double temp2 = explosionS2CPacket.getX();
      double temp3 = explosionS2CPacket.getY();
      double temp4 = explosionS2CPacket.getZ();
      Reference reference = SoundEvents.ENTITY_GENERIC_EXPLODE;
      double d = temp4;
      double d2 = temp3;
      double d3 = temp2;
      temp = temp;
      SoundEvent temp5 = (SoundEvent)reference.value();
      Random random = random2;
      SoundCategory soundCategory = SoundCategory.BLOCKS;
      SoundEvent soundEvent = temp5;
      d = d;
      d2 = d2;
      d3 = d3;
      ClientWorldMixin2 clientWorldMixin2 = temp;
      float f = (1.0F + (random.nextFloat() - random2.nextFloat()) * 0.2F) * 0.7F;
      long l = random2.nextLong();
      float f2 = f;
      d = d;
      d2 = d2;
      d3 = d3;
      clientWorldMixin2.hookPlaySound(d3, d2, d, soundEvent, soundCategory, 4.0F, f2, false, l);
   }

   static VoxelShape anv(Mutable mutable, VoxelShape voxelShape) {
      return voxelShape;
   }

   private Boolean isNoRotationVisible() {
      return (Boolean)this.onlyPhased.atx();
   }

   private Boolean isTrappedVisible() {
      return (Boolean)this.onlyPhased.atx();
   }

   private Boolean isGroundOnlyVisible() {
      return (Boolean)this.onlyPhased.atx();
   }

   private Boolean isPearlPassVisible() {
      boolean flag;
      if (this.mode.atx() == VelocityNormalGrimMode.GRIM) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isLagPauseVisible() {
      boolean flag;
      if (this.mode.atx() == VelocityNormalGrimMode.GRIM) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isAllowLagVisible() {
      boolean flag;
      if (this.mode.atx() == VelocityNormalGrimMode.GRIM && (Boolean)this.grimPacket.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isGrimPacketVisible() {
      boolean flag;
      if (this.mode.atx() == VelocityNormalGrimMode.GRIM) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isStaticSettingVisible() {
      boolean flag;
      if (this.mode.atx() == VelocityNormalGrimMode.GRIM) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isResetVelocityVisible() {
      boolean flag;
      if (((Float)this.horizontal.atx() != 0.0F || (Float)this.vertical.atx() != 0.0F) && this.mode.atx() != VelocityNormalGrimMode.GRIM) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private Boolean isVerticalVisible() {
      boolean flag;
      if (this.mode.atx() == VelocityNormalGrimMode.NORMAL) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isHorizontalVisible() {
      boolean flag;
      if (this.mode.atx() == VelocityNormalGrimMode.NORMAL) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static BlockCollisionSpliterator a(CollisionView collisionView, Entity entity2, Box box, BiFunction biFunction) {
      return new BlockCollisionSpliterator(collisionView, entity2, box, false, biFunction);
   }
}
