package team.starworld.shark.core.entity.fluid;

import team.starworld.shark.core.registries.RegistryEntry;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.core.registries.SharkRegistries;

public class Fluids {

    public static final RegistryEntry <Fluid> AIR = SharkRegistries.FLUIDS.register(ResourceLocation.of("air"), Fluid::new);
    public static final RegistryEntry <Fluid> MILK = SharkRegistries.FLUIDS.register(ResourceLocation.of("milk"), Fluid::new);


    public static void bootstrap () {}

}
