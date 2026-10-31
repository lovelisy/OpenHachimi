package pw.hachimi.client.module.player;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.combat.AttackBlockEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.manager.ServerManager;
import pw.hachimi.client.model.player.NukerNormalSilentMode;
import pw.hachimi.client.model.player.NukerSphereSelectMode;
import pw.hachimi.client.model.player.NukerWhitelistBlacklistMode;
import pw.hachimi.client.model.render.DataAnimation;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.setting.BlockListSetting;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.player.EnchantmentUtils;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.world.BlockPosDirectionHelper;
import pw.hachimi.client.utils.world.BlockUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Nuker extends Multitask {
   private final Queue queue = new LinkedList();
   private final Queue queue2 = new ArrayDeque();
   Setting whitelistBlacklistMode = this.registerSetting(new EnumSetting("Selection", "The selection of blocks to use for scaffold", NukerWhitelistBlacklistMode.ALL, NukerWhitelistBlacklistMode.values()));
   Setting blockListSetting1 = this.registerSetting(new BlockListSetting("Whitelist", "Valid block whitelist", Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN));
   Setting blockListSetting2 = this.registerSetting(new BlockListSetting("Blacklist", "Valid block blacklist"));
   Setting sphereSelectMode = this.registerSetting(new EnumSetting("Mode", "The nuker selection mode", NukerSphereSelectMode.SPHERE, NukerSphereSelectMode.values()));
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Flatten", "Only clears above the player y-level", false, this::isBooleanSetting1Visible));
   Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("StrictDirection", "Only mines on visible faces", false));
   Setting numberSetting1 = this.registerSetting(new NumberSetting("Range", "The range to mine blocks", 0.0F, 4.0F, 6.0F));
   Setting booleanSetting3 = this.registerSetting(new BooleanSetting("DoubleBreak", "Allows you to mine two blocks at once", false));
   Setting numberSetting2 = this.registerSetting(new NumberSetting("MiningTicks", "The max number of ticks to hold a pickaxe for the packet mine", 5, 20, 60, this::isNumberSetting2Visible));
   Setting numberSetting3 = this.registerSetting(new NumberSetting("Speed", "The speed to mine blocks", 0.1F, 1.0F, 1.0F));
   Setting normalSilentMode = this.registerSetting(new EnumSetting("AutoSwap", "Swaps to the best tool once the mining is complete", NukerNormalSilentMode.SILENT, NukerNormalSilentMode.values()));
   Setting booleanSetting4 = this.registerSetting(new BooleanSetting("SwapBefore", "Swaps before mining", false, this::isBooleanSetting4Visible));
   Setting booleanSetting5 = this.registerSetting(new BooleanSetting("Rotate", "Rotates when mining the block", true));
   Setting booleanSetting6 = this.registerSetting(new BooleanSetting("SwitchReset", "Resets mining after switching items", false));
   Setting booleanSetting7 = this.registerSetting(new BooleanSetting("Grim", "Uses grim block breaking speeds", false));
   Setting booleanSetting8 = this.registerSetting(new BooleanSetting("GrimV3", "Allows mining on new grim servers", false, this::isBooleanSetting8Visible));
   Setting colorSetting1 = this.registerSetting(createColorSetting("MineColor", "The mine render color", Color.RED));
   Setting colorSetting2 = this.registerSetting(createColorSetting("DoneColor", "The done render color", Color.GREEN));
   Setting booleanSetting9 = this.registerSetting(new BooleanSetting("Debug-Ticks", "Shows the mining ticks", false));
   Setting numberSetting4 = this.registerSetting(new NumberSetting("Fade-Time", "Time to fade", 0, 250, 1000));
   Setting booleanSetting10 = this.registerSetting(new BooleanSetting("SmoothColor", "Interpolates from start to done color", false));
   private BlockPosDirectionHelper blockPosDirectionHelper;
   private BlockPosDirectionHelper blockPosDirectionHelper2;
   private boolean z;
   private boolean A;
   private boolean B;
   private int C;
   private DataAnimation dataAnimation = new DataAnimation(BlockPosDirectionHelper.cz(), d(200.0F));
   private DataAnimation dataAnimation2 = new DataAnimation(BlockPosDirectionHelper.cz(), d(200.0F));
   public static boolean fb;

   public Nuker() {
      super("Nuker", "Clears nearby blocks", ModuleCategory.PLAYER);
   }

   @Override
   public void awe() {
      this.queue2.clear();
      this.blockPosDirectionHelper = null;
      if (this.blockPosDirectionHelper2 != null) {
         this.Bi(this.blockPosDirectionHelper2);
         this.blockPosDirectionHelper2 = null;
      }

      this.dataAnimation = new DataAnimation(BlockPosDirectionHelper.cz(), d(200.0F));
      this.dataAnimation2 = new DataAnimation(BlockPosDirectionHelper.cz(), d(200.0F));
      this.C = 0;
      this.B = false;
      if (this.z) {
         Managers.inventoryTracker.JI();
         this.z = false;
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (!client.player.isCreative() && !client.player.isSpectator() && tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if (this.Bl()) {
            if (this.A) {
               this.A = false;
            }

            if (this.B) {
               this.B = false;
            }
         }

         this.C--;
         if (this.blockPosDirectionHelper != null && this.blockPosDirectionHelper.cH() > (Integer)this.numberSetting2.atx()) {
            this.dataAnimation.animation().qV(false);
            if (this.z) {
               Managers.inventoryTracker.JI();
               this.z = false;
            }

            Queue temp = this.queue;
            BlockPos pos = this.blockPosDirectionHelper.getPos();
            temp.remove(pos);
            this.blockPosDirectionHelper = null;
            if (!this.Bl()) {
               this.B = true;
            }
         }

         if (this.blockPosDirectionHelper != null) {
            BlockState state = this.blockPosDirectionHelper.cK();
            ClientWorld temp2 = client.world;
            BlockPos pos2 = this.blockPosDirectionHelper.getPos();
            ClientWorld world2 = temp2;
            BlockState state2 = state;
            float f2 = this.AW(state2, world2, pos2);
            this.blockPosDirectionHelper.cC(f2);
            int n = this.blockPosDirectionHelper.cL();
            float f3 = this.blockPosDirectionHelper.cI();
            boolean flag = (Boolean)this.booleanSetting4.atx();
            float f4 = f3;
            float f5;
            if (flag) {
               f4 = f2;
               f5 = f4;
            } else {
               f4 = 0.0F;
               f5 = f4;
            }

            if (f5 + f4 >= 1.0F && this.Bq(this.blockPosDirectionHelper.cK()) && n != -1 && !this.RG()) {
               this.blockPosDirectionHelper.cB();
               Managers.inventoryTracker.JF(n);
               this.z = true;
            }
         }

         if (this.z && (this.blockPosDirectionHelper == null || !this.Bq(this.blockPosDirectionHelper.cK()))) {
            Managers.inventoryTracker.JI();
            this.z = false;
            this.dataAnimation.animation().qV(false);
            if (this.blockPosDirectionHelper != null) {
               Queue queue3 = this.queue;
               BlockPos pos3 = this.blockPosDirectionHelper.getPos();
               queue3.remove(pos3);
            }

            this.blockPosDirectionHelper = null;
            if (!this.Bl()) {
               this.B = true;
            }
         }

         if (this.blockPosDirectionHelper2 != null) {
            Vec3d vec = client.player.getEyePos();
            Vec3d vec2 = this.blockPosDirectionHelper2.getPos().toCenterPos();
            if (vec.squaredDistanceTo(vec2) > ((NumberSetting)this.numberSetting1).abq()
               || this.blockPosDirectionHelper2.cH() > (Integer)this.numberSetting2.atx()) {
               this.Bi(this.blockPosDirectionHelper2);
               this.dataAnimation2.animation().qV(false);
               Queue queue4 = this.queue;
               BlockPos pos4 = this.blockPosDirectionHelper2.getPos();
               queue4.remove(pos4);
               this.blockPosDirectionHelper2 = null;
            }
         }

         if (this.blockPosDirectionHelper2 != null) {
            BlockState state3 = this.blockPosDirectionHelper2.cK();
            ClientWorld world3 = client.world;
            BlockPos pos5 = this.blockPosDirectionHelper2.getPos();
            ClientWorld world4 = world3;
            BlockState state4 = state3;
            float f6 = this.AW(state4, world4, pos5);
            this.blockPosDirectionHelper2.cC(f6);
            if (this.blockPosDirectionHelper2.cI() >= (Float)this.numberSetting3.atx()) {
               if (this.Bq(this.blockPosDirectionHelper2.cK())) {
                  if (!this.RG() || (Boolean)this.breakSetting.atx() || this.normalSilentMode.atx() == NukerNormalSilentMode.OFF) {
                     this.Bj(this.blockPosDirectionHelper2);
                     this.blockPosDirectionHelper2.cB();
                  }
               } else {
                  this.Bi(this.blockPosDirectionHelper2);
                  this.dataAnimation2.animation().qV(false);
                  Queue queue5 = this.queue;
                  BlockPos pos6 = this.blockPosDirectionHelper2.getPos();
                  queue5.remove(pos6);
                  this.blockPosDirectionHelper2 = null;
               }
            }
         }

         if (!this.queue2.isEmpty() && this.C <= 0) {
            BlockPosDirectionHelper blockPosDirectionHelper3 = (BlockPosDirectionHelper)this.queue2.poll();
            if (blockPosDirectionHelper3 != null) {
               this.Bh(blockPosDirectionHelper3);
               this.C = 5;
            }
         }

         if (this.queue2.isEmpty()) {
            BlockPosDirectionHelper blockPosDirectionHelper4 = this.Bg();
            if (blockPosDirectionHelper4 != null && (this.blockPosDirectionHelper == null && (Boolean)this.booleanSetting3.atx() || this.Bl())) {
               this.Bd(blockPosDirectionHelper4);
            }
         }
      }
   }

   public float AW(BlockState state, BlockView blockView, BlockPos pos) {
      if (this.normalSilentMode.atx() == NukerNormalSilentMode.OFF) {
         ClientWorld world2 = client.world;
         ClientPlayerEntity player2 = client.player;
         return state.calcBlockBreakingDelta((PlayerEntity)player2, (BlockView)world2, pos);
      }

      float f2 = state.getHardness(blockView, pos);
      if (f2 == -1.0F) {
         return 0.0F;
      }

      byte b;
      if (this.AY(state)) {
         b = 30;
      } else {
         b = 100;
      }

      byte b2 = b;
      return this.AX(state) / f2 / b2;
   }

   private float AX(BlockState state) {
      int n = AutoTool.UZ(state);
      float f2 = client.player.getInventory().getStack(n).getMiningSpeedMultiplier(state);
      if (f2 > 1.0F) {
         ItemStack stack = client.player.getInventory().getStack(n);
         int n2 = EnchantmentUtils.VN(stack, Enchantments.EFFICIENCY);
         if (n2 > 0 && !stack.isEmpty()) {
            f2 += n2 * n2 + 1;
         }
      }

      if (StatusEffectUtil.hasHaste((LivingEntity)client.player)) {
         f2 *= 1.0F + (StatusEffectUtil.getHasteAmplifier((LivingEntity)client.player) + 1) * 0.2F;
      }

      RegistryEntry registryEntry = StatusEffects.MINING_FATIGUE;
      if (client.player.hasStatusEffect(registryEntry)) {
         RegistryEntry registryEntry2 = StatusEffects.MINING_FATIGUE;

         f2 *= switch (client.player.getStatusEffect(registryEntry2).getAmplifier()) {
            case 0 -> 0.3F;
            case 1 -> 0.09F;
            case 2 -> 0.0027F;
            default -> 8.1E-4F;
         };
      }

      if (!client.player.isOnGround()) {
         f2 /= 5.0F;
      }

      return f2;
   }

   private boolean AY(BlockState state) {
      if (state.isToolRequired()) {
         int n = AutoTool.UZ(state);
         return client.player.getInventory().getStack(n).isSuitableFor(state);
      } else {
         return true;
      }
   }

   @EventListener
   public void onAttackBlock(AttackBlockEvent attackBlockEvent) {
      if (!client.player.isCreative() && !client.player.isSpectator()) {
         attackBlockEvent.aim();
         if (attackBlockEvent.getState().getBlock().getHardness() != -1.0F && this.Bq(attackBlockEvent.getState())) {
            Queue temp = this.queue;
            BlockPos pos = attackBlockEvent.getPos();
            if (!temp.contains(pos)) {
               Vec3d vec = client.player.getEyePos();
               Vec3d vec2 = attackBlockEvent.getPos().toCenterPos();
               if (vec.squaredDistanceTo(vec2) > ((NumberSetting)this.numberSetting1).abq()) {
                  return;
               }

               temp = this.queue;
               BlockPos pos2 = attackBlockEvent.getPos();
               temp.add(pos2);
               return;
            }
         }
      }
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (sendImmediatelyEvent.getPacket() instanceof UpdateSelectedSlotC2SPacket && (Boolean)this.booleanSetting6.atx()) {
         this.blockPosDirectionHelper2.cD(0.0F, 0.0F);
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      Packet packet2 = channelRead0Event.getPacket();
      if (packet2 instanceof BlockUpdateS2CPacket && !this.Bq(((BlockUpdateS2CPacket)packet2).getState()) && this.blockPosDirectionHelper != null) {
         BlockPos temp = this.blockPosDirectionHelper.getPos();
         BlockPos pos = ((BlockUpdateS2CPacket)packet2).getPos();
         if (temp.equals(pos)) {
            this.dataAnimation.animation().qV(false);
            if (this.z) {
               Managers.inventoryTracker.JI();
               this.z = false;
            }

            this.blockPosDirectionHelper = null;
            boolean flag;
            if (!this.Bl()) {
               flag = true;
            } else {
               flag = false;
            }

            this.B = flag;
         }
      }
   }

   private List Bc(Vec3d vec) {
      ArrayList arrayList = new ArrayList();
      double d2 = Math.ceil(((Float)this.numberSetting1.atx()).floatValue());

      for (double d3 = -d2; d3 <= d2; d3++) {
         double d4;
         if ((Boolean)this.booleanSetting1_1.atx()) {
            d4 = 0.0;
         } else {
            d4 = -d2;
         }

         for (double d5 = d4; d5 <= d2; d5++) {
            for (double d6 = -d2; d6 <= d2; d6++) {
               BlockPos pos = new BlockPos(new Vec3i((int)(vec.getX() + d3), (int)(vec.getY() + d5), (int)(vec.getZ() + d6)));
               arrayList.add(pos);
            }
         }
      }

      return arrayList;
   }

   public void Bd(BlockPosDirectionHelper blockPosDirectionHelper3) {
      if (this.Bq(blockPosDirectionHelper3.cK()) && !this.Bo(blockPosDirectionHelper3.getPos())) {
         if (!(Boolean)this.booleanSetting3.atx()) {
            this.blockPosDirectionHelper2 = blockPosDirectionHelper3;
            this.queue2.offer(blockPosDirectionHelper3);
         } else if ((!this.A || this.Bl()) && !this.B) {
            if (!this.Bl() && !this.A) {
               if (this.blockPosDirectionHelper != null) {
                  this.blockPosDirectionHelper2 = blockPosDirectionHelper3;
                  this.queue2.offer(blockPosDirectionHelper3);
                  this.A = true;
                  return;
               }

               this.blockPosDirectionHelper = this.blockPosDirectionHelper2.cE();
               Setting setting2 = this.numberSetting4;
               BlockPosDirectionHelper blockPosDirectionHelper4 = this.blockPosDirectionHelper;
               this.dataAnimation = new DataAnimation(blockPosDirectionHelper4, d(((Integer)setting2.atx()).intValue()));
            }

            this.blockPosDirectionHelper2 = blockPosDirectionHelper3;
            this.queue2.offer(blockPosDirectionHelper3);
         }
      }
   }

   @EventListener
   public void Be(Render3DContext render3DContext) {
      if (!client.player.isCreative() && !client.player.isSpectator()) {
         Iterator iterator2 = this.queue.iterator();

         while (iterator2.hasNext()) {
            BlockPos pos = (BlockPos)iterator2.next();
            render3DContext.boxRenderer.Zc(pos, Color.white, ColorHelper3.auO().auW(160), ShapeMode.Lines);
         }

         if (this.dataAnimation2 != null && this.dataAnimation2.animation().qW() > 0.01F) {
            this.Bf(render3DContext, this.dataAnimation2, true);
         }

         if (this.dataAnimation != null && this.dataAnimation.animation().qW() > 0.01F) {
            this.Bf(render3DContext, this.dataAnimation, false);
         }
      }
   }

   public void Bf(Render3DContext render3DContext, DataAnimation dataAnimation3, boolean flag) {
      BlockPosDirectionHelper blockPosDirectionHelper3 = dataAnimation3.getData();
      UnidentifiedHelper7 unidentifiedHelper7 = dataAnimation3.animation();
      int n = (int)(40.0 * unidentifiedHelper7.qW());
      int n2 = (int)(100.0 * unidentifiedHelper7.qW());
      Color color;
      Color color2;
      if ((Boolean)this.booleanSetting10.atx()) {
         Color color3;
         if (!this.Bq(blockPosDirectionHelper3.cK())) {
            color3 = ((ColorSetting)this.colorSetting2).Tz(n);
         } else {
            color3 = ColorUtils.iA(Math.min(blockPosDirectionHelper3.cI(), 1.0F), ((ColorSetting)this.colorSetting2).Tz(n), ((ColorSetting)this.colorSetting1).Tz(n));
         }

         color = color3;
         if (!this.Bq(blockPosDirectionHelper3.cK())) {
            color3 = ((ColorSetting)this.colorSetting2).Tz(n2);
         } else {
            color3 = ColorUtils.iA(Math.min(blockPosDirectionHelper3.cI(), 1.0F), ((ColorSetting)this.colorSetting2).Tz(n2), ((ColorSetting)this.colorSetting1).Tz(n2));
         }

         color2 = color3;
      } else {
         Color color4;
         if (!(blockPosDirectionHelper3.cI() >= 0.95F) && this.Bq(blockPosDirectionHelper3.cK())) {
            color4 = ((ColorSetting)this.colorSetting1).Tz(n);
         } else {
            color4 = ((ColorSetting)this.colorSetting2).Tz(n);
         }

         color = color4;
         if (!(blockPosDirectionHelper3.cI() >= 0.95F) && this.Bq(blockPosDirectionHelper3.cK())) {
            color4 = ((ColorSetting)this.colorSetting1).Tz(n2);
         } else {
            color4 = ((ColorSetting)this.colorSetting2).Tz(n2);
         }

         color2 = color4;
      }

      BlockPos pos = blockPosDirectionHelper3.getPos();
      VoxelShape voxelShape = VoxelShapes.fullCube();
      if (!flag || blockPosDirectionHelper3.cI() < (Float)this.numberSetting3.atx()) {
         BlockState temp = blockPosDirectionHelper3.cK();
         ClientWorld world2 = client.world;
         voxelShape = temp.getOutlineShape((BlockView)world2, pos);
         VoxelShape voxelShape2;
         if (voxelShape.isEmpty()) {
            voxelShape2 = VoxelShapes.fullCube();
         } else {
            voxelShape2 = voxelShape;
         }

         voxelShape = voxelShape2;
      }

      Box box = voxelShape.getBoundingBox();
      Vec3d vec = box.offset(pos).getCenter();
      float f2;
      if (flag) {
         f2 = (Float)this.numberSetting3.atx();
      } else {
         f2 = 1.0F;
      }

      float f3 = f2;
      if ((!flag || !(blockPosDirectionHelper3.cI() >= (Float)this.numberSetting3.atx())) && this.Bq(blockPosDirectionHelper3.cK())) {
         f2 = MathHelper.clamp((blockPosDirectionHelper3.cI() + (blockPosDirectionHelper3.cI() - blockPosDirectionHelper3.cJ()) * render3DContext.f) / f3, 0.0F, 1.0F);
      } else {
         f2 = 1.0F;
      }

      float f4 = f2;
      double d2 = (box.maxX - box.minX) / 2.0;
      double d3 = (box.maxY - box.minY) / 2.0;
      double d4 = (box.maxZ - box.minZ) / 2.0;
      Box box2 = new Box(vec, vec);
      double temp2 = d2 * f4;
      double temp3 = d3 * f4;
      double d5 = d4 * f4;
      double d6 = temp3;
      double d7 = temp2;
      Box box3 = box2.expand(d7, d6, d5);
      render3DContext.boxRenderer.Zd(box3, color, color2, ShapeMode.Both);
      if ((Boolean)this.booleanSetting9.atx()) {
         RenderUtils.acv(render3DContext.matrices, String.valueOf(blockPosDirectionHelper3.cH()), vec, -1);
      }
   }

   public BlockPosDirectionHelper Bg() {
      if (this.sphereSelectMode.atx() == NukerSphereSelectMode.SPHERE) {
         Iterator iterator2 = this.Bc(client.player.getPos()).iterator();

         while (iterator2.hasNext()) {
            BlockPos pos = (BlockPos)iterator2.next();
            BlockState state = client.world.getBlockState(pos);
            if (this.Bq(state) && !this.Bo(pos)) {
               Vec3d temp = client.player.getEyePos();
               Vec3d vec = pos.toCenterPos();
               if (!(temp.squaredDistanceTo(vec) > ((NumberSetting)this.numberSetting1).abq())) {
                  int n = pos.getY();
                  temp = client.player.getEyePos();
                  Vec3d vec2 = pos.toCenterPos();
                  double d2 = temp.squaredDistanceTo(vec2);
                  if (n > -128 || n == -128 && d2 < Double.MAX_VALUE) {
                     label74:
                     while (true) {
                        BlockPos pos2 = pos;
                        int n2 = n;
                        double d3 = d2;

                        while (iterator2.hasNext()) {
                           pos = (BlockPos)iterator2.next();
                           state = client.world.getBlockState(pos);
                           if (this.Bq(state) && !this.Bo(pos)) {
                              temp = client.player.getEyePos();
                              Vec3d vec3 = pos.toCenterPos();
                              if (!(temp.squaredDistanceTo(vec3) > ((NumberSetting)this.numberSetting1).abq())) {
                                 n = pos.getY();
                                 temp = client.player.getEyePos();
                                 Vec3d vec4 = pos.toCenterPos();
                                 d2 = temp.squaredDistanceTo(vec4);
                                 if (n > n2 || n == n2 && d2 < d3) {
                                    continue label74;
                                 }
                              }
                           }
                        }

                        if (pos2 != null) {
                           BlockPos pos3 = pos2;
                           boolean flag = (Boolean)this.booleanSetting2_1.atx();
                           BlockPos pos4 = pos3;
                           BlockPos pos5;
                           Direction direction;
                           if (flag) {
                              BlockPos pos6 = pos4;
                              direction = PlacementUtils.Fh(pos2);
                              pos5 = pos6;
                           } else {
                              BlockPos pos7 = pos4;
                              direction = Direction.UP;
                              pos5 = pos7;
                           }

                           return new BlockPosDirectionHelper(pos5, direction);
                        }

                        return null;
                     }
                  }
               }
            }
         }
      } else {
         if (this.queue.isEmpty()) {
            return null;
         }

         if (this.blockPosDirectionHelper != null && this.blockPosDirectionHelper2 != null) {
            return null;
         }

         BlockPos pos8 = (BlockPos)(new LinkedList<>(this.queue)).poll();
         if (this.blockPosDirectionHelper2 != null && this.blockPosDirectionHelper2.getPos().equals(pos8)) {
            pos8 = (BlockPos)this.queue.poll();
         }

         if (pos8 != null) {
            Direction direction2;
            if ((Boolean)this.booleanSetting2_1.atx()) {
               direction2 = PlacementUtils.Fh(pos8);
            } else {
               direction2 = Direction.UP;
            }

            return new BlockPosDirectionHelper(pos8, direction2);
         }
      }

      return null;
   }

   public void Bh(BlockPosDirectionHelper blockPosDirectionHelper3) {
      if ((Boolean)this.booleanSetting3.atx()) {
         if ((Boolean)this.booleanSetting8.atx()) {
            Action action = Action.STOP_DESTROY_BLOCK;
            ServerManager serverManager2 = Managers.serverManager;
            PlayerActionC2SPacket playerActionC2SPacket = new PlayerActionC2SPacket(action, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
            serverManager2.iO('\uf560', playerActionC2SPacket);
            action = Action.START_DESTROY_BLOCK;
            serverManager2 = Managers.serverManager;
            playerActionC2SPacket = new PlayerActionC2SPacket(action, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
            serverManager2.iO('\uf560', playerActionC2SPacket);
            action = Action.ABORT_DESTROY_BLOCK;
            serverManager2 = Managers.serverManager;
            playerActionC2SPacket = new PlayerActionC2SPacket(action, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
            serverManager2.iO('\uf560', playerActionC2SPacket);
            action = Action.STOP_DESTROY_BLOCK;
            serverManager2 = Managers.serverManager;
            playerActionC2SPacket = new PlayerActionC2SPacket(action, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
            serverManager2.iO('\uf560', playerActionC2SPacket);
            Managers.serverManager.iO('\uf560', new HandSwingC2SPacket(Hand.MAIN_HAND));
            Managers.serverManager.iO('\uf560', new HandSwingC2SPacket(Hand.MAIN_HAND));
            Managers.serverManager.iO('\uf560', new HandSwingC2SPacket(Hand.MAIN_HAND));
         } else {
            Action action2 = Action.STOP_DESTROY_BLOCK;
            ServerManager serverManager3 = Managers.serverManager;
            PlayerActionC2SPacket playerActionC2SPacket2 = new PlayerActionC2SPacket(action2, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
            serverManager3.iO('\uf560', playerActionC2SPacket2);
            action2 = Action.START_DESTROY_BLOCK;
            serverManager3 = Managers.serverManager;
            playerActionC2SPacket2 = new PlayerActionC2SPacket(action2, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
            serverManager3.iO('\uf560', playerActionC2SPacket2);
            action2 = Action.STOP_DESTROY_BLOCK;
            serverManager3 = Managers.serverManager;
            playerActionC2SPacket2 = new PlayerActionC2SPacket(action2, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
            serverManager3.iO('\uf560', playerActionC2SPacket2);
            action2 = Action.STOP_DESTROY_BLOCK;
            serverManager3 = Managers.serverManager;
            playerActionC2SPacket2 = new PlayerActionC2SPacket(action2, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
            serverManager3.iO('\uf560', playerActionC2SPacket2);
            action2 = Action.START_DESTROY_BLOCK;
            serverManager3 = Managers.serverManager;
            playerActionC2SPacket2 = new PlayerActionC2SPacket(action2, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
            serverManager3.iO('\uf560', playerActionC2SPacket2);
            action2 = Action.STOP_DESTROY_BLOCK;
            serverManager3 = Managers.serverManager;
            playerActionC2SPacket2 = new PlayerActionC2SPacket(action2, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
            serverManager3.iO('\uf560', playerActionC2SPacket2);
            Managers.serverManager.iO('\uf560', new HandSwingC2SPacket(Hand.MAIN_HAND));
         }
      } else {
         Action action3 = Action.START_DESTROY_BLOCK;
         ServerManager serverManager4 = Managers.serverManager;
         PlayerActionC2SPacket playerActionC2SPacket3 = new PlayerActionC2SPacket(action3, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
         serverManager4.iO('\uf560', playerActionC2SPacket3);
         if (!(Boolean)this.booleanSetting7.atx()) {
            final BlockPosDirectionHelper blockPosDirectionHelper4 = blockPosDirectionHelper3;
            Managers.serverManager.iQ(new SequencedPacketCreator() {
               public Packet predict(int blockPosDirectionHelper3) {
                  return Nuker.Br(blockPosDirectionHelper4, blockPosDirectionHelper3);
               }
            });
            action3 = Action.STOP_DESTROY_BLOCK;
            serverManager4 = Managers.serverManager;
            playerActionC2SPacket3 = new PlayerActionC2SPacket(action3, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
            serverManager4.iO('\uf560', playerActionC2SPacket3);
         }
      }

      this.dataAnimation2 = new DataAnimation(blockPosDirectionHelper3, d(((Integer)this.numberSetting4.atx()).intValue()));
   }

   public void Bi(BlockPosDirectionHelper blockPosDirectionHelper3) {
      Action action = Action.ABORT_DESTROY_BLOCK;
      ServerManager serverManager2 = Managers.serverManager;
      PlayerActionC2SPacket playerActionC2SPacket = new PlayerActionC2SPacket(action, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
      serverManager2.iO('\uf560', playerActionC2SPacket);
   }

   public void Bj(BlockPosDirectionHelper blockPosDirectionHelper3) {
      if ((Boolean)this.booleanSetting5.atx()) {
         float[] fs = RotationUtils.nM(blockPosDirectionHelper3.getPos().toCenterPos());
         if ((Boolean)this.booleanSetting7.atx()) {
            RotationUtils.nH(fs[0], fs[1]);
         } else {
            this.ajM(fs[0], fs[1]);
         }
      }

      int n = blockPosDirectionHelper3.cL();
      if (n != -1 && n != Managers.inventoryTracker.JS()) {
         this.Bm(n);
         this.Bk(blockPosDirectionHelper3);
         this.Bn(n);
      } else {
         this.Bk(blockPosDirectionHelper3);
      }

      if ((Boolean)this.booleanSetting5.atx()) {
         RotationUtils.nK();
      }
   }

   private void Bk(BlockPosDirectionHelper blockPosDirectionHelper3) {
      Action action = Action.STOP_DESTROY_BLOCK;
      ServerManager serverManager2 = Managers.serverManager;
      PlayerActionC2SPacket playerActionC2SPacket = new PlayerActionC2SPacket(action, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
      serverManager2.iO('\uf560', playerActionC2SPacket);
      action = Action.ABORT_DESTROY_BLOCK;
      serverManager2 = Managers.serverManager;
      playerActionC2SPacket = new PlayerActionC2SPacket(action, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection());
      serverManager2.iO('\uf560', playerActionC2SPacket);
   }

   public boolean Bl() {
      boolean flag;
      if (this.blockPosDirectionHelper2 != null
         && (!(this.blockPosDirectionHelper2.cI() >= (Float)this.numberSetting3.atx()) || this.Bq(this.blockPosDirectionHelper2.cK()))) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private void Bm(int n) {
      switch ((NukerNormalSilentMode)this.normalSilentMode.atx()) {
         case NORMAL:
            Managers.inventoryTracker.JU(n);
            break;
         case SILENT:
            Managers.inventoryTracker.JF(n);
            break;
         case SILENT_ALT:
            Managers.inventoryTracker.JG(n);
      }
   }

   private void Bn(int n) {
      switch ((NukerNormalSilentMode)this.normalSilentMode.atx()) {
         case SILENT:
            Managers.inventoryTracker.JI();
            break;
         case SILENT_ALT:
            Managers.inventoryTracker.JG(n);
      }
   }

   private boolean Bo(BlockPos pos) {
      boolean flag;
      if ((this.blockPosDirectionHelper2 == null || !this.blockPosDirectionHelper2.getPos().equals(pos))
         && (this.blockPosDirectionHelper == null || !this.blockPosDirectionHelper.getPos().equals(pos))) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private boolean Bp(Block block2) {
      if (BlockUtils.TO(block2)) {
         return false;
      }

      return switch ((NukerWhitelistBlacklistMode)this.whitelistBlacklistMode.atx()) {
         case WHITELIST -> ((BlockListSetting)this.blockListSetting1).afW(block2);
         case BLACKLIST -> {
            if (!((BlockListSetting)this.blockListSetting2).afW(block2)) {
               yield true;
            } else {
               yield false;
            }
         }
         case ALL -> true;
      };
   }

   public boolean Bq(BlockState state) {
      boolean flag;
      if (!state.isAir() && state.getFluidState().isEmpty()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static Packet Br(BlockPosDirectionHelper blockPosDirectionHelper3, int n) {
      return new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, blockPosDirectionHelper3.getPos(), blockPosDirectionHelper3.getDirection(), n);
   }

   private Boolean isBooleanSetting8Visible() {
      return (Boolean)this.booleanSetting7.atx();
   }

   private Boolean isBooleanSetting4Visible() {
      boolean flag;
      if (this.normalSilentMode.atx() != NukerNormalSilentMode.OFF) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isNumberSetting2Visible() {
      return (Boolean)this.booleanSetting3.atx();
   }

   private Boolean isBooleanSetting1Visible() {
      boolean flag;
      if (this.sphereSelectMode.atx() == NukerSphereSelectMode.SPHERE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static MatchException m() {
      return new MatchException(null, null);
   }

   private static UnidentifiedHelper7 d(float f2) {
      return new UnidentifiedHelper7(true, f2);
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color) {
      return new ColorSetting(str, str2, color, false, false);
   }
}
