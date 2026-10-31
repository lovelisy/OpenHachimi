package pw.hachimi.client.utils.world;

import java.util.Iterator;
import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import pw.hachimi.client.api.UnidentifiedInterface;
import pw.hachimi.client.config.ConfigContainer;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.world.BlockPosEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.NoneNormalMode;
import pw.hachimi.client.model.network.DisablerNewDisablerMode;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.player.SwingMode;
import pw.hachimi.client.module.combat.AutoAnchor;
import pw.hachimi.client.module.combat.MainHand;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.module.player.BlockPosBoxMultitask;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.player.StackPlayerUtils;
import pw.hachimi.eventbus.EventBusImpl;

public class BlockPosPlayerHelper {
   public BlockPos pos;
   public float b;
   public float c;
   public float d;
   public boolean e;
   public boolean f;
   public ItemStack stack;
   public int h;
   public int i;
   public int j;
   public BlockState state;
   public int l;
   public int m;
   public int n;
   public boolean o;
   public double p;
   final BlockPosBoxMultitask blockPosBoxMultitask;
   public static boolean a;

   public BlockPosPlayerHelper(BlockPosBoxMultitask blockPosBoxMultitask2, BlockPos pos2, int n2, float f2) {
      this.blockPosBoxMultitask = blockPosBoxMultitask2;
      this.pos = pos2;
      this.h = n2;
      this.b = f2;
   }

   public void xq() {
      if (!Blink.bm()) {
         label99: {
            if ((Boolean)this.blockPosBoxMultitask.booleanSetting17.atx() && !ConfigContainer.client.player.isOnGround()) {
               ClientWorld temp = ConfigContainer.client.world;
               BlockPos pos2 = ConfigContainer.client.player.getBlockPos();
               if (!(temp.getBlockState(pos2).getBlock() instanceof LadderBlock)) {
                  break label99;
               }
            }

            if ((!(Boolean)this.blockPosBoxMultitask.booleanSetting18.atx() || !ConfigContainer.client.player.isFallFlying())
               && (!(Boolean)this.blockPosBoxMultitask.booleanSetting19.atx() || !ConfigContainer.client.player.isInFluid())
               && ((Boolean)BlockPosBoxMultitask.alR(this.blockPosBoxMultitask).atx() || !this.blockPosBoxMultitask.RG())
               && !this.blockPosBoxMultitask.k) {
               if (!this.f && !this.blockPosBoxMultitask.alP()) {
                  this.xr();
               }

               if (this.f) {
                  this.xs();
               }

               if (this.d >= 1.0F && !BlockUtils.TP(this.pos)) {
                  BlockPos pos3 = this.pos;
                  label61:
                  if (!ConfigContainer.client.world.isAir(pos3)) {
                     label69: {
                        if (AutoAnchor.aaj().aax() != null) {
                           BlockPos pos4 = this.pos;
                           BlockPos pos5 = AutoAnchor.aaj().aax();
                           if (!pos4.equals(pos5)) {
                              break label69;
                           }
                        }

                        if (AutoAnchor.aaj().aax() != null) {
                           break label61;
                        }
                     }

                     this.xt();
                  }
               }

               this.n++;
               return;
            }
         }

         if (!this.f && !this.blockPosBoxMultitask.alP() && !(Boolean)this.blockPosBoxMultitask.booleanSetting16.atx()) {
            this.xr();
         }

         if (this.f) {
            this.xs();
         }
      } else if (this.f) {
         this.xs();
      }
   }

