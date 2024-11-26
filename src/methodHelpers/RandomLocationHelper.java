package methodHelpers;

import Main.Main;
import itumulator.world.Location;
import itumulator.world.RabbitHole;
import itumulator.world.World;

import java.util.Random;

public class RandomLocationHelper {
    Random r = new Random();
    World world;

    public RandomLocationHelper(World world) {
        super();
        this.world = world;
    }

    public Location getRandomLocation() {
        Location l = new Location(r.nextInt(Main.getSize()), r.nextInt(Main.getSize()));
        while (world.getTile(l) != null) {
            l = new Location(r.nextInt(Main.getSize()), r.nextInt(Main.getSize()));
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

    /**
     * Gives a random location in the world that is not occupied by a non blocking object
     * @return Location
     */
    public Location getRandomNonBlockingLocation() {
        Location l = new Location(r.nextInt(Main.getSize()), r.nextInt(Main.getSize()));
        while (world.getNonBlocking(l) != null) {
            l = new Location(r.nextInt(Main.getSize()), r.nextInt(Main.getSize()));
        }
        return l;
    }
}
