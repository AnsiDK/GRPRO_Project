package itumulator.world;

import Main.Main;
import itumulator.simulator.Grass;
import itumulator.simulator.Rabbit;
import methodHelpers.RandomLocationHelper;
import java.util.*;
import itumulator.world.*;

public class RabbitHole implements NonBlocking {
    Location location;
    List<Rabbit> rabbits;
    RabbitHole otherExit;
    Random r = new Random();
    RandomLocationHelper rLoc;
    World world;

    public RabbitHole(World world) {
        super();
        rabbits = new ArrayList<>();
        rLoc = new RandomLocationHelper(world);
        this.world = world;
    }

    //Getter for the location of the hole
    public Location getLocation() {
        return location;
    }

    //Adds rabbit to the hole
    public void addRabbit(Rabbit rabbit) {
        rabbits.add(rabbit);
    }

    //Removes rabbit from the hole
    public void removeRabbit(Rabbit rabbit) {
        if (rabbits.size() > 6) {
            splitRabbits();
        }
        rabbits.remove(rabbit);
    }

    //Checker to see if a rabbit is first
    public boolean isFirst(Rabbit rabbit) {
        return rabbits.getFirst() == rabbit;
    }

    public void splitRabbits() {
        Location newHoleLocation = rLoc.getRandomNonRabbitHoleLocation();
        if (world.getNonBlocking(newHoleLocation) instanceof Grass) {
            world.delete(world.getNonBlocking(newHoleLocation));
        } else {
            Main.setNonBlockingObjects(Main.getNonBlockingObjects()+1);
        }
        System.out.println("Setting new rabbitHole at: " + newHoleLocation + " replacing " + world.getNonBlocking(newHoleLocation));

        RabbitHole newHole = new RabbitHole(world);
        world.setTile(newHoleLocation, newHole);

        List<Rabbit> excessRabbits = new ArrayList<>(rabbits.subList(5, rabbits.size()));
        for (Rabbit rabbit : excessRabbits) {
            rabbit.hole = newHole;
            newHole.addRabbit(rabbit);
        }
        rabbits.removeAll(excessRabbits);
    }

    //Checker to see if more than 2 grown rabbits share the same hole
    public boolean hasGrownRabbits() {
        int grownCount = 0;

        for (Rabbit rabbit : rabbits) {
            if (rabbit.hasGrown()) {
                grownCount++;
            }
        }

        return grownCount >= 2;
    }
}
