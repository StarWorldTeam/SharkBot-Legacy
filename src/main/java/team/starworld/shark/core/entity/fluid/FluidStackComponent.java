package team.starworld.shark.core.entity.fluid;

import lombok.Getter;
import team.starworld.shark.core.entity.user.User;
import team.starworld.shark.network.chat.Component;
import team.starworld.shark.network.chat.TooltipComponent;

public class FluidStackComponent extends TooltipComponent {

    @Getter
    private final FluidStack stack;


    public FluidStackComponent (FluidStack stack, Component baseComponent) {
        super(baseComponent);
        this.stack = stack;
    }

    @Override
    public Component getTitle () {
        return stack.getName();
    }

    @Override
    public Component getDescription () {
        return stack.getTooltip();
    }

    @Override
    public Component getTitle (User user) {
        return stack.getName(user);
    }

    @Override
    public Component getDescription (User user) {
        return stack.getTooltip(user);
    }

}
