package itumulator.world;

import java.util.*;
import itumulator.simulator.Animal;
import itumulator.simulator.Rabbit;
import itumulator.world.Location;

public class Home implements NonBlocking {
    private List<Animal> animals;
    private Location location;

    public Home() {
        super();
        animals = new ArrayList<>();
    }

    //Getter for the location of the home
    public Location getLocation() {
        return location;
    }

    //Adds animal to home
    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    //Remove an animal from home
    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    //Checker to see if a rabbit is first
    public boolean isFirst(Animal animal) {
        return animals.getFirst() == animal;
    }


}
