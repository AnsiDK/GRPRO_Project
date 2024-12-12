import itumulator.executable.Program;
import ourActors.*;
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

/**
 * This class tests if the requirements of the assignment is fulfilled
 */

public class Tests {
    World w;
    int size;
    Program p;
    final static Random r = new Random();
    static RandomLocationHelper rLoc;

    /**
     * This method tests if grass will be placed when the input file requests it
     */
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

    /**
     * This test makes sure that grass can spread
     */
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

    /**
     * This test makes sure that animals can walk on top of grass
     */
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

    /**
     * This test makes sure that rabbits can be placed on the map when the input file specifies it
     */
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

    /**
     * This makes sure that when rabbits die, they get removed from the world
     */
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

    /**
     * This test makes sure that rabbits with less energy doesn't move as much
     */
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

    /**
     * This tests that rabbits can reproduce
     */
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

    /**
     * This tests that rabbits can dig holes
     */
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

    /**
     * This tests that rabbits go towards their holes when it gets dark
     */
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

    /**
     * This tests that RabbitHoles can be placed when the input file specifies
     */
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

    /**
     * This tests that anaimals can walk on top of RabbitHoles
     */
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

    /**
     * This tests that wolfs can be placed when the input file specifies
     */
    @Test
    public void Test21a () {
        CreateWorld("week-2/t2-1ab.txt");

        int occupied = 0;

        Map<Object, Location> objects = w.getEntities();

        for (Object o : objects.keySet()) {
            if (o instanceof Wolf) {
                occupied++;
            }
        }

        assertEquals(1, occupied);
    }

    /**
     * This tests that when wolfs die, they get removed from the world
     */
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

    /**
     * This tests that the wolf will hunt other animals
     */
    @Test
    public void Test21c () {
        CreateWorld("week-2/t2-1c.txt");

        boolean hunting = false;

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

        Location rabbitBefore = w.getLocation(rabbit);
        Location wolfBefore = w.getLocation(wolf);

        double distBefore = Searcher.getDistance(rabbitBefore, wolfBefore);

        p.simulate();

        Location rabbitAfter = w.getLocation(rabbit);
        Location wolfAfter = w.getLocation(wolf);

        double distAfter = Searcher.getDistance(rabbitAfter, wolfAfter);

        if (distBefore > distAfter) {
            hunting = true;
        }

        assertTrue(hunting);
    }

    /**
     * This tests that wolf will be walking together with other wolfs in their pack
     */
    @Test
    public void Test22a () {
        CreateWorld("week-2/t2-2aa.txt");

        boolean flok = true;

        Map<Object, Location> objects = w.getEntities();

        List<Animal> animals = new ArrayList<Animal>();

        for (Object o : objects.keySet()) {
            if (o instanceof Wolf) {
                animals.add((Wolf) o);
            }
        }

        for (int i = 0; i < 4; i++) {
            p.simulate();

            double dist = Searcher.getDistance(w.getLocation(animals.get(0)), w.getLocation(animals.get(1)));

            if (dist > 4) {
                flok = false;
            }
        }

        assertTrue(flok);
    }

