package team.starworld.shark.event.network;

import j2html.tags.DomContent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import team.starworld.shark.SharkBotApplication;
import team.starworld.shark.core.entity.user.User;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.network.chat.Component;
import team.starworld.shark.network.command.SharkCommand;
import team.starworld.shark.network.command.SharkOptionMapping;
import team.starworld.shark.util.PlayWrightUtil;

import java.util.Objects;
import java.util.Optional;

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

    public ReplyCallbackAction replyImage (Component component) {
        return reply(
            new MessageCreateBuilder().setFiles(PlayWrightUtil.getDiscordImage(PlayWrightUtil.makeScreenShot(component.getDomContent(User.of(interaction).getLocale()))))
        );
    }

    public ReplyCallbackAction replyImage (String html) {
        return reply(
            new MessageCreateBuilder().setFiles(PlayWrightUtil.getDiscordImage(PlayWrightUtil.makeScreenShot(html)))
        );
    }

    public ReplyCallbackAction replyImage (DomContent content) {
        return reply(
            new MessageCreateBuilder().setFiles(PlayWrightUtil.getDiscordImage(PlayWrightUtil.makeScreenShot(content.render())))
        );
    }

    @NotNull
    public SharkCommand getCommand () {
        for (var i : SharkBotApplication.SHARK_CLIENT.commands) {
            if (i.getData().getName().equals(getInteraction().getName()))
                return i;
        }
        throw new NullPointerException();
    }

    public User getUser () {
        return User.of(this.interaction);
    }

    public void needPermissions (ResourceLocation... permissions) {
        for (var permission : permissions) {
            if (!getUser().hasPermission(permission)) {
                replyPermissionDenied().queue();
                return;
            }
        }
    }

    public ReplyCallbackAction replyPermissionDenied () {
        return reply(Component.translatable("message.shark.general.permission_denied"));
    }

    public ReplyCallbackAction replyNeedOption (String option) {
        var command = getCommand();
        return reply(Component.translatable("message.shark.general.need_option", getUser().getLocale().getOrDefault("network.command.%s.%s.option.%s.name".formatted(command.getNamespace(), command.getName(), option), option)));
    }

    public void needOptions (String... options) {
        for (var option : options) {
            if (getInteraction().getOption(option) != null) continue;
            replyNeedOption(option).queue();
            return;
        }
    }

    public MessageCreateAction send (String content) {
        return getInteraction().getGuildChannel().sendMessage(content);
    }

    public MessageCreateAction send (MessageCreateData content) {
        return getInteraction().getGuildChannel().sendMessage(content);
    }

    public MessageCreateAction send (MessageCreateBuilder content) {
        return getInteraction().getGuildChannel().sendMessage(content.build());
    }

    public MessageCreateAction send (Component content) {
        return getInteraction().getGuildChannel().sendMessage(content.getString(getUser().getLocale()));
    }

    public MessageCreateAction sendImage (Component component) {
        return send(
            new MessageCreateBuilder().setFiles(PlayWrightUtil.getDiscordImage(PlayWrightUtil.makeScreenShot(component.getDomContent(User.of(interaction).getLocale()))))
        );
    }

    public MessageCreateAction sendImage (String html) {
        return send(
            new MessageCreateBuilder().setFiles(PlayWrightUtil.getDiscordImage(PlayWrightUtil.makeScreenShot(html)))
        );
    }

    public MessageCreateAction sendImage (DomContent content) {
        return send(
            new MessageCreateBuilder().setFiles(PlayWrightUtil.getDiscordImage(PlayWrightUtil.makeScreenShot(content.render())))
        );
    }

    public SharkOptionMapping getOptionSafe (String name) {
        return new SharkOptionMapping(Objects.requireNonNull(getInteraction().getOption(name)));
    }

    public Optional <SharkOptionMapping> getOption (String name) {
        var option = getInteraction().getOption(name);
        return option == null ? Optional.empty() : Optional.of(new SharkOptionMapping(option));
    }


}
