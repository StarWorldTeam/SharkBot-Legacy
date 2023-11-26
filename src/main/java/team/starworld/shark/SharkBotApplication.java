package team.starworld.shark;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.core.registries.SharkRegistries;
import team.starworld.shark.data.plugin.PluginLoader;
import team.starworld.shark.data.resource.Locale;
import team.starworld.shark.data.resource.ResourceLoader;
import team.starworld.shark.event.application.CommonSetupEvent;
import team.starworld.shark.event.application.resource.ResourceLoadEvent;
import team.starworld.shark.event.bus.EventBus;
import team.starworld.shark.network.SharkClient;
import team.starworld.shark.util.ConfigUtil;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

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
    public static EventBus EVENT_BUS = new EventBus("EventBus@Main");

    public static Thread BOT_THREAD = new Thread(SharkBotApplication::startBot, "Shark-Bot");
    public static Thread BACKEND_THREAD = new Thread(SharkBotApplication::startBackend, "Shark-Backend");

    @SneakyThrows
    public static void main (String[] args) {
        SpringApplication.run(SharkBotApplication.class, args);
        CONFIG = ConfigUtil.useConfig(ResourceLocation.of("config"), Config.class, Config::new);
        SHARK_CLIENT = new SharkClient(CONFIG.clientConfig);
        BACKEND_THREAD.start();
        BOT_THREAD.start();
    }

    @SneakyThrows
    public static void startBot () {
        SharkRegistries.bootstrap();
        PLUGIN_LOADER.load(PluginLoader.getPluginPath("corePlugins"));
        PLUGIN_LOADER.load(PluginLoader.getPluginPath("plugins"));
        PLUGIN_LOADER.loadPlugins();
        defaultOperation();
        RESOURCE_LOADER.load(true, true, true);
        SHARK_CLIENT.start(builder -> builder);
        SHARK_CLIENT.getClient().awaitReady();
        EVENT_BUS.emit(new CommonSetupEvent());
    }

    @SneakyThrows
    public static void defaultOperation () {
        RESOURCE_LOADER.getEventBus().on(
            ResourceLoadEvent.class,
            event -> {
                var location = event.getLocation();
                var resources = event.getResources();
                if (Objects.equals(location.getPath(), "lang")) {
                    var mapper = new ObjectMapper();
                    for (var resource : resources) {
                        if (!Objects.requireNonNull(resource.getResource().getFilename()).endsWith(".json")) continue;
                        var name = Arrays.stream(resource.getResource().getFilename().split("\\.")).toList().get(0);
                        try {
                            var value = mapper.readValue(resource.getResource().getInputStream(), new TypeReference <Map <String, String>> () {});
                            if (!Locale.INSTANCES.containsKey(name)) Locale.INSTANCES.put(name, new Locale());
                            value.forEach(Locale.INSTANCES.get(name)::put);
                        } catch (Throwable ignored) {}
                    }
                }
            }
        );
    }

    public static void startBackend () {}

}
