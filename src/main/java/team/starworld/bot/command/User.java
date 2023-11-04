package team.starworld.bot.command;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import team.starworld.shark.api.annotation.command.Command;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.core.registries.SharkRegistries;
import team.starworld.shark.data.resource.Locale;
import team.starworld.shark.event.network.CommandInteractionEvent;
import team.starworld.shark.event.network.data.CommandSetupEvent;
import team.starworld.shark.util.Constants;

import java.util.List;
import java.util.Objects;


public class User {

    public static final ResourceLocation PERMISSION_ADMIN = ResourceLocation.of("admin");

    @Command(name = "user-give")
    public static class Give {

        @Command.Action
        public static void run (CommandInteractionEvent event) {
            event.needPermissions(PERMISSION_ADMIN);
            event.needOptions("user", "type", "count");
            var user = team.starworld.shark.core.entity.user.User.of(Objects.requireNonNull(event.getInteraction().getOption("user")).getAsUser());
            var type = ResourceLocation.of(Objects.requireNonNull(event.getInteraction().getOption("type")).getAsString());
        }

        @Command.Setup
        public static void setup (CommandSetupEvent event) {
            event.getCommandData().addOption(OptionType.USER, "user", Constants.UNDEFINED, true);
            event.getCommandData().addOption(OptionType.STRING, "type", Constants.UNDEFINED, true, true);
            event.getCommandData().addOption(OptionType.NUMBER, "count", Constants.UNDEFINED, true);
            event.getCommandData().addOption(OptionType.STRING, "tag", Constants.UNDEFINED, false);
            event.getCommandData().addOption(OptionType.STRING, "id", Constants.UNDEFINED, false);
            event.autoComplete(
                autoCompleteEvent -> {
                    var locale = Locale.fromDiscord(autoCompleteEvent.getEvent().getUserLocale());
                    var locations = List.of(ResourceLocation.of("energy"), SharkRegistries.Keys.ITEM.getLocation(), SharkRegistries.Keys.FLUID.getLocation());
                    locations.forEach(i -> autoCompleteEvent.add(locale.get(i.toLanguageKey("type", "name")), i.toString()));
                },
                "type"
            );
        }

    }

}
