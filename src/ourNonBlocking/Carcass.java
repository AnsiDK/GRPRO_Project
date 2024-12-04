package ourNonBlocking;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.*;
import java.util.Random;

public class Carcass implements Actor, DynamicDisplayInformationProvider, NonBlocking {
    private int energy;
    private World world;
    private Random r;
    private boolean infested = false;

    public Carcass(World world, int energy) {
        super();
        this.energy = energy;
        this.world = world;
        r = new Random();
    }

    @Override
    public void act(World world) {
        energy--;

        if (r.nextInt(10) == 0) {
            growShrooms();
        }

        if (energy < 1) {
            dissapear();
        }
    }

    public void dissapear() {
        Location l = world.getLocation(this);
        world.remove(this);
        if (infested) {
            Object object = world.getNonBlocking(l);
            if (object != null) {
                world.delete(object);
            }
            world.setTile(l, new MushRoom(world, energy + 30));
        }
        world.delete(this);
    }

    public void growShrooms() {
        infested = true;
    }

    @Override
    public DisplayInformation getInformation() {
        if (energy < 20) {
            return new DisplayInformation(Color.blue, "carcass-small");
        } else {
            return new DisplayInformation(Color.ORANGE, "carcass");
        }
    }
}
