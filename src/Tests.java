import itumulator.executable.Program;
import itumulator.simulator.Grass;
import itumulator.simulator.Rabbit;
import itumulator.world.World;
import itumulator.world.Location;
import methodHelpers.RandomLocationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static Main.Main.fileImport;
import static Main.Main.spawnStuff;
import static org.junit.jupiter.api.Assertions.*;


public class Tests {
    World w;
    int size;
    Program p;

    @Test
    public void Test11a () {
        CreateWorld("week-1/t1-1a");
        int occupied = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (w.isTileEmpty(new Location(i, j))) {
                    occupied++;
                }
            }
        }
        assertEquals(3, occupied);
    }

    @Test
    public void Test11b () {
        CreateWorld("week-1/t1-1b");

        int occupied = 0;

        for (int i = 0; i < 10; i++) {
            p.simulate();
        }
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                if (w.isTileEmpty(new Location(i, j))) {
                    occupied++;
                }
            }
        }

        assertNotEquals(1, occupied);
    }

    @Test
    public void Test11c () {
        CreateWorld("week-1/t1-1c");
        boolean isOnGrass = false;

        Map<Object, Location> objects = w.getEntities();

        Rabbit rabbit = null;

        for (Object o : objects.keySet()) {
            if (o == Rabbit.class) {
                Location loc = objects.get(o);
                rabbit = (Rabbit) w.getTile(loc);
            }
        }

        for (int i = 0; i < 10; i++) {
            objects = w.getEntities();

            for (Object key : objects.keySet()) {
                if (key == Grass.class) {
                    if(w.getLocation(rabbit) == objects.get(key)) {
                        isOnGrass = true;
                    }
                }
            }
            p.simulate();
        }

        assertTrue(isOnGrass);
    }

    @Test
    public void Test12a () {
        CreateWorld("week-1/t1-2a");

        int occupied = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (w.isTileEmpty(new Location(i, j))) {
                    occupied++;
                }
            }
        }
        assertEquals(3, occupied);
    }

    @Test
    public void Test12b () {
        CreateWorld("week-1/t1-2b");

        boolean isNotDead = true;

        Map<Object, Location> objects = w.getEntities();

        Rabbit rabbit = null;

        for (Object o : objects.keySet()) {
            if (o == Rabbit.class) {
                Location loc = objects.get(o);
                rabbit = (Rabbit) w.getTile(loc);
            }
        }

        // This needs to be implemented...
        //rabbit.kill();

        objects = w.getEntities();

        for (Object key : objects.keySet()) {
            if (key != null) {
                isNotDead = false;
            }
        }

        assertTrue(isNotDead);
    }

    @Test
    public void Test12cde () {
        CreateWorld("week-1/t1-2cde");

        
    }

    public void CreateWorld(String filename) {
        Map<String, ArrayList<String>> input = fileImport(filename);

        // Find size of world
        for (String name : input.keySet()) {
            if (input.get(name) == null) {
                size = Integer.parseInt(name);
                break;
            }
        }

        // Make world
        p = new Program(size, 800, 500);
        w = p.getWorld();

        for (String name : input.keySet()) {
            spawnStuff(name, input);
        }
    }
}
