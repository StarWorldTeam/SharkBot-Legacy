package team.starworld.shark.core.registries;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class Registry <T extends Registrable> {

    @RequiredArgsConstructor
    public static class Entry <T extends Registrable> implements RegistryEntry <T> {

        private final ResourceLocation location;
        private final T value;

        @Override
        public ResourceLocation getLocation () {
            return location;
        }

        @Override
        public T get () {
            return value;
        }

    }

    private static final Map <ResourceKey <?>, Registry <?>> INSTANCES = new HashMap <> ();

    @SuppressWarnings("unchecked")
    public static <T extends Registrable, E> Registry <T> create (ResourceKey <T> key) {
        if (!INSTANCES.containsKey(key)) INSTANCES.put(key, new Registry <> (key));
        return (Registry <T>) INSTANCES.get(key);
    }

    @Getter
    private final ResourceKey <T> key;

    @Getter
    private final TagManager <T> tags;

    private final Map <ResourceLocation, T> map = new HashMap <> ();

    private Registry (ResourceKey <T> key) {
        this.key = key;
        this.tags = new TagManager <> (this);
    }

    public Set <T> values () { return new HashSet <> (map.values()); }
    public Set <ResourceLocation> keys () { return map.keySet(); }

    public Set <Map.Entry <ResourceLocation, T>> entries () { return map.entrySet(); }

    @NotNull
    public T get (ResourceLocation location) {
        return this.map.get(location);
    }

    public boolean containsKey (ResourceLocation key) { return this.map.containsKey(key); }
    public boolean containsValue (T value) { return this.map.containsValue(value); }

    @NotNull
    public ResourceLocation getKey (T value) {
        for (var entry : map.entrySet()) {
            if (entry.getValue() == value) return entry.getKey();
        }
        throw new NullPointerException();
    }

    public RegistryEntry <T> register (ResourceLocation location, Supplier <T> supplier) {
        return register(location, ignored -> supplier.get());
    }

    public RegistryEntry <T> register (ResourceLocation location, Function <ResourceLocation, T> function) {
        var value = function.apply(location);
        if (map.containsKey(location) || map.containsValue(value))
            throw new RuntimeException("%s (%s) has already registered".formatted(location, value));
        map.put(location, value);
        return new Entry <> (location, value);
    }



}
