package team.starworld.shark.data.plugin;

import lombok.Data;
import lombok.Getter;
import team.starworld.shark.SharkBotApplication;
import team.starworld.shark.event.application.plugin.PluginLoadEvent;
import team.starworld.shark.event.bus.EventBus;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PluginLoader {

    public final List <String> pluginTypes = new ArrayList <> ();

    @Data @Getter
    public static class PluginConfig {

        private String name;
        private String id;
        private String mainClassName;

        private String description = "";
        private String version = "0.0.0";

    }

    @Getter
    private final List <JarPlugin> plugins = new ArrayList <> ();
    public final EventBus eventBus = new EventBus ("EventBus@PluginLoader");

    public PluginLoader () {
        this.pluginTypes.add(".jar");
        this.pluginTypes.add(".plugin");
    }

    public static Path getPluginPath (String path) {
        return Path.of(
            SharkBotApplication.getBaseDir().toString(),
            path
        );
    }

    public void load (Path path) {
        if (!path.toFile().exists()) path.toFile().mkdirs();
        var files = path.toFile().listFiles();
        if (files != null) for (var i : files) {
            if (pluginTypes.stream().anyMatch(type -> i.isFile() && i.getPath().endsWith(type))) {
                plugins.add(new JarPlugin(i, this));
            }
        }
    }

    public synchronized void loadPlugins () {
        this.plugins.forEach(
            plugin -> {
                var event = new PluginLoadEvent(plugin, this);
                event.setEventBus(eventBus);
                plugin.load(event);
            }
        );
    }

}
