package team.starworld.shark.network.command;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import team.starworld.shark.api.annotation.command.Command;
import team.starworld.shark.event.network.data.CommandSetupEvent;
import team.starworld.shark.event.network.CommandInteractionEvent;

import java.util.Arrays;

public class SharkCommand {

    private final Class <?> clazz;

    @Getter @Setter
    private CommandData data;

    public SharkCommand (Class <?> clazz) {
        this.clazz = clazz;
    }

    public String getName () {
        return clazz.getAnnotation(Command.class).name();
    }
    public String getNamespace () { return clazz.getAnnotation(Command.class).namespace(); }
    public String getDescription () {
        return clazz.getAnnotation(Command.class).description();
    }

    @SneakyThrows
    public void run (CommandInteractionEvent event) {
        for (var method : Arrays.stream(clazz.getDeclaredMethods()).filter(i -> i.isAnnotationPresent(Command.Action.class)).toList()) {
            method.invoke(null, event);
        }
    }

    @SneakyThrows
    public void setup (CommandSetupEvent event) {
        for (var method : Arrays.stream(clazz.getDeclaredMethods()).filter(i -> i.isAnnotationPresent(Command.Setup.class)).toList()) {
            method.invoke(null, event);
        }
    }

}
