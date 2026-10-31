package pw.hachimi.client.utils.world;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import pw.hachimi.client.Mc;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.module.player.AirPlace;
import pw.hachimi.client.module.render.PlaceEcho;
import pw.hachimi.client.utils.player.StackSlotHelper;

public class BlockPosBlockItemHelper {
   public boolean a;
   public BlockPos pos;
   public Item item;
   public List list;
   public Predicate predicate;
   public boolean f;
   public SwapMode swapMode;
   public boolean h;
   public BlockItem blockItem;
   public StackSlotHelper stackSlotHelper;
   public Direction direction;
   public Direction direction2;
   public boolean m;
   public boolean n;
   public boolean o;
   public boolean p;
   private static final long b = 0L;

   public BlockPosBlockItemHelper(BlockPos pos2, Item item2, boolean flag, SwapMode swapMode2) {
      this.a = PlacementUtils.p;
      this.pos = pos2;
      this.item = item2;
      this.f = flag;
      this.swapMode = swapMode2;
   }

   public BlockPosBlockItemHelper(BlockPos pos2, List list2, boolean flag, SwapMode swapMode2) {
      this.a = PlacementUtils.p;
      this.pos = pos2;
      this.list = list2;
      this.f = flag;
      this.swapMode = swapMode2;
   }

   public BlockPosBlockItemHelper(BlockPos pos2, Predicate predicate2, boolean flag, SwapMode swapMode2) {
      this.a = PlacementUtils.p;
      this.pos = pos2;
      this.predicate = predicate2;
      this.f = flag;
      this.swapMode = swapMode2;
   }

