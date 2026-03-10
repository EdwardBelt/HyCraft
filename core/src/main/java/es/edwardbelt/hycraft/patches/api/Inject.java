package es.edwardbelt.hycraft.patches.api;

import es.edwardbelt.hycraft.patches.api.At;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Inject {
    String method();
    At at();
    boolean cancellable() default false;
    Class<?>[] args() default {};
}
