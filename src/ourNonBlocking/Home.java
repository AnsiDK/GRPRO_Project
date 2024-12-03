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

    public Home(World world) {
        super();
        animals = new ArrayList<>();
        this.world = world;
        location = world.getCurrentLocation();
        Main.setNonBlockingObjects(Main.getNonBlockingObjects()+1);
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
            if (animal.getHasGrown()) {
                grownCount++;
            }
        }

        return grownCount >= 2;
    }
}
