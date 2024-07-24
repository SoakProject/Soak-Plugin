package org.soak.annotation;

import org.jetbrains.annotations.NotNull;
import org.soak.TargetMinecraftVersion;

public @interface UsesNms {

    @NotNull TargetMinecraftVersion[] value() default {};
}
