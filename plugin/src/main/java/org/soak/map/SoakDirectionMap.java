package org.soak.map;

import org.bukkit.block.BlockFace;
import org.spongepowered.api.util.Axis;
import org.spongepowered.api.util.Direction;

public class SoakDirectionMap {

    public static Axis toSponge(org.bukkit.Axis axis) {
        return Axis.valueOf(axis.name());
    }

    public static org.bukkit.Axis toBukkit(Axis axis) {
        return org.bukkit.Axis.valueOf(axis.name());
    }

    public static Direction toSponge(BlockFace face) {
        switch (face) {
            case NORTH -> {
                return Direction.NORTH;
            }
            case EAST -> {
                return Direction.EAST;
            }
            case SOUTH -> {
                return Direction.SOUTH;
            }
            case WEST -> {
                return Direction.WEST;
            }
            case UP -> {
                return Direction.UP;
            }
            case DOWN -> {
                return Direction.DOWN;
            }
            case NORTH_EAST -> {
                return Direction.NORTHEAST;
            }
            case NORTH_WEST -> {
                return Direction.NORTHWEST;
            }
            case SOUTH_EAST -> {
                return Direction.SOUTHEAST;
            }
            case SOUTH_WEST -> {
                return Direction.SOUTHWEST;
            }
            case WEST_NORTH_WEST -> {
                return Direction.WEST_NORTHWEST;
            }
            case NORTH_NORTH_WEST -> {
                return Direction.NORTH_NORTHWEST;
            }
            case NORTH_NORTH_EAST -> {
                return Direction.NORTH_NORTHEAST;
            }
            case EAST_NORTH_EAST -> {
                return Direction.EAST_NORTHEAST;
            }
            case EAST_SOUTH_EAST -> {
                return Direction.EAST_SOUTHEAST;
            }
            case SOUTH_SOUTH_EAST -> {
                return Direction.SOUTH_SOUTHEAST;
            }
            case SOUTH_SOUTH_WEST -> {
                return Direction.SOUTH_SOUTHWEST;
            }
            case WEST_SOUTH_WEST -> {
                return Direction.WEST_SOUTHWEST;
            }
            case SELF -> {
                return Direction.NONE;
            }
        }
        throw new RuntimeException("Unknown direction map from BlockFace." + face.name());
    }

    public static BlockFace toBukkit(Direction direction) {
        switch (direction) {
            case NORTH -> {
                return BlockFace.NORTH;
            }
            case NORTH_NORTHEAST -> {
                return BlockFace.NORTH_NORTH_EAST;
            }
            case NORTHEAST -> {
                return BlockFace.NORTH_EAST;
            }
            case EAST_NORTHEAST -> {
                return BlockFace.EAST_NORTH_EAST;
            }
            case EAST -> {
                return BlockFace.EAST;
            }
            case EAST_SOUTHEAST -> {
                return BlockFace.EAST_SOUTH_EAST;
            }
            case SOUTHEAST -> {
                return BlockFace.SOUTH_EAST;
            }
            case SOUTH_SOUTHEAST -> {
                return BlockFace.SOUTH_SOUTH_EAST;
            }
            case SOUTH -> {
                return BlockFace.SOUTH;
            }
            case SOUTH_SOUTHWEST -> {
                return BlockFace.SOUTH_SOUTH_WEST;
            }
            case SOUTHWEST -> {
                return BlockFace.SOUTH_WEST;
            }
            case WEST_SOUTHWEST -> {
                return BlockFace.WEST_SOUTH_WEST;
            }
            case WEST -> {
                return BlockFace.WEST;
            }
            case WEST_NORTHWEST -> {
                return BlockFace.WEST_NORTH_WEST;
            }
            case NORTHWEST -> {
                return BlockFace.NORTH_WEST;
            }
            case NORTH_NORTHWEST -> {
                return BlockFace.NORTH_NORTH_WEST;
            }
            case UP -> {
                return BlockFace.UP;
            }
            case DOWN -> {
                return BlockFace.DOWN;
            }
            case NONE -> {
                return BlockFace.SELF;
            }
        }
        throw new RuntimeException("Unknown direction map from Direction." + direction.name());
    }
}
