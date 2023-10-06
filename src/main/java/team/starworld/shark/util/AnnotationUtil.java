package team.starworld.shark.util;

import lombok.SneakyThrows;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class AnnotationUtil {

    @SneakyThrows
    public static List <Class <?>> getInstances (Class <? extends Annotation> annotationClass) {
        List <Class <?>> list = new ArrayList <> ();
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotationClass));
        var components = scanner.findCandidateComponents("*");
        for (var component : components) {
            try {
                list.add(Class.forName(component.getBeanClassName()));
            } catch (Throwable ignored) {}
        }
        return list;
    }

}
