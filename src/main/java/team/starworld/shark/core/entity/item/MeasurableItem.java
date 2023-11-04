package team.starworld.shark.core.entity.item;

public interface MeasurableItem {

    // Default (g): (Volume * Density)
    default double getWeight (ItemStack stack) { return getVolume(stack) * getDensity(stack); }

    // Default (K): 0
    default double getTemperature (ItemStack stack) { return 0; }

    // Default (g/cm³): 0
    default double getDensity (ItemStack stack) { return 0; }

    // Default (cm³): 0
    default double getVolume (ItemStack stack) { return 0; }

}
