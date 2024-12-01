package ourActors;


import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.Searcher;
import ourNonBlocking.Grass;

import java.util.*;

public class Bear extends Animal {
    Location territoryCenter;
    Searcher searcher;

    public Bear(World world, Location territoryCenter) {
        super(world);
        this.territoryCenter = territoryCenter;
        searcher = new Searcher(world);
    }

    @Override
    void actDay() {

        serchForFood();

        if (foodEaten == 0) {

        }

        if (searcher.getDistance(world.getCurrentLocation(), territoryCenter) > 4) {
            setTarget(territoryCenter);
        }

        if (target != null) {
            goTowardsTarget();
        } else {
            moveRandomly();
        }
    }

    @Override
    void actNight() {
        //Sleep
    }

    @Override
    void performAction() {
        if (world.getTile(target) instanceof Animal) {
            Animal animal = (Animal) world.getTile(target);
            animal.die();
            foodEaten++;
        }

        /*
        else if (world.getTile(target) instanceof berryBush) {
            BerryBush berryBush = (BerryBush) world.getTile(target);
            berryBush.eatBerry();
            foodEaten++;
        }
         */
    }

    @Override
    public void findHome() {
        //Don't think the bear has a "home"
    }

    @Override
    void buildHome() {
        //Don't think the bear has a "home"
    }

    @Override
    public void reproduce() {
        //Don't think the bear can reproduce
    }

    void serchForFood() {
        Map<Object, Location> entities = world.getEntities();

        //Problem here, somehow entities is always null

        for (Object food : entities.entrySet()) {

            Location l = entities.get(food);

            //Problem here, somehow, the location is always null

            if (l != null) {
                System.out.println("Looking for food");
                if (searcher.getDistance(l, territoryCenter) > 4) {
                    if (world.getTile(l) instanceof Rabbit) {
                        System.out.println("found Rabbit in territory");
                        setTarget(l);
                    }
                    if (food instanceof Wolf) {
                        setTarget(l);
                    }
                    if (food instanceof Grass) {
                        System.out.println("found grass in territory");
                        setTarget(l);
                    }
                }
            }
        }
    }
}
