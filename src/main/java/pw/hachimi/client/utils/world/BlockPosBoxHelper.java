package pw.hachimi.client.utils.world;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.model.ObsidianObsidianBedrockMode;

public class BlockPosBoxHelper implements Position {
   private final List list;
   private final BlockPos pos;
   private final ObsidianObsidianBedrockMode obsidianObsidianBedrockMode;

   public BlockPosBoxHelper(BlockPos pos2, ObsidianObsidianBedrockMode obsidianObsidianBedrockMode2, BlockPos... value) {
      this.pos = pos2;
      this.obsidianObsidianBedrockMode = obsidianObsidianBedrockMode2;
      this.list = Lists.newArrayList(value);
      this.list.add(pos2);
   }

   public double adZ(Entity entity2) {
      Vec3d temp = entity2.getEyePos();
      Vec3d vec = this.aej();
      return temp.squaredDistanceTo(vec);
   }

   public boolean aea() {
      boolean flag;
      if (this.list.size() == 5) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean aeb() {
      boolean flag;
      if (this.list.size() == 8) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean aec() {
      if (this.aeb()) {
         List temp = this.list;
         BlockPos pos2 = this.pos.add(2, 0, 0);
         if (temp.contains(pos2)) {
            return true;
         }
      }

      return false;
   }

   public boolean aed() {
      if (this.aeb()) {
         List temp = this.list;
         BlockPos pos2 = this.pos.add(0, 0, 2);
         if (temp.contains(pos2)) {
            return true;
         }
      }

      return false;
   }

   public boolean aee() {
      boolean flag;
      if (this.list.size() == 12) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public ObsidianObsidianBedrockMode getObsidianObsidianBedrockMode() {
      return this.obsidianObsidianBedrockMode;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public List getList() {
      return this.list;
   }

   public boolean aei(BlockPos... value) {
      List temp = this.list;
      List list2 = Arrays.asList(value);
      return temp.addAll(list2);
   }

   public Vec3d aej() {
      BlockPos pos2;
      if (this.aec()) {
         pos2 = this.pos.add(1, 0, 0);
      } else if (this.aed()) {
         pos2 = this.pos.add(0, 0, -1);
      } else {
         if (!this.aee()) {
            return this.pos.toCenterPos();
         }

         pos2 = this.pos.add(1, 0, -1);
      }

      return Vec3d.of(pos2);
   }

   public Box aek(double d) {
      Box box = null;
      if (this.getObsidianObsidianBedrockMode() == ObsidianObsidianBedrockMode.VOID) {
         box = new Box(this.getX(), this.getY(), this.getZ(), this.getX() + 1.0, this.getY() + 1.0, this.getZ() + 1.0);
      } else if (this.aec()) {
         box = new Box(this.getX(), this.getY(), this.getZ(), this.getX() + 2.0, this.getY() + d, this.getZ() + 1.0);
      } else if (this.aed()) {
         box = new Box(this.getX(), this.getY(), this.getZ(), this.getX() + 1.0, this.getY() + d, this.getZ() + 2.0);
      } else if (this.aee()) {
         box = new Box(this.getX(), this.getY(), this.getZ(), this.getX() + 2.0, this.getY() + d, this.getZ() + 2.0);
      } else if (this.aea()) {
         box = new Box(this.getX(), this.getY(), this.getZ(), this.getX() + 1.0, this.getY() + d, this.getZ() + 1.0);
      }

      return box;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj instanceof BlockPosBoxHelper) {
         HashSet temp = new HashSet(((BlockPosBoxHelper)obj).getList());
         List list2 = this.list;
         return temp.containsAll(list2);
      } else {
         return false;
      }
   }

   public double getX() {
      return this.pos.getX();
   }

   public double getY() {
      return this.pos.getY();
   }

   public double getZ() {
      return this.pos.getZ();
   }
}