    /**
     * This tests that wolfs will dig a hole
     */
    @Test
    public void Test23a () {
        CreateWorld("week-2/t2-3a.txt");

        boolean buildDen = false;

        Map<Object, Location> objects = w.getEntities();

        Wolf wolf = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Wolf) {
                wolf = (Wolf) o;
                break;
            }
        }

        for (int i = 0; i < 12; i++) {
            p.simulate();
        }

        if (wolf.getHome() != null) {
            buildDen = true;
        }

        assertTrue(buildDen);
    }

    /**
     * This tests that bears will be placed when the input file specifies
     */
    @Test
    public void Test24a () {
        CreateWorld("week-2/t2-4a.txt");

        int occupied = 0;

        Map<Object, Location> objects = w.getEntities();

        for (Object o : objects.keySet()) {
            if (o instanceof Bear) {
                occupied++;
            }
        }

        assertEquals(1, occupied);
    }

    /**
     * This tests that bears will hunt
     */
    @Test
    public void Test24b () {
        CreateWorld("week-2/t2-4bb.txt");

        boolean hasEaten = false;

        Map<Object, Location> objects = w.getEntities();

        Bear bear = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Bear) {
                bear = (Bear) o;
                break;
            }
        }

        for (int i = 0; i < 10; i++) {
            p.simulate();
        }

        if (bear.getFoodEaten() > 0) {
            hasEaten = true;
        }

        assertTrue(hasEaten);
    }

    /**
     * This tests that bears have a territory
     */
    @Test
    public void Test25a () {
        CreateWorld("week-2/t2-5a.txt");

        boolean hasTeretory = false;

        Map<Object, Location> objects = w.getEntities();

        Bear bear = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Bear) {
                bear = (Bear) o;
                break;
            }
        }

        if (bear.getTerritoryCenter() != null) {
            hasTeretory = true;
        }

        assertTrue(hasTeretory);
    }

    /**
     * This tests that bears can eat berryBushes
     */
    @Test
    public void Test26a () {
        CreateWorld("week-2/t2-6a.txt");

        boolean hasEaten = false;

        Map<Object, Location> objects = w.getEntities();

        Bear bear = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Bear) {
                bear = (Bear) o;
            } else if (o instanceof BerryBush) {
                BerryBush bush = (BerryBush) o;
                bush.grow();
            }
        }

        for (int i = 0; i < 10; i++) {
            p.simulate();
        }

        if (bear.getFoodEaten() > 0) {
            hasEaten = true;
        }

        assertTrue(hasEaten);
    }

    /**
     * This tests that carcasses can be placed when the input file specifies
     */
    @Test
    public void Test31a () {
        CreateWorld("week-3/t3-1a.txt");

        int occupied = 0;

        Map<Object, Location> objects = w.getEntities();

        for (Object o : objects.keySet()) {
            if (o instanceof Carcass) {
                occupied++;
            }
        }

        assertEquals(1, occupied);
    }

    /**
     * This tests that animals will leave a carcass when they die
     */
    @Test
    public void Test31b () {
        CreateWorld("week-3/t3-1b.txt");

        boolean madeCarcass = false;
        Rabbit rabbit = null;

        Map<Object, Location> objects = w.getEntities();

        for (Object o : objects.keySet()) {
            if (o instanceof Rabbit) {
                rabbit = (Rabbit) o;
                break;
            }
        }

        rabbit.die();

        objects = w.getEntities();

        for (Object o : objects.keySet()) {
            if (o instanceof Carcass) {
                madeCarcass = true;
                break;
            }
        }

        assertTrue(madeCarcass);
    }

    /**
     * This tests that animals can eat carcasses
     */
    @Test
    public void Test31bb () {
        CreateWorld("week-3/t3-1bb.txt");

        boolean hasEaten = false;

        Map<Object, Location> objects = w.getEntities();

        Wolf wolf = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Wolf) {
                wolf = (Wolf) o;
                break;
            }
        }

        for (int i = 0; i < 10; i++) {
            p.simulate();
        }

        if (wolf.getFoodEaten() > 0) {
            hasEaten = true;
        }

        assertTrue(hasEaten);
    }

    /**
     * This tests that carcasses will be removed when they at some point decay
     */
    @Test
    public void Test31c () {
        CreateWorld("week-3/t3-1c.txt");

        Map<Object, Location> objects = w.getEntities();

        Carcass carcass = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Carcass) {
                carcass = (Carcass) o;
                break;
            }
        }
        carcass.setEnergy(2);

        for (int i = 0; i < 10; i++) {
            p.simulate();
        }

        objects = w.getEntities();

        int amount = 0;

        for (Object o : objects.keySet()) {
            if (o instanceof Carcass) {
                amount++;
            }
        }

        assertEquals(0, amount);
    }

    /**
     * This tests that carcasses can be placed with fungi when the input file specifies
     */
    @Test
    public void Test32a () {
        CreateWorld("week-3/t3-2a.txt");

        boolean hasFungi = false;

        Map<Object, Location> objects = w.getEntities();

        Carcass carcass = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Carcass) {
                carcass = (Carcass) o;
                break;
            }
        }

        if (carcass.hasFungi()) {
            hasFungi = true;
        }

        assertTrue(hasFungi);
    }

    /**
     * This tests that mushrooms will die if there is nowhere to spread
     */
    @Test
    public void Test32ba () {
        CreateWorld("week-3/t3-2a.txt");

        boolean hasDied = true;

        Map<Object, Location> objects = w.getEntities();
        Carcass carcass = null;

        for (Object o : objects.keySet()) {
            if (o instanceof Carcass) {
                carcass = (Carcass) o;
                break;
            }
        }

        carcass.setEnergy(1);

        Mushroom mushroom = carcass.getMushroom();

        for (int i = 0; i < 30; i++) {
            p.simulate();
        }

        objects = w.getEntities();

        for (Object o : objects.keySet()) {
            if (o instanceof Mushroom) {
                hasDied = false;
            }
        }

        assertTrue(hasDied);
    }

    /**
     * This method creates a world
     * @param filename is the name of the input file
     */
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

    /**
     * This function creates a Map with the different values from the input files
     * @param fileName This is the name of the file that you want to import
     * @return This returns a HashMap with keys as the name of the object and values as an ArrayList that has the values for that object
     */
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

    /**
     * This function spawns the different objects
     * @param name The name of the object
     * @param input The Map with the values for the object
     * @param w The world that you want to spawn the animal in
     */
    public static void spawnStuff(String name, Map<String, ArrayList<String>> input, World w) {
        int amount = 0;
        String stringAmount = null;

        boolean fungi = false;
        List<String> temp = new ArrayList<>();

        int x = 0;
        int y = 0;

        if (input.get(name) != null) {
            try {
                if (input.get(name).size() == 2) {
                    if (name.equals("bear")) {
                        String coordinates = input.get(name).get(1);
                        String regex = "[,()]";
                        String[] test = coordinates.split(regex);

                        x = Integer.parseInt(test[1]);
                        y = Integer.parseInt(test[2]);

                        stringAmount = input.get(name).get(0);
                    } else if (name.equals("carcass")) {
                        fungi = true;
                        amount = Integer.parseInt(input.get(name).get(1));
                        stringAmount = input.get(name).get(1);
                    }
                } else {
                    stringAmount = input.get(name).get(0);
                }

                if (stringAmount.contains("-")) {
                    temp = new ArrayList<>(List.of(input.get(name).getFirst().split("-")));
                    int lower = Integer.parseInt(temp.getFirst());
                    int upper = Integer.parseInt(temp.getLast());
                    amount = r.nextInt(lower, upper+1);
                } else {
                    amount = Integer.parseInt(stringAmount);
                }

                System.out.println(amount);

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
                    case "carcass" -> {
                        for (int i = 0; i < amount; i++) {
                            spawnCarcass(w, fungi);
                        }
                    }
                    case "" -> {
                        //Do nothing
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }


    /**
     * This spawns grass
     * @param w The world that you want to spawn it in
     */
    public static void spawnGrass(World w) {
        w.setTile(rLoc.getNonBlockingLocation(w.getSize()), new Grass(w));
    }

    /**
     * This spawns Rabbits
     * @param w The world that you want to spawn it in
     */
    public static void spawnRabbit(World w) {
        w.setTile(blockingLocation(w), new Rabbit(w));
    }

    /**
     * This spawns RabbitHoles
     * @param w The world that you want to spawn it in
     */
    public static void spawnHole(World w) {
        Location l = nonBlockingLocation(w);
        w.setTile(l, new RabbitHole(w));
    }

    /**
     * This spawns Wolfs
     * @param w The world that you want to spawn it in
     */
    public static void spawnWolf(World w, int amount) {
        Location l = blockingLocation(w);
        new WolfPack(w, amount, l);
    }

    /**
     * This spawns Bears
     * @param w The world that you want to spawn it in
     */
    public static void spawnBear(World w, Location location) {
        w.setTile(location, new Bear(w, location));
    }

    /**
     * This spawns BerryBushes
     * @param w The world that you want to spawn it in
     */
    public static void spawnBerryBush(World w) {
        w.setTile(blockingLocation(w), new BerryBush(w));
    }

    /**
     * This spawns carcasses
     * @param w The world that you want to spawn it in
     * @param fungi Does the carcass have fungi
     */
    public static void spawnCarcass(World w, boolean fungi) {
        Location l = blockingLocation(w);
        Carcass carcass = new Carcass(w, 30, l);
        w.setTile(l, carcass);
        if (fungi) {
            carcass.growShrooms();
        }
    }

    /**
     * Finds a free location for a nonBlocking object
     * @param w the World
     * @return The free location
     */
    public static Location nonBlockingLocation(World w) {
        return rLoc.getNonBlockingLocation(w.getSize());
    }

    /**
     * Finds a free location for a blocing object
     * @param w The world
     * @return The free location
     */
    public static Location blockingLocation(World w) {
        return rLoc.getRandomLocation(w.getSize());
    }

    /**
     * This finds the average in a list of integers
     * @param list list of integers that you want to find the average of
     * @return A double that is the average value
     */
    public static double avg(List<Integer> list) {
        int sum = 0;
        for (int i : list) {
            sum += i;
        }
        return 1.0 * sum / list.size();
    }
}
