package team.starworld.shark.core.registries;

import team.starworld.shark.core.data.material.*;
import team.starworld.shark.core.entity.fluid.*;
import team.starworld.shark.core.entity.item.*;

public class SharkRegistries {

    public static void bootstrap () {
        Elements.bootstrap();
        Materials.bootstrap();
        Items.bootstrap();
        Fluids.bootstrap();
    }

    public static final Registry <Item> ITEMS = Registry.create(Keys.ITEM);
    public static final Registry <Fluid> FLUIDS = Registry.create(Keys.FLUID);
    public static final Registry <Material> MATERIALS = Registry.create(Keys.MATERIAL);
    public static final Registry <Element> ELEMENTS = Registry.create(Keys.ELEMENT);

    public static class Keys {

        public static final ResourceKey <Item> ITEM = ResourceKey.create(ResourceLocation.of("item"));
        public static final ResourceKey <Fluid> FLUID = ResourceKey.create(ResourceLocation.of("fluid"));
        public static final ResourceKey <Material> MATERIAL = ResourceKey.create(ResourceLocation.of("material"));
        public static final ResourceKey <Element> ELEMENT = ResourceKey.create(ResourceLocation.of("element"));

    }

}
