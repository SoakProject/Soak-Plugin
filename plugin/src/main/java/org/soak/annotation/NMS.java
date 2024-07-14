package org.soak.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.TYPE, ElementType.METHOD})
public @interface NMS {

    TargetMinecraftVersion[] value();

    String[] knownPlugins() default {};

}
