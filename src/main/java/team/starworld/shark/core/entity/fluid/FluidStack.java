package team.starworld.shark.core.entity.fluid;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.starworld.shark.core.entity.user.User;
import team.starworld.shark.core.registries.RegistryEntry;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.core.registries.SharkRegistries;
import team.starworld.shark.data.serialization.CompoundTag;
import team.starworld.shark.data.serialization.TagSerializable;
import team.starworld.shark.network.chat.Component;
import team.starworld.shark.network.chat.MultiComponent;

import java.util.ArrayList;

public class FluidStack implements FluidSupplier, TagSerializable {

    @Getter
    @Setter
    private int count;

    @Nullable @Setter
    private Fluid fluid;

    public Fluid getFluid () {
        return this.fluid == null ? Fluids.AIR.get() : this.fluid;
    }

    @Getter
    private final CompoundTag tag;

    public static FluidStack empty () {
        return new FluidStack((Fluid) null, 0);
    }

    public FluidStack (@Nullable FluidSupplier fluid) {
        this(fluid, 1);
    }

    public FluidStack (@Nullable FluidSupplier fluid, int count) {
        this(fluid, count, new CompoundTag());
    }

    public FluidStack (@Nullable FluidSupplier fluid, int count, CompoundTag tag) {
        this.count = count;
        this.fluid = fluid != null && SharkRegistries.FLUIDS.containsValue(fluid.asFluid()) ? fluid.asFluid() : null;
        this.tag = tag;
    }

    public FluidStack (@Nullable RegistryEntry <? extends FluidSupplier> fluid, int count, CompoundTag tag) {
        this(fluid != null ? fluid.get(): null, count, tag);
    }

    public FluidStack (@Nullable RegistryEntry <? extends FluidSupplier> fluid) {
        this(fluid, 1);
    }

    public FluidStack (@Nullable RegistryEntry <? extends FluidSupplier> fluid, int count) {
        this(fluid, count, new CompoundTag());
    }

    public FluidStack (@NotNull ResourceLocation fluid) {
        this(fluid, 1);
    }

    public FluidStack (@NotNull ResourceLocation fluid, int count) {
        this(fluid, count, new CompoundTag());
    }

    public FluidStack (@NotNull ResourceLocation fluid, int count, CompoundTag tag) { this(SharkRegistries.FLUIDS.get(fluid), count, tag); }

    @Override
    public Fluid asFluid () { return this.getFluid(); }

    @Override
    public void load (CompoundTag tag) {
        setTag(tag.getCompound("tag"));
        setFluid(SharkRegistries.FLUIDS.get(ResourceLocation.of(tag.getString("id"))));
        setCount(tag.getInt("count"));
    }

    @Override
    public CompoundTag save () {
        return new CompoundTag()
            .putString("id", this.getId().toString())
            .putCompound("tag", getTag())
            .putInt("count", getCount());
    }

    public ResourceLocation getId () {
        return SharkRegistries.FLUIDS.getKey(this.getFluid());
    }

    public void setTag (CompoundTag tag) {
        this.tag.load(tag.saveAsMap());
    }

    public boolean isEmpty () { return this.count <= 0 || getFluid() == Fluids.AIR.get(); }
    public boolean isAir () { return getFluid() == Fluids.AIR.get(); }

    @Override
    public int hashCode () {
        return save().hashCode();
    }

    @Override
    public boolean equals (Object obj) {
        return obj instanceof FluidStack stack && stack.save().hashCode() == save().hashCode();
    }

    public FluidStackComponent getName (@Nullable User user) {
        return new FluidStackComponent(this, getFluid().getName(this, user));
    }

    public FluidStackComponent getName () {
        return getName(null);
    }

    public FluidStackComponent getTooltip (@Nullable User user) {
        var components = new ArrayList <Component> ();
        getFluid().appendToolTip(this, components, user);
        return new FluidStackComponent(this, new MultiComponent(components.toArray(new Component[] {})));
    }

    public FluidStackComponent getTooltip () {
        return getTooltip(null);
    }

    @Override
    public String toString () {
        return "%s [%smL] (%s) %s".formatted(getClass().getSimpleName(), getCount(), getId(), getTag());
    }

}