   public void xr() {
      BlockPos pos2 = this.pos;
      this.state = ConfigContainer.client.world.getBlockState(pos2);
      Direction direction = PlacementUtils.Fi(this.pos, (Double)this.blockPosBoxMultitask.numberSetting2.atx());
      if (direction != null) {
         if (this.state.isAir()) {
            if (!(Boolean)this.blockPosBoxMultitask.booleanSetting11.atx()) {
               return;
            }

            this.o = true;
         }

         if (!BlockUtils.TQ(this.state)) {
            if ((Boolean)this.blockPosBoxMultitask.booleanSetting9.atx()) {
               RotationUtils.nJ(this.pos, direction);
            }

            if (this.blockPosBoxMultitask.disablerNewDisablerMode.atx() == DisablerNewDisablerMode.Disabler) {
               final Direction direction2 = direction;
               final BlockPosPlayerHelper blockPosPlayerHelper3 = this;
               Managers.serverManager.iQ(new SequencedPacketCreator() {
                  public Packet predict(int n2) {
                     return blockPosPlayerHelper3.xG(direction2, n2);
                  }
               });
            }

            UnidentifiedInterface unidentifiedInterface = ((SwapMode)this.blockPosBoxMultitask.swapMode.atx()).er();
            int n3 = Managers.inventoryTracker.JS();
            final Direction direction3 = direction;
            final BlockPosPlayerHelper blockPosPlayerHelper4 = this;
            Managers.serverManager.iQ(new SequencedPacketCreator() {
               public Packet predict(int n2) {
                  return blockPosPlayerHelper4.xF(direction3, n2);
               }
            });
            if (this.blockPosBoxMultitask.disablerNewDisablerMode.atx() == DisablerNewDisablerMode.NewDisabler) {
               Managers.serverManager.iQ(BlockPosPlayerHelper::xE);
            }

            if ((Boolean)this.blockPosBoxMultitask.booleanSetting7.atx()) {
               final Direction direction4 = direction;
               final BlockPosPlayerHelper blockPosPlayerHelper5 = this;
               Managers.serverManager.iQ(new SequencedPacketCreator() {
                  public Packet predict(int n2) {
                     return blockPosPlayerHelper5.xD(direction4, n2);
                  }
               });
               if (this.blockPosBoxMultitask.blockPosPlayerHelper == null || this.blockPosBoxMultitask.blockPosPlayerHelper.e) {
                  this.blockPosBoxMultitask.blockPosPlayerHelper = new BlockPosPlayerHelper(this.blockPosBoxMultitask, this.pos, this.h, 1.0F);
                  this.blockPosBoxMultitask.blockPosPlayerHelper.f = true;
               }
            }

            if (((DisablerNewDisablerMode)this.blockPosBoxMultitask.disablerNewDisablerMode.atx()).FX() && this.i != -1) {
               unidentifiedInterface.ba(this.i, n3);
               ((SwingMode)this.blockPosBoxMultitask.swingMode.atx()).avX(Hand.MAIN_HAND);
               unidentifiedInterface.bb(this.i, n3);
            } else {
               ((SwingMode)this.blockPosBoxMultitask.swingMode.atx()).avX(Hand.MAIN_HAND);
            }

            this.f = true;
         }
      }
   }

