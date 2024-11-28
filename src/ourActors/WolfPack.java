package ourActors;

import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.RandomLocationHelper;
import methodHelpers.Searcher;

import java.util.*;

public class WolfPack {
    private int wolfAmount;
    private List<Wolf> wolves;
    private int wolfPackID;
    private World world;
    private Location spawnLocation;
    private Random r;
    RandomLocationHelper randomLocationHelper;

    public WolfPack(World world, int wolfAmount, Location spawnLocation) {
        wolves = new ArrayList<Wolf>();
        this.wolfAmount = wolfAmount;
        this.world = world;
        wolfPackID = hashCode();
        this.spawnLocation = spawnLocation;
        r = new Random();
        RandomLocationHelper randomLocationHelper = new RandomLocationHelper(world);

        spawnWolfPack();
    }

    private void spawnWolfPack() {
        Wolf packLeader = new Wolf(world, this, wolfPackID, true);
        wolves.add(packLeader);

        world.setTile(spawnLocation, packLeader);
        for (int i = 0; i < wolfAmount - 1; i++) {
            spawnWolvesAroundPackLeader();
        }
    }

    private void spawnWolvesAroundPackLeader() {
        List<Location> spawnPoints = new ArrayList<>(RandomLocationHelper.getMoreEmptySurroundingTiles(spawnLocation, 1));

        world.setTile(spawnPoints.get(r.nextInt(spawnPoints.size())), new Wolf(world, this, wolfPackID));
    }


    public Wolf getPackLeader() {
        return wolves.get(0);
    }
}
