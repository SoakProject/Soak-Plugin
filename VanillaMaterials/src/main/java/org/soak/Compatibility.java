package org.soak;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

public class Compatibility {

    public String getName() {
        return "Vanilla";
    }

    public ArtifactVersion getVersion() {
        return new DefaultArtifactVersion("0.0.1");
    }

    public ArtifactVersion getTargetMinecraftVersion() {
        return new DefaultArtifactVersion("1.19.4");
    }
}
