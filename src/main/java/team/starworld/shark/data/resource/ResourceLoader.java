package team.starworld.shark.data.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;
import team.starworld.shark.SharkBotApplication;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.event.application.resources.ResourceLoadEvent;
import team.starworld.shark.event.bus.EventBus;

import java.net.URI;
import java.nio.file.Path;
import java.util.*;

public class ResourceLoader {

    public final EventBus eventBus = new EventBus("EventBus@ResourceLoader");

    public Map <String, Locale> locales = new HashMap <> ();

    public static Path getResourcePackPath () {
        return Path.of(
            SharkBotApplication.getBaseDir().toString(), "resourcepacks"
        );
    }

    public static String getJarResource (String jarFile, String... files) {
        return "jar:file:/%s!/%s".formatted(jarFile.replaceAll("\\\\", "/"), String.join("/", files));
    }

    public synchronized void load () {
        try {
            load("classpath:assets/*/*/**/*.*", "classpath:assets");
        } catch (Throwable ignored) {}
        try {
            var plugins = SharkBotApplication.PLUGIN_LOADER.getPlugins();
            for (var plugin : plugins) {
                var baseDir = URI.create(getJarResource(plugin.getFile().getAbsolutePath(), "assets")).toString();
                var path = baseDir + "/*/*/**/*.*";
                load(path, baseDir);
            }
        } catch (Throwable ignored) {}
        try {
            if (!getResourcePackPath().toFile().exists()) getResourcePackPath().toFile().mkdirs();
            for (var file : Objects.requireNonNull(getResourcePackPath().toFile().listFiles())) {
                if (!file.isDirectory()) continue;
                try {
                    load(
                        "file:" + Path.of(getResourcePackPath().toString(), file.getName(), "assets") + "/*/*/**/*.*",
                        Path.of(getResourcePackPath().toString(), file.getName(), "assets").toString()
                    );
                } catch (Throwable ignored) {}
            }
        } catch (Throwable ignored) {}
    }

    public static List <String> splitPath (String path) {
        return Arrays.stream(String.join("/", path.split("\\\\")).split("/")).filter(i -> i.length() > 0).toList();
    }

    public static List <String> splitPath (Path path) {
        return splitPath(path.toString());
    }

    @SneakyThrows
    public void loadURLResource (UrlResource resource, Map <ResourceLocation, List <SharkResource>> resourceMap, String baseDir) {
        var realPath = splitPath(resource.getURI().toString().substring(baseDir.length()));
        if (realPath.size() >= 2) {
            var location = ResourceLocation.of(realPath.get(0), realPath.get(1));
            var resourcePath = realPath.subList(2, realPath.size());
            var resourceLocation = ResourceLocation.of(realPath.get(0), realPath.get(1) + "/" + String.join("/", resourcePath));
            var split = Arrays.stream(baseDir.split("!/")).toList();
            var sharkResource = new SharkResource(
                location,
                resource, String.join("!/", split.subList(1, split.size())), String.join("/", resourcePath), resourceLocation
            );
            if (!resourceMap.containsKey(location)) resourceMap.put(location, new ArrayList <> ());
            resourceMap.get(location).add(sharkResource);
        }
    }

    @SneakyThrows
    public void loadFileSystemResource (FileSystemResource resource, Map <ResourceLocation, List <SharkResource>> resourceMap, String baseDir) {
        var list = splitPath(resource.getPath());
        var baseDirPath = splitPath(ResourceUtils.getURL(baseDir).getPath());
        list = list.subList(baseDirPath.size(), list.size());
        if (list.size() >= 2) {
            var location = ResourceLocation.of(list.get(0), list.get(1));
            var resourcePath = String.join("/", list.subList(2, list.size()));
            var sharkResource = new SharkResource(
                location,
                resource, String.join("/", baseDirPath), resourcePath,
                ResourceLocation.of(location.getNamespace(), String.join("/", splitPath(location.getPath() + "/" + resourcePath)))
            );
            if (!resourceMap.containsKey(location)) resourceMap.put(location, new ArrayList <> ());
            resourceMap.get(location).add(sharkResource);
        }
    }

    @SneakyThrows
    public void loadClassPathResource (ClassPathResource resource, Map <ResourceLocation, List <SharkResource>> resourceMap, String baseDir) {
        var baseDirFiles = Arrays.stream(new PathMatchingResourcePatternResolver().getResources("%s".formatted(baseDir))).toList();
        if (baseDirFiles.size() == 0 || !(baseDirFiles.get(0) instanceof ClassPathResource baseDirFile)) return;
        var resourceBaseDir = splitPath(baseDirFile.getPath());
        var locationPath = splitPath(resource.getPath()).subList(resourceBaseDir.size(), splitPath(resource.getPath()).size());
        if (locationPath.size() >= 2) {
            var typeLocation = ResourceLocation.of(locationPath.get(0), locationPath.get(1));
            var resourcePath = String.join("/", locationPath.subList(2, locationPath.size()));
            var sharkResource = new SharkResource(
                typeLocation,
                resource, String.join("/", resourceBaseDir), resourcePath,
                ResourceLocation.of(typeLocation.getNamespace(), String.join("/", splitPath(resourcePath).subList(1, splitPath(resourcePath).size())))
            );
            if (!resourceMap.containsKey(typeLocation)) resourceMap.put(typeLocation, new ArrayList <> ());
            resourceMap.get(typeLocation).add(sharkResource);
        }
    }

    @SneakyThrows
    public void load (String path, String baseDir) {
        var urlList = new PathMatchingResourcePatternResolver().getResources(path);
        var resourceMap = new HashMap <ResourceLocation, List <SharkResource>> ();
        for (var i : urlList) {
            if (i instanceof UrlResource resource) loadURLResource(resource, resourceMap, baseDir);
            else if (i instanceof FileSystemResource resource) loadFileSystemResource(resource, resourceMap, baseDir);
            else if (i instanceof ClassPathResource resource) loadClassPathResource(resource, resourceMap, baseDir);
        }
        resourceMap.forEach(this::loadResource);
    }

    @SneakyThrows
    public void loadResource (ResourceLocation location, List <SharkResource> resources) {
        if (Objects.equals(location.getPath(), "lang")) {
            var mapper = new ObjectMapper();
            for (var resource : resources) {
                if (!Objects.requireNonNull(resource.getResource().getFilename()).endsWith(".json")) continue;
                var name = Arrays.stream(resource.getResource().getFilename().split("\\.")).toList().get(0);
                var value = mapper.readValue(resource.getResource().getInputStream(), new TypeReference <Map <String, String>> () {});
                if (!locales.containsKey(name)) locales.put(name, new Locale());
                value.forEach(locales.get(name)::put);
            }
        }
        eventBus.emit(new ResourceLoadEvent(location, resources));
    }

}
