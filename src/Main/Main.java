package Main;

import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.simulator.Grass;
import itumulator.world.Location;
import itumulator.world.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main {
    private static int allGrass = 0;
    private static int size;

    public static void main(String[] args) {
        size = 5;

        Random r = new Random();




        Map<String, String> input = fileImport("week-1/t1-1a.txt");

        // Find size of world
        for (String name : input.keySet()) {
            if (input.get(name) == null) {
                size = Integer.parseInt(name);
                break;
            }
        }

        // Make world
        Program p = new Program(size, 800, 1000);
        World w = p.getWorld();

        // Plant the grass
        for (String name : input.keySet()) {
            if (name.equals("grass")) {
                if (input.get(name).contains("/")) {
                    List<String> temp = new ArrayList<>(List.of(input.get(name).split("/")));

                    int lower = Integer.parseInt(temp.getFirst());
                    int upper = Integer.parseInt(temp.getLast());
                    int amount = r.nextInt(lower, upper);

                    for (int i = 0; i < amount; i++) {
                        plantGrass(w);
                    }

                } else {
                    for (int i = 0; i < Integer.parseInt(input.get(name)); i++) {
                        plantGrass(w);
                    }
                }
            }
        }



        DisplayInformation di = new DisplayInformation(Color.green, "grass");
        p.setDisplayInformation(Grass.class, di);

        p.show();

        for (int i = 0; i < 200; i++) {
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

    public static void plantGrass(World w) {
        Random r = new Random();

        Location l = new Location(r.nextInt(size), r.nextInt(size));

        while (w.containsNonBlocking(l)) {
            l = new Location(r.nextInt(size), r.nextInt(size));
        }

        allGrass++;
        w.setTile(l, new Grass(w));
    }

    public static int getGrass() {          //Get the amount of grass in the world
        return allGrass;
    }

    public static void setGrass(int value) {           //"Change" the amount of grass in the world
        allGrass = value;
    }

    public static int getSize() {           //"Change" the amount of grass in the world
        return size;
    }
}