package team.starworld.shark.event.network.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import team.starworld.shark.event.bus.EventCallback;
import team.starworld.shark.event.network.CommandAutoCompleteEvent;
import team.starworld.shark.network.command.SharkCommand;
import team.starworld.shark.event.Event;
import team.starworld.shark.util.Constants;

import java.util.Arrays;
import java.util.function.Consumer;

@Getter @AllArgsConstructor
public class CommandSetupEvent extends Event {


    private final @Event.Property SharkCommand command;
    private final @Event.Property SlashCommandData commandData;

    public CommandSetupEvent addOption (OptionType type, String name) {
        return addOption(type, name, Constants.UNDEFINED);
    }

    public CommandSetupEvent addOption (OptionType type, String name, String description) {
        this.commandData.addOption(type, name, description);
        return this;
    }

    public CommandSetupEvent autoComplete (Consumer <CommandAutoCompleteEvent> callback, String... options) {
        getEventBus().on(
            CommandAutoCompleteEvent.class,
            event -> {
                if (!Arrays.stream(options).toList().contains(event.getEvent().getFocusedOption().getName())) return;
                if (event.isCommand(this.getCommandData().getName())) callback.accept(event);
            }
        );
        return this;
    }

    public CommandSetupEvent autoComplete (EventCallback <CommandAutoCompleteEvent> callback, String... options) {
        getEventBus().on(
            CommandAutoCompleteEvent.class,
            (event, eventCallback) -> {
                if (!Arrays.stream(options).toList().contains(event.getEvent().getFocusedOption().getName())) return;
                if (event.isCommand(this.getCommandData().getName())) callback.call(event, eventCallback);
            }
        );
        return this;
    }

}