   public boolean avJ() {
      boolean flag;
      if (!this.m || !this.n || !this.h && !this.o || !this.stackSlotHelper.LL() || this.direction2 == null && this.direction == null) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public boolean avK(double d, boolean flag) {
      this.h = flag && Managers.antiCheatManager.Uc();
      this.m = CollisionUtils.aiE(this.pos, false, null);
      this.direction2 = PlacementUtils.Fk(this.pos, d);
      this.direction = !this.f && !AirPlace.acT().awg() ? null : PlacementUtils.Fi(this.pos, d);
      if (this.predicate != null) {
         this.stackSlotHelper = new StackSlotHelper(this.predicate, this.swapMode).LK();
         BlockItem blockItem2;
         if (this.stackSlotHelper.stack != null && this.stackSlotHelper.stack.getItem() instanceof BlockItem) {
            blockItem2 = (BlockItem)this.stackSlotHelper.stack.getItem();
         } else {
            blockItem2 = (BlockItem)Items.OBSIDIAN;
         }

         this.blockItem = blockItem2;
         this.n = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), false);
         this.o = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), true);
      } else if (this.list != null) {
         Iterator iterator2 = this.list.iterator();

         while (iterator2.hasNext()) {
            Item item2 = ((Block)iterator2.next()).asItem();
            this.stackSlotHelper = new StackSlotHelper(item2, this.swapMode).LK();
            if (this.stackSlotHelper.LL()) {
               BlockItem blockItem3;
               if (item2 instanceof BlockItem) {
                  blockItem3 = (BlockItem)item2;
               } else {
                  blockItem3 = (BlockItem)Items.OBSIDIAN;
               }

               this.blockItem = blockItem3;
               this.n = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), false);
               this.o = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), true);
               break;
            }
         }

         if (!this.stackSlotHelper.LL()) {
            this.blockItem = (BlockItem)Items.OBSIDIAN;
            this.n = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), false);
            this.o = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), true);
         }
      } else {
         this.stackSlotHelper = new StackSlotHelper(this.item, this.swapMode).LK();
         BlockItem blockItem4;
         if (this.item instanceof BlockItem) {
            blockItem4 = (BlockItem)this.item;
         } else {
            blockItem4 = (BlockItem)Items.OBSIDIAN;
         }

         this.blockItem = blockItem4;
         this.n = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), false);
         this.o = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), true);
      }

      return this.m && this.n && (this.h || this.o) && this.stackSlotHelper.LL() && (this.direction2 != null || this.direction != null);
   }

   public boolean avL(double d, boolean flag) {
      boolean flag2;
      if (flag && Managers.antiCheatManager.Uc()) {
         flag2 = true;
      } else {
         flag2 = false;
      }

      this.h = flag2;
      this.m = true;
      this.direction2 = PlacementUtils.Fj(this.pos, d);
      Direction direction3;
      if (!this.f && !AirPlace.acT().awg()) {
         direction3 = null;
      } else {
         direction3 = PlacementUtils.Fi(this.pos, d);
      }

      this.direction = direction3;
      if (this.predicate != null) {
         this.stackSlotHelper = new StackSlotHelper(this.predicate, this.swapMode).LK();
         Item item2 = this.item;
         BlockItem blockItem2;
         if (item2 instanceof BlockItem) {
            blockItem2 = (BlockItem)item2;
         } else {
            blockItem2 = (BlockItem)Items.OBSIDIAN;
         }

         this.blockItem = blockItem2;
         this.n = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), false);
         this.o = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), true);
      } else if (this.list == null) {
         this.stackSlotHelper = new StackSlotHelper(this.item, this.swapMode).LK();
         this.blockItem = (BlockItem)Items.OBSIDIAN;
         this.n = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), false);
         this.o = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), true);
      } else {
         Iterator iterator2 = this.list.iterator();

         while (iterator2.hasNext()) {
            Item item3 = ((Block)iterator2.next()).asItem();
            this.stackSlotHelper = new StackSlotHelper(item3, this.swapMode).LK();
            if (this.stackSlotHelper.LL()) {
               BlockItem blockItem3;
               if (item3 instanceof BlockItem) {
                  blockItem3 = (BlockItem)item3;
               } else {
                  blockItem3 = (BlockItem)Items.OBSIDIAN;
               }

               this.blockItem = blockItem3;
               this.n = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), false);
               this.o = this.avQ(this.blockItem.getBlock().getDefaultState(), this.pos, ShapeContext.absent(), true);
               break;
            }
         }

         if (!this.stackSlotHelper.LL()) {
            this.n = this.avQ(Blocks.OBSIDIAN.getDefaultState(), this.pos, ShapeContext.absent(), false);
            this.o = this.avQ(Blocks.OBSIDIAN.getDefaultState(), this.pos, ShapeContext.absent(), true);
         }
      }

      if (!this.n || !this.h && !this.o || !this.stackSlotHelper.LL() || this.direction2 == null && this.direction == null) {
         flag2 = false;
      } else {
         flag2 = true;
      }

      return flag2;
   }

   public boolean avM(double d, boolean flag, boolean flag2, double d2) {
      if (!this.avK(d, flag2)) {
         return false;
      }

      if (!this.o) {
         BlockState temp = this.blockItem.getBlock().getDefaultState();
         ClientWorld temp2 = Mc.client.world;
         BlockPos temp3 = this.pos;
         ShapeContext shapeContext = ShapeContext.absent();
         BlockPos pos2 = temp3;
         ClientWorld world2 = temp2;
         VoxelShape voxelShape = temp.getCollisionShape((BlockView)world2, pos2, shapeContext);
         VoxelShape voxelShape2 = voxelShape;
         double temp4 = this.pos.getX();
         double d3 = this.pos.getZ();
         double d4 = this.pos.getY();
         double d5 = temp4;
         Entity entity2 = this.avR(voxelShape2.offset(d5, d4, d3), true);
         if (entity2 != null && (!flag2 || !PlacementUtils.Fp(entity2, d2, flag))) {
            return false;
         }
      }

      int n2 = Managers.inventoryTracker.JS();
      this.stackSlotHelper.LO(n2);
      if (this.direction2 != null) {
         PlacementUtils.Fd(this.pos, this.direction2, Hand.MAIN_HAND, this.a, flag);
      } else if (this.direction != null) {
         PlacementUtils.Fc(this.pos, this.direction, Hand.MAIN_HAND, this.a, flag);
      }

      this.stackSlotHelper.LP(n2);
      this.avP();
      return true;
   }

   public void avN(boolean flag, boolean flag2, double d) {
      if ((this.direction2 != null || this.direction != null) && this.stackSlotHelper != null && this.stackSlotHelper.LL()) {
         if (!this.o) {
            BlockState temp = this.blockItem.getBlock().getDefaultState();
            ClientWorld temp2 = Mc.client.world;
            BlockPos temp3 = this.pos;
            ShapeContext shapeContext = ShapeContext.absent();
            BlockPos pos2 = temp3;
            ClientWorld world2 = temp2;
            VoxelShape voxelShape = temp.getCollisionShape((BlockView)world2, pos2, shapeContext);
            VoxelShape voxelShape2 = voxelShape;
            double temp4 = this.pos.getX();
            double d2 = this.pos.getZ();
            double d3 = this.pos.getY();
            double d4 = temp4;
            Entity entity2 = this.avR(voxelShape2.offset(d4, d3, d2), true);
            if (entity2 != null && (!flag2 || !PlacementUtils.Fp(entity2, d, flag))) {
               return;
            }
         }

         int n2 = Managers.inventoryTracker.JS();
         this.stackSlotHelper.LO(n2);
         if (this.direction2 != null) {
            PlacementUtils.Fd(this.pos, this.direction2, Hand.MAIN_HAND, this.a, flag);
         } else if (this.direction != null) {
            PlacementUtils.Fc(this.pos, this.direction, Hand.MAIN_HAND, this.a, flag);
         }

         this.stackSlotHelper.LP(n2);
         this.avP();
      }
   }

   public boolean avO(double d, boolean flag, boolean flag2, double d2) {
      if (!this.avL(d, flag2)) {
         return false;
      }

      if (!this.o) {
         BlockState temp = this.blockItem.getBlock().getDefaultState();
         ClientWorld temp2 = Mc.client.world;
         BlockPos temp3 = this.pos;
         ShapeContext shapeContext = ShapeContext.absent();
         BlockPos pos2 = temp3;
         ClientWorld world2 = temp2;
         VoxelShape voxelShape = temp.getCollisionShape((BlockView)world2, pos2, shapeContext);
         VoxelShape voxelShape2 = voxelShape;
         double temp4 = this.pos.getX();
         double d3 = this.pos.getZ();
         double d4 = this.pos.getY();
         double d5 = temp4;
         Entity entity2 = this.avR(voxelShape2.offset(d5, d4, d3), true);
         if (entity2 != null && (!flag2 || !PlacementUtils.Fp(entity2, d2, flag))) {
            return false;
         }
      }

      int n2 = Managers.inventoryTracker.JS();
      this.stackSlotHelper.LO(n2);
      if (this.direction2 != null) {
         PlacementUtils.Fd(this.pos, this.direction2, Hand.MAIN_HAND, this.a, flag);
      } else if (this.direction != null) {
         PlacementUtils.Fc(this.pos, this.direction, Hand.MAIN_HAND, this.a, flag);
      }

      this.stackSlotHelper.LP(n2);
      this.avP();
      return true;
   }

   public void avP() {
      if (!PlacementUtils.Fm(this.blockItem.getBlock().getDefaultState(), this.pos)) {
         BlockPos pos2 = this.pos;
         PlacementUtils.a_.add(pos2);
      }

      if (!this.p) {
         PlaceEcho.Mz().MB(this.pos);
      }
   }

   boolean avQ(BlockState state, BlockPos pos2, ShapeContext shapeContext, boolean flag) {
      if (flag) {
      }

      ClientWorld world2 = Mc.client.world;
      VoxelShape voxelShape = state.getCollisionShape((BlockView)world2, pos2, shapeContext);
      if (!voxelShape.isEmpty()) {
         VoxelShape temp = voxelShape;
         double temp2 = pos2.getX();
         double d = pos2.getZ();
         double d2 = pos2.getY();
         double d3 = temp2;
         if (this.avR(temp.offset(d3, d2, d), flag) != null) {
            return false;
         }
      }

      return true;
   }

   Entity avR(VoxelShape voxelShape, boolean flag) {
      if (voxelShape.isEmpty()) {
         return null;
      }

      List list = flag ? this.avT(voxelShape.getBoundingBox()) : this.avS(voxelShape.getBoundingBox());
      Iterator iterator2 = list.iterator();
      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (!entity2.isRemoved() && entity2.intersectionChecked && VoxelShapes.matchesAnywhere(voxelShape, VoxelShapes.cuboid(entity2.getBoundingBox()), BooleanBiFunction.AND)) {
            return entity2;
         }
      }

      return null;
   }

   List avS(Box box) {
      ClientWorld temp = Mc.client.world;
      Predicate predicate2 = new Predicate() {
         @Override
         public boolean test(Object box) {
            return BlockPosBlockItemHelper.avV((Entity)box);
         }
      };
      return temp.getOtherEntities((Entity)null, box, predicate2);
   }

   List avT(Box box) {
      ClientWorld temp = Mc.client.world;
      Predicate predicate2 = new Predicate() {
         @Override
         public boolean test(Object box) {
            return BlockPosBlockItemHelper.avU((Entity)box);
         }
      };
      return temp.getOtherEntities((Entity)null, box, predicate2);
   }

   static boolean avU(Entity entity2) {
      return entity2 instanceof EndCrystalEntity;
   }

   static boolean avV(Entity entity2) {
      boolean flag;
      if (!entity2.isSpectator() && !(entity2 instanceof EndCrystalEntity)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
