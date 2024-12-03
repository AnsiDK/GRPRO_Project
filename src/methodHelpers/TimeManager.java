package methodHelpers;

import ourActors.Animal;

public class TimeManager {
    private boolean isDay;
    Animal animal;

    public TimeManager(Animal animal) {
        super();
        this.animal = animal;
    }

    public void updateTime(boolean dayOrNight) {
        //Switch so that we only do stuff when it turns night or day
        if (isDay != dayOrNight) {
            isDay = dayOrNight;
            if (!isDay) {
                checkHunger();
                if (animal.isOnMap()) {
                    animal.findHome();
                }
            }
            if (isDay) {
                checkReproduce();
                animal.setFoodEaten(0);

            }
        }
    }

    public void checkHunger() {
        if (animal.getFoodEaten() == 0) { animal.die(); }
    }

    public void checkReproduce() {
        if (animal.getFoodEaten() > 2 && animal.getHasGrown()) { animal.reproduce(); };
    }
}
