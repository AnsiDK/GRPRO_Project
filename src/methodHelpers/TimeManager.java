package methodHelpers;

import ourActors.Animal;

public class TimeManager {
    private boolean isDay;
    Animal animal;

    public TimeManager(Animal animal) {
        super();
        this.animal = animal;
    }

    /**
     * A method that triggers certain events once it turns day or night
     * @param dayOrNight provides a boolean that is true during day and false during night
     */
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
                animal.setFoodEaten(0);
                checkReproduce();
            }
        }
    }

    /**
     * A method that makes sure to kill animals if they do not have enough enerfy
     */
    public void checkHunger() {
        if (animal.getEnergy() == 0) { animal.die(); }
    }

    /**
     * A method that checks whether an animal can reproduce
     */
    public void checkReproduce() {
        if (animal.getFoodEaten() > 2 && animal.getHasGrown()) { animal.reproduce(); };
    }
}
