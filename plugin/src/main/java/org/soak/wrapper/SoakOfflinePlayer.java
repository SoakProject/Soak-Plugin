package org.soak.wrapper;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.wrapper.entity.living.human.SoakPlayer;
import org.spongepowered.api.entity.living.player.User;

import java.util.Map;
import java.util.UUID;

public class SoakOfflinePlayer implements OfflinePlayer {

    private final User user;

    public SoakOfflinePlayer(User user) {
        this.user = user;
    }

    public User spongeUser() {
        return this.user;
    }

    @Override
    public String getName() {
        return this.user.name();
    }

    @Override
    public boolean isOnline() {
        return this.user.isOnline();
    }

    @Override
    public SoakPlayer getPlayer() {
        return this.user.player().map(SoakPlayer::new).orElse(null);
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return this.user.uniqueId();
    }

    @Override
    public boolean isBanned() {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "isBanned");
    }

    @Override
    public boolean isWhitelisted() {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "isWhitelisted");
    }

    @Override
    public void setWhitelisted(boolean arg0) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "setWhitelisted", boolean.class);
    }

    @Override
    public long getFirstPlayed() {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "getFirstPlayed");
    }

    @Deprecated
    @Override
    public long getLastPlayed() {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "getLastPlayed");
    }

    @Override
    public boolean hasPlayedBefore() {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "hasPlayedBefore");
    }

    @Override
    public Location getBedSpawnLocation() {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "getBedSpawnLocation");
    }

    @Override
    public long getLastLogin() {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "getLastLogin");
    }

    @Override
    public long getLastSeen() {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "getLastSeen");
    }

    @Override
    public void incrementStatistic(Statistic arg0, Material arg1, int arg2) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "incrementStatistic",
                Statistic.class,
                Material.class,
                int.class);
    }

    @Override
    public void incrementStatistic(Statistic arg0, Material arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "incrementStatistic",
                Statistic.class,
                Material.class);
    }

    @Override
    public void incrementStatistic(Statistic arg0, EntityType arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "incrementStatistic",
                Statistic.class,
                EntityType.class);
    }

    @Override
    public void incrementStatistic(Statistic arg0, EntityType arg1, int arg2) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "incrementStatistic",
                Statistic.class,
                EntityType.class,
                int.class);
    }

    @Override
    public void incrementStatistic(Statistic arg0) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "incrementStatistic", Statistic.class);
    }

    @Override
    public void incrementStatistic(Statistic arg0, int arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "incrementStatistic",
                Statistic.class,
                int.class);
    }

    @Override
    public void decrementStatistic(Statistic arg0) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "decrementStatistic", Statistic.class);
    }

    @Override
    public void decrementStatistic(Statistic arg0, Material arg1, int arg2) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "decrementStatistic",
                Statistic.class,
                Material.class,
                int.class);
    }

    @Override
    public void decrementStatistic(Statistic arg0, EntityType arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "decrementStatistic",
                Statistic.class,
                EntityType.class);
    }

    @Override
    public void decrementStatistic(Statistic arg0, EntityType arg1, int arg2) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "decrementStatistic",
                Statistic.class,
                EntityType.class,
                int.class);
    }

    @Override
    public void decrementStatistic(Statistic arg0, int arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "decrementStatistic",
                Statistic.class,
                int.class);
    }

    @Override
    public void decrementStatistic(Statistic arg0, Material arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "decrementStatistic",
                Statistic.class,
                Material.class);
    }

    @Override
    public void setStatistic(Statistic arg0, EntityType arg1, int arg2) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "setStatistic",
                Statistic.class,
                EntityType.class,
                int.class);
    }

    @Override
    public void setStatistic(Statistic arg0, int arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "setStatistic", Statistic.class, int.class);
    }

    @Override
    public void setStatistic(Statistic arg0, Material arg1, int arg2) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "setStatistic",
                Statistic.class,
                Material.class,
                int.class);
    }

    @Override
    public int getStatistic(Statistic arg0, Material arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "getStatistic",
                Statistic.class,
                Material.class);
    }

    @Override
    public int getStatistic(Statistic arg0) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "getStatistic", Statistic.class);
    }

    @Override
    public int getStatistic(Statistic arg0, EntityType arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "getStatistic",
                Statistic.class,
                EntityType.class);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "serialize");
    }

    @Override
    public boolean isOp() {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "isOp");
    }

    @Override
    public void setOp(boolean value) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "setOp", boolean.class);
    }
}
