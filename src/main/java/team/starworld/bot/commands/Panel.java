package team.starworld.bot.commands;

import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import team.starworld.shark.api.annotation.command.Command;
import team.starworld.shark.event.network.CommandInteractionEvent;
import team.starworld.shark.util.PlayWrightUtil;

@Command(name = "panel")
public class Panel {

    @Command.Action
    public static void run (CommandInteractionEvent event) {
        event.reply(
            new MessageCreateBuilder().setFiles(PlayWrightUtil.getDiscordImage(PlayWrightUtil.makeScreenShot(
                ""
            )))
        ).queue();
    }

}
