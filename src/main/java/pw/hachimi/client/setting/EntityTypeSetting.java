package pw.hachimi.client.setting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.UnknownEvent3;
import pw.hachimi.eventbus.EventBusImpl;

public class EntityTypeSetting extends Setting {
   public EntityTypeSetting(String str, String str2, EntityType... value2) {
      super(str, str2, new ArrayList<>(Arrays.asList(value2)));
   }

   public void mf(EntityType entityType) {
      UnknownEvent3 unknownEvent3 = new UnknownEvent3(this);
      unknownEvent3.aqo(EventPhase.PRE);
      EventBusImpl.INSTANCE.rC(unknownEvent3);
      ((List)this.atx()).add(entityType);
      unknownEvent3.aqo(EventPhase.POST);
      EventBusImpl.INSTANCE.rC(unknownEvent3);
   }

   public void mg(EntityType entityType) {
      UnknownEvent3 unknownEvent3 = new UnknownEvent3(this);
      unknownEvent3.aqo(EventPhase.PRE);
      EventBusImpl.INSTANCE.rC(unknownEvent3);
      ((List)this.atx()).remove(entityType);
      unknownEvent3.aqo(EventPhase.POST);
      EventBusImpl.INSTANCE.rC(unknownEvent3);
   }

   public void mh() {
      UnknownEvent3 unknownEvent3 = new UnknownEvent3(this);
      unknownEvent3.aqo(EventPhase.PRE);
      EventBusImpl.INSTANCE.rC(unknownEvent3);
      ((List)this.atx()).clear();
      unknownEvent3.aqo(EventPhase.POST);
      EventBusImpl.INSTANCE.rC(unknownEvent3);
   }

   public boolean mi(Object obj) {
      return obj instanceof EntityType && ((List)this.atx()).contains(obj);
   }

   @Override
   public JsonObject LT(int n, int n2, char c) {
      JsonObject jsonObject = super.LT(25681, 44222, '\u218e');
      JsonArray jsonArray = new JsonArray();
      Iterator iterator2 = ((List)this.atx()).iterator();

      while (iterator2.hasNext()) {
         Object obj = iterator2.next();
         Identifier id = Registries.ENTITY_TYPE.getId((EntityType<?>)obj);
         jsonArray.add(id.toString());
      }

      jsonObject.add("value", jsonArray);
      return jsonObject;
   }

   public List mj(JsonObject jsonObject) {
      if (!jsonObject.has("value")) {
         return null;
      }

      JsonElement jsonElement = jsonObject.get("value");
      ArrayList arrayList = new ArrayList();
      Iterator iterator2 = jsonElement.getAsJsonArray().iterator();

      while (iterator2.hasNext()) {
         String str = ((JsonElement)iterator2.next()).getAsString();
         DefaultedRegistry temp = Registries.ENTITY_TYPE;
         Identifier id = Identifier.of(str);
         Object obj = temp.get(id);
         arrayList.add(obj);
      }

      return arrayList;
   }

   @Override
   public Object LU(JsonObject jsonObject) {
      return this.mj(jsonObject);
   }
}
