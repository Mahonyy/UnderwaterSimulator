import java.util.Random;

/**
 * The weather manager class manages the different types of weather
 * phenomenons and how long will they last within the simulation, it
 * also manages how will these affect the animals and plants.
 * 
 * @author Nicolás Alcalá Olea and Bailey Crossan
 */
public class WeatherManager {
    private Weather currentWeather;
    private double timeRemaining; // The simulation steps remaining in the current weather.
    private Random random;

    /**
     * Weather manager constructor, where we make the simulation start
     * with a clear weather and give it a random time duration.
     */
    public WeatherManager() 
    {
        random = new Random();
        currentWeather = Weather.CLEAR;
        timeRemaining = randomDuration();
    }

    /**
     * Updates the time remaining counter until it reaches 0, then
     * it will change the weather.
     */
    public void update(double oneStep) 
    {
        timeRemaining -= oneStep;
        if (timeRemaining <= 0) {
            changeWeather();
        }
    }

    /**
     * Pick randomly one of the different weather phenomenons.
     */
    private void changeWeather() 
    {
        Weather[] types = Weather.values();
        currentWeather = types[random.nextInt(types.length)];
        timeRemaining = randomDuration();
    }

    /**
     * Generate a number which will be for how long the simulation
     * will have that weather phenomenon.
     * 
     * @return A random integer starting from 20 up to 40.
     */
    private double randomDuration() 
    {
        return 20 + random.nextInt(21);
    }

    /**
     * Return the current weather.
     * 
     * @return The current weather.
     */
    public Weather getCurrentWeather() 
    {
        return currentWeather;
    }
}