   public void xs() {
      this.l++;
      this.c = this.d;
      this.stack = null;
      BlockPos pos2 = this.pos;
      this.state = ConfigContainer.client.world.getBlockState(pos2);
      if (this.state.isAir()) {
         this.m = 0;
         if (!this.o) {
            if (!(Boolean)this.blockPosBoxMultitask.booleanSetting6.atx()) {
               this.f = false;
               this.d = 0.0F;
               this.c = 0.0F;
               this.l = 0;
            }

            this.e = true;
         }
      }

      int n2;
      if ((n2 = UnidentifiedHelper25.a[((SwapMode)this.blockPosBoxMultitask.swapMode.atx()).ordinal()]) != 1) {
         if (n2 != 2) {
            if (n2 == 3) {
               this.i = this.xw(this.state);
            }
         } else {
            this.i = this.xv(this.state);
         }
      } else {
         this.i = this.xx(this.state);
      }

      this.j = ConfigContainer.client.player.currentScreenHandler.syncId;
      if (this.stack == null) {
         this.stack = this.blockPosBoxMultitask.alL();
      }

      if (this.f && !this.state.isAir() && !BlockUtils.TQ(this.state)) {
         float f2 = StackPlayerUtils.oQ(
               ConfigContainer.client.player,
               this.stack,
               this.state,
               ConfigContainer.client.player.isOnGround() || !(Boolean)this.blockPosBoxMultitask.booleanSetting12.atx(),
               ConfigContainer.client.player.isFallFlying() && (Boolean)this.blockPosBoxMultitask.booleanSetting13.atx(),
               ConfigContainer.client.player.isSubmergedInWater()
            )
            / this.b;
         this.d += f2;
         this.d = Math.min(1.0F, this.d);
         double d2 = Math.max(
            this.p,
            StackPlayerUtils.oQ(
               ConfigContainer.client.player,
               this.stack,
               this.state,
               ConfigContainer.client.player.isOnGround(),
               ConfigContainer.client.player.isFallFlying(),
               ConfigContainer.client.player.isSubmergedInWater()
            )
         );
         if (d2 > this.p) {
            this.p = d2;
            if (((DisablerNewDisablerMode)this.blockPosBoxMultitask.disablerNewDisablerMode.atx()).FX()) {
               ((SwingMode)this.blockPosBoxMultitask.swingMode.atx()).avX(Hand.MAIN_HAND);
            }
         }

         float f3 = StackPlayerUtils.oQ(ConfigContainer.client.player, this.stack, this.state, true, false, false) / this.b * this.l;
         if (f3 < this.d || this.o) {
            this.d = f3;
            this.o = false;
         }
      }
   }

