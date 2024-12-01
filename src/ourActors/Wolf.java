package ourActors;

import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.Searcher;
import methodHelpers.TimeManager;
import ourNonBlocking.RabbitHole;
import ourNonBlocking.WolfDen;

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
        //timeManager.updateTime(true);

        if (isOnMap) {
            Location l = world.getCurrentLocation();
            Location rabbit = searcher.searchForObject(Rabbit.class, l, 3);
            if(rabbit != null) {
                setTarget(rabbit);
            }


            //I do not know how this piece of shit code is ever accssesed while the wolf is not on the map...
            try {
                if (!isPackLeader && distFromPackLeader() > 3 && foodEaten != 0) {
                    Location packLeaderLocation = world.getLocation(wolfPack.getPackLeader());
                    setTarget(packLeaderLocation);
                }
            } catch (IllegalArgumentException e) {
                //well...
            }

            if (target != null) {
                goTowardsTarget();
            } else {
                moveRandomly();
            }

        } else { leaveHome(); }
    }

    void actNight() {
        timeManager.updateTime(false);

        if (isOnMap) {
            if (target == null) { findHome(); }

            if (target != null) {
                goTowardsTarget();
            } else {
                moveRandomly();
                System.out.println("Chose to walk around randomly while home = " + home);
            }
        }
    }

    @Override
    void performAction() {
        if (world.getTile(target) instanceof Rabbit) {
            Rabbit r = (Rabbit) world.getTile(target);
            eatRabbit(r);
        }

        if (world.getTile(target) instanceof WolfDen) {
            enterHome();
        }
    }

    @Override
    public void findHome() {
        if (home == null) {
            buildHome();
            setTarget(home.getLocation());
        } else {
            setTarget(home.getLocation());
        }
    }

    @Override
    void buildHome() {
        Location l = world.getLocation(this);

        if (searcher.grassAt(l)) {
            world.delete(world.getNonBlocking(l));
        }

        WolfDen wolfDen = new WolfDen(world);
        world.setTile(l, wolfDen);
        wolfPack.buildDen(wolfDen);
    }


    @Override
    public void reproduce() {

    }

    private void eatRabbit(Rabbit eatenRabbit) {
        eatenRabbit.setIsOnMap(false);
        world.delete(eatenRabbit);
        foodEaten++;
    }

    private double distFromPackLeader() {
        Location l = world.getLocation(wolfPack.getPackLeader());
        Location l2 = world.getCurrentLocation();

        if (l == null) {
            l = home.getLocation();
        }

        if (l2 == null) {
            l2 = home.getLocation();
        }

        int x = l.getX() - l2.getX();
        int y = l.getY() - l2.getY();

        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public void setHome(WolfDen home) {
        this.home = home;
    }
}
