package itumulator.simulator;

import Main.Main;
import itumulator.world.RabbitHole;
import itumulator.world.World;
import itumulator.world.Location;


import java.util.*;

public class Rabbit implements Actor{

    int age;
    int stepsLived;
    int eatenGrass;
    int energy;                         //We are missing an actual use for energy
    public RabbitHole hole;
    Random random = new Random();
    World world;
    Boolean isOnMap = true;

    public Rabbit() {
        super();
        age = 0; // 0 years old
        eatenGrass = 0;
        stepsLived = 0;
        energy = 100;
    }

    //Overloaded Rabbit function for small rabbits so that they come "attached" to a hole
    public Rabbit(RabbitHole hole) {
        super();
        age = 0; // 0 years old
        eatenGrass = 0;
        stepsLived = 0;
        energy = 100;
        this.hole = hole;
        this.isOnMap = false;
    }

    @Override
    public void act(World world) {
        if (world.isDay() && !isOnMap) {
            if (eatenGrass > 2) {
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
                    isOnMap = false;
                    hole = null;
                    world.delete(this);
                }

                if (hole == null) {
                    //Mega fed kode der skifter kanines sprite til en der sover
                }

                //Ellers går kaninen mod sit hul
                else {
                    goTowardsHole(world);
                }


            } else {
                think(world);
            }
        }

        //Rabbit has lived 1 day
        if (stepsLived % 20 == 0) {
            age ++;

            if (age == 2) {
                grow();
            }
            energy -= 5;
            eatenGrass = 0;
        }
    }

    void think(World world) {
        //Make the rabbit less and less likely to eat grass
        if (world.getNonBlocking(world.getLocation(this)) instanceof Grass && eatenGrass == random.nextInt(eatenGrass) + 1) {
            eatGrass(world);
        } else if (hole == null && eatenGrass > 1) {
            //The rabbit will dig a hole if it has eaten more than 3 grass in one day
            digHole(world);
        } else {
            move(world);
        }
    }

    void eatGrass(World world) {
        Main.setGrass(Main.getGrass() - 1);
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

    void goTowardsHole(World world) {
        Location start = world.getLocation(this);
        Location end = hole.getLocation();
        Location currentBest = start;

        //Find initial distance
        double initDist = Math.sqrt(Math.pow(end.getX() - start.getX(), 2)
                       + Math.pow(end.getY() - start.getY(), 2));

        //If distance is too great, make a new hole
        if (initDist > 5) {
            digHole(world);
        }

        //Determine the best square for a rabbit to move to
        Set<Location> Set = world.getEmptySurroundingTiles();
        for (Location loc : Set) {

            //Find distance for a given location
            double distance = Math.sqrt(Math.pow(end.getX() - loc.getX(), 2)
                            + Math.pow(end.getY() - loc.getY(), 2));

            //A closer location has been found
            if (distance < initDist) {
                currentBest = loc;
            }
        }

        //Move closer to the hole
        world.move(this, currentBest);

        //If the rabbit is at the hole
        if (start.getX() == end.getX() && start.getY() == end.getY()) {
            isOnMap = false;
            hole.addRabbit(this);
            world.remove(this);
        }
    }

    //Dig a hole at rabbit's location
    public void digHole(World world) {
        //Delete whatever is in the way
        if (world.getNonBlocking(world.getLocation(this)) instanceof Grass ||
                world.getNonBlocking(world.getLocation(this)) instanceof RabbitHole) {

            world.delete(world.getNonBlocking(world.getLocation(this)));
        }

        RabbitHole rabbitHole = new RabbitHole(world.getLocation(this));
        world.setTile(world.getLocation(this), rabbitHole);
        this.hole = rabbitHole;
    }

    public void exitHole(World world) {
        if (hole.isFirst(this) && world.isTileEmpty(world.getLocation(hole))) {
            hole.removeRabbit(this);
            isOnMap = true;
            world.setTile(hole.getLocation(), this);
        }
    }

    void reproduce(World world) {
        System.out.println("Rabbit reproduced");
        Rabbit babyRabbit = new Rabbit(hole);
        world.add(babyRabbit);
        hole.addRabbit(babyRabbit);
    }

    void grow() {
        System.out.println("Rabbit has grown");
        //Mega fed kode der gør at kaninen skifter sprite
    }
}