   public void xt() {
      BlockPos pos2 = this.pos;
      if (this.blockPosBoxMultitask.alM(ConfigContainer.client.world.getBlockState(pos2).getBlock())) {
         if (((DisablerNewDisablerMode)this.blockPosBoxMultitask.disablerNewDisablerMode.atx()).FX()) {
            double d2 = Math.ceil(1.0 / this.p) * 50.0;
            double d3 = System.currentTimeMillis() - this.blockPosBoxMultitask.j;
            if (d2 - d3 >= 25.0) {
               return;
            }
         }

         Direction direction = PlacementUtils.Fi(this.pos, (Double)this.blockPosBoxMultitask.numberSetting2.atx());
         if (direction != null) {
            BlockPosEvent blockPosEvent = (BlockPosEvent)new BlockPosEvent(this.pos, direction).aqo(EventPhase.PRE);
            EventBusImpl.INSTANCE.rC(blockPosEvent);
            if (!blockPosEvent.aik()) {
               int n2 = this.i;
               if (n2 == -1) {
                  if ((Boolean)this.blockPosBoxMultitask.booleanSetting10.atx()) {
                     RotationUtils.nJ(this.pos, direction);
                  }

                  this.m++;
                  final Direction direction2 = direction;
                  final BlockPosPlayerHelper blockPosPlayerHelper3 = this;
                  Managers.serverManager.iQ(new SequencedPacketCreator() {
                     public Packet predict(int n3) {
                        return blockPosPlayerHelper3.xz(direction2, n3);
                     }
                  });
                  ((SwingMode)this.blockPosBoxMultitask.swingMode.atx()).avX(Hand.MAIN_HAND);
                  EventBusImpl.INSTANCE.rC(new BlockPosEvent(this.pos, direction).aqo(EventPhase.POST));
               } else if (!this.blockPosBoxMultitask.alL().equals(this.stack)) {
                  if (this.blockPosBoxMultitask.runnable != null) {
                     this.blockPosBoxMultitask.d = true;
                     this.blockPosBoxMultitask.runnable.run();
                     this.blockPosBoxMultitask.d = false;
                  }

                  UnidentifiedInterface unidentifiedInterface = ((SwapMode)this.blockPosBoxMultitask.swapMode.atx()).er();
                  int n4 = Managers.inventoryTracker.JS();
                  if (!(Boolean)this.blockPosBoxMultitask.booleanSetting4.atx()) {
                     this.n = 0;
                     unidentifiedInterface.ba(n2, n4);
                     if ((Boolean)this.blockPosBoxMultitask.booleanSetting10.atx()) {
                        RotationUtils.nJ(this.pos, direction);
                     }

                     this.m++;
                     final Direction direction3 = direction;
                     final BlockPosPlayerHelper blockPosPlayerHelper4 = this;
                     Managers.serverManager.iQ(new SequencedPacketCreator() {
                        public Packet predict(int n3) {
                           return blockPosPlayerHelper4.xA(direction3, n3);
                        }
                     });
                     unidentifiedInterface.bb(n2, n4);
                     ((SwingMode)this.blockPosBoxMultitask.swingMode.atx()).avX(Hand.MAIN_HAND);
                     BlockPosBoxMultitask.stopwatchImpl.afr(29432, 5265492, (byte)40);
                     BlockPos pos3 = this.pos;
                     BlockPosBoxMultitask.list.add(pos3);
                     EventBusImpl.INSTANCE.rC(new BlockPosEvent(this.pos, direction).aqo(EventPhase.POST));
                  } else if (!(Boolean)this.blockPosBoxMultitask.booleanSetting5.atx() && !MainHand.hz().hB()) {
                     unidentifiedInterface.ba(n2, n4);
                     int n5 = this.j;
                     this.n = 0;
                     final int n6 = n4;
                     final int n7 = n2;
                     final UnidentifiedInterface unidentifiedInterface2 = unidentifiedInterface;
                     final int n8 = n5;
                     final BlockPosPlayerHelper blockPosPlayerHelper5 = this;
                     this.blockPosBoxMultitask.runnable = new Runnable() {
                        @Override
                        public void run() {
                           blockPosPlayerHelper5.xB(n8, unidentifiedInterface2, n7, n6);
                        }
                     };
                     this.blockPosBoxMultitask.e = false;
                  }
               } else {
                  this.n = 0;
                  if ((Boolean)this.blockPosBoxMultitask.booleanSetting10.atx()) {
                     RotationUtils.nJ(this.pos, direction);
                  }

                  this.m++;
                  if (((DisablerNewDisablerMode)this.blockPosBoxMultitask.disablerNewDisablerMode.atx()).FX()) {
                     ((SwingMode)this.blockPosBoxMultitask.swingMode.atx()).avX(Hand.MAIN_HAND);
                  }

                  final Direction direction4 = direction;
                  final BlockPosPlayerHelper blockPosPlayerHelper6 = this;
                  Managers.serverManager.iQ(new SequencedPacketCreator() {
                     public Packet predict(int n3) {
                        return blockPosPlayerHelper6.xC(direction4, n3);
                     }
                  });
                  ((SwingMode)this.blockPosBoxMultitask.swingMode.atx()).avX(Hand.MAIN_HAND);
                  BlockPosBoxMultitask.stopwatchImpl.afr(29432, 5265492, (byte)40);
                  BlockPos pos4 = this.pos;
                  BlockPosBoxMultitask.list.add(pos4);
                  if (!this.blockPosBoxMultitask.e && this.blockPosBoxMultitask.runnable != null) {
                     this.blockPosBoxMultitask.runnable.run();
                  }

                  EventBusImpl.INSTANCE.rC(new BlockPosEvent(this.pos, direction).aqo(EventPhase.POST));
               }

               if (this.m > (Integer)this.blockPosBoxMultitask.numberSetting5.atx() && (!(Boolean)this.blockPosBoxMultitask.booleanSetting6.atx() || !this.e)) {
                  this.f = false;
                  this.l = 0;
                  this.m = 0;
                  this.d = 0.0F;
                  this.c = 0.0F;
               }
            } else if (!this.blockPosBoxMultitask.e && this.blockPosBoxMultitask.runnable != null) {
               this.blockPosBoxMultitask.d = true;
               this.blockPosBoxMultitask.runnable.run();
               this.blockPosBoxMultitask.d = false;
            }
         }
      }
   }

