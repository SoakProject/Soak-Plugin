package net.minecraft.server.wrapper;

import com.mojang.authlib.GameProfile;

//attempts to silence some NMS issues
public interface TileEntitySkull {

    void setGameProfile(GameProfile profile);
}
