/**
 * The time class holds the time logic within the simulation, so
 * the organisms can differ day and night.
 *
 * @author Nicolás Alcalá Olea and Bailey Crossan
 */
public class Time
{
    // The time in which day time ends. (20PM)
    private static final int DAY_TIME = 20;
    // The time in which night time ends. (5AM)
    private static final int NIGHT_TIME = 5;

    private static int hour;
    private static int minute;
    /**
     * We set the starting time of the simlation at 12PM.
     */
    public Time()
    {
        hour = 6;
        minute = 0;
    }

    /**
     * @return The hour the simulation is currently at.
     */
    public int getHour()
    {
        return hour;
    }

    /**
     * @return The minute the simulation is currently at.
     */
    public int getMinute()
    {
        return minute;
    }

    /**
     * Increments the time by 20 minutes. If the minutes reach 60, they reset to 0 
     * and the hour is incremented. The hour wraps around after reaching 24.
     */
    public void increment()
    {
        minute = (minute + 20) % 60;
        if(minute == 0) {
            hour = (hour + 1) % 24;
        }
    }

    /**
     * Checking wether its day or night time.
     * 
     * @return true If its day time, false otherwise.
     */
    public static boolean isDay()
    {
        return hour < DAY_TIME && hour > NIGHT_TIME;
    }
}