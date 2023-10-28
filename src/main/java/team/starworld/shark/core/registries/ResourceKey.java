package team.starworld.shark.core.registries;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ResourceKey <T> {

    private static final Map <ResourceLocation, ResourceKey <?>> INSTANCES = new HashMap <> ();

    @Getter
    private final ResourceLocation location;

    @SuppressWarnings("unchecked")
    public static <T> ResourceKey <T> create (ResourceLocation location) {
        if (!INSTANCES.containsKey(location)) INSTANCES.put(location, new ResourceKey <> (location));
        return (ResourceKey <T>) INSTANCES.get(location);
    }

    private ResourceKey (ResourceLocation location) {
        this.location = location;
    }

    @Override
    public boolean equals (Object obj) {
        return obj instanceof ResourceKey <?> key && key.location.equals(this.location);
    }

    @Override
    public int hashCode () {
        return location.hashCode();
    }

}
