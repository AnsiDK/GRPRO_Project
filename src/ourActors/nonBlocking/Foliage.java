package ourActors.nonBlocking;

import Main.Main;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class Foliage implements NonBlocking, Actor {
    protected World world;
    Random r;

    public Foliage(World world) {
        super();
        this.world = world;
        r = new Random();
    }

    @Override
    public void act(World world) {
        spread(world.getCurrentLocation());
    }

    protected abstract void spread(Location location);
}
