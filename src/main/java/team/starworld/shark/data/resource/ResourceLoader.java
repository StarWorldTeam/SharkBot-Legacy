package team.starworld.shark.data.resource;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;
import team.starworld.shark.SharkBotApplication;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.data.plugin.PluginLoader;
import team.starworld.shark.event.application.resource.AllResourceLoadedEvent;
import team.starworld.shark.event.application.resource.ResourceLoadEvent;
import team.starworld.shark.event.bus.EventBus;

import java.net.URI;
import java.nio.file.Path;
import java.util.*;

public class ResourceLoader {

    @Getter
    private final EventBus eventBus = new EventBus("EventBus@ResourceLoader");

    public static Path getResourcePackPath () {
        return Path.of(
            SharkBotApplication.getBaseDir().toString(), "resourcepacks"
        );
    }

    public static String getJarResource (String jarFile, String... files) {
        return "jar:file:/%s!/%s".formatted(jarFile.replaceAll("\\\\", "/"), String.join("/", files));
    }

    @Getter
    private final PluginLoader[] pluginLoaders;

    public ResourceLoader (PluginLoader[] pluginLoaders) {
        this.pluginLoaders = pluginLoaders;
    }

    public synchronized void load (boolean sharkResource, boolean pluginResource, boolean fileResource) {
        if (sharkResource) try {
            load("classpath:assets/*/*/**/*.*", "classpath:assets");
        } catch (Throwable ignored) {}
        if (pluginResource) try {
            for (var plugins : pluginLoaders) {
                for (var plugin : plugins.getPlugins()) {
                    var baseDir = URI.create(getJarResource(plugin.getFile().getAbsolutePath(), "assets")).toString();
                    var path = baseDir + "/*/*/**/*.*";
                    load(path, baseDir);
                }
            }
        } catch (Throwable ignored) {}
        if (fileResource) try {
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
        eventBus.emit(new AllResourceLoadedEvent(this));
    }

    public static List <String> splitPath (String path) {
        return Arrays.stream(String.join("/", path.split("\\\\")).split("/")).filter(i -> i.length() > 0).toList();
    }

    public static List <String> splitPath (Path path) {
        return splitPath(path.toString());
    }

    /**
     * @see UrlResource
     * 加载URL资源
     */
    @SneakyThrows
    public void loadURLResource (UrlResource resource, Map <ResourceLocation, List <SharkResource>> resourceMap, String rawBaseDir) {
        var realPath = splitPath(resource.getURI().toString().substring(rawBaseDir.length()));
        if (realPath.size() >= 2) {
            var type = ResourceLocation.of(realPath.get(0), realPath.get(1));
            var resourcePath = realPath.subList(2, realPath.size());
            var resourceLocation = ResourceLocation.of(realPath.get(0), realPath.get(1) + "/" + String.join("/", resourcePath));
            var splitRawBaseDir = Arrays.stream(rawBaseDir.split("!/")).toList();
            var baseDir = String.join("!/", splitRawBaseDir.subList(1, splitRawBaseDir.size()));
            var sharkResource = new SharkResource(
                type,
                resource, baseDir, String.join("/", resourcePath), resourceLocation
            );
            if (!resourceMap.containsKey(type)) resourceMap.put(type, new ArrayList <> ());
            resourceMap.get(type).add(sharkResource);
        }
    }

    /**
     * @see FileSystemResource
     * 加载文件资源
     */
    @SneakyThrows
    public void loadFileSystemResource (FileSystemResource resource, Map <ResourceLocation, List <SharkResource>> resourceMap, String baseDir) {
        var list = splitPath(resource.getPath());
        var baseDirPath = splitPath(ResourceUtils.getURL(baseDir).getPath());
        list = list.subList(baseDirPath.size(), list.size());
        if (list.size() >= 2) {
            var type = ResourceLocation.of(list.get(0), list.get(1));
            var resourcePath = String.join("/", list.subList(2, list.size()));
            var sharkResource = new SharkResource(
                type,
                resource, String.join("/", baseDirPath), resourcePath,
                ResourceLocation.of(type.getNamespace(), String.join("/", splitPath(type.getPath() + "/" + resourcePath)))
            );
            if (!resourceMap.containsKey(type)) resourceMap.put(type, new ArrayList <> ());
            resourceMap.get(type).add(sharkResource);
        }
    }

    /**
     * @see ClassPathResource
     * 加载类内部资源
     */
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
        this.getEventBus().emit(new ResourceLoadEvent(location, resources, this));
    }

}
