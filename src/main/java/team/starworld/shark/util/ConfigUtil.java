package team.starworld.shark.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import team.starworld.shark.SharkBotApplication;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Supplier;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ConfigUtil {

    public static Path getConfigPath () {
        return Path.of(
            SharkBotApplication.getBaseDir().toString(), "config"
        );
    }

    @SneakyThrows
    public static File getConfigFile (String name) {
        var path = getConfigPath().toFile();
        path.mkdirs();
        return Path.of(path.getPath(), name + ".yml").toFile();
    }

    @SneakyThrows
    public static <T> T useConfig (String name, Class <T> clazz, Supplier <T> defaultValue) {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        var file = getConfigFile(name);
        if (!file.exists()) {
            file.createNewFile();
            mapper.writeValue(file, defaultValue.get());
        }
        return mapper.readValue(
            file,
            clazz
        );
    }

}
