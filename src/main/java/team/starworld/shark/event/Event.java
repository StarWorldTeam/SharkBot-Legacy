package team.starworld.shark.event;

import lombok.Getter;
import lombok.Setter;
import team.starworld.shark.event.bus.EventBus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Event {

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Property {}

    @Setter @Getter
    private EventBus <?> eventBus;

    public String getEventName () {
        return this.getClass().getSimpleName();
    }

    public String getDisplayString () {
        List <String> properties = new ArrayList <> ();
        for (var i : Arrays.stream(this.getClass().getDeclaredFields()).filter(i -> i.isAnnotationPresent(Property.class)).toList()) {
            try {
                i.setAccessible(true);
                properties.add(i.getName() + "=" + i.get(this));
            } catch (Throwable ignored) {}
        }
        return getEventName() + "(" + String.join(", ", properties) + ")";
    }

}
