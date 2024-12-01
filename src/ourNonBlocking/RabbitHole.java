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

    public RabbitHole(World world) {
        super(world);
        r = new Random();
        rLoc = new RandomLocationHelper(world);
    }

    //Removes rabbit from the hole
    @Override
    public void removeAnimal(Animal rabbit) {
        if (animals.size() > 6) {
            splitRabbits();
        }
        animals.remove(rabbit);
    }

    public void splitRabbits() {
        Location newHoleLocation = rLoc.getRandomNonRabbitHoleLocation();
        if (world.getNonBlocking(newHoleLocation) instanceof Grass) {
            world.delete(world.getNonBlocking(newHoleLocation));
        } else {
            Main.setNonBlockingObjects(Main.getNonBlockingObjects()+1);
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
