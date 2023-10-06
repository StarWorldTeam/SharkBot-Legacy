package team.starworld.shark.network.command;

import lombok.SneakyThrows;
import team.starworld.shark.api.annotation.command.Command;
import team.starworld.shark.event.network.data.CommandBeforeRegisterEvent;
import team.starworld.shark.event.network.CommandInteractionEvent;

import java.util.Arrays;

public class SharkCommand {

    private final Class <?> clazz;

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
    public void handleBeforeRegister (CommandBeforeRegisterEvent event) {
        for (var method : Arrays.stream(clazz.getDeclaredMethods()).filter(i -> i.isAnnotationPresent(Command.BeforeRegister.class)).toList()) {
            method.invoke(null, event);
        }
    }

}
