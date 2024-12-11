package ourActors;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.Searcher;
import methodHelpers.TimeManager;
import ourNonBlocking.Carcass;
import ourNonBlocking.Home;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class Animal implements Actor, DynamicDisplayInformationProvider {
    protected World world;
    protected Random r;
    protected int age = 0;
    protected boolean isOnMap = true;
    protected boolean hasGrown;
    public Home home;
    protected Location target;
    protected int foodEaten = 0;
    protected int stepsLived = 0;
    protected Searcher searcher;
    protected TimeManager timeManager;
    protected int energy;

    /**
     * Constructor for an animal, this is called by all subclasses of an animal
     * @param world provides the world where the animal will live
     */
    public Animal(World world) {
        this.world = world;
        r = new Random();
        hasGrown = false;
        timeManager = new TimeManager(this);
        energy = 25;
    }

    @Override
    public DisplayInformation getInformation() {
        String name = (this.getClass().getSimpleName()).toLowerCase();
        if (hasGrown) {
            return new DisplayInformation(Color.red, name);
        } else {
            return new DisplayInformation(Color.black, name + "-small");
        }
    }

    @Override
    public void act(World world) {
        actAlways();
        if (world.isDay()) { actDay(); }
        if (world.isNight()) { actNight(); }
    }

    /**
     * A method we always have to call
     */
    void actAlways() {
        updateAge();
        energy--;
    }

    /**
     * A method that is called during the day
     */
    void actDay() {
        timeManager.updateTime(true);
    }

    /**
     * A method that is called during night
     */
    void actNight() {
        timeManager.updateTime(false);
    }

    /**
     * A method for an animal to move around randomly
     */
    void moveRandomly() {
        if (energy < 10 && r.nextInt(5) == 0) {
            return;
        }

        else if (energy > 19 && energy < 40 && r.nextInt(20) == 0) {
            return;
        }

        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());
        if (!list.isEmpty()) {
            Location loc = list.get(r.nextInt(list.size()));

            world.move(this, loc);
        }
    }

    /**
     * A method for an animal to approach its target. This is done by comparing x and y values, finding an optimal move and the executing the action once were at our target or next to it.
     */
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

        if (world.getTile(move) instanceof Rabbit || world.getTile(move) instanceof Carcass && this instanceof Wolf) {
            performAction();
            target = null;
            return;
        }

        if (world.getTile(move) instanceof Animal && this instanceof Bear) {
            performAction();
            target = null;
            return;
        }

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

    abstract void performAction();
    public abstract void findHome();
    abstract void buildHome();

    /**
     * A methodd for our animals such that they can enter a home
     */
    protected void enterHome() {
        isOnMap = false;
        home.addAnimal(this);
        world.remove(this);
    }

    /**
     * A method for our animals so that they can leave their home
     */
    protected void leaveHome() {
        if (home.isFirst(this) && world.isTileEmpty(world.getLocation(home)) && !(isOnMap)) {
            home.removeAnimal(this);
            Location l = world.getLocation(home);
            try {
                world.setTile(l, this);
            } catch (IllegalArgumentException e) {
                //sometimes the code tries to put the same animal on the map multiple times, and i don't know what causes it
            }
            isOnMap = true;
        }
    }

    public abstract void reproduce();

    public void setTarget(Location location) {
        target = location;
    }

    /**
     * A method that updates our animals age and makes them "grow" if they have lived long enough
     */
    public void updateAge() {
        stepsLived++;
        if (stepsLived % 20 == 0) { age++; }
        if (age % 3 == 0 && age != 0) { grow(); }
    }

    /**
     * A method that grows an animal
     */
    public void grow(){
        hasGrown = true;
        energy -=5;
    }

    /**
     * A method that handles the death of an animal, this includes placing carcasses on the map
     */
    public void die () {
        isOnMap = false;
        Location l = world.getLocation(this);
        Object object = world.getNonBlocking(l);
        if (object instanceof Home) {
            return;
        }
        if (object != null) {
            world.delete(object);
        }
        world.delete(this);
        world.setTile(l, new Carcass(world, energy, l));
    }

    public void influence() {
        energy--;
    }

    public int getFoodEaten() {
        return foodEaten;
    }

    public boolean isOnMap() {
        return isOnMap;
    }

    public boolean getHasGrown() {
        return hasGrown;
    }

    public int getEnergy() {
        return energy;
    }

    public void setFoodEaten(int i) {
        foodEaten = i;
    }

    public Home getHome() {
        return home;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}