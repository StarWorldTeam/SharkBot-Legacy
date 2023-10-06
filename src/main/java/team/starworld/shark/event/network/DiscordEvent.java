package team.starworld.shark.event.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.events.Event;

@AllArgsConstructor
@Getter
public class DiscordEvent <T extends Event> extends team.starworld.shark.event.Event {

    protected final @team.starworld.shark.event.Event.Property T event;

}
