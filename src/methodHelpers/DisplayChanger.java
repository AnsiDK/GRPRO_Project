package methodHelpers;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import ourActors.Bear;
import ourActors.Rabbit;
import ourActors.Wolf;
import ourNonBlocking.BerryBush;
import ourNonBlocking.Grass;
import ourNonBlocking.RabbitHole;
import ourNonBlocking.WolfDen;

import java.awt.*;

public class DisplayChanger {

    private static Program program;

    public DisplayChanger(Program program) {
        this.program = program;
        initializeDisplays();
    }

    public static void initializeDisplays() {
        DisplayInformation grassDi = new DisplayInformation(Color.green, "grass");
        program.setDisplayInformation(Grass.class, grassDi);

        DisplayInformation rabbitHoleDi = new DisplayInformation(Color.yellow, "hole-small");
        program.setDisplayInformation(RabbitHole.class, rabbitHoleDi);

        DisplayInformation wolfDenDi = new DisplayInformation(Color.orange, "hole");
        program.setDisplayInformation(WolfDen.class, wolfDenDi);

        DisplayInformation bushDi = new DisplayInformation(Color.pink, "bush");
        program.setDisplayInformation(BerryBush.class, bushDi);
    }

    public static void changeSprite(Object object, boolean grow) {

    }

    public DisplayInformation changeBerryBushSprite(BerryBush bush) {
        if (bush.getBerries()) {
            return new DisplayInformation(Color.red, "bush-berries");
        } else {
            return new DisplayInformation(Color.blue, "bush");
        }
    }
}
