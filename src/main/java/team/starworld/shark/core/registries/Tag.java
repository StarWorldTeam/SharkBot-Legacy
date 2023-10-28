package team.starworld.shark.core.registries;

import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Tag <T> {

    private final Set <T> values = new HashSet <> ();

    @Getter
    private final TagKey <T> key;

    public Tag (TagKey <T> key) {
        this.key = key;
    }

    public Set <T> values () {
        return new HashSet <> (values);
    }

    public void add (T value) { values.add(value); }
    public void addAll (Collection <T> value) { values.addAll(value);}

    public boolean containsValue (T value) { return values.contains(value); }

}
