package org.soak.wrapper.entity.living.human;

import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.service.permission.Subject;

public class SoakHumanEntity extends SoakHumanBase<Human> {

    public SoakHumanEntity(Subject subject, Audience audience, Human entity) {
        super(subject, audience, entity);
    }
}
