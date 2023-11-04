package team.starworld.shark.event.bus;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import team.starworld.shark.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus {

    public static final List <EventBus> INSTANCES = new ArrayList <> ();

    private final @Getter Map <Class <?>, List <EventCallback <?>>> listeners = new HashMap <> ();
    private final @Getter Map <Class <?>, List <EventCallback <?>>> onceListeners = new HashMap <> ();
    private final @Getter List <EventCallback <?>> allListeners = new ArrayList <> ();

    @Getter @Setter
    private String name = "EventBus";

    public EventBus () {
        INSTANCES.add(this);
    }

    public EventBus (String name) {
        INSTANCES.add(this);
        this.name = name;
    }

    @SuppressWarnings("DuplicatedCode")
    @SneakyThrows
    public <T extends Event> EventCallback <T> on (Class <T> type, EventCallback <T> callback) {
        if (!listeners.containsKey(type)) listeners.put(type, new ArrayList <> ());
        listeners.get(type).add(callback);
        return callback;
    }

    public void remove (Class <? extends Event> type, EventCallback <?> callback) {
        if (!listeners.containsKey(type)) return;
        listeners.get(type).removeIf(i -> i == callback);
    }

    public void removeOnce (Class <? extends Event> type, EventCallback <?> callback) {
        if (!onceListeners.containsKey(type)) return;
        listeners.get(type).removeIf(i -> i == callback);
    }

    public void removeAll (EventCallback <?> callback) {
        allListeners.removeIf(i -> i == callback);
    }

    public <T extends Event> EventCallback <T> on (Class <T> type, Consumer <T> callback) {
        return on(type, ((event, ignored) -> callback.accept(event)));
    }

    @SuppressWarnings("DuplicatedCode")
    @SneakyThrows
    public <T extends Event> EventCallback <T> once (Class <T> type, EventCallback <T> callback) {
        if (!onceListeners.containsKey(type)) onceListeners.put(type, new ArrayList <> ());
        onceListeners.get(type).add(callback);
        return callback;
    }

    public <T extends Event> EventCallback <T> once (Class <T> type, Consumer <T> callback) {
        return once(type, (event, ignored) -> callback.accept(event));
    }

    public EventCallback <? extends Event> all (EventCallback <? extends Event> callback) {
        this.allListeners.add(callback);
        return callback;
    }

    public EventCallback <? extends Event> all (Consumer <Event> callback) {
        return all((event, ignored) -> callback.accept(event));
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <T extends Event> void emit (T event) {
        event.setEventBus(this);
        Class <Event> type = (Class <Event>) event.getClass();
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
        for (var i : allListeners) ((EventCallback <Event>) i).call(event, (EventCallback <Event>) i);
        onceListeners.get(type).clear();
    }

}
