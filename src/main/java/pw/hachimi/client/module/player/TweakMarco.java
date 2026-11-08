package pw.hachimi.client.module.player;

import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import pw.hachimi.client.manager.InventoryTracker;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.player.SwingMode;
import pw.hachimi.client.model.player.TweakMarcoOption;
import pw.hachimi.client.model.world.PosState;
import pw.hachimi.client.module.Module5;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.combat.AntiRegear;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.KeybindSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper17;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.client.utils.world.Vec3dEntityUtils2;
import pw.hachimi.client.module.player.Freecam;

public class TweakMarco extends Module5 {
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting swing = this.registerSetting(new EnumSetting("Swing", "Swing side for interact block", SwingMode.Normal, SwingMode.values()));
   Setting friend;
   Setting firework;
   Setting chestSwap;
   Setting priority;
   Setting withFirework;
   Setting pearl;
   Setting regear;
   Setting enderChest;
   Setting throughWall;
   Setting airPlace;
   Setting autoOpen;
   Setting rotate;

   public TweakMarco() {
      super("TweakMarco", "Adds an additional bind on the button", ModuleCategory.PLAYER);
      KeybindSetting keybindSetting = new KeybindSetting("Friend", "Friends players when click", a(this.Kn() + "-friend", this::isFriendVisible));
      this.friend = this.registerSetting(keybindSetting);
      keybindSetting = new KeybindSetting("Firework", "Uses firework to boost elytra when click", a(this.Kn() + "-firework", this::isFireworkVisible));
      this.firework = this.registerSetting(keybindSetting);
      keybindSetting = new KeybindSetting("ChestSwap", "Swaps chestplate when click", a(this.Kn() + "-chestswap", this::isChestSwapVisible));
      this.chestSwap = this.registerSetting(keybindSetting);
      this.priority = this.registerSetting(new EnumSetting("Priority", "The chestplate material to prioritize", TweakMarcoOption.a_, TweakMarcoOption.values()));
      this.withFirework = this.registerSetting(new BooleanSetting("WithFirework", "Automatically fireworks when swapping to an elytra", false));
      keybindSetting = new KeybindSetting("Pearl", "Throws a pearl when click", a(this.Kn() + "-pearl", this::isPearlVisible));
      this.pearl = this.registerSetting(keybindSetting);
      keybindSetting = new KeybindSetting("Regear", "Regear when click", a(this.Kn() + "-shulkerbox", this::isRegearVisible));
      this.regear = this.registerSetting(keybindSetting);
      keybindSetting = new KeybindSetting("EnderChest", "Regear when click", a(this.Kn() + "-enderchest", this::isEnderChestVisible));
      this.enderChest = this.registerSetting(keybindSetting);
      this.throughWall = this.registerSetting(new BooleanSetting("ThroughWall", "Place the shulkerbox through the wall", false));
      this.airPlace = this.registerSetting(new BooleanSetting("AirPlace", "Replace block exploit allow you place block at air", false));
      this.autoOpen = this.registerSetting(new BooleanSetting("AutoOpen", "Open shulkerbox after place", true));
      this.rotate = this.registerSetting(new BooleanSetting("Rotate", "Automatically faces towards the block being placed", true));
      Managers.playerScreenManager
         .akq(
            (UnidentifiedHelper17)this.pearl.atx(),
            (UnidentifiedHelper17)this.firework.atx(),
            (UnidentifiedHelper17)this.friend.atx(),
            (UnidentifiedHelper17)this.chestSwap.atx(),
            (UnidentifiedHelper17)this.regear.atx(),
            (UnidentifiedHelper17)this.enderChest.atx()
         );
   }

