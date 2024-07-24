package org.soak;

import org.jetbrains.annotations.Nullable;

import java.util.OptionalDouble;
import java.util.regex.Pattern;

public enum TargetMinecraftVersion {

    V1_21(12),
    V1_20_6(11),
    V1_20_5(),
    V1_20_4(),
    V1_20_3(),
    V1_20_2(),
    V1_20_1(),
    V1_20(),
    V1_19_4(10),
    V1_19_3(),
    V1_19_2();

    private final @Nullable Double apiVersion;

    TargetMinecraftVersion() {
        this(null);
    }

    TargetMinecraftVersion(int version) {
        this((double) version);
    }

    TargetMinecraftVersion(@Nullable Double version) {
        this.apiVersion = version;
    }

    public String minecraftVersion() {
        String name = name().replaceAll("_", ".").substring(1, name().length() - 1);
        if (name.split(Pattern.quote(".")).length == 1) {
            name = name + ".0";
        }
        return name;
    }

    public OptionalDouble apiVersion() {
        if (apiVersion == null) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(this.apiVersion);
    }

}
