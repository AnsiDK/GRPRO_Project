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

    public Foliage(World world) {
        super();
        this.world = world;
        r = new Random();
        Main.setNonBlockingObjects(Main.getNonBlockingObjects()+1);
    }

    @Override
    public void act(World world) {
        spread(world.getCurrentLocation());
    }

    protected abstract void spread(Location location);
}
