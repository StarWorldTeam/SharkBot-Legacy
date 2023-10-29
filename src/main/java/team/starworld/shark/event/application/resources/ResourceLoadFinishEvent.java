package team.starworld.shark.event.application.resources;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.starworld.shark.data.resource.ResourceLoader;
import team.starworld.shark.event.Event;

@RequiredArgsConstructor
public class ResourceLoadFinishEvent extends Event {

    @Getter private final ResourceLoader loader;

}
