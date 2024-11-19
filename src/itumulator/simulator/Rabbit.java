package itumulator.simulator;

import itumulator.world.World;
import itumulator.world.Location;

import java.util.*;

public class Rabbit implements Actor{

    World world;
    int age;
    int stepsLived;
    boolean hole;

    Rabbit(int age, boolean hole) {
        super();
        this.age = 0; // 0 years old
        this.hole = false; // no nest
    }

    @Override
    public void act(World world) {

        Set<Location> freeTiles = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(freeTiles);
        Random random = new Random();
        int r = random.nextInt(list.size());
        Location loc = list.get(r);

        world.move(this,loc);

    }


}
