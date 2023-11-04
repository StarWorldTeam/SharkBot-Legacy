package team.starworld.shark;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import team.starworld.shark.core.registries.Registries;
import team.starworld.shark.data.plugin.PluginLoader;
import team.starworld.shark.data.resource.ResourceLoader;
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
    public static PluginLoader PLUGIN_LOADER = new PluginLoader();
    public static ResourceLoader RESOURCE_LOADER = new ResourceLoader(new PluginLoader[] { PLUGIN_LOADER });

    public static Thread BOT_THREAD = new Thread(SharkBotApplication::startBot, "Shark-Bot");
    public static Thread BACKEND_THREAD = new Thread(SharkBotApplication::startBackend, "Shark-Backend");

    @SneakyThrows
    public static void main (String[] args) {
        SpringApplication.run(SharkBotApplication.class, args);
        CONFIG = ConfigUtil.useConfig("shark", Config.class, Config::new);
        SHARK_CLIENT = new SharkClient(CONFIG.clientConfig);
        BACKEND_THREAD.start();
        BOT_THREAD.start();
    }

    @SneakyThrows
    public static void startBot () {
        Registries.bootstrap();
        PLUGIN_LOADER.load(PluginLoader.getPluginPath("corePlugins"));
        PLUGIN_LOADER.load(PluginLoader.getPluginPath("plugins"));
        PLUGIN_LOADER.loadPlugins();
        RESOURCE_LOADER.load();
        SHARK_CLIENT.start(builder -> builder);
        SHARK_CLIENT.getClient().awaitReady();
    }

    public static void startBackend () {}

}
