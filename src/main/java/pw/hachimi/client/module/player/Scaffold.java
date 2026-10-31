package pw.hachimi.client.module.player;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.player.ScaffoldNormalGreatestMode;
import pw.hachimi.client.model.player.ScaffoldOption;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.setting.BlockListSetting;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.movement.MovementUtils;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.player.ScaffoldHitResultBlockPosHelper;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public final class Scaffold extends Multitask {
   private final Map map = new HashMap();
   Setting multitask = this.registerSetting(new BooleanSetting("Rotate", "Rotates to block before placing", false));
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("RotateHold", "Holds rotations to scaffold blocks", false, this::isBooleanSetting1Visible));
   Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("Grim", "Uses grim interactions", false));
   Setting grimV3 = this.registerSetting(new BooleanSetting("GrimV3", "Uses grim new interactions", false));
   Setting selection = this.registerSetting(new EnumSetting("Selection", "The selection of blocks to use for scaffold", ScaffoldOption.c, ScaffoldOption.values()));
   Setting whitelist = this.registerSetting(new BlockListSetting("Whitelist", "Valid block whitelist", Blocks.DIRT, Blocks.OBSIDIAN));
   Setting blacklist = this.registerSetting(new BlockListSetting("Blacklist", "Valid block blacklist", Blocks.SHULKER_BOX));
   Setting keepY = this.registerSetting(new BooleanSetting("KeepY", "Keeps the same y-level", false));
   Setting tower = this.registerSetting(new BooleanSetting("Tower", "Goes up faster when holding down space", true, this::isTowerVisible));
   Setting blockSelection = this.registerSetting(new EnumSetting("BlockSelection", "How to pick a block from the hotbar", ScaffoldNormalGreatestMode.NORMAL, ScaffoldNormalGreatestMode.values()));
   Setting render = this.registerSetting(new BooleanSetting("Render", "Renders where scaffold is placing blocks", false));
   Setting fadeTime = this.registerSetting(new NumberSetting("Fade-Time", "Timer for the fade", 0, 250, 1000));
   private ScaffoldHitResultBlockPosHelper scaffoldHitResultBlockPosHelper;
   private ScaffoldHitResultBlockPosHelper scaffoldHitResultBlockPosHelper2;
   private float[] p;
   private int q;
   public static boolean aj;

   public Scaffold() {
      super("Scaffold", "Places blocks at the players feet", ModuleCategory.PLAYER, 790);
   }

   @Override
   public void awe() {
      if (client.player != null) {
         Managers.inventoryTracker.JI();
      }

      this.q = -1;
      this.p = null;
      this.scaffoldHitResultBlockPosHelper = null;
      this.scaffoldHitResultBlockPosHelper2 = null;
      this.map.clear();
   }

   @EventListener
   public void ate(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (!(Boolean)this.breakSetting.atx() && this.RG()) {
         this.scaffoldHitResultBlockPosHelper = null;
         this.scaffoldHitResultBlockPosHelper2 = null;
      } else {
         int n = this.atj();
         if (n == -1) {
            this.scaffoldHitResultBlockPosHelper = null;
            this.scaffoldHitResultBlockPosHelper2 = null;
         } else {
            this.scaffoldHitResultBlockPosHelper2 = this.ath(false);
            this.scaffoldHitResultBlockPosHelper = this.ath((Boolean)this.booleanSetting1_1.atx());
            if (this.scaffoldHitResultBlockPosHelper == null) {
               if ((Boolean)this.grimV3.atx() && (Boolean)this.multitask.atx()) {
                  float f = client.player.getYaw();
                  if (client.options.forwardKey.isPressed() && !client.options.backKey.isPressed()) {
                     if (client.options.leftKey.isPressed() && !client.options.rightKey.isPressed()) {
                        f -= 45.0F;
                     } else if (client.options.rightKey.isPressed() && !client.options.leftKey.isPressed()) {
                        f += 45.0F;
                     }
                  } else if (client.options.backKey.isPressed() && !client.options.forwardKey.isPressed()) {
                     f += 180.0F;
                     if (client.options.leftKey.isPressed() && !client.options.rightKey.isPressed()) {
                        f += 45.0F;
                     } else if (client.options.rightKey.isPressed() && !client.options.leftKey.isPressed()) {
                        f -= 45.0F;
                     }
                  } else if (client.options.leftKey.isPressed() && !client.options.rightKey.isPressed()) {
                     f -= 90.0F;
                  } else if (client.options.rightKey.isPressed() && !client.options.leftKey.isPressed()) {
                     f += 90.0F;
                  }

                  this.ajM(MathHelper.wrapDegrees(f), 90.0F);
               }
            } else {
               this.atg(this.scaffoldHitResultBlockPosHelper);
               if (this.scaffoldHitResultBlockPosHelper.alq() == null) {
                  if (!this.atl() && (Boolean)this.multitask.atx() && this.p != null) {
                     this.ajM(this.p[0], this.p[1]);
                  }
               } else {
                  ClientWorld temp = client.world;
                  BlockPos pos = this.scaffoldHitResultBlockPosHelper.alp();
                  if (temp.getBlockState(pos).isReplaceable()) {
                     if (CollisionUtils.aiz(new Box(this.scaffoldHitResultBlockPosHelper.alp()))) {
                        Direction direction = this.scaffoldHitResultBlockPosHelper.getBlockHitResult().getSide().getOpposite();
                        if (direction != null) {
                           int n2 = Managers.inventoryTracker.JS();
                           if ((Boolean)this.multitask.atx()) {
                              float[] fs = this.scaffoldHitResultBlockPosHelper.alq();
                              if (fs == null) {
                                 return;
                              }

                              this.p = fs;
                              if ((Boolean)this.booleanSetting2_1.atx()) {
                                 RotationUtils.nH(fs[0], fs[1]);
                              } else {
                                 this.ajM(fs[0], fs[1]);
                              }

                              if (n2 != n) {
                                 Managers.inventoryTracker.JH(n);
                                 PlacementUtils.Fd(this.scaffoldHitResultBlockPosHelper.alp(), direction, Hand.MAIN_HAND, false, false);
                                 Managers.inventoryTracker.JH(n2);
                              } else {
                                 PlacementUtils.Fd(this.scaffoldHitResultBlockPosHelper.alp(), direction, Hand.MAIN_HAND, false, false);
                              }

                              RotationUtils.nK();
                           } else if (n2 != n) {
                              Managers.inventoryTracker.JH(n);
                              PlacementUtils.Fd(this.scaffoldHitResultBlockPosHelper.alp(), direction, Hand.MAIN_HAND, false, false);
                              Managers.inventoryTracker.JH(n2);
                           } else {
                              PlacementUtils.Fd(this.scaffoldHitResultBlockPosHelper.alp(), direction, Hand.MAIN_HAND, false, false);
                           }

                           if (!this.atl() && (Boolean)this.tower.atx() && client.options.jumpKey.isPressed()) {
                              Vec3d vec = client.player.getVelocity();
                              double d2 = vec.y;
                              if (client.player.isOnGround() || d2 < 0.1 || d2 <= 0.16477328182606651) {
                                 double d3 = vec.z;
                                 double d4 = vec.x;
                                 client.player.setVelocity(d4, 0.42F, d3);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventListener
   public void atf(Render3DContext render3DContext) {
      if ((Boolean)this.render.atx()) {
         Iterator iterator2 = this.map.entrySet().iterator();

         while (iterator2.hasNext()) {
            Entry entry = (Entry)iterator2.next();
            ((UnidentifiedHelper7)entry.getValue()).qV(false);
            int n = (int)(40.0 * ((UnidentifiedHelper7)entry.getValue()).qW());
            int n2 = (int)(100.0 * ((UnidentifiedHelper7)entry.getValue()).qW());
            Color color = ColorHelper3.auO().auW(n);
            Color color2 = ColorHelper3.auO().auW(n2);
            render3DContext.boxRenderer.Zc((BlockPos)entry.getKey(), color, color2, ShapeMode.Both);
         }

         if (this.scaffoldHitResultBlockPosHelper2 == null || this.scaffoldHitResultBlockPosHelper2.getBlockHitResult() == null) {
            return;
         }

         if ((Boolean)this.render.atx()) {
            UnidentifiedHelper7 unidentifiedHelper7 = b(((Integer)this.fadeTime.atx()).intValue());
            Map temp = this.map;
            BlockPos pos = this.scaffoldHitResultBlockPosHelper2.alp();
            temp.put(pos, unidentifiedHelper7);
         }

         Set set = this.map.entrySet();
         final short s = 21118;
         final byte b2 = -74;
         final short s2 = 22714;
         Predicate predicate = new Predicate() {
            @Override
            public boolean test(Object render3DContext) {
               return Scaffold.atm(s2, b2, s, (Entry)render3DContext);
            }
         };
         set.removeIf(predicate);
      }
   }

   private void atg(ScaffoldHitResultBlockPosHelper scaffoldHitResultBlockPosHelper3) {
      BlockPos pos = scaffoldHitResultBlockPosHelper3.getBlockHitResult().getBlockPos();
      Direction direction = scaffoldHitResultBlockPosHelper3.getBlockHitResult().getSide();
      Vec3d temp = pos.toCenterPos();
      double temp2 = direction.getOffsetX() * 0.5F;
      double d2 = direction.getOffsetZ() * 0.5F;
      double d3 = direction.getOffsetY() * 0.5F;
      double d4 = temp2;
      Vec3d vec = temp.add(d4, d3, d2);
      scaffoldHitResultBlockPosHelper3.alr(RotationUtils.nM(vec));
      scaffoldHitResultBlockPosHelper3.setBlockHitResult(b(vec, direction, pos));
   }

   private ScaffoldHitResultBlockPosHelper ath(boolean flag) {
      int n = (int)Math.round(client.player.getY()) - 1;
      if ((Boolean)this.keepY.atx() && MovementUtils.awH()) {
         if (client.player.isOnGround() || this.q == -1) {
            this.q = (int)Math.floor(client.player.getY()) - 1;
         }

         n = this.q;
      }

      double temp = client.player.getX();
      double temp2 = n;
      double d2 = client.player.getZ();
      double d3 = temp2;
      double d4 = temp;
      BlockPos pos = this.ati(d4, d3, d2);
      if (!flag && !client.world.getBlockState(pos).isReplaceable()) {
         return null;
      }

      Direction[] directions = Direction.values();
      int n2 = directions.length;

      for (int n3 = 0; n3 < n2; n3++) {
         Direction direction = directions[n3];
         BlockPos pos2 = pos.offset(direction);
         if (!client.world.getBlockState(pos2).isReplaceable()) {
            return ScaffoldHitResultBlockPosHelper.alm(pos2, direction.getOpposite());
         }
      }

      directions = Direction.values();
      n2 = directions.length;

      for (int n4 = 0; n4 < n2; n4++) {
         Direction direction2 = directions[n4];
         BlockPos pos3 = pos.offset(direction2);
         if (client.world.getBlockState(pos3).isReplaceable()) {
            Direction[] directions2 = Direction.values();
            int n5 = directions2.length;

            for (int n6 = 0; n6 < n5; n6++) {
               Direction direction3 = directions2[n6];
               BlockPos pos4 = pos3.offset(direction3);
               if (!client.world.getBlockState(pos4).isReplaceable()) {
                  return ScaffoldHitResultBlockPosHelper.alm(pos4, direction3.getOpposite());
               }
            }
         }
      }

      return null;
   }

   public BlockPos ati(double d2, double d3, double d4) {
      int n = MathHelper.floor(d2);
      int n2 = (int)Math.round(d3);
      int n3 = MathHelper.floor(d4);
      return new BlockPos(n, n2, n3);
   }

   private int atj() {
      int n;
      ItemStack stack;
      label97: {
         label96: {
            ItemStack stack2 = Managers.inventoryTracker.JV();
            if (!stack2.isEmpty()) {
               Item item2 = stack2.getItem();
               if (item2 instanceof BlockItem) {
                  if (this.atk(((BlockItem)item2).getBlock())) {
                     return Managers.inventoryTracker.JS();
                  }

                  n = 0;
                  stack = client.player.getInventory().getStack(0);
                  if (!stack.isEmpty()) {
                     Item item3 = stack.getItem();
                     if (item3 instanceof BlockItem && this.atk(((BlockItem)item3).getBlock())) {
                        if (this.blockSelection.atx() == ScaffoldNormalGreatestMode.NORMAL) {
                           return n;
                        }

                        int n2 = stack.getCount();

                        while (++n < 9) {
                           stack = client.player.getInventory().getStack(n);
                           if (!stack.isEmpty()) {
                              item3 = stack.getItem();
                              if (item3 instanceof BlockItem && this.atk(((BlockItem)item3).getBlock())) {
                                 if (this.blockSelection.atx() == ScaffoldNormalGreatestMode.NORMAL) {
                                    return n;
                                 }

                                 if (stack.getCount() > n2) {
                                    break label97;
                                 }
                              }
                           }
                        }

                        return 0;
                     }
                  }

                  n++;
                  break label96;
               }
            }

            n = 0;
         }

         while (true) {
            if (n >= 9) {
               return -1;
            }

            PlayerInventory temp = client.player.getInventory();
            int n3 = n;
            stack = temp.getStack(n3);
            if (!stack.isEmpty()) {
               Item item4 = stack.getItem();
               if (item4 instanceof BlockItem && this.atk(((BlockItem)item4).getBlock())) {
                  if (this.blockSelection.atx() == ScaffoldNormalGreatestMode.NORMAL) {
                     return n;
                  }
                  break;
               }
            }

            n++;
         }
      }

      label62:
      while (true) {
         int n4 = n;
         int n5 = stack.getCount();

         while (++n < 9) {
            stack = client.player.getInventory().getStack(n);
            if (!stack.isEmpty()) {
               Item item5 = stack.getItem();
               if (item5 instanceof BlockItem && this.atk(((BlockItem)item5).getBlock())) {
                  if (this.blockSelection.atx() == ScaffoldNormalGreatestMode.NORMAL) {
                     return n;
                  }

                  if (n4 == -1 || stack.getCount() > n5) {
                     continue label62;
                  }
               }
            }
         }

         return n4;
      }
   }

   private boolean atk(Block block2) {
      return switch ((ScaffoldOption)this.selection.atx()) {
         case a_ -> ((BlockListSetting)this.whitelist).afW(block2);
         case b -> {
            if (!((BlockListSetting)this.blacklist).afW(block2)) {
               yield true;
            } else {
               yield false;
            }
         }
         case c -> true;
      };
   }

   public boolean atl() {
      boolean flag;
      if (!(Boolean)this.booleanSetting2_1.atx() && !(Boolean)this.grimV3.atx()) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   static boolean atm(int n, short s, short s2, Entry entry) {
      return ((UnidentifiedHelper7)entry.getValue()).qW() == 0.0;
   }

   private Boolean isTowerVisible() {
      boolean flag;
      if (!this.atl()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting1Visible() {
      return (Boolean)this.multitask.atx();
   }

   private static MatchException d() {
      return new MatchException(null, null);
   }

   private static BlockHitResult b(Vec3d vec, Direction direction, BlockPos pos) {
      return new BlockHitResult(vec, direction, pos, false);
   }

   private static UnidentifiedHelper7 b(float f) {
      return new UnidentifiedHelper7(true, f);
   }
}
