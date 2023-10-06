package team.starworld.shark.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.undercouch.bson4jackson.BsonFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import team.starworld.shark.SharkBotApplication;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Supplier;

public class DataUtil {

    @AllArgsConstructor @Getter
    public enum FileType {

        YAML ("yml"), BSON ("bson"), JSON ("json");

        private final String fileType;

        public static JsonFactory getFactory (FileType fileType) {
            if (fileType == FileType.YAML) return new YAMLFactory();
            if (fileType == FileType.BSON) return new BsonFactory();
            return null;
        }

    }

    public static Path getDataPath () {
        return Path.of(
            SharkBotApplication.getBaseDir().toString(), "data"
        );
    }

    @SneakyThrows
    public static File getDataFile (String name, String fileType) {
        var path = getDataPath().toFile();
        var filePath = Path.of(path.getPath(), name + "." + fileType);
        filePath.getParent().toFile().mkdirs();
        return filePath.toFile();
    }

    @SneakyThrows
    public static <T> DataHolder <T> useData (String name, Class <T> clazz, Supplier <T> defaultValue, FileType fileType) {
        var factory = FileType.getFactory(fileType);
        final ObjectMapper mapper = factory == null ? new ObjectMapper() : new ObjectMapper(factory);
        var file = getDataFile(name, fileType.getFileType());
        if (!file.exists()) {
            file.createNewFile();
            mapper.writeValue(file, defaultValue.get());
        }
        return new DataHolder <> (name, file, mapper, clazz, defaultValue, fileType);
    }

    public static class DataHolder <T> {

        private final @Getter String name;
        private final @Getter File file;
        private final @Getter ObjectMapper mapper;
        private final @Getter Class <T> clazz;
        private final @Getter Supplier <T> defaultValue;
        private final @Getter FileType type;

        public DataHolder (String name, File file, ObjectMapper mapper, Class <T> clazz, Supplier <T> defaultValue, FileType type) {
            this.name = name;
            this.file = file;
            this.mapper = mapper;
            this.clazz = clazz;
            this.defaultValue = defaultValue;
            this.type = type;
        }

        @SneakyThrows
        public T get () { return mapper.readValue(file, clazz); }

        @SneakyThrows
        public void set (T value) { mapper.writeValue(file, value); }

        @SneakyThrows
        public void reset () { mapper.writeValue(file, defaultValue); }

    }


}
