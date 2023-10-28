package team.starworld.shark.util;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ObjectUtil {

    public static <T> Optional <T> toOptional (@Nullable T value) {
        return value == null ? Optional.empty() : Optional.of(value);
    }

}
