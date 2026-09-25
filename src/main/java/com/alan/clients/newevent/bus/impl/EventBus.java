package com.alan.clients.newevent.bus.impl;

import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.bus.Bus;
import com.alan.clients.newevent.impl.other.GameEvent;
import com.alan.clients.newevent.impl.other.ServerJoinEvent;
import com.alan.clients.newevent.impl.other.ServerKickEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.newevent.impl.other.BackendS2CEvent;
import com.alan.clients.newevent.impl.render.RenderGuiEvent;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import lombok.Generated;

public final class EventBus<Event>
implements InstanceAccess,
Bus<Event> {
    private final Map<Type, List<CallSite<Event>>> callSiteMap;
    private final Map<Type, List<Listener<Event>>> listenerCache;
    private final ConcurrentLinkedQueue<Function<Event, Boolean>> iX = new ConcurrentLinkedQueue();
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    public EventBus() {
        this.callSiteMap = new HashMap<Type, List<CallSite<Event>>>();
        this.listenerCache = new HashMap<Type, List<Listener<Event>>>();
    }

    @Override
    public void b(Object object) {
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                EventLink eventLink = field.getAnnotation(EventLink.class);
                if (eventLink == null) continue;
                Type type = ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                try {
                    Listener listener = (Listener)LOOKUP.unreflectGetter(field).invokeWithArguments(object);
                    byte by2 = eventLink.value();
                    CallSite callSite3 = new CallSite(object, listener, by2);
                    if (this.callSiteMap.containsKey(type)) {
                        List<CallSite<Event>> list = this.callSiteMap.get(type);
                        list.add(callSite3);
                        list.sort((callSite, callSite2) -> callSite2.priority - callSite.priority);
                        continue;
                    }
                    ArrayList arrayList = new ArrayList(1);
                    arrayList.add(callSite3);
                    this.callSiteMap.put(type, arrayList);
                }
                catch (Throwable throwable) {
                    return;
                }
            }
            this.populateListenerCache();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void populateListenerCache() {
        Map<Type, List<CallSite<Event>>> map = this.callSiteMap;
        Map<Type, List<Listener<Event>>> map2 = this.listenerCache;
        for (Type type : map.keySet()) {
            List<CallSite<Event>> list = map.get(type);
            int size = list.size();
            ArrayList arrayList = new ArrayList(size);
            for (int i2 = 0; i2 < size; ++i2) {
                arrayList.add(list.get(i2).listener);
            }
            map2.put(type, arrayList);
        }
    }

    @Override
    public void c(Object object) {
        Iterator<List<CallSite<Event>>> iterator = this.callSiteMap.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().removeIf(callSite -> {
                if (callSite.owner != object) return false;
                return true;
            });
        }
        this.populateListenerCache();
    }

    @Override
    public void d(Event event) {
        try {
            if (!(EventBus.aEg.theWorld != null && aEg.getNetHandler() != null && (EventBus.aEg.getNetHandler().doneLoadingTerrain || event instanceof PacketSendEvent) || event instanceof BackendS2CEvent || event instanceof RenderGuiEvent || event instanceof ServerKickEvent || event instanceof GameEvent || event instanceof WorldChangeEvent || event instanceof ServerJoinEvent)) {
                return;
            }
            List list = this.listenerCache.getOrDefault(event.getClass(), Collections.emptyList());
            int n2 = 0;
            int size = list.size();
            while (n2 < size) {
                ((Listener)list.get(n2++)).call(event);
            }
            if (!this.iX.isEmpty()) {
                this.iX.removeIf(function -> (Boolean)function.apply(event));
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public void a(Function<Event, Boolean> function) {
        this.iX.add(function);
    }

    public void handle(Event event, Object ... objectArray) {
        try {
            System.out.println(objectArray.getClass().getDeclaredFields().length);
            for (Field field : objectArray.getClass().getDeclaredFields()) {
                if (field.getAnnotation(EventLink.class) == null) continue;
                Type type = ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Listener unused0 = (Listener)LOOKUP.unreflectGetter(field).invokeWithArguments(objectArray);
                System.out.println("Name: " + type.getTypeName());
                System.out.println("Event: " + event.getClass().getSimpleName());
            }
            return;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return;
        }
    }

    @Generated
    public Map<Type, List<CallSite<Event>>> getCallSiteMap() {
        return this.callSiteMap;
    }

    @Generated
    public Map<Type, List<Listener<Event>>> getListenerCache() {
        return this.listenerCache;
    }

    static class CallSite<Event> {
        final Object owner;
        final Listener<Event> listener;
        final byte priority;

        public CallSite(Object object, Listener<Event> listener, byte priority) {
            this.owner = object;
            this.listener = listener;
            this.priority = priority;
        }
    }
}
