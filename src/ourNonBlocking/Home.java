package ourNonBlocking;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import methodHelpers.RandomLocationHelper;
import ourActors.*;

import java.util.ArrayList;
import java.util.Random;

public abstract class Home implements NonBlocking {
    protected Location location;
    protected ArrayList<Animal> animals;
    protected World world;

    public Home(World world) {
        super();
        animals = new ArrayList<>();
        this.world = world;
        location = world.getCurrentLocation();
    }

    //Getter for the location of the home
    public Location getLocation() {
        return location;
    }

    //Adds animal to the home
    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    //Checker to see if an animal is first
    public boolean isFirst(Animal animal) {
        return animals.getFirst() == animal;
    }

    //Checker to see if more than 2 grown animals share the same hole
    public boolean hasGrownAnimals() {
        int grownCount = 0;

        for (Animal animal : animals) {
            if (animal.hasGrown()) {
                grownCount++;
            }
        }

        return grownCount >= 2;
    }
}
