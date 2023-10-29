package team.starworld.shark.event.application.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.data.resource.ResourceLoader;
import team.starworld.shark.data.resource.SharkResource;
import team.starworld.shark.event.Event;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ResourceLoadEvent extends Event {

    private final ResourceLocation location;
    private final List <SharkResource> resources;
    private final ResourceLoader loader;

}
