package team.starworld.shark.data.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import lombok.SneakyThrows;
import team.starworld.shark.SharkBotApplication;
import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.event.application.plugin.PluginLoadEvent;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class JarPlugin {

    @Getter
    private final File file;
    private PluginLoader.PluginConfig config;
    private Class <?> mainClass;
    private Object instance;
    private URLClassLoader loader;
    @Getter private PluginLoader pluginLoader;
    @Getter private boolean loaded = true;

    public JarPlugin (File file, PluginLoader loader) {
        this.file = file;
    }

    @SneakyThrows
    public void load (PluginLoadEvent event) {
        event.getEventBus().emit(event);
        if (event.isCancelled()) return;
        if (!ResourceLocation.isValidNamespace(this.getConfig().getId())) return;
        setLoader(new URLClassLoader(new URL[] {getURL()}, SharkBotApplication.class.getClassLoader()));
        setMainClass(loader.loadClass(getConfig().getMainClassName()));
        if (mainClass.isAnnotationPresent(Plugin.class)) {
            setInstance(mainClass.getConstructor(PluginLoadEvent.class).newInstance(event));
            setLoaded();
        }
    }

    public String getPluginId () {
        return this.getConfig().getId();
    }

    @SneakyThrows
    public URL getURL () {
        return new URL("file:" + this.getFile().getPath());
    }

    public Object getInstance () {
        if (this.instance == null) throw new RuntimeException("Plugin not constructed yet.");
        return this.instance;
    }

    public void setInstance (Object object) {
        if (this.instance == null) this.instance = object;
    }

    public URLClassLoader getLoader () {
        if (this.loader == null) throw new RuntimeException("Plugin not loaded yet.");
        return this.loader;
    }

    public void setLoader (URLClassLoader loader) {
        if (this.loader == null) this.loader = loader;
    }

    public Class <?> getMainClass () {
        if (this.mainClass == null) throw new RuntimeException("Plugin not loaded yet.");
        return this.mainClass;
    }

    public void setMainClass (Class <?> clazz) {
        if (this.mainClass == null) this.mainClass = clazz;
    }

    public void setLoaded () {
        this.loaded = true;
    }

    public Plugin getAnnotation () {
        return this.getMainClass().getAnnotation(Plugin.class);
    }

    @SneakyThrows
    public JarFile getJarFile () {
        return new JarFile(this.getFile());
    }

    public String getName () {
        return getConfig().getName();
    }

    @SneakyThrows
    public PluginLoader.PluginConfig getConfig () {
        if (this.config != null) return this.config;
        var jarFile = getJarFile();
        var stream = jarFile.getInputStream(jarFile.getEntry("plugin.yml"));
        var mapper = new ObjectMapper(new YAMLFactory());
        this.config = mapper.readValue(stream, PluginLoader.PluginConfig.class);
        return getConfig();
    }

    @Override
    public String toString () {
        return "Plugin [%s]".formatted(this.getConfig());
    }

}
