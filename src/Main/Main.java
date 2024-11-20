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
        Program p = new Program(size, 800, 75);
        Random r = new Random();


        World w = p.getWorld();

        //Grass test * * *
        DisplayInformation di = new DisplayInformation(Color.green, "grass");
        p.setDisplayInformation(Grass.class, di);

        for (int i = 0; i < 1; i++) {           //Place grass randomly
            Location l = new Location(r.nextInt(size), r.nextInt(size));

            while (w.containsNonBlocking(l)) {
                l = new Location(r.nextInt(size), r.nextInt(size));
            }
            allGrass++;
            w.setTile(l, new Grass(w));
        }
        //Grass test * * *


        p.show();

        for (int i = 0; i < 200; i++) {
            p.simulate();
        }
    }

    static void fileImport(String fileName) {

        File file = new File(fileName);

        try {
            Scanner sc = new Scanner(file);
            for (String line : sc.nextLine().split("\n")) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }
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