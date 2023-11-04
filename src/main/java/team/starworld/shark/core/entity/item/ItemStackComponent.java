package team.starworld.shark.core.entity.item;

import lombok.Getter;
import team.starworld.shark.core.entity.user.User;
import team.starworld.shark.network.chat.TooltipComponent;
import team.starworld.shark.network.chat.Component;

public class ItemStackComponent extends TooltipComponent {

    @Getter
    private final ItemStack stack;


    public ItemStackComponent (ItemStack stack, Component baseComponent) {
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
