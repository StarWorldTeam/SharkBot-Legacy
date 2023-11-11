package team.starworld.shark.core.entity.fluid;

import org.jetbrains.annotations.Nullable;
import team.starworld.shark.core.entity.user.User;
import team.starworld.shark.core.registries.Registrable;
import team.starworld.shark.core.registries.SharkRegistries;
import team.starworld.shark.network.chat.Component;

import java.util.List;

public class Fluid implements FluidSupplier, Registrable {

    public Fluid () {}

    @Override
    public Fluid asFluid () { return this; }

    public int getColor () { return 0; }

    public Component getName (FluidStack stack, @Nullable User user) {
        return Component.translatable(this.getTranslationId("name"));
    }

    public void appendToolTip (FluidStack stack, List <Component> tooltip, @Nullable User user) {}


    public String getTranslationId (String attribute) {
        return SharkRegistries.FLUIDS.getKey(this).toLanguageKey("fluid", attribute);
    }


}
