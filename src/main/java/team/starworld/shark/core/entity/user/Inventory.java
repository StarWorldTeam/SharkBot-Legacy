package team.starworld.shark.core.entity.user;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import team.starworld.shark.core.entity.energy.AbstractEnergyHolder;
import team.starworld.shark.core.entity.energy.EnergyHolder;
import team.starworld.shark.core.entity.fluid.Fluid;
import team.starworld.shark.core.entity.fluid.FluidStack;
import team.starworld.shark.core.entity.item.Item;
import team.starworld.shark.core.entity.item.ItemStack;
import team.starworld.shark.data.serialization.CompoundTag;
import team.starworld.shark.data.serialization.ListTag;
import team.starworld.shark.data.serialization.TagSerializable;

import java.util.ArrayList;
import java.util.List;

public class Inventory implements TagSerializable {

    private final List <ItemStack> itemStackStored = new ArrayList <> ();
    private final List <FluidStack> fluidStackStored = new ArrayList <> ();

    @Getter
    private EnergyHolder energyStored = new EnergyHolder();

    @Override
    public void load (CompoundTag tag) {
        if (tag.containsKey("itemStackStored")) tag.getList("itemStackStored").toList()
            .stream()
            .filter(i -> i instanceof CompoundTag)
            .map(i -> new CompoundTag().load(((CompoundTag) i).save()))
            .forEach(
                itemStackTag -> {
                    var stack = ItemStack.empty();
                    stack.load(itemStackTag);
                    itemStackStored.add(stack);
                }
            );
        if (tag.containsKey("fluidStackStored")) tag.getList("fluidStackStored").toList()
            .stream()
            .filter(i -> i instanceof CompoundTag)
            .map(i -> new CompoundTag().load(((CompoundTag) i).save()))
            .forEach(
                fluidStackTag -> {
                    var stack = FluidStack.empty();
                    stack.load(fluidStackTag);
                    fluidStackStored.add(stack);
                }
            );
        if (tag.containsKey("energyStored"))
            energyStored.setEnergy(tag.getLong("energyStored"));
    }

    @Override
    public CompoundTag save () {
        return new CompoundTag()
            .putList(
                "itemStackStored",
                new ListTag().putAll(itemStackStored.stream().map(ItemStack::save).toList())
            ).putList(
                "fluidStackStored",
                new ListTag().putAll(fluidStackStored.stream().map(FluidStack::save).toList())
            ).putLong(
                "energyStored",
                energyStored.getEnergy()
            );
    }

    public ItemStack[] getItemStackStored () {
        check();
        return itemStackStored.toArray(new ItemStack[] {});
    }

    public FluidStack[] getFluidStackStored () {
        check();
        return fluidStackStored.toArray(new FluidStack[] {});
    }

    public void check () {
        fluidStackStored.removeIf(i -> i == null || i.isEmpty());
        itemStackStored.removeIf(i -> i == null || i.isEmpty());
    }

    public void give (ItemStack stack) {
        for (var i : itemStackStored) {
            if (i.getId() == stack.getId() && i.getTag().equals(stack.getTag())) {
                i.setCount(i.getCount() + stack.getCount());
                return;
            }
        }
        itemStackStored.add(stack);
        check();
    }
    public void give (FluidStack stack) {
        for (var i : this.fluidStackStored) {
            if (i.getId() == stack.getId() && i.getTag().equals(stack.getTag())) {
                i.setCount(i.getCount() + stack.getCount());
                return;
            }
        }
        fluidStackStored.add(stack);
        check();
    }
    public void give (AbstractEnergyHolder holder) {
        energyStored.insert(holder);
    }

    public void drop (ItemStack stack) {
        drop(stack.getItem(), stack.getCount(), stack.getTag());
    }

    public void drop (Item item) {
        itemStackStored.removeIf(i -> i.getItem() == item);
        check();
    }

    public void drop (Item item, int amount) {
        drop(item, amount, null);
    }

    public void drop (Item item, @Nullable Integer amount, @Nullable CompoundTag tag) {
        var needRemove = amount;
        if (amount == null) for (var i : itemStackStored) {
            if (i.getItem() != item && !(tag == null || i.getTag().equals(tag))) continue;
            i.setCount(0);
        }
        else for (var i : this.itemStackStored) {
            if (!(i.getItem() == item) && (tag == null || i.getTag().equals(tag)))
                continue;
            if (i.getCount() >= needRemove) {
                i.setCount(i.getCount() - needRemove);
                return;
            }
            needRemove -= i.getCount();
            i.setCount(0);
        }
        check();
    }

    public void drop (FluidStack stack) {
        drop(stack.getFluid(), stack.getCount(), stack.getTag());
    }

    public void drop (Fluid fluid) {
        fluidStackStored.removeIf(i -> i.getFluid() == fluid);
        check();
    }

    public void drop (Fluid fluid, int amount) {
        drop(fluid, amount, null);
    }

    public void drop (Fluid fluid, @Nullable Integer amount, @Nullable CompoundTag tag) {
        var needRemove = amount;
        if (amount == null) for (var i : fluidStackStored) {
            if (i.getFluid() != fluid && !(tag == null || i.getTag().equals(tag))) continue;
            i.setCount(0);
        }
        else for (var i : fluidStackStored) {
            if (i.getFluid() != fluid && !(tag == null || i.getTag().equals(tag)))
                continue;
            if (i.getCount() >= needRemove) {
                i.setCount(i.getCount() - needRemove);
                return;
            }
            needRemove -= i.getCount();
            i.setCount(0);
        }
        check();
    }

    public void drop (AbstractEnergyHolder holder) {
        energyStored.extract(Math.min(energyStored.getEnergy(), holder.getEnergy()));
    }

    public int count (Item item) {
        return itemStackStored.stream()
            .filter(i -> i.getItem() == item)
            .map(ItemStack::getCount)
            .reduce(Integer::sum).orElse(0);
    }

    public int count (Item item, CompoundTag tag) {
        return itemStackStored.stream()
            .filter(i -> i.getItem() == item && i.getTag().equals(tag))
            .map(ItemStack::getCount)
            .reduce(Integer::sum).orElse(0);
    }

    public int count (Fluid fluid) {
        return fluidStackStored.stream()
            .filter(i -> i.getFluid() == fluid)
            .map(FluidStack::getCount)
            .reduce(Integer::sum).orElse(0);
    }

    public int count (Fluid fluid, CompoundTag tag) {
        return fluidStackStored.stream()
            .filter(i -> i.getFluid() == fluid && i.getTag().equals(tag))
            .map(FluidStack::getCount)
            .reduce(Integer::sum).orElse(0);
    }

}