   public boolean xu() {
      if (this.e) {
         return true;
      }

      if (this.d >= 1.0F) {
         if (this.i == -1 || this.blockPosBoxMultitask.alL().equals(this.stack)) {
            this.m++;
            return this.m > (Integer)this.blockPosBoxMultitask.numberSetting6.atx();
         }

         if ((Boolean)this.blockPosBoxMultitask.booleanSetting8.atx() && this.blockPosBoxMultitask.runnable == null) {
            if ((Boolean)this.blockPosBoxMultitask.booleanSetting17.atx() && !ConfigContainer.client.player.isOnGround()) {
               ClientWorld temp = ConfigContainer.client.world;
               BlockPos pos2 = ConfigContainer.client.player.getBlockPos();
               if (!(temp.getBlockState(pos2).getBlock() instanceof LadderBlock)) {
                  return false;
               }
            }

            if ((!(Boolean)this.blockPosBoxMultitask.booleanSetting18.atx() || !ConfigContainer.client.player.isFallFlying())
               && (!(Boolean)this.blockPosBoxMultitask.booleanSetting19.atx() || !ConfigContainer.client.player.isInFluid())
               && !ConfigContainer.client.player.isUsingItem()
               && !Blink.bm()
               && !this.blockPosBoxMultitask.k
               && !MainHand.hz().hB()) {
               UnidentifiedInterface unidentifiedInterface = ((SwapMode)this.blockPosBoxMultitask.swapMode.atx()).er();
               int n2 = Managers.inventoryTracker.JS();
               int n3 = this.i;
               unidentifiedInterface.ba(n3, n2);
               int n4 = this.j;
               final int n5 = n2;
               final int n6 = n3;
               final UnidentifiedInterface unidentifiedInterface2 = unidentifiedInterface;
               final int n7 = n4;
               final BlockPosPlayerHelper blockPosPlayerHelper3 = this;
               this.blockPosBoxMultitask.runnable = new Runnable() {
                  @Override
                  public void run() {
                     blockPosPlayerHelper3.xy(n7, unidentifiedInterface2, n6, n5);
                  }
               };
               this.blockPosBoxMultitask.e = true;
               this.m++;
               return this.m > (Integer)this.blockPosBoxMultitask.numberSetting6.atx();
            }

            return false;
         }
      }

      this.m = 0;
      return false;
   }

   public int xv(BlockState state2) {
      int n2 = -1;
      float f2 = 1.0F;
      int n3 = 0;

      do {
         PlayerInventory temp = ConfigContainer.client.player.getInventory();
         int n4 = n3;
         ItemStack stack2 = temp.getStack(n4);
         float f3 = StackPlayerUtils.oR(state2, stack2);
         if (f3 > f2) {
            this.stack = stack2;
            f2 = f3;
            n2 = n3;
         }
      } while (++n3 < 9);

      return n2;
   }

   public int xw(BlockState state2) {
      int n2 = -1;
      float f2 = 1.0F;
      Iterator iterator2 = ConfigContainer.client.player.currentScreenHandler.slots.iterator();

      while (iterator2.hasNext()) {
         Slot slot2 = (Slot)iterator2.next();
         ItemStack stack2 = slot2.getStack();
         float f3 = StackPlayerUtils.oR(state2, stack2);
         if (f3 > f2) {
            this.stack = stack2;
            f2 = f3;
            n2 = slot2.id;
         }
      }

      return n2;
   }

   public int xx(BlockState state2) {
      int n2 = -1;
      float f2 = 1.0F;
      int n3 = 0;

      do {
         PlayerInventory temp = ConfigContainer.client.player.getInventory();
         int n4 = n3;
         ItemStack stack2 = temp.getStack(n4);
         float f3 = StackPlayerUtils.oR(state2, stack2);
         if (f3 > f2) {
            this.stack = stack2;
            f2 = f3;
            n2 = n3;
         }
      } while (++n3 < 36);

      return n2;
   }

