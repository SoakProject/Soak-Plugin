package org.soak.plugin.loader.common;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.jetbrains.annotations.NotNull;
import org.soak.plugin.SoakPlugin;

import java.util.ArrayList;
import java.util.List;

public class SoakPluginVersion implements ArtifactVersion {

    private String pluginVersion;
    private int major;
    private int minor;
    private int patch;
    private int build;

    public SoakPluginVersion() {

    }

    public SoakPluginVersion(String version) {
        this.parseVersion(version);
    }

    @Override
    public int getMajorVersion() {
        return this.major;
    }

    @Override
    public int getMinorVersion() {
        return this.minor;
    }

    @Override
    public int getIncrementalVersion() {
        return this.patch;
    }

    @Override
    public int getBuildNumber() {
        return this.build;
    }

    @Override
    public String getQualifier() {
        return this.pluginVersion;
    }

    @Override
    public void parseVersion(String version) {
        List<Integer> numbers = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (int at = 0; at < version.length(); at++) {
            char charAt = version.charAt(at);
            if (Character.isDigit(charAt)) {
                current.append(charAt);
                continue;
            }
            if ((!Character.isLetterOrDigit(charAt))) {
                String built = current.toString();
                if (!built.isEmpty()) {
                    try {
                        numbers.add(Integer.parseInt(built));
                    } catch (NumberFormatException e) {
                        SoakPlugin.plugin().logger().error("Could not read part of version: '" + built + "'. Using 0 instead");
                        numbers.add(0);
                    }
                }
                current = new StringBuilder();
            }
        }
        String built = current.toString();
        if (!built.isEmpty()) {
            try {
                numbers.add(Integer.parseInt(built));
            } catch (NumberFormatException e) {
                SoakPlugin.plugin().logger().error("Could not read part of version: '" + built + "'. Using 0 instead");
                numbers.add(0);
            }
        }
        this.pluginVersion = version;
        if (numbers.size() >= 4) {
            this.major = numbers.get(0);
            this.minor = numbers.get(1);
            this.patch = numbers.get(2);
            this.build = numbers.get(3);
            return;
        }
        this.major = 0;
        int iter = 0;
        if (numbers.size() == 3) {
            this.minor = numbers.get(iter);
            iter++;
        }
        if (numbers.size() >= 2) {
            this.patch = numbers.get(iter);
            iter++;
        }
        if (!numbers.isEmpty()) {
            this.build = numbers.get(iter);
        }
    }

    @Override
    public int compareTo(@NotNull ArtifactVersion artifactVersion) {
        Integer majorCompare = numberCompare(this.major, artifactVersion.getMajorVersion());
        if (majorCompare != null) {
            return majorCompare;
        }

        Integer minorCompare = numberCompare(this.minor, artifactVersion.getMinorVersion());
        if (minorCompare != null) {
            return minorCompare;
        }

        Integer patchCompare = numberCompare(this.patch, artifactVersion.getIncrementalVersion());
        if (patchCompare != null) {
            return patchCompare;
        }
        return this.build - artifactVersion.getBuildNumber();
    }

    private Integer numberCompare(int self, int compare) {
        int result = self - compare;
        if (result == 0) {
            return null;
        }
        return result;
    }

    @Override
    public String toString() {
        return this.pluginVersion;
    }
}
