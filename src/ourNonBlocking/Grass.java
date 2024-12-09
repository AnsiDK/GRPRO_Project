package ourNonBlocking;

import itumulator.world.*;
import Main.Main;

import java.util.*;

public class Grass extends Foliage {

    /**
     * Constructor for our grass
     * @param world provides information regarding the world where the grass is
     */
    public Grass (World world) {
        super(world);
        lifeTime = 40;
    }

    /**
     * An overridden method that determines how grass should spread
     * @param location provides the location from which the grass spreads from
     */
    @Override
    protected void spread(Location location) {
        if (r.nextInt(3) < 1 && Main.getNonBlockingObjects() != (Main.getSize() * Main.getSize())) {

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
