package team.starworld.shark.core.entity.item;

public interface MeasurableItem {

    class Item extends team.starworld.shark.core.entity.item.Item implements MeasurableItem { }

    static MeasurableItem of (team.starworld.shark.core.entity.item.Item item) {
        if (item instanceof MeasurableItem measurableItem) return measurableItem;
        return new Item ();
    }

    // Default (g): (Volume * Density)
    default double getWeight (ItemStack stack) { return getVolume(stack) * getDensity(stack); }

    // Default (K): 0
    default double getTemperature (ItemStack stack) { return 0; }

    // Default (g/cm³): 0
    default double getDensity (ItemStack stack) { return 0; }

    // Default (cm³): 0
    default double getVolume (ItemStack stack) { return 0; }

}
