package team.starworld.shark.core.registries;

import team.starworld.shark.core.entity.fluid.Fluid;
import team.starworld.shark.core.entity.item.Item;

public class SharkRegistries {

    public static final Registry <Item> ITEMS = Registry.create(Keys.ITEM);
    public static final Registry <Fluid> FLUIDS = Registry.create(Keys.FLUID);

    public static class Keys {

        public static final ResourceKey <Item> ITEM = ResourceKey.create(ResourceLocation.of("item"));
        public static final ResourceKey <Fluid> FLUID = ResourceKey.create(ResourceLocation.of("fluid"));

    }

}
