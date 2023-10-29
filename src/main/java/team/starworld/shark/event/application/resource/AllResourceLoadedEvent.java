package team.starworld.shark.event.application.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.starworld.shark.data.resource.ResourceLoader;
import team.starworld.shark.event.Event;

@RequiredArgsConstructor
public class AllResourceLoadedEvent extends Event {

    @Getter private final ResourceLoader loader;

}
