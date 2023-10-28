package team.starworld.shark.network;

import com.neovisionaries.ws.client.WebSocketFactory;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.starworld.shark.api.annotation.command.Command;
import team.starworld.shark.data.resource.Locale;
import team.starworld.shark.event.bus.EventBus;
import team.starworld.shark.event.network.CommandAutoCompleteEvent;
import team.starworld.shark.event.network.CommandInteractionEvent;
import team.starworld.shark.event.network.CommandListUpdateEvent;
import team.starworld.shark.event.network.data.CommandSetupEvent;
import team.starworld.shark.network.command.SharkCommand;
import team.starworld.shark.util.AnnotationUtil;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class SharkClient {

    @Getter
    private JDA client;

    public static Logger LOGGER = LoggerFactory.getLogger(SharkClient.class);

    @Data @Getter @Setter
    public static class ClientConfig {

        @Data @Getter @Setter
        public static class ProxyConfig {

            private boolean enabled = false;
            private int port = 7890;
            private String host = "127.0.0.1";

        }

        private String token = "";
        private ProxyConfig proxy = new ProxyConfig();

    }

    public final List <SharkCommand> commands = new ArrayList <> ();
    public final ClientConfig config;

    public EventBus eventBus = new EventBus ("EventBus@SharkClient");

    public SharkClient (ClientConfig config) {
        this.config = config;
    }

    public SharkClient start (Function <JDABuilder, JDABuilder> beforeBuild) {
        var builder = JDABuilder.createDefault(config.getToken())
            .setWebsocketFactory(getWebSocketFactory())
            .setHttpClientBuilder(getHttpClientBuilder())
            .addEventListeners(new SharkClient.EventListener(this));
        client = beforeBuild.apply(builder).build();
        this.addCommands(client.updateCommands());
        return this;
    }

    public void addCommands (CommandListUpdateAction action) {
        var event = new CommandListUpdateEvent();
        commands.addAll(AnnotationUtil.getInstances(Command.class).stream().map(SharkCommand::new).toList());
        eventBus.emit(event);
        var discordCommands = new ArrayList <> (event.getCommands());
        for (var command : commands) {
            var data = Commands.slash(command.getName(), command.getDescription());
            var sharkEvent = new CommandSetupEvent(command, data);
            eventBus.emit(sharkEvent);
            command.handleBeforeRegister(sharkEvent);
            for (var discordLocale : DiscordLocale.values()) {
                if (discordLocale == DiscordLocale.UNKNOWN) continue;
                var locale = Locale.fromDiscord(discordLocale);
                data.setNameLocalization(discordLocale, locale.getOrDefault("network.command.%s.%s.name".formatted(command.getNamespace(), command.getName()), command.getName()));
                data.setDescriptionLocalization(discordLocale, locale.getOrDefault("network.command.%s.%s.description".formatted(command.getNamespace(), command.getName()), command.getDescription()));
                for (var option : data.getOptions()) {
                    option.setNameLocalization(discordLocale, locale.getOrDefault("network.command.%s.%s.option.%s.name".formatted(command.getNamespace(), command.getName(), option.getName()), option.getName()));
                    option.setDescriptionLocalization(discordLocale, locale.getOrDefault("network.command.%s.%s.option.%s.description".formatted(command.getNamespace(), command.getName(), option.getName()), option.getDescription()));
                }
            }
            discordCommands.add(data);
        }
        for (var command : discordCommands) {
            action = action.addCommands(command);
        }
        if (discordCommands.size() > 0) action.queue();
    }

    public WebSocketFactory getWebSocketFactory () {
        var factory = new WebSocketFactory();
        if (this.config.proxy.enabled)
            factory.getProxySettings().setHost(config.proxy.host).setPort(config.proxy.port);
        return factory;
    }

    public OkHttpClient.Builder getHttpClientBuilder () {
        var builder = new OkHttpClient.Builder();
        if (this.config.proxy.enabled) builder.proxy(
            new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.proxy.host, config.proxy.port))
        );
        return builder;
    }

    public static class EventListener extends ListenerAdapter {

        private final SharkClient client;

        public EventListener (SharkClient client) {
            this.client = client;
        }

        @Override
        public void onSlashCommandInteraction (@NotNull SlashCommandInteractionEvent event) {
            var sharkEvent = new CommandInteractionEvent(event);
            for (var command : client.commands) {
                if (Objects.equals(command.getName(), event.getName())) {
                    command.run(sharkEvent);
                }
            }
            client.eventBus.emit(sharkEvent);
        }

        @Override
        public void onCommandAutoCompleteInteraction (@NotNull CommandAutoCompleteInteractionEvent event) {
            var sharkEvent = new CommandAutoCompleteEvent(event);
            client.eventBus.emit(sharkEvent);
            event.replyChoices(sharkEvent.getChoices()).queue();
        }

    }

}
