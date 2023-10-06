package team.starworld.shark.event.network;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import team.starworld.shark.event.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class CommandListUpdateEvent extends Event {

    private final @Event.Property List <CommandData> commands = new ArrayList <> ();

    public void addCommands (CommandData... data) {
        commands.addAll(Arrays.asList(data));
    }

}
