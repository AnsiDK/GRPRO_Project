package ourActors;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.Searcher;
import methodHelpers.TimeManager;
import ourNonBlocking.Carcass;
import ourNonBlocking.Foliage;
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

    public Animal(World world) {
        this.world = world;
        r = new Random();
        hasGrown = false;
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

    void actAlways() {
        updateAge();
    }

    abstract void actDay();
    abstract void actNight();

    void moveRandomly() {
        Set<Location> set = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(set);
        if (!list.isEmpty()) {
            Location loc = list.get(r.nextInt(list.size()));

            world.move(this, loc);
        }
    }

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

        if (world.getTile(move) instanceof Rabbit && this instanceof Wolf) {
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

    protected void enterHome() {
        isOnMap = false;
        home.addAnimal(this);
        world.remove(this);
    }

    //Exit the rabbit home
    protected void leaveHome() {
        if (home.isFirst(this) && world.isTileEmpty(world.getLocation(home))) {
            home.removeAnimal(this);
            Location l = world.getLocation(home);
            world.setTile(l, this);
            isOnMap = true;
        }
    }

    public abstract void reproduce();

    public void setTarget(Location location) {
        target = location;
    }

    public void updateAge() {
        stepsLived++;
        if (stepsLived % 20 == 0) { age++; }
        if (age % 3 == 0 && age != 0) { grow(); }
    }

    public void grow(){
        hasGrown = true;
    }

    public void die () {
        isOnMap = false;
        Location l = world.getLocation(this);
        world.delete(this);
        Object object = world.getNonBlocking(l);
        if (object != null) {
            world.delete(object);
        }
        world.setTile(l, new Carcass(world, energy + 1));
    }

    public int getFoodEaten() {
        return foodEaten;
    }

    public void setFoodEaten(int value) {
        foodEaten = value;
    }

    public boolean isOnMap() {
        return isOnMap;
    }

    public boolean getHasGrown() {
        return hasGrown;
    }

    protected void setIsOnMap(boolean b) {
        isOnMap = b;
    }
}
