package team.starworld.shark.event.network;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class CommandInteractionEvent extends DiscordEvent <SlashCommandInteractionEvent> {

    public CommandInteractionEvent (SlashCommandInteractionEvent event) {
        super(event);
    }

    public ReplyCallbackAction reply (MessageCreateBuilder builder) {
        return event.reply(builder.build());
    }

    public ReplyCallbackAction reply (MessageCreateData data) {
        return event.reply(data);
    }

    public ReplyCallbackAction reply (String message) {
        return event.reply(message);
    }

}
