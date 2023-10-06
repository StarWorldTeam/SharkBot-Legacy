package team.starworld.shark.event.bus;

import team.starworld.shark.event.Event;

@FunctionalInterface
public interface EventCallback <T extends Event> {

    void call (T event, EventCallback <T> callback);

}
