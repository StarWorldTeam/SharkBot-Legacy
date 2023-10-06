package team.starworld.shark.annotation.command;

import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.util.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Action {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface BeforeRegister {}

    String namespace () default ResourceLocation.DEFAULT_NAMESPACE;

    String name();
    String description() default Constants.UNDEFINED;

}
