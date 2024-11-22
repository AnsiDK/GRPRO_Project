package itumulator.world;

import itumulator.simulator.Rabbit;

import java.util.*;

public class RabbitHole implements NonBlocking {
    Location location;
    List<Rabbit> rabbits;
    boolean rabbitExiting;

    public RabbitHole(Location location) {
        super();
        this.location = location;
        rabbits = new ArrayList<>();
    }

    public Location getLocation() {
        return location;
    }

    public void addRabbit(Rabbit rabbit) {
        rabbits.add(rabbit);

        //Add code here that makes a different entrance if there is above a certain number of rabbits in a given hole
    }

    public void removeRabbit(Rabbit rabbit) {
        rabbits.remove(rabbit);
    }

    public boolean isFirst(Rabbit rabbit) {
        return rabbits.get(0) == rabbit;
    }
}
