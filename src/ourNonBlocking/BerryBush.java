package ourNonBlocking;

import Main.Main;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import methodHelpers.DisplayProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BerryBush extends Foliage implements DynamicDisplayInformationProvider {
    DisplayProvider displayChanger;
    protected boolean hasBerries = false;

    public BerryBush(World world) {
        super(world);
    }

    @Override
    public void act(World world) {
        grow();
        super.act(world);
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

                world.setTile(l, new BerryBush(world));
            }
        }
    }

    protected void grow() {
        if (hasBerries == false) {
            if (r.nextInt(5) == 0) {
                hasBerries = true;
            }
        }
    }

    public boolean getBerries() {
        return hasBerries;
    }

    public void eatBerry() {
        hasBerries = false;
    }

    @Override
    public DisplayInformation getInformation() {
        if (hasBerries) {
            return new DisplayInformation(Color.red, "bush-berries");
        } else {
            return new DisplayInformation(Color.blue, "bush");
        }
    }
}
