import itumulator.executable.Program;
import ourActors.Bear;
import ourActors.Rabbit;
import ourActors.Wolf;
import ourActors.WolfPack;
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

        boolean isDead = false;

        Map<Object, Location> objects = w.getEntities();

        Rabbit rabbit = null;

        int before = 0;

        for (Object o : objects.keySet()) {
            if (o instanceof Rabbit) {
                Location loc = objects.get(o);
                rabbit = (Rabbit) w.getTile(loc);
                before++;
            }
        }

        rabbit.die();

        objects = w.getEntities();

        int after = 0;

        for (Object key : objects.keySet()) {
            if (key instanceof Rabbit) {
                after++;
            }
        }

        if (after < before) {
            isDead = true;
        }

        assertTrue(isDead);
    }

    @Test
    public void Test12d () {
        CreateWorld("week-1/t1-2a.txt");

        boolean isSlow = false;

        List<Integer> moved = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            w.setDay();
            Rabbit rabbit = new Rabbit(w);
            Location before = blockingLocation(w);
            w.setTile(before, rabbit);
            rabbit.setEnergy(100);

            p.simulate();

            Location after = w.getLocation(rabbit);

            if (before != after) {
                moved.add(1);
            } else {
                moved.add(0);
            }

            w.delete(rabbit);
        }

        List<Integer> movedLow = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            w.setDay();
            Rabbit rabbit = new Rabbit(w);
            Location before = blockingLocation(w);
            w.setTile(before, rabbit);
            rabbit.setEnergy(2);

            p.simulate();

            Location after = w.getLocation(rabbit);

            if (before != after) {
                movedLow.add(1);
            } else {
                movedLow.add(0);
            }

            w.delete(rabbit);
        }

        if (avg(movedLow) < avg(moved)) {
            isSlow = true;
        }

        assertTrue(isSlow);
    }

    @Test
    public void Test2e() {
        CreateWorld("week-1/t1-2e.txt");
        boolean reproduced = false;

        Rabbit rabbit = null;

        Map<Object, Location> objects = w.getEntities();

        for (Object o : objects.keySet()) {
            if (o instanceof Rabbit) {
                rabbit = (Rabbit) o;
            }
        }

        rabbit.buildHome();
        Home home = rabbit.getHome();
        Rabbit rabbit2 = new Rabbit(w);
        home.addAnimal(rabbit2);
        w.setTile(blockingLocation(w), rabbit2);
        rabbit.setEnergy(200);
        rabbit2.setEnergy(200);
        rabbit.grow();
        rabbit2.grow();

        for (int i = 0; i < 40; i++) {
            p.simulate();
        }

        objects = w.getEntities();
        int rabbitsAfter = 0;

        for (Object o : objects.keySet()) {
            if (o instanceof Rabbit) {
                rabbitsAfter++;
            }
        }

        System.out.println(rabbitsAfter);

        if (2 < rabbitsAfter) {
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
        CreateWorld("week-1/t1-2cde.txt");

        w.setDay();

        boolean goToHole = false;

        Map<Object, Location> objects = w.getEntities();

        Rabbit rabbit = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Rabbit) {
                Location loc = objects.get(o);
                rabbit = (Rabbit) w.getTile(loc);
                break;
            }
        }

        Location test = w.getLocation(rabbit);

        for (int i = 0; i < 9; i++) {
            p.simulate();
        }

        rabbit.buildHome();

        Home hole = rabbit.getHome();

        Location locRabBefore = w.getLocation(rabbit);

        double dist = Searcher.getDistance(locRabBefore, hole.getLocation());

        p.simulate();

        try {
            Location locRabAfter = w.getLocation(rabbit);

            double distAfter = Searcher.getDistance(locRabAfter, hole.getLocation());

            if (dist > distAfter) {
                goToHole = true;
            }
        } catch (Exception _) {
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

    @Test
    public void Test21a () {
        CreateWorld("week-2/t2-1ab.txt");

        int occupied = 0;

        Map<Object, Location> objects = w.getEntities();

        for (Object o : objects.keySet()) {
            System.out.println(o);
            if (o instanceof Wolf) {
                occupied++;
            }
        }

        assertEquals(1, occupied);
    }

    @Test
    public void Test21b () {
        CreateWorld("week-2/t2-1ab.txt");

        boolean isDead = false;

        Map<Object, Location> objects = w.getEntities();

        Wolf wolf = null;

        int before = 0;

        for (Object o : objects.keySet()) {
            if (o instanceof Wolf) {
                Location loc = objects.get(o);
                wolf = (Wolf) w.getTile(loc);
                before++;
            }
        }

        wolf.die();

        objects = w.getEntities();

        int after = 0;

        for (Object key : objects.keySet()) {
            if (key instanceof Wolf) {
                after++;
            }
        }

        if (after < before) {
            isDead = true;
        }

        assertTrue(isDead);
    }

    @Test
    public void Test21c () {
        CreateWorld("week-2/t2-1c.txt");

        Map<Object, Location> objects = w.getEntities();

        Wolf wolf = null;
        Rabbit rabbit = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Wolf) {
                wolf = (Wolf) o;
            } else if (o instanceof Rabbit) {
                rabbit = (Rabbit) o;
            }
        }


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

                int x = 0;
                int y = 0;

                if (input.get(name).size() == 3) {
                    String coordinates = input.get(name).get(2);
                    x = coordinates.charAt(1) - '0';
                    y = coordinates.charAt(3) - '0';
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
                            spawnBear(w, new Location(x, y));
                        }
                    }
                    case "wolf" -> {
                        System.out.println("wolf");
                        spawnWolf(w, amount);
                    }
                    case "berry" -> {
                        for (int i = 0; i < amount; i++) {
                            spawnBerryBush(w);
                        }
                    }
                    case "" -> {
                        //Do nothing
                    }
                }

            } else {
                int amount = Integer.parseInt(input.get(name).getFirst());

                int x = 0;
                int y = 0;

                if (input.get(name).size() == 3) {
                    String coordinates = input.get(name).get(2);
                    x = coordinates.charAt(1) - '0';
                    y = coordinates.charAt(3) - '0';
                }

                switch (name) {
                    case "grass" -> {
                        int j = amount;
                        for (int i = 0; i < j; i++) {
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
                            spawnBear(w, new Location(x, y));
                        }
                    }
                    case "wolf" -> {
                        System.out.println("wolf");
                        spawnWolf(w, amount);
                    }
                    case "berry" -> {
                        for (int i = 0; i < amount; i++) {
                            spawnBerryBush(w);
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

    public static void spawnWolf(World w, int amount) {
        Location l = blockingLocation(w);
        new WolfPack(w, amount, l);
    }

    public static void spawnBear(World w, Location location) {
        w.setTile(location, new Bear(w, location));
    }

    public static void spawnBerryBush(World w) {
        w.setTile(blockingLocation(w), new BerryBush(w));
    }

    // Find a location for a NonBlocking object
    public static Location nonBlockingLocation(World w) {
        return rLoc.getNonBlockingLocation(w.getSize());
    }

    // Find a location for a Blocking object
    public static Location blockingLocation(World w) {
        return rLoc.getRandomLocation(w.getSize());
    }

    public static double avg(List<Integer> list) {
        int sum = 0;
        for (int i : list) {
            sum += i;
        }
        return 1.0 * sum / list.size();
    }
}
