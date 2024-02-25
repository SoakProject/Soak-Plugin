package org.soak.utils;

import org.soak.utils.single.SoakSingleInstance;
import org.soak.wrapper.entity.living.human.SoakPlayer;
import org.soak.wrapper.world.SoakWorld;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Collection;
import java.util.concurrent.LinkedTransferQueue;
import java.util.function.BiPredicate;
import java.util.function.Function;

/*
There are a few things in Bukkit where you can compare the memory address rather than using .equals()
this means the typical new wrapped object won't work. This stores the wrapped version for memory equals
 */
public class SoakMemoryStore {

    private static final BiPredicate<SoakWorld, ServerWorld> COMPARE_WORLD = (soak, sponge) -> soak.sponge()
            .equals(sponge);
    private static final Function<ServerWorld, SoakWorld> CREATE_WORLD = SoakWorld::new;
    private static final BiPredicate<SoakPlayer, ServerPlayer> COMPARE_PLAYER = (soak, sponge) -> soak.spongeEntity()
            .equals(sponge);
    private static final Function<ServerPlayer, SoakPlayer> CREATE_PLAYER = SoakPlayer::new;
    private final LinkedTransferQueue<SoakWorld> worlds = new LinkedTransferQueue<>();
    private final LinkedTransferQueue<SoakPlayer> players = new LinkedTransferQueue<>();

    private <Sponge, Soak extends SoakSingleInstance<Sponge>> Soak getOrCreate(Collection<Soak> collection, BiPredicate<Soak, Sponge> match, Function<Sponge, Soak> create, Sponge sponge) {
        var found = collection.stream().filter(soak -> match.test(soak, sponge)).findAny();
        if (found.isPresent()) {
            return found.get();
        }
        var overrideInstance = collection.stream().filter(soak -> soak.isSame(sponge)).findAny();
        if (overrideInstance.isPresent()) {
            var instance = overrideInstance.get();
            instance.setSponge(sponge);
            return instance;
        }

        var created = create.apply(sponge);
        collection.add(created);
        return created;
    }

    public SoakWorld get(ServerWorld world) {
        return getOrCreate(this.worlds, COMPARE_WORLD, CREATE_WORLD, world);
    }

    public SoakPlayer get(ServerPlayer player) {
        return getOrCreate(this.players, COMPARE_PLAYER, CREATE_PLAYER, player);
    }

}
