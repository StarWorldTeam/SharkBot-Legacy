package team.starworld.shark.core.registries;

import team.starworld.shark.core.entity.fluid.Fluids;
import team.starworld.shark.core.entity.item.Items;

public class Registries {

    public static void bootstrap () {
        Items.bootstrap();
        Fluids.bootstrap();
    }

}
