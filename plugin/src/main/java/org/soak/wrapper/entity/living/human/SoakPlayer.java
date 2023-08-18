package org.soak.wrapper.entity.living.human;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.soak.map.SoakMessageMap;
import org.soak.map.SoakSoundMap;
import org.soak.plugin.SoakPlugin;
import org.soak.plugin.exception.NotImplementedException;
import org.soak.wrapper.inventory.SoakInventory;
import org.soak.wrapper.inventory.SoakInventoryView;
import org.soak.wrapper.inventory.SoakOpeningInventoryView;
import org.soak.wrapper.inventory.SoakPlayerInventory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.math.vector.Vector3d;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SoakPlayer extends AbstractHumanBase<ServerPlayer> implements Player {

    public SoakPlayer(ServerPlayer entity) {
        super(entity, entity, entity);
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
    public void setResourcePack(@NotNull String arg0, @NotNull String arg1) {
        throw NotImplementedException.createByLazy(Player.class, "setResourcePack", String.class, String.class);
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
    public void updateCommands() {
        throw NotImplementedException.createByLazy(Player.class, "updateCommands");
    }

    @Override
    public void openBook(@NotNull ItemStack arg0) {
        throw NotImplementedException.createByLazy(Player.class, "openBook", ItemStack.class);
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
    public @NotNull Set<Player> getTrackedPlayers() {
        throw NotImplementedException.createByLazy(Player.class, "getTrackedPlayers");
    }

    @Override
    public String getClientBrandName() {
        throw NotImplementedException.createByLazy(Player.class, "getClientBrandName");
    }

    @Deprecated
    @Override
    public void kickPlayer(String arg0) {
        throw NotImplementedException.createByLazy(Player.class, "kickPlayer", String.class);
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
        throw NotImplementedException.createByLazy(Player.class, "getBedSpawnLocation");
    }

    @Override
    public void setBedSpawnLocation(Location arg0) {
        throw NotImplementedException.createByLazy(Player.class, "setBedSpawnLocation", Location.class);
    }

    @Override
    public void setBedSpawnLocation(Location arg0, boolean arg1) {
        throw NotImplementedException.createByLazy(Player.class, "setBedSpawnLocation", Location.class, boolean.class);
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

    @Deprecated
    @Override
    public void playEffect(@NotNull Location arg0, @NotNull Effect arg1, int arg2) {
        throw NotImplementedException.createByLazy(Player.class, "playEffect", Location.class, Effect.class, int.class);
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

    @Deprecated
    @Override
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
    public void updateInventory() {
        throw NotImplementedException.createByLazy(Player.class, "updateInventory");
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
}
