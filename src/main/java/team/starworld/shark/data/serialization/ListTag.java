package team.starworld.shark.data.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.undercouch.bson4jackson.BsonFactory;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ListTag extends ArrayList <Object> {

    public ListTag put (Object value) {
        add(value);
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
        this.addAll(value);
        this.toList().clear();
        return this;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public Object get (int index) {
        var value = super.get(index);
        if (!(value instanceof ListTag) && value instanceof List <?> list)
            set(index, new ListTag().load(list));
        if (value instanceof Map <?, ?> map)
            set(index, new CompoundTag().load(map));
        return super.get(index);
    }

    @SneakyThrows
    public ListTag load (List <?> list) {
        this.clear();
        this.addAll(list);
        this.toList().clear();
        return this;
    }

    @SuppressWarnings("UseBulkOperation")
    public List <Object> toList () {
        var list = new ArrayList <> ();
        for (var index = 0; index < size(); index ++) list.add(get(index));
        return list;
    }

    @Override
    public String toString () {
        return "ListTag {%s}".formatted(
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
