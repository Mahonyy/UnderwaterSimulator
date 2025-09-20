import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A model of a turtle they can breed, eat algae to survive, they also 
 * have a period in which they sleep and if the maximun age is reached or 
 * they dont eat what they are required they will die. They will also likely
 * die if the disease catches them, it can also be transmitted to their mate
 * or baby.
 * 
 * @author Nicolás Alcalá Olea and Bailey Crossan
 */
public class Turtle extends Animal
{
    // Characteristics shared by all turtle's (class variables).

    // The age in which a turtle can start breeding.
    private static final int BREEDING_AGE = 5;
    // The age to which a turtle can live.
    private static final int MAX_AGE = 50;
    // The likelihood of a turtle breeding.
    private static final double BREEDING_PROBABILITY = 0.3;
    // The likelihood of a turtlr catching the disease.
    private static final double INFECTION_PROBABILITY = 0.01;
    // The likelihood of a turtle transmitting the disease.
    private static final double TRANSMISSION_PROBABILITY = 0.02;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of an algae. Basically, the steps
    // they can go before they have to eat again.
    private static final int ALGAE_FOOD_VALUE = 30;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).

    // The turtle's age.
    private int age;
    // The turtle's food level, which is increased by eating algae.
    private int foodLevel;

    /**
     * Create a turtle. A turtle can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the turtle will have random age and hunger level.
     * @param location The location within the field.
     */
    public Turtle(boolean randomAge, Location location)
    {
        super(location);

        name = "turtle";

        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
        else {
            age = 0;
        }
        foodLevel = rand.nextInt(ALGAE_FOOD_VALUE);
    }

    /**
     * Defines the actions performed by the turtle during one simulation
     * step: it looks for its source of food and in the process, it might 
     * give birth, die of hunger, die of the disease or die of old age. They
     * are active during the day time.
     * 
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState)
    {
        incrementAge();
        if(isAlive()) {
            List<Location> freeLocations =
                nextFieldState.getFreeAdjacentLocations(getLocation());

            if(Time.isDay()) { // What they do if its day time.
                incrementHunger();

                if(!infected && rand.nextDouble() <= INFECTION_PROBABILITY) {
                    setInfected();
                }
                if(infected && rand.nextDouble() <= 0.2) {
                    setDead();
                }

                if(! freeLocations.isEmpty()) {
                    giveBirth(nextFieldState);
                }
                // Move towards a source of food if found.
                Location nextLocation = findFood(currentField);

                double movingModifier = getPreyMovingModifier();
                if(rand.nextDouble() <= movingModifier){
                    if(nextLocation == null && ! freeLocations.isEmpty()) {
                        // No food found - try to move to a free location.
                        nextLocation = freeLocations.remove(0);
                    }
                    // See if it was possible to move.
                    if(nextLocation != null) {
                        setLocation(nextLocation);
                        nextFieldState.placeAnimal(this, nextLocation);
                    }
                    else {
                        // Overcrowding.
                        setDead();
                    }
                }
            }
            else {
                nextFieldState.placeAnimal(this, getLocation());// Sleep if its night time.
                if(infected && rand.nextDouble() <= 0.1) {
                    setDead();
                }
            }
        }
    }

    @Override
    public String toString() 
    {
        return "Turtle{" +
        "age=" + age +
        ", alive=" + isAlive() +
        ", location=" + getLocation() +
        ", foodLevel=" + foodLevel +
        '}';
    }

    /**
     * Increase the age. This could result in the turtle's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this turtle more hungry. This could result in the turtles's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for algae adjacent to the current location.
     * Only the first algae is eaten.
     * Weather could alter this behaviour.
     * 
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(Field field)
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;

        double feedingModifier = getPreyFeedingModifier();
        while(foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            Plant plant = field.getPlantAt(loc);
            if(plant != null && plant.getName().equals("algae") && plant.isAlive()) {
                if(rand.nextDouble() <= feedingModifier){
                    plant.setDead();
                    foodLevel = ALGAE_FOOD_VALUE;
                    foodLocation = loc;
                }
            }
        }
        return foodLocation;
    }

    /**
     * Give birth to a new turtle that spawns in the first free 
     * location around their parent. When mating if one of the parents has the disease
     * there is a chance that it transmitts the disease to the other mate. And
     * if both have the disease, their baby will also have the disease.
     * 
     * @param nextFieldState Where the new turtle is going to be added.
     */
    public void giveBirth(Field nextFieldState) 
    {
        Animal mate = findBreedingMate(nextFieldState);
        if (mate != null) {
            if (this.isInfected() && !mate.isInfected()) {
                if (rand.nextDouble() <= TRANSMISSION_PROBABILITY) {
                    mate.setInfected();
                }
            } else if (!this.isInfected() && mate.isInfected()) {
                if (rand.nextDouble() <= TRANSMISSION_PROBABILITY) {
                    this.setInfected();
                }
            }
            int births = breed();
            List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(this.getLocation());
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Turtle young = new Turtle(false, loc);
                double INHERIT_PROBABILITY = 0.01;
                if(mate.isInfected() || this.isInfected()) {
                    if (rand.nextDouble() <= INHERIT_PROBABILITY) {
                        young.setInfected();
                    }
                }
                nextFieldState.placeAnimal(young, loc);
            }
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * 
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births;
        if(canBreed(age, BREEDING_AGE) && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }

}
