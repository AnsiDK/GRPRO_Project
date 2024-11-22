package itumulator.world;

import itumulator.simulator.Rabbit;

import java.util.*;

public class RabbitHole implements NonBlocking {
    Location location;
    List<Rabbit> rabbits;
    RabbitHole otherExit;
    Random r = new Random();

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

        if (rabbits.size() > 4) {
            //Super awesome kode der tilføjer en anden udgang til et hul
        }
    }

    public void removeRabbit(Rabbit rabbit) {
        rabbits.remove(rabbit);
    }

    public boolean isFirst(Rabbit rabbit) {
        return rabbits.get(0) == rabbit;
    }
}
