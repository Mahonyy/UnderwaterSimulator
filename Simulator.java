import java.util.*;

/**
 * The simulator class manages the execution of the simulation, coordinating
 * the environment and organisms.
 * 
 * @author Nicolás Alcalá Olea and Bailey Crossan
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.

    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;

    // The probability that a swordfish will be created in any given grid position.
    private static final double SWORDFISH_CREATION_PROBABILITY = 0.01;

    // The probability that a trutle will be created in any given position.
    private static final double TURTLE_CREATION_PROBABILITY = 0.14;

    // The probability that a parrotfish will be created in any given position.
    private static final double PARROTFISH_CREATION_PROBABILITY = 0.145;

    // The probability that a clownfish will be created in any given position.
    private static final double CLOWNFISH_CREATION_PROBABILITY = 0.145;

    // The probability that a white shark will be created in any given position.
    private static final double WHITESHARK_CREATION_PROBABILITY = 0.03;

    // The probability that a killer whale will be created in any given position.
    private static final double KILLERWHALE_CREATION_PROBABILITY = 0.008;

    // The probability that a algae will be created in any given position.
    private static final double ALGAE_CREATION_PROBABILITY = 0.5;

    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private final SimulatorView view;

    // An instance of time to keep track of it in the simulator.
    private static Time time = new Time();

    // An instance of WeatherManager for the simulation to have a view of it.
    public static final WeatherManager weatherManager = new WeatherManager();

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * 
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be >= zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        view = new SimulatorView(depth, width);
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long 
     * period (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(700);
    }

    /**
     * Run the simulation for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * 
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        reportStats();
        for(int n = 1; n <= numSteps && field.isViable(); n++) {
            simulateOneStep();
            delay(50);         // Adjust this to change execution speed
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of every organism,
     * the weather and making the time pass.
     */
    public void simulateOneStep()
    {
        time.increment();
        weatherManager.update(1.0);
        step++;
        // Use a separate Field to store the starting state of
        // the next step.
        Field nextFieldState = new Field(field.getDepth(), field.getWidth());

        List<Animal> animals = field.getAnimals();
        for (Animal anAnimal : animals) {
            anAnimal.act(field, nextFieldState);
        }

        List<Plant> plants = field.getPlants();
        for (Plant plant : plants) {
            plant.act(field, nextFieldState);
        }

        // Replace the old state with the new one.
        field = nextFieldState;

        reportStats();
        view.showStatus(step, field);
    }

    /**
     * Reset the simulation to a starting position and time.
     */
    public void reset()
    {
        time = new Time();
        field = new Field(DEFAULT_DEPTH, DEFAULT_WIDTH);
        step = 0;
        populate();
        view.showStatus(step, field);
    }

    /**
     * Randomly populate the field with organisms.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= SWORDFISH_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Swordfish swordfish = new Swordfish(true, location);
                    field.placeAnimal(swordfish, location);
                }
                else if(rand.nextDouble() <= TURTLE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Turtle turtle = new Turtle(true, location);
                    field.placeAnimal(turtle, location);
                }
                else if(rand.nextDouble() <= PARROTFISH_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Parrotfish parrotfish = new Parrotfish(true, location);
                    field.placeAnimal(parrotfish, location);
                }
                else if(rand.nextDouble() <= WHITESHARK_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    WhiteShark whiteShark = new WhiteShark(true, location);
                    field.placeAnimal(whiteShark, location);
                }
                else if(rand.nextDouble() <= KILLERWHALE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    KillerWhale killerwhale = new KillerWhale(true, location);
                    field.placeAnimal(killerwhale, location);
                }
                else if(rand.nextDouble() <= CLOWNFISH_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Clownfish clownfish = new Clownfish(true, location);
                    field.placeAnimal(clownfish, location);
                }
                else if(rand.nextDouble() <= ALGAE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Algae algae = new Algae(true, location);
                    field.placePlant(algae, location);
                }
                // Else leave the location empty.
            }
        }
    }

    /**
     * Report on the number of each type of animal in the field.
     */
    public void reportStats()
    {
        //System.out.print("Step: " + step + " ");
        field.fieldStats();
    }

    /**
     * Pause for a given time.
     * 
     * @param milliseconds The time to pause for, in milliseconds
     */
    private void delay(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            // ignore
        }
    }
}
