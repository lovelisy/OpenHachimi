package pw.hachimi.client.module.combat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TippedArrowItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.movement.UnidentifiedInterface14;
import pw.hachimi.client.module.Module5;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.eventbus.annotation.EventListener;

public class SelfBow extends Module5 {
   private static SelfBow INSTANCE;
   private final Set set = new HashSet();
   private int b;

   public SelfBow() {
      super("SelfBow", "Shoots player with beneficial tipped arrows", ModuleCategory.COMBAT, 755);
      INSTANCE = this;
   }

   public static SelfBow aiZ() {
      return INSTANCE;
   }

   @Override
   public void awe() {
      if (client.options != null) {
         ((UnidentifiedInterface14)client.options.useKey).WK();
         this.set.clear();
      }
   }

   @EventListener
   public void aja(TickEntitiesPreEvent tickEntitiesPreEvent) {
      int n = 0;

      label135:
      while (true) {
         PlayerInventory temp = client.player.getInventory();
         int n2 = n;
         ItemStack stack = temp.getStack(n2);
         if (!stack.isEmpty() && stack.getItem() instanceof TippedArrowItem) {
            ComponentMap componentMap = stack.getComponents();
            ComponentType componentType = DataComponentTypes.POTION_CONTENTS;
            if (componentMap.contains(componentType)) {
               ComponentMap componentMap2 = stack.getComponents();
               ComponentType componentType2 = DataComponentTypes.POTION_CONTENTS;
               Iterator iterator2 = ((List)((PotionContentsComponent)componentMap2.get(componentType2)).getEffects()).iterator();

               while (iterator2.hasNext()) {
                  StatusEffectInstance statusEffectInstance = (StatusEffectInstance)iterator2.next();
                  if (((StatusEffect)statusEffectInstance.getEffectType().value()).isBeneficial() && !this.set.contains(statusEffectInstance)) {
                     int n3;
                     StatusEffectInstance statusEffectInstance2;
                     label122:
                     while (true) {
                        n3 = n;
                        statusEffectInstance2 = statusEffectInstance;

                        while (true) {
                           if (n3 != -1) {
                              break label122;
                           }

                           do {
                              if (++n >= 36) {
                                 break label122;
                              }

                              stack = client.player.getInventory().getStack(n);
                           } while (stack.isEmpty() || !(stack.getItem() instanceof TippedArrowItem));

                           ComponentMap componentMap3 = stack.getComponents();
                           ComponentType componentType3 = DataComponentTypes.POTION_CONTENTS;
                           if (componentMap3.contains(componentType3)) {
                              ComponentMap componentMap4 = stack.getComponents();
                              componentType2 = DataComponentTypes.POTION_CONTENTS;
                              iterator2 = ((List)((PotionContentsComponent)componentMap4.get(componentType2)).getEffects()).iterator();

                              while (iterator2.hasNext()) {
                                 statusEffectInstance = (StatusEffectInstance)iterator2.next();
                                 if (((StatusEffect)statusEffectInstance.getEffectType().value()).isBeneficial() && !this.set.contains(statusEffectInstance)) {
                                    continue label122;
                                 }
                              }
                           }
                        }
                     }

                     n = -1;
                     int n4 = 0;

                     do {
                        temp = client.player.getInventory();
                        int n5 = n4;
                        ItemStack stack2 = temp.getStack(n5);
                        if (!stack2.isEmpty() && stack2.getItem() == Items.BOW) {
                           n = n4;
                           break;
                        }
                     } while (++n4 < 9);

                     float f = BowItem.getPullProgress(this.b);
                     if (client.player.getMainHandStack().getItem() == Items.BOW && n != -1 && n3 != -1) {
                        this.ajM(client.player.getYaw(), -90.0F);
                        if (n3 != 9) {
                           if (client.player.currentScreenHandler.getCursorStack().getItem() != Items.TIPPED_ARROW) {
                              int n6 = n3;
                              ClientPlayerInteractionManager interaction = client.interactionManager;
                              int n7;
                              ClientPlayerInteractionManager interaction2;
                              if (n6 < 9) {
                                 ClientPlayerInteractionManager interaction3 = interaction;
                                 n7 = n3 + 36;
                                 interaction2 = interaction3;
                              } else {
                                 ClientPlayerInteractionManager interaction4 = interaction;
                                 n7 = n3;
                                 interaction2 = interaction4;
                              }

                              ClientPlayerInteractionManager interaction5 = interaction2;
                              ClientPlayerEntity player2 = client.player;
                              SlotActionType slotActionType = SlotActionType.PICKUP;
                              interaction5.clickSlot(0, n7, 0, slotActionType, (PlayerEntity)player2);
                           }

                           if (client.player.currentScreenHandler.getCursorStack().getItem() == Items.TIPPED_ARROW) {
                              ClientPlayerEntity player3 = client.player;
                              SlotActionType slotActionType2 = SlotActionType.PICKUP;
                              client.interactionManager.clickSlot(0, 9, 0, slotActionType2, (PlayerEntity)player3);
                           }

                           if (!client.player.currentScreenHandler.getCursorStack().isEmpty()) {
                              int n8 = n3;
                              ClientPlayerInteractionManager interaction6 = client.interactionManager;
                              ClientPlayerInteractionManager interaction7;
                              int n9;
                              if (n8 < 9) {
                                 ClientPlayerInteractionManager interaction8 = interaction6;
                                 n9 = n3 + 36;
                                 interaction7 = interaction8;
                              } else {
                                 ClientPlayerInteractionManager interaction9 = interaction6;
                                 n9 = n3;
                                 interaction7 = interaction9;
                              }

                              ClientPlayerInteractionManager interaction10 = interaction7;
                              ClientPlayerEntity player4 = client.player;
                              SlotActionType slotActionType3 = SlotActionType.PICKUP;
                              interaction10.clickSlot(0, n9, 0, slotActionType3, (PlayerEntity)player4);
                           }
                        }

                        if (f >= 0.15F) {
                           StatusEffectInstance statusEffectInstance3 = statusEffectInstance2;
                           this.set.add(statusEffectInstance3);
                           client.options.useKey.setPressed(false);
                           Managers.serverManager.iO('\uf560', new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
                           client.player.stopUsingItem();
                           this.b = 0;
                        } else {
                           client.options.useKey.setPressed(true);
                           this.b++;
                        }

                        return;
                     }
                     break label135;
                  }
               }
            }
         }

         if (++n >= 36) {
            n = -1;

            for (int n10 = 0; n10 < 9; n10++) {
               temp = client.player.getInventory();
               int n11 = n10;
               ItemStack stack3 = temp.getStack(n11);
               if (!stack3.isEmpty() && stack3.getItem() == Items.BOW) {
                  n = n10;
                  break;
               }
            }

            BowItem.getPullProgress(this.b);
            if (client.player.getMainHandStack().getItem() == Items.BOW && n != -1) {
            }
            break;
         }
      }

      this.awc();
   }
}
