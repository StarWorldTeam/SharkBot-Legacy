package team.starworld.shark.core.registries;

import lombok.Getter;

import java.util.*;

public class TagManager <T extends Registrable> {

    @Getter
    private final Registry <T> registry;

    private final Map <TagKey <T>, Tag <T>> map = new HashMap <> ();

    TagManager (Registry <T> registry) {
        this.registry = registry;
    }

    @SafeVarargs
    public final void add (TagKey <T> key, T... values) {
        if (!map.containsKey(key)) map.put(key, new Tag <> (key));
        map.get(key).addAll(Arrays.stream(values).toList());
    }

    public final void addAll (TagKey <T> key, T value) {
        add(key, value);
    }

    public boolean containsKey (TagKey <T> key) { return map.containsKey(key); }
    public boolean containsValue (Tag <T> value) { return map.containsValue(value); }

    public Tag <T> get (TagKey <T> key) { return map.get(key); }

    public Set <TagKey <T>> keys () { return map.keySet(); }
    public Set <Tag <T>> values () { return new HashSet <> (map.values()); }
    public Set <Map.Entry<TagKey <T>, Tag <T>>> entries () { return map.entrySet(); }

    public TagKey <T> createTagKey (ResourceLocation location) {
        return new TagKey <> (registry.getKey(), location);
    }

}
