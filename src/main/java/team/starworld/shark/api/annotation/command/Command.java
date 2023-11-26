package team.starworld.shark.api.annotation.command;

import team.starworld.shark.core.registries.ResourceLocation;
import team.starworld.shark.util.ConstantUtil;

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
    @interface Setup {}

    String namespace () default ResourceLocation.DEFAULT_NAMESPACE;

    String name();
    String description() default ConstantUtil.UNDEFINED;

    String[] subName() default {};

}
