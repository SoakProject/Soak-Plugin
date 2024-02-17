package org.soak.wrapper.entity.living.human;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.math.Position;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.util.TriState;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.map.SoakLocationMap;
import org.soak.map.SoakMessageMap;
import org.soak.map.SoakSoundMap;
import org.soak.plugin.SoakPlugin;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.utils.single.SoakSingleInstance;
import org.soak.wrapper.inventory.SoakInventory;
import org.soak.wrapper.inventory.SoakInventoryView;
import org.soak.wrapper.inventory.SoakOpeningInventoryView;
import org.soak.wrapper.inventory.SoakPlayerInventory;
import org.soak.wrapper.world.SoakWorld;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.util.RespawnLocation;
import org.spongepowered.math.vector.Vector3d;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SoakPlayer extends AbstractHumanBase<ServerPlayer> implements Player, SoakSingleInstance<ServerPlayer> {

    private final UUID id;

    public SoakPlayer(ServerPlayer entity) {
        super(entity, entity, entity);
        id = entity.uniqueId();
    }

    @Override
    public @NotNull String getName() {
        return PlainTextComponentSerializer.plainText().serialize(this.displayName());
    }

    @Override
    public @NotNull PlayerInventory getInventory() {
        return new SoakPlayerInventory(this.spongeEntity().inventory());
    }

    @Override
    public @Nullable InventoryView openInventory(@NotNull Inventory inventory) {
        SoakInventory<?> soakInv = (SoakInventory<?>) inventory;
        try {
            return openInventory(soakInv.sponge(), soakInv.requestedTitle().orElse(null)).map(SoakInventoryView::new)
                    .orElse(null);
        } catch (UnsupportedOperationException ex) {
            Sponge.server().scheduler().executor(SoakPlugin.plugin().container()).execute(() -> {
                openInventory(soakInv.sponge(), soakInv.requestedTitle().orElse(null));
            });
            return new SoakOpeningInventoryView(inventory, this, "");
        }
    }

    @Override
    public void openInventory(@NotNull InventoryView inventory) {
        var soakInv = (SoakInventoryView) inventory;
        Sponge.server()
                .scheduler()
                .executor(SoakPlugin.plugin().container())
                .execute(() -> this.spongeEntity().openInventory(soakInv.sponge()));

    }

    @Override
    public void closeInventory() {
        Sponge.server().scheduler().executor(SoakPlugin.plugin().container()).execute(() -> {
            this.spongeEntity().closeInventory();
        });
    }

    @Override
    public void closeInventory(InventoryCloseEvent.@NotNull Reason reason) {
        //TODO this may trigger a event .... need to look into this
        closeInventory();
    }

    private Optional<Container> openInventory(org.spongepowered.api.item.inventory.Inventory inv, @Nullable Component title) {
        if (title == null) {
            return this.spongeEntity().openInventory(inv);
        }
        return this.spongeEntity().openInventory(inv, title);
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    @Override
    public boolean isBanned() {
        BanService banService = Sponge.serviceProvider()
                .provide(BanService.class)
                .orElseThrow(() -> new RuntimeException("Could not find Ban service"));
        CompletableFuture<Boolean> isBannedFuture = banService.find(this.spongeEntity().profile())
                .thenApply(Optional::isPresent);
        try {
            return isBannedFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
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
        return this.spongeEntity()
                .get(Keys.FIRST_DATE_JOINED)
                .orElseThrow(() -> new RuntimeException("No first played time on player.... how?"))
                .getNano();
    }

    @Deprecated
    @Override
    public long getLastPlayed() {
        return getLastSeen();
    }

    @Override
    public boolean hasPlayedBefore() {
        return true;
    }

    @Override
    public long getLastLogin() {
        return this.spongeEntity()
                .get(Keys.LAST_DATE_JOINED)
                .orElseThrow(() -> new RuntimeException("No last played time on player.... how?"))
                .getNano();
    }

    @Override
    public long getLastSeen() {
        return this.spongeEntity()
                .get(Keys.LAST_DATE_PLAYED)
                .orElseThrow(() -> new RuntimeException("No last played time on player.... how?"))
                .getNano();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic arg0, @NotNull Material arg1, int arg2) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "incrementStatistic",
                Statistic.class,
                Material.class,
                int.class);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic arg0, @NotNull Material arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "incrementStatistic",
                Statistic.class,
                Material.class);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic arg0, @NotNull EntityType arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "incrementStatistic",
                Statistic.class,
                EntityType.class);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic arg0, @NotNull EntityType arg1, int arg2) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "incrementStatistic",
                Statistic.class,
                EntityType.class,
                int.class);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic arg0) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "incrementStatistic", Statistic.class);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic arg0, int arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "incrementStatistic",
                Statistic.class,
                int.class);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic arg0) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "decrementStatistic", Statistic.class);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic arg0, @NotNull Material arg1, int arg2) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "decrementStatistic",
                Statistic.class,
                Material.class,
                int.class);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic arg0, @NotNull EntityType arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "decrementStatistic",
                Statistic.class,
                EntityType.class);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic arg0, @NotNull EntityType arg1, int arg2) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "decrementStatistic",
                Statistic.class,
                EntityType.class,
                int.class);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic arg0, int arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "decrementStatistic",
                Statistic.class,
                int.class);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic arg0, @NotNull Material arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "decrementStatistic",
                Statistic.class,
                Material.class);
    }

    @Override
    public void setStatistic(@NotNull Statistic arg0, @NotNull EntityType arg1, int arg2) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "setStatistic",
                Statistic.class,
                EntityType.class,
                int.class);
    }

    @Override
    public void setStatistic(@NotNull Statistic arg0, int arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "setStatistic", Statistic.class, int.class);
    }

    @Override
    public void setStatistic(@NotNull Statistic arg0, @NotNull Material arg1, int arg2) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "setStatistic",
                Statistic.class,
                Material.class,
                int.class);
    }

    @Override
    public int getStatistic(@NotNull Statistic arg0, @NotNull Material arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "getStatistic",
                Statistic.class,
                Material.class);
    }

    @Override
    public int getStatistic(@NotNull Statistic arg0) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class, "getStatistic", Statistic.class);
    }

    @Override
    public int getStatistic(@NotNull Statistic arg0, @NotNull EntityType arg1) {
        throw NotImplementedException.createByLazy(OfflinePlayer.class,
                "getStatistic",
                Statistic.class,
                EntityType.class);
    }

    @Deprecated
    @Override
    public @NotNull String getDisplayName() {
        return SoakMessageMap.mapToBukkit(this.displayName());
    }

    @Deprecated
    @Override
    public void setDisplayName(String arg0) {
        this.displayName(SoakMessageMap.toComponent(arg0));
    }

    @Deprecated
    @Override
    public @NotNull String getLocale() {
        return this.locale().getDisplayName();
    }

    @Override
    public void displayName(Component arg0) {
        this.spongeEntity().offer(Keys.DISPLAY_NAME, arg0);
    }

    @Override
    public @NotNull Component displayName() {
        return this.spongeEntity().get(Keys.DISPLAY_NAME).orElse(Component.empty());
    }

    @Override
    public void sendExperienceChange(float arg0) {
        throw NotImplementedException.createByLazy(Player.class, "sendExperienceChange", float.class);
    }

    @Override
    public void sendExperienceChange(float arg0, int arg1) {
        throw NotImplementedException.createByLazy(Player.class, "sendExperienceChange", float.class, int.class);
    }

    @Override
    public int getLevel() {
        throw NotImplementedException.createByLazy(Player.class, "getLevel");
    }

    @Override
    public void setLevel(int arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setLevel", int.class);
    }

    @Override
    public boolean isFlying() {
        throw NotImplementedException.createByLazy(Player.class, "isFlying");
    }

    @Override
    public void setFlying(boolean arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setFlying", boolean.class);
    }

    @Override
    public float getFlySpeed() {
        throw NotImplementedException.createByLazy(Player.class, "getFlySpeed");
    }

    @Override
    public void setFlySpeed(float arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setFlySpeed", float.class);
    }

    @Override
    public float getWalkSpeed() {
        throw NotImplementedException.createByLazy(Player.class, "getWalkSpeed");
    }

    @Override
    public void setWalkSpeed(float arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setWalkSpeed", float.class);
    }

    @Deprecated
    @Override
    public void setTexturePack(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setTexturePack", String.class);
    }

    @Override
    public void setResourcePack(@NotNull String arg0, byte[] arg1) {
        throw NotImplementedException.createByLazy(Player.class, "setResourcePack", String.class, byte[].class);
    }

    @Override
    public void setResourcePack(@NotNull String s, @Nullable byte[] bytes, @Nullable String s1) {
        throw NotImplementedException.createByLazy(Player.class, "setResourcePack", String.class, byte.class, String.class);
    }

    @Override
    public void setResourcePack(@NotNull String s, @Nullable byte[] bytes, boolean b) {
        setResourcePack(s, bytes, (Component) null, b);
    }

    @Override
    public void setResourcePack(@NotNull String s, @Nullable byte[] bytes, @Nullable String s1, boolean b) {
        setResourcePack(s, bytes, s1 == null ? null : LegacyComponentSerializer.legacySection().deserialize(s1), b);
    }

    @Override
    public void setResourcePack(@NotNull String s, byte @Nullable [] bytes, @Nullable Component component, boolean b) {
        throw NotImplementedException.createByLazy(Player.class, "setResourcePack", String.class, byte.class, Component.class, boolean.class);
    }

    @Override
    public void setResourcePack(@NotNull String arg0, @NotNull String arg1) {
        throw NotImplementedException.createByLazy(Player.class, "setResourcePack", String.class, String.class);
    }

    @Override
    public void setResourcePack(@NotNull String s, @NotNull String s1, boolean b) {
        setResourcePack(s, s1, b, null);
    }

    @Override
    public void setResourcePack(@NotNull String s, @NotNull String s1, boolean b, @Nullable Component component) {
        throw NotImplementedException.createByLazy(Player.class, "setResourcePack", String.class, String.class, boolean.class, Component.class);
    }

    @Deprecated
    @Override
    public void setResourcePack(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setResourcePack", String.class);
    }

    @Override
    public @NotNull Scoreboard getScoreboard() {
        throw NotImplementedException.createByLazy(Player.class, "getScoreboard");
    }

    @Override
    public void setScoreboard(@NotNull Scoreboard arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setScoreboard", Scoreboard.class);
    }

    @Override
    public @Nullable WorldBorder getWorldBorder() {
        throw NotImplementedException.createByLazy(Player.class, "getWorldBorder");
    }

    @Override
    public void setWorldBorder(@Nullable WorldBorder worldBorder) {
        throw NotImplementedException.createByLazy(Player.class, "setWorldBorder", WorldBorder.class);

    }

    @Override
    public boolean isHealthScaled() {
        throw NotImplementedException.createByLazy(Player.class, "isHealthScaled");
    }

    @Override
    public void setHealthScaled(boolean arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setHealthScaled", boolean.class);
    }

    @Override
    public double getHealthScale() {
        throw NotImplementedException.createByLazy(Player.class, "getHealthScale");
    }

    @Override
    public void setHealthScale(double arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setHealthScale", double.class);
    }

    @Override
    public void sendHealthUpdate(double v, int i, float v1) {
        throw NotImplementedException.createByLazy(Player.class, "sendHealthUpdate", double.class, int.class, float.class);
    }

    @Override
    public void sendHealthUpdate() {
        throw NotImplementedException.createByLazy(Player.class, "sendHealthUpdate");
    }

    @Override
    public Entity getSpectatorTarget() {
        throw NotImplementedException.createByLazy(Player.class, "getSpectatorTarget");
    }

    @Override
    public void setSpectatorTarget(Entity arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setSpectatorTarget", Entity.class);
    }

    @Override
    public void resetTitle() {
        throw NotImplementedException.createByLazy(Player.class, "resetTitle");
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, @NotNull Location arg1, int arg2, double arg3, double arg4, double arg5) {
        throw NotImplementedException.createByLazy(Player.class,
                "spawnParticle",
                Particle.class,
                Location.class,
                int.class,
                double.class,
                double.class,
                double.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7) {
        throw NotImplementedException.createByLazy(Player.class,
                "spawnParticle",
                Particle.class,
                double.class,
                double.class,
                double.class,
                int.class,
                double.class,
                double.class,
                double.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, @NotNull Location arg1, int arg2, double arg3, double arg4, double arg5, Object arg6) {
        throw NotImplementedException.createByLazy(Player.class,
                "spawnParticle",
                Particle.class,
                Location.class,
                int.class,
                double.class,
                double.class,
                double.class,
                Object.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, Object arg8) {
        throw NotImplementedException.createByLazy(Player.class,
                "spawnParticle",
                Particle.class,
                double.class,
                double.class,
                double.class,
                int.class,
                double.class,
                double.class,
                double.class,
                Object.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, @NotNull Location arg1, int arg2, double arg3, double arg4, double arg5, double arg6) {
        throw NotImplementedException.createByLazy(Player.class,
                "spawnParticle",
                Particle.class,
                Location.class,
                int.class,
                double.class,
                double.class,
                double.class,
                double.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, double arg8) {
        throw NotImplementedException.createByLazy(Player.class,
                "spawnParticle",
                Particle.class,
                double.class,
                double.class,
                double.class,
                int.class,
                double.class,
                double.class,
                double.class,
                double.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, @NotNull Location arg1, int arg2, double arg3, double arg4, double arg5, double arg6, Object arg7) {
        throw NotImplementedException.createByLazy(Player.class,
                "spawnParticle",
                Particle.class,
                Location.class,
                int.class,
                double.class,
                double.class,
                double.class,
                double.class,
                Object.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, double arg8, Object arg9) {
        throw NotImplementedException.createByLazy(Player.class,
                "spawnParticle",
                Particle.class,
                double.class,
                double.class,
                double.class,
                int.class,
                double.class,
                double.class,
                double.class,
                double.class,
                Object.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, @NotNull Location arg1, int arg2) {
        throw NotImplementedException.createByLazy(Player.class,
                "spawnParticle",
                Particle.class,
                Location.class,
                int.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, double arg1, double arg2, double arg3, int arg4, Object arg5) {
        throw NotImplementedException.createByLazy(Player.class,
                "spawnParticle",
                Particle.class,
                double.class,
                double.class,
                double.class,
                int.class,
                Object.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, @NotNull Location arg1, int arg2, Object arg3) {
        throw NotImplementedException.createByLazy(Player.class,
                "spawnParticle",
                Particle.class,
                Location.class,
                int.class,
                Object.class);
    }

    @Override
    public void spawnParticle(@NotNull Particle arg0, double arg1, double arg2, double arg3, int arg4) {
        throw NotImplementedException.createByLazy(Player.class,
                "spawnParticle",
                Particle.class,
                double.class,
                double.class,
                double.class,
                int.class);
    }

    @Override
    public @NotNull AdvancementProgress getAdvancementProgress(@NotNull Advancement arg0) {
        throw NotImplementedException.createByLazy(Player.class, "getAdvancementProgress", Advancement.class);
    }

    @Override
    public int getClientViewDistance() {
        throw NotImplementedException.createByLazy(Player.class, "getClientViewDistance");
    }

    @Override
    public int getPing() {
        throw NotImplementedException.createByLazy(Player.class, "getPing");
    }

    @Override
    public boolean getAffectsSpawning() {
        throw NotImplementedException.createByLazy(Player.class, "getAffectsSpawning");
    }

    @Override
    public void setAffectsSpawning(boolean arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setAffectsSpawning", boolean.class);
    }

    @Deprecated
    @Override
    public int getViewDistance() {
        throw NotImplementedException.createByLazy(Player.class, "getViewDistance");
    }

    @Deprecated
    @Override
    public void setViewDistance(int arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setViewDistance", int.class);
    }

    @Override
    public int getSimulationDistance() {
        throw NotImplementedException.createByLazy(Player.class, "getSimulationDistance");
    }

    @Override
    public void setSimulationDistance(int i) {
        throw NotImplementedException.createByLazy(Player.class, "setSimulationDistance", int.class);
    }

    @Override
    public int getNoTickViewDistance() {
        throw NotImplementedException.createByLazy(Player.class, "getNoTickViewDistance");
    }

    @Override
    public void setNoTickViewDistance(int i) {
        throw NotImplementedException.createByLazy(Player.class, "setNoTickViewDistance", int.class);
    }

    @Override
    public int getSendViewDistance() {
        throw NotImplementedException.createByLazy(Player.class, "getSendViewDistance");
    }

    @Override
    public void setSendViewDistance(int i) {
        throw NotImplementedException.createByLazy(Player.class, "setSendViewDistance", int.class);
    }

    @Override
    public void updateCommands() {
        throw NotImplementedException.createByLazy(Player.class, "updateCommands");
    }

    @Override
    public void openBook(@NotNull ItemStack arg0) {
        throw NotImplementedException.createByLazy(Player.class, "openBook", ItemStack.class);
    }

    @Override
    public void openSign(@NotNull Sign sign, @NotNull Side side) {
        throw NotImplementedException.createByLazy(Player.class, "openSign", Sign.class, Side.class);
    }

    @Override
    public void showDemoScreen() {
        throw NotImplementedException.createByLazy(Player.class, "showDemoScreen");
    }

    @Override
    public boolean isAllowingServerListings() {
        throw NotImplementedException.createByLazy(Player.class, "isAllowingServerListings");
    }

    @Override
    public PlayerResourcePackStatusEvent.Status getResourcePackStatus() {
        throw NotImplementedException.createByLazy(Player.class, "getResourcePackStatus");
    }

    @Deprecated
    @Override
    public String getResourcePackHash() {
        throw NotImplementedException.createByLazy(Player.class, "getResourcePackHash");
    }

    @Override
    public boolean hasResourcePack() {
        throw NotImplementedException.createByLazy(Player.class, "hasResourcePack");
    }

    @Override
    public @NotNull PlayerProfile getPlayerProfile() {
        throw NotImplementedException.createByLazy(Player.class, "getPlayerProfile");
    }

    @Override
    public void setPlayerProfile(@NotNull PlayerProfile arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setPlayerProfile", PlayerProfile.class);
    }

    @Override
    public float getCooldownPeriod() {
        throw NotImplementedException.createByLazy(Player.class, "getCooldownPeriod");
    }

    @Override
    public float getCooledAttackStrength(float arg0) {
        throw NotImplementedException.createByLazy(Player.class, "getCooledAttackStrength", float.class);
    }

    @Override
    public void resetCooldown() {
        throw NotImplementedException.createByLazy(Player.class, "resetCooldown");
    }

    @Override
    public <T> @NotNull T getClientOption(@NotNull ClientOption<T> option) {
        throw NotImplementedException.createByLazy(Player.class, "getClientOption", ClientOption.class);
    }

    @Override
    public Firework boostElytra(@NotNull ItemStack arg0) {
        throw NotImplementedException.createByLazy(Player.class, "boostElytra", ItemStack.class);
    }

    @Override
    public void sendOpLevel(byte arg0) {
        throw NotImplementedException.createByLazy(Player.class, "sendOpLevel", byte.class);
    }

    @Override
    public void addAdditionalChatCompletions(@NotNull Collection<String> collection) {
        throw NotImplementedException.createByLazy(Player.class, "addAdditionalChatCompletions", Collection.class);

    }

    @Override
    public void removeAdditionalChatCompletions(@NotNull Collection<String> collection) {
        throw NotImplementedException.createByLazy(Player.class, "removeAdditionalChatCompletions", Collection.class);
    }

    @Override
    public @NotNull Set<Player> getTrackedPlayers() {
        throw NotImplementedException.createByLazy(Player.class, "getTrackedPlayers");
    }

    @Override
    public String getClientBrandName() {
        throw NotImplementedException.createByLazy(Player.class, "getClientBrandName");
    }

    @Override
    public void lookAt(double v, double v1, double v2, @NotNull LookAnchor lookAnchor) {
        throw NotImplementedException.createByLazy(Player.class, "lookAt", double.class, double.class, double.class, LookAnchor.class);
    }

    @Override
    public void lookAt(@NotNull Entity entity, @NotNull LookAnchor lookAnchor, @NotNull LookAnchor lookAnchor1) {
        throw NotImplementedException.createByLazy(Player.class, "lookAt", Entity.class, LookAnchor.class, LookAnchor.class);
    }

    @Override
    public void showElderGuardian(boolean b) {
        throw NotImplementedException.createByLazy(Player.class, "showElderGuardian", boolean.class);
    }

    @Override
    public int getWardenWarningCooldown() {
        throw NotImplementedException.createByLazy(Player.class, "getWardenWarningCooldown");
    }

    @Override
    public void setWardenWarningCooldown(int i) {
        throw NotImplementedException.createByLazy(Player.class, "setWardenWarningCooldown", int.class);

    }

    @Override
    public int getWardenTimeSinceLastWarning() {
        throw NotImplementedException.createByLazy(Player.class, "getWardenTimeSinceLastWarning");
    }

    @Override
    public void setWardenTimeSinceLastWarning(int i) {
        throw NotImplementedException.createByLazy(Player.class, "setWardenTimeSinceLastWarning", int.class);

    }

    @Override
    public int getWardenWarningLevel() {
        throw NotImplementedException.createByLazy(Player.class, "getWardenWarningLevel");
    }

    @Override
    public void setWardenWarningLevel(int i) {
        throw NotImplementedException.createByLazy(Player.class, "setWardenWarningLevel", int.class);

    }

    @Override
    public void increaseWardenWarningLevel() {
        throw NotImplementedException.createByLazy(Player.class, "increaseWardenWarningLevel");
    }

    @Deprecated
    @Override
    public void kickPlayer(String arg0) {
        this.kick(LegacyComponentSerializer.legacySection().deserialize(arg0));
    }

    @Override
    public void kick() {
        this.entity.kick();
    }

    @Override
    public @NotNull Player.Spigot spigot() {
        return new SoakSpigotPlayer(this);
    }

    @Override
    public void giveExp(int arg0, boolean arg1) {
        throw NotImplementedException.createByLazy(Player.class, "giveExp", int.class, boolean.class);
    }

    @Override
    public Component playerListName() {
        throw NotImplementedException.createByLazy(Player.class, "playerListName");
    }

    @Override
    public void playerListName(Component arg0) {
        throw NotImplementedException.createByLazy(Player.class, "playerListName", Component.class);
    }

    @Override
    public Component playerListHeader() {
        throw NotImplementedException.createByLazy(Player.class, "playerListHeader");
    }

    @Override
    public Component playerListFooter() {
        throw NotImplementedException.createByLazy(Player.class, "playerListFooter");
    }

    @Deprecated
    @Override
    public @NotNull String getPlayerListName() {
        throw NotImplementedException.createByLazy(Player.class, "getPlayerListName");
    }

    @Deprecated
    @Override
    public void setPlayerListName(String arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setPlayerListName", String.class);
    }

    @Deprecated
    @Override
    public String getPlayerListHeader() {
        throw NotImplementedException.createByLazy(Player.class, "getPlayerListHeader");
    }

    @Deprecated
    @Override
    public void setPlayerListHeader(String arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setPlayerListHeader", String.class);
    }

    @Deprecated
    @Override
    public String getPlayerListFooter() {
        throw NotImplementedException.createByLazy(Player.class, "getPlayerListFooter");
    }

    @Deprecated
    @Override
    public void setPlayerListFooter(String arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setPlayerListFooter", String.class);
    }

    @Deprecated
    @Override
    public void setPlayerListHeaderFooter(BaseComponent[] arg0, BaseComponent[] arg1) {
        throw NotImplementedException.createByLazy(Player.class,
                "setPlayerListHeaderFooter",
                BaseComponent[].class,
                BaseComponent[].class);
    }

    @Deprecated
    @Override
    public void setPlayerListHeaderFooter(String arg0, String arg1) {
        throw NotImplementedException.createByLazy(Player.class,
                "setPlayerListHeaderFooter",
                String.class,
                String.class);
    }

    @Deprecated
    @Override
    public void setPlayerListHeaderFooter(BaseComponent arg0, BaseComponent arg1) {
        throw NotImplementedException.createByLazy(Player.class,
                "setPlayerListHeaderFooter",
                BaseComponent.class,
                BaseComponent.class);
    }

    @Override
    public @NotNull Location getCompassTarget() {
        throw NotImplementedException.createByLazy(Player.class, "getCompassTarget");
    }

    @Override
    public void setCompassTarget(@NotNull Location arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setCompassTarget", Location.class);
    }

    @Override
    public boolean isConversing() {
        return false;
    }

    @Override
    public void acceptConversationInput(@NotNull String input) {

    }

    @Override
    public boolean beginConversation(@NotNull Conversation conversation) {
        return false;
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation) {

    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent details) {

    }

    @Override
    public void sendRawMessage(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Player.class, "sendRawMessage", String.class);
    }

    @Override
    public void sendRawMessage(@Nullable UUID sender, @NotNull String message) {

    }

    @Override
    public void kick(Component arg0) {
        throw NotImplementedException.createByLazy(Player.class, "kick", Component.class);
    }

    @Override
    public void kick(Component arg0, @NotNull PlayerKickEvent.Cause arg1) {
        throw NotImplementedException.createByLazy(Player.class, "kick", Component.class, Cause.class);
    }

    @Override
    public void chat(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Player.class, "chat", String.class);
    }

    @Override
    public boolean performCommand(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Player.class, "performCommand", String.class);
    }

    @Deprecated
    @Override
    public boolean isOnGround() {
        throw NotImplementedException.createByLazy(Player.class, "isOnGround");
    }

    @Override
    public boolean isSneaking() {
        return this.spongeEntity().get(Keys.IS_SNEAKING).orElse(false);
    }

    @Override
    public void setSneaking(boolean arg0) {
        this.spongeEntity().offer(Keys.IS_SNEAKING, arg0);
    }

    @Override
    public boolean isSprinting() {
        return this.spongeEntity().get(Keys.IS_SPRINTING).orElse(false);
    }

    @Override
    public void setSprinting(boolean arg0) {
        this.spongeEntity().offer(Keys.IS_SPRINTING, arg0);
    }

    @Override
    public void saveData() {
        throw NotImplementedException.createByLazy(Player.class, "saveData");
    }

    @Override
    public void loadData() {
        throw NotImplementedException.createByLazy(Player.class, "loadData");
    }

    @Override
    public boolean isSleepingIgnored() {
        throw NotImplementedException.createByLazy(Player.class, "isSleepingIgnored");
    }

    @Override
    public void setSleepingIgnored(boolean arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setSleepingIgnored", boolean.class);
    }

    @Override
    public Location getBedSpawnLocation() {
        var respawnLocations = this.spongeEntity().get(Keys.RESPAWN_LOCATIONS).orElseGet(HashMap::new);
        var worldKey = ((SoakWorld) this.getWorld()).sponge().key();
        var spongeLocation = respawnLocations.get(worldKey);
        if (spongeLocation == null) {
            return null;
        }
        return spongeLocation.asLocation().map(SoakLocationMap::toBukkit).orElseGet(() -> {
            var position = spongeLocation.position();
            return new Location(getWorld(), position.x(), position.y(), position.z());
        });
    }

    @Override
    public void setBedSpawnLocation(@Nullable Location location) {
        setBedSpawnLocation(location, false);
    }

    @Override
    public void setBedSpawnLocation(@Nullable Location location, boolean force) {
        var world = (SoakWorld) (location == null ? this.getWorld() : location.getWorld());
        var respawnLocations = this.spongeEntity().get(Keys.RESPAWN_LOCATIONS).orElseGet(HashMap::new);
        if (location == null) {
            respawnLocations.remove(world.sponge().key());
            return;
        }
        var spongeLocation = SoakLocationMap.toSponge(location);
        var respawnLocation = RespawnLocation.builder().location(spongeLocation).forceSpawn(force).build();
        respawnLocations.put(world.sponge().key(), respawnLocation);
    }

    @Deprecated
    @Override
    public void playNote(@NotNull Location arg0, byte arg1, byte arg2) {
        throw NotImplementedException.createByLazy(Player.class, "playNote", Location.class, byte.class, byte.class);
    }

    @Override
    public void playNote(@NotNull Location arg0, @NotNull Instrument arg1, @NotNull Note arg2) {
        throw NotImplementedException.createByLazy(Player.class,
                "playNote",
                Location.class,
                Instrument.class,
                Note.class);
    }

    @Override
    public void playSound(@NotNull Location arg0, @NotNull Sound arg1, float arg2, float arg3) {
        throw NotImplementedException.createByLazy(Player.class,
                "playSound",
                Location.class,
                Sound.class,
                float.class,
                float.class);
    }

    @Override
    public void playSound(@NotNull Location arg0, @NotNull String arg1, @NotNull SoundCategory arg2, float arg3, float arg4) {
        var soundType = SoakSoundMap.toSponge(arg1);
        var soundSource = SoakSoundMap.toAdventure(arg2);
        var sound = net.kyori.adventure.sound.Sound.sound(soundType, soundSource, arg3, arg4);
        this.spongeEntity().playSound(sound, new Vector3d(arg0.getX(), arg0.getY(), arg0.getZ()));
    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, float v, float v1) {

    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull String s, float v, float v1) {

    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v, float v1) {

    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull String s, @NotNull SoundCategory soundCategory, float v, float v1) {

    }

    @Override
    public void playSound(@NotNull Location arg0, @NotNull String arg1, float arg2, float arg3) {
        throw NotImplementedException.createByLazy(Player.class,
                "playSound",
                Location.class,
                String.class,
                float.class,
                float.class);
    }

    @Override
    public void playSound(@NotNull Location arg0, @NotNull Sound arg1, @NotNull SoundCategory arg2, float arg3, float arg4) {
        throw NotImplementedException.createByLazy(Player.class,
                "playSound",
                Location.class,
                Sound.class,
                SoundCategory.class,
                float.class,
                float.class);
    }

    @Override
    public void stopSound(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Player.class, "stopSound", String.class);
    }

    @Override
    public void stopSound(@NotNull Sound arg0, SoundCategory arg1) {
        throw NotImplementedException.createByLazy(Player.class, "stopSound", Sound.class, SoundCategory.class);
    }

    @Override
    public void stopSound(@NotNull Sound arg0) {
        throw NotImplementedException.createByLazy(Player.class, "stopSound", Sound.class);
    }

    @Override
    public void stopSound(@NotNull String arg0, SoundCategory arg1) {
        throw NotImplementedException.createByLazy(Player.class, "stopSound", String.class, SoundCategory.class);
    }

    @Override
    public void stopSound(@NotNull SoundCategory soundCategory) {

    }

    @Override
    public void stopAllSounds() {

    }

    @Deprecated
    @Override
    public void playEffect(@NotNull Location arg0, @NotNull Effect arg1, int arg2) {
        throw NotImplementedException.createByLazy(Player.class, "playEffect", Location.class, Effect.class, int.class);
    }

    @Override
    public boolean breakBlock(@NotNull Block block) {
        return false;
    }

    @Override
    public void playEffect(@NotNull Location arg0, @NotNull Effect arg1, Object arg2) {
        throw NotImplementedException.createByLazy(Player.class,
                "playEffect",
                Location.class,
                Effect.class,
                Object.class);
    }

    @Override
    public void sendBlockChange(@NotNull Location arg0, @NotNull BlockData arg1) {
        throw NotImplementedException.createByLazy(Player.class, "sendBlockChange", Location.class, BlockData.class);
    }

    @Override
    public void sendBlockChanges(@NotNull Collection<BlockState> collection, boolean b) {

    }

    @Deprecated
    @Override
    public void sendBlockChange(@NotNull Location arg0, @NotNull Material arg1, byte arg2) {
        throw NotImplementedException.createByLazy(Player.class,
                "sendBlockChange",
                Location.class,
                Material.class,
                byte.class);
    }

    @Override
    public void sendBlockDamage(@NotNull Location arg0, float arg1) {
        throw NotImplementedException.createByLazy(Player.class, "sendBlockDamage", Location.class, float.class);
    }

    @Override
    public void sendMultiBlockChange(@NotNull Map<? extends Position, BlockData> map, boolean b) {

    }

    @Override
    public void sendBlockDamage(@NotNull Location location, float v, @NotNull Entity entity) {

    }

    @Override
    public void sendBlockDamage(@NotNull Location location, float v, int i) {

    }

    @Override
    public void sendEquipmentChange(@NotNull LivingEntity livingEntity, @NotNull EquipmentSlot equipmentSlot, @Nullable ItemStack itemStack) {

    }

    @Override
    public void sendEquipmentChange(@NotNull LivingEntity livingEntity, @NotNull Map<EquipmentSlot, ItemStack> map) {

    }

    @Override
    public void sendSignChange(@NotNull Location location, @Nullable List<? extends Component> list, @NotNull DyeColor dyeColor, boolean b) throws IllegalArgumentException {

    }

    @Deprecated
    public boolean sendChunkChange(@NotNull Location arg0, int arg1, int arg2, int arg3, byte[] arg4) {
        throw NotImplementedException.createByLazy(Player.class,
                "sendChunkChange",
                Location.class,
                int.class,
                int.class,
                int.class,
                byte[].class);
    }

    @Override
    public void sendSignChange(@NotNull Location arg0, List arg1, @NotNull DyeColor arg2) {
        throw NotImplementedException.createByLazy(Player.class,
                "sendSignChange",
                Location.class,
                List.class,
                DyeColor.class);
    }

    @Deprecated
    @Override
    public void sendSignChange(@NotNull Location arg0, String[] arg1, @NotNull DyeColor arg2) {
        throw NotImplementedException.createByLazy(Player.class,
                "sendSignChange",
                Location.class,
                String[].class,
                DyeColor.class);
    }

    @Override
    public void sendSignChange(@NotNull Location location, @Nullable String[] strings, @NotNull DyeColor dyeColor, boolean b) throws IllegalArgumentException {

    }

    @Deprecated
    @Override
    public void sendSignChange(@NotNull Location arg0, String[] arg1) {
        throw NotImplementedException.createByLazy(Player.class, "sendSignChange", Location.class, String[].class);
    }

    @Override
    public void sendSignChange(@NotNull Location arg0, List arg1) {
        throw NotImplementedException.createByLazy(Player.class, "sendSignChange", Location.class, List.class);
    }

    @Override
    public void sendMap(@NotNull MapView arg0) {
        throw NotImplementedException.createByLazy(Player.class, "sendMap", MapView.class);
    }

    @Override
    public void showWinScreen() {

    }

    @Override
    public boolean hasSeenWinScreen() {
        return false;
    }

    @Override
    public void setHasSeenWinScreen(boolean b) {

    }

    @Deprecated
    @Override
    public void sendActionBar(BaseComponent[] arg0) {
        throw NotImplementedException.createByLazy(Player.class, "sendActionBar", BaseComponent[].class);
    }

    @Deprecated
    @Override
    public void sendActionBar(char arg0, @NotNull String arg1) {
        throw NotImplementedException.createByLazy(Player.class, "sendActionBar", char.class, String.class);
    }

    @Deprecated
    @Override
    public void sendActionBar(@NotNull String arg0) {
        throw NotImplementedException.createByLazy(Player.class, "sendActionBar", String.class);
    }

    @Deprecated
    @Override
    public void setTitleTimes(int arg0, int arg1, int arg2) {
        throw NotImplementedException.createByLazy(Player.class, "setTitleTimes", int.class, int.class, int.class);
    }

    @Deprecated
    @Override
    public void setSubtitle(BaseComponent arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setSubtitle", BaseComponent.class);
    }

    @Deprecated
    @Override
    public void setSubtitle(BaseComponent[] arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setSubtitle", BaseComponent[].class);
    }

    @Deprecated
    @Override
    public void showTitle(BaseComponent[] arg0) {
        throw NotImplementedException.createByLazy(Player.class, "showTitle", BaseComponent[].class);
    }

    @Deprecated
    @Override
    public void showTitle(BaseComponent arg0) {
        throw NotImplementedException.createByLazy(Player.class, "showTitle", BaseComponent.class);
    }

    @Deprecated
    @Override
    public void showTitle(BaseComponent[] arg0, BaseComponent[] arg1, int arg2, int arg3, int arg4) {
        throw NotImplementedException.createByLazy(Player.class,
                "showTitle",
                BaseComponent[].class,
                BaseComponent[].class,
                int.class,
                int.class,
                int.class);
    }

    @Deprecated
    @Override
    public void showTitle(BaseComponent arg0, BaseComponent arg1, int arg2, int arg3, int arg4) {
        throw NotImplementedException.createByLazy(Player.class,
                "showTitle",
                BaseComponent.class,
                BaseComponent.class,
                int.class,
                int.class,
                int.class);
    }

    @Override
    public void sendTitle(String arg0, String arg1, int arg2, int arg3, int arg4) {
        throw NotImplementedException.createByLazy(Player.class,
                "sendTitle",
                String.class,
                String.class,
                int.class,
                int.class,
                int.class);
    }

    @Deprecated
    @Override
    public void sendTitle(String arg0, String arg1) {
        throw NotImplementedException.createByLazy(Player.class, "sendTitle", String.class, String.class);
    }

    @Deprecated
    @Override
    public void sendTitle(@NotNull Title arg0) {
        throw NotImplementedException.createByLazy(Player.class, "sendTitle", Title.class);
    }

    @Deprecated
    @Override
    public void updateTitle(@NotNull Title arg0) {
        throw NotImplementedException.createByLazy(Player.class, "updateTitle", Title.class);
    }

    @Deprecated
    @Override
    public void hideTitle() {
        throw NotImplementedException.createByLazy(Player.class, "hideTitle");
    }

    @Override
    public void sendHurtAnimation(float v) {

    }

    @Override
    public void addCustomChatCompletions(@NotNull Collection<String> collection) {

    }

    @Override
    public void removeCustomChatCompletions(@NotNull Collection<String> collection) {

    }

    @Override
    public void setCustomChatCompletions(@NotNull Collection<String> collection) {

    }

    @Override
    public void updateInventory() {
        throw NotImplementedException.createByLazy(Player.class, "updateInventory");
    }

    @Override
    public @Nullable GameMode getPreviousGameMode() {
        return null;
    }

    @Override
    public void setPlayerTime(long arg0, boolean arg1) {
        throw NotImplementedException.createByLazy(Player.class, "setPlayerTime", long.class, boolean.class);
    }

    @Override
    public long getPlayerTime() {
        throw NotImplementedException.createByLazy(Player.class, "getPlayerTime");
    }

    @Override
    public long getPlayerTimeOffset() {
        throw NotImplementedException.createByLazy(Player.class, "getPlayerTimeOffset");
    }

    @Override
    public boolean isPlayerTimeRelative() {
        throw NotImplementedException.createByLazy(Player.class, "isPlayerTimeRelative");
    }

    @Override
    public void resetPlayerTime() {
        throw NotImplementedException.createByLazy(Player.class, "resetPlayerTime");
    }

    @Override
    public WeatherType getPlayerWeather() {
        throw NotImplementedException.createByLazy(Player.class, "getPlayerWeather");
    }

    @Override
    public void setPlayerWeather(@NotNull WeatherType arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setPlayerWeather", WeatherType.class);
    }

    @Override
    public void resetPlayerWeather() {
        throw NotImplementedException.createByLazy(Player.class, "resetPlayerWeather");
    }

    @Override
    public int getExpCooldown() {
        return 0;
    }

    @Override
    public void setExpCooldown(int i) {

    }

    @Override
    public int applyMending(int arg0) {
        throw NotImplementedException.createByLazy(Player.class, "applyMending", int.class);
    }

    @Override
    public void giveExpLevels(int arg0) {
        throw NotImplementedException.createByLazy(Player.class, "giveExpLevels", int.class);
    }

    @Override
    public float getExp() {
        throw NotImplementedException.createByLazy(Player.class, "getExp");
    }

    @Override
    public void setExp(float arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setExp", float.class);
    }

    @Override
    public int getTotalExperience() {
        throw NotImplementedException.createByLazy(Player.class, "getTotalExperience");
    }

    @Override
    public void setTotalExperience(int arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setTotalExperience", int.class);
    }

    @Override
    public boolean getAllowFlight() {
        throw NotImplementedException.createByLazy(Player.class, "getAllowFlight");
    }

    @Override
    public void setAllowFlight(boolean arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setAllowFlight", boolean.class);
    }

    @Override
    public void setFlyingFallDamage(@NotNull TriState triState) {

    }

    @Override
    public @NotNull TriState hasFlyingFallDamage() {
        return null;
    }

    @Deprecated
    @Override
    public void hidePlayer(@NotNull Player arg0) {
        throw NotImplementedException.createByLazy(Player.class, "hidePlayer", Player.class);
    }

    @Override
    public void hidePlayer(@NotNull Plugin arg0, @NotNull Player arg1) {
        throw NotImplementedException.createByLazy(Player.class, "hidePlayer", Plugin.class, Player.class);
    }

    @Deprecated
    @Override
    public void showPlayer(@NotNull Player arg0) {
        throw NotImplementedException.createByLazy(Player.class, "showPlayer", Player.class);
    }

    @Override
    public void showPlayer(@NotNull Plugin arg0, @NotNull Player arg1) {
        throw NotImplementedException.createByLazy(Player.class, "showPlayer", Plugin.class, Player.class);
    }

    @Override
    public boolean canSee(@NotNull Player arg0) {
        throw NotImplementedException.createByLazy(Player.class, "canSee", Player.class);
    }

    @Override
    public void hideEntity(@NotNull Plugin plugin, @NotNull Entity entity) {

    }

    @Override
    public void showEntity(@NotNull Plugin plugin, @NotNull Entity entity) {

    }

    @Override
    public boolean canSee(@NotNull Entity entity) {
        return false;
    }

    @Override
    public @NotNull Locale locale() {
        throw NotImplementedException.createByLazy(Player.class, "locale");
    }

    @Override
    public InetSocketAddress getAddress() {
        throw NotImplementedException.createByLazy(Player.class, "getAddress");
    }

    @Override
    public int getProtocolVersion() {
        throw NotImplementedException.createByLazy(Player.class, "getProtocolVersion");
    }

    @Override
    public @Nullable InetSocketAddress getVirtualHost() {
        throw NotImplementedException.createByLazy(Player.class, "getVirtualHost");
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        throw NotImplementedException.createByLazy(Player.class, "serialize");
    }

    @Override
    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, byte[] message) {
        throw NotImplementedException.createByLazy(Player.class,
                "sendPluginMessage",
                Plugin.class,
                String.class,
                byte[].class);
    }

    @Override
    public @NotNull Set<String> getListeningPluginChannels() {
        throw NotImplementedException.createByLazy(Player.class, "getListeningPluginChannels");
    }

    @Override
    public void setSponge(ServerPlayer sponge) {
        this.entity = sponge;
        this.audience = sponge;
        this.subject = sponge;
    }

    @Override
    public boolean isSame(ServerPlayer sponge) {
        return this.id.equals(sponge.uniqueId());
    }
}
