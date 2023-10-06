package team.starworld.shark.event.bus;

import lombok.Getter;
import lombok.SneakyThrows;
import team.starworld.shark.event.Event;

import java.util.*;
import java.util.function.Consumer;

public class EventBus <E extends Event> {

    private final @Getter Map <Class <?>, List <EventCallback <?>>> listeners = new HashMap <> ();
    private final @Getter Map <Class <?>, List <EventCallback <?>>> onceListeners = new HashMap <> ();
    private final @Getter List <EventCallback <E>> allListeners = new ArrayList <> ();


    @SuppressWarnings("DuplicatedCode")
    @SneakyThrows
    public <T extends E> EventCallback <T> on (Class <T> type, EventCallback <T> callback) {
        if (!listeners.containsKey(type)) listeners.put(type, new ArrayList <> ());
        listeners.get(type).add(callback);
        return callback;
    }

    public <T extends E> EventCallback <T> on (Class <T> type, Consumer <T> callback) {
        return on(type, ((event, ignored) -> callback.accept(event)));
    }

    @SuppressWarnings("DuplicatedCode")
    @SneakyThrows
    public <T extends E> EventCallback <T> once (Class <T> type, EventCallback <T> callback) {
        if (!onceListeners.containsKey(type)) onceListeners.put(type, new ArrayList <> ());
        onceListeners.get(type).add(callback);
        return callback;
    }

    public <T extends E> EventCallback <T> once (Class <T> type, Consumer <T> callback) {
        return once(type, (event, ignored) -> callback.accept(event));
    }

    public EventCallback <E> all (EventCallback <E> callback) {
        this.allListeners.add(callback);
        return callback;
    }

    public EventCallback <E> all (Consumer <E> callback) {
        return all((event, ignored) -> callback.accept(event));
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <T extends E> void emit (T event) {
        event.setEventBus(this);
        Class<Event> type = (Class <Event>) event.getClass();
        if (!listeners.containsKey(type)) listeners.put(type, new ArrayList <> ());
        if (!onceListeners.containsKey(type)) onceListeners.put(type, new ArrayList <> ());
        for (var i : listeners.get(type)) {
            var call = (EventCallback <Event>) i;
            call.call(event, call);
        }
        for (var i : onceListeners.get(type)) {
            var call = (EventCallback <Event>) i;
            call.call(event, call);
        }
        for (var i : allListeners) i.call(event, i);
        onceListeners.get(type).clear();
    }

}
