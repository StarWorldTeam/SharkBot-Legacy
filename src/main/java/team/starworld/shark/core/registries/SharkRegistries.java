package team.starworld.shark.core.registries;

import team.starworld.shark.core.item.Item;

public class SharkRegistries {

    public static final Registry <Item> ITEMS = Registry.create(Keys.ITEM);

    public static class Keys {

        public static final ResourceKey <Item> ITEM = ResourceKey.create(ResourceLocation.of("item"));

    }

}
