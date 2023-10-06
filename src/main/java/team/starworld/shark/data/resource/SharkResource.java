package team.starworld.shark.data.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.Resource;
import team.starworld.shark.core.registries.ResourceLocation;

@AllArgsConstructor
public class SharkResource {

    private final @Getter Resource resource;
    private final @Getter String baseDirectory;
    private final @Getter String path;
    private final @Getter ResourceLocation location;

    @Override
    public String toString () {
        return "SharkResource [%s]".formatted(this.path);
    }

}
