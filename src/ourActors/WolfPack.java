package ourActors;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.RandomLocationHelper;
import ourNonBlocking.WolfDen;

import java.awt.*;
import java.util.*;
import java.util.List;

public class WolfPack {
    private int wolfAmount;
    private List<Wolf> wolves;
    private int wolfPackID;
    private World world;
    private Location spawnLocation;
    private Random r;
    RandomLocationHelper randomLocationHelper;
    protected WolfDen wolfDen;

    /**
     * A constructor for our wolf pack, this is used to make sure all wolves in a pack is automaticly put on the map together
     * @param world provides information about the world that we have to place stuff in
     * @param wolfAmount provides the amount of wolves that the wolfpack has to spawn
     * @param spawnLocation provides the location where the leader wolf should be spawned
     */
    public WolfPack(World world, int wolfAmount, Location spawnLocation) {
        wolves = new ArrayList<Wolf>();
        this.wolfAmount = wolfAmount;
        this.world = world;
        wolfPackID = hashCode();
        this.spawnLocation = spawnLocation;
        r = new Random();
        randomLocationHelper = new RandomLocationHelper(world);

        spawnWolfPack();
    }

    /**
     * A method that spawns a single packleader, then calls "spawnWolvesAroundPackLeader"
     */
    private void spawnWolfPack() {
        Wolf packLeader = new Wolf(world, this, wolfPackID, true);
        wolves.add(packLeader);

        world.setTile(spawnLocation, packLeader);
        for (int i = 0; i < wolfAmount - 1; i++) {
            spawnWolvesAroundPackLeader();
        }
    }

    /**
     * A method that uses on of our helperclasses, to make sure it's able to put all wolves on the map, unless of course there is more wolves than size squared.
     */
    private void spawnWolvesAroundPackLeader() {
        List<Location> spawnPoints = new ArrayList<>(RandomLocationHelper.getMoreEmptySurroundingTiles(spawnLocation, 1));
        Wolf newWolf = new Wolf(world, this, wolfPackID);
        wolves.add(newWolf);
        world.setTile(spawnPoints.get(r.nextInt(spawnPoints.size())), newWolf);
    }

    public Wolf getPackLeader() {
        return wolves.get(0);
    }

    /**
     * A method that ensures that whenever a wolf den is built it is assigned to all wolves in the pack.
     * @param wolfDen provides the wolfDen that is to be assigned to the rest of the wolfPack
     */
    public void buildDen(WolfDen wolfDen) {
        this.wolfDen = wolfDen;
        for (Wolf w : wolves) {
            w.setHome(wolfDen);
        }
    }
}
