package team.starworld.shark.event.application.plugin;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import team.starworld.shark.data.plugin.JarPlugin;
import team.starworld.shark.data.plugin.PluginLoader;
import team.starworld.shark.event.Event;

@Getter @RequiredArgsConstructor
public class PluginLoadEvent extends Event {

    @Event.Property
    private final JarPlugin plugin;

    @Event.Property
    private final PluginLoader loader;

    @Setter @Getter
    private boolean cancelled;

    public PluginLoader.PluginConfig getConfig () {
        return plugin.getConfig();
    }

}
