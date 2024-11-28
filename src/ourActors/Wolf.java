package ourActors;

import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.Searcher;
import methodHelpers.TimeManager;

import java.util.Random;

public class Wolf extends Animal {
    private int wolfPackID;
    private boolean isPackLeader;
    private WolfPack wolfPack;

    public Wolf(World world, WolfPack wolfPack, int wolfPackID) {
        super(world);
        searcher = new Searcher(world);
        timeManager = new TimeManager(this);
        r = new Random();
        this.wolfPackID = wolfPackID;
        this.wolfPack = wolfPack;
    }

    //Overloaded constructor for a pack leader
    public Wolf(World world, WolfPack wolfPack, int wolfPackID, boolean isPackLeader) {
        super(world);
        searcher = new Searcher(world);
        timeManager = new TimeManager(this);
        r = new Random();
        this.wolfPackID = wolfPackID;
        this.wolfPack = wolfPack;
        this.isPackLeader = isPackLeader;
    }

    @Override
    public void actDay() {
        timeManager.updateTime(true);

        if (isOnMap) {
            Location l = world.getCurrentLocation();
            Location rabbit = Searcher.searchForObject(Rabbit.class, l, 3);
            if(rabbit != null) {
                setTarget(rabbit);
            }

            if (!isPackLeader && distFromPackLeader() > 3) {
                Location packLeaderLocation = world.getLocation(wolfPack.getPackLeader());
                setTarget(packLeaderLocation);
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
        //timeManager.updateTime(false);

        if (isOnMap) {
            //if (!(world.getNonBlocking(target) instanceof WolfDen)) { findHome(); }
            //goTowardsTarget();
        }
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

    private double distFromPackLeader() {
        Location l = world.getLocation(wolfPack.getPackLeader());
        int x = l.getX() - world.getCurrentLocation().getX();
        int y = l.getY() - world.getCurrentLocation().getY();

        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
}
