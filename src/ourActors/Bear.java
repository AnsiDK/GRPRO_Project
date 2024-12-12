package ourActors;


import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.Searcher;
import ourNonBlocking.BerryBush;
import ourNonBlocking.Carcass;
import ourNonBlocking.Grass;

import java.awt.*;
import java.util.*;

public class Bear extends Animal {
    Location territoryCenter;
    Searcher searcher;

    /**
     * Constructor for a bear
     * @param world provides the world where the bear will live
     * @param territoryCenter provides the location of the bears territory center, which it will stay near
     */
    public Bear(World world, Location territoryCenter) {
        super(world);
        this.territoryCenter = territoryCenter;
        searcher = new Searcher(world);
    }

    /**
     * Overridden method for bears to determine what to do during the day, this includes searching for food, or going closer to the territory center whenever it goes beyond a certain threshold
     */
    @Override
    void actDay() {
        super.actDay();

        serchForFood();

        if (searcher.getDistance(world.getCurrentLocation(), territoryCenter) > 4 && foodEaten != 0) {
            setTarget(territoryCenter);
        }

        if (target != null) {
            goTowardsTarget();
        } else {
            moveRandomly();
        }
    }

    /**
     * Overridden method for bears to determine what to do at night, in our case they are just sleeping/doing nothing
     */
    @Override
    void actNight() {
        super.actNight();
        //Sleep
    }

    /**
     * An overridden method from the animal class that handles the actions performed by bears when they reach their target
     */
    @Override
    void performAction() {
        if (world.getTile(target) instanceof Animal && !(world.getTile(target) instanceof Bear)) {
            Animal animal = (Animal) world.getTile(target);
            animal.die();
            foodEaten++;
            energy += 20;
        }

        else if (world.getTile(target) instanceof Carcass) {
            Carcass carcass = (Carcass) world.getTile(target);
            foodEaten++;
            energy += carcass.getEnergy();
            carcass.dissapear();
        }

        else if (world.getNonBlocking(target) instanceof BerryBush) {
            BerryBush berryBush = (BerryBush) world.getNonBlocking(target);
            if (berryBush.getBerries()) {
                berryBush.eatBerry();
                foodEaten++;
                energy += 5;
            }
        }
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

    public Location getTerritoryCenter() {
        return territoryCenter;
    }

    /**
     * Method for bears to search for food, this is done by using the "searcher" and looking for objects of certain classes within the bears territory
     */
    void serchForFood() {
        if (searcher.isInVicinity(territoryCenter, Carcass.class, 3)) {
            Location l = Searcher.searchForObject(Carcass.class, territoryCenter, 3);
            setTarget(l);
        }

        if (searcher.isInVicinity(territoryCenter, Wolf.class, 3)) {
            Location l = Searcher.searchForObject(Wolf.class, territoryCenter, 3);
            setTarget(l);
        }

        if (searcher.isInVicinity(territoryCenter, Rabbit.class, 3)) {
            Location l = Searcher.searchForObject(Rabbit.class, territoryCenter, 3);
            setTarget(l);
        }

        if (searcher.isInVicinity(territoryCenter, Carcass.class, 3)) {
            Location l = Searcher.searchForObject(Carcass.class, territoryCenter, 3);
            setTarget(l);
        }

        if (searcher.isInVicinity(territoryCenter, BerryBush.class, 3)) {
            Location l = Searcher.searchForObject(BerryBush.class, territoryCenter, 3);
            BerryBush berryBush = (BerryBush) world.getTile(l);
            if (berryBush.getBerries()) {
                setTarget(l);
            }
        }
    }

    @Override
    public DisplayInformation getInformation() {
        if (hasGrown) {
            if (world.isDay()) {
                return new DisplayInformation(Color.magenta, "bear");
            } else {
                return new DisplayInformation(Color.yellow, "bear-sleeping");
            }
        } else {
            if (world.isDay()) {
                return new DisplayInformation(Color.orange, "bear-small");
            } else {
                return new DisplayInformation(Color.green, "bear-small-sleeping");
            }
        }
    }
}