package ourNonBlocking;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.Set;

public class MushRoom extends Foliage {
    private int energy;

    public MushRoom(World world, int energy) {
        super(world);
    }

    @Override
    public void act(World world) {
        super.act(world);
        energy--;

        if (energy < 1) {
            dissapear();
        }
    }

    @Override
    protected void spread(Location location) {
        Set<Location> set = world.getSurroundingTiles();
        for (Location l : set) {
            if (world.getNonBlocking(l) instanceof Carcass) {
                ((Carcass) world.getNonBlocking(l)).growShrooms();
            }
        }
    }

    public void dissapear() {
        world.delete(this);
        world.setTile(world.getLocation(this), new Grass(world));
    }
}
