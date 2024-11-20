package itumulator.simulator;

import itumulator.world.*;
import Main.Main;
import java.util.*;


public class Grass implements NonBlocking, Actor {

    World world;
    Random r = new Random();

    public Grass (World world) {
        super();
        this.world = world;
    }

    @Override
    public void act (World world) {
        if (Main.getGrass() == Main.getSize() * Main.getSize()) {
            System.out.println("There is no room for more grass");
        }
        if (r.nextInt(10) == 9 && Main.getGrass() != (Main.getSize() * Main.getSize())) {
            spread(world.getCurrentLocation());
        }
    }

    public void spread(Location location) {
        Set<Location> set = world.getSurroundingTiles();
        List<Location> list = new ArrayList<>(set);

        int i = 0;
        for (Location l : list) {
            if (!world.containsNonBlocking(l)) {
                break;
            }
            i++;
        }

        if (i != list.size()) {
            Location l = list.get(r.nextInt(list.size()));

            while (world.containsNonBlocking(l)) {
                l = list.get(r.nextInt(list.size()));
            }

            world.setTile(l, new Grass(world));
            int grass = Main.getGrass();
            Main.setGrass(grass + 1);
        }
    }
}
