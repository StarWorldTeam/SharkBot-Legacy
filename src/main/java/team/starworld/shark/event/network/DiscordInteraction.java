package team.starworld.shark.event.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.Interaction;
import team.starworld.shark.event.Event;

@Getter @AllArgsConstructor
public class DiscordInteraction <T extends Interaction> extends Event {

    protected final @team.starworld.shark.event.Event.Property T interaction;

}
