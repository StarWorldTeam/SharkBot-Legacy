package team.starworld.shark.core.entity.item;

import team.starworld.shark.core.registries.RegistryEntry;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.core.registries.SharkRegistries;

public class Items {

    public static final RegistryEntry <Item> AIR = SharkRegistries.ITEMS.register(ResourceLocation.of("air"), Item::new);

}
