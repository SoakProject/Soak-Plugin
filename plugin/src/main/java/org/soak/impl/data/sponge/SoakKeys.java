package org.soak.impl.data.sponge;

import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.event.lifecycle.RegisterDataEvent;
import org.spongepowered.api.util.Ticks;

public class SoakKeys {

    public static final Key<Value<Ticks>> PORTAL_COOLDOWN = PortalCooldownCustomData.generateKey();

    public static void init(RegisterDataEvent event) {
        PortalCooldownCustomData.init(event);
    }
}
