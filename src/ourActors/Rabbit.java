package ourActors;

import Main.Main;
import ourNonBlocking.Carcass;
import ourNonBlocking.Grass;
import ourNonBlocking.Home;
import ourNonBlocking.RabbitHole;
import itumulator.world.World;
import itumulator.world.Location;
import methodHelpers.TimeManager;
import methodHelpers.Searcher;
import java.util.*;

public class Rabbit extends Animal {

    /**
     * Rabbits constructor
     * @param world provides the world where the rabbit will live
     */
    public Rabbit(World world) {
        super(world);
        initializeRabbit(null, true, world);
    }

    /**
     * Overloaded constructor for a rabbit born within a rabbit hole
     * @param hole provides the hole given to the rabbit at birth
     * @param world provides the world where the rabbit will live
     */
    public Rabbit(RabbitHole hole, World world) {
        super(world);
        initializeRabbit(hole, false, world);
    }

    /**
     * A method such that we don't have to write it under both constructors
     * @param hole provides the hole given to the rabbit at birth if it was born in a hole
     * @param isOnMap provides a boolean which determines whether the rabbit is on the map or not
     * @param world provides the world where the rabbit will live
     */
    private void initializeRabbit(Home hole, boolean isOnMap, World world) {
        this.home = hole;
        this.isOnMap = isOnMap;
        this.world = world;
        searcher = new Searcher(world);
        r = new Random();
    }

    /**
     * Overridden method for rabbits to determine what to do during the day. This includes looking for grass, or just jumping around randomly
     */
    @Override
    void actDay() {
        super.actDay();

        if (isOnMap) {
            Location loc = world.getCurrentLocation();

            //Finding grass is 1st priority
            if (energy < 30 && searcher.isInVicinity(loc, Grass.class, 3) && target == null) {
                Location l = searcher.searchForObject(Grass.class, loc, 3);
                setTarget(l);
            }

            if (target != null) {
                goTowardsTarget();
            } else {
                moveRandomly();
            }
        }

        if (!isOnMap) { leaveHome(); }
    }

    /**
     * Overridden method for rabbits to determine what to do at night. This mostly includes going towards their home, unless they don't have one, in which case they will search for or make one
     */
    @Override
    void actNight() {
        super.actNight();

        if (isOnMap) {
            if (home == null && target == null) { findHome(); }

            if (target != null) {
                goTowardsTarget();
            } else {
                moveRandomly();
            }
        }
    }

    /**
     * A method for our rabbits to eat grass
     */
    void eatGrass() {
        Main.setNonBlockingObjects(Main.getNonBlockingObjects() - 1);
        if (world.getNonBlocking(world.getLocation(this)) instanceof Grass) {
            foodEaten ++;
            energy += 5;
            world.delete(world.getNonBlocking(world.getLocation(this)));
        }
    }

    /**
     * An overridden method for rabbits that make it so that there is a chance they will eat grass after moving randomly, this chance falls for every piece of grass eaten
     */
    @Override
    void moveRandomly() {
        super.moveRandomly();
        if (r.nextInt(foodEaten + 2) == 0 && searcher.foliageAt(world.getLocation(this))) {
            eatGrass();
        }
    }

    /**
     * A method for our rabbits to find a nearby hole, unless there is none, in which case they will dig a new one. Due to how act is called, the "first" rabbit can dig a hole which the "second" can then find during the same step.
     */
    public void findHome() {
        if (this.home == null) {
            Location l = world.getLocation(this);

            Location holeLocation = searcher.searchForObject(RabbitHole.class, l, 3);

            if (holeLocation != null) {
                this.home = (RabbitHole) world.getTile(holeLocation);
                setTarget(holeLocation);  // Move towards the found hole
            } else {
                // No nearby hole found, dig a new one
                buildHome();
                setTarget(world.getLocation(home));
            }
        }

        if (home != null && target == null) {
            setTarget(world.getLocation(home));
        }
    }

    /**
     * A method for our rabbits that digs a hole and makes sure to delete whatever is in the way
     */
    public void buildHome () {
        Location l = world.getLocation(this);

        if (searcher.foliageAt(l) || world.getNonBlocking(l) instanceof Carcass) {
            world.delete(world.getNonBlocking(l));
        }

        RabbitHole rabbitHole = new RabbitHole(world);
        world.setTile(l, rabbitHole);
        this.home = rabbitHole;
    }

    /**
     * A method for our rabbits to reproduce
     */
    public void reproduce() {
        if (home.hasGrownAnimals() && home != null) {
            Rabbit babyRabbit = new Rabbit((RabbitHole) this.home, world);
            world.add(babyRabbit);
            home.addAnimal(babyRabbit);
        }
    }

    /**
     * An overridden method for our rabbits to determine what to do when they reach their target
     */
    @Override
    void performAction() {
        Object o = world.getNonBlocking(target);

        if (o instanceof RabbitHole) {
            if (this.home == null) {
                this.home = (RabbitHole) world.getTile(target);
            }

            enterHome();
            setTarget(null);
        }
        if (o instanceof Grass) {
            eatGrass();
            setTarget(null);
        }
    }
}
