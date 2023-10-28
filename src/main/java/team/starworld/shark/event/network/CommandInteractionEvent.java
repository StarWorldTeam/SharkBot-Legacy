package team.starworld.shark.event.network;

import j2html.tags.DomContent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import team.starworld.shark.core.entity.user.User;
import team.starworld.shark.network.chat.Component;
import team.starworld.shark.util.PlayWrightUtil;

public class CommandInteractionEvent extends DiscordInteraction <SlashCommandInteractionEvent> {

    public CommandInteractionEvent (SlashCommandInteractionEvent event) {
        super(event);
    }

    public ReplyCallbackAction reply (MessageCreateBuilder builder) {
        return interaction.reply(builder.build());
    }

    public ReplyCallbackAction reply (MessageCreateData data) {
        return interaction.reply(data);
    }

    public ReplyCallbackAction reply (String message) {
        return interaction.reply(message);
    }

    public ReplyCallbackAction reply (Component component) {
        return reply(
            component.getString(User.of(interaction).getLocale())
        );
    }

    public ReplyCallbackAction replyHTML (Component component) {
        return reply(
            new MessageCreateBuilder().setFiles(PlayWrightUtil.getDiscordImage(PlayWrightUtil.makeScreenShot(
                component.getDomContent(User.of(interaction).getLocale())
            )))
        );
    }

    public ReplyCallbackAction replyHTML (String html) {
        return reply(
            new MessageCreateBuilder().setFiles(PlayWrightUtil.getDiscordImage(PlayWrightUtil.makeScreenShot(
                html
            )))
        );
    }

    public ReplyCallbackAction replyHTML (DomContent content) {
        return reply(
            new MessageCreateBuilder().setFiles(PlayWrightUtil.getDiscordImage(PlayWrightUtil.makeScreenShot(
                content.render()
            )))
        );
    }

    public User getUser () {
        return User.of(this.interaction);
    }

}
