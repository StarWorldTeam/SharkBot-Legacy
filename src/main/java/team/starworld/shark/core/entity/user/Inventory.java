package team.starworld.shark.core.entity.user;

import team.starworld.shark.core.item.ItemStack;
import team.starworld.shark.data.serialization.CompoundTag;
import team.starworld.shark.data.serialization.ListTag;
import team.starworld.shark.data.serialization.TagSerializable;

import java.util.ArrayList;
import java.util.List;

public class Inventory implements TagSerializable <Inventory> {

    private final List <ItemStack> itemStackList = new ArrayList <> ();

    @Override
    public Inventory load (CompoundTag tag) {
        if (tag.containsKey("itemStackList")) tag.getList("itemStackList").toList()
            .stream()
            .filter(i -> i instanceof CompoundTag)
            .map(i -> new CompoundTag().load(((CompoundTag) i).save()))
            .forEach(
                itemStackTag -> itemStackList.add(ItemStack.empty().load(itemStackTag))
            );
        return this;
    }

    @Override
    public CompoundTag save () {
        return new CompoundTag()
            .putList(
                "itemStackList",
                new ListTag()
                    .putAll(
                        itemStackList
                            .stream()
                            .map(ItemStack::save)
                            .toList()
                    )
            );
    }

}
