package pw.hachimi.client.module.player;

import java.util.Iterator;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.handler.EntityHandler;
import pw.hachimi.client.model.world.EntityInteractType;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoMount extends ClientModule {
   Setting range = this.registerSetting(new NumberSetting("Range", "The range to mount entities", 0.1F, 4.0F, 6.0F));
   Setting forceMount = this.registerSetting(new BooleanSetting("ForceMount", "Forces the mounting without packets", false));
   Setting horse = this.registerSetting(new BooleanSetting("Horse", "Mounts horses", true));
   Setting donkey = this.registerSetting(new BooleanSetting("Donkey", "Mounts donkeys", true));
   Setting mule = this.registerSetting(new BooleanSetting("Mule", "Mounts mules", true));
   Setting llama = this.registerSetting(new BooleanSetting("Llama", "Mounts llamas", false));
   public static boolean ec;

   public AutoMount() {
      super("AutoMount", "Mounts nearby entities", ModuleCategory.PLAYER);
   }

   @EventListener
   public void Ck(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (!client.player.isRiding()) {
         Iterator iterator2 = client.world.getEntities().iterator();

         while (iterator2.hasNext()) {
            Entity entity2 = (Entity)iterator2.next();
            Vec3d temp = client.player.getEyePos();
            Vec3d vec = entity2.getPos();
            if (!(temp.distanceTo(vec) > ((Float)this.range.atx()).floatValue()) && this.Cm(entity2)) {
               Hand hand = Hand.MAIN_HAND;
               ClientPlayerEntity player2 = client.player;
               client.interactionManager.interactEntity((PlayerEntity)player2, entity2, hand);
               return;
            }
         }
      }
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      Packet packet2 = sendImmediatelyEvent.getPacket();
      if (packet2 instanceof EntityHandler
         && (Boolean)this.forceMount.atx()
         && ((EntityHandler)packet2).Qd() == EntityInteractType.INTERACT_AT
         && this.Cm(((EntityHandler)packet2).Qc())) {
         sendImmediatelyEvent.aim();
      }
   }

   private boolean Cm(Entity entity2) {
      boolean flag;
      if ((!(Boolean)this.horse.atx() || (!(entity2 instanceof HorseEntity) || ((HorseEntity)entity2).isBaby()) && !(entity2 instanceof SkeletonHorseEntity))
         && (!(Boolean)this.donkey.atx() || !(entity2 instanceof DonkeyEntity))
         && (!(Boolean)this.mule.atx() || !(entity2 instanceof MuleEntity))
         && (!(Boolean)this.llama.atx() || !(entity2 instanceof LlamaEntity) || ((LlamaEntity)entity2).isBaby())) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }
}
