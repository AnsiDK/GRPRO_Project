package methodHelpers;

import Main.Main;
import itumulator.world.Location;
import ourNonBlocking.RabbitHole;
import itumulator.world.World;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomLocationHelper extends Searcher{
    Random r = new Random();

    public RandomLocationHelper(World world) {
        super(world);
        this.world = world;
    }

    /**
     * A function that gives a random location in the world where there is nothing
     * @param size Provides size of the world
     * @return an empty location on the map
     */
    public Location getRandomLocation(int size) {
        Location l = new Location(r.nextInt(size), r.nextInt(size));
        while (world.getTile(l) != null) {
            l = new Location(r.nextInt(size), r.nextInt(size));
        }
        return l;
    }

    /**
     * A function the gives us a random location in the world that does not contain a nonBlocking object.
     * @return a location on the map that is not occupied by a nonBlocking object
     */
    public Location getNonBlockingLocation(int size) {
        Location l = new Location(r.nextInt(size), r.nextInt(size));
        while (world.getNonBlocking(l) != null) {
            l = new Location(r.nextInt(size), r.nextInt(size));
        }
        return l;
    }

    /**
     * A function that gives us a set of increasingly larger empty surrounding tiles.
     * @param location provides the center location from where the search will start
     * @param radius provides the radius we are searching in, this is increased everytime we fail to find empty tiles
     * @return a set with empty surrounding tiles
     */
    public static Set<Location> getMoreEmptySurroundingTiles(Location location, int radius) {
        Set<Location> set = new HashSet<>(world.getSurroundingTiles(location, radius));
        set.removeIf(l -> !world.isTileEmpty(l));
        if (set.isEmpty()) {
            set = getMoreEmptySurroundingTiles(location, radius + 1);
        }
        return set;
    }
}
