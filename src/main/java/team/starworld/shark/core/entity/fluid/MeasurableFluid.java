package team.starworld.shark.core.entity.fluid;

public interface MeasurableFluid {

    class Fluid extends team.starworld.shark.core.entity.fluid.Fluid implements MeasurableFluid { }

    static MeasurableFluid of (team.starworld.shark.core.entity.fluid.Fluid fluid) {
        if (fluid instanceof MeasurableFluid measurableFluid) return measurableFluid;
        return new Fluid();
    }

    // Default (g): (Volume * Density)
    default double getWeight (FluidStack stack) { return getVolume(stack) * getDensity(stack); }

    // Default (K): 0
    default double getTemperature (FluidStack stack) { return 0; }

    // Default (g/mL): 0
    default double getDensity (FluidStack stack) { return 0; }

    // Default (mL): 0
    default double getVolume (FluidStack stack) { return 0; }

}
