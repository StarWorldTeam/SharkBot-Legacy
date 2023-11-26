package team.starworld.shark.core.entity.energy;

/**
 * 能量存储
 */
public interface AbstractEnergyHolder {

    long getEnergy ();

    void insert (long energy);

    default void insert (AbstractEnergyHolder energy) {
        insert(energy.getEnergy());
    }

    void extract (long energy);

    default void extract (long energy, AbstractEnergyHolder holder) {
        extract(energy);
        holder.insert(energy);
    }

}
