package team.starworld.shark.core.entity.energy;

public class EnergyHolder implements AbstractEnergyHolder {

    private long energy;

    public EnergyHolder (long energy) {
        this.energy = energy;
    }

    public EnergyHolder () {
        this(0);
    }

    @Override
    public long getEnergy () {
        return energy;
    }

    @Override
    public void insert (long energy) {
        this.energy += energy;
    }

    @Override
    public void extract (long energy) {
        this.energy -= energy;
        if (this.energy < 0) this.energy = 0;
    }

    public void setEnergy (long energy) {
        this.energy = energy;
    }

}
