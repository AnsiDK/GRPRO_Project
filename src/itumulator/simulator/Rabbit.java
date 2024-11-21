package itumulator.simulator;

import itumulator.world.RabbitHole;
import itumulator.world.World;
import itumulator.world.Location;


import java.util.*;

public class Rabbit implements Actor{

    int age;
    int stepsLived;
    boolean eatenGrass;
    int energy;
    //RabbitHole hole;

    Rabbit() {
        super();
        age = 0; // 0 years old
        eatenGrass = false;
        stepsLived = 0;
        energy = 100;
    }

    @Override
    public void act(World world) {
        // age rabbit
        stepsLived++;

        // Make the rabbit eat if it is on grass and hasn't eaten
        if (world.getNonBlocking(world.getLocation(this)) == Grass.class && !eatenGrass) {
            eatenGrass = true;
            world.delete(world.getNonBlocking(world.getLocation(this)));
        }

        // Remove rabbit when it turns night
        if (world.isNight()) {
            world.remove(this);

            // If rabbit haven't eaten then it dies
            if (!eatenGrass) {
                world.delete(this);
            }
        }

        // ny random lokation:
        Set<Location> freeTiles = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(freeTiles);
        Random random = new Random();
        int r = random.nextInt(list.size());
        Location loc = list.get(r);

        world.move(this,loc);

        if (stepsLived % 20 == 0) {
            age ++;
            energy -= 5;
        }

        if (age == 2) {
            // Rabbit bliver stor
        }

    }
}