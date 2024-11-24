package methodHelpers;

import itumulator.simulator.Grass;
import itumulator.world.Location;
import itumulator.world.RabbitHole;
import itumulator.world.World;

import java.util.Set;

public class Searcher {
    static private World world;

    public Searcher(World world) {
        super();
        this.world = world;
    }

    public Location searchForObject(Class<?> clazz, Location l, int radius) {
        Set<Location> set = world.getSurroundingTiles(l, radius);

        for (Location location : set) {
            if (world.getTile(location) != null) {
                if (world.getTile(location).getClass() == clazz) {
                    return location;
                }
            }
        }
        return null;
    }

    public boolean isInVicinity(Location l, Class<?> clazz, int radius) {
        Set<Location> set = world.getSurroundingTiles(l, radius);

        for (Location location : set) {
            if (world.getNonBlocking(location) != null) {
                if (world.getNonBlocking(location).getClass() == clazz) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean grassAt(Location l) {
        return world.getNonBlocking(l) instanceof Grass;
    }

    public boolean rabbitHoleAt(Location l) {
        return world.getNonBlocking(l) instanceof RabbitHole;
    }

    //Distance helper
    public double getDistance(Location a, Location b) {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
    }
}
