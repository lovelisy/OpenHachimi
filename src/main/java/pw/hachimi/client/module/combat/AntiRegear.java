package pw.hachimi.client.module.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.model.combat.AntiRegearBlockBreakMode;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.world.PosTime3;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.module.player.BlockPosBoxMultitask;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;
import pw.hachimi.client.utils.world.BlockPosBlockUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class AntiRegear extends Multitask {
   private static AntiRegear INSTANCE;
   private final List list = new ArrayList();
   Setting mode = this.registerSetting(new EnumSetting("Mode", "mode", AntiRegearBlockBreakMode.Break, AntiRegearBlockBreakMode.values()));
   Setting shulkerRange = this.registerSetting(new NumberSetting("ShulkerRange", "The maximum range the shulker box can be to destory it", 0.0, 4.0, 6.0));
   Setting minePriority = this.registerSetting(new NumberSetting("MinePriority", "Priority of mining shulker", 0, 9, 10));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Automatically faces towards the obsidian being placed", true, this::isRotateVisible));
   Setting placeRange = this.registerSetting(new NumberSetting("PlaceRange", "The distance of your block reach", 0.0, 4.5, 6.0, this::isPlaceRangeVisible));
   Setting breakCrystal = this.registerSetting(new BooleanSetting("BreakCrystal", "Break crystals when placing blocks", true, this::isBreakCrystalVisible));
   Setting breakRange = this.registerSetting(new NumberSetting("BreakRange", "The distance of your entity reach", 0.0, 3.0, 6.0, this::isBreakRangeVisible));
   Setting airPlace = this.registerSetting(new BooleanSetting("AirPlace", "Replace block exploit allow you place block at air", false, this::isAirPlaceVisible));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values(), this::isSwapActionVisible));
   public static boolean ah;

   public AntiRegear() {
      super("AntiRegear", "Lets go to fuck enemy shulkers", ModuleCategory.COMBAT);
      INSTANCE = this;
   }

   public static AntiRegear yz() {
      return INSTANCE;
   }

   @Override
   public void awe() {
      this.list.clear();
   }

   @EventListener
   public void yA(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (!Blink.bm() && ((Boolean)this.breakSetting.atx() || !client.player.isUsingItem())) {
         List temp = this.list;
         Predicate predicate = new Predicate() {
            @Override
            public boolean test(Object tickEntitiesPreEvent) {
               return AntiRegear.yH((PosTime3)tickEntitiesPreEvent);
            }
         };
         temp.removeIf(predicate);
         int n;
         if ((n = ((AntiRegearBlockBreakMode)this.mode.atx()).ordinal()) != 0) {
            if (n != 1) {
               if (n == 2) {
                  this.yD();
                  this.yB();
               }
            } else {
               this.yB();
            }
         } else {
            this.yD();
         }
      }
   }

   private void yB() {
      BlockPosBoxMultitask blockPosBoxMultitask = BlockPosBoxMultitask.alv();
      Iterator iterator2 = yG().iterator();

      while (iterator2.hasNext()) {
         BlockEntity blockEntity = (BlockEntity)iterator2.next();
         if (blockEntity instanceof ShulkerBoxBlockEntity && !this.yC(((ShulkerBoxBlockEntity)blockEntity).getPos())) {
            ClientPlayerEntity temp = client.player;
            Vec3d vec = ((ShulkerBoxBlockEntity)blockEntity).getPos().toCenterPos();
            if (MathHelper.sqrt((float)temp.squaredDistanceTo(vec)) <= (Double)this.shulkerRange.atx()
               && blockPosBoxMultitask.alH(((ShulkerBoxBlockEntity)blockEntity).getPos(), (Integer)this.minePriority.atx())) {
               return;
            }
         }
      }
   }

   private boolean yC(BlockPos pos2) {
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         if (((PosTime3)iterator2.next()).getPos().equals(pos2)) {
            return true;
         }
      }

      return false;
   }

   private void yD() {
      Iterator iterator2 = yG().iterator();

      while (iterator2.hasNext()) {
         BlockEntity blockEntity = (BlockEntity)iterator2.next();
         if (blockEntity instanceof ShulkerBoxBlockEntity && !this.yC(((ShulkerBoxBlockEntity)blockEntity).getPos())) {
            ClientPlayerEntity temp = client.player;
            Vec3d vec = ((ShulkerBoxBlockEntity)blockEntity).getPos().toCenterPos();
            if (MathHelper.sqrt((float)temp.squaredDistanceTo(vec)) <= (Double)this.shulkerRange.atx()) {
               BlockPos temp2 = ((ShulkerBoxBlockEntity)blockEntity).getPos();
               BlockState temp3 = ((ShulkerBoxBlockEntity)blockEntity).getCachedState();
               DirectionProperty directionProperty = FacingBlock.FACING;
               Direction direction = (Direction)temp3.get((Property)directionProperty);
               if ((new BlockPosBlockItemHelper(temp2.offset(direction), Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()))
                  .avM((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx())) {
                  return;
               }
            }
         }
      }
   }

   public void yE(BlockPos pos2) {
      List temp = this.list;
      PosTime3 posTime3 = new PosTime3(pos2, System.currentTimeMillis());
      temp.add(posTime3);
   }

   @EventListener
   public void yF(BlockPosBlockUtils blockPosBlockUtils) {
      if (blockPosBlockUtils.block instanceof ShulkerBoxBlock) {
         this.yE(blockPosBlockUtils.pos);
      }
   }

   public static List yG() {
      ArrayList arrayList = new ArrayList();

      for (int n = -2; n < 2; n++) {
         for (int n2 = -2; n2 < 2; n2++) {
            ClientChunkManager temp = client.world.getChunkManager();
            ClientPlayerEntity player2 = client.player;
            ClientChunkManager clientChunkManager = temp;
            int n3 = (int)player2.getX() / 16 + n;
            int n4 = (int)client.player.getZ() / 16 + n2;
            int n5 = n3;
            WorldChunk worldChunk = clientChunkManager.getWorldChunk(n5, n4);
            if (worldChunk != null) {
               ArrayList arrayList2 = arrayList;
               Collection collection = worldChunk.getBlockEntities().values();
               arrayList2.addAll(collection);
            }
         }
      }

      return arrayList;
   }

   static boolean yH(PosTime3 posTime3) {
      if (System.currentTimeMillis() - posTime3.getTime() > 200L) {
         ClientWorld temp = client.world;
         BlockPos pos2 = posTime3.getPos();
         if (!(temp.getBlockState(pos2).getBlock() instanceof ShulkerBoxBlock)) {
            return true;
         }
      }

      return false;
   }

   private Boolean isSwapActionVisible() {
      boolean flag;
      if (this.mode.atx() == AntiRegearBlockBreakMode.Block) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isAirPlaceVisible() {
      boolean flag;
      if (this.mode.atx() == AntiRegearBlockBreakMode.Block) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBreakRangeVisible() {
      boolean flag;
      if (this.mode.atx() == AntiRegearBlockBreakMode.Block) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBreakCrystalVisible() {
      boolean flag;
      if (this.mode.atx() == AntiRegearBlockBreakMode.Block) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isPlaceRangeVisible() {
      boolean flag;
      if (this.mode.atx() == AntiRegearBlockBreakMode.Block) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isRotateVisible() {
      boolean flag;
      if (this.mode.atx() == AntiRegearBlockBreakMode.Block) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
