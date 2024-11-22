package itumulator.simulator;

import Main.Main;
import itumulator.world.RabbitHole;
import itumulator.world.World;
import itumulator.world.Location;


import java.util.*;

public class Rabbit implements Actor{

    private static final int maxHoleDistance = 5;
    private static final int energyDecay = 2;
    private static final int ageToGrow = 2;
    private static final int stepsPerDay = 20;
    private static final int grassNeededToDigHole = 3;
    private static final int initialEnergy = 10;

    private int ageStage;
    private int stepsLived;
    private int eatenGrass;
    private int energy;
    private RabbitHole hole;
    private Boolean isOnMap = true;
    private final Random random = new Random();
    private Location target;
    private int targetX;
    private int targetY;

    public Rabbit() {
        super();
        initializeRabbit(null, true);
    }

    //Overloaded Rabbit function for small rabbits so that they come "attached" to a hole
    public Rabbit(RabbitHole hole) {
        super();
        initializeRabbit(hole, false);
    }

    private void initializeRabbit(RabbitHole hole, boolean isOnMap) {
        ageStage = 0; // 0 years old
        eatenGrass = 0;
        stepsLived = 0;
        energy = initialEnergy;
        this.hole = hole;
        this.isOnMap = isOnMap;
    }

    @Override
    public void act(World world) {
        if (world.isDay() && !isOnMap) {
            if (eatenGrass > 2 && hole.rabbitsInHole() >= 2) {
                reproduce(world);
            }
            exitHole(world);
        } else if (isOnMap) {

            // age rabbit
            stepsLived++;

            // Make the rabbit eat if it is on grass and hasn't eaten
            if (world.getNonBlocking(world.getLocation(this)) instanceof Grass && eatenGrass == 0) {
                eatGrass(world);
            }

            // The rabbit goes towards it's hole when it turns night
            if (world.isNight()) {
                // If rabbit haven't eaten then it dies
                if (eatenGrass == 0) {
                    System.out.println("Rabbit died of hunger");
                    hole = null;
                    world.delete(this);
                }

                if (hole == null) {
                    //Mega fed kode der skifter kanines sprite til en der sover
                }

                //Ellers går kaninen mod sit hul
                else {
                    setTarget(world.getLocation(hole));
                    goToTarget(world);
                }

            } else {
                think(world);
            }
        }

        //Rabbit has lived 1 day
        if (stepsLived % stepsPerDay == 0) {
            ageStage++;

            if (ageStage == ageToGrow) {
                grow(world);
            }
            eatenGrass = 0;
        }
    }

    void think(World world) {
        //Make the rabbit less and less likely to eat grass
        if (world.getNonBlocking(world.getLocation(this)) instanceof Grass && eatenGrass == random.nextInt(eatenGrass) + 1) {
            eatGrass(world);
        } else if (hole == null && eatenGrass > grassNeededToDigHole) {
            //The rabbit will dig a hole if it has eaten more than 3 grass in one day
            digHole(world);
            eatenGrass =- 2;
        } else {
            move(world);
        }
    }

    void eatGrass(World world) {
        Main.setNonBlockingObjects(Main.getNonBlockingObjects() - 1);
        eatenGrass ++;
        world.delete(world.getNonBlocking(world.getLocation(this)));
    }

    void move(World world) {
        Set<Location> freeTiles = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(freeTiles);
        if (!list.isEmpty()) {
            Location loc = list.get(random.nextInt(list.size()));

            world.move(this, loc);

            //A rabbit "claims" a rabbit hole if it walks over one and it does'nt already have one
            if (world.getNonBlocking(loc) instanceof RabbitHole && hole == null) {
                this.hole = (RabbitHole) world.getNonBlocking(loc);
            }
        }
    }

    void goToTarget(World world) {
        Location start = world.getLocation(this);
        Location currentBest = start;

        //Find initial distance
        double initDist = Math.sqrt(Math.pow(targetX - start.getX(), 2)
                       + Math.pow(targetY - start.getY(), 2));

        //Determine the best square for a rabbit to move to
        Set<Location> Set = world.getEmptySurroundingTiles();
        for (Location loc : Set) {

            //Find distance for a given location
            double distance = Math.sqrt(Math.pow(targetX - loc.getX(), 2)
                            + Math.pow(targetY - loc.getY(), 2));

            //A closer location has been found
            if (distance < initDist) {
                currentBest = loc;
            }
        }

        //Move closer to the hole
        world.move(this, currentBest);

        //If the rabbit is at the target
        if (start.getX() == targetX && start.getY() == targetY) {
            performAction(world);
        }
    }

    //Dig a hole at rabbit's location
    public void digHole(World world) {
        //Delete whatever is in the way
        if (world.getNonBlocking(world.getLocation(this)) instanceof Grass ||
                world.getNonBlocking(world.getLocation(this)) instanceof RabbitHole) {

            world.delete(world.getNonBlocking(world.getLocation(this)));
        }

        RabbitHole rabbitHole = new RabbitHole();
        world.setTile(world.getLocation(this), rabbitHole);
        this.hole = rabbitHole;
    }

    public void exitHole(World world) {
        if (hole.isFirst(this) && world.isTileEmpty(world.getLocation(hole))) {
            hole.removeRabbit(this);
            isOnMap = true;
            world.setTile(hole.getLocation(), this);
            if (hole.rabbitsInHole() > 5) {
                hole = null;
            }
        }
    }

    void reproduce(World world) {
        System.out.println("Rabbit reproduced");
        Rabbit babyRabbit = new Rabbit(hole);
        world.add(babyRabbit);
        hole.addRabbit(babyRabbit);
    }

    void grow(World world) {
        energy -= energyDecay;
        if (energy == 2) {
            world.delete(this);
        }

        //Mega fed kode der gør at kaninen skifter sprite
    }

    void setTarget(Location location) {
        target = location;
        targetX = location.getX();
        targetY = location.getY();
    }

    void performAction(World world) {
        Object o = world.getTile(target);
        if (o instanceof RabbitHole) {
            System.out.println("Reached hole");
            isOnMap = false;
            hole.addRabbit(this);
            world.remove(this);
        }
    }

    boolean ableToMove() {
        return (random.nextInt(energy) != 0);
    }
}