package team.starworld.shark;

import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import team.starworld.shark.data.resource.ResourceLoader;
import team.starworld.shark.network.SharkClient;
import team.starworld.shark.util.ConfigUtil;

import java.nio.file.Path;

@Service
public class SharkBotApplication {

    public static Logger LOGGER = LoggerFactory.getLogger(SharkBotApplication.class);

    @Data @Getter
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

    public static void main (String[] args) {
        SpringApplication.run(SharkBotApplication.class, args);
        RESOURCE_LOADER.init();
        CONFIG = ConfigUtil.useConfig("shark", Config.class, Config::new);
        SHARK_CLIENT = new SharkClient(CONFIG.clientConfig);
        SHARK_CLIENT.eventBus.all(
            event -> SharkClient.LOGGER.info("[Event] [%s] %s".formatted(event.getEventName(), event.getDisplayString()))
        );
        SHARK_CLIENT.start(builder -> builder);
    }



}
