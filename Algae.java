import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * The algae class represents a plant organism in the simulation.
 * Algae reproduce and serve as a food source for herbivorous species.
 * 
 * @author Nicolás Alcalá Olea and Bailey Crossan
 */
public class Algae extends Plant
{
    // Characteristics shared by all algae (class variables).

    // The stage at which an algae can start to reproduce.
    private static final int GROWTH_AGE = 1;
    // The age to which an algae can last.
    private static final int MAX_AGE = 10;
    // The likelihood of an algae reproducing.
    private static final double GROWTH_PROBABILITY = 0.9;
    // The maximum number of algae fragments that an algae can drop.
    private static final int MAX_LITTER_SIZE = 7;

    // A shared random number generator to control reproduction.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).

    // The algae's age.
    private int age;

    /**
     * Create an algae. An algae can be created as a new plant (age zero)
     * or with a random age.
     * 
     * @param randomAge If true, the algae will have random age.
     * @param location The location within the field.
     */
    public Algae(boolean randomAge, Location location)
    {
        super(location);

        name = "algae";

        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
        else {
            age = 0;
        }
    }

    /**
     * Defines the actions performed by the algae during one simulation
     * step: they might reproduce and die of old age. They are active 
     * during the whole day.
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
            if(!freeLocations.isEmpty()) {
                grow(nextFieldState);
            }
        }
    }

    @Override
    public String toString() {
        return "Algae{" +
        "age=" + age +
        ", alive=" + isAlive() +
        ", location=" + getLocation() +
        '}';
    }

    /**
     * Increase the age. This could result in the white shark's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Look for rabbits and deers adjacent to the current location.
     * Only the first live rabbit or deer is eaten.
     * 
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */

    public void grow(Field nextFieldState) {
        double growthModifier = getPlantGrowthModifier();

        int algaes = (int) (grow() * growthModifier);
        List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(this.getLocation());
        for (int b = 0; b < algaes && !freeLocations.isEmpty(); b++) {
            Location loc = freeLocations.remove(0);
            Algae algae = new Algae(false, loc);
            nextFieldState.placePlant(algae, loc);
        }
    }

    /**
     * Generate a number representing the number of new algae,
     * if it can grow.
     * 
     * @return The number of births (may be zero).
     */
    private int grow()
    {
        int algae;
        if(canGrow() && rand.nextDouble() <= GROWTH_PROBABILITY) {
            algae = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        else {
            algae = 0;
        }
        return algae;
    }

    /**
     * A plant can reproduce if it has reached the reproducing age.
     * 
     * @return true If they can start reproducing.
     */
    private boolean canGrow()
    {
        return age >= GROWTH_AGE;
    }
}