   void xy(int n2, UnidentifiedInterface unidentifiedInterface, int n3, int n4) {
      if ((Blink.bm() || this.blockPosBoxMultitask.d || this.e || MainHand.hz().hB())
         && (this.blockPosBoxMultitask.swapMode.atx() != SwapMode.Screen || n2 == ConfigContainer.client.player.currentScreenHandler.syncId)) {
         unidentifiedInterface.bb(n3, n4);
         this.blockPosBoxMultitask.runnable = null;
      }
   }

   Packet xz(Direction direction, int n2) {
      if (((NoneNormalMode)this.blockPosBoxMultitask.noneNormalMode.atx()).cU()) {
         BlockPos pos2 = this.pos;
         ConfigContainer.client.interactionManager.breakBlock(pos2);
      }

      if (((NoneNormalMode)this.blockPosBoxMultitask.noneNormalMode.atx()).cV()) {
         BlockPos pos3 = this.pos;
         ConfigContainer.client.world.removeBlock(pos3, false);
      }

      return new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, this.pos, direction, n2);
   }

   Packet xA(Direction direction, int n2) {
      if (((NoneNormalMode)this.blockPosBoxMultitask.noneNormalMode.atx()).cU()) {
         BlockPos pos2 = this.pos;
         ConfigContainer.client.interactionManager.breakBlock(pos2);
      }

      if (((NoneNormalMode)this.blockPosBoxMultitask.noneNormalMode.atx()).cV()) {
         BlockPos pos3 = this.pos;
         ConfigContainer.client.world.removeBlock(pos3, false);
      }

      return new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, this.pos, direction, n2);
   }

   void xB(int n2, UnidentifiedInterface unidentifiedInterface, int n3, int n4) {
      if (this.blockPosBoxMultitask.blockPosPlayerHelper2 != null && !Blink.bm() && !MainHand.hz().hB() && !this.blockPosBoxMultitask.k) {
         BlockPos pos2 = this.pos;
         if ((!ConfigContainer.client.world.isAir(pos2) || this.n < (Integer)this.blockPosBoxMultitask.numberSetting4.atx())
            && !BlockUtils.TP(this.pos)
            && !this.blockPosBoxMultitask.d
            && PlacementUtils.Fi(this.pos, (Double)this.blockPosBoxMultitask.numberSetting2.atx()) != null) {
            return;
         }
      }

      if (this.blockPosBoxMultitask.swapMode.atx() != SwapMode.Screen || n2 == ConfigContainer.client.player.currentScreenHandler.syncId) {
         unidentifiedInterface.bb(n3, n4);
         this.blockPosBoxMultitask.runnable = null;
      }
   }

   Packet xC(Direction direction, int n2) {
      if (((NoneNormalMode)this.blockPosBoxMultitask.noneNormalMode.atx()).cU()) {
         BlockPos pos2 = this.pos;
         ConfigContainer.client.interactionManager.breakBlock(pos2);
      }

      if (((NoneNormalMode)this.blockPosBoxMultitask.noneNormalMode.atx()).cV()) {
         BlockPos pos3 = this.pos;
         ConfigContainer.client.world.removeBlock(pos3, false);
      }

      return new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, this.pos, direction, n2);
   }

   Packet xD(Direction direction, int n2) {
      return new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, this.pos, direction, n2);
   }

   private static Packet xE(int n2) {
      return new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, a(), Direction.UP, n2);
   }

   Packet xF(Direction direction, int n2) {
      return new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, this.pos, direction, n2);
   }

   Packet xG(Direction direction, int n2) {
      return new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, this.pos, direction, n2);
   }

   private static BlockPos a() {
      return new BlockPos(0, -999, 0);
   }
}
