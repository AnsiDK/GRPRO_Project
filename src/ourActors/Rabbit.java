package ourActors;

import Main.Main;
import ourNonBlocking.Grass;
import ourNonBlocking.Home;
import ourNonBlocking.RabbitHole;
import itumulator.world.World;
import itumulator.world.Location;
import methodHelpers.TimeManager;
import methodHelpers.Searcher;
import java.util.*;

public class Rabbit extends Animal {

    public Rabbit(World world) {
        super(world);
        initializeRabbit(null, true, world);
    }

    //Overloaded Rabbit function for small rabbits so that they come "attached" to a hole
    public Rabbit(RabbitHole hole, World world) {
        super(world);
        initializeRabbit(hole, false, world);
    }

    //Initializing a rabbit
    private void initializeRabbit(Home hole, boolean isOnMap, World world) {
        this.home = hole;
        this.isOnMap = isOnMap;
        this.world = world;
        searcher = new Searcher(world);
        timeManager = new TimeManager(this);
        r = new Random();
    }

    //Called when acting during day
    void actDay() {
        timeManager.updateTime(true);

        if (isOnMap) {
            Location loc = world.getCurrentLocation();

            //Finding grass is 1st priority
            if (foodEaten == 0 && searcher.isInVicinity(loc, Grass.class, 3) && target == null) {
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

    //Called when acting at night
    void actNight() {
        timeManager.updateTime(false);

        if (isOnMap) {
            if (home == null && target == null) { findHome(); }

            if (target != null) {
                goTowardsTarget();
            } else {
                moveRandomly();
            }
        }
    }

    //The rabbit eats grass
    void eatGrass() {
        Main.setNonBlockingObjects(Main.getNonBlockingObjects() - 1);
        if (world.getNonBlocking(world.getLocation(this)) instanceof Grass) {
            foodEaten ++;
            energyOfAnimal += 2;
            world.delete(world.getNonBlocking(world.getLocation(this)));
        }
    }

    //The bunny moves to a random location in its vicinity
    @Override
    void moveRandomly() {
        super.moveRandomly();
        if (r.nextInt(foodEaten + 2) == 0 && searcher.foliageAt(world.getLocation(this))) {
            eatGrass();
        }
    }

    //Rabbit tries to find premade hole
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

    //A rabbit digs a hole
    public void buildHome () {
        Location l = world.getLocation(this);

        if (searcher.foliageAt(l)) {
            world.delete(world.getNonBlocking(l));
        }

        RabbitHole rabbitHole = new RabbitHole(world);
        world.setTile(l, rabbitHole);
        this.home = rabbitHole;
    }

    //When a rabbit has had enough food, has grown or above and shares a hole with another rabbit, it reproduces
    public void reproduce() {
        if (home.hasGrownAnimals() && home != null) {
            Rabbit babyRabbit = new Rabbit((RabbitHole) this.home, world);
            world.add(babyRabbit);
            home.addAnimal(babyRabbit);
        }
    }

    //Rabbit performs action when it reaches target
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

    public RabbitHole getHome() {
        return (RabbitHole) home;
    }
}
