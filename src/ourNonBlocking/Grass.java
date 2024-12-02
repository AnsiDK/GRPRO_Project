package ourNonBlocking;

import itumulator.world.*;
import Main.Main;

import java.util.*;


public class Grass extends Foliage {
    public Grass (World world) {
        super(world);
    }

    protected void spread(Location location) {
        if (r.nextInt(2) == 0 && Main.getNonBlockingObjects() != (Main.getSize() * Main.getSize())) {

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
            }
        }
    }
}
