package team.starworld.shark.util;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ObjectUtil {

    public static <T> Optional <T> toOptional (@Nullable T value) {
        return value == null ? Optional.empty() : Optional.of(value);
    }

    public static <T> T tryExecute (Runnable runnable, Function <Optional <Throwable>, T> finish) {
        Throwable throwable = null;
        try {
            runnable.run();
        } catch (Throwable error) {
            throwable = error;
        }
        return finish.apply(Optional.ofNullable(throwable));
    }

    public static String sliceString (String string, int start, int end) {
        return string.substring(start, end < 0 ? string.length() - end + 1 : end);
    }

    public static <T> List <T> sliceList (List <T> list, int start, int end) {
        return list.subList(start, end < 0 ? list.size() - end + 1 : end);
    }

}
