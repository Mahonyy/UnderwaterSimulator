import java.util.List;

/**
 * The plant class represents a plant-based organism in the simulation.
 * 
 * @author Nicolás Alcalá Olea and Bailey Crossan
 */
public abstract class Plant implements Organism
{
    // Whether the plant is alive or not.
    private boolean alive;
    // The plant's position.
    private Location location;
    // The name of the plant
    protected String name;

    /**
     * Constructor for objects of class Plant, where we declare that the
     * plant is alive and where it is going to spawn.
     * 
     * @param location The plant's location.
     */
    public Plant(Location location)
    {
        this.alive = true;
        this.location = location;
    }

    /**
     * Makes the plant perform within the simulation. 
     * See more in the subclass.
     * 
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     */
    abstract public void act(Field currentField, Field nextFieldState);

    /**
     * Check whether the plant is alive or not.
     * 
     * @return true if the plant is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the plant is no longer alive.
     */
    protected void setDead()
    {
        alive = false;
        location = null;
    }

    /**
     * Return the plant's location.
     * 
     * @return The plant's location.
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Set the plant's location.
     * 
     * @param location The new location.
     */
    protected void setLocation(Location location)
    {
        this.location = location;
    }

    /**
     * Gets the name of the given animal.
     * 
     * @return name of animal.
     */
    public String getName(){
        return name;
    }

    /**
     * Check if the weather is foggy if so make the algae grow
     * less as there is less sunlight hitting them, if it is cold
     * they also grow less.
     * 
     * return The algae growth rate.
     */
    protected double getPlantGrowthModifier() {
        switch (Simulator.weatherManager.getCurrentWeather()) {
            case Weather.FOG:
                return 0.9;

            case Weather.COLD:
                return 0.8;

            default: return 1;
        }
    }
}
