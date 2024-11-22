package Main;

import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.simulator.Grass;
import itumulator.simulator.Rabbit;
import itumulator.world.Location;
import itumulator.world.RabbitHole;
import itumulator.world.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main {
    private static int nonBlockingObjects = 0;
    private static int size;
    final static Random r = new Random();

    public static void main(String[] args) {
        Map<String, String> input = fileImport("week-1/t1-1a.txt");

        // Find size of world
        for (String name : input.keySet()) {
            if (input.get(name) == null) {
                //size = Integer.parseInt(name);

                //TestSize
                size = 5;
                break;
            }
        }

        // Make world
        Program p = new Program(size, 800, 1000);
        World w = p.getWorld();

        // Plant the grass
        for (String name : input.keySet()) {
            spawnStuff(name, input, w);
        }

        DisplayInformation grassDi = new DisplayInformation(Color.green, "grass");
        p.setDisplayInformation(Grass.class, grassDi);

        DisplayInformation rabbitDi = new DisplayInformation(Color.blue, "rabbit-small");
        p.setDisplayInformation(Rabbit.class, rabbitDi);

        DisplayInformation rabbitHoleDi = new DisplayInformation(Color.yellow, "hole");
        p.setDisplayInformation(RabbitHole.class, rabbitHoleDi);

        //Rabbit and Rabbithole test
        for (int i = 0; i < 3; i++) {
            Location l = new Location(r.nextInt(size), r.nextInt(size));
            while (w.contains(Rabbit.class)) {
                l = new Location(r.nextInt(size), r.nextInt(size));
            }
            w.setTile(l, new Rabbit());
        }
        //Location l = new Location(2, 2);
        //w.setTile(l, new Grass(w));
        //Test * * * * *

        p.show();

        for (int i = 0; i < 1000; i++) {
            p.simulate();
        }
    }

    static Map<String, String> fileImport(String fileName) {

        File file = new File(fileName);

        try {
            Scanner sc = new Scanner(file);
            Map<String, String> map = new HashMap<>();

            while (sc.hasNextLine()) {
                List<String> temp = Arrays.asList(sc.nextLine().split(" "));
                if (temp.size() < 2) {
                    map.put(temp.getFirst(), null);
                } else {
                    map.put(temp.get(0), temp.get(1));
                }
            }
            return map;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            return null;
        }
    }

    public static void spawnStuff(String name, Map<String, String> input, World w) {
        try {
            if (input.get(name).contains("/")) {
                List<String> temp = new ArrayList<>(List.of(input.get(name).split("/")));

                int lower = Integer.parseInt(temp.getFirst());
                int upper = Integer.parseInt(temp.getLast());
                int amount = r.nextInt(lower, upper);

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
                }

            } else {
                switch (name) {
                    case "grass" -> {
                        for (int i = 0; i < Integer.parseInt(input.get(name)); i++) {
                            spawnGrass(w);
                        }
                    }
                    case "rabbit" -> {
                        for (int i = 0; i < Integer.parseInt(input.get(name)); i++) {
                            spawnRabbit(w);
                        }
                    }
                    case "burrow" -> {
                        for (int i = 0; i < Integer.parseInt(input.get(name)); i++) {
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
        w.setTile(nonBlockingLocation(w), new Grass(w));
    }

    // Spawn a rabbit
    public static void spawnRabbit(World w) {
        w.setTile(blockingLocation(w), new Rabbit());
    }

    // Spawn a RabbitHole
    public static void spawnHole(World w) {
        Location l = nonBlockingLocation(w);
        w.setTile(l, new RabbitHole());
    }

    // Find a location for a NonBlocking object
    public static Location nonBlockingLocation(World w) {
        Location l = new Location(r.nextInt(size), r.nextInt(size));

        while (w.containsNonBlocking(l)) {
            l = new Location(r.nextInt(size), r.nextInt(size));
        }

        nonBlockingObjects++;
        return l;
    }

    // Find a location for a Blocking object
    public static Location blockingLocation(World w) {
        Location l = new Location(r.nextInt(size), r.nextInt(size));

        while (!w.isTileEmpty(l)) {
            l = new Location(r.nextInt(size), r.nextInt(size));
        }

        nonBlockingObjects++;
        return l;
    }

    public static int getNonBlockingObjects() {          //Get the amount of grass in the world
        return nonBlockingObjects;
    }

    public static void setNonBlockingObjects(int value) {           //"Change" the amount of grass in the world
        nonBlockingObjects = value;
    }

    public static int getSize() {           //"Change" the amount of grass in the world
        return size;
    }
}