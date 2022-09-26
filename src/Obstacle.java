import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Obstacle {
    protected Point coords;
    protected Rectangle boundary;

    public Obstacle(Point coords) {
        this.coords = coords;
    }
    public abstract void drawObstacle();

    public Rectangle getRect() {
        return boundary;
    }
}
