package ourNonBlocking;

import Main.Main;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import ourActors.*;

import java.util.ArrayList;

public abstract class Home implements NonBlocking {
    protected Location location;
    protected ArrayList<Animal> animals;
    protected World world;

    /**
     * Constructor for the home class
     * @param world provides information regarding the world where the home is located
     */
    public Home(World world) {
        super();
        animals = new ArrayList<>();
        this.world = world;
        location = world.getCurrentLocation();
        Main.setNonBlockingObjects(Main.getNonBlockingObjects()+1);
    }

    public Location getLocation() {
        return location;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    /**
     * Method that checks whether an animal is first in line to exit their home
     * @param animal provides the animal in question
     * @return a boolean that depends on whether the animal is first in line or not
     */
    public boolean isFirst(Animal animal) {
        if (animals.size() == 0) {
            return true;
        }
        return animals.getFirst() == animal;
    }

    /**
     * Method that ensures a home contains at least 2 grown animals
     * @return a boolean that depends on if there are 2 or more grown animals in a given home
     */
    public boolean hasGrownAnimals() {
        int grownCount = 0;

        for (Animal animal : animals) {
            if (animal.getHasGrown()) {
                grownCount++;
            }
        }

        return grownCount >= 2;
    }
}
