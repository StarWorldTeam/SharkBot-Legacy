package team.starworld.shark;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import team.starworld.shark.data.plugin.PluginLoader;
import team.starworld.shark.data.resource.ResourceLoader;
import team.starworld.shark.event.bus.EventBus;
import team.starworld.shark.network.SharkClient;
import team.starworld.shark.util.ConfigUtil;

import java.nio.file.Path;

@Service
public class SharkBotApplication {

    public static Logger LOGGER = LoggerFactory.getLogger(SharkBotApplication.class);

    @Data @Getter @Setter
    public static class Config {

        SharkClient.ClientConfig clientConfig = new SharkClient.ClientConfig();

        private String defaultLanguage = "zh_cn";

    }

    public static Path getBaseDir () {
        return Path.of(System.getProperty("user.dir"), "shark");
    }

    public static Config CONFIG;
    public static SharkClient SHARK_CLIENT;
    public static ResourceLoader RESOURCE_LOADER = new ResourceLoader();
    public static PluginLoader PLUGIN_LOADER = new PluginLoader();

    public static void main (String[] args) throws InterruptedException {
        SpringApplication.run(SharkBotApplication.class, args);
        CONFIG = ConfigUtil.useConfig("shark", Config.class, Config::new);
        SHARK_CLIENT = new SharkClient(CONFIG.clientConfig);
        EventBus.INSTANCES.forEach(
            bus -> bus.all(
                event -> SharkClient.LOGGER.info("[%s] [%s] %s".formatted(bus.getName(), event.getEventName(), event.getDisplayString()))
            )
        );
        PLUGIN_LOADER.load();
        PLUGIN_LOADER.loadPlugins();
        RESOURCE_LOADER.load();
        SHARK_CLIENT.start(builder -> builder);
        SHARK_CLIENT.getClient().awaitReady();
    }



}
