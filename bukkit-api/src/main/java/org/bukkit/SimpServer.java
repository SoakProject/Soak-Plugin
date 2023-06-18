package org.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface SimpServer extends Server {

    default @Nullable Player getPlayer(Predicate<Player> predicate) {
        return this.getOnlinePlayers().stream().filter(predicate).findFirst().orElse(null);
    }

    @Override
    default @Nullable Player getPlayer(@NotNull String name) {
        return getPlayer(player -> player.getName().equals(name));
    }

    @Override
    default @Nullable Player getPlayer(@NotNull UUID id) {
        return getPlayer(player -> player.getUniqueId().equals(id));
    }


    default @Nullable World getWorld(Predicate<World> world) {
        return this.getWorlds().stream().filter(world).findFirst().orElse(null);
    }

    @Override
    default @Nullable World getWorld(@NotNull String name) {
        return this.getWorld(world -> world.getName().equals(name));
    }

    @Override
    default @Nullable World getWorld(@NotNull UUID uid) {
        return this.getWorld(world -> world.getUID().equals(uid));
    }

    @Override
    default @Nullable World getWorld(@NotNull NamespacedKey worldKey) {
        return this.getWorld(world -> world.getKey().equals(worldKey));
    }

    @Override
    default boolean unloadWorld(@NotNull String name, boolean save) {
        World world = this.getWorld(t -> t.getName().equals(name));
        if (world == null) {
            return false;
        }
        return this.unloadWorld(world, save);
    }

    @Override
    default @NotNull List<Recipe> getRecipesFor(@NotNull ItemStack result) {
        Recipe recipe;
        Iterator<Recipe> iterator = this.recipeIterator();
        List<Recipe> recipes = new LinkedList<>();
        while (iterator.hasNext()) {
            recipe = iterator.next();
            if (recipe.getResult().equals(result)) {
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    @Override
    default @Nullable Recipe getRecipe(@NotNull NamespacedKey recipeKey) {
        Iterator<Recipe> iterator = this.recipeIterator();
        List<Recipe> recipes = new LinkedList<>();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();

            if (!(recipe instanceof Keyed keyed)) {
                continue;
            }
            if (keyed.getKey().equals(recipeKey)) {
                return recipe;
            }
        }
        return null;
    }

}
