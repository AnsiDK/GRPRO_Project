package Main;

import itumulator.executable.Program;
import itumulator.world.Location;
import methodHelpers.DisplayProvider;
import ourActors.Bear;
import ourActors.Rabbit;
import ourActors.WolfPack;
import ourNonBlocking.BerryBush;
import ourNonBlocking.Carcass;
import ourNonBlocking.Grass;
import ourNonBlocking.RabbitHole;
import itumulator.world.World;
import methodHelpers.RandomLocationHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main {
    private static int nonBlockingObjects = 0;
    private static int size;
    final static Random r = new Random();
    static RandomLocationHelper rLoc;
    static World w;
    static DisplayProvider displayChanger;
    static Program p;

    public static void main(String[] args) {
        CreateWorld("week-3/Demo.txt");

        //Initialize RandomLocationHelper
        rLoc = new RandomLocationHelper(w);

        //Initialize displayChnager
        displayChanger = new DisplayProvider(p);

        p.show();

        for (int i = 0; i < 1000; i++) {
            p.simulate();
        }
    }

    /**
     * This method creates a world
     * @param filename is the name of the input file
     */
    public static void CreateWorld(String filename) {
        Map<String, ArrayList<String>> input = fileImport(filename);

        // Find size of world
        for (String name : input.keySet()) {
            if (input.get(name) == null) {
                size = Integer.parseInt(name);
                break;
            }
        }

        // Make world
        p = new Program(size, 800, 1000);
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

    public static int getNonBlockingObjects() { return nonBlockingObjects; }

    public static void setNonBlockingObjects(int value) {           //"Change" the amount of grass in the world
        nonBlockingObjects = value;
    }

    public static int getSize() {           //"Change" the amount of grass in the world
        return size;
    }
}