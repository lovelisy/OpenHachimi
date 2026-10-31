package pw.hachimi.client.module.player;

import java.awt.Color;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.combat.AttackBlockEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.event.player.StackEvent;
import pw.hachimi.client.event.render.GetCollisionShapeEvent;
import pw.hachimi.client.event.world.CancelBlockBreakingEvent;
import pw.hachimi.client.event.world.MiningCooldownFixEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.mixin.ClientPlayerInteractionManagerMixin2;
import pw.hachimi.client.model.NoneNormalMode;
import pw.hachimi.client.model.combat.DamagePacketMode;
import pw.hachimi.client.model.network.DisablerNewDisablerMode;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.player.SwingMode;
import pw.hachimi.client.model.player.WhitelistBlacklistMode;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.setting.BlockListSetting;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.KeybindSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.UnidentifiedHelper17;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;
import pw.hachimi.client.utils.world.BlockPosPlayerHelper;
import pw.hachimi.client.utils.world.BlockUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.eventbus.annotation.EventListener;
import java.util.ArrayList;

public class BlockPosBoxMultitask extends Multitask {
   public static StopwatchImpl stopwatchImpl = new StopwatchImpl();
   public static List list = new ArrayList();
   private static BlockPosBoxMultitask INSTANCE;
   public ItemStack stack;
   public boolean d = false;
   public boolean e = false;
   public Runnable runnable;
   public BlockPosPlayerHelper blockPosPlayerHelper;
   public BlockPosPlayerHelper blockPosPlayerHelper2;
   private long i;
   public long j;
   public boolean k;
   Setting damagePacketMode = this.registerSetting(new EnumSetting("Mode", "Mode", DamagePacketMode.Packet, DamagePacketMode.values()));
   Setting numberSetting1 = this.registerSetting(new NumberSetting("Speed", "Multiplier for mining progress required", 0.0F, 0.99F, 2.0F));
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("NoDelay", "Allow you to mine without delay", false, this::isBooleanSetting1Visible));
   Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("NoReset", "Switch items during mining will not reset the progress", false, this::isBooleanSetting2Visible));
   Setting booleanSetting3 = this.registerSetting(new BooleanSetting("NoAbort", "Never cancel mining", false, this::isBooleanSetting3Visible));
   public Setting numberSetting2 = this.registerSetting(new NumberSetting("Range", "Interact reach", 0.0, 5.0, 6.0, this::isNumberSetting2Visible));
   public Setting swingMode = this.registerSetting(new EnumSetting("Swing", "Swing side for interact block", SwingMode.Normal, SwingMode.values(), this::isSwingModeVisible));
   Setting numberSetting3 = this.registerSetting(new NumberSetting("Priority", "Priority of your mining", 0, 8, 10, this::isNumberSetting3Visible));
   Setting whitelistBlacklistMode = this.registerSetting(
      new EnumSetting("Selection", "How to use the blocks setting", WhitelistBlacklistMode.All, WhitelistBlacklistMode.values(), this::isWhitelistBlacklistModeVisible)
   );
   Setting blockListSetting1 = this.registerSetting(new BlockListSetting("Whitelist", "Valid block whitelist", this::isBlockListSetting1Visible, Blocks.OBSIDIAN, Blocks.ENDER_CHEST));
   Setting blockListSetting2 = this.registerSetting(new BlockListSetting("Blacklist", "Valid block blacklist", this::isBlockListSetting2Visible, Blocks.FIRE));
   public Setting swapMode = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values(), this::isSwapModeVisible));
   public Setting booleanSetting4 = this.registerSetting(new BooleanSetting("Hold", "Applicable to 1.21+ server", false, this::isBooleanSetting4Visible));
   public Setting booleanSetting5 = this.registerSetting(new BooleanSetting("Manual", "Manual switch tool", false, this::isBooleanSetting5Visible));
   public Setting numberSetting4 = this.registerSetting(new NumberSetting("HoldTicks", "The max number of ticks to hold a pickaxe for the packet mine", 0, 2, 60, this::isNumberSetting4Visible));
   public Setting booleanSetting6 = this.registerSetting(new BooleanSetting("InstantMine", "Vanilla exploit allows you to rebreak broken block", false, this::isBooleanSetting6Visible));
   public Setting numberSetting5 = this.registerSetting(new NumberSetting("RetryTicks", "The max number of ticks to hold a pickaxe for the instant mine", 0, 10, 60, this::isNumberSetting5Visible));
   public Setting booleanSetting7 = this.registerSetting(new BooleanSetting("DoubleBreak", "Vanilla exploit allows you to have two mining progressions", true, this::isBooleanSetting7Visible));
   public Setting numberSetting6 = this.registerSetting(new NumberSetting("MiningTicks", "The max number of ticks to hold a pickaxe for the packet mine", 0, 10, 60, this::isNumberSetting6Visible));
   public Setting booleanSetting8 = this.registerSetting(new BooleanSetting("AutoSwitch", "Allow extra mine auto switch", true, this::isBooleanSetting8Visible));
   public Setting booleanSetting9 = this.registerSetting(new BooleanSetting("StartRotate", "Automatically faces towards the block when start mining", true, this::isBooleanSetting9Visible));
   public Setting booleanSetting10 = this.registerSetting(new BooleanSetting("EndRotate", "Automatically faces towards the block when end mining", false, this::isBooleanSetting10Visible));
   public Setting booleanSetting11 = this.registerSetting(new BooleanSetting("MineAir", "Allow mine air", true, this::isBooleanSetting11Visible));
   public Setting booleanSetting12 = this.registerSetting(new BooleanSetting("CheckGround", "Does mining calculation detect on ground", false, this::isBooleanSetting12Visible));
   public Setting booleanSetting13 = this.registerSetting(new BooleanSetting("CheckFly", "Does mining calculation detect flying", true, this::isBooleanSetting13Visible));
   public Setting disablerNewDisablerMode = this.registerSetting(new EnumSetting("Grim", "Bypasses Grim's fastbreak check", DisablerNewDisablerMode.None, DisablerNewDisablerMode.values()));
   public Setting noneNormalMode = this.registerSetting(new EnumSetting("Desync", "Break block desync server", NoneNormalMode.None, NoneNormalMode.values(), this::isNoneNormalModeVisible));
   Setting booleanSetting14 = this.registerSetting(new BooleanSetting("NoCollision", "Assume block has broken", false, this::isBooleanSetting14Visible));
   Setting booleanSetting15 = this.registerSetting(new BooleanSetting("IgnoreBroken", "When blocks broke will not become crosshairtarget", false, this::isBooleanSetting15Visible));
   public Setting booleanSetting16 = this.registerSetting(new BooleanSetting("StrictCheck", "Prohibit sending packets during the pause period", false, this::isBooleanSetting16Visible));
   public Setting booleanSetting17 = this.registerSetting(new BooleanSetting("OnlyOnGround", "Works only when you are standing on blocks", false, this::isBooleanSetting17Visible));
   public Setting booleanSetting18 = this.registerSetting(new BooleanSetting("PauseOnFlying", "Pauses the current process while you are flying", false, this::isBooleanSetting18Visible));
   public Setting booleanSetting19 = this.registerSetting(new BooleanSetting("PauseInFluid", "Pauses the current process while you are swimming", false, this::isBooleanSetting19Visible));
   Setting settingSetting1_1;
   Setting settingSetting2_1;
   Setting settingSetting3;
   Setting numberSetting7;
   Setting booleanSetting20;
   Setting swapMode2;
   Setting booleanSetting21;
   Setting shapeMode;
   Setting colorSetting1;
   Setting colorSetting2;
   Setting colorSetting3;
   Setting colorSetting4;
   Setting easingMode;
   Setting booleanSetting22;
   Setting booleanSetting23;

   public BlockPosBoxMultitask() {
      super("SpeedMine", "Allows you to quickly mine blocks", ModuleCategory.PLAYER, 9999);
      KeybindSetting keybindSetting = new KeybindSetting("Pause", "Pause the mining", f(this.Kn() + "-pause", this::amn), this::isSettingSetting1Visible);
      this.settingSetting1_1 = this.registerSetting(keybindSetting);
      keybindSetting = new KeybindSetting("Cancel", "Cancel mine", f(this.Kn() + "-cancel", this::alw), this::isSettingSetting2Visible);
      this.settingSetting2_1 = this.registerSetting(keybindSetting);
      keybindSetting = new KeybindSetting("EnderChest", "Place ender chest on mine", f(this.Kn() + "-enderchest", BlockPosBoxMultitask::amk), this::isSettingSetting3Visible);
      this.settingSetting3 = this.registerSetting(keybindSetting);
      this.numberSetting7 = this.registerSetting(new NumberSetting("PlaceRange", "Interact block reach", 0.0, 4.5, 8.0, this::isNumberSetting7Visible));
      this.booleanSetting20 = this.registerSetting(new BooleanSetting("PlaceRotate", "Look at placement", true, this::isBooleanSetting20Visible));
      this.swapMode2 = this.registerSetting(new EnumSetting("PlaceSwap", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values(), this::isSwapMode2Visible));
      this.booleanSetting21 = this.registerSetting(new BooleanSetting("Render", "Renders a block overlay where the block will be breaked", true, this::isBooleanSetting21Visible));
      this.shapeMode = this.registerSetting(new EnumSetting("BoxMode", "Box rendering mode", ShapeMode.Both, ShapeMode.values(), this::isShapeModeVisible));
      this.colorSetting1 = this.registerSetting(createColorSetting("StartSide", "The side color", k(255, 0), this::isColorSetting1Visible));
      this.colorSetting2 = this.registerSetting(createColorSetting("EndSide", "The side color", k(0, 255), this::isColorSetting2Visible));
      this.colorSetting3 = this.registerSetting(createColorSetting("StartLine", "The line color", k(255, 0), this::isColorSetting3Visible));
      this.colorSetting4 = this.registerSetting(createColorSetting("EndLine", "The line color", k(0, 255), this::isColorSetting4Visible));
      this.easingMode = this.registerSetting(new EnumSetting("Easing", "Which type of curve animation to implement", Easing.CIRC_OUT, Easing.values(), this::isEasingModeVisible));
      this.booleanSetting22 = this.registerSetting(new BooleanSetting("BlockOutline", "Draw the outline of the block", true, this::isBooleanSetting22Visible));
      this.booleanSetting23 = this.registerSetting(new BooleanSetting("BoxAnimation", "Renders a block overlay with animation", true, this::isBooleanSetting23Visible));
      INSTANCE = this;
      Managers.playerScreenManager
         .akq(
            (UnidentifiedHelper17)this.settingSetting1_1.atx(),
            (UnidentifiedHelper17)this.settingSetting2_1.atx(),
            (UnidentifiedHelper17)this.settingSetting3.atx()
         );
   }

   public static BlockPosBoxMultitask alv() {
      return INSTANCE;
   }

   public void alw() {
      if (this.awg()) {
         if (this.runnable != null && !this.e) {
            this.d = true;
            this.runnable.run();
            this.d = false;
         }

         this.blockPosPlayerHelper2 = null;
      }
   }

   @Override
   public void awe() {
      list.clear();
      if (client.player != null && this.runnable != null) {
         this.d = true;
         this.runnable.run();
      }

      this.blockPosPlayerHelper2 = null;
      this.runnable = null;
      this.blockPosPlayerHelper = null;
      this.d = false;
      this.e = false;
   }

   @Override
   public String hy() {
      return this.blockPosPlayerHelper2 != null ? (int)(this.blockPosPlayerHelper2.d * 100.0F) + "%" : super.hy();
   }

   public boolean alx() {
      boolean flag;
      if (this.runnable != null) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   @EventListener
   public void aly(Render3DContext render3DContext) {
      if ((Boolean)this.booleanSetting21.atx()) {
         Easing easing = (Easing)this.easingMode.atx();
         if (this.blockPosPlayerHelper2 != null) {
            this.alz(render3DContext, easing, this.blockPosPlayerHelper2);
         }

         if (this.blockPosPlayerHelper != null && !this.blockPosPlayerHelper.e) {
            if (this.blockPosPlayerHelper2 != null) {
               BlockPos pos2 = this.blockPosPlayerHelper.pos;
               if (this.blockPosPlayerHelper2.pos.equals(pos2)) {
                  return;
               }
            }

            this.alz(render3DContext, easing, this.blockPosPlayerHelper);
         }
      }
   }

   private void alz(Render3DContext render3DContext, Easing easing, BlockPosPlayerHelper blockPosPlayerHelper3) {
      double d2 = easing.Yy(MathHelper.clamp(blockPosPlayerHelper3.c + (blockPosPlayerHelper3.d - blockPosPlayerHelper3.c) * render3DContext.f, 0.0F, 1.0F));
      Box box;
      if ((Boolean)this.booleanSetting23.atx()) {
         BlockPos pos2 = blockPosPlayerHelper3.pos;
         BlockState temp = client.world.getBlockState(pos2);
         BlockPos pos3 = blockPosPlayerHelper3.pos;
         ClientWorld world2 = client.world;
         VoxelShape voxelShape = temp.getOutlineShape((BlockView)world2, pos3);
         if (voxelShape.isEmpty()) {
            box = new Box(blockPosPlayerHelper3.pos);
         } else {
            Box box2;
            if ((Boolean)this.booleanSetting22.atx()) {
               Box box3 = voxelShape.getBoundingBox();
               BlockPos pos4 = blockPosPlayerHelper3.pos;
               box2 = box3.offset(pos4);
            } else {
               box2 = new Box(blockPosPlayerHelper3.pos);
            }

            Vec3d vec = box2.getCenter();
            double d3 = box2.getLengthX() / 2.0 * d2;
            double d4 = box2.getLengthY() / 2.0 * d2;
            double d5 = box2.getLengthZ() / 2.0 * d2;
            box = new Box(vec.subtract(d3, d4, d5), vec.add(d3, d4, d5));
         }
      } else {
         box = new Box(blockPosPlayerHelper3.pos);
      }

      Color color = ColorUtils.iE((Color)this.colorSetting1.atx(), ((Color)this.colorSetting1.atx()).getAlpha());
      Color color2 = ColorUtils.iE((Color)this.colorSetting2.atx(), ((Color)this.colorSetting2.atx()).getAlpha());
      Color color3 = ColorUtils.iD(color, color2, d2);
      Color color4 = ColorUtils.iE((Color)this.colorSetting3.atx(), ((Color)this.colorSetting3.atx()).getAlpha());
      Color color5 = ColorUtils.iE((Color)this.colorSetting4.atx(), ((Color)this.colorSetting4.atx()).getAlpha());
      Color color6 = ColorUtils.iD(color4, color5, d2);
      render3DContext.boxRenderer.Zd(box, color3, color6, (ShapeMode)this.shapeMode.atx());
   }

   @EventListener
   public void onAttackBlock(AttackBlockEvent attackBlockEvent) {
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         this.alI(attackBlockEvent.getPos(), (Integer)this.numberSetting3.atx());
         attackBlockEvent.aim();
      }
   }

   @EventListener
   public void onCancelBlockBreaking(CancelBlockBreakingEvent cancelBlockBreakingEvent) {
      if ((Boolean)this.booleanSetting3.atx() && !client.options.useKey.isPressed() && this.damagePacketMode.atx() == DamagePacketMode.Damage) {
         cancelBlockBreakingEvent.aim();
      }
   }

   @EventListener
   public void alC(StackEvent stackEvent) {
      if ((Boolean)this.booleanSetting2_1.atx() && this.damagePacketMode.atx() == DamagePacketMode.Damage) {
         stackEvent.aim();
      }
   }

   @EventListener
   public void onMiningCooldownFix(MiningCooldownFixEvent miningCooldownFixEvent) {
      if ((Boolean)this.booleanSetting1_1.atx() && this.damagePacketMode.atx() == DamagePacketMode.Damage) {
         miningCooldownFixEvent.aim();
      }
   }

   @EventListener
   public void onGetCollisionShape(GetCollisionShapeEvent getCollisionShapeEvent) {
      BlockPos pos2 = getCollisionShapeEvent.getPos();
      if (this.awg() && this.alN() && this.blockPosPlayerHelper2 != null) {
         BlockPos pos3 = this.blockPosPlayerHelper2.pos;
         if (pos2.equals(pos3) && this.blockPosPlayerHelper2.m > 0) {
            getCollisionShapeEvent.aim();
            getCollisionShapeEvent.setVoxelShape(VoxelShapes.empty());
         }
      }
   }

   public float alF() {
      return this.blockPosPlayerHelper2 != null ? this.blockPosPlayerHelper2.d : 0.0F;
   }

   public BlockPos alG() {
      return this.blockPosPlayerHelper2 != null ? this.blockPosPlayerHelper2.pos : null;
   }

   public boolean alH(BlockPos pos2, int n) {
      return this.blockPosPlayerHelper != null && (Boolean)this.booleanSetting7.atx() && this.blockPosPlayerHelper.pos.equals(pos2) ? false : this.alI(pos2, n);
   }

   public boolean alI(BlockPos pos2, int n) {
      if (!BlockUtils.TP(pos2)) {
         if (!this.alM(client.world.getBlockState(pos2).getBlock())) {
            return false;
         }

         if (this.damagePacketMode.atx() == DamagePacketMode.Packet && this.awg()) {
            if (this.blockPosPlayerHelper2 != null && this.blockPosPlayerHelper2.pos.equals(pos2)) {
               return true;
            }

            if (this.blockPosPlayerHelper2 != null
               && (this.blockPosPlayerHelper2.pos.equals(pos2) || !this.blockPosPlayerHelper2.e && this.blockPosPlayerHelper2.h > n)) {
               Vec3d temp = client.player.getEyePos();
               Vec3d vec = PlacementUtils.Fn(this.blockPosPlayerHelper2.pos, PlacementUtils.Fh(this.blockPosPlayerHelper2.pos));
               if (!(temp.distanceTo(vec) > (Double)this.numberSetting2.atx())) {
                  return false;
               }
            }

            if (this.runnable != null && !this.e) {
               this.d = true;
               this.runnable.run();
               this.d = false;
            }

            this.blockPosPlayerHelper2 = new BlockPosPlayerHelper(this, pos2, n, (Float)this.numberSetting1.atx());
            if (this.blockPosPlayerHelper != null) {
               this.blockPosPlayerHelper2.xq();
            } else {
               this.blockPosPlayerHelper2.xq();
               if (this.blockPosPlayerHelper != null) {
                  this.blockPosPlayerHelper.xs();
                  if (this.blockPosPlayerHelper.xu()) {
                     this.blockPosPlayerHelper = null;
                     if (this.e && this.runnable != null) {
                        this.d = true;
                        this.runnable.run();
                        this.d = false;
                     }
                  }
               }
            }

            return true;
         } else {
            Direction direction = PlacementUtils.Fh(pos2);
            Direction direction2 = direction != null ? direction : Direction.UP;
            client.interactionManager.attackBlock(pos2, direction2);
            if (this.disablerNewDisablerMode.atx() == DisablerNewDisablerMode.Disabler) {
               final Direction direction3 = direction;
               final BlockPos pos3 = pos2;
               Managers.serverManager.iQ(new SequencedPacketCreator() {
                  public Packet predict(int pos2) {
                     return BlockPosBoxMultitask.alW(pos3, direction3, pos2);
                  }
               });
            }

            final Direction direction4 = direction;
            final BlockPos pos4 = pos2;
            Managers.serverManager.iQ(new SequencedPacketCreator() {
               public Packet predict(int pos2) {
                  return BlockPosBoxMultitask.alV(pos4, direction4, pos2);
               }
            });
            return true;
         }
      } else {
         return false;
      }
   }

   @EventListener(priority = 100)
   private void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() != EventPhase.POST) {
         if (client.player != null && client.world != null) {
            if (((UnidentifiedHelper17)this.settingSetting3.atx()).c && this.blockPosPlayerHelper2 != null) {
               BlockPos pos2 = this.blockPosPlayerHelper2.pos;
               new BlockPosBlockItemHelper(pos2, Items.ENDER_CHEST, false, (SwapMode)this.swapMode2.atx())
                  .avM((Double)this.numberSetting7.atx(), (Boolean)this.booleanSetting20.atx(), false, 0.0);
            }

            int n;
            if ((n = ((DamagePacketMode)this.damagePacketMode.atx()).ordinal()) != 0) {
               if (n == 1) {
                  if (this.runnable != null) {
                     this.runnable.run();
                  }

                  if (this.blockPosPlayerHelper2 != null) {
                     this.blockPosPlayerHelper2.xq();
                  }

                  if (this.blockPosPlayerHelper != null) {
                     this.blockPosPlayerHelper.xs();
                     if (this.blockPosPlayerHelper.xu()) {
                        this.blockPosPlayerHelper = null;
                        if (this.e && this.runnable != null) {
                           this.d = true;
                           this.runnable.run();
                           this.d = false;
                        }
                     }
                  }
               }
            } else {
               ClientPlayerInteractionManager interaction = client.interactionManager;
               if (((ClientPlayerInteractionManagerMixin2)interaction).hookGetCurrentBreakingProgress() >= (Float)this.numberSetting1.atx()) {
                  float f2 = 1.0F;
                  ((ClientPlayerInteractionManagerMixin2)interaction).hookSetCurrentBreakingProgress(f2);
               }
            }

            this.stack = Managers.inventoryTracker.JW();
         } else {
            this.runnable = null;
            this.blockPosPlayerHelper = null;
            this.blockPosPlayerHelper2 = null;
         }
      }
   }

   @EventListener
   private void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      Packet packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof PlayerActionC2SPacket) {
         if (((PlayerActionC2SPacket)packet2).getAction() != Action.START_DESTROY_BLOCK) {
            if (((PlayerActionC2SPacket)packet2).getAction() == Action.STOP_DESTROY_BLOCK) {
               this.i = System.currentTimeMillis();
               if (this.disablerNewDisablerMode.atx() != DisablerNewDisablerMode.Time) {
                  this.j = this.i;
               }
            }
         } else {
            this.j = System.currentTimeMillis();
            if (((PlayerActionC2SPacket)packet2).getPos().getY() != -999) {
               if (this.disablerNewDisablerMode.atx() == DisablerNewDisablerMode.Disabler && this.damagePacketMode.atx() == DamagePacketMode.Damage) {
                  final PlayerActionC2SPacket playerActionC2SPacket = (PlayerActionC2SPacket)packet2;
                  Managers.serverManager.iQ(new SequencedPacketCreator() {
                     public Packet predict(int sendImmediatelyEvent) {
                        return BlockPosBoxMultitask.alU(playerActionC2SPacket, sendImmediatelyEvent);
                     }
                  });
               }

               if (this.disablerNewDisablerMode.atx() == DisablerNewDisablerMode.NewDisabler && this.damagePacketMode.atx() == DamagePacketMode.Damage) {
                  Managers.serverManager.iQ(BlockPosBoxMultitask::alT);
               }
            }
         }
      }
   }

   public ItemStack alL() {
      return Managers.inventoryTracker.JW();
   }

   public boolean alM(Block block2) {
      if (this.whitelistBlacklistMode.atx() == WhitelistBlacklistMode.Blacklist) {
         boolean flag;
         if (!((List)this.blockListSetting2.atx()).contains(block2)) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         return this.whitelistBlacklistMode.atx() == WhitelistBlacklistMode.Whitelist ? ((List)this.blockListSetting1.atx()).contains(block2) : true;
      }
   }

   public boolean alN() {
      return (Boolean)this.booleanSetting14.atx();
   }

   @EventListener
   public void onTickPre2(TickPreEvent tickPreEvent) {
      if (!list.isEmpty()) {
         if (stopwatchImpl.afq(125)) {
            list.clear();
         } else {
            List temp = list;
            Predicate predicate = new Predicate() {
               @Override
               public boolean test(Object tickPreEvent) {
                  return BlockPosBoxMultitask.alS((BlockPos)tickPreEvent);
               }
            };
            temp.removeIf(predicate);
         }
      }
   }

   public boolean alP() {
      boolean flag;
      if (System.currentTimeMillis() - this.i <= 280L && ((DisablerNewDisablerMode)this.disablerNewDisablerMode.atx()).FW()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean alQ() {
      boolean flag;
      if (this.awg() && (Boolean)this.booleanSetting15.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static Setting alR(BlockPosBoxMultitask blockPosBoxMultitask) {
      return blockPosBoxMultitask.breakSetting;
   }

   static boolean alS(BlockPos pos2) {
      return client.world.isAir(pos2);
   }

   private static Packet alT(int n) {
      return new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, j(), Direction.UP, n);
   }

   static Packet alU(PlayerActionC2SPacket playerActionC2SPacket, int n) {
      return new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, playerActionC2SPacket.getPos(), playerActionC2SPacket.getDirection(), n);
   }

   static Packet alV(BlockPos pos2, Direction direction, int n) {
      return new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, pos2, direction, n);
   }

   static Packet alW(BlockPos pos2, Direction direction, int n) {
      return new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, pos2, direction, n);
   }

   private Boolean isBooleanSetting23Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet && (Boolean)this.booleanSetting21.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting22Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet && (Boolean)this.booleanSetting21.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isEasingModeVisible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet && (Boolean)this.booleanSetting21.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isColorSetting4Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet && (Boolean)this.booleanSetting21.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isColorSetting3Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet && (Boolean)this.booleanSetting21.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isColorSetting2Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet && (Boolean)this.booleanSetting21.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isColorSetting1Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet && (Boolean)this.booleanSetting21.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isShapeModeVisible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet && (Boolean)this.booleanSetting21.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting21Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSwapMode2Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting20Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isNumberSetting7Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSettingSetting3Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static void amk() {
   }

   private Boolean isSettingSetting2Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSettingSetting1Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private void amn() {
      boolean flag;
      if (!this.k) {
         flag = true;
      } else {
         flag = false;
      }

      this.k = flag;
   }

   private Boolean isBooleanSetting19Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting18Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting17Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting16Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting15Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting14Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isNoneNormalModeVisible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting13Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting12Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting11Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting10Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting9Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting8Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isNumberSetting6Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting7Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isNumberSetting5Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting6Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isNumberSetting4Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet && !(Boolean)this.booleanSetting5.atx() && (Boolean)this.booleanSetting4.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting5Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet && (Boolean)this.booleanSetting4.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting4Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSwapModeVisible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBlockListSetting2Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBlockListSetting1Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isWhitelistBlacklistModeVisible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isNumberSetting3Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSwingModeVisible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isNumberSetting2Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Packet) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting3Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Damage) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting2Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Damage) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting1Visible() {
      boolean flag;
      if (this.damagePacketMode.atx() == DamagePacketMode.Damage) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static Color k(int n, int n2) {
      return new Color(n, n2, 0, 80);
   }

   private static UnidentifiedHelper17 f(String str, Runnable runnable2) {
      return new UnidentifiedHelper17(str, -1, runnable2);
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color, Supplier supplier) {
      return new ColorSetting(str, str2, color, true, false, supplier);
   }

   private static BlockPos j() {
      return new BlockPos(0, -999, 0);
   }
}