   public void isEnderChestVisible() {
      if (this.awg()) {
         HitResult hitResult = client.crosshairTarget;
         if (hitResult instanceof BlockHitResult) {
            ClientWorld temp = client.world;
            BlockPos pos2 = ((BlockHitResult)hitResult).getBlockPos();
            if (temp.getBlockState(pos2).getBlock() instanceof EnderChestBlock) {
               PlacementUtils.Fe(((BlockHitResult)hitResult).getBlockPos(), (Boolean)this.rotate.atx());
               return;
            }
         }

         BlockPos pos3 = this.GQ();
         if (pos3 != null) {
            Direction direction3 = GI(pos3);
            if (direction3 != null) {
               BlockPosBlockItemHelper blockPosBlockItemHelper = new BlockPosBlockItemHelper(pos3, new Predicate() {
                  @Override
                  public boolean test(Object obj) {
                     return TweakMarco.GX((ItemStack)obj);
                  }
               }, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx());
               blockPosBlockItemHelper.avK(4.5, false);
               if (blockPosBlockItemHelper.avJ()) {
                  int n = this.GH();
                  int n2 = Managers.inventoryTracker.JS();
                  if (!(Boolean)this.autoOpen.atx() || this.swapAction.atx() == SwapMode.Hotbar || n == -1) {
                     blockPosBlockItemHelper.stackSlotHelper.LO(n2);
                     if (blockPosBlockItemHelper.direction2 == null) {
                        if (blockPosBlockItemHelper.direction != null) {
                           PlacementUtils.Fc(blockPosBlockItemHelper.pos, direction3, Hand.MAIN_HAND, blockPosBlockItemHelper.a, (Boolean)this.rotate.atx());
                        }
                     } else {
                        PlacementUtils.Fd(blockPosBlockItemHelper.pos, blockPosBlockItemHelper.direction2, Hand.MAIN_HAND, blockPosBlockItemHelper.a, (Boolean)this.rotate.atx());
                     }

                     blockPosBlockItemHelper.avP();
                     if ((Boolean)this.autoOpen.atx()) {
                        PlacementUtils.Fg(pos3, direction3, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                     }

                     blockPosBlockItemHelper.stackSlotHelper.LP(n2);
                  } else if (n != -2) {
                     Managers.inventoryTracker.JF(n);
                     blockPosBlockItemHelper.stackSlotHelper.LO(n);
                     if (blockPosBlockItemHelper.direction2 == null) {
                        if (blockPosBlockItemHelper.direction != null) {
                           PlacementUtils.Fc(blockPosBlockItemHelper.pos, direction3, Hand.MAIN_HAND, blockPosBlockItemHelper.a, (Boolean)this.rotate.atx());
                        }
                     } else {
                        PlacementUtils.Fd(blockPosBlockItemHelper.pos, blockPosBlockItemHelper.direction2, Hand.MAIN_HAND, blockPosBlockItemHelper.a, (Boolean)this.rotate.atx());
                     }

                     blockPosBlockItemHelper.avP();
                     blockPosBlockItemHelper.stackSlotHelper.LP(n);
                     PlacementUtils.Fg(pos3, direction3, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                     Managers.inventoryTracker.JF(n2);
                  } else {
                     blockPosBlockItemHelper.stackSlotHelper.LO(n2);
                     if (blockPosBlockItemHelper.direction2 == null) {
                        if (blockPosBlockItemHelper.direction != null) {
                           PlacementUtils.Fc(blockPosBlockItemHelper.pos, direction3, Hand.MAIN_HAND, blockPosBlockItemHelper.a, (Boolean)this.rotate.atx());
                        }
                     } else {
                        PlacementUtils.Fd(blockPosBlockItemHelper.pos, blockPosBlockItemHelper.direction2, Hand.MAIN_HAND, blockPosBlockItemHelper.a, (Boolean)this.rotate.atx());
                     }

                     blockPosBlockItemHelper.avP();
                     blockPosBlockItemHelper.stackSlotHelper.LP(n2);
                     PlacementUtils.Fg(pos3, direction3, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                  }
               }
            }
         }
      }
   }

   public void isRegearVisible() {
      if (this.awg()) {
         HitResult hitResult = client.crosshairTarget;
         if (hitResult instanceof BlockHitResult) {
            ClientWorld temp = client.world;
            BlockPos pos2 = ((BlockHitResult)hitResult).getBlockPos();
            if (temp.getBlockState(pos2).getBlock() instanceof ShulkerBoxBlock) {
               PlacementUtils.Fe(((BlockHitResult)hitResult).getBlockPos(), (Boolean)this.rotate.atx());
               return;
            }
         }

         BlockPos pos3 = this.GR();
         if (pos3 != null) {
            Direction direction3 = GI(pos3);
            if (direction3 != null) {
               BlockPosBlockItemHelper blockPosBlockItemHelper = new BlockPosBlockItemHelper(pos3, new Predicate() {
                  @Override
                  public boolean test(Object obj) {
                     return TweakMarco.GW((ItemStack)obj);
                  }
               }, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx());
               blockPosBlockItemHelper.avK(4.5, false);
               blockPosBlockItemHelper.direction2 = GJ(pos3);
               if (blockPosBlockItemHelper.avJ()) {
                  int n = this.GH();
                  int n2 = Managers.inventoryTracker.JS();
                  if (!(Boolean)this.autoOpen.atx() || this.swapAction.atx() == SwapMode.Hotbar || n == -1) {
                     blockPosBlockItemHelper.stackSlotHelper.LO(n2);
                     if (blockPosBlockItemHelper.direction2 == null) {
                        if (blockPosBlockItemHelper.direction != null) {
                           PlacementUtils.Fc(blockPosBlockItemHelper.pos, direction3, Hand.MAIN_HAND, blockPosBlockItemHelper.a, (Boolean)this.rotate.atx());
                        }
                     } else {
                        PlacementUtils.Fd(blockPosBlockItemHelper.pos, blockPosBlockItemHelper.direction2, Hand.MAIN_HAND, blockPosBlockItemHelper.a, (Boolean)this.rotate.atx());
                     }

                     AntiRegear.yz().yE(pos3);
                     blockPosBlockItemHelper.avP();
                     if ((Boolean)this.autoOpen.atx()) {
                        PlacementUtils.Fg(pos3, direction3, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                     }

                     blockPosBlockItemHelper.stackSlotHelper.LP(n2);
                  } else if (n != -2) {
                     Managers.inventoryTracker.JF(n);
                     blockPosBlockItemHelper.stackSlotHelper.LO(n);
                     if (blockPosBlockItemHelper.direction2 == null) {
                        if (blockPosBlockItemHelper.direction != null) {
                           PlacementUtils.Fc(blockPosBlockItemHelper.pos, direction3, Hand.MAIN_HAND, blockPosBlockItemHelper.a, (Boolean)this.rotate.atx());
                        }
                     } else {
                        PlacementUtils.Fd(blockPosBlockItemHelper.pos, blockPosBlockItemHelper.direction2, Hand.MAIN_HAND, blockPosBlockItemHelper.a, (Boolean)this.rotate.atx());
                     }

                     AntiRegear.yz().yE(pos3);
                     blockPosBlockItemHelper.avP();
                     blockPosBlockItemHelper.stackSlotHelper.LP(n);
                     PlacementUtils.Fg(pos3, direction3, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                     Managers.inventoryTracker.JF(n2);
                  } else {
                     blockPosBlockItemHelper.stackSlotHelper.LO(n2);
                     if (blockPosBlockItemHelper.direction2 == null) {
                        if (blockPosBlockItemHelper.direction != null) {
                           PlacementUtils.Fc(blockPosBlockItemHelper.pos, direction3, Hand.MAIN_HAND, blockPosBlockItemHelper.a, (Boolean)this.rotate.atx());
                        }
                     } else {
                        PlacementUtils.Fd(blockPosBlockItemHelper.pos, blockPosBlockItemHelper.direction2, Hand.MAIN_HAND, blockPosBlockItemHelper.a, (Boolean)this.rotate.atx());
                     }

                     AntiRegear.yz().yE(pos3);
                     blockPosBlockItemHelper.avP();
                     blockPosBlockItemHelper.stackSlotHelper.LP(n2);
                     PlacementUtils.Fg(pos3, direction3, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                  }
               }
            }
         }
      }
   }

   private int GH() {
      ItemStack stack = Managers.inventoryTracker.JV();
      int n;
      if (!stack.isEmpty()) {
         if (stack.getItem() instanceof BlockItem) {
            return -2;
         }

         n = 0;
      } else {
         n = 0;
         if (false) {
            return -1;
         }
      }

      do {
         PlayerInventory temp = client.player.getInventory();
         int n2 = n;
         ItemStack stack2 = temp.getStack(n2);
         if (!stack2.isEmpty() && stack2.getItem() instanceof BlockItem) {
            return n;
         }
      } while (++n < 9);

      return -1;
   }

   public static Direction GI(BlockPos pos2) {
      Direction direction3 = null;
      double d5 = Double.MAX_VALUE;
      Direction[] directions = Direction.values();
      int n = directions.length;

      for (int n2 = 0; n2 < n; n2++) {
         Direction direction4 = directions[n2];
         Vec3d temp = client.player.getEyePos();
         Vec3d vec = pos2.toCenterPos().offset(direction4, 0.5);
         double d6 = temp.distanceTo(vec);
         if (d6 < d5
            && PlacementUtils.blockPosDirectionBase.Hu(pos2, direction4, client.world, client.player, new PosState(pos2, Blocks.SHULKER_BOX.getDefaultState()))) {
            d5 = d6;
            direction3 = direction4;
         }
      }

      return direction3;
   }

   public static Direction GJ(BlockPos pos2) {
      if (pos2.getY() >= client.world.getBottomY() && pos2.getY() < client.world.getTopY()) {
         Direction[] directions = Direction.values();
         int n = directions.length;

         for (int n2 = 0; n2 < n; n2++) {
            Direction direction3 = directions[n2];
            BlockPos pos3 = pos2.offset(direction3);
            BlockState state = client.world.getBlockState(pos3);
            ClientWorld temp = client.world;
            Direction direction4 = direction3.getOpposite();
            BlockPos pos4 = pos2.offset(direction4);
            if (!temp.isAir(pos4)) {
               temp = client.world;
               Direction direction5 = direction3.getOpposite();
               BlockPos pos5 = pos2.offset(direction5);
               if (temp.getBlockState(pos5).getFluidState().isEmpty()) {
                  continue;
               }
            }

            if (!PlacementUtils.Fm(state, pos3)
               && !state.isReplaceable()
               && state.getFluidState().isEmpty()
               && PlacementUtils.blockPosDirectionBase.Ht(pos3, direction3.getOpposite(), client.world, client.player)) {
               return direction3;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public void isFriendVisible() {
      if (this.awg()) {
         double d5 = client.player.getEntityInteractionRange();
         HitResult hitResult;
         if (Freecam.gP().awg()) {
            float[] fs = Freecam.gP().hh();
            Vec3d vec = Freecam.gP().getVec();
            hitResult = Vec3dEntityUtils2.Ry(d5, vec, fs);
         } else {
            hitResult = Vec3dEntityUtils2.Rz(d5);
         }

         if (hitResult != null && hitResult.getType() == Type.ENTITY) {
            Entity entity2 = ((EntityHitResult)hitResult).getEntity();
            if (entity2 instanceof PlayerEntity) {
               if (Managers.friendManager.awt(((PlayerEntity)entity2).getName())) {
                  Managers.friendManager.awy(((PlayerEntity)entity2).getName());
               } else {
                  Managers.friendManager.aww(((PlayerEntity)entity2).getName());
               }

               return;
            }
         }

         ((UnidentifiedHelper17)this.friend.atx()).agy();
      }
   }

   public void isFireworkVisible() {
      if (this.awg()) {
         if (((UnidentifiedHelper17)this.friend.atx()).c
            && ((UnidentifiedHelper17)this.firework.atx()).agt() == ((UnidentifiedHelper17)this.friend.atx()).agt()) {
            return;
         }

         StackSlotHelper stackSlotHelper2 = new StackSlotHelper(Items.FIREWORK_ROCKET, (SwapMode)this.swapAction.atx()).LK();
         if (!client.player.isFallFlying() || !stackSlotHelper2.LL()) {
            ((UnidentifiedHelper17)this.firework.atx()).agy();
            return;
         }

         int n = Managers.inventoryTracker.JS();
         stackSlotHelper2.LO(n);
         Managers.serverManager.iQ(TweakMarco::GV);
         ((SwingMode)this.swing.atx()).avX(Hand.MAIN_HAND);
         stackSlotHelper2.LP(n);
      }
   }

   public void isPearlVisible() {
      if (this.awg()) {
         ItemCooldownManager temp = client.player.getItemCooldownManager();
         Item item2 = Items.ENDER_PEARL;
         if (temp.isCoolingDown(item2)) {
            return;
         }

         if (((UnidentifiedHelper17)this.friend.atx()).c && ((UnidentifiedHelper17)this.pearl.atx()).agt() == ((UnidentifiedHelper17)this.friend.atx()).agt()) {
            return;
         }

         if (((UnidentifiedHelper17)this.firework.atx()).c
            && ((UnidentifiedHelper17)this.pearl.atx()).agt() == ((UnidentifiedHelper17)this.firework.atx()).agt()) {
            return;
         }

         if (((UnidentifiedHelper17)this.pearl.atx()).agt() == ((UnidentifiedHelper17)this.firework.atx()).agt()
            && client.player.isFallFlying()
            && new StackSlotHelper(Items.FIREWORK_ROCKET, (SwapMode)this.swapAction.atx()).LK().LL()) {
            return;
         }

         StackSlotHelper stackSlotHelper2 = new StackSlotHelper(Items.ENDER_PEARL, (SwapMode)this.swapAction.atx()).LK();
         if (!stackSlotHelper2.LL()) {
            return;
         }

         float f = client.player.getYaw();
         float f2 = client.player.getPitch();
         int n = Managers.inventoryTracker.JS();
         RotationUtils.nH(f, f2);
         stackSlotHelper2.LO(n);
         final float f3 = f2;
         final float f4 = f;
         Managers.serverManager.iQ(new SequencedPacketCreator() {
            public Packet predict(int n2) {
               return TweakMarco.GU(f4, f3, n2);
            }
         });
         ((SwingMode)this.swing.atx()).avX(Hand.MAIN_HAND);
         stackSlotHelper2.LP(n);
         RotationUtils.nK();
      }
   }

   public void isChestSwapVisible() {
      if (this.awg()) {
         Item item2 = client.player.getInventory().getArmorStack(2).getItem();
         if (item2 instanceof ArmorItem && ((ArmorItem)item2).getSlotType() == EquipmentSlot.CHEST) {
            int n = this.GP();
            if (n != -1) {
               InventoryTracker inventoryTracker2 = Managers.inventoryTracker;
               int n2;
               if (n < 9) {
                  InventoryTracker inventoryTracker3 = inventoryTracker2;
                  n2 = n + 36;
                  inventoryTracker2 = inventoryTracker3;
               } else {
                  InventoryTracker inventoryTracker4 = inventoryTracker2;
                  n2 = n;
                  inventoryTracker2 = inventoryTracker4;
               }

               inventoryTracker2.JM(n2);
               Managers.inventoryTracker.JM(6);
               inventoryTracker2 = Managers.inventoryTracker;
               if (n < 9) {
                  InventoryTracker inventoryTracker5 = inventoryTracker2;
                  n2 = n + 36;
                  inventoryTracker2 = inventoryTracker5;
               } else {
                  InventoryTracker inventoryTracker6 = inventoryTracker2;
                  n2 = n;
                  inventoryTracker2 = inventoryTracker6;
               }

               inventoryTracker2.JM(n2);
               if ((Boolean)this.withFirework.atx() && !client.player.isOnGround()) {
                  Managers.serverManager.iO('\uf560', new ClientCommandC2SPacket(client.player, Mode.START_FALL_FLYING));
                  client.player.startFallFlying();
                  int n3 = 0;
                  int n4;
                  if (client.player.getInventory().getStack(0).getItem() == Items.FIREWORK_ROCKET) {
                     n4 = 0;
                  } else {
                     label77: {
                        while (++n3 < 45) {
                           if (client.player.getInventory().getStack(n3).getItem() == Items.FIREWORK_ROCKET) {
                              n4 = n3;
                              if (n3 != -1) {
                                 if (n3 >= 9) {
                                    Managers.inventoryTracker.JK(n3, Managers.inventoryTracker.JS());
                                    Managers.serverManager.iQ(TweakMarco::GS);
                                    Managers.inventoryTracker.JK(n3, Managers.inventoryTracker.JS());
                                    return;
                                 }
                                 break label77;
                              }
                              break;
                           }
                        }

                        return;
                     }
                  }

                  n3 = Managers.inventoryTracker.JS();
                  Managers.inventoryTracker.JF(n4);
                  Managers.serverManager.iQ(TweakMarco::GT);
                  Managers.inventoryTracker.JF(n3);
               }
            }
         } else {
            int n5 = this.GO();
            if (n5 != -1) {
               InventoryTracker inventoryTracker7 = Managers.inventoryTracker;
               int n6;
               if (n5 < 9) {
                  InventoryTracker temp = inventoryTracker7;
                  n6 = n5 + 36;
                  inventoryTracker7 = temp;
               } else {
                  InventoryTracker inventoryTracker8 = inventoryTracker7;
                  n6 = n5;
                  inventoryTracker7 = inventoryTracker8;
               }

               inventoryTracker7.JM(n6);
               Managers.inventoryTracker.JM(6);
               inventoryTracker7 = Managers.inventoryTracker;
               if (n5 < 9) {
                  InventoryTracker inventoryTracker9 = inventoryTracker7;
                  n6 = n5 + 36;
                  inventoryTracker7 = inventoryTracker9;
               } else {
                  InventoryTracker inventoryTracker10 = inventoryTracker7;
                  n6 = n5;
                  inventoryTracker7 = inventoryTracker10;
               }

               inventoryTracker7.JM(n6);
            }
         }
      }
   }

   private int GO() {
      int n = -1;
      int n2 = 0;

      do {
         PlayerInventory temp = client.player.getInventory();
         int n3 = n2;
         Item item2 = temp.getStack(n3).getItem();
         if (item2 instanceof ArmorItem && ((ArmorItem)item2).getSlotType() == EquipmentSlot.CHEST) {
            if (((ArmorItem)item2).getMaterial() == ArmorMaterials.NETHERITE && this.priority.atx() == TweakMarcoOption.a_) {
               n = n2;
               break;
            }

            if (((ArmorItem)item2).getMaterial() == ArmorMaterials.DIAMOND && this.priority.atx() == TweakMarcoOption.b) {
               n = n2;
               break;
            }

            n = n2;
         }
      } while (++n2 < 36);

      return n;
   }

   private int GP() {
      int n = -1;
      int n2 = 0;

      do {
         PlayerInventory temp = client.player.getInventory();
         int n3 = n2;
         if (temp.getStack(n3).getItem() instanceof ElytraItem) {
            n = n2;
            break;
         }
      } while (++n2 < 36);

      return n;
   }

   private BlockPos GQ() {
      Vec3d vec = client.player.getEyePos();
      Vec3d vec2 = client.player.getRotationVec(1.0F);
      Vec3d temp = vec;
      Vec3d vec3 = vec2.multiply(4.5);
      Vec3d vec4 = temp.add(vec3);
      double d5 = 4.5;
      ClientWorld world2 = client.world;
      RaycastContext raycastContext2 = new RaycastContext(vec, vec4, ShapeType.OUTLINE, FluidHandling.NONE, client.player);
      BlockHitResult blockHitResult2 = world2.raycast(raycastContext2);
      if (blockHitResult2.getType() == Type.BLOCK) {
         temp = vec;
         Vec3d vec5 = blockHitResult2.getPos();
         d5 = temp.distanceTo(vec5);
      }

      BlockPos pos2 = null;
      double d6 = 0.0;

      while (true) {
         label96: {
            if (d6 <= d5) {
               temp = vec;
               Vec3d temp2 = vec2;
               double d7 = d6;
               Vec3d vec6 = temp2.multiply(d7);
               Vec3d vec7 = temp.add(vec6);
               BlockPos pos3 = new BlockPos((int)Math.floor(vec7.x), (int)Math.floor(vec7.y), (int)Math.floor(vec7.z));
               if (pos3.equals(null)) {
                  break label96;
               }

               ClientWorld world3 = client.world;
               BlockPos pos4 = pos3.up();
               if (world3.getBlockState(pos4).blocksMovement()
                  || !CollisionUtils.aiE(pos3, true, Blocks.ENDER_CHEST)
                  || !(Boolean)this.airPlace.atx() && PlacementUtils.Fk(pos3, 4.5) == null) {
                  break label96;
               }

               label76:
               while (true) {
                  pos2 = pos3;

                  while (true) {
                     d6 += 0.2;
                     if (!(d6 <= d5)) {
                        if (pos2 != null) {
                           return pos2;
                        }
                        break label76;
                     }

                     temp = vec;
                     Vec3d vec8 = vec2.multiply(d6);
                     vec7 = temp.add(vec8);
                     pos3 = new BlockPos((int)Math.floor(vec7.x), (int)Math.floor(vec7.y), (int)Math.floor(vec7.z));
                     if (!pos3.equals(pos2)) {
                        ClientWorld world4 = client.world;
                        BlockPos pos5 = pos3.up();
                        if (!world4.getBlockState(pos5).blocksMovement()
                           && CollisionUtils.aiE(pos3, true, Blocks.ENDER_CHEST)
                           && ((Boolean)this.airPlace.atx() || PlacementUtils.Fk(pos3, 4.5) != null)) {
                           break;
                        }
                     }
                  }
               }
            }

            if ((Boolean)this.throughWall.atx()) {
               for (double d8 = 0.0; d8 <= 4.5; d8 += 0.2) {
                  temp = vec;
                  Vec3d vec9 = vec2;
                  double d9 = d8;
                  Vec3d vec10 = vec9.multiply(d9);
                  Vec3d vec11 = temp.add(vec10);
                  BlockPos pos6 = new BlockPos((int)Math.floor(vec11.x), (int)Math.floor(vec11.y), (int)Math.floor(vec11.z));
                  ClientWorld world5 = client.world;
                  BlockPos pos7 = pos6.up();
                  if (!world5.getBlockState(pos7).blocksMovement()
                     && CollisionUtils.aiE(pos6, true, Blocks.SHULKER_BOX)
                     && ((Boolean)this.airPlace.atx() || PlacementUtils.Fk(pos6, 4.5) != null)) {
                     return pos6;
                  }
               }
            }

            return pos2;
         }

         d6 += 0.2;
      }
   }

   // restored with CFR (compiles, call/field/constant fingerprint equals the deobf jar)
   private BlockPos GR() {
       BlockPos blockPos;
       block7: {
           BlockPos blockPos2;
           Vec3d vec3d;
           double d;
           Vec3d vec3d2 = TweakMarco.client.player.getEyePos();
           Vec3d vec3d3 = TweakMarco.client.player.getRotationVec(1.0f);
           Vec3d vec3d4 = vec3d3.multiply(4.5);
           Vec3d vec3d5 = vec3d2.add(vec3d4);
           double d2 = 4.5;
           RaycastContext raycastContext = new RaycastContext(vec3d2, vec3d5, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity)TweakMarco.client.player);
           BlockHitResult blockHitResult = TweakMarco.client.world.raycast(raycastContext);
           if (blockHitResult.getType() == HitResult.Type.BLOCK) {
               Vec3d vec3d6 = blockHitResult.getPos();
               d2 = vec3d2.distanceTo(vec3d6);
           }
           blockPos = null;
           for (d = 0.0; d <= d2; d += 0.2) {
               double d3 = d;
               Vec3d vec3d7 = vec3d3.multiply(d3);
               vec3d = vec3d2.add(vec3d7);
               blockPos2 = new BlockPos((int)Math.floor(vec3d.x), (int)Math.floor(vec3d.y), (int)Math.floor(vec3d.z));
               if (blockPos2.equals(null) || !CollisionUtils.aiE(blockPos2, true, Blocks.SHULKER_BOX) || !((Boolean)this.airPlace.atx()).booleanValue() && TweakMarco.GJ(blockPos2) == null) {
                   continue;
               }
               block1: while (true) {
                   blockPos = blockPos2;
                   while ((d += 0.2) <= d2) {
                       Vec3d vec3d8 = vec3d3.multiply(d);
                       vec3d = vec3d2.add(vec3d8);
                       blockPos2 = new BlockPos((int)Math.floor(vec3d.x), (int)Math.floor(vec3d.y), (int)Math.floor(vec3d.z));
                       if (blockPos2.equals((Object)blockPos) || !CollisionUtils.aiE(blockPos2, true, Blocks.SHULKER_BOX) || !((Boolean)this.airPlace.atx()).booleanValue() && TweakMarco.GJ(blockPos2) == null) continue;
                       continue block1;
                   }
                   break;
               }
               if (blockPos != null) break block7;
           }
           if (((Boolean)this.throughWall.atx()).booleanValue()) {
               for (d = 0.0; d <= 4.5; d += 0.2) {
                   double d4 = d;
                   Vec3d vec3d9 = vec3d3.multiply(d4);
                   vec3d = vec3d2.add(vec3d9);
                   blockPos2 = new BlockPos((int)Math.floor(vec3d.x), (int)Math.floor(vec3d.y), (int)Math.floor(vec3d.z));
                   if (!CollisionUtils.aiE(blockPos2, true, Blocks.SHULKER_BOX) || !((Boolean)this.airPlace.atx()).booleanValue() && TweakMarco.GJ(blockPos2) == null) continue;
                   return blockPos2;
               }
           }
       }
       return blockPos;
   }

   private static Packet GS(int n) {
      return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, client.player.getYaw(), client.player.getPitch());
   }

   private static Packet GT(int n) {
      return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, client.player.getYaw(), client.player.getPitch());
   }

   static Packet GU(float f, float f2, int n) {
      return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, f, f2);
   }

   private static Packet GV(int n) {
      return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, client.player.getYaw(), client.player.getPitch());
   }

   static boolean GW(ItemStack stack) {
      Item item2 = stack.getItem();
      boolean flag;
      if (item2 instanceof BlockItem && ((BlockItem)item2).getBlock() instanceof ShulkerBoxBlock) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static boolean GX(ItemStack stack) {
      Item item2 = stack.getItem();
      boolean flag;
      if (item2 instanceof BlockItem && ((BlockItem)item2).getBlock() instanceof EnderChestBlock) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static UnidentifiedHelper17 a(String str, Runnable runnable) {
      return new UnidentifiedHelper17(str, -1, runnable);
   }
}
