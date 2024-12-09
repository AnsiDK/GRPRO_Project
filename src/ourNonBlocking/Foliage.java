package ourNonBlocking;

import Main.Main;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.Random;

public abstract class Foliage implements NonBlocking, Actor {
    protected World world;
    Random r;
    protected int lifeTime;

    /**
     * Constructor for foliage, this is called by subclasses of foliage, and it contains all the stuff we need for all types of foliage
     * @param world provides information such as location and the world which foliage is in
     */
    public Foliage(World world) {
        super();
        this.world = world;
        r = new Random();
        Main.setNonBlockingObjects(Main.getNonBlockingObjects()+1);
    }

    /**
     * Overridden act method, this assures, that foliage tries to spreas, and that it dies after a certain time.
     * @param world provides information from the world we are acting in
     */
    @Override
    public void act(World world) {
        lifeTime--;
        if (lifeTime <= 0) {
            world.delete(this);
            Main.setNonBlockingObjects(Main.getNonBlockingObjects()-1);
        }
        spread(world.getCurrentLocation());
    }

    /**
     * Abstract method that determines how foliage should spread
     * @param location provides the location from which foliage should spread
     */
    protected abstract void spread(Location location);
}
