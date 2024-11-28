package ourActors;

import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.Searcher;
import methodHelpers.TimeManager;

import java.util.Random;

public class Wolf extends Animal {

    public Wolf(World world) {
        super(world);
        searcher = new Searcher(world);
        timeManager = new TimeManager(this);
        r = new Random();
    }

    @Override
    public void actDay() {
        if (isOnMap) {
            Location l = world.getCurrentLocation();
            Location rabbit = Searcher.searchForObject(Rabbit.class, l, 3);
            if(rabbit != null) {
                setTarget(rabbit);
            }

            if (target != null) {
                goTowardsTarget();
            } else {
                moveRandomly();
            }

        } else { leaveHome(); }
    }

    @Override
    void actNight() {
        findHome();
    }

    @Override
    void performAction() {
        if (world.getTile(target) instanceof Rabbit) {
            Rabbit r = (Rabbit) world.getTile(target);
            eatRabbit(r);
        }

        /*
        if (world.getTile(target) instanceof WolfDen) {

        }

         */
    }

    @Override
    public void findHome() {

    }

    @Override
    void buildHome() {

    }

    @Override
    void enterHome() {

    }

    @Override
    void leaveHome() {

    }

    @Override
    public void reproduce() {

    }

    private void eatRabbit(Rabbit eatenRabbit) {
        eatenRabbit.setIsOnMap(false);
        world.delete(eatenRabbit);
        foodEaten++;

        System.out.println("I have eaten " + foodEaten + " rabbits");
    }
}
