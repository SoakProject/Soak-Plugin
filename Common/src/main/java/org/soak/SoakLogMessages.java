package org.soak;

import java.util.Collection;
import java.util.concurrent.LinkedTransferQueue;

public class SoakLogMessages {

    public static final Collection<String> CUSTOM_MODEL_DATA = new LinkedTransferQueue<>();

    public static boolean hasSentCustomModelData(String identifier) {
        boolean contains = CUSTOM_MODEL_DATA.contains(identifier);
        if (!contains) {
            CUSTOM_MODEL_DATA.add(identifier);
        }
        return contains;
    }
}
