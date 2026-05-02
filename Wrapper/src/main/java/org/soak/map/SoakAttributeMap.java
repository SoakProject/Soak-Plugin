package org.soak.map;

import org.spongepowered.api.entity.attribute.AttributeModifier;
import org.spongepowered.api.entity.attribute.AttributeOperation;
import org.spongepowered.api.entity.attribute.AttributeOperations;
import org.spongepowered.api.registry.DefaultedRegistryReference;

public class SoakAttributeMap {

    public static DefaultedRegistryReference<AttributeOperation> toSponge(org.bukkit.attribute.AttributeModifier.Operation operation) {
        return switch (operation) {
            case ADD_NUMBER -> AttributeOperations.ADDITION;
            case ADD_SCALAR -> AttributeOperations.MULTIPLY_TOTAL; //TODO CHECK THIS AND BELOW
            case MULTIPLY_SCALAR_1 -> AttributeOperations.MULTIPLY_BASE;
        };
    }

    public static AttributeModifier toSponge(org.bukkit.attribute.AttributeModifier modifier) {
        return AttributeModifier
                .builder()
                .amount(modifier.getAmount())
                .key(SoakResourceKeyMap.mapToSponge(modifier.getKey()))
                .operation(toSponge(modifier.getOperation()))
                .build();
    }
}
