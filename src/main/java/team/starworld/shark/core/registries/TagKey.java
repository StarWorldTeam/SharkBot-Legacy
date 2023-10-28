package team.starworld.shark.core.registries;

import lombok.Getter;

public class TagKey <T> {

    @Getter
    private final ResourceKey <T> key;

    @Getter
    private final ResourceLocation location;

    public TagKey (ResourceKey <T> key, ResourceLocation location) {
        this.key = key;
        this.location = location;
    }

    @Override
    public int hashCode () {
        return 31 * key.hashCode() + location.hashCode();
    }

    @Override
    public boolean equals (Object obj) {
        return obj instanceof TagKey <?> tagKey && tagKey.hashCode() == hashCode();
    }

}
