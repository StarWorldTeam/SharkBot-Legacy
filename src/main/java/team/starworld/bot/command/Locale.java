package team.starworld.bot.command;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import team.starworld.shark.SharkBotApplication;
import team.starworld.shark.api.annotation.command.Command;
import team.starworld.shark.event.network.CommandInteractionEvent;
import team.starworld.shark.event.network.data.CommandSetupEvent;
import team.starworld.shark.network.chat.Component;
import team.starworld.shark.util.Constants;

@Command(name = "locale")
public class Locale {

    public static void list (CommandInteractionEvent event) {
        event.reply(
            String.join(", ", SharkBotApplication.RESOURCE_LOADER.locales.values().stream().map(team.starworld.shark.data.resource.Locale::getName).toList())
        ).queue();
    }

    @Command.Action
    public static void run (CommandInteractionEvent event) {
        var option = event.getInteraction().getOption("locale");
        var list = event.getInteraction().getOption("list");
        if (list != null && list.getAsBoolean()) {
            list(event);
            return;
        }
        if (option != null) {
            var optionString = option.getAsString().trim();
            if (!SharkBotApplication.RESOURCE_LOADER.locales.containsKey(optionString)) {
                event.reply(
                    Component.translatable(
                        "network.command.shark.locale.reply.invalid",
                        String.join(", ", SharkBotApplication.RESOURCE_LOADER.locales.keySet())
                    )
                ).queue();
                return;
            }
            var locale = SharkBotApplication.RESOURCE_LOADER.locales.get(optionString);
            event.getUser().setLocale(locale);
            event.reply(Component.translatable("network.command.shark.locale.reply", locale.getName(), locale.getLocalizedName())).queue();
        } else {
            var locale = event.getUser().getLocale();
            event.reply("[%s] %s".formatted(locale.getName(), locale.getLocalizedName())).queue();
        }
    }

    @Command.Setup
    public static void setup (CommandSetupEvent event) {
        event.getCommandData().addOption(OptionType.BOOLEAN, "list", Constants.UNDEFINED, false, false);
        event.getCommandData()
            .addOption(OptionType.STRING, "locale", Constants.UNDEFINED, false, false);
    }

}
