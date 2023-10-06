package team.starworld.shark.event.application.plugin;


import lombok.AllArgsConstructor;
import lombok.Getter;
import team.starworld.shark.data.plugin.JarPlugin;
import team.starworld.shark.data.plugin.PluginLoader;
import team.starworld.shark.event.Event;

@Getter @AllArgsConstructor
public class PluginLoadEvent extends Event {

    @Event.Property
    private final JarPlugin plugin;

    @Event.Property
    private final PluginLoader loader;

    public PluginLoader.PluginConfig getConfig () {
        return plugin.getConfig();
    }

}
