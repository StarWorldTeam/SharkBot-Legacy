package team.starworld.shark.core.entity.energy;

public interface AbstractEnergyHolder {

    long getEnergy ();

    void insert (long energy);

    default void insert (AbstractEnergyHolder energy) { insert(energy.getEnergy()); }

    void extract (long energy);

    default void extract (long energy, AbstractEnergyHolder holder) {
        extract(energy);
        holder.insert(energy);
    }

}
