package pw.hachimi.client.utils.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.Mc;

public class BoxUtils {
   public static final Direction[] a_ = Direction.values();
   public static final Direction[] b = new Direction[]{Direction.NORTH, Direction.WEST, Direction.EAST, Direction.SOUTH};
   public static final Direction[] c = new Direction[]{Direction.NORTH, Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.DOWN};
   public static final Direction[] d = new Direction[]{Direction.NORTH, Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.UP};
   private static final long a = 0L;

   public static Vec3d jx(Vec3d vec, double d2, double d3, double d4, double d5, double d6, double d7) {
      double d8 = Math.clamp(vec.x, d2, d5);
      double d9 = Math.clamp(vec.y, d3, d6);
      double d10 = Math.clamp(vec.z, d4, d7);
      return new Vec3d(d8, d9, d10);
   }

   public static Vec3d jy(Vec3d vec, Box box) {
      return jx(vec, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
   }

   public static Vec3d jz(Box box) {
      return jy(Mc.client.player.getEyePos(), box);
   }

   public static Box jA(List list) {
      int n;
      int n2;
      int n3;
      int n4;
      int n5;
      int n6;
      Iterator iterator2;
      label176: {
         BlockPos pos;
         label175: {
            label180: {
               label173: {
                  label186: {
                     pos = null;
                     n = Integer.MAX_VALUE;
                     n2 = Integer.MAX_VALUE;
                     n3 = Integer.MAX_VALUE;
                     n4 = Integer.MIN_VALUE;
                     n5 = Integer.MIN_VALUE;
                     n6 = Integer.MIN_VALUE;
                     iterator2 = list.iterator();
                     int n7;
                     int n8;
                     int n9;
                     if (!iterator2.hasNext()) {
                        n7 = -10401114;
                        n9 = 552398787;
                        n8 = 1262199539;
                     } else {
                        label185: {
                           label169: {
                              pos = (BlockPos)iterator2.next();
                              if (pos.getX() < Integer.MAX_VALUE) {
                                 n = pos.getX();
                                 if (pos.getY() >= Integer.MAX_VALUE) {
                                    break label169;
                                 }
                              } else if (pos.getY() >= Integer.MAX_VALUE) {
                                 if (pos.getZ() < Integer.MAX_VALUE) {
                                    break label186;
                                 }

                                 if (pos.getX() > Integer.MIN_VALUE) {
                                    n4 = pos.getX();
                                    if (pos.getY() > Integer.MIN_VALUE) {
                                       n5 = pos.getY();
                                       if (pos.getZ() <= Integer.MIN_VALUE) {
                                          break label176;
                                       }
                                       break label175;
                                    }

                                    if (pos.getZ() > Integer.MIN_VALUE) {
                                       break label175;
                                    }

                                    n8 = 1456734941;
                                    int n10 = -1355202534
                                       + 1160455643 * ((-1452939472 & -1452939472 | ~-1452939472) ^ -1452939472 & -1452939472 ^ -1452939472 & -1452939472)
                                       + 681154870 * -1452939472
                                       + -824351560 * (-1452939472 | -1452939472 & -1452939472 | -1452939472 | -1452939472)
                                       + 448974550 * -1452939472
                                       + -1834131993 * (-1452939472 & -1452939472 ^ -1452939472 ^ -1452939472 | -1452939472 ^ -1452939472 & -1452939472)
                                       + -1601948415 * (-1452939472 ^ -1452939472 ^ ~-1452939472 | -1452939472 & -1452939472 & (-1452939472 ^ -1452939472))
                                       + -829047528 * ((~-1452939472 | -1452939472) ^ -1452939472 & -1452939472 & -1452939472 & -1452939472)
                                       + -942107284 * (~(-1452939472 & -1452939472) ^ (-1452939472 | -1452939472 | -1452939472))
                                       + 902641810 * ~((-1452939472 | -1452939472) & (-1452939472 | -1452939472))
                                       + 448474400 * ~((-1452939472 | -1452939472) ^ ~-1452939472)
                                       + -828476485 * ((-1452939472 | ~-1452939472) & ((-1452939472 | -1452939472) ^ -1452939472))
                                       + 355146880 * ((-1452939472 | -1452939472 | ~-1452939472) ^ (-1452939472 ^ -1452939472) & -1452939472);
                                    n7 = 262333059
                                       + -825420461 * (n10 | -1452939472 & n10 & (n10 | n10))
                                       + 119624224 * (~(n10 & -1452939472) | -1452939472)
                                       + -1623312196 * (-1452939472 ^ n10 | n10 ^ n10 | n10 & (n10 ^ -1452939472))
                                       + 1051304145 * (n10 & n10 & n10 & (~-1452939472 | -1452939472 | -1452939472))
                                       + -909530212 * ((n10 | -1452939472) & n10 & (~n10 ^ -1452939472 ^ n10))
                                       + 1277906240 * (~n10 & (-1452939472 | n10 | -1452939472 & -1452939472))
                                       + -421198984 * (-1452939472 & n10 ^ (n10 | -1452939472) ^ ~n10 ^ (-1452939472 | n10))
                                       + -1658403284 * ((n10 | ~-1452939472) ^ ~(n10 ^ n10))
                                       + -1812279725 * ~(~(-1452939472 | n10))
                                       + -2065274371 * ((-1452939472 | ~-1452939472) ^ (-1452939472 | n10) & n10 & n10)
                                       + 478878330 * ~-1452939472
                                       + 1632765095 * (~(n10 & -1452939472) & n10);
                                    n9 = 0;
                                 } else {
                                    n7 = 1637451306;
                                    n9 = -134249936;
                                    n8 = 1791642949;
                                 }
                                 break label185;
                              }

                              n2 = pos.getY();
                           }

                           if (pos.getZ() >= Integer.MAX_VALUE) {
                              break label173;
                           }
                           break label186;
                        }
                     }

                     while (true) {
                        int temp = n8;
                        if (n9 == 0) {
                           n8 *= 1524323687;
                        }

                        switch (n7 ^ temp) {
                           case -2143022005:
                              if (pos.getZ() <= n6) {
                                 break label176;
                              }

                              int n11 = n8 * -1023232499;
                              n7 = -2072734966;
                              n9 = -1879600684;
                              n8 = n11;
                              continue;
                           case -1896949115:
                              n6 = pos.getZ();
                              int n12 = n8 * 419237955;
                              n7 = -1611305082;
                              n9 = -721465456;
                              n8 = n12;
                              continue;
                           case -1886213047:
                              if (pos.getZ() >= n3) {
                                 break label173;
                              }

                              int n13 = n8 * -239166863;
                              n7 = -897463758;
                              n9 = -1376229981;
                              n8 = n13;
                              continue;
                           case -1750736431:
                              if (!iterator2.hasNext()) {
                                 return new Box(n, n2, n3, n4, n5, n6);
                              }

                              pos = (BlockPos)iterator2.next();
                              int n14 = n8 * -420402869;
                              n7 = 1444921270;
                              n9 = -719817236;
                              n8 = n14;
                              continue;
                           case -1328343799:
                              n5 = pos.getY();
                              int n15 = n8 * 1542431615;
                              n7 = 2059766368;
                              n9 = 696646952;
                              n8 = n15;
                              continue;
                           case -1269110699:
                              int n16 = 504694661
                                 + -1633354677 * (n9 | n9 | n9 | n9 | n9 ^ n9)
                                 + -1218416986 * (~n9 & (n9 | n9) & n9)
                                 + 1514162334 * (~(n9 & n9) & (n9 & n9 | n9))
                                 + -216773019 * ~(~n9 & (n9 | n9))
                                 + -616057682 * ((n9 | n9) & n9 & n9 & (n9 | n9))
                                 + -2045554937 * ~(~n9 | n9 ^ n9)
                                 + -2117107477 * (n9 & (~n9 | n9 ^ n9))
                                 + 1302718478 * ((n9 | n9 | n9 ^ n9) ^ ~n9 ^ n9 ^ n9)
                                 + -775280157 * (((n9 | n9) ^ n9 & n9) & (n9 ^ n9 ^ (n9 | n9)))
                                 + -1966004930 * (n9 & ~(n9 & n9))
                                 + 1920539609 * ~(n9 & n9 ^ n9 & n9)
                                 + -1897096884 * ~(n9 & (n9 ^ n9));
                              n7 = -701055170
                                 + -497294481 * (~(~n9) ^ n9)
                                 + 2029071678 * ((n16 | n9 | n9 & n9) & n9 & n16 & n9 & n9)
                                 + -161553643 * ~(n16 & n9 | n9 | n16)
                                 + -50504961 * n16
                                 + 1936681715 * ((n16 & n9 ^ n9) & (n16 ^ n9 & n16))
                                 + -326253687 * (n16 & n9 ^ n16 | n9)
                                 + -1976687622 * ((n9 & n9 | n9) ^ (n9 | n16) ^ (n16 | n16))
                                 + -52577509 * (n16 ^ n9 & n16 | n9 ^ n16 | n16 & n16)
                                 + -570595441 * (~(~n16) | ~(n9 ^ n16))
                                 + 267782515 * (~n9 & (n16 | n9) & n16)
                                 + 568716345 * (n16 & n16 & n9 & n9)
                                 + -353317887 * n9;
                              n9 = 0;
                              continue;
                           case -1211119045:
                              return new Box(n, n2, n3, n4, n5, n6);
                           case -1117433135:
                              if (pos.getX() <= n4) {
                                 if (pos.getY() > n5) {
                                    n5 = pos.getY();
                                 }
                                 break label180;
                              }

                              int n17 = n8 * -1769601527;
                              n7 = -1794969184;
                              n9 = 2078593126;
                              n8 = n17;
                              continue;
                           case -1100436645:
                              n = Integer.MAX_VALUE;
                              int n18 = n8 * -1725744035;
                              n7 = -533408440;
                              n9 = -1583952905;
                              n8 = n18;
                              continue;
                           case -812128171:
                              break label176;
                           case -312083073:
                              int n19 = -1355202534
                                 + 1160455643 * ((n9 & n9 | ~n9) ^ n9 & n9 ^ n9 & n9)
                                 + 681154870 * n9
                                 + -824351560 * (n9 | n9 & n9 | n9 | n9)
                                 + 448974550 * n9
                                 + -1834131993 * (n9 & n9 ^ n9 ^ n9 | n9 ^ n9 & n9)
                                 + -1601948415 * (n9 ^ n9 ^ ~n9 | n9 & n9 & (n9 ^ n9))
                                 + -829047528 * ((~n9 | n9) ^ n9 & n9 & n9 & n9)
                                 + -942107284 * (~(n9 & n9) ^ (n9 | n9 | n9))
                                 + 902641810 * ~((n9 | n9) & (n9 | n9))
                                 + 448474400 * ~((n9 | n9) ^ ~n9)
                                 + -828476485 * ((n9 | ~n9) & ((n9 | n9) ^ n9))
                                 + 355146880 * ((n9 | n9 | ~n9) ^ (n9 ^ n9) & n9);
                              n7 = 262333059
                                 + -825420461 * (n19 | n9 & n19 & (n19 | n19))
                                 + 119624224 * (~(n19 & n9) | n9)
                                 + -1623312196 * (n9 ^ n19 | n19 ^ n19 | n19 & (n19 ^ n9))
                                 + 1051304145 * (n19 & n19 & n19 & (~n9 | n9 | n9))
                                 + -909530212 * ((n19 | n9) & n19 & (~n19 ^ n9 ^ n19))
                                 + 1277906240 * (~n19 & (n9 | n19 | n9 & n9))
                                 + -421198984 * (n9 & n19 ^ (n19 | n9) ^ ~n19 ^ (n9 | n19))
                                 + -1658403284 * ((n19 | ~n9) ^ ~(n19 ^ n19))
                                 + -1812279725 * ~(~(n9 | n19))
                                 + -2065274371 * ((n9 | ~n9) ^ (n9 | n19) & n19 & n19)
                                 + 478878330 * ~n9
                                 + 1632765095 * (~(n19 & n9) & n19);
                              n9 = 0;
                              continue;
                           case 84296263:
                              iterator2 = list.iterator();
                              int n20 = n8 * 173020509;
                              n7 = -1145962590;
                              n9 = -1452939472;
                              n8 = n20;
                              continue;
                           case 154980495:
                              n3 = pos.getZ();
                              int n21 = n8 * 1906550865;
                              n7 = -492921452;
                              n9 = 842246555;
                              n8 = n21;
                              continue;
                           case 190040943:
                              int n22 = -1100015386
                                 + -171671998 * (~(n9 | n9) | n9 & (n9 ^ n9))
                                 + -233893708 * ~(n9 & (n9 ^ n9))
                                 + 240299735 * ((n9 ^ n9 | n9 ^ n9) ^ (n9 ^ n9 | n9 & n9))
                                 + 1253548646 * (n9 & n9 ^ n9 & n9 ^ n9)
                                 + 1732107517 * (~(~n9) & (~n9 ^ ~n9))
                                 + 148070012 * n9
                                 + -814367544 * n9
                                 + 1851160659 * n9
                                 + -1412321275 * (~(n9 ^ n9) | n9 | n9 | n9)
                                 + -509807338 * ~(~(~n9))
                                 + 934776452 * n9
                                 + -770666252 * ~(n9 & n9 ^ n9);
                              n7 = 1212768108
                                 + 1835515351 * n9
                                 + -1892009086 * ((n22 ^ n9) & n9 | n22)
                                 + -813225959 * (n9 & n9 & n22 ^ ~n22)
                                 + 529676287 * (~n22 & (n22 | n9) ^ n22)
                                 + 1489829583 * ((n22 | n9 & n22) ^ (n9 ^ n9 | n9))
                                 + 1300180947 * n22
                                 + -1963012134 * (n22 ^ (n9 ^ n9 | n22 ^ n9))
                                 + -443969320 * (~(n9 & n22) | ~(~n22))
                                 + -1261016203 * n22
                                 + -979887486 * (n22 | n22)
                                 + -1864414972 * (n9 & n9 & n22 & n22)
                                 + -1637468328 * (n9 & ~(n9 & n9));
                              n9 = 0;
                              continue;
                           case 481777691:
                              n3 = Integer.MAX_VALUE;
                              int n23 = n8 * -260444195;
                              n7 = 1651076128;
                              n9 = -260164334;
                              n8 = n23;
                              continue;
                           case 587277713:
                              n = pos.getX();
                              int n24 = n8 * -1991740617;
                              n7 = -883190920;
                              n9 = -616186483;
                              n8 = n24;
                              continue;
                           case 651411205:
                              n6 = Integer.MIN_VALUE;
                              int n25 = n8 * 2086472275;
                              n7 = 2123126200;
                              n9 = -1430428172;
                              n8 = n25;
                              continue;
                           case 676362665:
                              n2 = Integer.MAX_VALUE;
                              int n26 = n8 * 95605639;
                              n7 = -1972063918;
                              n9 = 324548202;
                              n8 = n26;
                              continue;
                           case 1083147151:
                              n2 = pos.getY();
                              int n27 = n8 * -890347663;
                              n7 = -883807804;
                              n9 = 894100179;
                              n8 = n27;
                              continue;
                           case 1499085365:
                              n5 = Integer.MIN_VALUE;
                              int n28 = n8 * -1765420629;
                              n7 = 1454970340;
                              n9 = -1625495310;
                              n8 = n28;
                              continue;
                           case 1752217681:
                              if (pos.getX() < n) {
                                 int n29 = n8 * -1409631389;
                                 n7 = -1240858894;
                                 n9 = 274800640;
                                 n8 = n29;
                                 continue;
                              }

                              if (pos.getY() < n2) {
                                 n2 = pos.getY();
                              }
                              break;
                           case 1804991133:
                              n4 = pos.getX();
                              int n30 = n8 * -1433003283;
                              n7 = -560945904;
                              n9 = -910747553;
                              n8 = n30;
                              continue;
                           case 1939974689:
                              n4 = Integer.MIN_VALUE;
                              int n31 = n8 * -502813057;
                              n7 = 1876442584;
                              n9 = -578456090;
                              n8 = n31;
                              continue;
                           case 2106221015:
                              if (pos.getY() < n2) {
                                 int n32 = n8 * -2015825643;
                                 n7 = -1032452766;
                                 n9 = -281157133;
                                 n8 = n32;
                                 continue;
                              }
                              break;
                           case 2127967497:
                              if (pos.getY() <= n5) {
                                 break label180;
                              }

                              int temp2 = n8 * 246402189;
                              n7 = -2092581096;
                              n9 = -1226083502;
                              n8 = temp2;
                              continue;
                           default:
                              return null;
                        }

                        if (pos.getZ() < n3) {
                           n3 = pos.getZ();
                        }
                        break label173;
                     }
                  }

                  n3 = pos.getZ();
               }

               if (pos.getX() > n4) {
                  n4 = pos.getX();
               }

               if (pos.getY() > n5) {
                  n5 = pos.getY();
               }

               if (pos.getZ() > n6) {
                  n6 = pos.getZ();
               }
               break label176;
            }

            if (pos.getZ() > n6) {
               n6 = pos.getZ();
            }
            break label176;
         }

         n6 = pos.getZ();
      }

      while (iterator2.hasNext()) {
         BlockPos pos2 = (BlockPos)iterator2.next();
         if (pos2.getX() < n) {
            n = pos2.getX();
         }

         if (pos2.getY() < n2) {
            n2 = pos2.getY();
         }

         if (pos2.getZ() < n3) {
            n3 = pos2.getZ();
         }

         if (pos2.getX() > n4) {
            n4 = pos2.getX();
         }

         if (pos2.getY() > n5) {
            n5 = pos2.getY();
         }

         if (pos2.getZ() > n6) {
            n6 = pos2.getZ();
         }
      }

      return new Box(n, n2, n3, n4, n5, n6);
   }

   public static boolean jB(Box box, BlockPos pos) {
      Stream temp = jD(box, pos).stream();
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object pos) {
            return BlockUtils.TP((BlockPos)pos);
         }
      };
      return temp.anyMatch(predicate);
   }

   public static Direction jC(BlockPos pos, LivingEntity living) {
      if (Math.abs(living.getX() - (pos.getX() + 0.5)) < 2.0 && Math.abs(living.getZ() - (pos.getZ() + 0.5)) < 2.0) {
         EntityPose entityPose = living.getPose();
         double d2 = living.getY() + living.getEyeHeight(entityPose);
         if (d2 - pos.getY() > 2.0) {
            return Direction.UP;
         }

         if (pos.getY() - d2 > 0.0) {
            return Direction.DOWN;
         }
      }

      return living.getHorizontalFacing().getOpposite();
   }

   public static List jD(Box box, BlockPos pos) {
      ArrayList arrayList = new ArrayList();

      for (int n = (int)Math.floor(box.minX); n < Math.ceil(box.maxX); n++) {
         for (int n2 = (int)Math.floor(box.minZ); n2 < Math.ceil(box.maxZ); n2++) {
            ArrayList temp = arrayList;
            BlockPos pos2 = new BlockPos(n, pos.getY(), n2);
            temp.add(pos2);
         }
      }

      return arrayList;
   }

   public static List jE(Box box) {
      ArrayList arrayList = new ArrayList();

      for (int n = (int)Math.floor(box.minX); n < Math.ceil(box.maxX); n++) {
         for (int n2 = (int)Math.floor(box.minY); n2 < Math.ceil(box.maxY); n2++) {
            for (int n3 = (int)Math.floor(box.minZ); n3 < Math.ceil(box.maxZ); n3++) {
               ArrayList temp = arrayList;
               BlockPos pos = new BlockPos(n, n2, n3);
               temp.add(pos);
            }
         }
      }

      return arrayList;
   }

   public static List jF(Box box) {
      ArrayList arrayList = new ArrayList();
      int n = MathHelper.floor(box.minX);
      int n2 = MathHelper.floor(box.minY);
      int n3 = MathHelper.floor(box.minZ);
      int n4 = MathHelper.floor(box.maxX);
      int n5 = MathHelper.floor(box.maxY);
      int n6 = MathHelper.floor(box.maxZ);

      for (int n7 = n; n7 <= n4; n7++) {
         for (int n8 = n2; n8 <= n5; n8++) {
            for (int n9 = n3; n9 <= n6; n9++) {
               ArrayList temp = arrayList;
               BlockPos pos = new BlockPos(n7, n8, n9);
               temp.add(pos);
            }
         }
      }

      return arrayList;
   }
}
