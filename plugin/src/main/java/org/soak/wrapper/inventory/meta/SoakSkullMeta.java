package org.soak.wrapper.inventory.meta;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.wrapper.SoakOfflinePlayer;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.property.ProfileProperty;

import java.util.Optional;

public class SoakSkullMeta extends AbstractItemMeta implements SkullMeta {

    private @Nullable com.mojang.authlib.GameProfile profile; //nms bounce

    public SoakSkullMeta(ItemStack stack) {
        super(stack);
    }

    public SoakSkullMeta(ItemStackSnapshot stack) {
        super(stack);
    }

    //shouldn't use if the type is known
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public SoakSkullMeta(ValueContainer container) {
        super(container);
    }

    @Deprecated
    @Override
    public String getOwner() {
        throw NotImplementedException.createByLazy(SkullMeta.class, "getOwner");
    }

    @Override
    public boolean hasOwner() {
        throw NotImplementedException.createByLazy(SkullMeta.class, "hasOwner");
    }

    @Override
    public PlayerProfile getPlayerProfile() {
        throw NotImplementedException.createByLazy(SkullMeta.class, "getPlayerProfile");
    }

    @Override
    public void setPlayerProfile(PlayerProfile arg0) {
        throw NotImplementedException.createByLazy(SkullMeta.class, "setPlayerProfile", PlayerProfile.class);
    }

    @Override
    public OfflinePlayer getOwningPlayer() {
        Optional<ProfileProperty> opProfile = this.container.get(Keys.SKIN_PROFILE_PROPERTY);
        return opProfile.map(profileProperty -> Bukkit.getOfflinePlayer(profileProperty.name())).orElse(null);
    }

    @Override
    public boolean setOwningPlayer(OfflinePlayer arg0) {
        if (arg0 == null) {
            this.remove(Keys.SKIN_PROFILE_PROPERTY);
            return true;
        }
        SoakOfflinePlayer soakOfflinePlayer = (SoakOfflinePlayer) arg0;
        var profile = soakOfflinePlayer.spongeUser().profile();
        return setGameProfile(profile);
    }

    @Deprecated
    @Override
    public boolean setOwner(String arg0) {
        throw NotImplementedException.createByLazy(SkullMeta.class, "setOwner", String.class);
    }


    @Override
    public @NotNull SoakSkullMeta clone() {
        return new SoakSkullMeta(this.container);
    }

    //for nms
    public boolean setGameProfile(com.mojang.authlib.GameProfile profile) {
        if (profile instanceof GameProfile) {
            return setGameProfile((GameProfile) profile);
        }
        this.profile = profile;
        //might make some plugins happy
        return true;
    }


    public boolean setGameProfile(GameProfile profile) {
        var properties = profile.properties();
        var opTextureProperty = properties.stream()
                .filter(property -> property.name().equals(ProfileProperty.TEXTURES))
                .findAny();
        if (opTextureProperty.isEmpty()) {
            return false;
        }
        this.set(Keys.SKIN_PROFILE_PROPERTY, opTextureProperty.get());
        if (profile instanceof com.mojang.authlib.GameProfile) {
            this.profile = (com.mojang.authlib.GameProfile) profile;
        }
        return true;
    }
}
