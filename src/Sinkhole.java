// Class representing a sinkhole
import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Sinkhole {
    private final static int DAMAGE_POINTS = 30;
    private final Image sinkholeImage = new Image("res/sinkhole.png");
    private Point coords;
    private Rectangle rect;


    public Sinkhole(Point coords) {
        this.coords = coords;
        rect = new Rectangle(coords, sinkholeImage.getWidth(), sinkholeImage.getHeight());
    }

    // Getters
    public Rectangle getRect() {
        return rect;
    }

    public static int getDamagePoints() {
        return DAMAGE_POINTS;
    }

    // Draws a sinkhole
    public void drawSinkhole() {
        sinkholeImage.drawFromTopLeft(coords.x, coords.y);
    }
}
