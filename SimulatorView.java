import java.awt.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.lang.Math;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a blue background 
 * color, representing the ocean, it becomes darker at
 * night time.
 * Colors for each type of species can be defined using the
 * setColor method. If it becomes infected it will change to
 * a white color.
 * 
 * @author Nicolás Alcalá Olea and Bailey Crossan
 */
public class SimulatorView extends JFrame
{
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = new Color(51, 204, 255);

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private final String WEATHER_PREFIX = "Weather: ";
    private final JLabel stepLabel;
    private final JLabel weatherLabel;
    private final JLabel population;
    private final FieldView fieldView;

    // A map for storing colors for participants in the simulation
    private final Map<String, Color> colors;
    // A statistics object computing and storing simulation information
    private final FieldStats stats;

    /**
     * Create a view of the given width and height.
     * 
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(int height, int width)
    {
        stats = new FieldStats();
        colors = new LinkedHashMap<>();
        setColor("clownFish", Color.orange);
        setColor("swordFish", Color.blue);
        setColor("parrotFish", Color.yellow);
        setColor("whiteShark", Color.gray);
        setColor("killerWhale", Color.black);
        setColor("turtle", Color.green);
        setColor("algae", new Color(0, 153, 0));

        setTitle("Underwater Simulation");
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        JPanel northPanel = new JPanel(new BorderLayout());

        stepLabel = new JLabel(STEP_PREFIX);
        weatherLabel = new JLabel(WEATHER_PREFIX);

        stepLabel.setHorizontalAlignment(SwingConstants.LEFT);
        weatherLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        northPanel.add(stepLabel, BorderLayout.WEST);
        northPanel.add(weatherLabel, BorderLayout.EAST);

        setLocation(100, 50);

        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        contents.add(northPanel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    /**
     * Define a color to be used for a given class of animal.
     * 
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(String name, Color color)
    {
        colors.put(name, color);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(String animalName)
    {
        Color col = colors.get(animalName);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     * 
     * @param step Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus(int step, Field field)
    {
        if (!isVisible()) {
            setVisible(true);
        } 

        stepLabel.setText(STEP_PREFIX + step);
        weatherLabel.setText(WEATHER_PREFIX + Simulator.weatherManager.getCurrentWeather());
        stats.reset();
        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) { 
                Animal animal = field.getAnimalAt(new Location(row, col)); 
                Plant plant = field.getPlantAt(new Location(row, col)); 

                if (animal != null) { 
                    stats.incrementCount(animal.getClass());

                    if (animal.isInfected()) { 
                        fieldView.drawMark(col, row, Color.white);
                    } else { 
                        fieldView.drawMark(col, row, getColor(animal.getName())); 
                    }
                } else if (plant != null) { 
                    stats.incrementCount(plant.getClass());
                    fieldView.drawMark(col, row, getColor(plant.getName()));
                } else { 
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                } 
            }
        }

        stats.countFinished();
        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     * 
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        return stats.isViable(field);
    }

    /**
     * Provide a graphical view of a rectangular field. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class FieldView extends JPanel
    {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private final int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Graphics g2;
        private Image fieldImage;
        private Image backdrop;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                backdrop = fieldView.createImage(size.width, size.height);

                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {

                    g.drawImage(fieldImage, 0, 0, null);
                    //new Color(255, 255, 255, 0.5f)
                    if(!Time.isDay()){
                        g.setColor(new Color(0,0,0,100)); // Becomes darker
                    }
                    else{
                        g.setColor(new Color(0,0,0,0));
                    }

                    g.fillRect(0,0,size.width,size.height);

                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
