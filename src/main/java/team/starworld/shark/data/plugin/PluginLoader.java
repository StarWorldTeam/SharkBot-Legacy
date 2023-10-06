package team.starworld.shark.data.plugin;

import lombok.Data;
import lombok.Getter;
import team.starworld.shark.SharkBotApplication;
import team.starworld.shark.event.Event;
import team.starworld.shark.event.application.plugin.PluginLoadEvent;
import team.starworld.shark.event.bus.EventBus;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PluginLoader {

    @Data @Getter
    public static class PluginConfig {

        private String name;
        private String description = "";
        private String version = "0.0.0";

        private String mainClassName;

    }

    @Getter
    private final List <JarPlugin> plugins = new ArrayList <> ();
    public final EventBus <Event> eventBus = new EventBus <> ("EventBus@PluginLoader");

    public PluginLoader () {}

    public static Path getPluginPath () {
        return Path.of(
            SharkBotApplication.getBaseDir().toString(), "plugins"
        );
    }

    public void load () {
        if (!getPluginPath().toFile().exists()) getPluginPath().toFile().mkdirs();
        var files = getPluginPath().toFile().listFiles();
        if (files != null) for (var i : files) {
            if (i.isFile() && i.getPath().endsWith(".jar"))
                plugins.add(new JarPlugin(i));
        }
    }

    public void loadPlugins () {
        this.plugins.forEach(
            plugin -> {
                var event = new PluginLoadEvent(plugin, this);
                event.setEventBus(eventBus);
                plugin.load(event);
            }
        );
    }

}