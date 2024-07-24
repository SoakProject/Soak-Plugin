package org.soak;

import org.apache.maven.artifact.versioning.ArtifactVersion;

public class Compatibility {

    public Compatibility() {
        throw new RuntimeException("Compatibility class was never replaced");
    }

    public String getName() {
        throw new RuntimeException("This class should be replaced");
    }

    public ArtifactVersion getVersion() {
        throw new RuntimeException("This class should be replaced");
    }

    public ArtifactVersion getTargetMinecraftVersion() {
        throw new RuntimeException("This class should be replaced");
    }
}
