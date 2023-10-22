package team.starworld.shark.core.registries;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class ResourceLocation {

    public static final String DEFAULT_NAMESPACE = "shark";

    protected final @Getter String namespace;
    protected final @Getter String path;

    public static boolean isValidPath (String path) {
        return path.chars().allMatch(i -> isPathCharacter((char) i));
    }

    public static boolean isValidNamespace (String namespace) {
        return namespace.chars().allMatch(i -> isNamespaceCharacter((char) i));
    }

    public static boolean isPathCharacter (char character) {
        return character == '.' || character == '_' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '/';
    }

    public static boolean isNamespaceCharacter (char character) {
        return character == '_'|| character >= 'a' && character <= 'z' || character >= '0' && character <= '9';
    }


    public static ResourceLocation of (@Nullable String namespace, @NotNull String path) {
        return new ResourceLocation(Objects.requireNonNullElse(namespace, DEFAULT_NAMESPACE), path);
    }

    public static ResourceLocation of (@NotNull String location) {
        if (!location.contains(":")) return of(DEFAULT_NAMESPACE, location);
        else {
            var split = Arrays.stream(location.split(":")).toList();
            return of(split.get(0), split.get(1));
        }
    }

    protected ResourceLocation (@NotNull String namespace, @NotNull String path) {
        if (!isValidNamespace(namespace)) throw new IllegalArgumentException("Invalid namespace " + namespace);
        if (!isValidPath(path)) throw new IllegalArgumentException("Invalid path " + path);
        this.namespace = namespace;
        this.path = path;
    }

    @Override
    public boolean equals (Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ResourceLocation resourcelocation)) {
            return false;
        } else {
            return this.namespace.equals(resourcelocation.namespace) && this.path.equals(resourcelocation.path);
        }
    }

    @Override
    public int hashCode() {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }

    public String toLanguageKey () {
        return this.namespace + "." + this.path;
    }

    public String toLanguageKey (String type) {
        return type + "." + this.toLanguageKey();
    }

    @Override
    public String toString () {
        return "ResourceLocation [%s:%s]".formatted(this.namespace, this.path);
    }

}

