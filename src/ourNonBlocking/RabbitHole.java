package ourNonBlocking;

import Main.Main;
import ourActors.Animal;
import ourActors.Rabbit;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import methodHelpers.RandomLocationHelper;
import java.util.*;

public class RabbitHole extends Home {
    Random r;
    private RandomLocationHelper rLoc;

    /**
     * A constructor for the RabbitHole class, this also initializes the randomLocationHelper as well as java's built in random
     * @param world provides information regarding the world
     */
    public RabbitHole(World world) {
        super(world);
        r = new Random();
        rLoc = new RandomLocationHelper(world);
    }

    /**
     * A method that removes rabbits from their hole, if there are too many rabbits in the holw when this happens, they will be split.
     * @param rabbit provides the animal which should be removed from the hole
     */
    @Override
    public void removeAnimal(Animal rabbit) {
        if (animals.size() > 6) {
            splitRabbits();
        }
        animals.remove(rabbit);
    }

    /**
     * A method that handles the case where there are 7 or more rabbits in a hole in whic case it will take all index positions from 5 and above and make a new hole on the map for them.
     */
    public void splitRabbits() {
        Location newHoleLocation = rLoc.getNonBlockingLocation(world.getSize());
        Object object = world.getNonBlocking(newHoleLocation);

        if (object instanceof Foliage || object instanceof Carcass) {
            world.delete(object);
        }

        RabbitHole newHole = new RabbitHole(world);
        world.setTile(newHoleLocation, newHole);

        ArrayList<Animal> excessRabbits = new ArrayList<>(animals.subList(5, animals.size()));
        for (Animal rabbit : excessRabbits) {
            rabbit.home = newHole;
            newHole.addAnimal(rabbit);
        }
        animals.removeAll(excessRabbits);
    }
}
