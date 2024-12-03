package itumulator.simulator;

import Main.Main;
import itumulator.world.RabbitHole;
import itumulator.world.World;
import itumulator.world.Location;
import methodHelpers.TimeManager;
import methodHelpers.Searcher;
import java.util.*;

public class Animal implements Actor{

    int age;
    int stepsLived;
    boolean isOnMap;
    Location target;
    World world;
    boolean hasGrown;


    public Animal(World world) {

    }

    private void initialize(boolean isOnMap, World world) {
        this.isOnMap = isOnMap;
        this.world = world;
    }

    // Set target
    public void setTarget(Location location) {
        target = location;
        try {
            int targetX = target.getX();
            int targetY = target.getY();
        } catch (NullPointerException e) {
            //Do nothing
        }
    }

    //Update age
    public void updateAge() {
        stepsLived++;
        if (stepsLived % 20 == 0) { age++; }
        if (age % 3 == 0) { grow(); }
    }


    public void reproduce() {

    }
}
