import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;

public class HealthBar {
    private final static int LOW_HEALTH_PERC = 35;
    private final static int MEDIUM_HEALTH_PERC = 65;
    private final static Colour GREEN = new Colour(0,0.8,0.2);
    private final static Colour ORANGE = new Colour(0.9,0.6,0);
    private final static Colour RED = new Colour(1,0,0);
    private Colour healthColour = GREEN;
    private final static int HEALTH_FONT_SIZE = 30;
    private final Font HEALTH_FONT = new Font("res/frostbite.ttf", HEALTH_FONT_SIZE);

    private DrawOptions healthOptions = new DrawOptions();

    private int xCoord;
    private int yCoord;

    public HealthBar(int x, int y) {
        xCoord = x;
        yCoord = y;
    }
    // change to character
    public void drawHealth(Player fae) {
        HEALTH_FONT.drawString(String.format("%d%%", fae.getHealthPercentage()), xCoord, yCoord,
                healthOptions);
    }

    public void updateColour(Player fae) {
        /* changes health percentage colour */
        if (fae.getHealthPercentage() < LOW_HEALTH_PERC) {
            /* change colour to red when health is low */
            healthColour = RED;

        } else if (fae.getHealthPercentage() < MEDIUM_HEALTH_PERC) {
            /* change colour to orange when medium health */
            healthColour = ORANGE;
        }
    }

    public void setColour() {
        healthOptions.setBlendColour(healthColour);
    }

}
