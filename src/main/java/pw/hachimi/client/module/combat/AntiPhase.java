package pw.hachimi.client.module.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.ItemFrameItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.TargetPriority;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.module.render.PlaceEcho;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.combat.AntiPhaseHelper;
import pw.hachimi.client.utils.combat.TargetUtils;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.client.utils.world.BlockStateUtils;
import pw.hachimi.client.utils.world.BlockUtils;
import pw.hachimi.client.utils.world.BoxUtils;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class AntiPhase extends Multitask {
   private final List list = new ArrayList();
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting sortPriority = this.registerSetting(new EnumSetting("SortPriority", "How to filter targets within range", TargetPriority.ClosestAngle, TargetPriority.values()));
   Setting maxTargets = this.registerSetting(new NumberSetting("MaxTargets", "How many entities to target at once", 1, 1, 10));
   Setting targetRange = this.registerSetting(new NumberSetting("TargetRange", "", 0.0F, 5.0F, 7.0F));
   Setting placeRange = this.registerSetting(new NumberSetting("PlaceRange", "Interact block reach", 0.0, 4.5, 8.0));
   Setting entityRange = this.registerSetting(new NumberSetting("EntityRange", "Interact entity reach", 0.0, 3.0, 8.0));
   Setting ladder = this.registerSetting(new BooleanSetting("Ladder", "", true));
   Setting onlyHard = this.registerSetting(new BooleanSetting("OnlyHard", "", true, this.ladder::atx));
   Setting itemFrame = this.registerSetting(new BooleanSetting("ItemFrame", "", true));
   Setting fill = this.registerSetting(new BooleanSetting("Fill", "", false, this.itemFrame::atx));
   Setting always = this.registerSetting(new BooleanSetting("Always", "", false, this.itemFrame::atx));
   Setting paint = this.registerSetting(new BooleanSetting("Paint", "", true));
   Setting scaffolding = this.registerSetting(new BooleanSetting("Scaffolding", "", true));
   Setting fire = this.registerSetting(new BooleanSetting("Fire", "", true));
   Setting detectMining = this.registerSetting(new BooleanSetting("DetectMining", "", true, this.fire::atx));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "", true));
   Setting collideSkip = this.registerSetting(new BooleanSetting("CollideSkip", "", true));
   Setting crawlingSkip = this.registerSetting(new BooleanSetting("CrawlingSkip", "", true));
   Setting inAirSkip = this.registerSetting(new BooleanSetting("InAirSkip", "", false));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "", 0, 100, 2000));
   StopwatchImpl stopwatchImpl = new StopwatchImpl();
   public static boolean m;

   public AntiPhase() {
      super("AntiPhase", "", ModuleCategory.COMBAT);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static Box yT(Direction direction, BlockPos pos) {
      Box box = null;
      double d = direction.getOffsetX() * 0.5 + pos.getX() + 0.5;
      double d2 = pos.getY();
      double d3 = direction.getOffsetZ() * 0.5 + pos.getZ() + 0.5;
      switch (AntiPhaseHelper.a[direction.ordinal()]) {
         case 1:
            box = new Box(d, d2, d3, d + 0.1875, d2 + 1.0, d3 + 1.0);
            break;
         case 2:
            box = new Box(d, d2, d3, d - 0.1875, d2 + 1.0, d3 + 1.0);
            break;
         case 3:
            box = new Box(d, d2, d3, d + 1.0, d2 + 1.0, d3 + 0.1875);
            break;
         case 4:
            box = new Box(d, d2, d3, d + 1.0, d2 + 1.0, d3 - 0.1875);
      }

      return box;
   }

   public static boolean yU(Box box) {
      ClientWorld temp = client.world;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return AntiPhase.zf((Entity)obj);
         }
      };
      Iterator iterator2 = temp.getOtherEntities((Entity)null, box, predicate).iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (entity2.isAlive()
            && !(entity2 instanceof ItemEntity)
            && !(entity2 instanceof ExperienceOrbEntity)
            && !(entity2 instanceof ExperienceBottleEntity)
            && !(entity2 instanceof ArrowEntity)
            && !(entity2 instanceof ItemFrameEntity)) {
            Box box2 = entity2.getBoundingBox();
            if (box.intersects(box2)) {
               return true;
            }
         }
      }

      return false;
   }

   @Override
   public void awe() {
      this.list.clear();
   }

   @EventListener
   public void yV(TickEntitiesPreEvent tickEntitiesPreEvent) {
      Iterator iterator2;
      Entity entity2;
      label1997: {
         label1902: {
            label2002: {
               label1786: {
                  Object obj;
                  label1785: {
                     label1796: {
                        label1876: {
                           Object obj2;
                           Object obj3;
                           Object obj4;
                           label1782: {
                              label1781: {
                                 Object obj5;
                                 double d;
                                 int n;
                                 int n2;
                                 double d2;
                                 BlockState state;
                                 int n3;
                                 double d3;
                                 double d4;
                                 int n4;
                                 Vec3d vec;
                                 int n5;
                                 int n6;
                                 int n7;
                                 iterator2 = null;
                                 entity2 = null;
                                 obj = null;
                                 obj2 = null;
                                 obj3 = null;
                                 obj4 = null;
                                 obj5 = null;
                                 d = 0.0;
                                 n = 0;
                                 n2 = 0;
                                 d2 = 0.0;
                                 state = null;
                                 n3 = 0;
                                 d3 = 0.0;
                                 d4 = 0.0;
                                 n4 = 0;
                                 vec = null;
                                 label1780:
                                 if (!(Boolean)this.breakSetting.atx()) {
                                    if (client.player.isUsingItem()) {
                                       return;
                                    }

                                    if (Blink.bm()) {
                                       return;
                                    }

                                    label1777: {
                                       label1877: {
                                          this.list.clear();
                                          final AntiPhase antiPhase = this;
                                          TargetUtils.WY(this.list, new Predicate() {
                                             @Override
                                             public boolean test(Object tickEntitiesPreEvent) {
                                                return antiPhase.za((Entity)tickEntitiesPreEvent);
                                             }
                                          }, (TargetPriority)this.sortPriority.atx(), (Integer)this.maxTargets.atx());
                                          iterator2 = this.list.iterator();
                                          if (iterator2.hasNext()) {
                                             label1773:
                                             while (true) {
                                                entity2 = (Entity)iterator2.next();
                                                if (!(Boolean)this.crawlingSkip.atx()) {
                                                   break label1877;
                                                }

                                                if (!entity2.isCrawling()) {
                                                   if (!(Boolean)this.inAirSkip.atx()) {
                                                      break label1777;
                                                   }

                                                   if (entity2.isOnGround()) {
                                                      if (!(Boolean)this.collideSkip.atx()) {
                                                         n5 = 827361856;
                                                         n7 = -782885237;
                                                         n6 = 1819682119;
                                                         break label1780;
                                                      }

                                                      if (!CollisionUtils.aiy(entity2, entity2.getBoundingBox())
                                                         && this.stopwatchImpl.afq((Number)this.delay.atx())) {
                                                         if (!(Boolean)this.scaffolding.atx()) {
                                                            n5 = -1808263368;
                                                            n7 = 846618242;
                                                            n6 = 1433311613;
                                                            break label1780;
                                                         }

                                                         ClientWorld temp = client.world;
                                                         BlockPos pos = entity2.getBlockPos();
                                                         if (!temp.getBlockState(pos).isReplaceable()) {
                                                            n5 = 138950266;
                                                            n7 = -1610016001;
                                                            n6 = 1433311613;
                                                            break label1780;
                                                         }

                                                         obj = new StackSlotHelper(Items.SCAFFOLDING, (SwapMode)this.swapAction.atx()).LK();
                                                         if (!((StackSlotHelper)obj).LL()) {
                                                            n5 = -1808263368;
                                                            n7 = 846618242;
                                                            n6 = 1433311613;
                                                            break label1780;
                                                         }

                                                         obj2 = entity2.getBlockPos();
                                                         obj3 = null;
                                                         obj4 = PlacementUtils.Fk((BlockPos)obj2, 6.0);
                                                         if ((Direction)obj4 != null) {
                                                            n5 = 1609217358;
                                                            n7 = 699467869;
                                                            n6 = 1495066387;
                                                            break label1780;
                                                         }

                                                         if ((obj3 = this.yX(((BlockPos)obj2).down())) == null) {
                                                            n5 = 830625438;
                                                            n7 = 1082577447;
                                                            n6 = 1822751129;
                                                            break label1780;
                                                         }

                                                         temp = client.world;
                                                         BlockPos pos2 = ((BlockPos)obj2).down();
                                                         if (!(temp.getBlockState(pos2).getBlock() instanceof ScaffoldingBlock)) {
                                                            n5 = 1783802820;
                                                            n7 = 149808841;
                                                            n6 = 1822751129;
                                                            break label1780;
                                                         }

                                                         if (!client.player.isSneaking()) {
                                                            n5 = -1739953834;
                                                            n7 = -223642522;
                                                            n6 = 1495066387;
                                                            break label1780;
                                                         }

                                                         if (!client.player.isSneaking()) {
                                                            n5 = -1808263368;
                                                            n7 = 846618242;
                                                            n6 = 1433311613;
                                                            break label1780;
                                                         }

                                                         if ((obj3 = this.yW(((BlockPos)obj2).down())) == null) {
                                                            n5 = 1402522400;
                                                            n7 = 1751789169;
                                                            n6 = 1433311613;
                                                            break label1780;
                                                         }

                                                         temp = client.world;
                                                         BlockPos pos3 = ((BlockPos)obj2).down();
                                                         if (!(temp.getBlockState(pos3).getBlock() instanceof ScaffoldingBlock)) {
                                                            n5 = 138950266;
                                                            n7 = -1610016001;
                                                            n6 = 1433311613;
                                                            break label1780;
                                                         }

                                                         if ((Direction)obj4 == null) {
                                                            n5 = 1142234182;
                                                            n7 = -187538601;
                                                            n6 = 1122192923;
                                                            break label1780;
                                                         }

                                                         Vec3d vec2 = ((BlockPos)obj2).offset((Direction)obj4).toCenterPos();
                                                         double temp2 = ((Direction)obj4).getOpposite().getVector().getX() * 0.5;
                                                         double d5 = ((Direction)obj4).getOpposite().getVector().getZ() * 0.5;
                                                         double d6 = ((Direction)obj4).getOpposite().getVector().getY() * 0.5;
                                                         double d7 = temp2;
                                                         vec = vec2.add(d7, d6, d5);
                                                         obj5 = vec;
                                                         d = client.player.getEyePos().distanceTo(vec);
                                                         if (!(d <= (Double)this.placeRange.atx())) {
                                                            n5 = -1808263368;
                                                            n7 = 846618242;
                                                            n6 = 1433311613;
                                                            break label1780;
                                                         }

                                                         n2 = Managers.inventoryTracker.JS();
                                                         ((StackSlotHelper)obj).LO(n2);
                                                         temp = client.world;
                                                         BlockPos pos4 = ((BlockPos)obj2).down();
                                                         if (!(temp.getBlockState(pos4).getBlock() instanceof ScaffoldingBlock)) {
                                                            n5 = 11525808;
                                                            n7 = -1969107699;
                                                            n6 = 1569288631;
                                                            break label1780;
                                                         }

                                                         if ((Direction)obj3 == null) {
                                                            n5 = 11525808;
                                                            n7 = -1969107699;
                                                            n6 = 1569288631;
                                                            break label1780;
                                                         }

                                                         PlacementUtils.Fg(
                                                            ((BlockPos)obj2).down(), (Direction)obj3, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx()
                                                         );
                                                         ((StackSlotHelper)obj).LP(n2);
                                                         this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                                         if (!(Boolean)this.itemFrame.atx()) {
                                                            n5 = 1612201874;
                                                            n7 = -2000237117;
                                                            n6 = 1726901711;
                                                            break label1780;
                                                         }

                                                         temp = client.world;
                                                         BlockPos pos5 = entity2.getBlockPos();
                                                         if (!temp.isAir(pos5)) {
                                                            n5 = 1612201874;
                                                            n7 = -2000237117;
                                                            n6 = 1726901711;
                                                            break label1780;
                                                         }

                                                         obj = this.yZ(new Box(entity2.getBlockPos()));
                                                         if ((Boolean)this.always.atx()) {
                                                            n5 = 1940485284;
                                                            n7 = 214450911;
                                                            n6 = 1969170169;
                                                            break label1780;
                                                         }

                                                         if ((ItemFrameEntity)obj != null) {
                                                            n5 = 2003007682;
                                                            n7 = -1943252217;
                                                            n6 = 1905601183;
                                                            break label1780;
                                                         }

                                                         obj2 = new StackSlotHelper(new Predicate() {
                                                            @Override
                                                            public boolean test(Object tickEntitiesPreEvent) {
                                                               return AntiPhase.ze((ItemStack)tickEntitiesPreEvent);
                                                            }
                                                         }, (SwapMode)this.swapAction.atx()).LK();
                                                         if (!((StackSlotHelper)obj2).LL()) {
                                                            n5 = 2003007682;
                                                            n7 = -1943252217;
                                                            n6 = 1905601183;
                                                            break label1780;
                                                         }

                                                         obj3 = entity2.getBlockPos().down();
                                                         Vec3d vec3 = client.player.getEyePos();
                                                         Vec3d vec4 = ((BlockPos)obj3).toBottomCenterPos().add(0.0, 1.0, 0.0);
                                                         d2 = vec3.distanceTo(vec4);
                                                         if (!(d2 <= (Double)this.placeRange.atx())) {
                                                            n5 = 749962648;
                                                            n7 = -993559059;
                                                            n6 = 1905601183;
                                                            break label1780;
                                                         }

                                                         state = client.world.getBlockState((BlockPos)obj3);
                                                         if (!PlacementUtils.blockPosDirectionBase.Hr((BlockPos)obj3, Direction.UP)) {
                                                            n5 = 2003007682;
                                                            n7 = -1943252217;
                                                            n6 = 1905601183;
                                                            break label1780;
                                                         }

                                                         if (state.isReplaceable()) {
                                                            n5 = 749962648;
                                                            n7 = -993559059;
                                                            n6 = 1905601183;
                                                            break label1780;
                                                         }

                                                         if (!BlockStateUtils.Wy(state, (BlockPos)obj3)) {
                                                            n5 = 1419792634;
                                                            n7 = -1064553343;
                                                            n6 = 1381368487;
                                                            break label1780;
                                                         }

                                                         if (!client.player.isSneaking()) {
                                                            n5 = -1329355558;
                                                            n7 = 1446248860;
                                                            n6 = 1905601183;
                                                            break label1780;
                                                         }

                                                         n = Managers.inventoryTracker.JS();
                                                         ((StackSlotHelper)obj2).LO(n);
                                                         PlacementUtils.Fg((BlockPos)obj3, Direction.UP, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                                         ((StackSlotHelper)obj2).LP(n);
                                                         this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                                         if (!(Boolean)this.fill.atx()) {
                                                            n5 = 1612201874;
                                                            n7 = -2000237117;
                                                            n6 = 1726901711;
                                                            break label1780;
                                                         }

                                                         if ((ItemFrameEntity)obj == null) {
                                                            n5 = 1612201874;
                                                            n7 = -2000237117;
                                                            n6 = 1726901711;
                                                            break label1780;
                                                         }

                                                         if (!((ItemFrameEntity)obj).getHeldItemStack().isEmpty()) {
                                                            n5 = 1003012808;
                                                            n7 = -1057096919;
                                                            n6 = 1726901711;
                                                            break label1780;
                                                         }

                                                         obj2 = BoxUtils.jy(client.player.getEyePos(), ((ItemFrameEntity)obj).getBoundingBox());
                                                         if (!(client.player.getEyePos().distanceTo((Vec3d)obj2) <= (Double)this.entityRange.atx())) {
                                                            n5 = -1481055350;
                                                            n7 = 1377992912;
                                                            n6 = 1726901711;
                                                            break label1780;
                                                         }

                                                         obj3 = new StackSlotHelper(Items.OBSIDIAN, (SwapMode)this.swapAction.atx()).LK();
                                                         if (!((StackSlotHelper)obj3).LL()) {
                                                            n5 = 1612201874;
                                                            n7 = -2000237117;
                                                            n6 = 1726901711;
                                                            break label1780;
                                                         }

                                                         n3 = Managers.inventoryTracker.JS();
                                                         ((StackSlotHelper)obj3).LO(n3);
                                                         if (!(Boolean)this.rotate.atx()) {
                                                            n5 = 1298541738;
                                                            n7 = 172704855;
                                                            n6 = 1267760887;
                                                            break label1780;
                                                         }

                                                         RotationUtils.nI((Vec3d)obj2);
                                                         ClientPlayNetworkHandler network2 = client.getNetworkHandler();
                                                         ItemFrameEntity itemFrameEntity = (ItemFrameEntity)obj;
                                                         boolean temp3 = client.player.isSneaking();
                                                         Hand hand = Hand.MAIN_HAND;
                                                         boolean flag = temp3;
                                                         PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket = PlayerInteractEntityC2SPacket.interact(
                                                            (Entity)itemFrameEntity, flag, hand
                                                         );
                                                         network2.sendPacket((Packet)playerInteractEntityC2SPacket);
                                                         ((StackSlotHelper)obj3).LP(n3);
                                                         if (!(Boolean)this.rotate.atx()) {
                                                            n5 = 1230720006;
                                                            n7 = -1663748867;
                                                            n6 = 1336745563;
                                                            break label1780;
                                                         }

                                                         RotationUtils.nK();
                                                         this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                                         if (!(Boolean)this.paint.atx()) {
                                                            n5 = 165894552;
                                                            n7 = -1342563595;
                                                            n6 = 1422259871;
                                                            break label1780;
                                                         }

                                                         temp = client.world;
                                                         BlockPos pos6 = entity2.getBlockPos();
                                                         if (!temp.isAir(pos6)) {
                                                            n5 = 1379116226;
                                                            n7 = 1463365247;
                                                            n6 = 1422259871;
                                                            break label1780;
                                                         }

                                                         obj = this.yY(new Box(entity2.getBlockPos()));
                                                         if ((PaintingEntity)obj != null) {
                                                            n5 = 165894552;
                                                            n7 = -1342563595;
                                                            n6 = 1422259871;
                                                            break label1780;
                                                         }

                                                         obj2 = new StackSlotHelper(Items.PAINTING, (SwapMode)this.swapAction.atx()).LK();
                                                         if (!((StackSlotHelper)obj2).LL()) {
                                                            n5 = 165894552;
                                                            n7 = -1342563595;
                                                            n6 = 1422259871;
                                                            break label1780;
                                                         }

                                                         obj3 = this.zb(entity2.getPos());
                                                         if ((Direction)obj3 != null) {
                                                            BlockPos pos7 = entity2.getBlockPos().offset((Direction)obj3);
                                                            Vec3d vec5 = client.player.getEyePos();
                                                            Vec3d vec6 = pos7.toCenterPos();
                                                            double d8 = ((Direction)obj3).getOpposite().getVector().getX() * 0.5;
                                                            double d9 = ((Direction)obj3).getOpposite().getVector().getZ() * 0.5;
                                                            double d10 = ((Direction)obj3).getOpposite().getVector().getY() * 0.5;
                                                            double d11 = d8;
                                                            Vec3d vec7 = vec6.add(d11, d10, d9);
                                                            if (vec5.distanceTo(vec7) <= (Double)this.placeRange.atx()) {
                                                               n = Managers.inventoryTracker.JS();
                                                               ((StackSlotHelper)obj2).LO(n);
                                                               PlacementUtils.Fg(
                                                                  pos7, ((Direction)obj3).getOpposite(), Hand.MAIN_HAND, true, (Boolean)this.rotate.atx()
                                                               );
                                                               ((StackSlotHelper)obj2).LP(n);
                                                               this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                                            }
                                                            break label1781;
                                                         }

                                                         if (!(Boolean)this.fire.atx()) {
                                                            n5 = -2075864488;
                                                            n7 = -903965092;
                                                            n6 = 1158831133;
                                                            break label1780;
                                                         }

                                                         temp = client.world;
                                                         BlockPos pos8 = entity2.getBlockPos();
                                                         if (!temp.isAir(pos8)) {
                                                            n5 = 1139019328;
                                                            n7 = 281208391;
                                                            n6 = 1158831133;
                                                            break label1780;
                                                         }

                                                         if (!(Boolean)this.detectMining.atx()) {
                                                            n5 = -2096929176;
                                                            n7 = -1340706272;
                                                            n6 = 1112926253;
                                                            break label1780;
                                                         }

                                                         if (Managers.breakManager.Rv(entity2.getBlockPos())) {
                                                            n5 = -2075864488;
                                                            n7 = -903965092;
                                                            n6 = 1158831133;
                                                            break label1780;
                                                         }

                                                         obj = new StackSlotHelper(Items.FLINT_AND_STEEL, (SwapMode)this.swapAction.atx()).LK();
                                                         if (!((StackSlotHelper)obj).LL()) {
                                                            n5 = 406123290;
                                                            n7 = 1759629997;
                                                            n6 = 1158831133;
                                                            break label1780;
                                                         }

                                                         obj2 = entity2.getBlockPos().down();
                                                         Vec3d vec8 = client.player.getEyePos();
                                                         Vec3d vec9 = ((BlockPos)obj2).toBottomCenterPos().add(0.0, 1.0, 0.0);
                                                         d4 = vec8.distanceTo(vec9);
                                                         if (!(d4 <= (Double)this.placeRange.atx())) {
                                                            n5 = 1139019328;
                                                            n7 = 281208391;
                                                            n6 = 1158831133;
                                                            break label1780;
                                                         }

                                                         obj5 = client.world.getBlockState((BlockPos)obj2);
                                                         if (!PlacementUtils.blockPosDirectionBase.Hr((BlockPos)obj2, Direction.UP)) {
                                                            n5 = 406123290;
                                                            n7 = 1759629997;
                                                            n6 = 1158831133;
                                                            break label1780;
                                                         }

                                                         if (((BlockState)obj5).isReplaceable()) {
                                                            n5 = -2075864488;
                                                            n7 = -903965092;
                                                            n6 = 1158831133;
                                                            break label1780;
                                                         }

                                                         if (!BlockStateUtils.Wy((BlockState)obj5, (BlockPos)obj2)) {
                                                            n5 = 2079914288;
                                                            n7 = 1846610789;
                                                            n6 = 2098113389;
                                                            break label1780;
                                                         }

                                                         if (client.player.isSneaking()) {
                                                            n4 = Managers.inventoryTracker.JS();
                                                            ((StackSlotHelper)obj).LO(n4);
                                                            PlacementUtils.Fg((BlockPos)obj2, Direction.UP, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                                            ((StackSlotHelper)obj).LP(n4);
                                                            this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                                            break label1796;
                                                         }

                                                         if (!(Boolean)this.ladder.atx()) {
                                                            n5 = -1755791010;
                                                            n7 = -1953362184;
                                                            n6 = 1443778331;
                                                            break label1780;
                                                         }

                                                         obj = new StackSlotHelper(Items.LADDER, (SwapMode)this.swapAction.atx()).LK();
                                                         if (!((StackSlotHelper)obj).LL()) {
                                                            n5 = -1755791010;
                                                            n7 = -1953362184;
                                                            n6 = 1443778331;
                                                            break label1780;
                                                         }

                                                         temp = client.world;
                                                         BlockPos pos9 = entity2.getBlockPos();
                                                         if (!temp.getBlockState(pos9).isReplaceable()) {
                                                            n5 = 1358437702;
                                                            n7 = -782868517;
                                                            n6 = 1443778331;
                                                            break label1780;
                                                         }

                                                         obj2 = this.zb(entity2.getPos());
                                                         if ((Direction)obj2 == null) {
                                                            n5 = 187228188;
                                                            n7 = 695028049;
                                                            n6 = 1443778331;
                                                            break label1780;
                                                         }

                                                         obj3 = entity2.getBlockPos().offset((Direction)obj2);
                                                         Vec3d vec10 = client.player.getEyePos();
                                                         Vec3d vec11 = ((BlockPos)obj3).toCenterPos();
                                                         double d12 = ((Direction)obj2).getOpposite().getVector().getX() * 0.5;
                                                         double d13 = ((Direction)obj2).getOpposite().getVector().getZ() * 0.5;
                                                         double d14 = ((Direction)obj2).getOpposite().getVector().getY() * 0.5;
                                                         double d15 = d12;
                                                         Vec3d vec12 = vec11.add(d15, d14, d13);
                                                         d2 = vec10.distanceTo(vec12);
                                                         if (d2 <= (Double)this.placeRange.atx()) {
                                                            List list2 = PlacementUtils.a_;
                                                            BlockPos pos10 = entity2.getBlockPos();
                                                            list2.add(pos10);
                                                            PlaceEcho.Mz().MB(entity2.getBlockPos());
                                                            n4 = Managers.inventoryTracker.JS();
                                                            ((StackSlotHelper)obj).LO(n4);
                                                            PlacementUtils.Fg(
                                                               (BlockPos)obj3,
                                                               ((Direction)obj2).getOpposite(),
                                                               Hand.MAIN_HAND,
                                                               true,
                                                               (Boolean)this.rotate.atx()
                                                            );
                                                            ((StackSlotHelper)obj).LP(n4);
                                                            this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                                            break label1786;
                                                         }

                                                         do {
                                                            if (!iterator2.hasNext()) {
                                                               break label1773;
                                                            }

                                                            entity2 = (Entity)iterator2.next();
                                                            if (!(Boolean)this.crawlingSkip.atx()) {
                                                               break label1877;
                                                            }
                                                         } while (entity2.isCrawling());

                                                         if ((Boolean)this.inAirSkip.atx()) {
                                                            if (!entity2.isOnGround()) {
                                                               n6 = 1390806743;
                                                               int n8 = -110955690
                                                                  + 1560050958 * (479759296 ^ (479759296 | ~479759296))
                                                                  + -405859403 * 479759296
                                                                  + 1256362543 * ((~479759296 | 479759296) & (479759296 ^ 479759296 ^ 479759296 ^ 479759296))
                                                                  + 345264680 * (479759296 & 479759296 ^ ~479759296 ^ ~479759296)
                                                                  + 156693500
                                                                     * (479759296 ^ 479759296 | 479759296 ^ 479759296 | 479759296 & 479759296 & 479759296)
                                                                  + 263021090 * (479759296 | 479759296)
                                                                  + -1602784857 * (~(~479759296) | ~(479759296 | 479759296))
                                                                  + -1879972457 * (~479759296 | ~479759296 | 479759296)
                                                                  + 2102982738 * (479759296 ^ 479759296)
                                                                  + 1654302946 * ((479759296 | 479759296 | 479759296) & ~(479759296 | 479759296))
                                                                  + 1798553046 * 479759296
                                                                  + -597621955 * ((479759296 & 479759296 ^ ~479759296) & (479759296 | 479759296));
                                                               n5 = 2044912466
                                                                  + -2110030440 * 479759296
                                                                  + -1379852682 * (~(479759296 & 479759296) & 479759296)
                                                                  + 274650003 * ~(479759296 & (n8 | 479759296))
                                                                  + 1382682494 * ((~479759296 | 479759296) & (~n8 ^ 479759296))
                                                                  + 625969152 * (n8 & (~479759296 ^ n8 & 479759296))
                                                                  + -819826205 * ~(n8 | n8)
                                                                  + -222162564 * (~479759296 & 479759296 | 479759296 & n8 ^ (479759296 | 479759296))
                                                                  + 1307439742 * (479759296 & 479759296 ^ 479759296 | ~n8 & ~479759296)
                                                                  + -1941158461 * n8
                                                                  + -1001997948 * ((~479759296 | 479759296 ^ 479759296) & ~479759296)
                                                                  + -870970874 * (~479759296 & (n8 | n8) ^ ~(479759296 & n8))
                                                                  + -1980452829 * n8;
                                                               n7 = 0;
                                                            } else {
                                                               n5 = 1934132680;
                                                               n7 = 40298603;
                                                               n6 = 1975441301;
                                                            }
                                                            break label1780;
                                                         }
                                                         break label1777;
                                                      }
                                                   }
                                                }

                                                if (!iterator2.hasNext()) {
                                                   return;
                                                }
                                             }
                                          }

                                          n5 = 1696831830;
                                          n7 = 498552527;
                                          n6 = 1674955531;
                                          break label1780;
                                       }

                                       n5 = 1790483048;
                                       n7 = -1374892689;
                                       n6 = 1817102389;
                                       break label1780;
                                    }

                                    n5 = -1259841072;
                                    n7 = -662528888;
                                    n6 = 1975441301;
                                 } else {
                                    n5 = -1793945386;
                                    n7 = -1228836698;
                                    n6 = 1413750419;
                                 }

                                 label1651:
                                 while (true) {
                                    int n9 = n6;
                                    if (n7 == 0) {
                                       n6 *= 1307288573;
                                    }

                                    label1818: {
                                       switch (n5 ^ n9) {
                                          case -2108790861:
                                             BlockPos pos11 = (BlockPos)obj3;
                                             state = client.world.getBlockState(pos11);
                                             int n10 = n6 * 138965641;
                                             n5 = -1482308660;
                                             n7 = -1644580840;
                                             n6 = n10;
                                             continue;
                                          case -2102650877:
                                             PlaceEcho.Mz().MB(entity2.getBlockPos());
                                             int n11 = n6 * 1429124717;
                                             n5 = 1801064930;
                                             n7 = -1088069415;
                                             n6 = n11;
                                             continue;
                                          case -2075201669:
                                             if (!(Boolean)this.fire.atx()) {
                                                break label1796;
                                             }

                                             int n12 = n6 * -1298671571;
                                             n5 = 1689810042;
                                             n7 = 1494870569;
                                             n6 = n12;
                                             continue;
                                          case -1932623785:
                                             ((StackSlotHelper)obj2).LP(n);
                                             int n13 = n6 * -1632552397;
                                             n5 = 1109113650;
                                             n7 = -1776771649;
                                             n6 = n13;
                                             continue;
                                          case -1799682297:
                                             Vec3d vec13 = client.player.getEyePos();
                                             Vec3d vec14 = ((BlockPos)obj3).toBottomCenterPos().add(0.0, 1.0, 0.0);
                                             d2 = vec13.distanceTo(vec14);
                                             int n14 = n6 * -482101875;
                                             n5 = 1554737874;
                                             n7 = -1012557905;
                                             n6 = n14;
                                             continue;
                                          case -1789406901:
                                             iterator2 = this.list.iterator();
                                             int n15 = n6 * 180435857;
                                             n5 = 1410356362;
                                             n7 = 1178655027;
                                             n6 = n15;
                                             continue;
                                          case -1772715869:
                                             if (!(Boolean)this.itemFrame.atx()) {
                                                break label1902;
                                             }

                                             int n16 = n6 * 517245073;
                                             n5 = 796058526;
                                             n7 = -590022237;
                                             n6 = n16;
                                             continue;
                                          case -1734542079:
                                             if (!((StackSlotHelper)obj2).LL()) {
                                                break label1651;
                                             }

                                             int n17 = n6 * 559025325;
                                             n5 = 1177789214;
                                             n7 = -912216187;
                                             n6 = n17;
                                             continue;
                                          case -1720919175:
                                             PlacementUtils.Fg(((BlockPos)obj2).down(), (Direction)obj3, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                             ((StackSlotHelper)obj).LP(n2);
                                             this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                             break;
                                          case -1719643585:
                                             if (!(Boolean)this.rotate.atx()) {
                                                this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                                break label1902;
                                             }

                                             int n18 = n6 * 1508411015;
                                             n5 = 1496543544;
                                             n7 = -1900557311;
                                             n6 = n18;
                                             continue;
                                          case -1637889237:
                                             if ((Direction)obj3 == null) {
                                                break label1651;
                                             }

                                             int n19 = n6 * -654285521;
                                             n5 = 1627665858;
                                             n7 = -1593015329;
                                             n6 = n19;
                                             continue;
                                          case -1636363005:
                                             ((StackSlotHelper)obj).LP(n4);
                                             int n20 = n6 * -682896399;
                                             n5 = 21174810;
                                             n7 = -2105202029;
                                             n6 = n20;
                                             continue;
                                          case -1629211207:
                                             ((StackSlotHelper)obj).LO(n4);
                                             int n21 = n6 * -755501845;
                                             n5 = 814454660;
                                             n7 = -1055339929;
                                             n6 = n21;
                                             continue;
                                          case -1628581279:
                                             ((StackSlotHelper)obj).LP(n2);
                                             int n22 = n6 * 2120964853;
                                             n5 = -1305740176;
                                             n7 = 526475638;
                                             n6 = n22;
                                             continue;
                                          case -1536076723:
                                             final AntiPhase antiPhase2 = this;
                                             TargetUtils.WY(this.list, new Predicate() {
                                                @Override
                                                public boolean test(Object tickEntitiesPreEvent) {
                                                   return antiPhase2.za((Entity)tickEntitiesPreEvent);
                                                }
                                             }, (TargetPriority)this.sortPriority.atx(), (Integer)this.maxTargets.atx());
                                             int n23 = n6 * 1976974917;
                                             n5 = -1568913162;
                                             n7 = -416472920;
                                             n6 = n23;
                                             continue;
                                          case -1389091609:
                                             this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                             int n24 = n6 * 2065773981;
                                             n5 = -1808263368;
                                             n7 = 846618242;
                                             n6 = n24;
                                             continue;
                                          case -1339721679:
                                             ClientWorld world2 = client.world;
                                             BlockPos pos12 = entity2.getBlockPos();
                                             if (!world2.isAir(pos12)) {
                                                break label1796;
                                             }

                                             if (!(Boolean)this.detectMining.atx()) {
                                                break label2002;
                                             }

                                             if (Managers.breakManager.Rv(entity2.getBlockPos())) {
                                                break label1796;
                                             }

                                             int n25 = n6 * -1739957209;
                                             n5 = -2096929176;
                                             n7 = -1340706272;
                                             n6 = n25;
                                             continue;
                                          case -1336338501:
                                             PlacementUtils.Fa((BlockPos)obj2, Hand.MAIN_HAND, (Boolean)this.rotate.atx());
                                             int n26 = n6 * -562955953;
                                             n5 = 791867282;
                                             n7 = -1974935067;
                                             n6 = n26;
                                             continue;
                                          case -1327853933:
                                             ClientWorld world3 = client.world;
                                             BlockPos pos13 = entity2.getBlockPos();
                                             if (!world3.isAir(pos13)) {
                                                break label1651;
                                             }

                                             int n27 = n6 * -1835358639;
                                             n5 = 2045004040;
                                             n7 = 1830219721;
                                             n6 = n27;
                                             continue;
                                          case -1303116401:
                                             ClientWorld world4 = client.world;
                                             BlockPos pos14 = entity2.getBlockPos();
                                             if (world4.getBlockState(pos14).isReplaceable()) {
                                                int n28 = n6 * -1883222213;
                                                n5 = -1200484098;
                                                n7 = -42357616;
                                                n6 = n28;
                                                continue;
                                             }
                                             break;
                                          case -1275827209:
                                             if (!iterator2.hasNext()) {
                                                return;
                                             }

                                             entity2 = (Entity)iterator2.next();
                                             int n29 = n6 * -737905167;
                                             n5 = 424038092;
                                             n7 = 1759708655;
                                             n6 = n29;
                                             continue;
                                          case -1267886681:
                                             n4 = Managers.inventoryTracker.JS();
                                             int n30 = n6 * -1714741211;
                                             n5 = 1912225248;
                                             n7 = 1318776351;
                                             n6 = n30;
                                             continue;
                                          case -1190951471:
                                             if (!(d2 <= (Double)this.placeRange.atx())) {
                                                break label1786;
                                             }

                                             int n31 = n6 * -1763821153;
                                             n5 = 2114554342;
                                             n7 = 2086053821;
                                             n6 = n31;
                                             continue;
                                          case -1186053789:
                                             obj = this.yY(new Box(entity2.getBlockPos()));
                                             int n32 = n6 * -2025916073;
                                             n5 = 179775568;
                                             n7 = -2006504997;
                                             n6 = n32;
                                             continue;
                                          case -1177420649:
                                             this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                             int n33 = n6 * 1364107349;
                                             n5 = 1139019328;
                                             n7 = 281208391;
                                             n6 = n33;
                                             continue;
                                          case -1131950699:
                                             if ((PaintingEntity)obj != null) {
                                                break label1651;
                                             }

                                             int n34 = n6 * -197824591;
                                             n5 = -1846306354;
                                             n7 = -537198112;
                                             n6 = n34;
                                             continue;
                                          case -1051292091:
                                             int n35 = -110955690
                                                + 1560050958 * (n7 ^ (n7 | ~n7))
                                                + -405859403 * n7
                                                + 1256362543 * ((~n7 | n7) & (n7 ^ n7 ^ n7 ^ n7))
                                                + 345264680 * (n7 & n7 ^ ~n7 ^ ~n7)
                                                + 156693500 * (n7 ^ n7 | n7 ^ n7 | n7 & n7 & n7)
                                                + 263021090 * (n7 | n7)
                                                + -1602784857 * (~(~n7) | ~(n7 | n7))
                                                + -1879972457 * (~n7 | ~n7 | n7)
                                                + 2102982738 * (n7 ^ n7)
                                                + 1654302946 * ((n7 | n7 | n7) & ~(n7 | n7))
                                                + 1798553046 * n7
                                                + -597621955 * ((n7 & n7 ^ ~n7) & (n7 | n7));
                                             n5 = 2044912466
                                                + -2110030440 * n7
                                                + -1379852682 * (~(n7 & n7) & n7)
                                                + 274650003 * ~(n7 & (n35 | n7))
                                                + 1382682494 * ((~n7 | n7) & (~n35 ^ n7))
                                                + 625969152 * (n35 & (~n7 ^ n35 & n7))
                                                + -819826205 * ~(n35 | n35)
                                                + -222162564 * (~n7 & n7 | n7 & n35 ^ (n7 | n7))
                                                + 1307439742 * (n7 & n7 ^ n7 | ~n35 & ~n7)
                                                + -1941158461 * n35
                                                + -1001997948 * ((~n7 | n7 ^ n7) & ~n7)
                                                + -870970874 * (~n7 & (n35 | n35) ^ ~(n7 & n35))
                                                + -1980452829 * n35;
                                             n7 = 0;
                                             continue;
                                          case -987109385:
                                             if (!((StackSlotHelper)obj2).LL()) {
                                                break label1785;
                                             }

                                             int n36 = n6 * 1715665533;
                                             n5 = 1355589024;
                                             n7 = -1921881945;
                                             n6 = n36;
                                             continue;
                                          case -983427585:
                                             if (!(Boolean)this.paint.atx()) {
                                                break label1651;
                                             }

                                             int n37 = n6 * 539737491;
                                             n5 = 696933998;
                                             n7 = -1547176733;
                                             n6 = n37;
                                             continue;
                                          case -983097025:
                                          case -389708503:
                                          case 471180627:
                                          case 1026427659:
                                             break label1786;
                                          case -981105393:
                                             this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                             int n38 = n6 * -1769315515;
                                             n5 = 1358437702;
                                             n7 = -782868517;
                                             n6 = n38;
                                             continue;
                                          case -978504193:
                                             ClientWorld world5 = client.world;
                                             BlockPos pos15 = ((BlockPos)obj2).down();
                                             if (world5.getBlockState(pos15).getBlock() instanceof ScaffoldingBlock && (Direction)obj3 != null) {
                                                int n39 = n6 * 1777874681;
                                                n5 = 1323786892;
                                                n7 = 1446271163;
                                                n6 = n39;
                                                continue;
                                             }

                                             PlacementUtils.Fa((BlockPos)obj2, Hand.MAIN_HAND, (Boolean)this.rotate.atx());
                                             ((StackSlotHelper)obj).LP(n2);
                                             this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                             break;
                                          case -915143973:
                                             ((StackSlotHelper)obj3).LP(n3);
                                             int n40 = n6 * 668672203;
                                             n5 = 1825113394;
                                             n7 = 955597539;
                                             n6 = n40;
                                             continue;
                                          case -813411851:
                                             PlacementUtils.Fg((BlockPos)obj2, Direction.UP, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                             int n41 = n6 * -816037353;
                                             n5 = 932252086;
                                             n7 = 1941012651;
                                             n6 = n41;
                                             continue;
                                          case -807175911:
                                             List list3 = PlacementUtils.a_;
                                             BlockPos pos16 = entity2.getBlockPos();
                                             list3.add(pos16);
                                             int n42 = n6 * 771088409;
                                             n5 = 995885872;
                                             n7 = -2086158555;
                                             n6 = n42;
                                             continue;
                                          case -712556085:
                                             this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                             int n43 = n6 * -2084407451;
                                             n5 = 165894552;
                                             n7 = -1342563595;
                                             n6 = n43;
                                             continue;
                                          case -689906573:
                                             if (!(Boolean)this.fill.atx()
                                                || (ItemFrameEntity)obj == null
                                                || !((ItemFrameEntity)obj).getHeldItemStack().isEmpty()) {
                                                break label1902;
                                             }

                                             int n44 = n6 * -965620675;
                                             n5 = -2105715182;
                                             n7 = -790365890;
                                             n6 = n44;
                                             continue;
                                          case -677090951:
                                             n3 = Managers.inventoryTracker.JS();
                                             int n45 = n6 * -1497113287;
                                             n5 = 191192342;
                                             n7 = 272761737;
                                             n6 = n45;
                                             continue;
                                          case -667807431:
                                             this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                             int n46 = n6 * 1313575201;
                                             n5 = 1612201874;
                                             n7 = -2000237117;
                                             n6 = n46;
                                             continue;
                                          case -620837303:
                                             obj2 = entity2.getBlockPos();
                                             int n47 = n6 * -628947377;
                                             n5 = 202896386;
                                             n7 = -1058171851;
                                             n6 = n47;
                                             continue;
                                          case -451366687:
                                             if (!(Boolean)this.inAirSkip.atx() || entity2.isOnGround()) {
                                                break label1876;
                                             }

                                             int n48 = n6 * -1421058503;
                                             n5 = -1337018148;
                                             n7 = -1988855242;
                                             n6 = n48;
                                             continue;
                                          case -397053779:
                                             PlacementUtils.Fg(
                                                (BlockPos)obj4, ((Direction)obj3).getOpposite(), Hand.MAIN_HAND, true, (Boolean)this.rotate.atx()
                                             );
                                             int n49 = n6 * 1290608725;
                                             n5 = -1734792964;
                                             n7 = 11096466;
                                             n6 = n49;
                                             continue;
                                          case -385084843:
                                             this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                             int n50 = n6 * 80961813;
                                             n5 = -1329355558;
                                             n7 = 1446248860;
                                             n6 = n50;
                                             continue;
                                          case -371713757:
                                             n = Managers.inventoryTracker.JS();
                                             int n51 = n6 * -1068721377;
                                             n5 = 509065130;
                                             n7 = -1183266855;
                                             n6 = n51;
                                             continue;
                                          case -343509777:
                                             PlacementUtils.Fg(
                                                (BlockPos)obj3, ((Direction)obj2).getOpposite(), Hand.MAIN_HAND, true, (Boolean)this.rotate.atx()
                                             );
                                             int n52 = n6 * 1248073015;
                                             n5 = 1231883484;
                                             n7 = -704171189;
                                             n6 = n52;
                                             continue;
                                          case -320909933:
                                             if (!PlacementUtils.blockPosDirectionBase.Hr((BlockPos)obj2, Direction.UP) || ((BlockState)obj5).isReplaceable()
                                                )
                                              {
                                                break label1796;
                                             }

                                             if (!BlockStateUtils.Wy((BlockState)obj5, (BlockPos)obj2)) {
                                                n4 = Managers.inventoryTracker.JS();
                                                ((StackSlotHelper)obj).LO(n4);
                                                PlacementUtils.Fg((BlockPos)obj2, Direction.UP, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                                ((StackSlotHelper)obj).LP(n4);
                                                this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                                break label1796;
                                             }

                                             if (!client.player.isSneaking()) {
                                                break label1796;
                                             }

                                             int n53 = n6 * 524265653;
                                             n5 = -1135074008;
                                             n7 = 877361534;
                                             n6 = n53;
                                             continue;
                                          case -315541981:
                                             ClientWorld world6 = client.world;
                                             BlockPos pos17 = entity2.getBlockPos();
                                             if (!world6.isAir(pos17)) {
                                                break label1902;
                                             }

                                             int n54 = n6 * -2105750793;
                                             n5 = 2032182654;
                                             n7 = -971677791;
                                             n6 = n54;
                                             continue;
                                          case -268096399:
                                             obj3 = entity2.getBlockPos().down();
                                             int n55 = n6 * 1986714799;
                                             n5 = 2037744762;
                                             n7 = 812348723;
                                             n6 = n55;
                                             continue;
                                          case -223493087:
                                             obj3 = null;
                                             int n56 = n6 * -704845647;
                                             n5 = 2101789436;
                                             n7 = 774429783;
                                             n6 = n56;
                                             continue;
                                          case -164080501:
                                             RotationUtils.nK();
                                             int n57 = n6 * -820312981;
                                             n5 = -1896116194;
                                             n7 = 1175398406;
                                             n6 = n57;
                                             continue;
                                          case -109546387:
                                             ((StackSlotHelper)obj2).LO(n);
                                             int n58 = n6 * 850517953;
                                             n5 = 1081915876;
                                             n7 = 1972743983;
                                             n6 = n58;
                                             continue;
                                          case -45036959:
                                             ((StackSlotHelper)obj2).LP(n);
                                             int n59 = n6 * 1530370035;
                                             n5 = -1923580646;
                                             n7 = 1353390014;
                                             n6 = n59;
                                             continue;
                                          case -40816427:
                                             break label1997;
                                          case 78415565:
                                             n2 = Managers.inventoryTracker.JS();
                                             int n60 = n6 * -680029311;
                                             n5 = -1510354738;
                                             n7 = -709591666;
                                             n6 = n60;
                                             continue;
                                          case 78692993:
                                             ((StackSlotHelper)obj3).LO(n3);
                                             int n61 = n6 * -1749195807;
                                             n5 = 1705720432;
                                             n7 = -342369887;
                                             n6 = n61;
                                             continue;
                                          case 97895241:
                                             if (!((StackSlotHelper)obj3).LL()) {
                                                break label1902;
                                             }

                                             int n62 = n6 * 629091065;
                                             n5 = 275793354;
                                             n7 = -666636123;
                                             n6 = n62;
                                             continue;
                                          case 112364059:
                                             if (!(d2 <= (Double)this.placeRange.atx())) {
                                                break label1785;
                                             }

                                             int n63 = n6 * -310317641;
                                             n5 = 972734290;
                                             n7 = -2048656137;
                                             n6 = n63;
                                             continue;
                                          case 116806237:
                                             int n64 = -1531429432
                                                + -476030923 * (~n7 | (n7 | n7) ^ (n7 | n7))
                                                + 257115103 * ~(n7 & n7 ^ n7 & n7)
                                                + -1678239606 * (n7 & n7 & ~n7 ^ n7 ^ n7 ^ n7 & n7)
                                                + -1292879936 * ((n7 | n7) & (n7 ^ n7) & n7)
                                                + 150154700 * (n7 | n7 | n7)
                                                + -1521088941 * (n7 ^ (n7 | n7) ^ (n7 | n7))
                                                + 1602781680 * n7
                                                + -1425890974 * ((n7 ^ n7) & ~n7 | (n7 ^ n7) & n7 & n7)
                                                + -1472617301 * ~(n7 & n7 & n7 & n7)
                                                + -1440316942 * ~(~(n7 | n7))
                                                + -1740341179 * ~(n7 ^ n7 | n7 | n7)
                                                + -802280294 * n7;
                                             n5 = 1354206006
                                                + 631200659 * n64
                                                + 269180012 * n7
                                                + 635866637 * (~(n7 ^ n7) & ~(n7 | n7))
                                                + 1083830940 * (~n7 & (n7 ^ ~n7))
                                                + 945389706 * n7
                                                + 558537599 * (~n7 | ~n64 | n64)
                                                + 2046500860 * n64
                                                + 1417138647 * ~(n7 ^ n64 | ~n7)
                                                + 1184147984 * (n7 ^ n64)
                                                + 951157319 * ~((n7 | n7) ^ n7 & n64)
                                                + 272137195 * n7
                                                + -433117794 * ~n64;
                                             n7 = 0;
                                             continue;
                                          case 130919853:
                                             obj = new StackSlotHelper(Items.FLINT_AND_STEEL, (SwapMode)this.swapAction.atx()).LK();
                                             int n65 = n6 * 1946367679;
                                             n5 = 1698125594;
                                             n7 = -580794597;
                                             n6 = n65;
                                             continue;
                                          case 159387081:
                                             n = Managers.inventoryTracker.JS();
                                             int n66 = n6 * 1815885111;
                                             n5 = 1918887680;
                                             n7 = -743592287;
                                             n6 = n66;
                                             continue;
                                          case 182847649:
                                             ClientWorld world7 = client.world;
                                             BlockPos pos18 = entity2.getBlockPos();
                                             if (!world7.getBlockState(pos18).isReplaceable()) {
                                                break label1786;
                                             }

                                             int n67 = n6 * -1149885731;
                                             n5 = 1376256060;
                                             n7 = 170362829;
                                             n6 = n67;
                                             continue;
                                          case 189630563:
                                             RotationUtils.nI((Vec3d)obj2);
                                             int n68 = n6 * 2002861269;
                                             n5 = 1298541738;
                                             n7 = 172704855;
                                             n6 = n68;
                                             continue;
                                          case 216105581:
                                             Vec3d vec15 = client.player.getEyePos();
                                             Vec3d vec16 = (Vec3d)obj5;
                                             d = vec15.distanceTo(vec16);
                                             int n69 = n6 * 1142519097;
                                             n5 = 472017478;
                                             n7 = 366240855;
                                             n6 = n69;
                                             continue;
                                          case 313349777:
                                             if (Blink.bm()) {
                                                return;
                                             }

                                             n5 = 1794275278;
                                             n7 = 294766263;
                                             n6 = 1812211091;
                                             continue;
                                          case 365076729:
                                             n4 = Managers.inventoryTracker.JS();
                                             int n70 = n6 * -1498977637;
                                             n5 = -1074140588;
                                             n7 = -1057325260;
                                             n6 = n70;
                                             continue;
                                          case 392284679:
                                             if (d <= (Double)this.placeRange.atx()) {
                                                int n71 = n6 * -379273037;
                                                n5 = -1201919774;
                                                n7 = 2013550366;
                                                n6 = n71;
                                                continue;
                                             }
                                             break;
                                          case 497328603:
                                             BlockPos pos19 = entity2.getBlockPos();
                                             Direction direction = (Direction)obj3;
                                             obj4 = pos19.offset(direction);
                                             int n72 = n6 * 1631443633;
                                             n5 = -1436453514;
                                             n7 = -906321560;
                                             n6 = n72;
                                             continue;
                                          case 512236973:
                                             obj = this.yZ(new Box(entity2.getBlockPos()));
                                             int n73 = n6 * 1794537361;
                                             n5 = 568509312;
                                             n7 = 476278957;
                                             n6 = n73;
                                             continue;
                                          case 517359673:
                                             ((StackSlotHelper)obj).LP(n4);
                                             int n74 = n6 * 289379367;
                                             n5 = 1675844950;
                                             n7 = -2017280481;
                                             n6 = n74;
                                             continue;
                                          case 574057257:
                                             obj3 = this.zb(entity2.getPos());
                                             int n75 = n6 * -1233161341;
                                             n5 = -1524197536;
                                             n7 = -75503950;
                                             n6 = n75;
                                             continue;
                                          case 601255723:
                                             if (!(Boolean)this.always.atx()) {
                                                if ((ItemFrameEntity)obj != null) {
                                                   break label1785;
                                                }

                                                int n76 = n6 * -1691432661;
                                                n5 = 1940485284;
                                                n7 = 214450911;
                                                n6 = n76;
                                                continue;
                                             }
                                             break label1818;
                                          case 662701219:
                                             if (!(Boolean)this.rotate.atx()) {
                                                ClientPlayNetworkHandler network3 = client.getNetworkHandler();
                                                ItemFrameEntity itemFrameEntity2 = (ItemFrameEntity)obj;
                                                boolean flag2 = client.player.isSneaking();
                                                Hand hand2 = Hand.MAIN_HAND;
                                                boolean flag3 = flag2;
                                                PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket2 = PlayerInteractEntityC2SPacket.interact((Entity)itemFrameEntity2, flag3, hand2);
                                                network3.sendPacket((Packet)playerInteractEntityC2SPacket2);
                                                ((StackSlotHelper)obj3).LP(n3);
                                                if ((Boolean)this.rotate.atx()) {
                                                   RotationUtils.nK();
                                                }

                                                this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                                break label1902;
                                             }

                                             int n77 = n6 * 1034434415;
                                             n5 = 1620654634;
                                             n7 = -1303288785;
                                             n6 = n77;
                                             continue;
                                          case 731079275:
                                             BlockPos pos20 = entity2.getBlockPos();
                                             Direction direction2 = (Direction)obj2;
                                             obj3 = pos20.offset(direction2);
                                             int n78 = n6 * -1916726581;
                                             n5 = -1308429512;
                                             n7 = -1623089920;
                                             n6 = n78;
                                             continue;
                                          case 758982429:
                                             Vec3d vec17 = client.player.getEyePos();
                                             Vec3d vec18 = ((BlockPos)obj3).toCenterPos();
                                             double d16 = ((Direction)obj2).getOpposite().getVector().getX() * 0.5;
                                             double d17 = ((Direction)obj2).getOpposite().getVector().getZ() * 0.5;
                                             double d18 = ((Direction)obj2).getOpposite().getVector().getY() * 0.5;
                                             double d19 = d16;
                                             Vec3d vec19 = vec18.add(d19, d18, d17);
                                             d2 = vec17.distanceTo(vec19);
                                             int n79 = n6 * 1736950529;
                                             n5 = 133912974;
                                             n7 = -2135965631;
                                             n6 = n79;
                                             continue;
                                          case 772439593:
                                             if (!(Boolean)this.breakSetting.atx()) {
                                                if (client.player.isUsingItem()) {
                                                   return;
                                                }

                                                n5 = 1387416782;
                                                n7 = -308544483;
                                                n6 = 1413750419;
                                             } else {
                                                n5 = -1793945386;
                                                n7 = -1228836698;
                                                n6 = 1413750419;
                                             }
                                             continue;
                                          case 905086543:
                                             if (!((StackSlotHelper)obj).LL()) {
                                                break label1796;
                                             }

                                             int n80 = n6 * 68400015;
                                             n5 = 1543647064;
                                             n7 = -870537001;
                                             n6 = n80;
                                             continue;
                                          case 936158355:
                                             if (!(d3 <= (Double)this.placeRange.atx())) {
                                                break label1651;
                                             }

                                             int n81 = n6 * -1342435069;
                                             n5 = -1844651478;
                                             n7 = 992504828;
                                             n6 = n81;
                                             continue;
                                          case 945695955:
                                             ((StackSlotHelper)obj2).LO(n);
                                             int n82 = n6 * 1745265215;
                                             n5 = -1093896686;
                                             n7 = -1360751494;
                                             n6 = n82;
                                             continue;
                                          case 960678625:
                                             if (!PlacementUtils.blockPosDirectionBase.Hr((BlockPos)obj3, Direction.UP) || state.isReplaceable()) {
                                                break label1785;
                                             }

                                             if (!BlockStateUtils.Wy(state, (BlockPos)obj3)) {
                                                n = Managers.inventoryTracker.JS();
                                                ((StackSlotHelper)obj2).LO(n);
                                                PlacementUtils.Fg((BlockPos)obj3, Direction.UP, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                                ((StackSlotHelper)obj2).LP(n);
                                                this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                                break label1785;
                                             }

                                             if (!client.player.isSneaking()) {
                                                break label1785;
                                             }

                                             int n83 = n6 * -306515173;
                                             n5 = -1828680478;
                                             n7 = -1714149358;
                                             n6 = n83;
                                             continue;
                                          case 986135667:
                                             if (!((StackSlotHelper)obj).LL()) {
                                                break label1786;
                                             }

                                             int n84 = n6 * -1725267509;
                                             n5 = -1740183940;
                                             n7 = -2118871224;
                                             n6 = n84;
                                             continue;
                                          case 1033037369:
                                             obj2 = entity2.getBlockPos().down();
                                             int n85 = n6 * -516827873;
                                             n5 = -1882527638;
                                             n7 = -1160515830;
                                             n6 = n85;
                                             continue;
                                          case 1129931857:
                                             ((StackSlotHelper)obj).LO(n2);
                                             int n86 = n6 * -898878125;
                                             n5 = 901789386;
                                             n7 = -902133973;
                                             n6 = n86;
                                             continue;
                                          case 1293410507:
                                             obj2 = new StackSlotHelper(Items.PAINTING, (SwapMode)this.swapAction.atx()).LK();
                                             int n87 = n6 * 1824148165;
                                             n5 = 63436572;
                                             n7 = -1513320957;
                                             n6 = n87;
                                             continue;
                                          case 1317158321:
                                             if (!(Boolean)this.ladder.atx()) {
                                                break label1786;
                                             }

                                             int n88 = n6 * 1819601847;
                                             n5 = 1036241864;
                                             n7 = 1062958751;
                                             n6 = n88;
                                             continue;
                                          case 1326684335:
                                             if (client.player.isSneaking() && (obj3 = this.yW(((BlockPos)obj2).down())) != null) {
                                                ClientWorld world8 = client.world;
                                                BlockPos pos21 = ((BlockPos)obj2).down();
                                                if (world8.getBlockState(pos21).getBlock() instanceof ScaffoldingBlock) {
                                                   int n89 = n6 * -567564761;
                                                   n5 = -1739953834;
                                                   n7 = -223642522;
                                                   n6 = n89;
                                                   continue;
                                                }
                                             }
                                             break;
                                          case 1341312805:
                                             if ((Boolean)this.crawlingSkip.atx() && entity2.isCrawling()) {
                                                int n90 = n6 * 913389009;
                                                n5 = 1957421202;
                                                n7 = 2087235587;
                                                n6 = n90;
                                                continue;
                                             }

                                             if ((Boolean)this.inAirSkip.atx() && !entity2.isOnGround()) {
                                                break label1786;
                                             }
                                             break label1876;
                                          case 1365023633:
                                             PlacementUtils.Fg((BlockPos)obj3, Direction.UP, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                             int n91 = n6 * 1388785611;
                                             n5 = -1323832244;
                                             n7 = 1218039576;
                                             n6 = n91;
                                             continue;
                                          case 1383060961:
                                             if (!(d4 <= (Double)this.placeRange.atx())) {
                                                break label1796;
                                             }

                                             int n92 = n6 * 457987291;
                                             n5 = 1986790646;
                                             n7 = 502252593;
                                             n6 = n92;
                                             continue;
                                          case 1386789753:
                                             if ((Direction)obj2 == null) {
                                                break label1786;
                                             }

                                             int n93 = n6 * -1635431909;
                                             n5 = 1022776932;
                                             n7 = 691181585;
                                             n6 = n93;
                                             continue;
                                          case 1388651453:
                                             obj2 = new StackSlotHelper(new Predicate() {
                                                @Override
                                                public boolean test(Object tickEntitiesPreEvent) {
                                                   return AntiPhase.ze((ItemStack)tickEntitiesPreEvent);
                                                }
                                             }, (SwapMode)this.swapAction.atx()).LK();
                                             int n94 = n6 * 529372353;
                                             n5 = 631713234;
                                             n7 = -1164325317;
                                             n6 = n94;
                                             continue;
                                          case 1458850219:
                                             if ((Direction)obj4 != null) {
                                                n5 = 1609217358;
                                                n7 = 699467869;
                                                n6 = 1495066387;
                                                continue;
                                             }

                                             if ((obj3 = this.yX(((BlockPos)obj2).down())) != null) {
                                                ClientWorld world9 = client.world;
                                                BlockPos pos22 = ((BlockPos)obj2).down();
                                                if (world9.getBlockState(pos22).getBlock() instanceof ScaffoldingBlock) {
                                                   if (!client.player.isSneaking()) {
                                                      break label1782;
                                                   }

                                                   int n95 = n6 * 2063553599;
                                                   n5 = 830625438;
                                                   n7 = 1082577447;
                                                   n6 = n95;
                                                   continue;
                                                }
                                             }

                                             if (client.player.isSneaking() && (obj3 = this.yW(((BlockPos)obj2).down())) != null) {
                                                ClientWorld world10 = client.world;
                                                BlockPos pos23 = ((BlockPos)obj2).down();
                                                if (world10.getBlockState(pos23).getBlock() instanceof ScaffoldingBlock) {
                                                   break label1782;
                                                }
                                             }
                                             break;
                                          case 1512037585:
                                             break label1782;
                                          case 1562816263:
                                             int n96 = -355787274
                                                + -857997954 * ~(n7 | n7 | n7 & n7)
                                                + 2131459595 * (~(n7 ^ n7) & ~n7)
                                                + -1966073553 * (~(n7 ^ n7) & ~(~n7))
                                                + 823206751 * ~((n7 ^ n7) & (n7 | n7))
                                                + -776412145 * (~n7 | n7)
                                                + 717765603 * (~(n7 | n7) ^ (n7 ^ n7) & n7 & n7)
                                                + 1081492175 * n7
                                                + -963058011 * ((n7 | n7 | ~n7) & ~n7 & (n7 | n7))
                                                + -1043554373 * (((n7 | n7) ^ ~n7) & n7)
                                                + 1932294302 * n7
                                                + -1462212770 * n7
                                                + -845685833 * (~(n7 | n7) ^ ~n7 ^ n7 & n7);
                                             n5 = 432470172
                                                + 684825513 * n96
                                                + 863857998 * (n7 ^ n7 | ~n7 | ~(n7 & n96))
                                                + -194398381 * ~(n7 ^ n96 & n96)
                                                + -1160299120 * (n96 & n7)
                                                + 1882013418 * (n7 | n7 | n96 | n7 | (n96 ^ n96) & (n7 ^ n96))
                                                + -23781075 * n96
                                                + -1454345697 * (~n7 & (n96 ^ n96) | n7)
                                                + 508040640 * n96
                                                + -979589039 * n7
                                                + 328110684 * ~(~n7 | n7 | n7)
                                                + 357522938 * n7
                                                + 1049470420 * n96;
                                             n7 = 0;
                                             continue;
                                          case 1594661297:
                                             obj = new StackSlotHelper(Items.LADDER, (SwapMode)this.swapAction.atx()).LK();
                                             int n97 = n6 * 678600093;
                                             n5 = -1584933790;
                                             n7 = -1684761868;
                                             n6 = n97;
                                             continue;
                                          case 1619225995:
                                             Vec3d vec20 = client.player.getEyePos();
                                             Vec3d vec21 = ((BlockPos)obj4).toCenterPos();
                                             double d20 = ((Direction)obj3).getOpposite().getVector().getX() * 0.5;
                                             double d21 = ((Direction)obj3).getOpposite().getVector().getZ() * 0.5;
                                             double d22 = ((Direction)obj3).getOpposite().getVector().getY() * 0.5;
                                             double d23 = d20;
                                             Vec3d vec22 = vec21.add(d23, d22, d21);
                                             d3 = vec20.distanceTo(vec22);
                                             int n98 = n6 * -422676529;
                                             n5 = 1290782740;
                                             n7 = -1567683723;
                                             n6 = n98;
                                             continue;
                                          case 1712207317:
                                             obj5 = vec;
                                             int n99 = n6 * -83266935;
                                             n5 = 1558182752;
                                             n7 = -570812189;
                                             n6 = n99;
                                             continue;
                                          case 1714411189:
                                             BlockPos pos24 = (BlockPos)obj2;
                                             obj5 = client.world.getBlockState(pos24);
                                             int n100 = n6 * 160283891;
                                             n5 = 1527122256;
                                             n7 = 1986343857;
                                             n6 = n100;
                                             continue;
                                          case 1726456847:
                                             this.list.clear();
                                             int n101 = n6 * -2008457915;
                                             n5 = 2101221246;
                                             n7 = 1156265601;
                                             n6 = n101;
                                             continue;
                                          case 1742759645:
                                             obj2 = BoxUtils.jy(client.player.getEyePos(), ((ItemFrameEntity)obj).getBoundingBox());
                                             int n102 = n6 * 1628107461;
                                             n5 = 45952544;
                                             n7 = 1290141421;
                                             n6 = n102;
                                             continue;
                                          case 1754434637:
                                             if (!(Boolean)this.collideSkip.atx() || !CollisionUtils.aiy(entity2, entity2.getBoundingBox())) {
                                                break label1997;
                                             }

                                             int n103 = n6 * -526576503;
                                             n5 = -1100708468;
                                             n7 = 1169664384;
                                             n6 = n103;
                                             continue;
                                          case 1780178053:
                                             ((StackSlotHelper)obj).LO(n4);
                                             int n104 = n6 * -1117828193;
                                             n5 = -2143176426;
                                             n7 = 723088492;
                                             n6 = n104;
                                             continue;
                                          case 1836886183:
                                             Vec3d vec23 = ((BlockPos)obj2).down().toCenterPos();
                                             double d24 = ((Direction)obj3).getVector().getX() * 0.5;
                                             double d25 = ((Direction)obj3).getVector().getZ() * 0.5;
                                             double d26 = ((Direction)obj3).getVector().getY() * 0.5;
                                             double d27 = d24;
                                             vec = vec23.add(d27, d26, d25);
                                             int n105 = n6 * -1438856489;
                                             n5 = 103067902;
                                             n7 = 981272877;
                                             n6 = n105;
                                             continue;
                                          case 1854439755:
                                             ClientPlayNetworkHandler network4 = client.getNetworkHandler();
                                             ItemFrameEntity itemFrameEntity3 = (ItemFrameEntity)obj;
                                             boolean flag4 = client.player.isSneaking();
                                             Hand hand3 = Hand.MAIN_HAND;
                                             boolean flag5 = flag4;
                                             PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket3 = PlayerInteractEntityC2SPacket.interact((Entity)itemFrameEntity3, flag5, hand3);
                                             network4.sendPacket((Packet)playerInteractEntityC2SPacket3);
                                             int n106 = n6 * -861833565;
                                             n5 = -1827099020;
                                             n7 = 1517311390;
                                             n6 = n106;
                                             continue;
                                          case 1880093899:
                                             Vec3d vec24 = client.player.getEyePos();
                                             Vec3d vec25 = (Vec3d)obj2;
                                             if (!(vec24.distanceTo(vec25) <= (Double)this.entityRange.atx())) {
                                                break label1902;
                                             }

                                             int n107 = n6 * 1271988817;
                                             n5 = 1653566374;
                                             n7 = 1406957;
                                             n6 = n107;
                                             continue;
                                          case 1885124949:
                                             if (((StackSlotHelper)obj).LL()) {
                                                int n108 = n6 * 1420884667;
                                                n5 = -1923192638;
                                                n7 = 1186223986;
                                                n6 = n108;
                                                continue;
                                             }
                                             break;
                                          case 1905496147:
                                             obj2 = this.zb(entity2.getPos());
                                             int n109 = n6 * 473137009;
                                             n5 = -1568342328;
                                             n7 = -1406079852;
                                             n6 = n109;
                                             continue;
                                          case 1925904157:
                                             obj4 = PlacementUtils.Fk((BlockPos)obj2, 6.0);
                                             int n110 = n6 * -277263025;
                                             n5 = 904675572;
                                             n7 = 1024429377;
                                             n6 = n110;
                                             continue;
                                          case 1950854777:
                                             Vec3d vec26 = client.player.getEyePos();
                                             Vec3d vec27 = ((BlockPos)obj2).toBottomCenterPos().add(0.0, 1.0, 0.0);
                                             d4 = vec26.distanceTo(vec27);
                                             int n111 = n6 * 1858837543;
                                             n5 = -1831916864;
                                             n7 = -206273020;
                                             n6 = n111;
                                             continue;
                                          case 1965634111:
                                             return;
                                          case 2021873457:
                                             if ((Boolean)this.scaffolding.atx()) {
                                                int n112 = n6 * 1589759945;
                                                n5 = 1803189128;
                                                n7 = 1149901301;
                                                n6 = n112;
                                                continue;
                                             }
                                             break;
                                          case 2031424843:
                                             obj = new StackSlotHelper(Items.SCAFFOLDING, (SwapMode)this.swapAction.atx()).LK();
                                             int n113 = n6 * -1793145625;
                                             n5 = -1148431988;
                                             n7 = -886795956;
                                             n6 = n113;
                                             continue;
                                          case 2133322665:
                                             obj3 = new StackSlotHelper(Items.OBSIDIAN, (SwapMode)this.swapAction.atx()).LK();
                                             int n114 = n6 * 300967063;
                                             n5 = 499857886;
                                             n7 = 571072137;
                                             n6 = n114;
                                             continue;
                                          default:
                                             return;
                                       }

                                       if (!(Boolean)this.itemFrame.atx()) {
                                          break label1902;
                                       }

                                       ClientWorld world11 = client.world;
                                       BlockPos pos25 = entity2.getBlockPos();
                                       if (!world11.isAir(pos25)) {
                                          break label1902;
                                       }

                                       obj = this.yZ(new Box(entity2.getBlockPos()));
                                       if (!(Boolean)this.always.atx() && (ItemFrameEntity)obj != null) {
                                          break label1785;
                                       }
                                    }

                                    StackSlotHelper stackSlotHelper = new StackSlotHelper(new Predicate() {
                                       @Override
                                       public boolean test(Object tickEntitiesPreEvent) {
                                          return AntiPhase.ze((ItemStack)tickEntitiesPreEvent);
                                       }
                                    }, (SwapMode)this.swapAction.atx()).LK();
                                    if (stackSlotHelper.LL()) {
                                       BlockPos pos26 = entity2.getBlockPos().down();
                                       Vec3d vec28 = client.player.getEyePos();
                                       Vec3d vec29 = pos26.toBottomCenterPos().add(0.0, 1.0, 0.0);
                                       if (vec28.distanceTo(vec29) <= (Double)this.placeRange.atx()) {
                                          state = client.world.getBlockState(pos26);
                                          if (PlacementUtils.blockPosDirectionBase.Hr(pos26, Direction.UP)
                                             && !state.isReplaceable()
                                             && (!BlockStateUtils.Wy(state, pos26) || client.player.isSneaking())) {
                                             n = Managers.inventoryTracker.JS();
                                             stackSlotHelper.LO(n);
                                             PlacementUtils.Fg(pos26, Direction.UP, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                             stackSlotHelper.LP(n);
                                             this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                          }
                                       }
                                    }
                                    break label1785;
                                 }
                              }

                              if ((Boolean)this.fire.atx()) {
                                 ClientWorld world12 = client.world;
                                 BlockPos pos27 = entity2.getBlockPos();
                                 if (world12.isAir(pos27) && (!(Boolean)this.detectMining.atx() || !Managers.breakManager.Rv(entity2.getBlockPos()))) {
                                    break label2002;
                                 }
                              }
                              break label1796;
                           }

                           Vec3d vec30;
                           if ((Direction)obj4 != null) {
                              BlockPos pos28 = (BlockPos)obj2;
                              Direction direction3 = (Direction)obj4;
                              Vec3d vec31 = pos28.offset(direction3).toCenterPos();
                              double d28 = ((Direction)obj4).getOpposite().getVector().getX() * 0.5;
                              double d29 = ((Direction)obj4).getOpposite().getVector().getZ() * 0.5;
                              double d30 = ((Direction)obj4).getOpposite().getVector().getY() * 0.5;
                              double d31 = d28;
                              vec30 = vec31.add(d31, d30, d29);
                           } else {
                              Vec3d vec32 = ((BlockPos)obj2).down().toCenterPos();
                              double d32 = ((Direction)obj3).getVector().getX() * 0.5;
                              double d33 = ((Direction)obj3).getVector().getZ() * 0.5;
                              double d34 = ((Direction)obj3).getVector().getY() * 0.5;
                              double d35 = d32;
                              vec30 = vec32.add(d35, d34, d33);
                           }

                           if (client.player.getEyePos().distanceTo(vec30) <= (Double)this.placeRange.atx()) {
                              int n115 = Managers.inventoryTracker.JS();
                              ((StackSlotHelper)obj).LO(n115);
                              ClientWorld world13 = client.world;
                              BlockPos pos29 = ((BlockPos)obj2).down();
                              if (world13.getBlockState(pos29).getBlock() instanceof ScaffoldingBlock && (Direction)obj3 != null) {
                                 PlacementUtils.Fg(((BlockPos)obj2).down(), (Direction)obj3, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                 ((StackSlotHelper)obj).LP(n115);
                                 this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                              } else {
                                 PlacementUtils.Fa((BlockPos)obj2, Hand.MAIN_HAND, (Boolean)this.rotate.atx());
                                 ((StackSlotHelper)obj).LP(n115);
                                 this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                              }
                           }

                           if ((Boolean)this.itemFrame.atx()) {
                              ClientWorld world14 = client.world;
                              BlockPos pos30 = entity2.getBlockPos();
                              if (world14.isAir(pos30)) {
                                 ItemFrameEntity itemFrameEntity4 = this.yZ(new Box(entity2.getBlockPos()));
                                 if ((Boolean)this.always.atx() || itemFrameEntity4 == null) {
                                    StackSlotHelper stackSlotHelper2 = new StackSlotHelper(new Predicate() {
                                       @Override
                                       public boolean test(Object tickEntitiesPreEvent) {
                                          return AntiPhase.ze((ItemStack)tickEntitiesPreEvent);
                                       }
                                    }, (SwapMode)this.swapAction.atx()).LK();
                                    if (stackSlotHelper2.LL()) {
                                       BlockPos pos31 = entity2.getBlockPos().down();
                                       Vec3d vec33 = client.player.getEyePos();
                                       Vec3d vec34 = pos31.toBottomCenterPos().add(0.0, 1.0, 0.0);
                                       if (vec33.distanceTo(vec34) <= (Double)this.placeRange.atx()) {
                                          BlockState state2 = client.world.getBlockState(pos31);
                                          if (PlacementUtils.blockPosDirectionBase.Hr(pos31, Direction.UP)
                                             && !state2.isReplaceable()
                                             && (!BlockStateUtils.Wy(state2, pos31) || client.player.isSneaking())) {
                                             int n116 = Managers.inventoryTracker.JS();
                                             stackSlotHelper2.LO(n116);
                                             PlacementUtils.Fg(pos31, Direction.UP, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                             stackSlotHelper2.LP(n116);
                                             this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                          }
                                       }
                                    }
                                 }

                                 if ((Boolean)this.fill.atx() && itemFrameEntity4 != null && itemFrameEntity4.getHeldItemStack().isEmpty()) {
                                    Vec3d vec35 = BoxUtils.jy(client.player.getEyePos(), itemFrameEntity4.getBoundingBox());
                                    if (client.player.getEyePos().distanceTo(vec35) <= (Double)this.entityRange.atx()) {
                                       StackSlotHelper stackSlotHelper3 = new StackSlotHelper(Items.OBSIDIAN, (SwapMode)this.swapAction.atx()).LK();
                                       if (stackSlotHelper3.LL()) {
                                          int n117 = Managers.inventoryTracker.JS();
                                          stackSlotHelper3.LO(n117);
                                          if ((Boolean)this.rotate.atx()) {
                                             RotationUtils.nI(vec35);
                                          }

                                          ClientPlayNetworkHandler network5 = client.getNetworkHandler();
                                          ItemFrameEntity itemFrameEntity5 = itemFrameEntity4;
                                          boolean flag6 = client.player.isSneaking();
                                          Hand hand4 = Hand.MAIN_HAND;
                                          boolean flag7 = flag6;
                                          PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket4 = PlayerInteractEntityC2SPacket.interact((Entity)itemFrameEntity5, flag7, hand4);
                                          network5.sendPacket((Packet)playerInteractEntityC2SPacket4);
                                          stackSlotHelper3.LP(n117);
                                          if ((Boolean)this.rotate.atx()) {
                                             RotationUtils.nK();
                                          }

                                          this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                       }
                                    }
                                 }
                              }
                           }
                           break label1902;
                        }

                        if (!(Boolean)this.collideSkip.atx() || !CollisionUtils.aiy(entity2, entity2.getBoundingBox())) {
                           break label1997;
                        }
                        break label1786;
                     }

                     if ((Boolean)this.ladder.atx()) {
                        StackSlotHelper stackSlotHelper4 = new StackSlotHelper(Items.LADDER, (SwapMode)this.swapAction.atx()).LK();
                        if (stackSlotHelper4.LL()) {
                           ClientWorld world15 = client.world;
                           BlockPos pos32 = entity2.getBlockPos();
                           if (world15.getBlockState(pos32).isReplaceable()) {
                              Direction direction4 = this.zb(entity2.getPos());
                              if (direction4 != null) {
                                 BlockPos pos33 = entity2.getBlockPos().offset(direction4);
                                 Vec3d vec36 = client.player.getEyePos();
                                 Vec3d vec37 = pos33.toCenterPos();
                                 double d36 = direction4.getOpposite().getVector().getX() * 0.5;
                                 double d37 = direction4.getOpposite().getVector().getZ() * 0.5;
                                 double d38 = direction4.getOpposite().getVector().getY() * 0.5;
                                 double d39 = d36;
                                 Vec3d vec38 = vec37.add(d39, d38, d37);
                                 if (vec36.distanceTo(vec38) <= (Double)this.placeRange.atx()) {
                                    List list4 = PlacementUtils.a_;
                                    BlockPos pos34 = entity2.getBlockPos();
                                    list4.add(pos34);
                                    PlaceEcho.Mz().MB(entity2.getBlockPos());
                                    int n118 = Managers.inventoryTracker.JS();
                                    stackSlotHelper4.LO(n118);
                                    PlacementUtils.Fg(pos33, direction4.getOpposite(), Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                    stackSlotHelper4.LP(n118);
                                    this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                 }
                              }
                           }
                        }
                     }
                     break label1786;
                  }

                  if ((Boolean)this.fill.atx() && (ItemFrameEntity)obj != null && ((ItemFrameEntity)obj).getHeldItemStack().isEmpty()) {
                     Vec3d vec39 = BoxUtils.jy(client.player.getEyePos(), ((ItemFrameEntity)obj).getBoundingBox());
                     if (client.player.getEyePos().distanceTo(vec39) <= (Double)this.entityRange.atx()) {
                        StackSlotHelper stackSlotHelper5 = new StackSlotHelper(Items.OBSIDIAN, (SwapMode)this.swapAction.atx()).LK();
                        if (stackSlotHelper5.LL()) {
                           int n119 = Managers.inventoryTracker.JS();
                           stackSlotHelper5.LO(n119);
                           if ((Boolean)this.rotate.atx()) {
                              RotationUtils.nI(vec39);
                           }

                           ClientPlayNetworkHandler network6 = client.getNetworkHandler();
                           ItemFrameEntity itemFrameEntity6 = (ItemFrameEntity)obj;
                           boolean flag8 = client.player.isSneaking();
                           Hand hand5 = Hand.MAIN_HAND;
                           boolean flag9 = flag8;
                           PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket5 = PlayerInteractEntityC2SPacket.interact((Entity)itemFrameEntity6, flag9, hand5);
                           network6.sendPacket((Packet)playerInteractEntityC2SPacket5);
                           stackSlotHelper5.LP(n119);
                           if ((Boolean)this.rotate.atx()) {
                              RotationUtils.nK();
                           }

                           this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                        }
                     }
                  }
                  break label1902;
               }

               while (iterator2.hasNext()) {
                  entity2 = (Entity)iterator2.next();
                  if ((!(Boolean)this.crawlingSkip.atx() || !entity2.isCrawling())
                     && (!(Boolean)this.inAirSkip.atx() || entity2.isOnGround())
                     && (!(Boolean)this.collideSkip.atx() || !CollisionUtils.aiy(entity2, entity2.getBoundingBox()))) {
                     break label1997;
                  }
               }

               return;
            }

            StackSlotHelper stackSlotHelper6 = new StackSlotHelper(Items.FLINT_AND_STEEL, (SwapMode)this.swapAction.atx()).LK();
            if (stackSlotHelper6.LL()) {
               BlockPos pos35 = entity2.getBlockPos().down();
               Vec3d vec40 = client.player.getEyePos();
               Vec3d vec41 = pos35.toBottomCenterPos().add(0.0, 1.0, 0.0);
               if (vec40.distanceTo(vec41) <= (Double)this.placeRange.atx()) {
                  BlockState state3 = client.world.getBlockState(pos35);
                  if (PlacementUtils.blockPosDirectionBase.Hr(pos35, Direction.UP)
                     && !state3.isReplaceable()
                     && (!BlockStateUtils.Wy(state3, pos35) || client.player.isSneaking())) {
                     int n120 = Managers.inventoryTracker.JS();
                     stackSlotHelper6.LO(n120);
                     PlacementUtils.Fg(pos35, Direction.UP, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                     stackSlotHelper6.LP(n120);
                     this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                  }
               }
            }

            if ((Boolean)this.ladder.atx()) {
               stackSlotHelper6 = new StackSlotHelper(Items.LADDER, (SwapMode)this.swapAction.atx()).LK();
               if (stackSlotHelper6.LL()) {
                  ClientWorld world16 = client.world;
                  BlockPos pos36 = entity2.getBlockPos();
                  if (world16.getBlockState(pos36).isReplaceable()) {
                     Direction direction5 = this.zb(entity2.getPos());
                     if (direction5 != null) {
                        BlockPos pos37 = entity2.getBlockPos().offset(direction5);
                        Vec3d vec42 = client.player.getEyePos();
                        Vec3d vec43 = pos37.toCenterPos();
                        double d40 = direction5.getOpposite().getVector().getX() * 0.5;
                        double d41 = direction5.getOpposite().getVector().getZ() * 0.5;
                        double d42 = direction5.getOpposite().getVector().getY() * 0.5;
                        double d43 = d40;
                        Vec3d vec44 = vec43.add(d43, d42, d41);
                        if (vec42.distanceTo(vec44) <= (Double)this.placeRange.atx()) {
                           List list5 = PlacementUtils.a_;
                           BlockPos pos38 = entity2.getBlockPos();
                           list5.add(pos38);
                           PlaceEcho.Mz().MB(entity2.getBlockPos());
                           int n121 = Managers.inventoryTracker.JS();
                           stackSlotHelper6.LO(n121);
                           PlacementUtils.Fg(pos37, direction5.getOpposite(), Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                           stackSlotHelper6.LP(n121);
                           this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                        }
                     }
                  }
               }
            }

            while (iterator2.hasNext()) {
               entity2 = (Entity)iterator2.next();
               if ((!(Boolean)this.crawlingSkip.atx() || !entity2.isCrawling())
                  && (!(Boolean)this.inAirSkip.atx() || entity2.isOnGround())
                  && (!(Boolean)this.collideSkip.atx() || !CollisionUtils.aiy(entity2, entity2.getBoundingBox()))) {
                  break label1997;
               }
            }

            return;
         }

         if ((Boolean)this.paint.atx()) {
            ClientWorld world17 = client.world;
            BlockPos pos39 = entity2.getBlockPos();
            if (world17.isAir(pos39) && this.yY(new Box(entity2.getBlockPos())) == null) {
               StackSlotHelper stackSlotHelper7 = new StackSlotHelper(Items.PAINTING, (SwapMode)this.swapAction.atx()).LK();
               if (stackSlotHelper7.LL()) {
                  Direction direction6 = this.zb(entity2.getPos());
                  if (direction6 != null) {
                     BlockPos pos40 = entity2.getBlockPos().offset(direction6);
                     Vec3d vec45 = client.player.getEyePos();
                     Vec3d vec46 = pos40.toCenterPos();
                     double d44 = direction6.getOpposite().getVector().getX() * 0.5;
                     double d45 = direction6.getOpposite().getVector().getZ() * 0.5;
                     double d46 = direction6.getOpposite().getVector().getY() * 0.5;
                     double d47 = d44;
                     Vec3d vec47 = vec46.add(d47, d46, d45);
                     if (vec45.distanceTo(vec47) <= (Double)this.placeRange.atx()) {
                        int n122 = Managers.inventoryTracker.JS();
                        stackSlotHelper7.LO(n122);
                        PlacementUtils.Fg(pos40, direction6.getOpposite(), Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                        stackSlotHelper7.LP(n122);
                        this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                     }
                  }
               }
            }
         }

         if ((Boolean)this.fire.atx()) {
            ClientWorld world18 = client.world;
            BlockPos pos41 = entity2.getBlockPos();
            if (world18.isAir(pos41) && (!(Boolean)this.detectMining.atx() || !Managers.breakManager.Rv(entity2.getBlockPos()))) {
               StackSlotHelper stackSlotHelper8 = new StackSlotHelper(Items.FLINT_AND_STEEL, (SwapMode)this.swapAction.atx()).LK();
               if (stackSlotHelper8.LL()) {
                  BlockPos pos42 = entity2.getBlockPos().down();
                  Vec3d vec48 = client.player.getEyePos();
                  Vec3d vec49 = pos42.toBottomCenterPos().add(0.0, 1.0, 0.0);
                  if (vec48.distanceTo(vec49) <= (Double)this.placeRange.atx()) {
                     BlockState state4 = client.world.getBlockState(pos42);
                     if (PlacementUtils.blockPosDirectionBase.Hr(pos42, Direction.UP)
                        && !state4.isReplaceable()
                        && (!BlockStateUtils.Wy(state4, pos42) || client.player.isSneaking())) {
                        int n123 = Managers.inventoryTracker.JS();
                        stackSlotHelper8.LO(n123);
                        PlacementUtils.Fg(pos42, Direction.UP, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                        stackSlotHelper8.LP(n123);
                        this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                     }
                  }
               }

               if ((Boolean)this.ladder.atx()) {
                  stackSlotHelper8 = new StackSlotHelper(Items.LADDER, (SwapMode)this.swapAction.atx()).LK();
                  if (stackSlotHelper8.LL()) {
                     world18 = client.world;
                     BlockPos pos43 = entity2.getBlockPos();
                     if (world18.getBlockState(pos43).isReplaceable()) {
                        Direction direction7 = this.zb(entity2.getPos());
                        if (direction7 != null) {
                           BlockPos pos44 = entity2.getBlockPos().offset(direction7);
                           Vec3d vec50 = client.player.getEyePos();
                           Vec3d vec51 = pos44.toCenterPos();
                           double d48 = direction7.getOpposite().getVector().getX() * 0.5;
                           double d49 = direction7.getOpposite().getVector().getZ() * 0.5;
                           double d50 = direction7.getOpposite().getVector().getY() * 0.5;
                           double d51 = d48;
                           Vec3d vec52 = vec51.add(d51, d50, d49);
                           if (vec50.distanceTo(vec52) <= (Double)this.placeRange.atx()) {
                              List list6 = PlacementUtils.a_;
                              BlockPos pos45 = entity2.getBlockPos();
                              list6.add(pos45);
                              PlaceEcho.Mz().MB(entity2.getBlockPos());
                              int n124 = Managers.inventoryTracker.JS();
                              stackSlotHelper8.LO(n124);
                              PlacementUtils.Fg(pos44, direction7.getOpposite(), Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                              stackSlotHelper8.LP(n124);
                              this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                           }
                        }
                     }
                  }
               }

               while (iterator2.hasNext()) {
                  entity2 = (Entity)iterator2.next();
                  if ((!(Boolean)this.crawlingSkip.atx() || !entity2.isCrawling())
                     && (!(Boolean)this.inAirSkip.atx() || entity2.isOnGround())
                     && (!(Boolean)this.collideSkip.atx() || !CollisionUtils.aiy(entity2, entity2.getBoundingBox()))) {
                     break label1997;
                  }
               }

               return;
            }
         }

         if ((Boolean)this.ladder.atx()) {
            StackSlotHelper stackSlotHelper9 = new StackSlotHelper(Items.LADDER, (SwapMode)this.swapAction.atx()).LK();
            if (stackSlotHelper9.LL()) {
               ClientWorld world19 = client.world;
               BlockPos pos46 = entity2.getBlockPos();
               if (world19.getBlockState(pos46).isReplaceable()) {
                  Direction direction8 = this.zb(entity2.getPos());
                  if (direction8 != null) {
                     BlockPos pos47 = entity2.getBlockPos().offset(direction8);
                     Vec3d vec53 = client.player.getEyePos();
                     Vec3d vec54 = pos47.toCenterPos();
                     double d52 = direction8.getOpposite().getVector().getX() * 0.5;
                     double d53 = direction8.getOpposite().getVector().getZ() * 0.5;
                     double d54 = direction8.getOpposite().getVector().getY() * 0.5;
                     double d55 = d52;
                     Vec3d vec55 = vec54.add(d55, d54, d53);
                     if (vec53.distanceTo(vec55) <= (Double)this.placeRange.atx()) {
                        List list7 = PlacementUtils.a_;
                        BlockPos pos48 = entity2.getBlockPos();
                        list7.add(pos48);
                        PlaceEcho.Mz().MB(entity2.getBlockPos());
                        int n125 = Managers.inventoryTracker.JS();
                        stackSlotHelper9.LO(n125);
                        PlacementUtils.Fg(pos47, direction8.getOpposite(), Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                        stackSlotHelper9.LP(n125);
                        this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                     }
                  }
               }
            }
         }

         while (iterator2.hasNext()) {
            entity2 = (Entity)iterator2.next();
            if ((!(Boolean)this.crawlingSkip.atx() || !entity2.isCrawling())
               && (!(Boolean)this.inAirSkip.atx() || entity2.isOnGround())
               && (!(Boolean)this.collideSkip.atx() || !CollisionUtils.aiy(entity2, entity2.getBoundingBox()))) {
               break label1997;
            }
         }

         return;
      }

      label1122:
      while (true) {
         label1107:
         while (this.stopwatchImpl.afq((Number)this.delay.atx())) {
            label1878: {
               if ((Boolean)this.scaffolding.atx()) {
                  ClientWorld world20 = client.world;
                  BlockPos pos49 = entity2.getBlockPos();
                  if (world20.getBlockState(pos49).isReplaceable()) {
                     StackSlotHelper stackSlotHelper10 = new StackSlotHelper(Items.SCAFFOLDING, (SwapMode)this.swapAction.atx()).LK();
                     if (stackSlotHelper10.LL()) {
                        BlockPos pos50 = entity2.getBlockPos();
                        Direction direction9 = PlacementUtils.Fk(pos50, 6.0);
                        if (direction9 != null) {
                           Vec3d vec56;
                           if (direction9 != null) {
                              Vec3d vec57 = pos50.offset(direction9).toCenterPos();
                              double d56 = direction9.getOpposite().getVector().getX() * 0.5;
                              double d57 = direction9.getOpposite().getVector().getZ() * 0.5;
                              double d58 = direction9.getOpposite().getVector().getY() * 0.5;
                              double d59 = d56;
                              vec56 = vec57.add(d59, d58, d57);
                           } else {
                              Vec3d vec58 = pos50.down().toCenterPos();
                              double d60 = ((Direction)null).getVector().getX() * 0.5;
                              double d61 = ((Direction)null).getVector().getZ() * 0.5;
                              double d62 = ((Direction)null).getVector().getY() * 0.5;
                              double d63 = d60;
                              vec56 = vec58.add(d63, d62, d61);
                           }

                           if (client.player.getEyePos().distanceTo(vec56) <= (Double)this.placeRange.atx()) {
                              int n126 = Managers.inventoryTracker.JS();
                              stackSlotHelper10.LO(n126);
                              world20 = client.world;
                              BlockPos pos51 = pos50.down();
                              if (world20.getBlockState(pos51).getBlock() instanceof ScaffoldingBlock) {
                              }

                              PlacementUtils.Fa(pos50, Hand.MAIN_HAND, (Boolean)this.rotate.atx());
                              stackSlotHelper10.LP(n126);
                              this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                           }
                        } else {
                           label1879: {
                              Direction direction10;
                              label1838: {
                                 if ((direction10 = this.yX(pos50.down())) != null) {
                                    world20 = client.world;
                                    BlockPos pos52 = pos50.down();
                                    if (world20.getBlockState(pos52).getBlock() instanceof ScaffoldingBlock && !client.player.isSneaking()) {
                                       break label1838;
                                    }
                                 }

                                 if (!client.player.isSneaking() || (direction10 = this.yW(pos50.down())) == null) {
                                    break label1879;
                                 }

                                 world20 = client.world;
                                 BlockPos pos53 = pos50.down();
                                 if (!(world20.getBlockState(pos53).getBlock() instanceof ScaffoldingBlock)) {
                                    break label1879;
                                 }
                              }

                              Vec3d vec59;
                              if (direction9 != null) {
                                 Vec3d vec60 = pos50.offset(direction9).toCenterPos();
                                 double d64 = direction9.getOpposite().getVector().getX() * 0.5;
                                 double d65 = direction9.getOpposite().getVector().getZ() * 0.5;
                                 double d66 = direction9.getOpposite().getVector().getY() * 0.5;
                                 double d67 = d64;
                                 vec59 = vec60.add(d67, d66, d65);
                              } else {
                                 Vec3d vec61 = pos50.down().toCenterPos();
                                 double d68 = direction10.getVector().getX() * 0.5;
                                 double d69 = direction10.getVector().getZ() * 0.5;
                                 double d70 = direction10.getVector().getY() * 0.5;
                                 double d71 = d68;
                                 vec59 = vec61.add(d71, d70, d69);
                              }

                              if (client.player.getEyePos().distanceTo(vec59) <= (Double)this.placeRange.atx()) {
                                 int n127 = Managers.inventoryTracker.JS();
                                 stackSlotHelper10.LO(n127);
                                 world20 = client.world;
                                 BlockPos pos54 = pos50.down();
                                 if (world20.getBlockState(pos54).getBlock() instanceof ScaffoldingBlock && direction10 != null) {
                                    PlacementUtils.Fg(pos50.down(), direction10, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                    stackSlotHelper10.LP(n127);
                                    this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                 } else {
                                    PlacementUtils.Fa(pos50, Hand.MAIN_HAND, (Boolean)this.rotate.atx());
                                    stackSlotHelper10.LP(n127);
                                    this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                 }
                              }

                              if ((Boolean)this.itemFrame.atx()) {
                                 world20 = client.world;
                                 BlockPos pos55 = entity2.getBlockPos();
                                 if (world20.isAir(pos55)) {
                                    ItemFrameEntity itemFrameEntity7 = this.yZ(new Box(entity2.getBlockPos()));
                                    if ((Boolean)this.always.atx() || itemFrameEntity7 == null) {
                                       StackSlotHelper stackSlotHelper11 = new StackSlotHelper(new Predicate() {
                                          @Override
                                          public boolean test(Object tickEntitiesPreEvent) {
                                             return AntiPhase.ze((ItemStack)tickEntitiesPreEvent);
                                          }
                                       }, (SwapMode)this.swapAction.atx()).LK();
                                       if (stackSlotHelper11.LL()) {
                                          BlockPos pos56 = entity2.getBlockPos().down();
                                          Vec3d vec62 = client.player.getEyePos();
                                          Vec3d vec63 = pos56.toBottomCenterPos().add(0.0, 1.0, 0.0);
                                          if (vec62.distanceTo(vec63) <= (Double)this.placeRange.atx()) {
                                             BlockState state5 = client.world.getBlockState(pos56);
                                             if (PlacementUtils.blockPosDirectionBase.Hr(pos56, Direction.UP)
                                                && !state5.isReplaceable()
                                                && (!BlockStateUtils.Wy(state5, pos56) || client.player.isSneaking())) {
                                                int n128 = Managers.inventoryTracker.JS();
                                                stackSlotHelper11.LO(n128);
                                                PlacementUtils.Fg(pos56, Direction.UP, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                                stackSlotHelper11.LP(n128);
                                                this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                             }
                                          }
                                       }
                                    }

                                    if ((Boolean)this.fill.atx() && itemFrameEntity7 != null && itemFrameEntity7.getHeldItemStack().isEmpty()) {
                                       Vec3d vec64 = BoxUtils.jy(client.player.getEyePos(), itemFrameEntity7.getBoundingBox());
                                       if (client.player.getEyePos().distanceTo(vec64) <= (Double)this.entityRange.atx()) {
                                          StackSlotHelper stackSlotHelper12 = new StackSlotHelper(Items.OBSIDIAN, (SwapMode)this.swapAction.atx()).LK();
                                          if (stackSlotHelper12.LL()) {
                                             int n129 = Managers.inventoryTracker.JS();
                                             stackSlotHelper12.LO(n129);
                                             if ((Boolean)this.rotate.atx()) {
                                                RotationUtils.nI(vec64);
                                             }

                                             ClientPlayNetworkHandler network7 = client.getNetworkHandler();
                                             ItemFrameEntity itemFrameEntity8 = itemFrameEntity7;
                                             boolean flag10 = client.player.isSneaking();
                                             Hand hand6 = Hand.MAIN_HAND;
                                             boolean flag11 = flag10;
                                             PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket6 = PlayerInteractEntityC2SPacket.interact((Entity)itemFrameEntity8, flag11, hand6);
                                             network7.sendPacket((Packet)playerInteractEntityC2SPacket6);
                                             stackSlotHelper12.LP(n129);
                                             if ((Boolean)this.rotate.atx()) {
                                                RotationUtils.nK();
                                             }

                                             this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                                          }
                                       }
                                    }
                                 }
                              }
                              break label1878;
                           }
                        }
                     }
                  }
               }

               if ((Boolean)this.itemFrame.atx()) {
                  ClientWorld world21 = client.world;
                  BlockPos pos57 = entity2.getBlockPos();
                  if (world21.isAir(pos57)) {
                     ItemFrameEntity itemFrameEntity9 = this.yZ(new Box(entity2.getBlockPos()));
                     if ((Boolean)this.always.atx() || itemFrameEntity9 == null) {
                        StackSlotHelper stackSlotHelper13 = new StackSlotHelper(new Predicate() {
                           @Override
                           public boolean test(Object tickEntitiesPreEvent) {
                              return AntiPhase.ze((ItemStack)tickEntitiesPreEvent);
                           }
                        }, (SwapMode)this.swapAction.atx()).LK();
                        if (stackSlotHelper13.LL()) {
                           BlockPos pos58 = entity2.getBlockPos().down();
                           Vec3d vec65 = client.player.getEyePos();
                           Vec3d vec66 = pos58.toBottomCenterPos().add(0.0, 1.0, 0.0);
                           if (vec65.distanceTo(vec66) <= (Double)this.placeRange.atx()) {
                              BlockState state6 = client.world.getBlockState(pos58);
                              if (PlacementUtils.blockPosDirectionBase.Hr(pos58, Direction.UP)
                                 && !state6.isReplaceable()
                                 && (!BlockStateUtils.Wy(state6, pos58) || client.player.isSneaking())) {
                                 int n130 = Managers.inventoryTracker.JS();
                                 stackSlotHelper13.LO(n130);
                                 PlacementUtils.Fg(pos58, Direction.UP, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                 stackSlotHelper13.LP(n130);
                                 this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                              }
                           }
                        }
                     }

                     if ((Boolean)this.fill.atx() && itemFrameEntity9 != null && itemFrameEntity9.getHeldItemStack().isEmpty()) {
                        Vec3d vec67 = BoxUtils.jy(client.player.getEyePos(), itemFrameEntity9.getBoundingBox());
                        if (client.player.getEyePos().distanceTo(vec67) <= (Double)this.entityRange.atx()) {
                           StackSlotHelper stackSlotHelper14 = new StackSlotHelper(Items.OBSIDIAN, (SwapMode)this.swapAction.atx()).LK();
                           if (stackSlotHelper14.LL()) {
                              int n131 = Managers.inventoryTracker.JS();
                              stackSlotHelper14.LO(n131);
                              if ((Boolean)this.rotate.atx()) {
                                 RotationUtils.nI(vec67);
                              }

                              ClientPlayNetworkHandler network8 = client.getNetworkHandler();
                              ItemFrameEntity itemFrameEntity10 = itemFrameEntity9;
                              boolean flag12 = client.player.isSneaking();
                              Hand hand7 = Hand.MAIN_HAND;
                              boolean flag13 = flag12;
                              PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket7 = PlayerInteractEntityC2SPacket.interact((Entity)itemFrameEntity10, flag13, hand7);
                              network8.sendPacket((Packet)playerInteractEntityC2SPacket7);
                              stackSlotHelper14.LP(n131);
                              if ((Boolean)this.rotate.atx()) {
                                 RotationUtils.nK();
                              }

                              this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                           }
                        }
                     }
                  }
               }
            }

            if ((Boolean)this.paint.atx()) {
               ClientWorld world22 = client.world;
               BlockPos pos59 = entity2.getBlockPos();
               if (world22.isAir(pos59) && this.yY(new Box(entity2.getBlockPos())) == null) {
                  StackSlotHelper stackSlotHelper15 = new StackSlotHelper(Items.PAINTING, (SwapMode)this.swapAction.atx()).LK();
                  if (stackSlotHelper15.LL()) {
                     Direction direction11 = this.zb(entity2.getPos());
                     if (direction11 != null) {
                        BlockPos pos60 = entity2.getBlockPos().offset(direction11);
                        Vec3d vec68 = client.player.getEyePos();
                        Vec3d vec69 = pos60.toCenterPos();
                        double d72 = direction11.getOpposite().getVector().getX() * 0.5;
                        double d73 = direction11.getOpposite().getVector().getZ() * 0.5;
                        double d74 = direction11.getOpposite().getVector().getY() * 0.5;
                        double d75 = d72;
                        Vec3d vec70 = vec69.add(d75, d74, d73);
                        if (vec68.distanceTo(vec70) <= (Double)this.placeRange.atx()) {
                           int n132 = Managers.inventoryTracker.JS();
                           stackSlotHelper15.LO(n132);
                           PlacementUtils.Fg(pos60, direction11.getOpposite(), Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                           stackSlotHelper15.LP(n132);
                           this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                        }
                     }
                  }
               }
            }

            if ((Boolean)this.fire.atx()) {
               ClientWorld world23 = client.world;
               BlockPos pos61 = entity2.getBlockPos();
               if (world23.isAir(pos61) && (!(Boolean)this.detectMining.atx() || !Managers.breakManager.Rv(entity2.getBlockPos()))) {
                  StackSlotHelper stackSlotHelper16 = new StackSlotHelper(Items.FLINT_AND_STEEL, (SwapMode)this.swapAction.atx()).LK();
                  if (stackSlotHelper16.LL()) {
                     BlockPos pos62 = entity2.getBlockPos().down();
                     Vec3d vec71 = client.player.getEyePos();
                     Vec3d vec72 = pos62.toBottomCenterPos().add(0.0, 1.0, 0.0);
                     if (vec71.distanceTo(vec72) <= (Double)this.placeRange.atx()) {
                        BlockState state7 = client.world.getBlockState(pos62);
                        if (PlacementUtils.blockPosDirectionBase.Hr(pos62, Direction.UP)
                           && !state7.isReplaceable()
                           && (!BlockStateUtils.Wy(state7, pos62) || client.player.isSneaking())) {
                           int n133 = Managers.inventoryTracker.JS();
                           stackSlotHelper16.LO(n133);
                           PlacementUtils.Fg(pos62, Direction.UP, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                           stackSlotHelper16.LP(n133);
                           this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                        }
                     }
                  }

                  if ((Boolean)this.ladder.atx()) {
                     stackSlotHelper16 = new StackSlotHelper(Items.LADDER, (SwapMode)this.swapAction.atx()).LK();
                     if (stackSlotHelper16.LL()) {
                        world23 = client.world;
                        BlockPos pos63 = entity2.getBlockPos();
                        if (world23.getBlockState(pos63).isReplaceable()) {
                           Direction direction12 = this.zb(entity2.getPos());
                           if (direction12 != null) {
                              BlockPos pos64 = entity2.getBlockPos().offset(direction12);
                              Vec3d vec73 = client.player.getEyePos();
                              Vec3d vec74 = pos64.toCenterPos();
                              double d76 = direction12.getOpposite().getVector().getX() * 0.5;
                              double d77 = direction12.getOpposite().getVector().getZ() * 0.5;
                              double d78 = direction12.getOpposite().getVector().getY() * 0.5;
                              double d79 = d76;
                              Vec3d vec75 = vec74.add(d79, d78, d77);
                              if (vec73.distanceTo(vec75) <= (Double)this.placeRange.atx()) {
                                 List list8 = PlacementUtils.a_;
                                 BlockPos pos65 = entity2.getBlockPos();
                                 list8.add(pos65);
                                 PlaceEcho.Mz().MB(entity2.getBlockPos());
                                 int n134 = Managers.inventoryTracker.JS();
                                 stackSlotHelper16.LO(n134);
                                 PlacementUtils.Fg(pos64, direction12.getOpposite(), Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                                 stackSlotHelper16.LP(n134);
                                 this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                              }
                           }
                        }
                     }
                  }

                  while (iterator2.hasNext()) {
                     entity2 = (Entity)iterator2.next();
                     if ((!(Boolean)this.crawlingSkip.atx() || !entity2.isCrawling())
                        && (!(Boolean)this.inAirSkip.atx() || entity2.isOnGround())
                        && (!(Boolean)this.collideSkip.atx() || !CollisionUtils.aiy(entity2, entity2.getBoundingBox()))) {
                        continue label1107;
                     }
                  }

                  return;
               }
            }

            if ((Boolean)this.ladder.atx()) {
               StackSlotHelper stackSlotHelper17 = new StackSlotHelper(Items.LADDER, (SwapMode)this.swapAction.atx()).LK();
               if (stackSlotHelper17.LL()) {
                  ClientWorld world24 = client.world;
                  BlockPos pos66 = entity2.getBlockPos();
                  if (world24.getBlockState(pos66).isReplaceable()) {
                     Direction direction13 = this.zb(entity2.getPos());
                     if (direction13 != null) {
                        BlockPos pos67 = entity2.getBlockPos().offset(direction13);
                        Vec3d vec76 = client.player.getEyePos();
                        Vec3d vec77 = pos67.toCenterPos();
                        double d80 = direction13.getOpposite().getVector().getX() * 0.5;
                        double d81 = direction13.getOpposite().getVector().getZ() * 0.5;
                        double d82 = direction13.getOpposite().getVector().getY() * 0.5;
                        double d83 = d80;
                        Vec3d vec78 = vec77.add(d83, d82, d81);
                        if (vec76.distanceTo(vec78) <= (Double)this.placeRange.atx()) {
                           List list9 = PlacementUtils.a_;
                           BlockPos pos68 = entity2.getBlockPos();
                           list9.add(pos68);
                           PlaceEcho.Mz().MB(entity2.getBlockPos());
                           int n135 = Managers.inventoryTracker.JS();
                           stackSlotHelper17.LO(n135);
                           PlacementUtils.Fg(pos67, direction13.getOpposite(), Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                           stackSlotHelper17.LP(n135);
                           this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                        }
                     }
                  }
               }
            }

            while (iterator2.hasNext()) {
               entity2 = (Entity)iterator2.next();
               if ((!(Boolean)this.crawlingSkip.atx() || !entity2.isCrawling())
                  && (!(Boolean)this.inAirSkip.atx() || entity2.isOnGround())
                  && (!(Boolean)this.collideSkip.atx() || !CollisionUtils.aiy(entity2, entity2.getBoundingBox()))) {
                  continue label1107;
               }
            }

            return;
         }

         while (iterator2.hasNext()) {
            entity2 = (Entity)iterator2.next();
            if ((!(Boolean)this.crawlingSkip.atx() || !entity2.isCrawling())
               && (!(Boolean)this.inAirSkip.atx() || entity2.isOnGround())
               && (!(Boolean)this.collideSkip.atx() || !CollisionUtils.aiy(entity2, entity2.getBoundingBox()))) {
               continue label1122;
            }
         }

         return;
      }
   }

   private Direction yW(BlockPos pos) {
      return PlacementUtils.blockPosDirectionBase.Hr(pos, Direction.UP) ? Direction.UP : null;
   }

   private Direction yX(BlockPos pos) {
      Direction[] directions = Direction.values();
      int n = directions.length;

      for (int n2 = 0; n2 < n; n2++) {
         Direction direction = directions[n2];
         if (direction != Direction.UP && PlacementUtils.blockPosDirectionBase.Hr(pos, direction)) {
            return direction;
         }
      }

      return null;
   }

   private PaintingEntity yY(Box box) {
      ClientWorld temp = client.world;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object box) {
            return AntiPhase.zd((Entity)box);
         }
      };
      Iterator iterator2 = temp.getOtherEntities((Entity)null, box, predicate).iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (!entity2.isRemoved() && entity2.getFacing() == Direction.UP) {
            return (PaintingEntity)entity2;
         }
      }

      return null;
   }

   private ItemFrameEntity yZ(Box box) {
      ClientWorld temp = client.world;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object box) {
            return AntiPhase.zc((Entity)box);
         }
      };
      Iterator iterator2 = temp.getOtherEntities((Entity)null, box, predicate).iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (!entity2.isRemoved() && entity2.getFacing() == Direction.UP) {
            return (ItemFrameEntity)entity2;
         }
      }

      return null;
   }

   boolean za(Entity entity2) {
      ClientPlayerEntity player2 = client.player;
      if (!entity2.equals(player2)) {
         Entity entity3 = client.cameraEntity;
         if (!entity2.equals(entity3)) {
            if ((!(entity2 instanceof LivingEntity) || !((LivingEntity)entity2).isDead()) && entity2.isAlive()) {
               Vec3d temp = client.player.getEyePos();
               Vec3d vec = entity2.getPos();
               if (temp.distanceTo(vec) > ((Float)this.targetRange.atx()).floatValue()) {
                  return false;
               }

               if (entity2 instanceof PlayerEntity) {
                  if (((PlayerEntity)entity2).isCreative()) {
                     return false;
                  }

                  boolean flag;
                  if (!Managers.friendManager.awr((PlayerEntity)entity2)) {
                     flag = true;
                  } else {
                     flag = false;
                  }

                  return flag;
               }

               return false;
            }

            return false;
         }
      }

      return false;
   }

   private Direction zb(Vec3d vec) {
      Mutable mutable = new Mutable(vec.getX(), vec.getY(), vec.getZ());
      Vec3d vec2 = mutable.toBottomCenterPos();
      double d = Double.MAX_VALUE;
      Direction direction = null;
      Direction[] directions = Direction.values();
      int n = directions.length;

      for (int n2 = 0; n2 < n; n2++) {
         Direction direction2 = directions[n2];
         if (direction2 != Direction.UP && direction2 != Direction.DOWN) {
            BlockPos pos = mutable.offset(direction2);
            if (PlacementUtils.blockPosDirectionBase.Hr(pos, direction2.getOpposite())) {
               if ((Boolean)this.onlyHard.atx()) {
                  if (!BlockUtils.TR(pos)) {
                     continue;
                  }
               } else {
                  BlockState state = client.world.getBlockState(pos);
                  if (state.isReplaceable() || BlockStateUtils.Wy(state, pos) && !client.player.isSneaking()) {
                     continue;
                  }
               }

               Box box = yT(direction2, mutable);
               if (box != null && !yU(box)) {
                  Vec3d temp = vec2;
                  double d2 = direction2.getOffsetZ() * 0.4F;
                  double d3 = direction2.getOffsetX() * 0.4F;
                  double d4 = temp.add(d3, 0.0, d2).distanceTo(vec);
                  if (d4 < d) {
                     d = d4;
                     direction = direction2;
                  }
               }
            }
         }
      }

      return direction;
   }

   static boolean zc(Entity entity2) {
      return entity2 instanceof ItemFrameEntity;
   }

   static boolean zd(Entity entity2) {
      return entity2 instanceof PaintingEntity;
   }

   static boolean ze(ItemStack stack) {
      return stack.getItem() instanceof ItemFrameItem;
   }

   static boolean zf(Entity entity2) {
      boolean flag;
      if (!entity2.isSpectator()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
