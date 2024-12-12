package ourNonBlocking;

import Main.Main;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BerryBush extends Foliage implements DynamicDisplayInformationProvider {
    protected boolean hasBerries = false;

    /**
     * Constructor for our berryBushes
     * @param world provides information regarding the world where berry bushes live
     */
    public BerryBush(World world) {
        super(world);
        lifeTime = 35;
    }

    /**
     *
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {
        grow();
        super.act(world);
    }

    /**
     * An overridden method that determines how our berry bushes should spread
     * @param location provides the location from where berry bushes should spread
     */
    @Override
    protected void spread(Location location) {
        if (r.nextInt( 15) == 0 && Main.getNonBlockingObjects() != (Main.getSize() * Main.getSize())) {

            Set<Location> set = world.getSurroundingTiles();
            List<Location> list = new ArrayList<>(set);

            int i = 0;
            for (Location l : list) {
                if (!world.containsNonBlocking(l)) {
                    break;
                }

                if (world.getNonBlocking(l) instanceof Grass) {
                    if (r.nextInt(3) == 0) {
                        world.delete(world.getNonBlocking(l));
                        break;
                    }
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

    /**
     * A method that grows berries on our berry bushes
     */
    public void grow() {
        if (hasBerries == false) {
            if (r.nextInt(5) == 0) {
                hasBerries = true;
                lifeTime -= 10;
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