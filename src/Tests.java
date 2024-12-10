import itumulator.executable.Program;
import ourActors.Rabbit;
import ourNonBlocking.*;
import itumulator.world.World;
import itumulator.world.Location;
import methodHelpers.Searcher;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import methodHelpers.RandomLocationHelper;

import static org.junit.jupiter.api.Assertions.*;


public class Tests {
    World w;
    int size;
    Program p;
    final static Random r = new Random();
    static RandomLocationHelper rLoc;


    @Test
    public void Test11a () {
        CreateWorld("week-1/t1-1a.txt");

        int occupied = 0;

        Map<Object, Location> objects = w.getEntities();

        System.out.println(objects);

        for (Object o : objects.keySet()) {
            System.out.println(o);
            if (o instanceof Grass) {
                occupied++;
            }
        }

        assertEquals(3, occupied);
    }

    @Test
    public void Test11b () {
        CreateWorld("week-1/t1-1b.txt");

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

    // test
    @Test
    public void Test11c () {
        CreateWorld("week-1/t1-1c.txt");

        boolean isOnGrass = false;

        Map<Object, Location> objects = w.getEntities();

        Rabbit rabbit = null;
        Location grass = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Rabbit) {
                Location loc = w.getLocation(o);
                rabbit = (Rabbit) w.getTile(loc);
            } else if (o instanceof Grass) {
                grass = w.getLocation(o);
            }
        }

        try {
            w.move(rabbit, grass);
            isOnGrass = true;
        } catch (Exception _) {

        }

