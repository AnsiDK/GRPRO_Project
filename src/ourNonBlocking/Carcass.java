package ourNonBlocking;

import Main.Main;
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
    private Mushroom mushroom;
    private Location location;
    private int chanceToRot;

    /**
     * Constructor for carcasses
     * @param world provides information regarding location, and when to act
     * @param energy retains the energy of a dead animal
     * @param location provides the location for the eventual mushroom to be put on
     */
    public Carcass(World world, int energy, Location location) {
        super();
        this.energy = energy + 15;
        this.world = world;
        r = new Random();
        this.location = location;
        chanceToRot = r.nextInt(40) + 1;
        Main.setNonBlockingObjects(Main.getNonBlockingObjects()+1);
    }

    /**
     * Overridden act method from the actor interface, this ensures that there is a chance for carcasses to grow mushrooms, in which case it will grow one (The longer the carcass lies on the ground, the more likley it is)
     * @param world provides the "world" an actor acts in
     */
    @Override
    public void act(World world) {
        energy--;

        if (r.nextInt(chanceToRot) == 0 && mushroom == null) {
            growShrooms();
        } else {
            if (chanceToRot != 1) {
                chanceToRot--;
            }
        }

        if (energy < 1) {
            dissapear();
        }
    }

    /**
     * A method that handles the "dissapearence" of carcasses, and ensures that a mushroom is placed if they were growing inside it.
     */
    public void dissapear() {
        Main.setNonBlockingObjects(Main.getNonBlockingObjects()-1);
        Location l = world.getLocation(this);
        world.remove(this);
        if (mushroom != null) {
            Object object = world.getNonBlocking(l);
            if (object != null) {
                world.delete(object);
            }
            mushroom.setEnergy(energy + 15);
            world.setTile(l, mushroom);
        }
        world.delete(this);
    }

    /**
     * A method that grows a shroom within the carcass
     */
    public void growShrooms() {
        if (mushroom == null) {
            mushroom = new Mushroom(world, location);
            world.add(mushroom);
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

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean hasFungi() {
        return mushroom != null;
    }

    public Mushroom getMushroom() {
        return mushroom;
    }
}
