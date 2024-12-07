package ourNonBlocking;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.Random;

public class Carcass implements Actor, DynamicDisplayInformationProvider {
    private int energyOfAnimal;
    private World world;
    private Random r;
    private MushRoom mushRoom;

    public Carcass(World world, int energyOfAnimal) {
        super();
        this.energyOfAnimal = energyOfAnimal;
        this.world = world;
        r = new Random();
    }

    @Override
    public void act(World world) {
        energyOfAnimal--;

        if (r.nextInt(10) == 0) {
            growShrooms();
        }

        if (energyOfAnimal < 1) {
            dissapear();
        }
    }

    public void dissapear() {
        if (mushRoom == null) {
            mushRoom = new MushRoom(world, energyOfAnimal + 1);
        }
        Location location = world.getLocation(this);
        if (location != null) {
            world.delete(this);
            world.setTile(location, mushRoom);
        }
    }

    public void growShrooms() {
        if (mushRoom == null) {
            mushRoom = new MushRoom(world, energyOfAnimal + 1);
        }
    }

    @Override
    public DisplayInformation getInformation() {
        if (energyOfAnimal < 20) {
            return new DisplayInformation(Color.blue, "carcass-small");
        } else {
            return new DisplayInformation(Color.ORANGE, "carcass");
        }
    }
}