        assertTrue(isOnGrass);
    }

    @Test
    public void Test12a () {
        CreateWorld("week-1/t1-2a.txt");

        int occupied = 0;

        Map<Object, Location> objects = w.getEntities();

        for (Object o : objects.keySet()) {
            if (o instanceof Rabbit) {
                occupied++;
            }
        }

        assertEquals(1, occupied);
    }

    @Test
    public void Test12b () {
        CreateWorld("week-1/t1-2b.txt");

        boolean isNotDead = true;

        Map<Object, Location> objects = w.getEntities();

        Rabbit rabbit = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Rabbit) {
                Location loc = objects.get(o);
                rabbit = (Rabbit) w.getTile(loc);
            }
        }

        // This needs to be implemented...
        rabbit.die();

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
        CreateWorld("week-1/t1-2cde.txt");

        boolean reproduced = false;

        Map<Object, Location> objects = w.getEntities();

        int rabbits = 0;

        for (Object o : objects.keySet()) {
            if (o == Rabbit.class) {
                rabbits++;
            }
        }

        for (int i = 0; i < 100; i++) {
            p.simulate();
        }

        objects = w.getEntities();

        int rabbitsAfter = 0;

        for (Object o : objects.keySet()) {
            if (o == Rabbit.class) {
                rabbitsAfter++;
            }
        }

        if (rabbitsAfter > rabbits) {
            reproduced = true;
        }

        assertTrue(reproduced);
    }

    @Test
    public void Test12f () {
        CreateWorld("week-1/t1-2fg.txt");

        Map<Object, Location> objects = w.getEntities();

        boolean diggedHole = false;

        int holesBefore = 0;

        for (Object o : objects.keySet()) {
            if (o instanceof RabbitHole) {
                holesBefore++;
            }
        }

        for (int i = 0; i < 100; i++) {
            p.simulate();
        }

        objects = w.getEntities();

        int holesAfter = 0;

        for (Object o : objects.keySet()) {
            if (o instanceof RabbitHole) {
                holesAfter++;
            }
        }

        if (holesAfter > holesBefore) {
            diggedHole = true;
        }

        assertTrue(diggedHole);
    }

    @Test
    public void Test12g () {
        CreateWorld("week-1/t1-fg.txt");

        boolean goToHole = false;

        Map<Object, Location> objects = w.getEntities();

        Rabbit rabbit = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Rabbit) {
                Location loc = objects.get(o);
                rabbit = (Rabbit) w.getTile(loc);
            }
        }

        for (int i = 0; i < 10; i++) {
            p.simulate();
        }

        Home hole = rabbit.getHome();

        Location locRabBefore = w.getLocation(rabbit);
        Location locHol = w.getLocation(hole);

        double dist = Searcher.getDistance(locRabBefore, locHol);

        p.simulate();

        Location locRabAfter = w.getLocation(rabbit);

        double distAfter = Searcher.getDistance(locRabAfter, locHol);

        if (dist > distAfter) {
            goToHole = true;
        }

        assertTrue(goToHole);
    }

    @Test
    public void Test13a () {
        CreateWorld("week-1/t1-3a.txt");

        Map<Object, Location> objects = w.getEntities();

        boolean placedHole = false;

        for (Object o : objects.keySet()) {
            if (o instanceof RabbitHole) {
                placedHole = true;
            }
        }

        assertTrue(placedHole);
    }

    @Test
    public void Test13b () {
        CreateWorld("week-1/t1-3b.txt");

        boolean standOnHole = false;

        Map<Object, Location> objects = w.getEntities();

        Rabbit rabbit = null;
        RabbitHole hole = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Rabbit) {
                Location loc = objects.get(o);
                rabbit = (Rabbit) w.getTile(loc);
            } else if (o instanceof RabbitHole) {
                Location loc = objects.get(o);
                hole = (RabbitHole) w.getTile(loc);
            }
        }

        try {
            w.move(rabbit, w.getLocation(hole));
            standOnHole = true;
        } catch (Exception _) {

        }

        assertTrue(standOnHole);
    }

    public void CreateWorld(String filename) {
        Map<String, ArrayList<String>> input = fileImport(filename);

        // Find size of world
        for (String name : input.keySet()) {
            if (input.get(name) == null) {
                System.out.println(name);
                size = Integer.parseInt(name);
                break;
            }
        }

        // Make world
        p = new Program(size, 80, 500);
        w = p.getWorld();

        rLoc = new RandomLocationHelper(w);

        for (String name : input.keySet()) {
            spawnStuff(name, input, w);
        }
    }

    public static Map<String, ArrayList<String>> fileImport(String fileName) {

        File file = new File(fileName);

        try {
            Scanner sc = new Scanner(file);
            Map<String, ArrayList<String>> map = new HashMap<>();

            while (sc.hasNextLine()) {
                List<String> temp = Arrays.asList(sc.nextLine().split(" "));
                if (temp.size() < 2) {
                    map.put(temp.getFirst(), null);
                } else if (temp.size() == 2) {
                    map.put(temp.getFirst(), new ArrayList<>());
                    map.get(temp.getFirst()).add(temp.get(1));
                } else {
                    map.put(temp.getFirst(), new ArrayList<>());
                    map.get(temp.getFirst()).add(temp.get(1));
                    map.get(temp.getFirst()).add(temp.get(2));
                }
            }
            return map;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            return null;
        }
    }

    public static void spawnStuff(String name, Map<String, ArrayList<String>> input, World w) {
        try {
            if (input.get(name).getFirst().contains("-")) {
                List<String> temp = new ArrayList<>(List.of(input.get(name).getFirst().split("-")));
                String coordinates = null;

                if (input.get(name).size() == 3) {
                    coordinates = input.get(name).get(2);
                }

                int lower = Integer.parseInt(temp.getFirst());
                int upper = Integer.parseInt(temp.getLast());
                int amount = r.nextInt(lower, upper+1);

                switch (name) {
                    case "grass" -> {
                        for (int i = 0; i < amount; i++) {
                            spawnGrass(w);
                        }
                    }
                    case "rabbit" -> {
                        for (int i = 0; i < amount; i++) {
                            spawnRabbit(w);
                        }
                    }
                    case "burrow" -> {
                        for (int i = 0; i < amount; i++) {
                            spawnHole(w);
                        }
                    }
                    case "bear" -> {
                        for (int i = 0; i < amount; i++) {
                            //spawnBear(coordinates);
                        }
                    }
                    case "wolf" -> {
                        for (int i = 0; i < amount; i++) {
                            //spawnWolf();
                        }
                    }
                    case "berry" -> {
                        for (int i = 0; i < amount; i++) {
                            //spawnBerry();
                        }
                    }
                    case "" -> {
                        //Do nothing
                    }
                }

            } else {
                switch (name) {
                    case "grass" -> {
                        int j = Integer.parseInt(input.get(name).getFirst());
                        for (int i = 0; i < j; i++) {
                            spawnGrass(w);
                        }
                    }
                    case "rabbit" -> {
                        for (int i = 0; i < Integer.parseInt(input.get(name).getFirst()); i++) {
                            spawnRabbit(w);
                        }
                    }
                    case "burrow" -> {
                        for (int i = 0; i < Integer.parseInt(input.get(name).getFirst()); i++) {
                            spawnHole(w);
                        }
                    }
                }
            }
        } catch (NullPointerException _) {

        }
    }


    // Spawn some Grass
    public static void spawnGrass(World w) {
        w.setTile(rLoc.getNonBlockingLocation(w.getSize()), new Grass(w));
    }

    // Spawn a rabbit
    public static void spawnRabbit(World w) {
        w.setTile(blockingLocation(w), new Rabbit(w));
    }

    // Spawn a RabbitHole
    public static void spawnHole(World w) {
        Location l = nonBlockingLocation(w);
        w.setTile(l, new RabbitHole(w));
    }

    // Find a location for a NonBlocking object
    public static Location nonBlockingLocation(World w) {
        return rLoc.getNonBlockingLocation(w.getSize());
    }

    // Find a location for a Blocking object
    public static Location blockingLocation(World w) {
        return rLoc.getRandomLocation(w.getSize());
    }

    //public static int getNonBlockingObjects() { return nonBlockingObjects; }

    /*
    public static void setNonBlockingObjects(int value) {           //"Change" the amount of grass in the world
        nonBlockingObjects = value;
    }

     */

    /*
    public static int getSize() {           //"Change" the amount of grass in the world
        return size;
    }

     */
}
