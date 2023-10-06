package team.starworld.shark.event.application.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.data.resource.SharkResource;
import team.starworld.shark.event.Event;

import java.util.List;

@AllArgsConstructor @Getter
public class ResourceLoadEvent extends Event {

    private final ResourceLocation location;
    private final List <SharkResource> resources;

}
