package team.starworld.shark.data.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.undercouch.bson4jackson.BsonFactory;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ListTag extends ArrayList <Object> {

    public ListTag put (Object value) {
        add(value);
        return this;
    }

    public ListTag putAll (Collection <?> value) {
        value.forEach(this::put);
        return this;
    }

    public List <Object> saveAsList () {
        var list = new ArrayList <> ();
        for (var value : this) {
            if (value instanceof CompoundTag tag) list.add(tag.saveAsMap());
            else if (value instanceof ListTag tag) list.add(tag.saveAsList());
            else list.add(value);
        }
        return list;
    }

    @SneakyThrows
    public byte[] save () {
        var mapper = new ObjectMapper(new BsonFactory());
        return mapper.writeValueAsBytes(this.saveAsList());
    }

    @SneakyThrows
    public ListTag load (byte[] bytes) {
        var mapper = new ObjectMapper(new BsonFactory());
        var value = mapper.readValue(bytes, new TypeReference <List <?>> () {});
        this.clear();
        if (value != null)
            this.addAll(value);
        this.toList().clear();
        return this;
    }

    public Object getAndLoad (int index) {
        var value = super.get(index);
        if (!(value instanceof ListTag) && value instanceof List <?> list)
            set(index, new ListTag().load(list));
        if (value instanceof Map <?, ?> map)
            set(index, new CompoundTag().load(map));
        return super.get(index);
    }

    public Object get (int index) { return getAndLoad(index); }

    @SneakyThrows
    public ListTag load (@NotNull List <?> list) {
        this.clear();
        this.addAll(list);
        this.toList().clear();
        return this;
    }

    public List <Object> toList () {
        var list = new ArrayList <> ();
        for (var index = 0; index < size(); index ++)
            list.add(getAndLoad(index));
        return list;
    }

    @Override
    public String toString () {
        return "[%s]".formatted(
            String.join(
                ", ",
                toList().stream().map(String::valueOf).toList()
            )
        );
    }

    @Override
    public boolean equals (Object obj) {
        return obj instanceof ListTag && hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode () {
        return Arrays.hashCode(this.save());
    }

}
