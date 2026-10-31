package pw.hachimi.client.module.misc;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import pw.hachimi.client.event.player.UpdateDeathEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.AutoEZBotCustomMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.setting.StringSetting;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoEZ extends ClientModule {
   private static final String a = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
   public final List list = List.of(
      new String[]{
         "呐呐~杂鱼哥哥不会这样就被捉弄的不会说话了吧♡",
         "嘻嘻~杂鱼哥哥不会以为竖个大拇哥就能欺负我了吧~不会吧♡不会吧♡",
         "杂鱼哥哥怎么可能欺负得了别人呢~只能欺负自己哦♡~",
         "哥哥真是好欺负啊♡嘻嘻~",
         "哎♡~杂鱼说话就是无趣唉~",
         "吼？杂鱼酱一天天这么悠闲的吗？果然是废柴杂鱼♡嘻嘻",
         "嗯~难道杂鱼酱脑子里只知道被小女孩辱骂吗？真是又弱又笨诶♡~嘻嘻",
         "不是我想笑！是你的回复太好笑了才让我忍不住的！要负起责任来啊杂鱼！",
         "杂鱼杂鱼连这种小事都做不到吗？",
         "呐呐~杂鱼哥哥发这个是想教育我吗~嘻嘻~怎么可能啊♡",
         "什么嘛~废柴哥哥会想这种事情啊~唔呃",
         "把你肮脏的目光拿开啦~很恶心哦♡",
         "咱的期待就是被你这样的笨蛋破坏了~♡"
      }
   );
   public final List list2 = List.of(
      new String[]{
         "鼠标明天到，触摸板打的",
         "转人工",
         "收徒",
         "不收徒",
         "有真人吗",
         "墨镜上车",
         "接单",
         "征婚",
         "4399?",
         "暂时不考虑打职业",
         "bot?",
         "叫你家大人来打",
         "假肢上门安装",
         "浪费我的网费",
         "不收残疾人",
         "下课",
         "自己找差距",
         "不接代",
         "这样的治好了也流口水",
         "人机?",
         "人机怎么调难度啊",
         "领养",
         "纳亲",
         "正视差距",
         "近亲繁殖?",
         "我玩的是新手教程?",
         "下次记得晚点玩",
         "随便玩玩,不带妹",
         "扣1上车"
      }
   );
   Setting type = this.registerSetting(new EnumSetting("Type", "Ez meesage type", AutoEZBotCustomMode.Bot, AutoEZBotCustomMode.values()));
   Setting message = this.registerSetting(createStringSetting("Message", "Custom ez message", this::isMessageVisible));
   Setting range = this.registerSetting(new NumberSetting("Range", "Ez enemies within range", 0.0F, 10.0F, 30.0F));
   Setting random = this.registerSetting(new NumberSetting("Random", "Add random characters after the message", 0, 3, 20));
   Random random2_1 = new Random();

   public AutoEZ() {
      super("AutoEZ", "Say ez when enemy died", ModuleCategory.MISC);
   }

   @EventListener
   public void mk(UpdateDeathEvent updateDeathEvent) {
      LivingEntity living = updateDeathEvent.getLiving();
      if (living instanceof PlayerEntity && (PlayerEntity)living != client.player && !Managers.friendManager.awr((PlayerEntity)living)) {
         if ((Float)this.range.atx() > 0.0F && client.player.distanceTo((Entity)living) > (Float)this.range.atx()) {
            return;
         }

         String str = this.ml((Integer)this.random.atx());
         if (!str.isEmpty()) {
            str = " " + str;
         }

         switch ((AutoEZBotCustomMode)this.type.atx()) {
            case Bot:
               ClientPlayNetworkHandler network2 = client.getNetworkHandler();
               List list3 = this.list2;
               Random random2 = this.random2_1;
               int n = this.list2.size() - 1;
               int n2 = random2.nextInt(n);
               String str2 = (String)list3.get(n2) + " " + ((PlayerEntity)living).getName().getString() + str;
               network2.sendChatMessage(str2);
               break;
            case Custom:
               ClientPlayNetworkHandler network3 = client.getNetworkHandler();
               String str3 = (String)this.message.atx();
               String str4 = ((PlayerEntity)living).getName().getString();
               String str5 = str3.replaceAll("%player%", str4) + str;
               network3.sendChatMessage(str5);
               break;
            case Mesugaki:
               ClientPlayNetworkHandler temp = client.getNetworkHandler();
               List temp2 = this.list;
               Random temp3 = this.random2_1;
               int n3 = this.list.size() - 1;
               int n4 = temp3.nextInt(n3);
               String str6 = (String)temp2.get(n4) + " " + ((PlayerEntity)living).getName().getString() + str;
               temp.sendChatMessage(str6);
         }
      }
   }

   private String ml(int n) {
      StringBuilder stringBuilder = new StringBuilder(n);

      for (int n2 = 0; n2 < n; n2++) {
         Random temp = this.random2_1;
         int n3 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length();
         int n4 = temp.nextInt(n3);
         StringBuilder stringBuilder2 = stringBuilder;
         char c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(n4);
         stringBuilder2.append(c);
      }

      return stringBuilder.toString();
   }

   private Boolean isMessageVisible() {
      boolean flag;
      if (this.type.atx() == AutoEZBotCustomMode.Custom) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static StringSetting createStringSetting(String str, String str2, Supplier supplier) {
      return new StringSetting(str, str2, "EZ %player%", supplier);
   }
}
