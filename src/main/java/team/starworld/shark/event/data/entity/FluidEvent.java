package team.starworld.shark.event.data.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.starworld.shark.core.entity.fluid.FluidStack;
import team.starworld.shark.core.entity.user.User;
import team.starworld.shark.event.Event;
import team.starworld.shark.network.chat.Component;

import java.util.ArrayList;

public class FluidEvent {

    @Getter @RequiredArgsConstructor
    public static class GetNameEvent extends Event {

        @Setter
        private Component name;

        @NotNull
        private final FluidStack fluidStack;

        @Nullable
        private final User user;

    }

    @Getter @RequiredArgsConstructor
    public static class AppendTooltipEvent extends Event {

        private final ArrayList <Component> tooltip;

        @NotNull
        private final FluidStack fluidStack;

        @Nullable
        private final User user;

    }

}
