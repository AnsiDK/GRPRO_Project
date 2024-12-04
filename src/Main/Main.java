package Main;

import itumulator.executable.Program;
import itumulator.world.Location;
import methodHelpers.DisplayProvider;
import ourActors.Bear;
import ourActors.Rabbit;
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
        Map<String, String> input = fileImport();

        // Find size of world
        for (String name : input.keySet()) {
            if (input.get(name) == null) {
                //size = Integer.parseInt(name);

                //TestSize
                size = 10;
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


        /*
        for (String name : input.keySet()) {
            spawnStuff(name, input);
        }
        */

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
        //new WolfPack(w, 3, l);

        l = rLoc.getRandomLocation();
        //w.setTile(l, new Bear(w, l));



        //Testing * * * * *

        p.show();

        for (int i = 0; i < 1000; i++) {
            p.simulate();
        }
    }

    //Imports a random file from week-1 folder
    static Map<String, String> fileImport() {

        String folderPath = "week-1";

        File folder = new File(folderPath);

        if (folder.isDirectory()) {
            File[] txtFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

            if (txtFiles != null && txtFiles.length > 0) {
                int randomIndex = r.nextInt(txtFiles.length);
                File randomFile = txtFiles[randomIndex];

                try {
                    Scanner sc = new Scanner(randomFile);
                    Map<String, String> map = new HashMap<>();

                    while (sc.hasNextLine()) {
                        List<String> temp = Arrays.asList(sc.nextLine().split(" "));
                        if (temp.size() < 2) {
                            map.put(temp.getFirst(), null);
                        } else {
                            map.put(temp.get(0), temp.get(1));
                        }
                    }
                    System.out.println(randomFile.getName() + " " + map);
                    return map;
                } catch (FileNotFoundException e) {
                    System.out.println("File not found: " + randomFile);
                    return null;
                }
            }
        }
        return null;
    }

    public static void spawnStuff(String name, Map<String, String> input) {
        try {
            if (input.get(name).contains("-")) {
                List<String> temp = new ArrayList<>(List.of(input.get(name).split("-")));

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
                    default -> {
                        //Do nothing
                    }
                }

            } else {
                switch (name) {
                    case "grass" -> {
                        for (int i = 0; i < Integer.parseInt(input.get(name)); i++) {
                            spawnGrass();
                        }
                    }
                    case "rabbit" -> {
                        for (int i = 0; i < Integer.parseInt(input.get(name)); i++) {
                            spawnRabbit();
                        }
                    }
                    case "burrow" -> {
                        for (int i = 0; i < Integer.parseInt(input.get(name)); i++) {
                            spawnHole();
                        }
                    }
                    default -> {
                        //Do nothing
                    }
                }
            }
        } catch (NullPointerException _) {

        }
    }

    // Spawn some Grass
    public static void spawnGrass() {
        w.setTile(nonBlockingLocation(), new Grass(w));
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

    // Find a location for a NonBlocking object
    public static Location nonBlockingLocation() {
        return rLoc.getNonBlockingLocation();
    }

    // Find a location for a Blocking object
    public static Location blockingLocation() {
        return rLoc.getRandomLocation();
    }

    public static int getNonBlockingObjects() { return nonBlockingObjects; }

    public static void setNonBlockingObjects(int value) {           //"Change" the amount of grass in the world
        nonBlockingObjects = value;
    }

    public static int getSize() {           //"Change" the amount of grass in the world
        return size;
    }
}