package ourActors;


import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.Searcher;
import ourNonBlocking.BerryBush;
import ourNonBlocking.Carcass;
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

        if (searcher.getDistance(world.getCurrentLocation(), territoryCenter) > 4 && foodEaten != 0) {
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
        if (world.getTile(target) instanceof Animal && !(world.getTile(target) instanceof Bear)) {
            Animal animal = (Animal) world.getTile(target);
            animal.die();
            foodEaten++;
            energyOfAnimal += 2;
        }

        else if (world.getNonBlocking(target) instanceof BerryBush) {
            BerryBush berryBush = (BerryBush) world.getNonBlocking(target);
            if (berryBush.getBerries()) {
                berryBush.eatBerry();
                foodEaten++;
                energyOfAnimal += 2;
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

    void serchForFood() {
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
}
