package team.starworld.shark.core.item;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.starworld.shark.core.entity.user.User;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.core.registries.SharkRegistries;
import team.starworld.shark.data.serialization.CompoundTag;
import team.starworld.shark.data.serialization.TagSerializable;
import team.starworld.shark.network.chat.Component;
import team.starworld.shark.network.chat.MultiComponent;

import java.util.ArrayList;

public class ItemStack implements ItemSupplier, TagSerializable <ItemStack> {

    @Getter @Setter
    private int count;

    @Nullable @Setter
    private Item item;

    @Getter
    private final CompoundTag tag;

    public static ItemStack empty () {
        return new ItemStack((Item) null, 0);
    }

    public ItemStack (@Nullable Item item) {
        this(item, 1);
    }

    public ItemStack (@Nullable Item item, int count) {
        this(item, 1, new CompoundTag());
    }

    public ItemStack (@Nullable Item item, int count, CompoundTag tag) {
        this.count = count;
        this.item = SharkRegistries.ITEMS.containsValue(item) ? item : null;
        this.tag = tag;
    }

    public ItemStack (@NotNull ResourceLocation item) {
        this(item, 1);
    }

    public ItemStack (@NotNull ResourceLocation item, int count) {
        this(item, 1, new CompoundTag());
    }

    public ItemStack (@NotNull ResourceLocation item, int count, CompoundTag tag) { this(SharkRegistries.ITEMS.get(item), count, tag); }

    public ResourceLocation getLocation () { return SharkRegistries.ITEMS.getKey(item); }

    public Item getItem () {
        return this.item == null ? Items.AIR.get() : this.item;
    }

    public boolean isEmpty () { return this.count == 0 || getItem() == Items.AIR.get(); }
    public boolean isAir () { return getItem() == Items.AIR.get(); }

    @Override
    public Item asItem () {
        return getItem();
    }

    public void setTag (CompoundTag tag) {
        this.tag.load(tag.saveAsMap());
    }

    @Override
    public ItemStack load (CompoundTag tag) {
        setTag(tag.getCompound("tag"));
        setItem(SharkRegistries.ITEMS.get(ResourceLocation.of(tag.getString("id"))));
        setCount(tag.getInt("count"));
        return this;
    }

    @Override
    public CompoundTag save () {
        return new CompoundTag()
            .putString("id", this.getId().toString())
            .putCompound("tag", getTag())
            .putInt("count", getCount());
    }

    public ResourceLocation getId () {
        return SharkRegistries.ITEMS.getKey(getItem());
    }

    public ItemStackComponent getName (@Nullable User user) {
        return new ItemStackComponent(this, getItem().getName(this, user));
    }

    public ItemStackComponent getName () {
        return getName(null);
    }

    public ItemStackComponent getTooltip (@Nullable User user) {
        var components = new ArrayList <Component> ();
        getItem().appendToolTip(this, components, user);
        return new ItemStackComponent(this, new MultiComponent(components.toArray(new Component[] {})));
    }

    public ItemStackComponent getTooltip () {
        return getTooltip(null);
    }

    @Override
    public int hashCode () {
        return save().hashCode();
    }

    @Override
    public boolean equals (Object obj) {
        return obj instanceof ItemStack stack && stack.save().hashCode() == save().hashCode();
    }

}