package Main;

import itumulator.executable.Program;
import itumulator.world.Location;
import methodHelpers.DisplayProvider;
import ourActors.Bear;
import ourActors.Rabbit;
import ourActors.Wolf;
import ourActors.WolfPack;
import ourNonBlocking.BerryBush;
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

    public static void main(String[] args) {
        Map<String, ArrayList<String>> input = fileImport("week-1/t1-1a.txt"); // Input a file name

        // Find size of world
        for (String name : input.keySet()) {
            if (input.get(name) == null) {
                size = Integer.parseInt(name);

                //TestSize
                //size = 10;
                break;
            }
        }

        // Make world
        Program p = new Program(size, 800, 500);
        w = p.getWorld();
        //Initialize RandomLocationHelper
        rLoc = new RandomLocationHelper(w);

        //Initialize displayChnager
        displayChanger = new DisplayProvider(p);


        for (String name : input.keySet()) {
            spawnStuff(name, input, w);
        }

        /*
        //Testing * * * * *
        for (int i = 0; i < 6; i++) {
            Location l = rLoc.getRandomLocation();
            w.setTile(l, new Rabbit(w));
            l = rLoc.getRandomLocation();
            w.setTile(l, new Grass(w));
            l = rLoc.getRandomLocation();
            w.setTile(l, new BerryBush(w));
        }



        Location l = rLoc.getRandomLocation();
        new WolfPack(w, 3, l);

        l = rLoc.getRandomLocation();
        w.setTile(l, new Bear(w, l));

        */
        //Testing * * * * *

        p.show();

        for (int i = 0; i < 1000; i++) {
            p.simulate();
        }
    }

    //Imports a random file from week-1 folder
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

    public static void spawnStuff(String name, Map<String, ArrayList<String>> input, World world) {
        try {
            if (input.get(name).contains("-")) {
                List<String> temp = new ArrayList<>(List.of(input.get(name).get(1).split("-")));
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
                            System.out.println("Grass planted!");
                            spawnGrass(world);
                        }
                    }
                    case "rabbit" -> {
                        for (int i = 0; i < amount; i++) {
                            spawnRabbit();
                        }
                    }
                    case "burrow" -> {
                        for (int i = 0; i < amount; i++) {
                            spawnHole();
                        }
                    }
                    case "bear" -> {
                        for (int i = 0; i < amount; i++) {
                            spawnBear(new Location(x, y));
                        }
                    }
                    case "wolf" -> {
                        spawnWolf(amount);
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
                            System.out.println(i);
                            System.out.println(j);
                            spawnGrass(world);
                            System.out.println("Planted Grass");
                        }
                    }
                    case "rabbit" -> {
                        for (int i = 0; i < Integer.parseInt(input.get(name).get(1)); i++) {
                            spawnRabbit();
                        }
                    }
                    case "burrow" -> {
                        for (int i = 0; i < Integer.parseInt(input.get(name).get(1)); i++) {
                            spawnHole();
                        }
                    }
                }
            }
        } catch (NullPointerException _) {

        }
    }


    // Spawn some Grass
    public static void spawnGrass(World world) {
        world.setTile(rLoc.getNonBlockingLocation(getSize()), new Grass(world));
    }

    // Spawn a rabbit
    public static void spawnRabbit() {
        w.setTile(blockingLocation(), new Rabbit(w));
    }

    // Spawn a RabbitHole
    public static void spawnHole() {
        Location l = nonBlockingLocation();
        w.setTile(l, new RabbitHole(w));
    }

    public static void spawnWolf(int amount) {
        Location l = blockingLocation();
        w.setTile(l, new WolfPack(w, amount, l));
    }

    public static void spawnBear(Location location) {
        w.setTile(location, new Bear(w, location));
    }

    public static void spawnBerryBush() {
        w.setTile(blockingLocation(), new BerryBush(w));
    }

    // Find a location for a NonBlocking object
    public static Location nonBlockingLocation() {
        return rLoc.getNonBlockingLocation(getSize());
    }

    // Find a location for a Blocking object
    public static Location blockingLocation() {
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