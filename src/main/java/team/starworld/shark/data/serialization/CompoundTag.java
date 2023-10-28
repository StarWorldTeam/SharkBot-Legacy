package team.starworld.shark.data.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.undercouch.bson4jackson.BsonFactory;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class CompoundTag {

    private LinkedHashMap <String, Object> map = new LinkedHashMap <> ();

    @Override
    public boolean equals (Object obj) {
        return obj instanceof CompoundTag && this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode () {
        return Arrays.hashCode(this.save());
    }

    public LinkedHashMap <String, Object> saveAsMap () {
        var map = new LinkedHashMap <String, Object> ();
        for (var i : this.map.entrySet()) {
            var name = i.getKey();
            var value = i.getValue();
            if (value instanceof CompoundTag tag) map.put(name, tag.saveAsMap());
            else if (value instanceof ListTag tag) map.put(name, tag.saveAsList());
            else map.put(name, value);
        }
        return map;
    }

    @SneakyThrows
    public byte[] save () {
        var mapper = new ObjectMapper(new BsonFactory());
        return mapper.writeValueAsBytes(this.saveAsMap());
    }

    @SneakyThrows
    public CompoundTag load (byte[] bytes) {
        var mapper = new ObjectMapper(new BsonFactory());
        this.map = Objects.requireNonNullElse(mapper.readValue(bytes, new TypeReference <> () {}), this.map);
        this.entrySet();
        return this;
    }

    @SneakyThrows
    public CompoundTag load (@NotNull Map <?, ?> map) {
        this.map.clear();
        map.forEach((key, value) -> this.map.put(String.valueOf(key), value));
        this.entrySet();
        return this;
    }

    public CompoundTag putCompound (String name, CompoundTag value) {
        map.put(name, value);
        return this;
    }

    public CompoundTag getCompound (String name) { return (CompoundTag) get(name); }

    public CompoundTag putIfNull (String name, Supplier <Object> supplier) {
        if (!this.map.containsKey(name) || this.map.get(name) == null)
            put(name, supplier.get());
        return this;
    }

    public CompoundTag put (String name, Object value) {
        map.put(name, value);
        return this;
    }

    public Object get (String name) {
        var value = this.map.get(name);
        if (!(value instanceof ListTag) && value instanceof List <?> list) this.map.put(name, new ListTag().load(list));
        if (value instanceof Map <?, ?> valueMap)
            put(name, new CompoundTag().load(valueMap));
        return this.map.get(name);
    }


    public CompoundTag putLong (String name, long value) {
        this.map.put(name, value);
        return this;
    }

    public long getLong (String name) { return get(name) instanceof Long value ? value : Long.parseLong(get(name).toString()); }

    public CompoundTag putInt (String name, int value) {
        this.map.put(name, value);
        return this;
    }

    public int getInt (String name) { return get(name) instanceof Integer value ? value : Integer.parseInt(get(name).toString()); }

    public CompoundTag putFloat (String name, float value) {
        this.map.put(name, value);
        return this;
    }

    public float getFloat (String name) { return get(name) instanceof Float value ? value : Float.parseFloat(get(name).toString()); }

    public CompoundTag putDouble (String name, Double value) {
        this.map.put(name, value);
        return this;
    }

    public double getDouble (String name) { return get(name) instanceof Double value ? value : Double.parseDouble(get(name).toString()); }

    public CompoundTag putString (String name, String value) {
        this.map.put(name, value);
        return this;
    }

    public String getString (String name) { return String.valueOf(get(name)); }

    public CompoundTag putBoolean (String name, boolean value) {
        this.map.put(name, value);
        return this;
    }

    public boolean getBoolean (String name) {
        return get(name) instanceof Boolean value ? value :
            get(name) instanceof String string ? Boolean.parseBoolean(string) :
            get(name) instanceof Number number ? number.intValue() != 0 : Boolean.parseBoolean(String.valueOf(get(name)));
    }

    public CompoundTag putList (String name, ListTag value) {
        this.map.put(name, value);
        return this;
    }

    public ListTag getList (String name) {
        return (ListTag) get(name);
    }

    public Set <Map.Entry <String, Object>> entrySet () {
        this.map.forEach(
            (key, value) -> {
                if (value == null) this.map.remove(key);
            }
        );
        return Set.copyOf(this.map.keySet().stream().map(key -> Map.entry(key, get(key))).toList());
    }

    @Override
    public String toString () {
        return "{%s}".formatted(
            String.join(
                ", ",
                entrySet()
                .stream()
                .map(i -> "%s=%s".formatted(i.getKey(), i.getValue()))
                .toList()
            )
        );
    }

    public CompoundTag remove (String name) {
        map.remove(name);
        return this;
    }

    public CompoundTag clear () {
        map.clear();
        return this;
    }

    public boolean containsKey (String key) {
        return map.containsKey(key);
    }

    public boolean containsValue (String value) {
        return map.containsValue(value);
    }

}
