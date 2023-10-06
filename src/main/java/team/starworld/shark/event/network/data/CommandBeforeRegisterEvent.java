package team.starworld.shark.event.network.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import team.starworld.shark.network.command.SharkCommand;
import team.starworld.shark.event.Event;
import team.starworld.shark.util.Constants;

@Getter @AllArgsConstructor
public class CommandBeforeRegisterEvent extends Event {


    private final @Event.Property SharkCommand command;
    private final @Event.Property SlashCommandData commandData;

    public CommandBeforeRegisterEvent addOption (OptionType type, String name) {
        return addOption(type, name, Constants.UNDEFINED);
    }

    public CommandBeforeRegisterEvent addOption (OptionType type, String name, String description) {
        this.commandData.addOption(type, name, description);
        return this;
    }

}
