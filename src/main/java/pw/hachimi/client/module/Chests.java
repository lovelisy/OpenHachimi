package pw.hachimi.client.module;

import java.util.Iterator;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import pw.hachimi.client.utils.world.CollisionUtils;

public class Chests extends DrawRenderModule {
   public Chests() {
      super("Chests", "");
   }

   @Override
   public void adb() {
      int n = 0;
      int n2 = 0;
      Iterator iterator2 = CollisionUtils.aiv().iterator();

      while (iterator2.hasNext()) {
         BlockEntity blockEntity = (BlockEntity)iterator2.next();
         if (blockEntity instanceof ChestBlockEntity) {
            BlockState state2 = blockEntity.getCachedState();
            EnumProperty enumProperty = ChestBlock.CHEST_TYPE;
            if (state2.contains((Property)enumProperty)) {
               EnumProperty enumProperty2 = ChestBlock.CHEST_TYPE;
               if ((ChestType)state2.get((Property)enumProperty2) == ChestType.SINGLE) {
                  n++;
               } else {
                  n2++;
               }
            }
         }
      }

      Object[] objs = new Object[]{n + n2 / 2};
      this.text = String.format("Chests §f%d", objs);
      super.adb();
   }
}
