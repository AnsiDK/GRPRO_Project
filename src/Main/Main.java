package Main;

import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.simulator.Grass;
import itumulator.simulator.Rabbit;
import itumulator.world.Location;
import itumulator.world.RabbitHole;
import itumulator.world.World;
import methodHelpers.RandomLocationHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * This is the main class where the main function runs.
 *
 * @author Andreas, Alfred and Nicole
 */
public class Main {
    private static int nonBlockingObjects = 0;
    private static int size;
    final static Random r = new Random();
    static RandomLocationHelper rLoc;
    static World w;

    /**
     * Main function
     * @param args ...
     */
    public static void main(String[] args) {
        Map<String, ArrayList<String>> input = fileImport("week-1/t1-1a.txt");

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


        for (String name : input.keySet()) {
            spawnStuff(name, input);
        }


        DisplayInformation grassDi = new DisplayInformation(Color.green, "grass");
        p.setDisplayInformation(Grass.class, grassDi);

        DisplayInformation rabbitDi = new DisplayInformation(Color.blue, "rabbit-small");
        p.setDisplayInformation(Rabbit.class, rabbitDi);

        DisplayInformation rabbitHoleDi = new DisplayInformation(Color.yellow, "hole");
        p.setDisplayInformation(RabbitHole.class, rabbitHoleDi);

        //Rabbit and Rabbithole test

        for (int i = 0; i < 5; i++) {
            Location l = rLoc.getRandomLocation();
            Location l2 = rLoc.getRandomLocation();
            w.setTile(l, new Rabbit(w));
            w.setTile(l2, new Grass(w));
        }

        //Test * * * * *

        p.show();
        for (int i = 0; i < 1000; i++) {
            p.simulate();
        }

    }

    /**
     * This is the function that reads the files and puts the content in a HashMap,
     * with the keys being the name of the object, and the values being the amount of the objects
     *
     * @return A HashMap where keys are the object, and the values are the amount.
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
     * This function spawns the objects that are in the input file.
     * @param name This is the name f the object
     * @param input This is the amount of objects that needs to be spawned.
     */
    public static void spawnStuff(String name, Map<String, ArrayList<String>> input) {
        try {
            if (input.get(name).contains("-")) {
                List<String> temp = new ArrayList<>(List.of(input.get(name).get(1).split("-")));
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
                            spawnGrass();
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
                        for (int i = 0; i < Integer.parseInt(input.get(name).get(1)); i++) {
                            spawnGrass();
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
    public static void spawnGrass() {
        w.setTile(rLoc.getRandomNonBlockingLocation(), new Grass(w));
    }

    // Spawn a rabbit
    public static void spawnRabbit() {
        w.setTile(rLoc.getRandomLocation(), new Rabbit(w));
    }

    // Spawn a RabbitHole
    public static void spawnHole() {
        Location l = rLoc.getRandomNonBlockingLocation();
        w.setTile(l, new RabbitHole(w));
    }

    /**
     * Gives the amount of nonBlocking objects in the world
     * @return int
     */
    public static int getNonBlockingObjects() {
        return nonBlockingObjects;
    }

    /**
     * "Change the amount of non blocking objects in the world"
     * @param value
     */
    public static void setNonBlockingObjects(int value) {
        nonBlockingObjects = value;
    }

    /**
     * Get the size of the current world
     * @return int
     */
    public static int getSize() {
        return size;
    }
}