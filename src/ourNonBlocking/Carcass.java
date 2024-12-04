package ourNonBlocking;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

import java.awt.*;
import java.util.Random;

public class Carcass implements Actor, DynamicDisplayInformationProvider {
    private int energy;
    private World world;
    private Random r;
    private MushRoom mushRoom;

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
        world.delete(this);
        world.setTile(world.getLocation(this), mushRoom);
    }

    public void growShrooms() {
        if (mushRoom == null) {
            mushRoom = new MushRoom(world, energy + 1);
        }
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
