package org.soak.utils;

public class StringHelper {

    public static String toId(String name) {
        return name.toLowerCase().replaceAll(" ", "_");
    }
}
