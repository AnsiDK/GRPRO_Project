package itumulator.simulator;

import Main.Main;
import itumulator.world.RabbitHole;
import itumulator.world.World;
import itumulator.world.Location;
import methodHelpers.TimeManager;
import methodHelpers.Searcher;
import java.util.*;

public class Rabbit implements Actor{

    private final Random r = new Random();
    private Searcher searcher;
    private TimeManager timeManager;

    private int age = 0;
    private int stepsLived = 0;
    private int eatenGrass = 0;
    private int energy = 10;
    public RabbitHole hole;
    private Boolean isOnMap = true;
    private Location target;
    private int targetX;
    private int targetY;
    private World world;
    private boolean hasGrown = false;

    public Rabbit(World world) {
        super();
        initializeRabbit(null, true, world);
    }

    //Overloaded Rabbit function for small rabbits so that they come "attached" to a hole
    public Rabbit(RabbitHole hole, World world) {
        super();
        initializeRabbit(hole, false, world);
    }

    //Initializing a rabbit
    private void initializeRabbit(RabbitHole hole, boolean isOnMap, World world) {
        this.hole = hole;
        this.isOnMap = isOnMap;
        this.world = world;
        searcher = new Searcher(world);
        timeManager = new TimeManager(this);
    }

    @Override
    public void act(World world) {
        actAlways();                                //Do this every update
        if (world.isDay()) { actDay(); }            //Do this during the day
        else { actNight(); }                        //Do this at night
    }

    //Always called when acting
    void actAlways() {
        updateAge();
    }

    //Called when acting during day
    void actDay() {
        timeManager.updateTime(true);

        if (isOnMap) {
            Location loc = world.getCurrentLocation();
            if (eatenGrass == 0 && searcher.isInVicinity(loc, Grass.class, 3) && target == null) {
                Location l = searcher.searchForObject(Grass.class, loc, 3);
                setTarget(l);
            }

            if (target != null) {
                goTowardsTarget();
            } else {
                moveRandomly();
            }

            if (r.nextInt(eatenGrass + 2) == 0 && searcher.grassAt(world.getLocation(this))) {
                eatGrass();
            }
        }

        if (!isOnMap) { exitHole(); }
    }

    //Called when acting at night
    void actNight() {
        timeManager.updateTime(false);

        if (isOnMap) {
            if (hole == null && target == null) { findHole(); }

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
        eatenGrass ++;
        world.delete(world.getNonBlocking(world.getLocation(this)));
    }

    //The bunny moves to a random location in its vicinity
    void moveRandomly() {
        Set<Location> set = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(set);
        if (!list.isEmpty()) {
            Location loc = list.get(r.nextInt(list.size()));

            world.move(this, loc);
        }
    }

    //The rabbit goes towards its target
    void goTowardsTarget() {
        Location start = world.getLocation(this);
        double minDist = searcher.getDistance(start, target);
        Location bestMove = start;

        //Determine the best square for a rabbit to move to
        for (Location neighbor : world.getEmptySurroundingTiles()) {
            double dist = searcher.getDistance(neighbor, target);

            if (dist < minDist) {
                minDist = dist;
                bestMove = neighbor;
            }
        }

        //Move closer to the target
        world.move(this, bestMove);

        if (bestMove.equals(target)) {
            performAction();
            target = null;
        }
    }

    //Rabbit tries to find premade hole
    public void findHole() {
        if (this.hole == null) {
            Location l = world.getLocation(this);

            Location holeLocation = searcher.searchForObject(RabbitHole.class, l, 3);

            if (holeLocation != null) {
                this.hole = (RabbitHole) world.getTile(holeLocation);
                setTarget(holeLocation);  // Move towards the found hole
            } else {
                // No nearby hole found, dig a new one
                digHole();
                setTarget(world.getLocation(hole));
            }
        }

        if (hole != null && target == null) {
            setTarget(world.getLocation(hole));
        }
    }

    //A rabbit digs a hole
    public void digHole () {
        Location l = world.getLocation(this);

        if (searcher.grassAt(l)) {
            world.delete(world.getNonBlocking(l));
        }

        RabbitHole rabbitHole = new RabbitHole(world);
        world.setTile(l, rabbitHole);
        this.hole = rabbitHole;
    }

    //Rabbit enters rabbit hole
    public void enterRabbitHole() {
        isOnMap = false;
        hole.addRabbit(this);
        world.remove(this);
    }

    //Exit the rabbit hole
    public void exitHole() {
        if (hole.isFirst(this) && world.isTileEmpty(world.getLocation(hole))) {
            hole.removeRabbit(this);
            Location l = world.getLocation(hole);
            world.setTile(l, this);
            isOnMap = true;
        }
    }

    //When a rabbit has had enough food, has grown or above and shares a hole with another rabbit, it reproduces
    public void reproduce() {
        if (hole.hasGrownRabbits() && hole != null) {
            Rabbit babyRabbit = new Rabbit(this.hole, world);
            world.add(babyRabbit);
            hole.addRabbit(babyRabbit);
        }
    }

    //Sets a target for the rabbit
    public void setTarget(Location location) {
        target = location;
        try {
            targetX = target.getX();
            targetY = target.getY();
        } catch (NullPointerException e) {
            //Do nothing
        }
    }

    //Rabbit performs action when it reaches target
    void performAction() {
        Object o = world.getNonBlocking(target);

        if (o instanceof RabbitHole) {
            if (this.hole == null) {
                this.hole = (RabbitHole) world.getTile(target);
            }

            enterRabbitHole();
            setTarget(null);
        }
        if (o instanceof Grass) {
            eatGrass();
            setTarget(null);
        }
    }

    //Update age
    public void updateAge() {
        stepsLived++;
        if (stepsLived % 20 == 0) { age++; }
        if (age % 3 == 0) { grow(); }
    }

    //Rabbit grows every 3 days
    public void grow(){
        hasGrown = true;
    }

    //A rabbit dies
    public void die () {
        System.out.println("Rabbit died");
        isOnMap = false;
        world.delete(this);
    }

    //Getter for eatenGrass
    public int getEatenGrass () {
        return eatenGrass;
    }

    //setter for eatenGrass
    public void setEatenGrass (int value) {
        eatenGrass = value;
    }

    //Getter for hasGrown value
    public boolean hasGrown() {
        return hasGrown;
    }

    //Getter for isOnMap value
    public boolean isOnMap() {
        return isOnMap;
    }
}