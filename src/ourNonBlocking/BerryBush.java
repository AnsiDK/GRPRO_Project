package ourNonBlocking;

import Main.Main;
import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.DisplayChanger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BerryBush extends Foliage {
    DisplayChanger displayChanger;

    public BerryBush(World world) {
        super(world);
    }

    protected void spread(Location location) {
        if (r.nextInt( 10) == 0 && Main.getNonBlockingObjects() != (Main.getSize() * Main.getSize())) {

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
                int grass = Main.getNonBlockingObjects();
                Main.setNonBlockingObjects(grass + 1);
            }
        }
    }

}
