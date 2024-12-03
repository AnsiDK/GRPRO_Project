package methodHelpers;

import ourNonBlocking.Foliage;
import ourNonBlocking.Grass;
import itumulator.world.Location;
import ourNonBlocking.RabbitHole;
import itumulator.world.World;

import java.util.Set;

public class Searcher {
    static protected World world;

    public Searcher(World world) {
        super();
        this.world = world;
    }

    public static Location searchForObject(Class<?> clazz, Location l, int radius) {
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
            if (world.getTile(location) != null) {
                if (world.getTile(location).getClass() == clazz) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean foliageAt(Location l) {
        return world.getNonBlocking(l) instanceof Foliage;
    }

    //Distance helper
    public static double getDistance(Location a, Location b) {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
    }
}
