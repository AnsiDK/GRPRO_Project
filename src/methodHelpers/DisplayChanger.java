package methodHelpers;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import ourActors.blocking.Bear;
import ourActors.blocking.Rabbit;
import ourActors.blocking.Wolf;
import ourActors.nonBlocking.BerryBush;
import ourActors.nonBlocking.Grass;
import ourNonBlocking.RabbitHole;

import java.awt.*;

public class DisplayChanger {

    private static Program program;

    public DisplayChanger(Program program) {
        this.program = program;
    }

    public static void initializeDisplays() {
        DisplayInformation grassDi = new DisplayInformation(Color.green, "grass");
        program.setDisplayInformation(Grass.class, grassDi);

        DisplayInformation rabbitDi = new DisplayInformation(Color.blue, "rabbit-small");
        program.setDisplayInformation(Rabbit.class, rabbitDi);

        DisplayInformation rabbitHoleDi = new DisplayInformation(Color.yellow, "hole");
        program.setDisplayInformation(RabbitHole.class, rabbitHoleDi);

        DisplayInformation wolfDi = new DisplayInformation(Color.red, "wolf-small");
        program.setDisplayInformation(Wolf.class, wolfDi);

        DisplayInformation bearDi = new DisplayInformation(Color.cyan, "bear-small");
        program.setDisplayInformation(Bear.class, bearDi);

        DisplayInformation bushDi = new DisplayInformation(Color.pink, "bush");
        program.setDisplayInformation(BerryBush.class, bushDi);
    }

    public static void changeSprite(Object object, boolean grow) {
        //What the hell do i do here????
    }
}
