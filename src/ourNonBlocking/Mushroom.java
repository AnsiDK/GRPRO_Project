package ourNonBlocking;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.Searcher;
import ourActors.Animal;

import java.awt.*;
import java.util.Set;

public class Mushroom extends Foliage implements DynamicDisplayInformationProvider {
    private int energy;
    private Searcher searcher;
    private Location location;
    private boolean magic;

    //I HATE THE MUSHROOM CLASS

    /**
     * Constructor for the mushroom class
     * @param world provides information regarding the world the mushroom is in
     * @param location provides the location that the mushroom will be on when it is still inside the carcass
     */
    public Mushroom(World world, Location location) {
        super(world);
        searcher = new Searcher(world);
        this.location = location;
        lifeTime = 99999999;
        if (r.nextInt(20) == 0) {
            magic = true;
        }
    }

    /**
     * A method for a mushroom to spread, lose energu, and in the rare case it is a magic mushroom drain all animals in it's vicinity for 1 energy if it's small an 2 if it's large
     * @param world provides information regarding the world we are acting in
     */
    @Override
    public void act(World world) {
        super.act(world);
        energy--;

        if (energy < 1) {
            dissapear();
        }

        if (magic) {
            Set<Animal> set = Searcher.getAll(Animal.class, location, 3);

            for (Animal a : set) {
                if (energy <= 50) {
                    a.influence();
                }
                a.influence();
                energy++;
            }
        }
    }

    /**
     * Overridden spread method that determines how mushrooms should spread
     * @param location provides the location from where the mushrooms should spread
     */
    @Override
    protected void spread(Location location) {
        if (location == null) {
            location = this.location;
        }
        Location l = searcher.searchForObject(Carcass.class, location, 3);
        if (l != null) {
            Carcass carcass = (Carcass) world.getTile(l);
            carcass.growShrooms();
        }
    }

    /**
     * A method that handles the "dissapearance" of a mushroom, and places grass where it once were
     */
    public void dissapear() {
        world.delete(this);
        if (world.getNonBlocking(location) != null) {
            world.delete(world.getNonBlocking(location));
        }
        world.setTile(location, new Grass(world));
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    public DisplayInformation getInformation() {
        if (energy >= 50) {
            if (magic) {
                return new DisplayInformation(Color.magenta, "fungi_magic");
            } else {
                return new DisplayInformation(Color.blue, "fungi");
            }
        } else {
            if (magic) {
                return new DisplayInformation(Color.magenta, "fungi-small_magic");
            } else {
                return new DisplayInformation(Color.RED, "fungi-small");
            }
        }
    }

    public int getEnergy() {
        return energy;
    }
}
