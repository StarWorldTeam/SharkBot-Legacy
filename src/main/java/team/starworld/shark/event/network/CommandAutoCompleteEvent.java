package team.starworld.shark.event.network;

import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.utils.data.DataObject;
import team.starworld.shark.event.Event;

import java.util.ArrayList;
import java.util.List;

public class CommandAutoCompleteEvent extends DiscordEvent <CommandAutoCompleteInteractionEvent> {

    private final @Getter @Event.Property List <Command.Choice> choices = new ArrayList <> ();

    public CommandAutoCompleteEvent (CommandAutoCompleteInteractionEvent event) {
        super(event);
    }

    public CommandAutoCompleteEvent add (String name, String value) {
        choices.add(new Command.Choice(name, value));
        return this;
    }

    public CommandAutoCompleteEvent add (DataObject object) {
        choices.add(new Command.Choice(object));
        return this;
    }

    public CommandAutoCompleteEvent add (String name, long number) {
        choices.add(new Command.Choice(name, number));
        return this;
    }

    public CommandAutoCompleteEvent add (String name, double number) {
        choices.add(new Command.Choice(name, number));
        return this;
    }

    public CommandAutoCompleteEvent add (Command.Choice choice) {
        choices.add(choice);
        return this;
    }

    public boolean isCommand (String name) {
        return event.getFullCommandName().equals(name);
    }

    public boolean isOption (String name) {
        return event.getFocusedOption().getName().equals(name);
    }

}
