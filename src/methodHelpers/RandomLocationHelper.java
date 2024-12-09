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

    public Location getRandomLocation(int size) {
        Location l = new Location(r.nextInt(size), r.nextInt(size));
        while (world.getTile(l) != null) {
            l = new Location(r.nextInt(size), r.nextInt(size));
        }
        return l;
    }

    public Location getRandomNonRabbitHoleLocation() {
        Location l = new Location(r.nextInt(Main.getSize()), r.nextInt(Main.getSize()));
        while (world.getNonBlocking(l) instanceof RabbitHole) {
            l = new Location(r.nextInt(Main.getSize()), r.nextInt(Main.getSize()));
        }
        return l;
    }

    public Location getNonBlockingLocation(int size) {
        Location l = new Location(r.nextInt(size), r.nextInt(size));
        while (world.getNonBlocking(l) != null) {
            l = new Location(r.nextInt(size), r.nextInt(size));
        }
        return l;
    }

    public static Set<Location> getMoreEmptySurroundingTiles(Location loc, int radius) {
        Set<Location> set = new HashSet<>(world.getSurroundingTiles(loc, radius));
        set.removeIf(l -> !world.isTileEmpty(l));
        if (set.isEmpty()) {
            set = getMoreEmptySurroundingTiles(loc, radius + 1);
        }
        return set;
    }
}
