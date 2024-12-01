package ourActors.blocking;

import Main.Main;
import ourActors.nonBlocking.Grass;
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
    private void initializeRabbit(RabbitHole hole, boolean isOnMap, World world) {
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
            world.delete(world.getNonBlocking(world.getLocation(this)));
        }
    }

    //The bunny moves to a random location in its vicinity
    void moveRandomly() {
        Set<Location> set = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(set);
        if (!list.isEmpty()) {
            Location loc = list.get(r.nextInt(list.size()));

            world.move(this, loc);
        }
        if (r.nextInt(foodEaten + 2) == 0 && searcher.grassAt(world.getLocation(this))) {
            eatGrass();
        }
    }

    //The rabbit goes towards its target
    void goTowardsTarget() {
        Location start = world.getLocation(this);
        int moveX = start.getX();
        int moveY = start.getY();
        int endX = target.getX();
        int endY = target.getY();

        if (moveX > endX) {
            moveX--;
        } else if (moveX < endX) {
            moveX++;
        }

        if (moveY > endY) {
            moveY--;
        } else if (moveY < endY) {
            moveY++;
        }

        Location move = new Location(moveX, moveY);

        if (world.isTileEmpty(move)) {
            //Move closer to the target
            world.move(this, move);
        } else {
            List<Location> alternatives = new ArrayList<>(world.getEmptySurroundingTiles());
            if (!alternatives.isEmpty()) {
                world.move(this, alternatives.get(r.nextInt(alternatives.size())));
            }
        }

        if (target.equals(move)) {
            performAction();
            target = null;
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

        if (searcher.grassAt(l)) {
            world.delete(world.getNonBlocking(l));
        }

        RabbitHole rabbitHole = new RabbitHole(world);
        world.setTile(l, rabbitHole);
        this.home = rabbitHole;
    }

    //Rabbit enters rabbit hole
    public void enterHome() {
        isOnMap = false;
        home.addRabbit(this);
        world.remove(this);
    }

    //Exit the rabbit hole
    public void leaveHome() {
        if (home.isFirst(this) && world.isTileEmpty(world.getLocation(home))) {
            home.removeRabbit(this);
            Location l = world.getLocation(home);
            world.setTile(l, this);
            isOnMap = true;
        }
    }

    //When a rabbit has had enough food, has grown or above and shares a hole with another rabbit, it reproduces
    public void reproduce() {
        if (home.hasGrownRabbits() && home != null) {
            Rabbit babyRabbit = new Rabbit(this.home, world);
            world.add(babyRabbit);
            home.addRabbit(babyRabbit);
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
}
