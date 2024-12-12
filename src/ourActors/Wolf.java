package ourActors;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.Searcher;
import methodHelpers.TimeManager;
import ourNonBlocking.Carcass;
import ourNonBlocking.Foliage;
import ourNonBlocking.RabbitHole;
import ourNonBlocking.WolfDen;

import java.awt.*;
import java.util.Random;

public class Wolf extends Animal {
    private int wolfPackID;
    private boolean isPackLeader;
    private WolfPack wolfPack;

    /**
     * Constructor for our wolves
     * @param world provides the world where the wolf is
     * @param wolfPack provides the wolfPack that the wolf is a part of
     * @param wolfPackID provides the ID of the wolfPack that the wolf is part of
     */
    public Wolf(World world, WolfPack wolfPack, int wolfPackID) {
        super(world);
        searcher = new Searcher(world);
        timeManager = new TimeManager(this);
        r = new Random();
        this.wolfPackID = wolfPackID;
        this.wolfPack = wolfPack;
    }

    /**
     * An overloaded constructor for a wolf that is a packleader
     * @param world provides the world where the wolf is
     * @param wolfPack provides the wolfPack that the wolf is a part of
     * @param wolfPackID provides the ID of the wolfPack that the wolf is part of
     * @param isPackLeader provides information regarding if the wolf is a leader or not
     */
    public Wolf(World world, WolfPack wolfPack, int wolfPackID, boolean isPackLeader) {
        super(world);
        searcher = new Searcher(world);
        r = new Random();
        this.wolfPackID = wolfPackID;
        this.wolfPack = wolfPack;
        this.isPackLeader = isPackLeader;
    }

    /**
     * Overridden method for our wolves to determine what to do during the day, this includes looking for carcasses or rabbits as food, or going closer to the pack leader if they are too far away from it
     */
    @Override
    public void actDay() {
        super.actDay();

        if (isOnMap) {
            Location l = world.getCurrentLocation();
            Location food = searcher.searchForObject(Rabbit.class, l, 3);
            if (food == null) {
                food = searcher.searchForObject(Carcass.class, l, 3);
            }

            if(food != null) {
                setTarget(food);
            }

            if (wolfPack.getPackLeader().isOnMap) {
                if (!isPackLeader && distFromPackLeader() > 3 && foodEaten != 0) {
                    Location packLeaderLocation = world.getLocation(wolfPack.getPackLeader());
                    setTarget(packLeaderLocation);
                }
            }

            if (target != null) {
                goTowardsTarget();
            } else {
                moveRandomly();
            }

        } else { leaveHome(); }
    }

    /**
     * An overridden method for our wolves to determine what to do at night, much like the rabbits, they too searchers for their home
     */
    @Override
    void actNight() {
        super.actNight();

        if (isOnMap) {
            if (target == null) { findHome(); }

            if (target != null) {
                goTowardsTarget();
            } else {
                moveRandomly();
            }
        }
    }

    /**
     * Overridden method for wolves to determine what to do once they reach their target, this includes eating rabbits or carcasses, or going into their home.
     */
    @Override
    void performAction() {
        Object object = world.getTile(target);

        if (object instanceof Rabbit || object instanceof Carcass) {
            eat(object);
        }

        if (object instanceof WolfDen) {
            enterHome();
        }
    }

    /**
     * An overridden method for wolves to find a home
     */
    @Override
    public void findHome() {
        if (home == null) {
            buildHome();
            setTarget(home.getLocation());
        } else {
            setTarget(home.getLocation());
        }
    }

    /**
     * An overridden method for wolves to build a home, when this is done, the home is also assigned to all other wolves in the pact.
     */
    @Override
    void buildHome() {
        Location l = world.getLocation(this);

        if (searcher.foliageAt(l)) {
            world.delete(world.getNonBlocking(l));
        }

        WolfDen wolfDen = new WolfDen(world);
        Object object = world.getNonBlocking(world.getLocation(this));
        if (object instanceof Foliage || object instanceof Carcass) {
            world.delete(world.getNonBlocking(world.getLocation(this)));
        }
        world.setTile(l, wolfDen);
        wolfPack.buildDen(wolfDen);
    }


    @Override
    public void reproduce() {

    }

    /**
     * A method for wolves to eat different kinds of stuff, and depending on what they eat, certain stuff happens
     * @param eatenObject provides the object the wolf is about to eat
     */
    private void eat(Object eatenObject) {
        if (eatenObject instanceof Rabbit) {
            foodEaten++;
            energy += 20;
            Rabbit rabbit = (Rabbit) eatenObject;
            rabbit.die();
        } else if (eatenObject instanceof Carcass) {
            foodEaten++;
            energy += ((Carcass) eatenObject).getEnergy();
            ((Carcass) eatenObject).dissapear();
        }
    }


    private double distFromPackLeader() {
        return searcher.getDistance(world.getLocation(this), world.getLocation(wolfPack.getPackLeader()));
    }

    public void setHome(WolfDen home) {
        this.home = home;
    }

    @Override
    public DisplayInformation getInformation() {
        if (hasGrown) {
            if (isPackLeader) {
                return new DisplayInformation(Color.LIGHT_GRAY, "wolf-alpha");
            } else {
                return new DisplayInformation(Color.blue, "wolf");
            }
        } else {
            if (isPackLeader) {
                return new DisplayInformation(Color.LIGHT_GRAY, "wolf-small-alpha");
            } else {
                return new DisplayInformation(Color.red, "wolf-small");
            }
        }
    }
}
