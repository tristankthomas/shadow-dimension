import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * HealthBar.java: Class representing the health bar for all characters
 *
 * @author Tristan Thomas
 */
public class HealthBar {
    private final static int LOW_HEALTH_PERC = 35;
    private final static int MEDIUM_HEALTH_PERC = 65;
    private final static Colour GREEN = new Colour(0,0.8,0.2);
    private final static Colour ORANGE = new Colour(0.9,0.6,0);
    private final static Colour RED = new Colour(1,0,0);
    private Colour healthColour = GREEN;
    private final static String DISPLAY_MESSAGE = "%d%%";
    private int fontSize;
    private Font healthFont;

    private DrawOptions healthOptions = new DrawOptions();

    private double xCoord;
    private double yCoord;

    /**
     * Constructor to initialise font size and location
     * @param x
     * @param y
     * @param fontSize
     */

    public HealthBar(double x, double y, int fontSize) {
        this.fontSize = fontSize;
        healthFont = new Font("res/frostbite.ttf", fontSize);
        xCoord = x;
        yCoord = y;
    }

    /* Getters and setters */
    public void setXCoord(double xCoord) {
        this.xCoord = xCoord;
    }

    public void setYCoord(double yCoord) {
        this.yCoord = yCoord;
    }

    private void setColour() {
        healthOptions.setBlendColour(healthColour);
    }

    /**
     * Draws the health bar
     * @param character
     */
    public void drawHealth(Character character) {
        setColour();
        healthFont.drawString(String.format(DISPLAY_MESSAGE, character.getHealthPercentage()), xCoord, yCoord,
                healthOptions);
    }

    /**
     * Updates the health bar colour depending on character health
     * @param character
     */
    public void updateColour(Character character) {
        /* changes health percentage colour */
        if (character.getHealthPercentage() < LOW_HEALTH_PERC) {
            /* change colour to red when health is low */
            healthColour = RED;

        } else if (character.getHealthPercentage() < MEDIUM_HEALTH_PERC) {
            /* change colour to orange when medium health */
            healthColour = ORANGE;
        }
    }

}
