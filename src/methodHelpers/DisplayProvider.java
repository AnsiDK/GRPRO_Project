package methodHelpers;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import ourNonBlocking.*;

import java.awt.*;

public class DisplayProvider {

    private static Program program;

    /**
     * A class that provides static display information
     * @param program provides the program for which we provide the sprites for
     */
    public DisplayProvider(Program program) {
        this.program = program;
        initializeDisplays();
    }

    /**
     * Function that gets called once at the very start to initialize the sprites that does not change.
     */
    public static void initializeDisplays() {
        DisplayInformation grassDi = new DisplayInformation(Color.green, "grass");
        program.setDisplayInformation(Grass.class, grassDi);

        DisplayInformation rabbitHoleDi = new DisplayInformation(Color.yellow, "hole-small");
        program.setDisplayInformation(RabbitHole.class, rabbitHoleDi);

        DisplayInformation wolfDenDi = new DisplayInformation(Color.orange, "hole");
        program.setDisplayInformation(WolfDen.class, wolfDenDi);
    }
}
