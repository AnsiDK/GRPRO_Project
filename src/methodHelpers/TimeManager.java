package methodHelpers;

import itumulator.simulator.Rabbit;
import itumulator.world.World;

public class TimeManager {
    private boolean isDay;
    Rabbit rabbit;

    public TimeManager(Rabbit rabbit) {
        super();
        this.rabbit = rabbit;
    }

    public void updateTime(boolean dayOrNight) {
        //Switch so that we only do stuff when it turns night or day
        if (isDay != dayOrNight) {
            isDay = dayOrNight;
            if (!isDay) {
                checkHunger();
                if (rabbit.isOnMap()) {
                    rabbit.findHole();
                }
            }
            if (isDay) {
                checkReproduce();
                rabbit.setEatenGrass(0);

            }
        }
    }

    public void checkHunger() {
        if (rabbit.getEatenGrass() == 0) { rabbit.die(); }
    }

    public void checkReproduce() {
        if (rabbit.getEatenGrass() > 2 && rabbit.hasGrown()) { rabbit.reproduce(); };
    }
}
