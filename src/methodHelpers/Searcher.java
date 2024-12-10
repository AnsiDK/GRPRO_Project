package methodHelpers;

import ourNonBlocking.Foliage;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.HashSet;
import java.util.Set;

public class Searcher {
    static protected World world;

    public Searcher(World world) {
        super();
        this.world = world;
    }

    /**
     * a method for us to determine the location of a specified class within a given radius
     * @param clazz provides the class we are searching for
     * @param l provides the center of our search
     * @param radius provides the radius we are searching in
     * @return a location of a specified object
     */
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

    /**
     * A method for us to determine wheather an object of a given class is within a given radius
     * @param l provides the center of our search
     * @param clazz provides the class we are searching for
     * @param radius provides the radius we are searching in
     * @return a boolean that depends on whether an object of a given class is within a given radius
     */
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

    /**
     * A method that gives us a set of all the objects of a certain class within a given radius
     * @param clazz provides the class we are looking for
     * @param l provides the location we want to search from
     * @param radius provides the radius of our search
     * @return a set containing all the objects of a certain class within a given radius
     */
    public static Set getAll(Class<?> clazz, Location l, int radius) {
        Set<Location> set = world.getSurroundingTiles(l, radius);
        Set<Object> all = new HashSet<Object>();

        for (Location location : set) {
            if (world.getTile(location) != null) {
                if (world.getTile(location).getClass() == clazz) {
                    all.add(world.getTile(location));
                }
            }
        }

        return all;
    }

    /**
     * A methoh for us to determine whether there is foliage at a given location
     * @param l provides the location that we want to check
     * @return a boolean that depends on whether there is foliage at a given location
     */
    public boolean foliageAt(Location l) {
        return world.getNonBlocking(l) instanceof Foliage;
    }

    /**
     * A method for us to determine the length between two given locations
     * @param a
     * @param b
     * @return a double that depends on the distance between the two locations
     */
    public static double getDistance(Location a, Location b) {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
    }
}
