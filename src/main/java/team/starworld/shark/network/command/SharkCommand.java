package team.starworld.shark.network.command;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import team.starworld.shark.api.annotation.command.Command;
import team.starworld.shark.event.network.CommandInteractionEvent;
import team.starworld.shark.event.network.data.CommandSetupEvent;
import team.starworld.shark.network.SharkClient;

import java.util.Arrays;
import java.util.Objects;

public class SharkCommand {

    private final Class <?> clazz;

    @Getter @Setter
    private CommandData data;

    @Getter
    private final SharkClient client;

    public SharkCommand (Class <?> clazz, SharkClient client) {
        this.clazz = clazz;
        this.client = client;
    }

    public String getName () {
        return clazz.getAnnotation(Command.class).name();
    }

    public String getFullName () {
        return getName() + (hasSubName() ? "-" + String.join("-", getSubName()) : "");
    }

    public String[] getSubName () {
        return clazz.getAnnotation(Command.class).subName();
    }

    public boolean hasSubName () {
        return getSubName().length != 0;
    }

    public SharkCommand getMainCommand () {
        return hasSubName() ? getClient().commands.stream().filter(i -> Objects.equals(i.getName(), this.getName()) && !i.hasSubName()).toList().get(0) : this;
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
