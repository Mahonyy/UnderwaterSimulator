import java.util.Random;
import java.util.List;

/**
 * The animal class represents a concept of an organism that can move
 * and interact within the simulation in different ways depending on 
 * their needs.
 * 
 * @author Nicolás Alcalá Olea and Bailey Crossan
 */
public abstract class Animal implements Organism
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's position.
    private Location location;
    // The animal's gender.
    protected boolean isMale;
    // A Random for the animal's gender to be randomised.
    private Random random = new Random();
    // An instance of Time so they act different depending on what time it is.
    private static Time time = new Time();
    // A boolean which keeps track of whether the animal is diseased.
    protected boolean infected;
    // A string which holds the name of the animal in camel case.
    protected String name;

    /**
     * Constructor for objects of class Animal, where we declare that the
     * animal is alive, where it is going to spawn and give them a random 
     * gender.
     * 
     * @param location The animal's location.
     */
    public Animal(Location location)
    {
        this.alive = true;
        this.location = location;
        this.isMale = random.nextBoolean(); // Gender randomised.
        this.infected = false; 
    }

    /**
     * Makes the animal perform within the simulation. 
     * See more in the subclasses.
     * 
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     */
    abstract public void act(Field currentField, Field nextFieldState);

    /**
     * Check whether the animal is alive or not.
     * 
     * @return true If the animal is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     */
    protected void setDead()
    {
        alive = false;
        location = null;
    }

    /**
     * Return the animal's location.
     * 
     * @return The animal's location.
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Set the animal's location.
     * 
     * @param location The new location.
     */
    protected void setLocation(Location location)
    {
        this.location = location;
    }

    /**
     * Check wether the animal is a male or a female.
     * 
     * @return true If the animal is male, false if its a female.
     */
    public boolean getIsMale() 
    {
        return isMale;
    }

    /**
     * Ensures that the animal can breed with the others around them
     * if they are from the same species and have a different gender.
     * 
     * @param mate The animal who its going to breed with.
     * @return true If all the premises are met.
     */
    protected boolean canBreedWith(Animal mate) 
    {
        return mate != null && this.getClass().equals(mate.getClass()) && this.getIsMale() != mate.getIsMale();
    }

    /**
     * Go through every field around the animal checking if there is
     * an animal which it can breed with, if so breed.
     * 
     * @param field The field where the animal is currently at
     * @return A valid mate to breed with.
     */
    public Animal findBreedingMate(Field field) 
    {
        List<Location> adjacentFields = field.getAdjacentLocations(getLocation());
        for (Location loc : adjacentFields) {
            Animal animal = field.getAnimalAt(loc);
            if (animal != null && canBreedWith(animal) && animal.isAlive()) {
                return animal; // The first mate found.
            }
        }
        return null; // No valid mate found.
    }

    /**
     * Returns the current time.
     * 
     * @return The current time.
     */
    public static Time getTime()
    {
        return time;
    }

    /**
     * Infects the animal with the disease.
     */
    public void setInfected()
    {
        infected = true;
    }

    /**
     * Checks if the given animal can breed.
     * 
     * @return true If the animal can breed, false otherwise.
     */
    protected boolean canBreed(int age, int BREEDING_AGE)
    {
        return age >= BREEDING_AGE;
    }

    /**
     * Returns whether the animal is infected or not.
     * 
     * @return true If the animal is infected, false otherwise.
     */
    public boolean isInfected()
    {
        return infected;
    }

    /**
     * Gets the name of the given animal.
     * 
     * @return name of animal.
     */
    protected String getName(){
        return name;
    }

    /**
     * Check if the weather is foggy if so make the predators have
     * less of a chance to catch fish.
     * 
     * @return The predator efficiency when acting depending on the weather.
     */
    protected double getPredatorHuntingModifier() 
    {
        switch (Simulator.weatherManager.getCurrentWeather()) {
            case Weather.FOG:
                return 0.9;

            default: return 1;
        }
    }

    /**
     * Check if the weather is cold if so make the predators have
     * less of a chance to move.
     * 
     * @return The predator efficiency when acting depending on the weather.
     */
    protected double getPredatorMovingModifier() 
    {
        switch (Simulator.weatherManager.getCurrentWeather()) {  
            case Weather.COLD:
                return 0.8;

            default: return 1;
        }
    }

    /**
     * Check if the weather is foggy if so make the prey have
     * less of a chance to feed on the algae.
     * 
     * @return The prey efficiency when acting depending on the weather.
     */
    protected double getPreyFeedingModifier() 
    {
        switch (Simulator.weatherManager.getCurrentWeather()) {
            case Weather.FOG:
                return 0.95;

            default: return 1;
        }
    }

    /**
     * Check if the weather is cold if so make the prey have
     * less of a chance to move.
     * 
     * @return The prey efficiency when acting depending on the weather.
     */
    protected double getPreyMovingModifier() 
    {
        switch (Simulator.weatherManager.getCurrentWeather()) {
            case Weather.COLD:
                return 0.95;

            default: return 1;
        }
    }
}
