import java.util.*;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a single organism/object.
 * 
 * @author Nicolás Alcalá Olea and Bailey Crossan
 */
public class Field
{
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();

    // The dimensions of the field.
    private final int depth, width;
    // Organisms mapped by location.
    private final Map<Location, Organism> field = new HashMap<>();
    // The animals.
    private final List<Animal> animals = new ArrayList<>();
    // The plants.
    private final List<Plant> plants = new ArrayList<>();

    /**
     * Represent a field of the given dimensions.
     * 
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width)
    {
        this.depth = depth;
        this.width = width;
    }

    /**
     * Place an animal at the given location.
     * If there is already an animal at the location it will
     * be lost.
     * 
     * @param anAnimal The animal to be placed.
     * @param location Where to place the animal.
     */
    public void placeAnimal(Animal anAnimal, Location location)
    {
        assert location != null;
        Object other = field.get(location);
        if(other != null && other instanceof Animal) {
            animals.remove(other);
        }
        else if(other != null && other instanceof Plant) {
            plants.remove(other);
        }
        field.put(location, anAnimal);
        animals.add(anAnimal);
    }

    /**
     * Place a plant at the given location.
     * If there is already an plant at the location it will
     * be lost.
     * 
     * @param plant The Plant to be placed.
     * @param location Where to place the plant.
     */
    public void placePlant(Plant plant, Location location)
    {
        assert location != null;
        Object other = field.get(location);
        if(other != null && other instanceof Animal) {
            animals.remove(other);
        }
        else if(other != null && other instanceof Plant) {
            plants.remove(other);
        }
        field.put(location, plant);
        plants.add(plant);
    }

    /**
     * Return the animal at the given location, if any.
     * 
     * @param location Where in the field.
     * @return The animal at the given location, or null if there is none.
     */
    public Animal getAnimalAt(Location location)
    {
        Organism organism = field.get(location);

        if(organism instanceof Animal){
            return (Animal)organism;
        }
        else{
            return null;
        }
    }

    /**
     * Return the plant at the given location, if any.
     * 
     * @param location Where in the field.
     * @return The plant at the given location, or null if there is none.
     */
    public Plant getPlantAt(Location location)
    {
        Organism organism = field.get(location);

        if(organism instanceof Plant){
            return (Plant)organism;
        }
        else{
            return null;
        }
    }

    /**
     * Get a shuffled list of the free adjacent locations.
     * 
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location)
    {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = getAdjacentLocations(location);
        for(Location next : adjacent) {
            Organism organism = field.get(next);
            if(organism instanceof Animal) {
                Animal anAnimal = (Animal) organism;
                if(anAnimal == null) {
                    free.add(next);
                }
                else if(!anAnimal.isAlive()) {
                    free.add(next);
                }
            }
            else {
                Plant plant = (Plant) organism;
                if(plant == null) {
                    free.add(next);
                }
                else if(!plant.isAlive()) {
                    free.add(next);
                }
            }
        }
        return free;
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * 
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> getAdjacentLocations(Location location)
    {
        // The list of locations to be returned.
        List<Location> locations = new ArrayList<>();
        if(location != null) {
            int row = location.row();
            int col = location.col();
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }

            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Print out the number of all the organisms in the field.
     */
    public void fieldStats()
    {
        int numSwordFish = 0, numTurtle = 0, numParrotfish = 0, numWhiteSharks = 0, numKillerWhales = 0, numClownfish = 0, numAlgae = 0;
        for(Organism organism : field.values()) {
            if(organism instanceof Animal) {
                Animal anAnimal = (Animal)organism;
                if(anAnimal instanceof Turtle) {
                    if(anAnimal.isAlive()) {
                        numTurtle++;
                    }   
                }
                else if(anAnimal instanceof Swordfish) {
                    if(anAnimal.isAlive()) {
                        numSwordFish++;
                    }
                }
                else if(anAnimal instanceof Parrotfish) {
                    if(anAnimal.isAlive()) {
                        numParrotfish++;
                    }
                }
                else if(anAnimal instanceof WhiteShark) {
                    if(anAnimal.isAlive()) {
                        numWhiteSharks++;
                    }
                }
                else if(anAnimal instanceof Clownfish) {
                    if(anAnimal.isAlive()) {
                        numClownfish++;
                    }
                }
                else if(anAnimal instanceof KillerWhale) {
                    if(anAnimal.isAlive()) {
                        numKillerWhales++;
                    }
                }
            }
            else if(organism instanceof Plant){
                Plant plant = (Plant)organism;
                if(plant instanceof Algae) {
                    if(plant.isAlive()) {
                        numAlgae++;
                    }
                }
            }
        }
        System.out.println("Turtle: " + numTurtle +
            " White shark: " + numWhiteSharks +
            " Parrotfish: " + numParrotfish +
            " Killer whale: " + numWhiteSharks +
            " Clownfish: " + numClownfish +
            " Swordfish: " + numSwordFish +
            " Algae: " + numAlgae);
    }

    /**
     * Empty the field.
     */
    public void clear()
    {
        field.clear();
    }

    /**
     * Return whether there is at least one organism in the field.
     * 
     * @return true If there is at least one organism in the field.
     */
    public boolean isViable()
    {
        boolean turtleFound = false;
        boolean swordfishFound = false;
        boolean parrotfishFound = false;
        boolean whiteSharkFound = false;
        boolean clownfishFound = false;
        boolean killerWhaleFound = false;
        boolean algaeFound = false;

        Iterator<Animal> itA = animals.iterator();
        while(itA.hasNext() && ! (turtleFound && swordfishFound && parrotfishFound && whiteSharkFound && clownfishFound && killerWhaleFound)) {
            Animal anAnimal = itA.next();
            if(anAnimal instanceof Turtle) {
                if(anAnimal.isAlive()) {
                    turtleFound = true;
                }
            }
            else if(anAnimal instanceof Swordfish) {
                if(anAnimal.isAlive()) {
                    swordfishFound = true;
                }
            }
            else if(anAnimal instanceof Parrotfish) {
                if(anAnimal.isAlive()) {
                    parrotfishFound = true;
                }
            }
            else if(anAnimal instanceof WhiteShark) {
                if(anAnimal.isAlive()) {
                    whiteSharkFound = true;
                }
            }
            else if(anAnimal instanceof Clownfish) {
                if(anAnimal.isAlive()) {
                    clownfishFound = true;
                }
            }
            else if(anAnimal instanceof KillerWhale) {
                if(anAnimal.isAlive()) {
                    killerWhaleFound = true;
                }
            }
        }

        Iterator<Plant> itP = plants.iterator();
        while(itP.hasNext() && ! (algaeFound)){
            Plant plant = itP.next();
            if(plant instanceof Algae) {
                if(plant.isAlive()) {
                    algaeFound = true;
                }
            }
        }
        return turtleFound && swordfishFound && parrotfishFound && whiteSharkFound && clownfishFound && killerWhaleFound && algaeFound;
    }

    /**
     * Get the list of animals.
     */
    public List<Animal> getAnimals()
    {
        return animals;
    }

    /**
     * Get the list of plants.
     */
    public List<Plant> getPlants()
    {
        return plants;
    }

    /**
     * Return the depth of the field.
     * 
     * @return The depth of the field.
     */
    public int getDepth()
    {
        return depth;
    }

    /**
     * Return the width of the field.
     * 
     * @return The width of the field.
     */
    public int getWidth()
    {
        return width;
    }
}

