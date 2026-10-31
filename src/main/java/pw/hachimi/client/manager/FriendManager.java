package pw.hachimi.client.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import pw.hachimi.client.Mc;
import pw.hachimi.client.model.config.UnidentifiedOption2;
import pw.hachimi.client.module.ColorHelper;
import pw.hachimi.client.utils.chat.ChatUtils;

public class FriendManager {
   private final ConcurrentMap concurrentMap = new ConcurrentHashMap();

   public boolean awp(String str, UnidentifiedOption2 unidentifiedOption2) {
      boolean flag;
      if (this.concurrentMap.get(str) == unidentifiedOption2) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean awq(String str) {
      return !ColorHelper.wx().wz() ? false : this.awp(str, UnidentifiedOption2.a_);
   }

   public boolean awr(PlayerEntity player2) {
      return !ColorHelper.wx().wz() ? false : this.awp(player2.getGameProfile().getName(), UnidentifiedOption2.a_);
   }

   public boolean aws(String str) {
      return this.awp(str, UnidentifiedOption2.a_);
   }

   public boolean awt(Text text2) {
      if (!ColorHelper.wx().wz()) {
         return false;
      }

      boolean flag;
      if (text2 != null && this.awp(text2.getString(), UnidentifiedOption2.a_)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public void awu(String str, UnidentifiedOption2 unidentifiedOption2) {
      if (Mc.client.player != null) {
         String str2 = Mc.client.player.getDisplayName().getString();
         if (str.equals(str2)) {
            return;
         }
      }

      if ((UnidentifiedOption2)this.concurrentMap.get(str) != null) {
         this.concurrentMap.replace(str, unidentifiedOption2);
      } else {
         this.concurrentMap.put(str, unidentifiedOption2);
      }
   }

   public void awv(String str) {
      this.awu(str, UnidentifiedOption2.a_);
      if (ColorHelper.wx().wA() && Mc.client.world != null) {
         Iterator iterator2 = Mc.client.player.networkHandler.getPlayerList().iterator();

         while (iterator2.hasNext()) {
            String[] strs = ((PlayerListEntry)iterator2.next()).getProfile().getName().split(" ");
            int n = strs.length;

            for (int n2 = 0; n2 < n; n2++) {
               if (strs[n2].equals(str)) {
                  ChatUtils.UI("msg " + str + " I just added you as a friend on Hachimi!");
                  break;
               }
            }
         }
      }
   }

   public void aww(Text text2) {
      this.awv(text2.getString());
   }

   public UnidentifiedOption2 awx(String str) {
      return (UnidentifiedOption2)this.concurrentMap.remove(str);
   }

   public UnidentifiedOption2 awy(Text text2) {
      ConcurrentMap temp = this.concurrentMap;
      String str = text2.getString();
      return (UnidentifiedOption2)temp.remove(str);
   }

   public Collection awz(UnidentifiedOption2 unidentifiedOption2) {
      ArrayList arrayList = new ArrayList();
      Iterator iterator2 = this.concurrentMap.entrySet().iterator();

      while (iterator2.hasNext()) {
         Entry entry = (Entry)iterator2.next();
         if (entry.getValue() == unidentifiedOption2) {
            ArrayList temp = arrayList;
            String str = (String)entry.getKey();
            temp.add(str);
         }
      }

      return arrayList;
   }

   public Collection awA() {
      return this.awz(UnidentifiedOption2.a_);
   }
}
