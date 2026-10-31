package pw.hachimi.eventbus;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;
import pw.hachimi.client.api.UnidentifiedInterface10;
import pw.hachimi.client.api.UnidentifiedInterface4;
import pw.hachimi.eventbus.annotation.EventListener;

public class EventBusImplHelper implements UnidentifiedInterface4 {
   private static Method method;

   static {
      try {
         method = MethodHandles.class.getMethod("privateLookupIn", Class.class, Lookup.class);
      } catch (NoSuchMethodException noSuchMethodException) {
         throw new ExceptionInInitializerError(noSuchMethodException);
      }
   }

   private final Class classValue;
   private final boolean c;
   private final int d;
   private Consumer consumer;

   public EventBusImplHelper(UnidentifiedInterface10 unidentifiedInterface10, Class classValue2, Object obj, Method method2) {
      this.classValue = method2.getParameters()[0].getType();
      this.c = Modifier.isStatic(method2.getModifiers());
      this.d = method2.getAnnotation(EventListener.class).priority();

      try {
         String name = method2.getName();
         Lookup lookup = unidentifiedInterface10.create(method, classValue2);
         MethodType methodType = MethodType.methodType(void.class, method2.getParameters()[0].getType());
         MethodHandle methodHandle;
         MethodType methodType2;
         if (this.c) {
            methodHandle = lookup.findStatic(classValue2, name, methodType);
            methodType2 = MethodType.methodType(Consumer.class);
         } else {
            methodHandle = lookup.findVirtual(classValue2, name, methodType);
            methodType2 = MethodType.methodType(Consumer.class, classValue2);
         }

         CallSite callSite = LambdaMetafactory.metafactory(lookup, "accept", methodType2, MethodType.methodType(void.class, Object.class), methodHandle, methodType);
         MethodHandle target = callSite.getTarget();
         if (this.c) {
            this.consumer = (Consumer)target.invoke();
         } else {
            this.consumer = (Consumer)target.invoke(obj);
         }
      } catch (Throwable throwable) {
         throw new IllegalStateException(throwable);
      }
   }

   @Override
   public void cv(Object obj) {
      this.consumer.accept(obj);
   }

   @Override
   public Class cw() {
      return this.classValue;
   }

   @Override
   public int cx() {
      return this.d;
   }

   @Override
   public boolean cy() {
      return this.c;
   }
}
